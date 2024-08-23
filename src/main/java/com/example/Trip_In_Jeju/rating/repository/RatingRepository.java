package com.example.Trip_In_Jeju.rating.repository;

import com.example.Trip_In_Jeju.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.dessert.id = :dessertId")
    Double findAverageScoreByDessertId(@Param("dessertId") Long dessertId);

    List<Rating> findByDessertId(Long dessertId);

    void deleteById(Long id);
    List<Rating> findByItemIdAndCategory(Long itemId, String category);
    List<Rating> findByUsername(String username);

    boolean existsByUsernameAndItemIdAndCategory(String username, Long itemId, String category);

    void deleteRatingsByAttractionsId(Long attractionsId);

    void deleteRatingsByFestivalsId(Long festivalsId);

    void deleteRatingsByDessertId(Long dessertId);

    void deleteRatingsByOtherId(Long otherId);

    void deleteRatingsByFoodId(Long foodId);

    void deleteRatingsByActivityId(Long activityId);

    void deleteRatingsByShoppingId(Long shoppingId);
}
