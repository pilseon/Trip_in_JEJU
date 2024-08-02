package com.example.Trip_In_Jeju.rating.entity;

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
    private Long dessertId;
    private Integer score;
    private String nickname;
    private String thumbnailImg;
    private Long foodId;
    private String category;
    private Long itemId;

    private Long ratingId;

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
}