package com.example.Trip_In_Jeju.like.entity;


import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`like`")  // 테이블 이름을 명시적으로 지정
@Getter
@Setter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}