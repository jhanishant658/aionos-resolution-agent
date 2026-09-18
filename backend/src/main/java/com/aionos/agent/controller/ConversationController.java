package com.aionos.agent.controller;

import com.aionos.agent.entity.Conversation;
import com.aionos.agent.entity.Message;
import com.aionos.agent.repository.ConversationRepository;
import com.aionos.agent.repository.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationController(ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversationById(@PathVariable Long id) {
        return conversationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessagesByConversation(@PathVariable Long id) {
        return ResponseEntity.ok(messageRepository.findByConversationIdOrderByTimestampAsc(id));
    }

    @GetMapping("/pnr/{pnr}")
    public ResponseEntity<List<Conversation>> getConversationsByPnr(@PathVariable String pnr) {
        return ResponseEntity.ok(conversationRepository.findByPnrOrderByCreatedAtDesc(pnr));
    }
}