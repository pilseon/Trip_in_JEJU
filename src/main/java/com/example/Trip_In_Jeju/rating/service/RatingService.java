package com.example.Trip_In_Jeju.rating.service;

import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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


    @Value("${custom.fileDirPath}")
    private String fileDirPath;
    @Transactional
    public void saveRating(Long dessertId, Integer score, String comment, String nickname, MultipartFile thumbnail) {
        String thumbnailRelPath = "post/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

        Rating rating = Rating.builder()
                .dessertId(dessertId)
                .score(score)
                .comment(comment)
                .nickname(nickname)
                .thumbnailImg(thumbnailRelPath)  // 파일 경로 저장
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
