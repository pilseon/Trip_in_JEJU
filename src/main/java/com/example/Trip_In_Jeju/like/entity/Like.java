package com.example.Trip_In_Jeju.like.entity;


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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`like`")  // 테이블 이름을 명시적으로 지정
@Getter
@Setter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "attractions_id")
    private Attractions attractions;

    @ManyToOne
    @JoinColumn(name = "dessert_id")
    private Dessert dessert;

    @ManyToOne
    @JoinColumn(name = "other_id")
    private Other other;

    @ManyToOne
    @JoinColumn(name = "shopping_id")
    private Shopping shopping;

    @ManyToOne
    @JoinColumn(name = "festivals_id")
    private Festivals festivals;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}