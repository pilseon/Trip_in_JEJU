package com.example.Trip_In_Jeju.game.repository;

import com.example.Trip_In_Jeju.game.entity.Game;
import com.example.Trip_In_Jeju.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findTop10ByOrderByScoreDesc();

    Optional<Game> findTopByMemberOrderByScoreDesc(Member member);
}
