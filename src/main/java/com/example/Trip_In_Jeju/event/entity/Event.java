package com.example.Trip_In_Jeju.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 이벤트 제목
    private String content; // 이벤트 내용
    private String thumbnailImg; // 이벤트 썸네일

    private LocalDate periodStart; // 시작 기간
    private LocalDate periodEnd; // 종료 기간

    @CreatedDate
    private LocalDateTime createDate; // 작성일

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageStep> steps;
}
