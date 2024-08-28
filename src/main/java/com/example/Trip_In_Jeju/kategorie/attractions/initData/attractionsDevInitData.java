package com.example.Trip_In_Jeju.kategorie.attractions.initData;


import com.example.Trip_In_Jeju.kategorie.attractions.service.AttractionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class attractionsDevInitData implements BeforeIntiData {

    private final AttractionsService attractionsService;
    // 다른 서비스들도 동일하게 주입


    @Bean
    public ApplicationRunner init() {
        return args -> {
            try {
                // 현재 작업 디렉토리 출력
                String currentDir = System.getProperty("user.dir");
                System.out.println("현재 작업 디렉토리: " + currentDir);

                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
                String[] imagePaths = {
                        "/app/resources/static/images/attractions/그리스신화박물관.jpeg"
                };

                // 파일 경로를 절대 경로로 변경하여 로깅
                File file = new File(imagePaths[0]);
                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());

                // 여러 Attractions 데이터 생성
                createAttractionsData("자매국수","10:00","22:00", "제주도의 대표 국수 맛집입니다.","월요일",
                        "http://example.com","064-123-4567",33.5101,126.5215,"제주특별자치도 제주시 연동","자연경관", imagePaths[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void createAttractionsData(String title, String businessHoursStart,String businessHoursEnd, String description,String closedDay,
                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
        MultipartFile thumbnail = getMultipartFile(imagePath);

        // Attractions 데이터 생성
        attractionsService.create(
                title,
                businessHoursStart, // 영업 시작 시간
                businessHoursEnd, // 영업 종료 시간
                description,
                title,
                closedDay, // 휴무일
                websiteUrl,
                phoneNumber,
                thumbnail,
                latitude, // 예시 위도
                longitude, // 예시 경도
                "관광지", // 카테고리
                address, // 주소
                subCategory // 서브 카테고리
        );
    }

    private MultipartFile getMultipartFile(String filePath) throws IOException {
        // 절대 경로를 직접 사용함으로 System.getProperty("user.dir") 제거
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("파일이 존재하지 않습니다: " + file.getAbsolutePath());
            throw new IOException("파일이 존재하지 않습니다: " + file.getAbsolutePath());
        }
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(
                file.getName(),
                file.getName(),
                "image/jpeg",
                input
        );
    }
}