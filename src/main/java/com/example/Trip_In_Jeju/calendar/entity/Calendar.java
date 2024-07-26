package com.example.Trip_In_Jeju.calendar.entity;

import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
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

    private LocalTime businessHoursStart;
    private LocalTime businessHoursEnd;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    private String closedDay;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Festivals> festivals;
}
