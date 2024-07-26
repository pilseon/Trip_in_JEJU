package com.example.Trip_In_Jeju.rating.repository;

import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.rating.entity.Likey;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeyRepository extends JpaRepository<Likey, Long> {

    boolean existsByRatingAndMember(Rating rating, Member member);

    Optional<Likey> findByRatingAndMember(Rating rating, Member member);
}
