package com.example.chatservice.model;

public enum MessageType {
    PUBLIC,     // Gửi cho tất cả mọi người
    PRIVATE,    // Gửi cho một người cụ thể
    GROUP       // Gửi cho một nhóm người (có thể mở rộng sau)
}
