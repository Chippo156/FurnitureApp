package org.ecommerce.ecommerce.controllers;

import org.ecommerce.ecommerce.components.JwtTokenUtils;
import org.ecommerce.ecommerce.dtos.MessageDTO;
import org.ecommerce.ecommerce.models.Message;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.services.impl.MessageService;
import org.ecommerce.ecommerce.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;
    // POST: Gửi tin nhắn
    @PostMapping("/sendMessage")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO message) {
        Message savedMessage = messageService.sendMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    // GET: Lấy tất cả các tin nhắn giữa người dùng và admin
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessagesForUser(
            @RequestHeader("Authorization") String token) throws Exception {  // lấy thông tin người dùng đã đăng nhập từ security
        // tên người dùng đã đăng nhập
        User user = userService.getUserDetailsFromToken(token.substring(7));
        System.out.println("username: " + user.getFullName());
        List<Message> messages = messageService.getMessages(user.getId(), 6L);
        return ResponseEntity.ok(messages);
    }

    // GET: Lấy tất cả các tin nhắn chưa đọc
    @GetMapping("/unreadMessages")
    public ResponseEntity<List<Message>> getUnreadMessages(@RequestHeader("Authorization") String token) {
       return null;
    }
}