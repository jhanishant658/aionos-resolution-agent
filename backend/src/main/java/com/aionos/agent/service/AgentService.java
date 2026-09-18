package com.aionos.agent.service;

import com.aionos.agent.dto.AgentMessageRequest;
import com.aionos.agent.dto.AgentResponse;
import com.aionos.agent.entity.*;
import com.aionos.agent.enums.IntentType;
import com.aionos.agent.repository.BookingRepository;
import com.aionos.agent.repository.ConversationRepository;
import com.aionos.agent.repository.CustomerRepository;
import com.aionos.agent.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final IntentService intentService;
    private final DecisionEngine decisionEngine;
    private final AIService aiService;

    public AgentService(CustomerRepository customerRepository, BookingRepository bookingRepository,
                        ConversationRepository conversationRepository, MessageRepository messageRepository,
                        IntentService intentService, DecisionEngine decisionEngine, AIService aiService) {
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.intentService = intentService;
        this.decisionEngine = decisionEngine;
        this.aiService = aiService;
    }

    @Transactional
    public AgentResponse processMessage(AgentMessageRequest request) {
        String rawMessage = request.getMessage() != null ? request.getMessage().trim() : "";
        String pnr = request.getPnr() != null ? request.getPnr().trim().toUpperCase() : null;

        if (pnr == null || pnr.isEmpty()) {
            pnr = extractPnrFromMessage(rawMessage);
        }

        if (pnr == null || pnr.isEmpty()) {
            return handleMissingPnr(rawMessage, request.getConversationId());
        }

        Optional<Booking> bookingOpt = bookingRepository.findFirstByPnr(pnr);
        if (bookingOpt.isEmpty()) {
            return handleInvalidPnr(pnr, rawMessage, request.getConversationId());
        }

        Booking booking = bookingOpt.get();
        Customer customer = booking.getCustomer();

        Conversation conversation = getOrCreateConversation(request.getConversationId(), pnr, customer.getName());

        List<IntentType> detectedIntents = intentService.detectIntents(rawMessage);

        Message customerMsg = Message.builder()
                .conversation(conversation)
                .sender("CUSTOMER")
                .content(rawMessage)
                .timestamp(LocalDateTime.now())
                .detectedIntent(detectedIntents.isEmpty() ? "GENERAL" : detectedIntents.get(0).name())
                .build();
        messageRepository.save(customerMsg);

        DecisionEngine.DecisionOutcome outcome = decisionEngine.process(
                conversation.getId(), customer, booking, detectedIntents, rawMessage);

        String agentMsgContent = aiService.generateResponse(
                customer, booking, detectedIntents,
                outcome.getPolicyDecisions(), outcome.getExecutedActions(),
                outcome.isEscalated(), outcome.getEscalationReason(), rawMessage);

        Message agentMsg = Message.builder()
                .conversation(conversation)
                .sender("AGENT")
                .content(agentMsgContent)
                .timestamp(LocalDateTime.now())
                .detectedIntent(detectedIntents.isEmpty() ? "RESPONSE" : detectedIntents.get(0).name())
                .build();
        messageRepository.save(agentMsg);

        conversation.setUpdatedAt(LocalDateTime.now());
        if (outcome.isEscalated()) {
            conversation.setStatus("ESCALATED");
        } else {
            conversation.setStatus("ACTIVE");
        }
        conversationRepository.save(conversation);

        Flight flight = booking.getFlight();

        return AgentResponse.builder()
                .conversationId(conversation.getId())
                .customerName(customer.getName())
                .loyaltyTier(customer.getLoyaltyTier().name())
                .pnr(booking.getPnr())
                .flightNumber(flight.getFlightNumber())
                .flightStatus(flight.getStatus().name())
                .route(flight.getRouteFrom() + " -> " + flight.getRouteTo())
                .detectedIntents(detectedIntents)
                .agentMessage(agentMsgContent)
                .policyDecisions(outcome.getPolicyDecisions())
                .executedActions(outcome.getExecutedActions())
                .escalated(outcome.isEscalated())
                .escalationReason(outcome.getEscalationReason())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Conversation getOrCreateConversation(Long conversationId, String pnr, String customerName) {
        if (conversationId != null) {
            Optional<Conversation> existing = conversationRepository.findById(conversationId);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Conversation newConv = Conversation.builder()
                .pnr(pnr)
                .customerName(customerName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status("ACTIVE")
                .build();
        return conversationRepository.save(newConv);
    }

    private AgentResponse handleMissingPnr(String message, Long conversationId) {
        Conversation conv = getOrCreateConversation(conversationId, "UNKNOWN", "Guest");
        String reply = "Welcome to AIONOS Airline Customer Support. To look up your flight and assist you with policy entitlements, could you please provide your 6-character Booking Reference (PNR)?";

        Message agentMsg = Message.builder()
                .conversation(conv)
                .sender("AGENT")
                .content(reply)
                .timestamp(LocalDateTime.now())
                .detectedIntent("ASK_PNR")
                .build();
        messageRepository.save(agentMsg);

        return AgentResponse.builder()
                .conversationId(conv.getId())
                .customerName("Guest")
                .loyaltyTier("NONE")
                .pnr(null)
                .detectedIntents(Collections.singletonList(IntentType.BOOKING_STATUS))
                .agentMessage(reply)
                .policyDecisions(Collections.emptyList())
                .executedActions(Collections.emptyList())
                .escalated(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private AgentResponse handleInvalidPnr(String pnr, String message, Long conversationId) {
        Conversation conv = getOrCreateConversation(conversationId, pnr, "Guest");
        String reply = "I was unable to locate a booking with reference \"" + pnr + "\". Please verify your booking reference and try again, or let me know if you would like me to connect you with support.";

        Message agentMsg = Message.builder()
                .conversation(conv)
                .sender("AGENT")
                .content(reply)
                .timestamp(LocalDateTime.now())
                .detectedIntent("INVALID_PNR")
                .build();
        messageRepository.save(agentMsg);

        return AgentResponse.builder()
                .conversationId(conv.getId())
                .customerName("Guest")
                .loyaltyTier("NONE")
                .pnr(pnr)
                .detectedIntents(Collections.singletonList(IntentType.BOOKING_STATUS))
                .agentMessage(reply)
                .policyDecisions(Collections.emptyList())
                .executedActions(Collections.emptyList())
                .escalated(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String extractPnrFromMessage(String msg) {
        if (msg == null) return null;
        String upper = msg.toUpperCase();
        if (upper.contains("SK4821X")) return "SK4821X";
        if (upper.contains("TR1190B")) return "TR1190B";
        if (upper.contains("WL7742")) return "WL7742";
        return null;
    }
}