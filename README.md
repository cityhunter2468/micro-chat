# Chat Service - Spring Boot WebSocket

Một service chat realtime sử dụng Spring Boot 3.4.7, Java 17 và WebSocket với STOMP protocol.

## Tính năng

- ✅ Chat realtime cho user đã đăng nhập (authenticated)
- ✅ Chat realtime cho user chưa đăng nhập (guest)
- ✅ WebSocket endpoint `/ws-chat`
- ✅ Topic `/topic/public` để broadcast message
- ✅ Endpoint `/app/sendMessage` để gửi message
- ✅ Event `/app/typing` để thông báo trạng thái "đang nhập"
- ✅ Phân biệt user login (JWT) và guest (UUID)
- ✅ CORS support cho tất cả origins

## Yêu cầu hệ thống

- Java 17+
- Maven 3.6+
- Spring Boot 3.4.7

## Cài đặt và chạy

### 1. Cài đặt Maven (nếu chưa có)

**Windows:**
```bash
# Tải Maven từ https://maven.apache.org/download.cgi
# Giải nén và thêm vào PATH
```

**Hoặc sử dụng Chocolatey:**
```bash
choco install maven
```

### 2. Chạy ứng dụng

```bash
# Compile project
mvn clean compile

# Chạy ứng dụng
mvn spring-boot:run
```

Ứng dụng sẽ chạy trên `http://localhost:8080`

### 3. Test với Postman

1. Mở Postman
2. Tạo WebSocket request với URL: `ws://localhost:8080/ws-chat`
3. Kết nối và gửi message JSON

### 4. Test với Frontend

Mở file `test.html` trong browser hoặc sử dụng code JavaScript trong `TESTING_GUIDE.md`

## Cấu trúc Project

```
src/main/java/com/example/chatservice/
├── config/
│   ├── WebSocketConfig.java          # Cấu hình WebSocket + STOMP
│   └── SecurityConfig.java           # Cấu hình Security (tắt auth)
├── controller/
│   └── ChatController.java           # Xử lý message và typing
├── interceptor/
│   └── ChatHandshakeInterceptor.java # Phân biệt user/guest
├── model/
│   ├── ChatMessage.java              # Model cho chat message
│   └── TypingEvent.java              # Model cho typing event
└── ChatServiceApplication.java       # Main class
```

## API Endpoints

### WebSocket
- **Endpoint**: `/ws-chat`
- **Protocol**: STOMP over WebSocket
- **Fallback**: SockJS

### Message Endpoints
- **Send Message**: `/app/sendMessage`
- **Send Typing**: `/app/typing`
- **Receive Messages**: `/topic/public`

## Message Format

### ChatMessage
```json
{
    "senderId": "user-12345678",
    "senderName": "User 1234", 
    "content": "Hello world!",
    "timestamp": "2024-01-15 10:30:45",
    "isGuest": false
}
```

### TypingEvent
```json
{
    "senderId": "user-12345678",
    "typing": true
}
```

## Authentication

### Guest User
- Không cần header
- Tự động tạo `guest-xxxx` ID
- `isGuest: true`

### Authenticated User
- Header: `Authorization: Bearer your-jwt-token`
- Tạo `user-xxxx` ID từ token
- `isGuest: false`

## Troubleshooting

1. **Port 8080 đã được sử dụng**: Thay đổi port trong `application.yml`
2. **CORS errors**: Kiểm tra `setAllowedOriginPatterns("*")` trong WebSocketConfig
3. **Connection failed**: Đảm bảo ứng dụng đang chạy và port đúng
4. **Maven not found**: Cài đặt Maven và thêm vào PATH

## Development

### Thêm tính năng mới
1. Tạo model class trong `model/`
2. Thêm endpoint trong `ChatController`
3. Cập nhật `WebSocketConfig` nếu cần
4. Test với Postman hoặc frontend

### Logs
- Enable debug logs trong `application.yml`
- Xem logs trong console khi chạy `mvn spring-boot:run`
