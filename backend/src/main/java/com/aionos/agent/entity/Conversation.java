package com.aionos.agent.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pnr;

    private String customerName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String status;

    public Conversation() {}

    public Conversation(Long id, String pnr, String customerName, LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.id = id;
        this.pnr = pnr;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String pnr;
        private String customerName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String status;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Conversation build() {
            return new Conversation(id, pnr, customerName, createdAt, updatedAt, status);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}