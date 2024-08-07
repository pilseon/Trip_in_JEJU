package com.example.Trip_In_Jeju.inquiry.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String status;

    @Column(length = 1000)
    private String answer;

    private String author;

    @CreatedDate
    private LocalDateTime createdAt;




    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
