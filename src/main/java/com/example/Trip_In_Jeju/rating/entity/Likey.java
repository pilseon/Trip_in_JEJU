package com.example.Trip_In_Jeju.rating.entity;

import com.example.Trip_In_Jeju.member.entity.Member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Likey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Rating rating;

    @ManyToOne
    private Member member;
}