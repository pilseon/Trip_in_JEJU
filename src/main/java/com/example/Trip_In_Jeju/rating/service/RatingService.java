package com.example.Trip_In_Jeju.rating.service;

import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public double calculateAverageScore(Long dessertId) {
        List<Rating> ratings = ratingRepository.findByDessertId(dessertId);
        return ratings.stream().mapToDouble(rating -> rating.getScore().doubleValue()).average().orElse(0.0);
    }

    public List<Rating> getAllRatings(Long dessertId) {
        return ratingRepository.findByDessertId(dessertId);
    }
    public double calculateAverageRating(Long dessertId) {
        return calculateAverageScore(dessertId);
    }

    @Transactional
    public void saveRating(Long dessertId, Integer score, String comment, String nickname) {
        Rating rating = Rating.builder()
                .dessertId(dessertId)
                .score(score)
                .comment(comment)
                .nickname(nickname)
                .build();
        ratingRepository.save(rating);
    }

    @Transactional
    public void updateRating(Long ratingId, Integer score, String comment) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new RuntimeException("Rating not found"));
        rating.setScore(score);
        rating.setComment(comment);
        ratingRepository.save(rating);
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    public List<Rating> getRatings(Long dessertId) {
        return ratingRepository.findByDessertId(dessertId);
    }

    public Rating getRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId).orElse(null);
    }
}
