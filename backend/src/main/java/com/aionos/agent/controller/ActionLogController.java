package com.aionos.agent.controller;

import com.aionos.agent.entity.ActionLog;
import com.aionos.agent.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/action-logs")
public class ActionLogController {

    private final AuditLogService auditLogService;

    public ActionLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<ActionLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<ActionLog>> getLogsByConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(auditLogService.getLogsByConversation(conversationId));
    }

    @GetMapping("/pnr/{pnr}")
    public ResponseEntity<List<ActionLog>> getLogsByPnr(@PathVariable String pnr) {
        return ResponseEntity.ok(auditLogService.getLogsByPnr(pnr));
    }
}