package com.example.Trip_In_Jeju.calendar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime start; // 시작 시간
    private LocalDateTime end; // 종료 시간

    private LocalDateTime periodStart; // 기간 시작
    private LocalDateTime periodEnd; // 기간 종료

}
