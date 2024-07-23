package com.example.Trip_In_Jeju.kategorie.food.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.genFileDirPath}")
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
    public void create(String title, String periodStart, String periodEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String subCategory) {

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
        calendar.setTitle("Period");
        if (periodStart != null && periodEnd != null) {
            calendar.setPeriodStart(LocalDate.parse(periodStart));
            calendar.setPeriodEnd(LocalDate.parse(periodEnd));
        }
        calendarRepository.save(calendar);

        Food p = Food.builder()
                .title(title)
                .calendar(calendar)  // Calendar 엔티티 참조
                .content(content)
                .location(location)
                .place(place)
                .thumbnailImg(thumbnailRelPath)
                .closedDay(closedDay)
                .websiteUrl(websiteUrl)
                .phoneNumber(phoneNumber)
                .hashtags(hashtags)
                .likes(0)
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

    public void incrementLikes(Long id) {
        Food food = getFood(id);
        food.setLikes(food.getLikes() + 1);
        foodRepository.save(food);
    }

    public Food findByIdWithAverageRating(Long id) {
        Food food = findById(id);
        double averageRating = ratingService.calculateAverageRating(id);
        food.setAverageRating(averageRating);
        return food;
    }

    public Food findById(Long id) {
        Optional<Food> optionalFood = foodRepository.findById(id);
        return optionalFood.orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
    }

    public void save(Food food) {
        foodRepository.save(food);
    }

    public Food getFoodById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }
}