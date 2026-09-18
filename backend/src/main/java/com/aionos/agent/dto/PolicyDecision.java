package com.aionos.agent.dto;

import com.aionos.agent.enums.ActionType;
import com.aionos.agent.enums.EscalationReason;

public class PolicyDecision {
    private String ruleCode;
    private String ruleName;
    private boolean allowed;
    private ActionType recommendedAction;
    private String reason;
    private String policyReference;
    private EscalationReason escalationReason;
    private String details;

    public PolicyDecision() {}

    public PolicyDecision(String ruleCode, String ruleName, boolean allowed,
                          ActionType recommendedAction, String reason, String policyReference,
                          EscalationReason escalationReason, String details) {
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.allowed = allowed;
        this.recommendedAction = recommendedAction;
        this.reason = reason;
        this.policyReference = policyReference;
        this.escalationReason = escalationReason;
        this.details = details;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String ruleCode;
        private String ruleName;
        private boolean allowed;
        private ActionType recommendedAction;
        private String reason;
        private String policyReference;
        private EscalationReason escalationReason;
        private String details;

        public Builder ruleCode(String ruleCode) { this.ruleCode = ruleCode; return this; }
        public Builder ruleName(String ruleName) { this.ruleName = ruleName; return this; }
        public Builder allowed(boolean allowed) { this.allowed = allowed; return this; }
        public Builder recommendedAction(ActionType recommendedAction) { this.recommendedAction = recommendedAction; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder policyReference(String policyReference) { this.policyReference = policyReference; return this; }
        public Builder escalationReason(EscalationReason escalationReason) { this.escalationReason = escalationReason; return this; }
        public Builder details(String details) { this.details = details; return this; }
        public PolicyDecision build() {
            return new PolicyDecision(ruleCode, ruleName, allowed, recommendedAction, reason, policyReference, escalationReason, details);
        }
    }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }

    public ActionType getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(ActionType recommendedAction) { this.recommendedAction = recommendedAction; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getPolicyReference() { return policyReference; }
    public void setPolicyReference(String policyReference) { this.policyReference = policyReference; }

    public EscalationReason getEscalationReason() { return escalationReason; }
    public void setEscalationReason(EscalationReason escalationReason) { this.escalationReason = escalationReason; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}