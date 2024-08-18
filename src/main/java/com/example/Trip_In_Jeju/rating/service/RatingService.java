package com.example.Trip_In_Jeju.rating.service;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.repository.MemberRepository;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import com.example.Trip_In_Jeju.rating.entity.Likey;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.repository.LikeyRepository;
import com.example.Trip_In_Jeju.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;
    private final LikeyRepository likeyRepository;
    private final MemberService memberService;

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
    public void saveRating(Long itemId, Integer score, Long ratingId, String comment, String username, MultipartFile thumbnail, String category, String categoryTitle) {
        // 기본 이미지 경로를 null로 설정
        String thumbnailRelPath = null;

        // thumbnail이 null이 아니면 파일을 저장
        if (thumbnail != null && !thumbnail.isEmpty()) {
            thumbnailRelPath = "rating/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

            // 필요한 경우 상위 디렉토리 생성
            thumbnailFile.getParentFile().mkdirs();

            try {
                thumbnail.transferTo(thumbnailFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Rating 객체 생성
        Rating rating = Rating.builder()
                .itemId(itemId)
                .title(categoryTitle)
                .score(score)
                .ratingId(ratingId)
                .comment(comment)
                .username(username)
                .thumbnailImg(thumbnailRelPath)
                .category(category)
                .build();

        // Rating 객체 저장
        ratingRepository.save(rating);
    }



    @Transactional
    public void updateRating(Long ratingId, Integer score, String comment) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new RuntimeException("Rating not found"));
        rating.setScore(score);
        rating.setComment(comment);
        ratingRepository.save(rating);
    }

    public void updateRating2(Long ratingId, Integer score, String comment, MultipartFile thumbnail) {
        Rating rating = getRatingById(ratingId);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }

        rating.setScore(score);
        rating.setComment(comment);

        // 썸네일 파일 처리
        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailRelPath = "rating/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

            thumbnailFile.mkdirs();

            try {
                thumbnail.transferTo(thumbnailFile);
                rating.setThumbnailImg(thumbnailRelPath);  // 새 썸네일 경로 설정
            } catch (IOException e) {
                throw new RuntimeException("Failed to save thumbnail", e);
            }
        }

        ratingRepository.save(rating);
    }


    private String saveThumbnail(MultipartFile thumbnail) throws IOException {
        // 썸네일 파일을 저장하는 로직을 구현
        // 예를 들어, 파일 시스템에 저장하고 그 경로를 반환
        // 여기서 파일 시스템에 저장하는 로직을 구현하세요
        String fileName = thumbnail.getOriginalFilename();
        // 파일 저장 로직 ...
        return "thumbnail/" + fileName; // 예시 경로
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


    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public void likeReview(Long ratingId, Member member) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        if (!likeyRepository.existsByRatingAndMember(rating, member)) {
            Likey likey = Likey.builder()
                    .rating(rating)
                    .member(member)
                    .build();
            likeyRepository.save(likey);
        }
    }

    public void unlikeReview(Long ratingId, Member member) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        Optional<Likey> like = likeyRepository.findByRatingAndMember(rating, member);
        like.ifPresent(likeyRepository::delete);
    }

    public boolean toggleLikey(Long ratingId, Member member) {
        Optional<Rating> ratingOptional = ratingRepository.findById(ratingId);
        if (!ratingOptional.isPresent()) {
            return false;
        }

        Rating rating = ratingOptional.get();

        // 이미 좋아요가 눌려있는지 확인
        if (rating.getLikedMembers().contains(member)) {
            rating.getLikedMembers().remove(member); // 이미 눌려있으면 삭제
            rating.setLikeCount(rating.getLikeCount() - 1);
            ratingRepository.save(rating);
            return false;
        } else {
            rating.getLikedMembers().add(member); // 좋아요 추가
            rating.setLikeCount(rating.getLikeCount() + 1);
            ratingRepository.save(rating);
            return true;
        }
    }

    public int getLikeCount(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        return rating.getLikeCount();
    }

    public List<Rating> getRatingsByMember(Member member) {
        return ratingRepository.findByUsername(member.getUsername());
    }
    public List<Rating> getRatingsByUsername(String username) {

        return ratingRepository.findByUsername(username);
    }


    @Transactional
    public void removeAllRatingsForItem(Food food) {
    }
    @Transactional
    public void removeAllRatingsForItem(Activity activity) {
    }
    @Transactional
    public void removeAllRatingsForItem(Attractions attractions) {
    }
    @Transactional
    public void removeAllRatingsForItem(Dessert dessert) {
    }
    @Transactional
    public void removeAllRatingsForItem(Other other) {
    }
    @Transactional
    public void removeAllRatingsForItem(Shopping shopping) {
    }




    public void deleteRatingsByFoodId(Long foodId) {
    }

    public void deleteRatingsByActivityId(Long activityId) {

    }

    public void deleteRatingsByAttractionsId(Long attractionsId) {
    }

    public void deleteRatingsByDessertId(Long dessertId) {
    }

    public void deleteRatingsByOtherId(Long otherId) {

    }

    public void deleteRatingsByShoppingId(Long shoppingId) {
    }

    public boolean hasUserWrittenReview(String username, Long itemId, String category) {
        return ratingRepository.existsByUsernameAndItemIdAndCategory(username, itemId, category);
    }
}