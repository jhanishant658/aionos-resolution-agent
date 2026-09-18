package com.aionos.agent.dto;

import com.aionos.agent.enums.IntentType;
import java.time.LocalDateTime;
import java.util.List;

public class AgentResponse {
    private Long conversationId;
    private String customerName;
    private String loyaltyTier;
    private String pnr;
    private String flightNumber;
    private String flightStatus;
    private String route;
    private List<IntentType> detectedIntents;
    private String agentMessage;
    private List<PolicyDecision> policyDecisions;
    private List<ActionResult> executedActions;
    private boolean escalated;
    private String escalationReason;
    private LocalDateTime timestamp;

    public AgentResponse() {}

    public AgentResponse(Long conversationId, String customerName, String loyaltyTier,
                         String pnr, String flightNumber, String flightStatus, String route,
                         List<IntentType> detectedIntents, String agentMessage,
                         List<PolicyDecision> policyDecisions, List<ActionResult> executedActions,
                         boolean escalated, String escalationReason, LocalDateTime timestamp) {
        this.conversationId = conversationId;
        this.customerName = customerName;
        this.loyaltyTier = loyaltyTier;
        this.pnr = pnr;
        this.flightNumber = flightNumber;
        this.flightStatus = flightStatus;
        this.route = route;
        this.detectedIntents = detectedIntents;
        this.agentMessage = agentMessage;
        this.policyDecisions = policyDecisions;
        this.executedActions = executedActions;
        this.escalated = escalated;
        this.escalationReason = escalationReason;
        this.timestamp = timestamp;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long conversationId;
        private String customerName;
        private String loyaltyTier;
        private String pnr;
        private String flightNumber;
        private String flightStatus;
        private String route;
        private List<IntentType> detectedIntents;
        private String agentMessage;
        private List<PolicyDecision> policyDecisions;
        private List<ActionResult> executedActions;
        private boolean escalated;
        private String escalationReason;
        private LocalDateTime timestamp;

        public Builder conversationId(Long conversationId) { this.conversationId = conversationId; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder loyaltyTier(String loyaltyTier) { this.loyaltyTier = loyaltyTier; return this; }
        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
        public Builder flightStatus(String flightStatus) { this.flightStatus = flightStatus; return this; }
        public Builder route(String route) { this.route = route; return this; }
        public Builder detectedIntents(List<IntentType> detectedIntents) { this.detectedIntents = detectedIntents; return this; }
        public Builder agentMessage(String agentMessage) { this.agentMessage = agentMessage; return this; }
        public Builder policyDecisions(List<PolicyDecision> policyDecisions) { this.policyDecisions = policyDecisions; return this; }
        public Builder executedActions(List<ActionResult> executedActions) { this.executedActions = executedActions; return this; }
        public Builder escalated(boolean escalated) { this.escalated = escalated; return this; }
        public Builder escalationReason(String escalationReason) { this.escalationReason = escalationReason; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public AgentResponse build() {
            return new AgentResponse(conversationId, customerName, loyaltyTier, pnr, flightNumber, flightStatus, route,
                    detectedIntents, agentMessage, policyDecisions, executedActions, escalated, escalationReason, timestamp);
        }
    }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getLoyaltyTier() { return loyaltyTier; }
    public void setLoyaltyTier(String loyaltyTier) { this.loyaltyTier = loyaltyTier; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getFlightStatus() { return flightStatus; }
    public void setFlightStatus(String flightStatus) { this.flightStatus = flightStatus; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public List<IntentType> getDetectedIntents() { return detectedIntents; }
    public void setDetectedIntents(List<IntentType> detectedIntents) { this.detectedIntents = detectedIntents; }

    public String getAgentMessage() { return agentMessage; }
    public void setAgentMessage(String agentMessage) { this.agentMessage = agentMessage; }

    public List<PolicyDecision> getPolicyDecisions() { return policyDecisions; }
    public void setPolicyDecisions(List<PolicyDecision> policyDecisions) { this.policyDecisions = policyDecisions; }

    public List<ActionResult> getExecutedActions() { return executedActions; }
    public void setExecutedActions(List<ActionResult> executedActions) { this.executedActions = executedActions; }

    public boolean isEscalated() { return escalated; }
    public void setEscalated(boolean escalated) { this.escalated = escalated; }

    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}