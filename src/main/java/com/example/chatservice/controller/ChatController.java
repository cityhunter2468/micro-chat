package com.example.chatservice.controller;

import com.example.chatservice.model.ChatMessage;
import com.example.chatservice.model.TypingEvent;
import com.example.chatservice.model.MessageType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Get user info from session attributes
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String senderName = (String) headerAccessor.getSessionAttributes().get("senderName");
        Boolean isGuest = (Boolean) headerAccessor.getSessionAttributes().get("isGuest");
        
        // Override with session info
        chatMessage.setSenderId(userId);
        chatMessage.setSenderName(senderName);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setGuest(isGuest != null ? isGuest : true);
        
        // Set default message type to PUBLIC if not specified
        if (chatMessage.getMessageType() == null) {
            chatMessage.setMessageType(MessageType.PUBLIC);
        }
        
        return chatMessage;
    }

    @MessageMapping("/sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Get user info from session attributes
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String senderName = (String) headerAccessor.getSessionAttributes().get("senderName");
        Boolean isGuest = (Boolean) headerAccessor.getSessionAttributes().get("isGuest");
        
        // Override with session info
        chatMessage.setSenderId(userId);
        chatMessage.setSenderName(senderName);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setGuest(isGuest != null ? isGuest : true);
        chatMessage.setMessageType(MessageType.PRIVATE);
        
        // Send to specific user
        if (chatMessage.getTargetUserId() != null) {
            messagingTemplate.convertAndSendToUser(
                chatMessage.getTargetUserId(), 
                "/queue/private", 
                chatMessage
            );
            
            // Also send back to sender for confirmation
            messagingTemplate.convertAndSendToUser(
                userId, 
                "/queue/private", 
                chatMessage
            );
        }
    }

    @MessageMapping("/typing")
    @SendTo("/topic/public")
    public TypingEvent handleTyping(@Payload TypingEvent typingEvent, SimpMessageHeaderAccessor headerAccessor) {
        // Get user info from session attributes
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        
        // Override with session info
        typingEvent.setSenderId(userId);
        
        return typingEvent;
    }
}
