package com.aionos.agent.service;

import com.aionos.agent.entity.Escalation;
import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;
import com.aionos.agent.enums.EscalationReason;
import com.aionos.agent.repository.EscalationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EscalationService {

    private static final Logger log = LoggerFactory.getLogger(EscalationService.class);
    private final EscalationRepository escalationRepository;
    private final AuditLogService auditLogService;

    public EscalationService(EscalationRepository escalationRepository, AuditLogService auditLogService) {
        this.escalationRepository = escalationRepository;
        this.auditLogService = auditLogService;
    }

    public Escalation createEscalation(Long conversationId, String pnr, String customerName,
                                       String reason, EscalationReason escalationReason, String policyReference) {
        Escalation escalation = Escalation.builder()
                .conversationId(conversationId)
                .pnr(pnr)
                .customerName(customerName)
                .reason(reason)
                .escalationType(escalationReason)
                .status("ESCALATED_PENDING_HUMAN")
                .createdAt(LocalDateTime.now())
                .build();

        Escalation saved = escalationRepository.save(escalation);

        auditLogService.logAction(conversationId, pnr, customerName,
                ActionType.ESCALATE_TO_HUMAN,
                ActionStatus.ESCALATED,
                reason,
                policyReference);

        log.warn("[ESCALATION CREATED] ID: {} | PNR: {} | Reason: {}", saved.getId(), pnr, reason);
        return saved;
    }

    public List<Escalation> getAllEscalations() {
        return escalationRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Escalation> getEscalationsByPnr(String pnr) {
        return escalationRepository.findByPnrOrderByCreatedAtDesc(pnr);
    }
}