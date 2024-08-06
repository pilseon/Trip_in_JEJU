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
import com.example.Trip_In_Jeju.rating.service.RatingService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertRepository dessertRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final LikeRepository likeRepository;
    private final RatingService ratingService;

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
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String subCategory) {

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
                .hashtags(hashtags)
                .likes(0)
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        dessertRepository.save(p);
    }

    public void create2(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                        String websiteUrl, String phoneNumber, String hashtags, double latitude, double longitude, String subCategory) {

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
                .hashtags(hashtags)
                .likes(0)
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

    @Transactional
    public boolean toggleLike2(Long id, Member member) {
        Dessert dessert = dessertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dessert not found"));

        boolean alreadyLiked = likeRepository.existsByDessertAndMember(dessert, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByDessertAndMember(dessert, member);
            dessert.setLikey(dessert.getLikey() - 1);
            dessertRepository.save(dessert);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setDessert(dessert);
            like.setMember(member);
            likeRepository.save(like);
            dessert.setLikey(dessert.getLikey() + 1);
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
        return optionalDessert.orElseThrow(() -> new RuntimeException("Dessert not found with id: " + id));
    }

    public void save(Dessert dessert) {
        dessertRepository.save(dessert);
    }

    public Dessert getDessertById(Long id) {
        return dessertRepository.findById(id).orElse(null);
    }

    public Result findResultById(Long id) {
        Dessert dessert = findById(id); // 기존의 findById 메서드를 사용
        return new Result(dessert.getId(), dessert.getTitle(), dessert.getPlace(), dessert.getThumbnailImg(), dessert.getContent());
    }
}