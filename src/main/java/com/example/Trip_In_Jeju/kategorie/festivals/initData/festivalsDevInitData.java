//package com.example.Trip_In_Jeju.kategorie.festivals.initData;
//
//
//import com.example.Trip_In_Jeju.kategorie.festivals.initData.BeforeIntiData;
//import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
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
//@Profile("dev")
//@RequiredArgsConstructor
//public class festivalsDevInitData implements BeforeIntiData {
//
//    private final FestivalsService festivalsService;
//    // 다른 서비스들도 동일하게 주입
//
//
//    @Bean
//    public ApplicationRunner init() {
//        return args -> {
//            try {
//                // 여러 이미지 파일 경로 설정
//                String[] imagePaths = {
//                        "C:\\work\\Trip_In_Jeju\\src\\main\\resources\\static\\images\\자매국수.jpeg"
//                };
//
//                // 여러 Festivals 데이터 생성
//                createFestivalsData("자매국수", "제주도의 대표 국수 맛집입니다.", imagePaths[0]);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    private void createFestivalsData(String title, String description, String imagePath) throws IOException {
//        MultipartFile thumbnail = getMultipartFile(imagePath);
//
//        // Festivals 데이터 생성
//        festivalsService.create(
//                title,
//                "2024-01-01", "2024-01-10", // 축제 기간
//                description,
//                title,
//                "화요일", // 휴무일
//                "http://example.com",
//                "064-123-4567",
//                thumbnail,
//                33.5101, // 예시 위도
//                126.5215, // 예시 경도
//                "축제", // 카테고리
//                "제주특별자치도 제주시 연동", // 주소
//                "계절" // 서브 카테고리
//        );
//    }
//
//    private MultipartFile getMultipartFile(String filePath) throws IOException {
//        // 절대 경로를 직접 사용함으로 System.getProperty("user.dir") 제거
//        File file = new File(filePath);
//        if (!file.exists()) {
//            System.out.println("파일이 존재하지 않습니다: " + file.getAbsolutePath());
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