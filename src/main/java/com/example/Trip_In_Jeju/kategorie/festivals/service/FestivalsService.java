package com.example.Trip_In_Jeju.kategorie.festivals.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.repository.FestivalsRepository;
import com.example.Trip_In_Jeju.like.entity.Like;
import com.example.Trip_In_Jeju.like.repository.LikeRepository;
import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.rating.service.RatingService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FestivalsService {
    private final FestivalsRepository festivalsRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;

    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    public Page<Festivals> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return festivalsRepository.findAll(pageable);
        } else {
            return festivalsRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Festivals> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return festivalsRepository.findAll(pageable);
    }
    public void create(String title, String periodStart, String periodEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String subCategory) {

        String thumbnailRelPath = "festivals/" + UUID.randomUUID().toString() + ".jpg";
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

        Festivals p = Festivals.builder()
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

        festivalsRepository.save(p);
    }

    public void create2(String title, String periodStart, String periodEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags,  double latitude, double longitude, String subCategory) {



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

        Festivals p = Festivals.builder()
                .title(title)
                .calendar(calendar)  // Calendar 엔티티 참조
                .content(content)
                .location(location)
                .place(place)
                .closedDay(closedDay)
                .websiteUrl(websiteUrl)
                .phoneNumber(phoneNumber)
                .hashtags(hashtags)
                .likes(0)
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        festivalsRepository.save(p);
    }

    public Festivals getFestivals(Long id) {
        Optional<Festivals> festivals = festivalsRepository.findById(id);

        if (festivals.isPresent()) {
            return festivals.get();
        } else {
            throw new RuntimeException("festivals not found");
        }
    }

    public List<Festivals> getList() {
        return festivalsRepository.findAll();
    }

    @Transactional
    public boolean toggleLike(Long id, Member member) {
        Festivals festivals = festivalsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Festivals not found"));

        boolean alreadyLiked = likeRepository.existsByFestivalsAndMember(festivals, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByFestivalsAndMember(festivals, member);
            festivals.setLikes(festivals.getLikes() - 1);
            festivalsRepository.save(festivals);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setFestivals(festivals);
            like.setMember(member);
            likeRepository.save(like);
            festivals.setLikes(festivals.getLikes() + 1);
            festivalsRepository.save(festivals);
            return true; // 좋아요 추가됨
        }
    }


    public void incrementLikes(Long id) {
        Festivals festivals = getFestivals(id);
        festivals.setLikes(festivals.getLikes() + 1);
        festivalsRepository.save(festivals);
    }

    public Festivals findByIdWithAverageRating(Long id, String category) {
        Festivals festivals = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        festivals.setAverageRating(averageRating);
        return festivals;
    }

    public Festivals findById(Long id) {
        Optional<Festivals> optionalFestivals = festivalsRepository.findById(id);
        return optionalFestivals.orElseThrow(() -> new RuntimeException("Festivals not found with id: " + id));
    }

    public void save(Festivals festivals) {
        festivalsRepository.save(festivals);
    }

    public Festivals getFestivalsById(Long id) {
        return festivalsRepository.findById(id).orElse(null);
    }
}