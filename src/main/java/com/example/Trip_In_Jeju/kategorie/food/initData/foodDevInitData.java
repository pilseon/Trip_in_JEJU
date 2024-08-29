
package com.example.Trip_In_Jeju.kategorie.food.initData;


import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
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
public class foodDevInitData implements BeforeIntiData {

    private final FoodService foodService;
    // 다른 서비스들도 동일하게 주입


    @Bean(name = "foodInit")
    public ApplicationRunner init() {
        return args -> {
            try {
                // 현재 작업 디렉토리 출력
                String currentDir = System.getProperty("user.dir");
                System.out.println("현재 작업 디렉토리: " + currentDir);

                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
                String[] imagePaths = {
                        "/app/resources/static/images/food/이춘옥원조고등어쌈밥.jpg",
                        "/app/resources/static/images/food/꺼멍도새기.jpg",
                        "/app/resources/static/images/food/애월리에.jpg",
                        "/app/resources/static/images/food/우래.jpg",
                        "/app/resources/static/images/food/연태만.jpg",
                        "/app/resources/static/images/food/해오반.jpg",
                        "/app/resources/static/images/food/돗통흑돼지.jpg",
                        "/app/resources/static/images/food/계절식탁.jpg",
                        "/app/resources/static/images/food/난산리식당.jpg",
                        "/app/resources/static/images/food/달이뜨는식탁.jpg"
                };

                // 파일 경로를 절대 경로로 변경하여 로깅
                File file = new File(imagePaths[0]);
                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());

                // 여러 Food 데이터 생성
                createFoodData("이춘옥원조고등어쌈밥 본점","09:00","21:00", "당신의 의미있는 시간을 이곳에서 사용하기로 결정한 선택에 깊이 감사드립니다.\n" +
                                "완벽한 시간으로 기억되도록 맛있는 음식과 미소로 보답하겠습니다 항상 최선을 다해 노력하겠습니다 감사합니다.","연중무휴",
                        "https://booking.naver.com/?area=pll","0507-1402-9914",33.4889959115397,126.418698640025,"제주 제주시 애월읍 일주서로 7213","한식", imagePaths[0]);
                createFoodData("꺼멍도새기","08:00","21:00", "제주 동문시장 내에 위치한 꺼멍도새기 입니다.\n" +
                                "제주 갈치조림, 성게보말미역국, 전복해물뚝배기, 통갈치구이, 흑돼지주물럭 등 다양한 제주 향토음식을 전문으로 운영하며 제주지역분들이 먼저 추천하는 맛집입니다. 동문시장 내에 위치하여 접근성이 좋고 주차장을 편리하게 이용하실 수 있습니다^^ 동문시장 구경하시고 맛있는 꺼멍도새기를 찾아주세요~","연중무휴",
                        "http://bbsj.kr/sijang/store.php?sto_idx=500","064-757-7057",33.5123037630433,126.528157463155,"제주 제주시 동문로4길 9 동문공설시장 꺼멍도새기","한식", imagePaths[1]);
                createFoodData("제주애월리에","11:00","22:00", "제주 애월 향토 퓨전양식 맛집\n" +
                                "2024년 한국소비자베스트브랜드대상 1위수상\n" +
                                "2021년 스포츠 서울 고객 신뢰도 만족대상,\n" +
                                "2022년 KCIA 한국 소비자평가 외식업 부문 우수상을 시상한 제주 애월 대표 맛집","수요일",
                        "https://blog.naver.com/aewollier1","0507-1346-7623",33.47374972721081,126.37813673237835,"제주 제주시 애월읍 엄수로 8-11 제주애월리에","양식", imagePaths[2]);
                createFoodData("우래","18:00","02:00", "제주도 성산에 위치한 작은 퓨전 이자카야.\n" +
                                "혼술 대환영. 반려동물도 OK!\n" +
                                "아늑한 공간으로 여러분들을 초대합니다.\n" +
                                "\n" +
                                "생맥주, 하이볼, 사케, 소주와 곁들일\n" +
                                "우동, 꼬치, 볶음요리 등 각종 술안주가 준비되어 있습니다.\n" +
                                "\n" +
                                "언제 오셔도 편안하게 머물다 가실 수 있는\n" +
                                "우래가 되겠습니다.","수요일",
                        "https://www.instagram.com/jeju_urae/","0507-1322-2576",33.4499774512314,126.914549962039,"제주 서귀포시 성산읍 고성동서로56번길 15 1층","일식", imagePaths[3]);
                createFoodData("연태만","10:00","21:30", "제주공항근처 5분거리 동문시장근처 중식당입니다\n" +
                                "합리적인 가격으로 최고의 중식당 요리를 맛볼 수 있는 중국요리 전문점 [ 연태만 ] 입니다.","월요일",
                        "https://www.instagram.com/ttaemani/","0507-1328-8448",33.50812753060554,126.52530869106884,"제주 제주시 중앙로21길 10 1층 연태만","중식", imagePaths[4]);
                createFoodData("해오반","10:00","22:00", "안녕하세요 해오반 애월 본점입니다.\n" +
                                "저희 해오반은\n" +
                                "바다가 오는 한상이라는\n" +
                                "의미를 가진 가게로서,\n" +
                                "제주 해녀 어머님이\n" +
                                "직접 잡아 손질한 싱싱한 제주 해산물을\n" +
                                "저희 해오반 만의 비법으로 풀어내\n" +
                                "남녀노소 좋아하는 맛을 내었습니다.","연중무휴",
                        "https://www.instagram.com/haeoban_aewol/","0507-1495-1286",33.463947481190864,126.33940649880014,"제주 제주시 애월읍 고내로9길 1 1층","한식", imagePaths[5]);
                createFoodData("돗통 흑돼지","16:00","22:00", "산방산돗통에서는 실내의 아늑한 분위기와 야외 테이블에서 캠핑온 기븐으로 제주산 흑돼지를 구워 드실수있습니다, 워터에이징과 드라이에이징 교차숙성된 제주산 산방산흑돼지는 눈과 입이 즐거운 순간될겁니다 좋은 추억 만들어 보세요","화요일",
                        "https://app.catchtable.co.kr/ct/shop/jejudottong","0507-1401-0090",33.2494647589586,126.297797961849,"제주 서귀포시 안덕면 사계북로41번길 189 돗통","한식", imagePaths[6]);
                createFoodData("계절식탁","11:00","22:00", "제주함덕맛집 계절식탁 함덕점 입니다\n" +
                                "함덕해수욕장 바로 앞에 있어 식사하면서 함덕해수욕장 풍경과\n" +
                                "저녁에는 제주 노을을 볼 수 있습니다.\n" +
                                "주 메뉴로는 제주를 대표하는 해산물을 판매하고 있습니다.\n" +
                                "**고등어회, 딱새우회, 갈치회,\n" +
                                "**갈치조림, 갈치구이\n" +
                                "**모듬물회, 꽃게탕, 생선지리 등\n" +
                                "넓은실내, 깔끔한 인테리어, 함덕해수욕장 뷰 까지\n" +
                                "맛있는 음식과 쾌적한 공간에서 편안한 식사를 할 수 있도록 준비했습니다.","월요일",
                        "#","0507-1432-6577",33.5425552676401,126.66736743051113,"제주 제주시 조천읍 조함해안로 510 2층","한식", imagePaths[7]);
                createFoodData("제주 성산 난산리 식당","11:00","22:00", "안녕하세요. 난산리 식당 김창섭 요리사입니다. 관심 가져주셔서 진심으로 감사하다는 말을 전합니다.","화요일, 수요일",
                        "https://app.catchtable.co.kr/ct/shop/nansan_kitchen","0507-1370-4169",33.3992063158406,126.86792450235,"제주 서귀포시 성산읍 난산로41번길 38","양식", imagePaths[8]);
                createFoodData("달이뜨는식탁 구좌 월정리 본점","11:00","20:00", "월정리 맛집 '달이뜨는식탁’ 입니다.\n" +
                                "달이 뜨는 바닷가라는 의미를 가진 월정리 마을에 제주특산물을 사용하여 특색있는 파스타와 돈까스를 맛보이기 위해 '달이뜨는식탁'을 오픈 하였습니다.","연중무휴",
                        "https://dalsiktakjeju.modoo.at/","0507-1330-8538",33.5531575884987,126.790794863817,"제주 제주시 구좌읍 월정1길 14 1층 달이뜨는식탁","양식", imagePaths[9]);


            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void createFoodData(String title, String businessHoursStart,String businessHoursEnd, String description,String closedDay,
                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
        MultipartFile thumbnail = getMultipartFile(imagePath);

        // Food 데이터 생성
        foodService.create(
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
                "음식점", // 카테고리
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
