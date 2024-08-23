package com.example.Trip_In_Jeju.kategorie.other.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.other.repository.OtherRepository;
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
public class OtherService {
    private final OtherRepository otherRepository;
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

    public Page<Other> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        if ("all".equalsIgnoreCase(subCategory)) {
            return otherRepository.findAll(pageable);
        } else {
            return otherRepository.findBySubCategory(subCategory, pageable);
        }
    }

    public Page<Other> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return otherRepository.findAll(pageable);
    }
    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, MultipartFile thumbnail, double latitude, double longitude, String category, String subCategory) {

        String thumbnailRelPath = "other/" + UUID.randomUUID().toString() + ".jpg";
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

        Other p = Other.builder()
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

        otherRepository.save(p);
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

        Other p = Other.builder()
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

        otherRepository.save(p);
    }


    public Other getOther(Long id) {
        Optional<Other> other = otherRepository.findById(id);

        if (other.isPresent()) {
            return other.get();
        } else {
            throw new RuntimeException("other not found");
        }
    }

    public List<Other> getList() {
        return otherRepository.findAll();
    }

    @Transactional
    public boolean toggleLike(Long id, Member member) {
        Other other = otherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Other not found"));

        boolean alreadyLiked = likeRepository.existsByOtherAndMember(other, member);
        if (alreadyLiked) {
            // 이미 좋아요를 눌렀다면 좋아요 취소
            likeRepository.deleteByOtherAndMember(other, member);
            other.setLikes(other.getLikes() - 1);
            otherRepository.save(other);
            return false; // 좋아요 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setOther(other);
            like.setMember(member);
            likeRepository.save(like);
            other.setLikes(other.getLikes() + 1);
            otherRepository.save(other);
            return true; // 좋아요 추가됨
        }
    }


    public void incrementLikes(Long id) {
        Other other = getOther(id);
        other.setLikes(other.getLikes() + 1);
        otherRepository.save(other);
    }

    public Other findByIdWithAverageRating(Long id, String category) {
        Other other = findById(id);
        double averageRating = ratingService.calculateAverageScore(id,category);
        other.setAverageRating(averageRating);
        return other;
    }

    public Other findById(Long id) {
        Optional<Other> optionalOther = otherRepository.findById(id);
        Other other = optionalOther.orElseThrow(() -> new RuntimeException("other not found with id: " + id));
        // 스크랩 수를 업데이트
        other.setScrapCount(scrapService.getScrapCount(other));
        return other;
    }

    public void save(Other other) {
        otherRepository.save(other);
    }

    public Other getOtherById(Long id) {
        Other other = otherRepository.findById(id).orElse(null);

        if (other != null) {
            // 스크랩 수를 업데이트
            int scrapCount = scrapService.getScrapCount(other);
            other.setScrapCount(scrapCount);
        }
        return other;
    }

    public Result findResultById(Long id) {
        Other other = findById(id); // 기존의 findById 메서드를 사용
        return new Result(other.getId(), other.getTitle(), other.getPlace(), other.getThumbnailImg(), other.getContent());
    }

    @Transactional
    public void deleteOther(Long otherId) {
        // 먼저 스크랩 테이블에서 관련 데이터를 삭제해요
        scrapRepository.deleteByOtherId(otherId);

        // 좋아요 삭제
        likeRepository.deleteByOtherId(otherId);

        // 리뷰 삭제
        ratingRepository.deleteRatingsByOtherId(otherId);


        otherRepository.deleteById(otherId);
    }

    public List<Other> getRandomOthers(int limit) {
        List<Other> allOthers = otherRepository.findAll();
        return getRandomItems(allOthers, limit);
    }

    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }


}