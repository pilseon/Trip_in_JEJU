package com.example.Trip_In_Jeju.kategorie.food.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
import com.example.Trip_In_Jeju.like.entity.Like;
import com.example.Trip_In_Jeju.like.repository.LikeRepository;
import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import com.example.Trip_In_Jeju.scrap.ScrapService;
import com.example.Trip_In_Jeju.search.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;
    private final ScrapService scrapService; // 추가된 의존성


    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    public Page<Food> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return foodRepository.findAll(pageable);
        } else {
            return foodRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Food> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return foodRepository.findAll(pageable);
    }
    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String category, String subCategory) {

        String thumbnailRelPath = "food/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        thumbnailFile.mkdirs();

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Location 엔티티 생성 및 저장
        Location location = new Location();
        location.setName(place);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location = locationRepository.save(location);

        Calendar calendar = new Calendar();
        calendar.setTitle(title);
        calendar.setBusinessHoursStart(LocalTime.parse(businessHoursStart));
        calendar.setBusinessHoursEnd(LocalTime.parse(businessHoursEnd));
        calendar.setClosedDay(closedDay); // 휴무일 설정
        calendarRepository.save(calendar);

        Food p = Food.builder()
                .title(title)
                .calendar(calendar)  // Calendar 엔티티 참조
                .content(content)
                .location(location)
                .place(place)
                .thumbnailImg(thumbnailRelPath)
                .websiteUrl(websiteUrl)
                .phoneNumber(phoneNumber)
                .hashtags(hashtags)
                .likes(0)
                .scrapCount(0)
                .category(category)
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        foodRepository.save(p);
    }

    public void create2(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                        String websiteUrl, String phoneNumber, String hashtags, double latitude, double longitude, String category, String subCategory) {



        // Location 엔티티 생성 및 저장
        Location location = new Location();
        location.setName(place);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location = locationRepository.save(location);

        Calendar calendar = new Calendar();
        calendar.setTitle(title);
        calendar.setBusinessHoursStart(LocalTime.parse(businessHoursStart));
        calendar.setBusinessHoursEnd(LocalTime.parse(businessHoursEnd));
        calendar.setClosedDay(closedDay); // 휴무일 설정
        calendarRepository.save(calendar);

        Food p = Food.builder()
                .title(title)
                .calendar(calendar)  // Calendar 엔티티 참조
                .content(content)
                .location(location)
                .place(place)
                .websiteUrl(websiteUrl)
                .phoneNumber(phoneNumber)
                .hashtags(hashtags)
                .likes(0)
                .scrapCount(0)
                .category(category)
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        foodRepository.save(p);
    }


    public Food getFood(Long id) {
        Optional<Food> food = foodRepository.findById(id);

        if (food.isPresent()) {
            return food.get();
        } else {
            throw new RuntimeException("food not found");
        }
    }

    public List<Food> getList() {
        return foodRepository.findAll();
    }

    @Transactional
    public boolean toggleLike(Long id, Member member) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        boolean alreadyLiked = likeRepository.existsByFoodAndMember(food, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByFoodAndMember(food, member);
            food.setLikes(food.getLikes() - 1);
            foodRepository.save(food);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setFood(food);
            like.setMember(member);
            likeRepository.save(like);
            food.setLikes(food.getLikes() + 1);
            foodRepository.save(food);
            return true; // 좋아요 추가됨
        }
    }


    public void incrementLikes(Long id) {
        Food food = getFood(id);
        food.setLikes(food.getLikes() + 1);
        foodRepository.save(food);
    }

    public Food findByIdWithAverageRating(Long id, String category) {
        Food food = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        food.setAverageRating(averageRating);
        return food;
    }

    public Food findById(Long id) {
        Optional<Food> optionalFood = foodRepository.findById(id);
        Food food = optionalFood.orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
        // 스크랩 수를 업데이트
        food.setScrapCount(scrapService.getScrapCount(food));
        return food;
    }

    public Result findResultById(Long id) {
        Food food = findById(id); // 기존의 findById 메서드를 사용
        return new Result(food.getId(), food.getTitle(), food.getPlace(), food.getThumbnailImg(), food.getContent());
    }

    public void save(Food food) {
        foodRepository.save(food);
    }

    public Food getFoodById(Long id) {
        Food food = foodRepository.findById(id).orElse(null);
        if (food != null) {
            // 스크랩 수를 업데이트
            int scrapCount = scrapService.getScrapCount(food);
            food.setScrapCount(scrapCount);
        }
        return food;
    }

    // 랜덤으로 10개의 Food 항목을 가져오는 메서드
    public List<Food> getRandomFoods(int limit) {
        List<Food> allFoods = foodRepository.findAll();
        return getRandomItems(allFoods, limit);
    }

    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }
}