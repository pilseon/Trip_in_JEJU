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

    public double calculateAverageScore(Long itemId, String category) {
        List<Rating> ratings = ratingRepository.findByItemIdAndCategory(itemId, category);
        return ratings.stream().mapToDouble(rating -> rating.getScore().doubleValue()).average().orElse(0.0);
    }

    public List<Rating> getAllRatings(Long itemId, String category) {
        return ratingRepository.findByItemIdAndCategory(itemId, category);
    }

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    @Transactional
    public void saveRating(Long itemId, Integer score, String comment, String nickname, MultipartFile thumbnail, String category) {
        String thumbnailRelPath = "rating/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        thumbnailFile.mkdirs();

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Rating rating = Rating.builder()
                .itemId(itemId)
                .score(score)
                .comment(comment)
                .nickname(nickname)
                .thumbnailImg(thumbnailRelPath)
                .category(category)
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

    public List<Rating> getRatings(Long itemId, String category) {
        return ratingRepository.findByItemIdAndCategory(itemId, category);
    }

    public Rating getRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId).orElse(null);
    }


}
