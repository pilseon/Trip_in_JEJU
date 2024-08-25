package com.example.Trip_In_Jeju.kategorie.attractions.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.attractions.dto.AttractionsLocationDto;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.repository.AttractionsRepository;
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
public class AttractionsService {
    private final AttractionsRepository attractionsRepository;
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

    public Page<Attractions> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));

        Page<Attractions> paging;
        if ("all".equalsIgnoreCase(subCategory)) {
            paging = attractionsRepository.findAll(pageable);
        } else {
            paging = attractionsRepository.findBySubCategory(subCategory, pageable);
        }

        // 각 Attractions 엔티티에 대해 평균 별점 설정
        paging.forEach(attractions -> {
            double averageRating = ratingService.calculateAverageScore(attractions.getId(), "attractions");
            attractions.setAverageRating(averageRating); // averageRating 필드 설정
        });

        return paging;
    }

    public Page<Attractions> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));

        return attractionsRepository.findAll(pageable);
    }
    public void create(String title, String businessHoursStart, String businessHoursEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, MultipartFile thumbnail, double latitude, double longitude, String address, String category, String subCategory) {

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
        location.setAddress(address);
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

    @Transactional
    public void deleteAttractions(Long attractionsId) {
        // 먼저 스크랩 테이블에서 관련 데이터를 삭제해요
        scrapRepository.deleteByAttractionsId(attractionsId);

        // 좋아요 삭제
        likeRepository.deleteByAttractionsId(attractionsId);

        // 리뷰 삭제
        ratingRepository.deleteRatingsByAttractionsId(attractionsId);

        attractionsRepository.deleteById(attractionsId);

    }

    public List<Attractions> getRandomAttractions(int limit) {
        List<Attractions> allAttractions = attractionsRepository.findAll();
        return getRandomItems(allAttractions, limit);
    }

    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }

    public void processAttractionsLocation(Long memberId, LocationRequest locationRequest) {
        Location userLocation = new Location(locationRequest.getLatitude(), locationRequest.getLongitude());

        List<Attractions> attractionsLocations = attractionsRepository.findAll();

        for (Attractions attractions : attractionsLocations) {
            if (isNearLocation(userLocation, attractions.getLocation())) {
                VisitRequest visitRequest = new VisitRequest();
                visitRequest.setMemberId(memberId);
                visitRequest.setAttractionsId(attractions.getId());
                visitRecordService.saveVisitRecord(visitRequest);
            }
        }
    }

    private boolean isNearLocation(Location userLocation, Location attractionsLocation) {
        // 위치 간의 거리를 계산하고, 근접 여부를 반환합니다.
        // 예: 300미터 이내인지 확인
        double distance = calculateDistance(userLocation, attractionsLocation);
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

    public List<AttractionsLocationDto> getAllAttractionsLocations() {

        List<Attractions> attractionsList = attractionsRepository.findAll();

        List<AttractionsLocationDto> attractionsLocationDtos = new ArrayList<>();
        for (Attractions attractions : attractionsList) {
            AttractionsLocationDto dto = new AttractionsLocationDto(
                    attractions.getId(),
                    attractions.getTitle(),
                    attractions.getLocation().getLatitude(),
                    attractions.getLocation().getLongitude(),
                    attractions.getCategory(),
                    attractions.getPlace()
            );
            attractionsLocationDtos.add(dto);
        }

        // 변환된 DTO 리스트를 반환합니다.
        return attractionsLocationDtos;
    }
}