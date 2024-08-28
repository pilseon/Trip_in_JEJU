//package com.example.Trip_In_Jeju.kategorie.food.initData;
//
//import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
//import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//@Profile("prod")
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final FoodService foodService;
//    private final ActivityService activityService;
//
//    @Bean(name = "foodInit")
//    public ApplicationRunner initFood() {
//        return args -> {
//            try {
//                String[] imagePaths = {
//                        "/app/resources/static/images/food/자매식당.jpeg",
//                        "/app/resources/static/images/food/계절식탁.jpg"
//                };
//
//                createFoodData("자매국수", "10:00", "22:00", "제주도의 대표 국수 맛집입니다.", "월요일",
//                        "http://example.com", "064-123-4567", 33.5101, 126.5215, "제주특별자치도 제주시 연동", "한식", imagePaths[0]);
//                createFoodData("계절식탁", "11:00", "23:00", "제주도의 대표 계절식탁입니다.", "월요일",
//                        "http://example.com", "064-123-4567", 33.5201, 126.5315, "제주특별자치도 제주시 연동", "일식", imagePaths[1]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    @Bean(name = "activityInit")
//    public ApplicationRunner initActivity() {
//        return args -> {
//            try {
//                String[] imagePaths = {
//                        "/app/resources/static/images/activity/함덕돌핀레저.png"
//                };
//
//                createActivityData("함덕돌핀레저", "10:00", "22:00", "제주도의 대표 액티비티입니다.", "월요일",
//                        "http://example.com", "064-123-4567", 33.5101, 126.5215, "제주특별자치도 제주시 함덕", "스포츠", imagePaths[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    private void createFoodData(String title, String businessHoursStart, String businessHoursEnd, String description, String closedDay,
//                                String websiteUrl, String phoneNumber, double latitude, double longitude, String address, String subCategory, String imagePath) throws IOException {
//        MultipartFile thumbnail = getMultipartFile(imagePath);
//
//        foodService.create(
//                title,
//                businessHoursStart,
//                businessHoursEnd,
//                description,
//                title,
//                closedDay,
//                websiteUrl,
//                phoneNumber,
//                thumbnail,
//                latitude,
//                longitude,
//                "음식점",
//                address,
//                subCategory
//        );
//    }
//
//    private void createActivityData(String title, String businessHoursStart, String businessHoursEnd, String description, String closedDay,
//                                    String websiteUrl, String phoneNumber, double latitude, double longitude, String address, String subCategory, String imagePath) throws IOException {
//        MultipartFile thumbnail = getMultipartFile(imagePath);
//
//        activityService.create(
//                title,
//                businessHoursStart,
//                businessHoursEnd,
//                description,
//                title,
//                closedDay,
//                websiteUrl,
//                phoneNumber,
//                thumbnail,
//                latitude,
//                longitude,
//                "액티비티",
//                address,
//                subCategory
//        );
//    }
//
//    private MultipartFile getMultipartFile(String filePath) throws IOException {
//        File file = new File(filePath);
//        if (!file.exists()) {
//            throw new IOException("파일이 존재하지 않습니다: " + file.getAbsolutePath());
//        }
//        FileInputStream input = new FileInputStream(file);
//        return new MockMultipartFile(
//                file.getName(),
//                file.getName(),
//                "image/jpeg",
//                input
//        );
//    }
//}