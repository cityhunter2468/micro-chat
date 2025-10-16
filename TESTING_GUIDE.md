# Hướng dẫn Test Chat Service

## 1. Chạy ứng dụng

```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy trên port 8080.

## 2. Test với JavaScript (Frontend)

### HTML Test Page

Tạo file `test.html`:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Chat Test</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <div id="messages"></div>
    <input type="text" id="messageInput" placeholder="Nhập tin nhắn...">
    <button onclick="sendMessage()">Gửi</button>
    <button onclick="connect()">Kết nối</button>
    <button onclick="disconnect()">Ngắt kết nối</button>
    
    <script>
        let stompClient = null;
        let isConnected = false;

        function connect() {
            const socket = new SockJS('http://localhost:8080/ws-chat');
            stompClient = Stomp.over(socket);
            
            // For authenticated user, add Authorization header
            const headers = {
                // Uncomment for authenticated user:
                // 'Authorization': 'Bearer your-jwt-token-here'
            };
            
            stompClient.connect(headers, function (frame) {
                console.log('Connected: ' + frame);
                isConnected = true;
                
                // Subscribe to public messages
                stompClient.subscribe('/topic/public', function (message) {
                    const data = JSON.parse(message.body);
                    displayMessage(data);
                });
            });
        }

        function disconnect() {
            if (stompClient !== null) {
                stompClient.disconnect();
                isConnected = false;
                console.log("Disconnected");
            }
        }

        function sendMessage() {
            if (stompClient && isConnected) {
                const messageInput = document.getElementById('messageInput');
                const message = messageInput.value;
                
                if (message.trim()) {
                    stompClient.send("/app/sendMessage", {}, JSON.stringify({
                        content: message
                    }));
                    messageInput.value = '';
                }
            }
        }

        function sendTyping() {
            if (stompClient && isConnected) {
                stompClient.send("/app/typing", {}, JSON.stringify({
                    typing: true
                }));
            }
        }

        function displayMessage(message) {
            const messagesDiv = document.getElementById('messages');
            const messageElement = document.createElement('div');
            messageElement.innerHTML = `
                <strong>${message.senderName}</strong> 
                ${message.isGuest ? '(Guest)' : '(User)'}: 
                ${message.content} 
                <small>(${message.timestamp})</small>
            `;
            messagesDiv.appendChild(messageElement);
        }

        // Auto-connect on page load
        window.onload = function() {
            connect();
        };
    </script>
</body>
</html>
```

## 3. Test với Postman

### Kết nối WebSocket

1. Mở Postman
2. Tạo request mới, chọn WebSocket
3. URL: `ws://localhost:8080/ws-chat`
4. Headers (tùy chọn):
   - `Authorization: Bearer your-jwt-token` (cho user đã login)
   - Không có header (cho guest)

### Gửi Message

Sau khi kết nối, gửi JSON message:

```json
{
    "content": "Hello everyone!"
}
```

### Gửi Typing Event

```json
{
    "typing": true
}
```

## 4. Test Scenarios

### Scenario 1: Guest User
1. Kết nối WebSocket không có Authorization header
2. Gửi message → Sẽ nhận được response với `isGuest: true` và `senderId: guest-xxxx`

### Scenario 2: Authenticated User
1. Kết nối WebSocket với header `Authorization: Bearer valid-token`
2. Gửi message → Sẽ nhận được response với `isGuest: false` và `senderId: user-xxxx`

### Scenario 3: Multiple Users
1. Mở nhiều tab browser hoặc nhiều Postman WebSocket connections
2. Gửi message từ một client
3. Tất cả clients khác sẽ nhận được message realtime

## 5. Endpoints và Topics

- **WebSocket Endpoint**: `/ws-chat`
- **Send Message**: `/app/sendMessage`
- **Send Typing**: `/app/typing`
- **Receive Messages**: `/topic/public`

## 6. Message Format

### ChatMessage Response
```json
{
    "senderId": "user-12345678",
    "senderName": "User 1234",
    "content": "Hello world!",
    "timestamp": "2024-01-15 10:30:45",
    "isGuest": false
}
```

### TypingEvent Response
```json
{
    "senderId": "user-12345678",
    "typing": true
}
```

## 7. Troubleshooting

- Đảm bảo ứng dụng đang chạy trên port 8080
- Kiểm tra console log để xem connection status
- Sử dụng browser developer tools để debug WebSocket connection
- Kiểm tra CORS settings nếu test từ domain khác
