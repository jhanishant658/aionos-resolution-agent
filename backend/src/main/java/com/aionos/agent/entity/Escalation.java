package com.aionos.agent.entity;

import com.aionos.agent.enums.EscalationReason;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "escalations")
public class Escalation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long conversationId;

    @Column(nullable = false)
    private String pnr;

    private String customerName;

    @Column(nullable = false, length = 2000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EscalationReason escalationType;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Escalation() {}

    public Escalation(Long id, Long conversationId, String pnr, String customerName,
                      String reason, EscalationReason escalationType, String status, LocalDateTime createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.pnr = pnr;
        this.customerName = customerName;
        this.reason = reason;
        this.escalationType = escalationType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long conversationId;
        private String pnr;
        private String customerName;
        private String reason;
        private EscalationReason escalationType;
        private String status;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder conversationId(Long conversationId) { this.conversationId = conversationId; return this; }
        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder escalationType(EscalationReason escalationType) { this.escalationType = escalationType; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Escalation build() {
            return new Escalation(id, conversationId, pnr, customerName, reason, escalationType, status, createdAt);
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

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public EscalationReason getEscalationType() { return escalationType; }
    public void setEscalationType(EscalationReason escalationType) { this.escalationType = escalationType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}