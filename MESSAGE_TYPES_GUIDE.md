# Hướng dẫn sử dụng Message Types

## 📋 Tổng quan

Chat service hiện hỗ trợ 2 loại message chính:

1. **PUBLIC Message** - Gửi cho tất cả mọi người
2. **PRIVATE Message** - Gửi cho một người cụ thể

## 🌐 PUBLIC Messages

### Cách gửi:
- **Endpoint**: `/app/sendMessage`
- **Topic nhận**: `/topic/public`
- **Tất cả client** đã kết nối sẽ nhận được message

### JSON Format:
```json
{
    "content": "Hello everyone!",
    "messageType": "PUBLIC"
}
```

### Response:
```json
{
    "senderId": "user-12345678",
    "senderName": "User 1234",
    "content": "Hello everyone!",
    "timestamp": "2024-01-15 10:30:45",
    "isGuest": false,
    "messageType": "PUBLIC",
    "targetUserId": null
}
```

## 🔒 PRIVATE Messages

### Cách gửi:
- **Endpoint**: `/app/sendPrivateMessage`
- **Queue nhận**: `/user/queue/private`
- **Chỉ người gửi và người nhận** sẽ nhận được message

### JSON Format:
```json
{
    "content": "Secret message",
    "targetUserId": "user-12345678",
    "messageType": "PRIVATE"
}
```

### Response:
```json
{
    "senderId": "user-87654321",
    "senderName": "User 8765",
    "content": "Secret message",
    "timestamp": "2024-01-15 10:30:45",
    "isGuest": false,
    "messageType": "PRIVATE",
    "targetUserId": "user-12345678"
}
```

## 🎯 Cách phân biệt Message Types

### 1. Trong Frontend JavaScript:

```javascript
// Subscribe to public messages
stompClient.subscribe('/topic/public', function (message) {
    const data = JSON.parse(message.body);
    displayMessage(data, 'public'); // Hiển thị với icon 🌐
});

// Subscribe to private messages
stompClient.subscribe('/user/queue/private', function (message) {
    const data = JSON.parse(message.body);
    displayMessage(data, 'private'); // Hiển thị với icon 🔒
});
```

### 2. Trong Backend Java:

```java
// Kiểm tra message type
if (chatMessage.getMessageType() == MessageType.PUBLIC) {
    // Gửi cho tất cả mọi người
    return chatMessage; // @SendTo("/topic/public")
} else if (chatMessage.getMessageType() == MessageType.PRIVATE) {
    // Gửi cho người cụ thể
    messagingTemplate.convertAndSendToUser(targetUserId, "/queue/private", chatMessage);
}
```

## 📊 So sánh Message Types

| Tính năng | PUBLIC | PRIVATE |
|-----------|--------|---------|
| **Người nhận** | Tất cả client | Chỉ sender + target |
| **Endpoint** | `/app/sendMessage` | `/app/sendPrivateMessage` |
| **Topic/Queue** | `/topic/public` | `/user/queue/private` |
| **Bảo mật** | Không | Có |
| **Hiệu suất** | Broadcast | Point-to-point |
| **Use case** | Chat chung, thông báo | Tin nhắn riêng tư |

## 🧪 Test Scenarios

### Scenario 1: Public Message
1. Mở 2 tab browser
2. Kết nối cả 2 tab
3. Gửi public message từ tab 1
4. **Kết quả**: Cả 2 tab đều nhận được message với icon 🌐

### Scenario 2: Private Message
1. Mở 2 tab browser
2. Kết nối cả 2 tab, ghi nhớ User ID của mỗi tab
3. Gửi private message từ tab 1 đến User ID của tab 2
4. **Kết quả**: Chỉ tab 1 và tab 2 nhận được message với icon 🔒

### Scenario 3: Mixed Messages
1. Mở 3 tab browser (A, B, C)
2. Gửi public message từ tab A → Tất cả 3 tab nhận được
3. Gửi private message từ tab A đến tab B → Chỉ tab A và B nhận được
4. Tab C không nhận được private message

## 🔧 Troubleshooting

### Private message không nhận được:
1. Kiểm tra `targetUserId` có đúng không
2. Đảm bảo target user đã subscribe `/user/queue/private`
3. Kiểm tra User ID format: `user-xxxx` hoặc `guest-xxxx`

### Public message không hiển thị:
1. Kiểm tra đã subscribe `/topic/public` chưa
2. Kiểm tra endpoint `/app/sendMessage`
3. Kiểm tra `messageType: "PUBLIC"`

## 💡 Best Practices

1. **Luôn kiểm tra messageType** trước khi xử lý
2. **Validate targetUserId** trước khi gửi private message
3. **Hiển thị visual indicator** khác nhau cho public/private
4. **Log message types** để debug
5. **Handle errors** khi target user không tồn tại

## 🚀 Mở rộng trong tương lai

- **GROUP Messages**: Gửi cho một nhóm người
- **BROADCAST Messages**: Gửi cho tất cả user online
- **SYSTEM Messages**: Tin nhắn hệ thống
- **FILE Messages**: Gửi file đính kèm
