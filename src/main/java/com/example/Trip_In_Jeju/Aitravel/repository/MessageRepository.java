package com.example.Trip_In_Jeju.Aitravel.repository;

import com.example.Trip_In_Jeju.Aitravel.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
