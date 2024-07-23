package com.example.Trip_In_Jeju.calendar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@ToString
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalTime businessHoursStart; // 영업시간 시작
    private LocalTime businessHoursEnd;   // 영업시간 종료

    private LocalDate periodStart; // 기간 시작
    private LocalDate periodEnd; // 기간 종료

    private String closedDay; // 휴무일

}
