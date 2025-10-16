package com.example.chatservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ChatMessage {
    private String senderId;
    private String senderName;
    private String content;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private boolean isGuest;
    private MessageType messageType;
    private String targetUserId; // null nếu là PUBLIC message

    // Constructors
    public ChatMessage() {}

    public ChatMessage(String senderId, String senderName, String content, LocalDateTime timestamp, boolean isGuest) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
        this.isGuest = isGuest;
        this.messageType = MessageType.PUBLIC;
        this.targetUserId = null;
    }

    public ChatMessage(String senderId, String senderName, String content, LocalDateTime timestamp, boolean isGuest, MessageType messageType, String targetUserId) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
        this.isGuest = isGuest;
        this.messageType = messageType;
        this.targetUserId = targetUserId;
    }

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", isGuest=" + isGuest +
                ", messageType=" + messageType +
                ", targetUserId='" + targetUserId + '\'' +
                '}';
    }
}
