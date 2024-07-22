package com.example.Trip_In_Jeju.kategorie.food.entity;

import com.example.Trip_In_Jeju.location.entity.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String title;
    private String content;
    private int category;
    private Long hit;
    private String thumbnail;
    private String closedDay;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String thumbnailImg;

    private String businessHours;
    private String phoneNumber;
    private String websiteUrl;
    private String hashtags;
}