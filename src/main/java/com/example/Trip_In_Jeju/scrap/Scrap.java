package com.example.Trip_In_Jeju.scrap;

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


@Entity
@Getter
@Setter
public class Scrap {

    public boolean isEmpty() {
        return dessert == null && food == null && activity == null
                && attractions == null && other == null && shopping == null;
    }

    public Scrap() {
        this.dessert = null;
        this.food = null;
        this.activity = null;
        this.attractions = null;
        this.other = null;
        this.shopping = null;
        this.festivals = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String thumbnailImg;
    private String category;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "festivals_id") // 외래키 이름 설정
    private Festivals festivals;

    @ManyToOne
    private Member member;

    private String periodStart;

    private String periodEnd;




}