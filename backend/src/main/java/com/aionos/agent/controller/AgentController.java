package com.aionos.agent.controller;

import com.aionos.agent.dto.AgentMessageRequest;
import com.aionos.agent.dto.AgentResponse;
import com.aionos.agent.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/message")
    public ResponseEntity<AgentResponse> processMessage(@Valid @RequestBody AgentMessageRequest request) {
        AgentResponse response = agentService.processMessage(request);
        return ResponseEntity.ok(response);
    }
}