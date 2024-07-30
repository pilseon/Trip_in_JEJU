package com.example.Trip_In_Jeju.like.repository;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;

import com.example.Trip_In_Jeju.like.entity.Like;
import com.example.Trip_In_Jeju.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.food = :food AND l.member = :member")
    boolean existsByFoodAndMember(@Param("food") Food food, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.food = :food AND l.member = :member")
    void deleteByFoodAndMember(@Param("food") Food food, @Param("member") Member member);
}