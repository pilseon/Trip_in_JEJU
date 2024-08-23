package com.example.Trip_In_Jeju.kategorie.dessert.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.repository.DessertRepository;
import com.example.Trip_In_Jeju.like.entity.Like;
import com.example.Trip_In_Jeju.like.repository.LikeRepository;
import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
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
public class DessertService {
    private final DessertRepository dessertRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final LikeRepository likeRepository;
    private final RatingService ratingService;
    private final ScrapService scrapService;
    private final ScrapRepository scrapRepository;
    private final RatingRepository ratingRepository;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    // 디렉토리 경로를 변경합니다.
    private static final String DESSERT_IMAGE_DIR = "images/dessert/";

    public Page<Dessert> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return dessertRepository.findAll(pageable);
        } else {
            return dessertRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Dessert> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return dessertRepository.findAll(pageable);
    }

    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, MultipartFile thumbnail, double latitude, double longitude,String category, String subCategory) {

        // 이미지를 저장할 디렉토리의 전체 경로를 생성합니다.
        String thumbnailRelPath = DESSERT_IMAGE_DIR + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        // 디렉토리가 없으면 생성합니다.
        thumbnailFile.getParentFile().mkdirs();

        try {
            // 업로드된 파일을 저장합니다.
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

        Dessert p = Dessert.builder()
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

        dessertRepository.save(p);
    }

    public void create2(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                        String websiteUrl, String phoneNumber, double latitude, double longitude, String category, String subCategory) {

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

        Dessert p = Dessert.builder()
                .title(title)
                .calendar(calendar)  // Calendar 엔티티 참조
                .content(content)
                .location(location)
                .place(place)
                .websiteUrl(websiteUrl)
                .phoneNumber(phoneNumber)
                .likes(0)
                .scrapCount(0)
                .category(category)
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        dessertRepository.save(p);
    }


    public Dessert getDessert(Long id) {
        Optional<Dessert> dessert = dessertRepository.findById(id);

        if (dessert.isPresent()) {
            return dessert.get();
        } else {
            throw new RuntimeException("dessert not found");
        }
    }

    public List<Dessert> getList() {
        return dessertRepository.findAll();
    }

    @Transactional
    public boolean toggleLike(Long id, Member member) {
        Dessert dessert = dessertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dessert not found"));

        boolean alreadyLiked = likeRepository.existsByDessertAndMember(dessert, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByDessertAndMember(dessert, member);
            dessert.setLikes(dessert.getLikes() - 1);
            dessertRepository.save(dessert);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setDessert(dessert);
            like.setMember(member);
            likeRepository.save(like);
            dessert.setLikes(dessert.getLikes() + 1);
            dessertRepository.save(dessert);
            return true; // 좋아요 추가됨
        }
    }


    public void incrementLikes(Long id) {
        Dessert dessert = getDessert(id);
        dessert.setLikes(dessert.getLikes() + 1);
        dessertRepository.save(dessert);
    }

    public Dessert findByIdWithAverageRating(Long id, String category) {
        Dessert dessert = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        dessert.setAverageRating(averageRating);
        return dessert;
    }

    public Dessert findById(Long id) {
        Optional<Dessert> optionalDessert = dessertRepository.findById(id);
        Dessert dessert = optionalDessert.orElseThrow(() -> new RuntimeException("dessert not found with id: " + id));
        // 스크랩 수를 업데이트
        dessert.setScrapCount(scrapService.getScrapCount(dessert));
        return dessert;
    }

    public void save(Dessert dessert) {
        dessertRepository.save(dessert);
    }

    public Dessert getDessertById(Long id) {
        Dessert dessert = dessertRepository.findById(id).orElse(null);

        if (dessert != null) {
            // 스크랩 수를 업데이트
            int scrapCount = scrapService.getScrapCount(dessert);
            dessert.setScrapCount(scrapCount);
        }
        return dessert;
    }

    public Result findResultById(Long id) {
        Dessert dessert = findById(id); // 기존의 findById 메서드를 사용
        return new Result(dessert.getId(), dessert.getTitle(), dessert.getPlace(), dessert.getThumbnailImg(), dessert.getContent());
    }

    @Transactional
    public void deleteDessert(Long dessertId) {
        // 먼저 스크랩 테이블에서 관련 데이터를 삭제해요
        scrapRepository.deleteByDessertId(dessertId);

        // 좋아요 삭제
        likeRepository.deleteByDessertId(dessertId);

        // 리뷰 삭제
        ratingRepository.deleteRatingsByDessertId(dessertId);


        dessertRepository.deleteById(dessertId);
    }

    public List<Dessert> getRandomDesserts(int limit) {
        List<Dessert> allDesserts = dessertRepository.findAll();
        return getRandomItems(allDesserts, limit);
    }

    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }

}