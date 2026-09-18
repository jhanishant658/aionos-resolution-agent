package com.aionos.agent.service;

import com.aionos.agent.dto.ActionResult;
import com.aionos.agent.dto.PolicyDecision;
import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Customer;
import com.aionos.agent.enums.IntentType;

import java.util.List;

public interface AIService {
    String generateResponse(Customer customer, Booking booking, List<IntentType> intents,
                            List<PolicyDecision> decisions, List<ActionResult> actions,
                            boolean escalated, String escalationReason, String userMessage);
}
