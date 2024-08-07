package com.example.Trip_In_Jeju.scrap;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String thumbnailImg;

    @ManyToOne
    private Dessert dessert;

    @ManyToOne
    private Food food;

    @ManyToOne
    private Activity activity;

    @ManyToOne
    private Attractions attractions;

    @ManyToOne
    private Other other;

    @ManyToOne
    private Shopping shopping;

    @ManyToOne
    private Member member;




}