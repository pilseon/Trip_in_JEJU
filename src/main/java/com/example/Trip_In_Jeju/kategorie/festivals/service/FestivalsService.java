package com.example.Trip_In_Jeju.kategorie.festivals.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.festivals.dto.FestivalsLocationDto;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.repository.FestivalsRepository;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalsService {
    private final FestivalsRepository festivalsRepository;
    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;
    private final ScrapService scrapService;
    private final ScrapRepository scrapRepository;
    private final RatingRepository ratingRepository;

    private final VisitRecordService visitRecordService;
    @Value("${kakao.api.key}")
    private String apiKey;

    @Value("${custom.fileDirPath}")
    public String genFileDirPath;

    public Page<Festivals> getList(int page, String subCategory) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));

        Page<Festivals> paging;
        if ("all".equalsIgnoreCase(subCategory)) {
            paging = festivalsRepository.findAll(pageable);
        } else {
            paging = festivalsRepository.findBySubCategory(subCategory, pageable);
        }

        // 각 Festivals 엔티티에 대해 평균 별점 설정
        paging.forEach(festivals -> {
            double averageRating = ratingService.calculateAverageScore(festivals.getId(), "festivals");
            festivals.setAverageRating(averageRating); // averageRating 필드 설정
        });

        return paging;
    }

    public Page<Festivals> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));

        return festivalsRepository.findAll(pageable);
    }
    public void create(String title, String periodStart, String periodEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, MultipartFile thumbnail, double latitude, double longitude, String category, String address, String subCategory) {

        // 파일 이름에서 확장자 추출 및 UUID를 이용해 고유 파일 이름 생성
        String extension = "";
        String originalFilename = thumbnail.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String thumbnailRelPath = "festivals/" + UUID.randomUUID().toString() + extension;

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
                .likes(0)
                .scrapCount(0)
                .category(category)
                .subCategory(subCategory) // Ensure subCategory is used if provided
                .build();

        festivalsRepository.save(p);
    }

    public void create2(String title, String periodStart, String periodEnd, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber,  double latitude, double longitude, String subCategory) {



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
        Festivals festivals = festivalsRepository.findById(id).orElse(null);

        if (festivals != null) {
            // 스크랩 수를 업데이트
            int scrapCount = scrapService.getScrapCount(festivals);
            festivals.setScrapCount(scrapCount);
        }
        return festivals;
    }

    public List<Festivals> getAllFestivals() {
        return festivalsRepository.findAll();
    }

    public Result findResultById(Long id) {
        Festivals festivals = findById(id); // 기존의 findById 메서드를 사용
        return new Result(festivals.getId(), festivals.getTitle(), festivals.getPlace(), festivals.getThumbnailImg(), festivals.getContent());
    }

    public List<Festivals> getRandomFestivals(int limit) {

        List<Festivals> allFestivals = festivalsRepository.findAll();

        return getRandomItems(allFestivals, limit);
    }
    private <T> List<T> getRandomItems(List<T> items, int limit) {
        Random rand = new Random();
        Collections.shuffle(items, rand);
        return items.stream().limit(limit).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFestivals(Long festivalsId) {
        // 먼저 스크랩 테이블에서 관련 데이터를 삭제해요
        scrapRepository.deleteByAttractionsId(festivalsId);

        // 좋아요 삭제
        likeRepository.deleteByAttractionsId(festivalsId);

        // 리뷰 삭제
        ratingRepository.deleteRatingsByFestivalsId(festivalsId);

        festivalsRepository.deleteById(festivalsId);
    }

    public void processFestivalsLocation(Long memberId, LocationRequest locationRequest) {
        Location userLocation = new Location(locationRequest.getLatitude(), locationRequest.getLongitude());

        List<Festivals> festivalsLocations = festivalsRepository.findAll();

        for (Festivals festivals : festivalsLocations) {
            if (isNearLocation(userLocation, festivals.getLocation())) {
                VisitRequest visitRequest = new VisitRequest();
                visitRequest.setMemberId(memberId);
                visitRequest.setFestivalsId(festivals.getId());
                visitRecordService.saveVisitRecord(visitRequest);
            }
        }
    }

    private boolean isNearLocation(Location userLocation, Location festivalsLocation) {
        // 위치 간의 거리를 계산하고, 근접 여부를 반환합니다.
        // 예: 300미터 이내인지 확인
        double distance = calculateDistance(userLocation, festivalsLocation);
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

    public List<FestivalsLocationDto> getAllFestivalsLocations() {

        List<Festivals> festivalsList = festivalsRepository.findAll();

        List<FestivalsLocationDto> festivalsLocationDtos = new ArrayList<>();
        for (Festivals festivals : festivalsList) {
            FestivalsLocationDto dto = new FestivalsLocationDto(
                    festivals.getId(),
                    festivals.getTitle(),
                    festivals.getLocation().getLatitude(),
                    festivals.getLocation().getLongitude(),
                    festivals.getCategory(),
                    festivals.getPlace()
            );
            festivalsLocationDtos.add(dto);
        }

        // 변환된 DTO 리스트를 반환합니다.
        return festivalsLocationDtos;
    }
}