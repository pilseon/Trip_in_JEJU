package com.example.Trip_In_Jeju.kategorie.activity.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.activity.dto.ActivityLocationDto;
import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.repository.ActivityRepository;
import com.example.Trip_In_Jeju.like.entity.Like;
import com.example.Trip_In_Jeju.like.repository.LikeRepository;
import com.example.Trip_In_Jeju.location.dto.LocationRequest;
import com.example.Trip_In_Jeju.location.dto.VisitRequest;
import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
import com.example.Trip_In_Jeju.location.service.VisitRecordService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.rating.repository.RatingRepository;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import com.example.Trip_In_Jeju.scrap.ScrapRepository;
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
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final LikeRepository likeRepository;
    private final RatingService ratingService;
    private final ScrapService scrapService;
    private final ScrapRepository scrapRepository;
    private final RatingRepository ratingRepository;

    private final VisitRecordService visitRecordService;
    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    public Page<Activity> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));

        Page<Activity> paging;
        if ("all".equalsIgnoreCase(subCategory)) {
            paging = activityRepository.findAll(pageable);
        } else {
            paging = activityRepository.findBySubCategory(subCategory, pageable);
        }

        // 각 Activity 엔티티에 대해 평균 별점 설정
        paging.forEach(activity -> {
            double averageRating = ratingService.calculateAverageScore(activity.getId(), "activity");
            activity.setAverageRating(averageRating); // averageRating 필드 설정
        });

        return paging;
    }

    public Page<Activity> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));

        return activityRepository.findAll(pageable);
    }

    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, MultipartFile thumbnail, double latitude, double longitude, String category, String address, String subCategory) {

        // 파일 이름에서 확장자 추출 및 UUID를 이용해 고유 파일 이름 생성
        String extension = "";
        String originalFilename = thumbnail.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String thumbnailRelPath = "activit/" + UUID.randomUUID().toString() + extension;

        // 파일 저장 경로 설정
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        // 디렉토리가 없으면 생성
        if (!thumbnailFile.getParentFile().exists()) {
            thumbnailFile.getParentFile().mkdirs();
        }
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
        location.setAddress(address);
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
                .likes(0)
                .scrapCount(0)
                .category(category)
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

    @Transactional
    public boolean toggleLike(Long id, Member member) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        boolean alreadyLiked = likeRepository.existsByActivityAndMember(activity, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByActivityAndMember(activity, member);
            activity.setLikes(activity.getLikes() - 1);
            activityRepository.save(activity);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setActivity(activity);
            like.setMember(member);
            likeRepository.save(like);
            activity.setLikes(activity.getLikes() + 1);
            activityRepository.save(activity);
            return true; // 좋아요 추가됨
        }
    }


    public void incrementLikes(Long id) {
        Activity activity = getActivity(id);
        activity.setLikes(activity.getLikes() + 1);
        activityRepository.save(activity);
    }

    public Activity findByIdWithAverageRating(Long id, String category) {
        Activity activity = findById(id);
        double averageRating = ratingService.calculateAverageScore(id, category);
        activity.setAverageRating(averageRating);
        return activity;
    }

    public Activity findById(Long id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        Activity activity = optionalActivity.orElseThrow(() -> new RuntimeException("activity not found with id: " + id));
        // 스크랩 수를 업데이트
        activity.setScrapCount(scrapService.getScrapCount(activity));
        return activity;
    }

    public void save(Activity activity) {
        activityRepository.save(activity);
    }

    public Activity getActivityById(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);

        if (activity != null) {
            // 스크랩 수를 업데이트
            int scrapCount = scrapService.getScrapCount(activity);
            activity.setScrapCount(scrapCount);
        }
        return activity;
    }

    public Result findResultById(Long id) {
        Activity activity = findById(id); // 기존의 findById 메서드를 사용
        return new Result(activity.getId(), activity.getTitle(), activity.getPlace(), activity.getThumbnailImg(), activity.getContent());
    }


    @Transactional
    public void deleteActivity(Long activityId) {
        // 먼저 스크랩 테이블에서 관련 데이터를 삭제해요
        scrapRepository.deleteByActivityId(activityId);

        // 좋아요 삭제
        likeRepository.deleteByActivityId(activityId);

        // 리뷰 삭제
        ratingRepository.deleteRatingsByActivityId(activityId);

        activityRepository.deleteById(activityId);

    }

    public List<Activity> getRandomActivities(int limit) {
        List<Activity> allActivity = activityRepository.findAll();
        return getRandomItems(allActivity, limit);
    }

    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }

    public void processActivityLocation(Long memberId, LocationRequest locationRequest) {
        Location userLocation = new Location(locationRequest.getLatitude(), locationRequest.getLongitude());

        List<Activity> activityLocations = activityRepository.findAll();

        for (Activity activity : activityLocations) {
            if (isNearLocation(userLocation, activity.getLocation())) {
                VisitRequest visitRequest = new VisitRequest();
                visitRequest.setMemberId(memberId);
                visitRequest.setActivityId(activity.getId());
                visitRecordService.saveVisitRecord(visitRequest);
            }
        }
    }

    private boolean isNearLocation(Location userLocation, Location activityLocation) {
        // 위치 간의 거리를 계산하고, 근접 여부를 반환합니다.
        // 예: 300미터 이내인지 확인
        double distance = calculateDistance(userLocation, activityLocation);
        return distance <= 300;
    }

    private double calculateDistance(Location loc1, Location loc2) {
        // 지구 반지름 (미터 단위)
        final double R = 6371e3;

        // 위도와 경도를 라디안 단위로 변환
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double deltaLat = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double deltaLon = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());

        // Haversine 공식 적용
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 최종 거리 계산 (미터 단위)
        double distance = R * c;

        return distance;
    }

    public List<ActivityLocationDto> getAllActivityLocations() {

        List<Activity> activitieList = activityRepository.findAll();

        List<ActivityLocationDto> activityLocationDtos = new ArrayList<>();
        for (Activity activity : activitieList) {
            ActivityLocationDto dto = new ActivityLocationDto(
                    activity.getId(),
                    activity.getTitle(),
                    activity.getLocation().getLatitude(),
                    activity.getLocation().getLongitude(),
                    activity.getCategory(),
                    activity.getPlace()
            );
            activityLocationDtos.add(dto);
        }

        // 변환된 DTO 리스트를 반환합니다.
        return activityLocationDtos;
    }
}