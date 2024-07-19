package com.example.Trip_In_Jeju.kategorie.dessert.service;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.repository.DessertRepository;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertRepository dessertRepository;

    @Value("${custom.genFileDirPath}")
    public String genFileDirPath;

    public Page<Dessert> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 8, Sort.by(sorts));

        return dessertRepository.findAll(pageable);
    }
    public void create(String title, String businessHours, String content, String place, String closedDay,
                       String websiteUrl, String phoneNumber, String hashtags, MultipartFile thumbnail) {

        String thumbnailRelPath = "dessert/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        thumbnailFile.mkdirs();

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        Dessert p = Dessert.builder()
                .title(title)
                .businessHours(businessHours)
                .content(content)
                .place(place)
                .thumbnailImg(thumbnailRelPath)
                .closedDay(closedDay)
                .websiteUrl(websiteUrl)
                .phoneNumber(phoneNumber)
                .hashtags(hashtags)
                .likes(0)
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

    public void incrementLikes(Long id) {
        Dessert dessert = getDessert(id);
        dessert.setLikes(dessert.getLikes() + 1);
        dessertRepository.save(dessert);
    }
}