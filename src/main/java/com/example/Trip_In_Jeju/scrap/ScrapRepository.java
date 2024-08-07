package com.example.Trip_In_Jeju.scrap;


import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    // Food 관련 메서드
    Optional<Scrap> findByFoodAndMember(Food food, Member member);
    void deleteByFoodAndMember(Food food, Member member);
    int countByFood(Food food);
    List<Scrap> findByMember(Member member);

    // Dessert 관련 메서드
    Optional<Scrap> findByDessertAndMember(Dessert dessert, Member member);
    void deleteByDessertAndMember(Dessert dessert, Member member);
    int countByDessert(Dessert dessert);
}