package com.example.Trip_In_Jeju.calendar.entity;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Food> foods; // Food 엔티티를 참조
}
