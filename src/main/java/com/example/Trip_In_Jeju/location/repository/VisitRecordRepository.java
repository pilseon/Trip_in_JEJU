package com.example.Trip_In_Jeju.location.repository;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.location.entity.VisitRecord;
import com.example.Trip_In_Jeju.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {
    List<VisitRecord> findByMember(Member member);

    boolean existsByMemberAndFood(Member member, Food food);

    List<VisitRecord> findByMemberId(Long memberId);

    boolean existsByMemberIdAndFoodId(Long memberId, Long foodId);
}
