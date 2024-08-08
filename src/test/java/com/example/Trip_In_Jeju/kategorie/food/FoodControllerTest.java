package com.example.Trip_In_Jeju.kategorie.food;

import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Random;

@SpringBootTest
public class FoodControllerTest {

    @Value("${custom.fileDirPath}")
    private String genFileDirPath;

    @Autowired
    private FoodService foodService;

    private static final String[] PLACES = {
            "Jeju City"
    };

//    @Test
//    @DisplayName("한식 데이터 생성")
//    void test1() throws IOException {
//        Random random = new Random();
//
//        for (int i = 1; i <= 5; i++) {
//            String title = String.format("현지표 맛집 추천 %d", i);
//            String businessHoursStart = "08:00";
//            String businessHoursEnd = "22:00";
//            String content = String.format("맛집 %d", i);
//            String place = PLACES[random.nextInt(PLACES.length)];
//            String closedDay = String.format("매주 월요일 휴무");
//            String websiteUrl = "https://i.ibb.co/C2hv4Cp/Kakao-Talk-20240718-114435946.jpg";
//            String hashtags = String.format("#크크%d", i);
//            String phoneNumber = String.format("010-1234-%04d", i);
//
//            // 제주도의 위도 및 경도 범위로 수정
//            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
//            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
//            String subCategory = "korean";
//
//            // 디저트 데이터 생성
//            foodService.create2(
//                    title, businessHoursStart, businessHoursEnd, content, place, closedDay,
//                    websiteUrl, phoneNumber, hashtags, latitude, longitude, category, subCategory
//            );
//        }
    }

