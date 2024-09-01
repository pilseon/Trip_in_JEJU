
package com.example.Trip_In_Jeju.kategorie.festivals.initData;


import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
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
//@Profile("prod")
@Profile("dev")
@RequiredArgsConstructor
public class festivalsDevInitData implements BeforeIntiData {

    private final FestivalsService festivalsService;
    // 다른 서비스들도 동일하게 주입


    @Bean(name = "festivalsInit")
    public ApplicationRunner init() {
        return args -> {
            try {
                // 현재 작업 디렉토리 출력
                String currentDir = System.getProperty("user.dir");
                System.out.println("현재 작업 디렉토리: " + currentDir);

                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
//                String[] imagePaths = {
//                        "/app/resources/static/images/festivals/제주마축제.jpeg"
//                };

                String[] imagePaths = {
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/festivals/제주마축제.jpeg"
                };
                // 파일 경로를 절대 경로로 변경하여 로깅
                File file = new File(imagePaths[0]);
                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());

                // 여러 Festivals 데이터 생성
                createFestivalsData("제주마축제축제","2024-10-14","2024-10-15", "올해로 19회를 맞이하는 제주마 축제는 가을의 시작을 알리는 렛츠런파크 제주의 대표 축제로 매년 수만명의 도민과 관광객이 찾아와 즐기는 축제이다. 올해는 전통 마상무예 공연 등 제주말에 대해 알 수 있는 공연 프로그램을 비롯하여 몽생이 요리교실, 사생대회, 댄스 경연대회, 윷놀이, 미니운동회 등 어린이부터 청장년층까지 온 가족이 즐길 수 있는 체험 프로그램이 준비되어 방문객에게 풍성한 즐길거리를 선보일 예정이다.","연중무휴",
                        "#","1566-3333",33.4082554042881,126.398067927153,"제주 제주시 애월읍 평화로 2144","전통", imagePaths[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void createFestivalsData(String title, String periodStart, String periodEnd, String description, String closedDay,
                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
        MultipartFile thumbnail = getMultipartFile(imagePath);

        // Festivals 데이터 생성
        festivalsService.create(
                title,
                periodStart, // 영업 시작 시간
                periodEnd, // 영업 종료 시간
                description,
                title,
                closedDay, // 휴무일
                websiteUrl,
                phoneNumber,
                thumbnail,
                latitude, // 예시 위도
                longitude, // 예시 경도
                "축제", // 카테고리
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
