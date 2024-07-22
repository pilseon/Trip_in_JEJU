package com.example.Trip_In_Jeju.rating.entity;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "dessert_idd")
    private Dessert dessert;

    private Long dessertId;

    private Integer score;

    private String nickname;
}
