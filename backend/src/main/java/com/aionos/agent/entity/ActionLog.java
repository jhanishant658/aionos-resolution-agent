package com.aionos.agent.entity;

import com.aionos.agent.enums.ActionStatus;
import com.aionos.agent.enums.ActionType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;

    @Column(nullable = false)
    private String pnr;

    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionStatus status;

    @Column(length = 2000)
    private String reason;

    private String policyReference;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ActionLog() {}

    public ActionLog(Long id, Long conversationId, String pnr, String customerName,
                     ActionType actionType, ActionStatus status, String reason,
                     String policyReference, LocalDateTime timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.pnr = pnr;
        this.customerName = customerName;
        this.actionType = actionType;
        this.status = status;
        this.reason = reason;
        this.policyReference = policyReference;
        this.timestamp = timestamp;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long conversationId;
        private String pnr;
        private String customerName;
        private ActionType actionType;
        private ActionStatus status;
        private String reason;
        private String policyReference;
        private LocalDateTime timestamp;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder conversationId(Long conversationId) { this.conversationId = conversationId; return this; }
        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder actionType(ActionType actionType) { this.actionType = actionType; return this; }
        public Builder status(ActionStatus status) { this.status = status; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder policyReference(String policyReference) { this.policyReference = policyReference; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public ActionLog build() {
            return new ActionLog(id, conversationId, pnr, customerName, actionType, status, reason, policyReference, timestamp);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public ActionStatus getStatus() { return status; }
    public void setStatus(ActionStatus status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getPolicyReference() { return policyReference; }
    public void setPolicyReference(String policyReference) { this.policyReference = policyReference; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}