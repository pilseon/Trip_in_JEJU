package com.example.Trip_In_Jeju.scrap;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
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
    private Member member;


}