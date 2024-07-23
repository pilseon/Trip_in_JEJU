package com.example.Trip_In_Jeju.kategorie.shopping.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.kategorie.shopping.repository.ShoppingRepository;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingService {
    private final ShoppingRepository shoppingRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.genFileDirPath}")
    public String genFileDirPath;

    public Page<Shopping> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return shoppingRepository.findAll(pageable);
        } else {
            return shoppingRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Shopping> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return shoppingRepository.findAll(pageable);
    }
    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String subCategory) {

        String thumbnailRelPath = "shopping/" + UUID.randomUUID().toString() + ".jpg";
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

        Shopping p = Shopping.builder()
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
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        shoppingRepository.save(p);
    }

    public Shopping getShopping(Long id) {
        Optional<Shopping> shopping = shoppingRepository.findById(id);

        if (shopping.isPresent()) {
            return shopping.get();
        } else {
            throw new RuntimeException("shopping not found");
        }
    }

    public List<Shopping> getList() {
        return shoppingRepository.findAll();
    }

    public void incrementLikes(Long id) {
        Shopping shopping = getShopping(id);
        shopping.setLikes(shopping.getLikes() + 1);
        shoppingRepository.save(shopping);
    }

    public Shopping findByIdWithAverageRating(Long id, String category) {
        Shopping shopping = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        shopping.setAverageRating(averageRating);
        return shopping;
    }

    public Shopping findById(Long id) {
        Optional<Shopping> optionalShopping = shoppingRepository.findById(id);
        return optionalShopping.orElseThrow(() -> new RuntimeException("Shopping not found with id: " + id));
    }

    public void save(Shopping Shopping) {
        shoppingRepository.save(Shopping);
    }

    public Shopping getShoppingById(Long id) {
        return shoppingRepository.findById(id).orElse(null);
    }
}