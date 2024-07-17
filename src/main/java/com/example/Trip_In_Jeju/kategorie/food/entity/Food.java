package com.example.Trip_In_Jeju.kategorie.food.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Food {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String title;
    private String content;
    private int category;
    private Long hit;
    private String place;
    private String thumbnail;
    private String closedDay;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;


}
