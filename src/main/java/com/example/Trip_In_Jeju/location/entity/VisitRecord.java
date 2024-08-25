package com.example.Trip_In_Jeju.location.entity;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "visit_record", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "food_id"}),
        @UniqueConstraint(columnNames = {"member_id", "activity_id"}),
        @UniqueConstraint(columnNames = {"member_id", "attractions_id"}),
        @UniqueConstraint(columnNames = {"member_id", "dessert_id"}),
        @UniqueConstraint(columnNames = {"member_id", "festivals_id"}),
        @UniqueConstraint(columnNames = {"member_id", "other_id"}),
        @UniqueConstraint(columnNames = {"member_id", "shopping_id"})
})
public class VisitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = true)
    private Food food;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = true)
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "attractions_id", nullable = true)
    private Attractions attractions;

    @ManyToOne
    @JoinColumn(name = "dessert_id", nullable = true)
    private Dessert dessert;

    @ManyToOne
    @JoinColumn(name = "festivals_id", nullable = true)
    private Festivals festivals;

    @ManyToOne
    @JoinColumn(name = "other_id", nullable = true)
    private Other other;

    @ManyToOne
    @JoinColumn(name = "shopping_id", nullable = true)
    private Shopping shopping;


    @CreatedDate
    private LocalDateTime visitTime;
}