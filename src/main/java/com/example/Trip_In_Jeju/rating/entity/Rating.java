package com.example.Trip_In_Jeju.rating.entity;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Integer score;
    private String nickname;
    private String thumbnailImg;
    private String category;
    private Long itemId;
    private String username;
    private String title;

    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likey> likey;

    public int getNumberOfLikes() {
        return likey.size();
    }

    @ManyToMany
    @JoinTable(
            name = "rating_like",
            joinColumns = @JoinColumn(name = "rating_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> likedMembers = new HashSet<>();
    private int likeCount;

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
}