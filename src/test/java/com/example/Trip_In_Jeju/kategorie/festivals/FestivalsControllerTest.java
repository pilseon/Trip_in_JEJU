package com.example.Trip_In_Jeju.kategorie.festivals;

import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@SpringBootTest
public class FestivalsControllerTest {

    @Value("${custom.fileDirPath}")
    private String genFileDirPath;

    @Autowired
    private FestivalsService festivalsService;

    private static final String[] PLACES = {
            "Jeju City"
    };

    @Test
    @DisplayName("계절 축제 데이터 생성")
    void test1() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("계절 축제 %d", i);
            String content = String.format("계절 축제 내용 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = "매주 월요일 휴무";
            String websiteUrl = "https://example.com";
            String hashtags = String.format("#축제%d", i);
            String phoneNumber = String.format("010-1234-%04d", i);

            LocalDate startDate = LocalDate.of(2024, 8, 1);
            LocalDate endDate = LocalDate.of(2024, 8, 31);
            long randomDays = random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);
            LocalDate periodStart = startDate.plusDays(randomDays);
            LocalDate periodEnd = periodStart.plusDays(random.nextInt(10)); // Ensure end date is after start date

            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String subCategory = "계절";

            festivalsService.create2(
                    title, periodStart.toString(), periodEnd.toString(), content, place, closedDay,
                    websiteUrl, phoneNumber, hashtags, latitude, longitude, subCategory
            );
        }
    }

    @Test
    @DisplayName("음악/예술 데이터 생성")
    void test2() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("음악/예술 축제 %d", i);
            String content = String.format("음악/예술 축제 내용 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = "매주 월요일 휴무";
            String websiteUrl = "https://example.com";
            String hashtags = String.format("#축제%d", i);
            String phoneNumber = String.format("010-1234-%04d", i);

            LocalDate startDate = LocalDate.of(2024, 8, 1);
            LocalDate endDate = LocalDate.of(2024, 8, 31);
            long randomDays = random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);
            LocalDate periodStart = startDate.plusDays(randomDays);
            LocalDate periodEnd = periodStart.plusDays(random.nextInt(10)); // Ensure end date is after start date

            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String subCategory = "음악/예술";

            festivalsService.create2(
                    title, periodStart.toString(), periodEnd.toString(), content, place, closedDay,
                    websiteUrl, phoneNumber, hashtags, latitude, longitude, subCategory
            );
        }
    }

    @Test
    @DisplayName("전통 축제 데이터 생성")
    void test3() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("전통 축제 %d", i);
            String content = String.format("전통 축제 내용 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = "매주 월요일 휴무";
            String websiteUrl = "https://example.com";
            String hashtags = String.format("#축제%d", i);
            String phoneNumber = String.format("010-1234-%04d", i);

            LocalDate startDate = LocalDate.of(2024, 8, 1);
            LocalDate endDate = LocalDate.of(2024, 8, 31);
            long randomDays = random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);
            LocalDate periodStart = startDate.plusDays(randomDays);
            LocalDate periodEnd = periodStart.plusDays(random.nextInt(10)); // Ensure end date is after start date

            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String subCategory = "전통";

            festivalsService.create2(
                    title, periodStart.toString(), periodEnd.toString(), content, place, closedDay,
                    websiteUrl, phoneNumber, hashtags, latitude, longitude, subCategory
            );
        }
    }

    @Test
    @DisplayName("음식 축제 데이터 생성")
    void test4() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("음식 축제 %d", i);
            String content = String.format("음식 축제 내용 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = "매주 월요일 휴무";
            String websiteUrl = "https://example.com";
            String hashtags = String.format("#음식 축제%d", i);
            String phoneNumber = String.format("010-1234-%04d", i);

            LocalDate startDate = LocalDate.of(2024, 8, 1);
            LocalDate endDate = LocalDate.of(2024, 8, 31);
            long randomDays = random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);
            LocalDate periodStart = startDate.plusDays(randomDays);
            LocalDate periodEnd = periodStart.plusDays(random.nextInt(10)); // Ensure end date is after start date

            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String subCategory = "음식";

            festivalsService.create2(
                    title, periodStart.toString(), periodEnd.toString(), content, place, closedDay,
                    websiteUrl, phoneNumber, hashtags, latitude, longitude, subCategory
            );
        }
    }

    @Test
    @DisplayName("기타 축제 데이터 생성")
    void test5() throws IOException {
        Random random = new Random();

        for (int i = 1; i <= 5; i++) {
            String title = String.format("기타 축제 %d", i);
            String content = String.format("기타 축제 내용 %d", i);
            String place = PLACES[random.nextInt(PLACES.length)];
            String closedDay = "매주 월요일 휴무";
            String websiteUrl = "https://example.com";
            String hashtags = String.format("#기타축제%d", i);
            String phoneNumber = String.format("010-1234-%04d", i);

            LocalDate startDate = LocalDate.of(2024, 8, 1);
            LocalDate endDate = LocalDate.of(2024, 8, 31);
            long randomDays = random.nextInt((int) ChronoUnit.DAYS.between(startDate, endDate) + 1);
            LocalDate periodStart = startDate.plusDays(randomDays);
            LocalDate periodEnd = periodStart.plusDays(random.nextInt(10)); // Ensure end date is after start date

            double latitude = 33.0 + (random.nextDouble() * 0.5);  // 제주도 위도 범위: 33.0 ~ 33.5
            double longitude = 126.0 + (random.nextDouble() * 0.5); // 제주도 경도 범위: 126.0 ~ 126.5
            String subCategory = "기타";

            festivalsService.create2(
                    title, periodStart.toString(), periodEnd.toString(), content, place, closedDay,
                    websiteUrl, phoneNumber, hashtags, latitude, longitude, subCategory
            );
        }
    }
}
