package com.example.Trip_In_Jeju.kategorie.dessert;

import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

@SpringBootTest
public class DessertControllerTest {

    @Value("${custom.fileDirPath}")
    private String genFileDirPath;

    @Autowired
    private DessertService dessertService;

    private static final String[] PLACES = {
            "Jeju City"
    };

    @Test
    @DisplayName("카페 데이터 생성")
    void test1() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("디저트 맛집 추천 %d", i);
            String businessHoursStart = "08:00";
            String businessHoursEnd = "22:00";
            String content = String.format("카페 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = String.format("매주 월요일 휴무");
            String websiteUrl = "https://i.ibb.co/C2hv4Cp/Kakao-Talk-20240718-114435946.jpg";
            String phoneNumber = String.format("010-1234-%04d", i);

            // 제주도의 위도 및 경도 범위로 수정
            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String category = "디저트";
            String subCategory = "카페";

            // 디저트 데이터 생성
            dessertService.create2(
                    title, businessHoursStart, businessHoursEnd, content, place, closedDay,
                    websiteUrl, phoneNumber, latitude, longitude, category, subCategory
            );
        }
    }

    @Test
    @DisplayName("베이커리 데이터 생성")
    void test2() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("베이커리 맛집 추천 %d", i);
            String businessHoursStart = "08:00";
            String businessHoursEnd = "22:00";
            String content = String.format("카페 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = String.format("매주 월요일 휴무");
            String websiteUrl = "https://i.ibb.co/C2hv4Cp/Kakao-Talk-20240718-114435946.jpg";
            String phoneNumber = String.format("010-1234-%04d", i);

            // 제주도의 위도 및 경도 범위로 수정
            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String category = "디저트";
            String subCategory = "베이커리";

            // 디저트 데이터 생성
            dessertService.create2(
                    title, businessHoursStart, businessHoursEnd, content, place, closedDay,
                    websiteUrl, phoneNumber, latitude, longitude, category, subCategory
            );
        }
    }

    @Test
    @DisplayName("아이스크림 데이터 생성")
    void test3() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("아이스크림 맛집 추천 %d", i);
            String businessHoursStart = "08:00";
            String businessHoursEnd = "22:00";
            String content = String.format("아이스크림 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = String.format("매주 월요일 휴무");
            String websiteUrl = "https://i.ibb.co/C2hv4Cp/Kakao-Talk-20240718-114435946.jpg";
            String phoneNumber = String.format("010-1234-%04d", i);

            // 제주도의 위도 및 경도 범위로 수정
            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String category = "디저트";
            String subCategory = "아이스크림";

            // 디저트 데이터 생성
            dessertService.create2(
                    title, businessHoursStart, businessHoursEnd, content, place, closedDay,
                    websiteUrl, phoneNumber, latitude, longitude, category, subCategory
            );
        }
    }

    @Test
    @DisplayName("기타 데이터 생성")
    void test4() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("기타 맛집 추천 %d", i);
            String businessHoursStart = "08:00";
            String businessHoursEnd = "22:00";
            String content = String.format("기타 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = String.format("매주 월요일 휴무");
            String websiteUrl = "https://i.ibb.co/C2hv4Cp/Kakao-Talk-20240718-114435946.jpg";
            String phoneNumber = String.format("010-1234-%04d", i);

            // 제주도의 위도 및 경도 범위로 수정
            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String category = "디저트";
            String subCategory = "기타";

            // 디저트 데이터 생성
            dessertService.create2(
                    title, businessHoursStart, businessHoursEnd, content, place, closedDay,
                    websiteUrl, phoneNumber, latitude, longitude, category, subCategory
            );
        }
    }
}
