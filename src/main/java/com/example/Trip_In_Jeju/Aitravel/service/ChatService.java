package com.example.Trip_In_Jeju.Aitravel.service;

import com.example.Trip_In_Jeju.Aitravel.entity.ChatMessage;
import com.example.Trip_In_Jeju.Aitravel.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(String sender, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setMessage(message);
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessages() {
        return chatMessageRepository.findAll();
    }
}
