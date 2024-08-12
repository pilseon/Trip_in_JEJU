package com.example.Trip_In_Jeju.kategorie.attractions.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.repository.AttractionsRepository;
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
public class AttractionsService {
    private final AttractionsRepository attractionsRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final LikeRepository likeRepository;
    private final RatingService ratingService;
    private final ScrapService scrapService;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    public Page<Attractions> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return attractionsRepository.findAll(pageable);
        } else {
            return attractionsRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Attractions> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return attractionsRepository.findAll(pageable);
    }
    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String category, String subCategory) {

        String thumbnailRelPath = "attractions/" + UUID.randomUUID().toString() + ".jpg";
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

        Attractions p = Attractions.builder()
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

        attractionsRepository.save(p);
    }

    public Attractions getAttractions(Long id) {
        Optional<Attractions> attractions = attractionsRepository.findById(id);

        if (attractions.isPresent()) {
            return attractions.get();
        } else {
            throw new RuntimeException("attractions not found");
        }
    }

    public List<Attractions> getList() {
        return attractionsRepository.findAll();
    }

    @Transactional
    public boolean toggleLike(Long id, Member member) {
        Attractions attractions = attractionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attractions not found"));

        boolean alreadyLiked = likeRepository.existsByAttractionsAndMember(attractions, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByAttractionsAndMember(attractions, member);
            attractions.setLikes(attractions.getLikes() - 1);
            attractionsRepository.save(attractions);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setAttractions(attractions);
            like.setMember(member);
            likeRepository.save(like);
            attractions.setLikes(attractions.getLikes() + 1);
            attractionsRepository.save(attractions);
            return true; // 좋아요 추가됨
        }
    }


    public void incrementLikes(Long id) {
        Attractions attractions = getAttractions(id);
        attractions.setLikes(attractions.getLikes() + 1);
        attractionsRepository.save(attractions);
    }

    public Attractions findByIdWithAverageRating(Long id, String category) {
        Attractions attractions = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        attractions.setAverageRating(averageRating);
        return attractions;
    }

    public Attractions findById(Long id) {
        Optional<Attractions> optionalAttractions = attractionsRepository.findById(id);
        Attractions attractions = optionalAttractions.orElseThrow(() -> new RuntimeException("attractions not found with id: " + id));
        // 스크랩 수를 업데이트
        attractions.setScrapCount(scrapService.getScrapCount(attractions));
        return attractions;
    }

    public void save(Attractions attractions) {
        attractionsRepository.save(attractions);
    }

    public Attractions getAttractionsById(Long id) {
        Attractions attractions = attractionsRepository.findById(id).orElse(null);

        if (attractions != null) {
            // 스크랩 수를 업데이트
            int scrapCount = scrapService.getScrapCount(attractions);
            attractions.setScrapCount(scrapCount);
        }
        return attractions;
    }

    public Result findResultById(Long id) {
        Attractions attractions = findById(id); // 기존의 findById 메서드를 사용
        return new Result(attractions.getId(), attractions.getTitle(), attractions.getPlace(), attractions.getThumbnailImg(), attractions.getContent());
    }

    // 랜덤으로 10개의 Food 항목을 가져오는 메서드
    public List<Attractions> getRandomAttractions(int limit) {
        List<Attractions> allAttractions = attractionsRepository.findAll();
        return getRandomItems(allAttractions, limit);
    }

    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }
}