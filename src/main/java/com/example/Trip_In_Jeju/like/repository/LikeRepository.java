package com.example.Trip_In_Jeju.like.repository;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;

import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
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

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.activity = :activity AND l.member = :member")
    boolean existsByActivityAndMember(@Param("activity") Activity activity, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.activity = :activity AND l.member = :member")
    void deleteByActivityAndMember(@Param("activity") Activity activity, @Param("member") Member member);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.attractions = :attractions AND l.member = :member")
    boolean existsByAttractionsAndMember(@Param("attractions") Attractions attractions, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.attractions = :attractions AND l.member = :member")
    void deleteByAttractionsAndMember(@Param("attractions") Attractions attractions, @Param("member") Member member);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.dessert = :dessert AND l.member = :member")
    boolean existsByDessertAndMember(@Param("dessert") Dessert dessert, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.dessert = :dessert AND l.member = :member")
    void deleteByDessertAndMember(@Param("dessert") Dessert dessert, @Param("member") Member member);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.other = :other AND l.member = :member")
    boolean existsByOtherAndMember(@Param("other") Other other, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.other = :other AND l.member = :member")
    void deleteByOtherAndMember(@Param("other") Other other, @Param("member") Member member);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.shopping = :shopping AND l.member = :member")
    boolean existsByShoppingAndMember(@Param("shopping") Shopping shopping, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.shopping = :shopping AND l.member = :member")
    void deleteByShoppingAndMember(@Param("shopping") Shopping shopping, @Param("member") Member member);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.festivals = :festivals AND l.member = :member")
    boolean existsByFestivalsAndMember(@Param("festivals") Festivals festivals, @Param("member") Member member);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.festivals = :festivals AND l.member = :member")
    void deleteByFestivalsAndMember(@Param("festivals") Festivals festivals, @Param("member") Member member);

    
    
    void deleteByFoodId(Long foodId);

    void deleteByActivityId(Long activityId);

    void deleteByAttractionsId(Long attractionsId);

    void deleteByDessertId(Long dessertId);

    void deleteByOtherId(Long otherId);

    void deleteByShoppingId(Long shoppingId);


}