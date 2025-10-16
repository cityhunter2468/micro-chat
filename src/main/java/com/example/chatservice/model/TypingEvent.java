package com.example.chatservice.model;

public class TypingEvent {
    private String senderId;
    private boolean typing;

    // Constructors
    public TypingEvent() {}

    public TypingEvent(String senderId, boolean typing) {
        this.senderId = senderId;
        this.typing = typing;
    }

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    @Override
    public String toString() {
        return "TypingEvent{" +
                "senderId='" + senderId + '\'' +
                ", typing=" + typing +
                '}';
    }
}
