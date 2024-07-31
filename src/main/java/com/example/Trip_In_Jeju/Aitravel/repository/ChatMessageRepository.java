package com.example.Trip_In_Jeju.Aitravel.repository;

import com.example.Trip_In_Jeju.Aitravel.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}