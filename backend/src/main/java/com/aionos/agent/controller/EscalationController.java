package com.aionos.agent.controller;

import com.aionos.agent.entity.Escalation;
import com.aionos.agent.service.EscalationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/escalations")
public class EscalationController {

    private final EscalationService escalationService;

    public EscalationController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }

    @GetMapping
    public ResponseEntity<List<Escalation>> getAllEscalations() {
        return ResponseEntity.ok(escalationService.getAllEscalations());
    }

    @GetMapping("/pnr/{pnr}")
    public ResponseEntity<List<Escalation>> getEscalationsByPnr(@PathVariable String pnr) {
        return ResponseEntity.ok(escalationService.getEscalationsByPnr(pnr));
    }
}