package com.example.Trip_In_Jeju.rating.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private Long ratingId;

    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likey> likes;

    public int getNumberOfLikes() {
        return likes.size();
    }
}