package com.aionos.agent.controller;

import com.aionos.agent.dto.AgentResponse;
import com.aionos.agent.service.DemoScenarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DemoScenarioService demoScenarioService;

    public DemoController(DemoScenarioService demoScenarioService) {
        this.demoScenarioService = demoScenarioService;
    }

    @PostMapping("/scenario/{scenarioNumber}")
    public ResponseEntity<AgentResponse> runScenario(@PathVariable int scenarioNumber) {
        AgentResponse response = demoScenarioService.runScenario(scenarioNumber);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetDemo() {
        demoScenarioService.resetDemo();
        return ResponseEntity.ok(Map.of("message", "Demo database reset successfully to initial assignment data"));
    }
}