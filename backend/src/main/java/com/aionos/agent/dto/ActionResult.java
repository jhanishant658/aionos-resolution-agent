package com.aionos.agent.dto;

import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;

public class ActionResult {
    private ActionType actionType;
    private ActionStatus status;
    private String description;
    private String policyReference;
    private boolean escalationCreated;
    private String escalationReason;

    public ActionResult() {}

    public ActionResult(ActionType actionType, ActionStatus status, String description,
                        String policyReference, boolean escalationCreated, String escalationReason) {
        this.actionType = actionType;
        this.status = status;
        this.description = description;
        this.policyReference = policyReference;
        this.escalationCreated = escalationCreated;
        this.escalationReason = escalationReason;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private ActionType actionType;
        private ActionStatus status;
        private String description;
        private String policyReference;
        private boolean escalationCreated;
        private String escalationReason;

        public Builder actionType(ActionType actionType) { this.actionType = actionType; return this; }
        public Builder status(ActionStatus status) { this.status = status; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder policyReference(String policyReference) { this.policyReference = policyReference; return this; }
        public Builder escalationCreated(boolean escalationCreated) { this.escalationCreated = escalationCreated; return this; }
        public Builder escalationReason(String escalationReason) { this.escalationReason = escalationReason; return this; }
        public ActionResult build() {
            return new ActionResult(actionType, status, description, policyReference, escalationCreated, escalationReason);
        }
    }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public ActionStatus getStatus() { return status; }
    public void setStatus(ActionStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPolicyReference() { return policyReference; }
    public void setPolicyReference(String policyReference) { this.policyReference = policyReference; }

    public boolean isEscalationCreated() { return escalationCreated; }
    public void setEscalationCreated(boolean escalationCreated) { this.escalationCreated = escalationCreated; }

    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }
}