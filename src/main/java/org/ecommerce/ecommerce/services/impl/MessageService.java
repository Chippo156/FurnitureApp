package org.ecommerce.ecommerce.services.impl;

import org.ecommerce.ecommerce.dtos.MessageDTO;
import org.ecommerce.ecommerce.models.Message;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.repository.MessageRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;
    // Gửi tin nhắn từ người dùng hoặc admin
    public Message sendMessage(MessageDTO message) {
        User sender = userRepository.findById( message.getSender()).orElseThrow();
        User receiver = userRepository.findById(message.getReceiver()).orElseThrow();
        Message newMessage = Message.builder()
                .sender(sender.getId())
                .receiver(receiver.getId())
                .content(message.getContent())
                .build();
        return messageRepository.save(newMessage);
    }

    // Lấy danh sách tin nhắn giữa người dùng và admin
    public List<Message> getMessages(Long sender, Long receiver) {
        return messageRepository.findBySenderAndReceiver(sender,  receiver);
    }

    // Lấy danh sách tin nhắn chưa đọc của admin
    public List<Message> getUnreadMessages(String receiver) {
       return null;
    }
}