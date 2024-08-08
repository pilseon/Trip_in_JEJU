package com.example.Trip_In_Jeju.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class ImageStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stepNumber; // 스탭 순서

    private String imageFilename; // 스탭별 이미지 이름

    private String imageFilePath; // 스탭별 이미지 경로

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
