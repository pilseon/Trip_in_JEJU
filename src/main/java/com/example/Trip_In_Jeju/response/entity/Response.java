package com.example.Trip_In_Jeju.response.entity;

import com.example.Trip_In_Jeju.inquiry.entity.Inquiry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @OneToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    // Getters and Setters
}