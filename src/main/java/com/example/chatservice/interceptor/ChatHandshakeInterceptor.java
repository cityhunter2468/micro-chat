package com.example.chatservice.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

public class ChatHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            
            // Check for Authorization header (for authenticated users)
            String authHeader = httpRequest.getHeader("Authorization");
            String userId = null;
            String senderName = null;
            boolean isGuest = true;
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // Extract token and decode (simplified - in real app, validate JWT)
                String token = authHeader.substring(7);
                // For demo purposes, we'll extract user info from token
                // In real implementation, you would validate JWT and extract user info
                if (token.length() > 10) { // Simple validation
                    userId = "user-" + token.substring(0, 8);
                    senderName = "User " + token.substring(0, 4);
                    isGuest = false;
                }
            }
            
            // If no valid auth, create guest user
            if (userId == null) {
                String guestId = "guest-" + UUID.randomUUID().toString().substring(0, 8);
                userId = guestId;
                senderName = "Guest " + guestId.substring(6);
                isGuest = true;
            }
            
            // Store user info in session attributes
            attributes.put("userId", userId);
            attributes.put("senderName", senderName);
            attributes.put("isGuest", isGuest);
            
            return true;
        }
        
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                              WebSocketHandler wsHandler, Exception exception) {
        // Called after handshake is complete
    }
}
