package com.example.Trip_In_Jeju.kategorie.activity.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.repository.ActivityRepository;
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
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.genFileDirPath}")
    public String genFileDirPath;

    public Page<Activity> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return activityRepository.findAll(pageable);
        } else {
            return activityRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Activity> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return activityRepository.findAll(pageable);
    }
    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String subCategory) {

        String thumbnailRelPath = "activity/" + UUID.randomUUID().toString() + ".jpg";
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

        Activity p = Activity.builder()
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

        activityRepository.save(p);
    }

    public Activity getActivity(Long id) {
        Optional<Activity> activity = activityRepository.findById(id);

        if (activity.isPresent()) {
            return activity.get();
        } else {
            throw new RuntimeException("activity not found");
        }
    }

    public List<Activity> getList() {
        return activityRepository.findAll();
    }

    public void incrementLikes(Long id) {
        Activity activity = getActivity(id);
        activity.setLikes(activity.getLikes() + 1);
        activityRepository.save(activity);
    }

    public Activity findByIdWithAverageRating(Long id, String category) {
        Activity activity = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        activity.setAverageRating(averageRating);
        return activity;
    }

    public Activity findById(Long id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        return optionalActivity.orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
    }

    public void save(Activity activity) {
        activityRepository.save(activity);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }
}