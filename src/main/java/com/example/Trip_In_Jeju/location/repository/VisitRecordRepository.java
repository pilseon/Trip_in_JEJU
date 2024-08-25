package com.example.Trip_In_Jeju.location.repository;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.location.entity.VisitRecord;
import com.example.Trip_In_Jeju.member.entity.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {
    List<VisitRecord> findByMember(Member member, Sort sort);

    List<VisitRecord> findByMember(Member member);

    boolean existsByMemberIdAndFoodId(Long memberId, Long foodId);

//    List<VisitRecord> findByMemberId(Long memberId);

    boolean existsByMemberAndFood(Member member, Food food);

    boolean existsByMemberAndActivity(Member member, Activity activity);

    boolean existsByMemberAndAttractions(Member member, Attractions attractions);

    boolean existsByMemberAndDessert(Member member, Dessert dessert);

    boolean existsByMemberAndFestivals(Member member, Festivals festivals);

    boolean existsByMemberAndOther(Member member, Other other);

    boolean existsByMemberAndShopping(Member member, Shopping shopping);

    List<VisitRecord> findByMemberAndFoodIsNotNull(Member member);

    List<VisitRecord> findByMemberAndActivityIsNotNull(Member member);

    List<VisitRecord> findByMemberAndAttractionsIsNotNull(Member member);

    List<VisitRecord> findByMemberAndDessertIsNotNull(Member member);

    List<VisitRecord> findByMemberAndFestivalsIsNotNull(Member member);

    List<VisitRecord> findByMemberAndOtherIsNotNull(Member member);

    List<VisitRecord> findByMemberAndShoppingIsNotNull(Member member);

    boolean existsByMemberIdAndActivityId(Long memberId, Long itemId);

    boolean existsByMemberIdAndAttractionsId(Long memberId, Long itemId);

    boolean existsByMemberIdAndDessertId(Long memberId, Long itemId);

    boolean existsByMemberIdAndFestivalsId(Long memberId, Long itemId);

    boolean existsByMemberIdAndOtherId(Long memberId, Long itemId);

    boolean existsByMemberIdAndShoppingId(Long memberId, Long itemId);
}
