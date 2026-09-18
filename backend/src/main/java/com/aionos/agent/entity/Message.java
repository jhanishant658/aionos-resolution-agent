package com.aionos.agent.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String detectedIntent;

    public Message() {}

    public Message(Long id, Conversation conversation, String sender, String content, LocalDateTime timestamp, String detectedIntent) {
        this.id = id;
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.detectedIntent = detectedIntent;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Conversation conversation;
        private String sender;
        private String content;
        private LocalDateTime timestamp;
        private String detectedIntent;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder conversation(Conversation conversation) { this.conversation = conversation; return this; }
        public Builder sender(String sender) { this.sender = sender; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder detectedIntent(String detectedIntent) { this.detectedIntent = detectedIntent; return this; }
        public Message build() {
            return new Message(id, conversation, sender, content, timestamp, detectedIntent);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Conversation getConversation() { return conversation; }
    public void setConversation(Conversation conversation) { this.conversation = conversation; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDetectedIntent() { return detectedIntent; }
    public void setDetectedIntent(String detectedIntent) { this.detectedIntent = detectedIntent; }
}