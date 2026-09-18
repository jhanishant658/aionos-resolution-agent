package com.aionos.agent.dto;

import jakarta.validation.constraints.NotBlank;

public class AgentMessageRequest {
    private String pnr;

    @NotBlank(message = "Message content is required")
    private String message;

    private Long conversationId;

    public AgentMessageRequest() {}

    public AgentMessageRequest(String pnr, String message, Long conversationId) {
        this.pnr = pnr;
        this.message = message;
        this.conversationId = conversationId;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String pnr;
        private String message;
        private Long conversationId;

        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder conversationId(Long conversationId) { this.conversationId = conversationId; return this; }
        public AgentMessageRequest build() {
            return new AgentMessageRequest(pnr, message, conversationId);
        }
    }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
}