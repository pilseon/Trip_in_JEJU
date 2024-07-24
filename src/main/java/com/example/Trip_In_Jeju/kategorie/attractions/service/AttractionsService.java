package com.example.Trip_In_Jeju.kategorie.attractions.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.repository.AttractionsRepository;
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
public class AttractionsService {
    private final AttractionsRepository attractionsRepository;
    private final LocationRepository locationRepository;
    private final CalendarRepository calendarRepository;
    private final RatingService ratingService;

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
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail, double latitude, double longitude, String subCategory) {

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
        return optionalAttractions.orElseThrow(() -> new RuntimeException("Attractions not found with id: " + id));
    }

    public void save(Attractions attractions) {
        attractionsRepository.save(attractions);
    }

    public Attractions getAttractionsById(Long id) {
        return attractionsRepository.findById(id).orElse(null);
    }
}