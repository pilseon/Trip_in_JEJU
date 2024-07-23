package com.example.Trip_In_Jeju.rating.entity;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value;  // 별점 값
    private String comment;  // 리뷰 댓글



    private Long dessertId;

    private Integer score;

    private String nickname;

    private String thumbnailImg;



    private Long foodId;


    private String category;
    private Long itemId;
}
