package com.example.Trip_In_Jeju.rating.repository;

import com.example.Trip_In_Jeju.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.dessertId = ?1")
    Double findAverageScoreByDessertId(Long dessertId);

    List<Rating> findByDessertId(Long dessertId);

    void deleteById(Long id);
}
