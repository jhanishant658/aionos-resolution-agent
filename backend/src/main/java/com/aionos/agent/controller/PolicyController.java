package com.aionos.agent.controller;

import com.aionos.agent.entity.PolicyRule;
import com.aionos.agent.repository.PolicyRuleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRuleRepository policyRuleRepository;

    public PolicyController(PolicyRuleRepository policyRuleRepository) {
        this.policyRuleRepository = policyRuleRepository;
    }

    @GetMapping
    public ResponseEntity<List<PolicyRule>> getAllPolicies() {
        return ResponseEntity.ok(policyRuleRepository.findAll());
    }
}