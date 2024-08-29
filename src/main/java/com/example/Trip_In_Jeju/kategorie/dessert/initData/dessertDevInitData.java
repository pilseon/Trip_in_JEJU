//package com.example.Trip_In_Jeju.kategorie.dessert.initData;
//
//
//import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
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
//public class dessertDevInitData implements BeforeIntiData {
//
//    private final DessertService dessertService;
//    // 다른 서비스들도 동일하게 주입
//
//
//    @Bean(name = "dessertInit")
//    public ApplicationRunner init() {
//        return args -> {
//            try {
//                // 현재 작업 디렉토리 출력
//                String currentDir = System.getProperty("user.dir");
//                System.out.println("현재 작업 디렉토리: " + currentDir);
//
//                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
//                String[] imagePaths = {
//                        "/app/resources/static/images/dessert/노을리.jpg",
//                        "/app/resources/static/images/dessert/런던베이글.jpg",
//                        "/app/resources/static/images/dessert/무로이.png",
//                        "/app/resources/static/images/dessert/휴일로.jpg",
//                        "/app/resources/static/images/dessert/우유부단.jpg",
//                        "/app/resources/static/images/dessert/미르오메기떡.jpeg",
//                        "/app/resources/static/images/dessert/블랑로쉐.jpeg",
//                        "/app/resources/static/images/dessert/새빌.jpeg",
//                        "/app/resources/static/images/dessert/우도샌드.jpeg",
//                        "/app/resources/static/images/dessert/콜로세움.jpeg",
//                        "/app/resources/static/images/dessert/포레스트제이.jpeg"
//                };
//
//                // 파일 경로를 절대 경로로 변경하여 로깅
//                File file = new File(imagePaths[0]);
//                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());
//
//                // 여러 Dessert 데이터 생성
//                createDessertData("노을리","09:00","21:00", "제주 서쪽, 노을이 가장 아름답게지는 마을에 있는 애월카페 노을리는 아름답게 저무는 노을을 바라보며 가족이나 친구 그리고 연인들이 여유롭게 힐링할 수 있는 분위기 있는 브런치 & 카페입니다.","연중무휴",
//                        "https://blog.naver.com/jejuuni33","0507-1491-5032",33.47989826646665,126.37271334751566,"제주 제주시 애월읍 애월해안로 654 가동 1층","카페", imagePaths[0]);
//
//                createDessertData("런던베이글","08:00","18:00", "다정한 스텝과 방금 나온 베이글.\n" +
//                                "그리고 따뜻한 수프가 기다리는\n" +
//                                "가장 가까운 런던 속 베이글 뮤지엄,\n" +
//                                "\n" +
//                                "국내 어디에서도 맛볼 수 없었던\n" +
//                                "베이글의 식감과 무드를 선보입니다.","연중무휴",
//                        "https://app.catchtable.co.kr/ct/shop/london_bagel_museum_jeju","064-123-4567",33.55355734286627,126.71545924003011,"제주 제주시 구좌읍 동복로 85 제2동 1층","베이커리", imagePaths[1]);
//
//                createDessertData("무로이","18:30","19:00", "무로이[MUROI]\n" +
//                                ": 안개가 아주 짙거나 오래끼는 현상\n" +
//                                "\n" +
//                                "제주도를 대표하는 DESSERT CAFE\n" +
//                                "제주를 담은 최고의 디저트 한 입을 선물합니다.\n" +
//                                "\n" +
//                                "당신의 BLACK을 더욱 특별하게-","연중무휴",
//                        "https://www.instagram.com/_muroi/","0507-1412-0008",33.3077351431997,126.336566694261,"제주 서귀포시 안덕면 동광본동로 21 무로이","베이커리", imagePaths[2]);
//
//                createDessertData("휴일로","10:00","20:00", "- Open :10:00\n" +
//                                "- Close : 20:00 (Last order 19:30)\n" +
//                                "- 연중무휴\n" +
//                                "- 반려동물은 야외만 가능합니다 (목줄,배변봉투 지참)\n" +
//                                "\n" +
//                                "* 리뷰이벤트 휘낭시에 증정 !","연중무휴",
//                        "https://blog.naver.com/hueilot4965","0507-1491-5032",33.232208057130535,126.36652497979144,"제주 서귀포시 안덕면 창천리 786","카페", imagePaths[3]);
//
//                createDessertData("우유부단","10:00","18:00", "너무 부드러워 끊을 수 없는 치명적인 우유카페, 우유부단입니다.\n" +
//                                "(넘칠 우, 부드러울 유, 아닐 부, 끊을 단)\n" +
//                                "우유를 향한 부단한 노력이라는 이중적인 의미를 가지고 있습니다.\n" +
//                                "\n" +
//                                "우유부단은 국내 최대 유기농 우유 목장인 제주 성이시돌목장 내에 사진찍기 명소인 테쉬폰과 함께 위치해 있습니다.\n" +
//                                "유기농 우유를 활용한 건강한 맛의 수제 아이스크림, 밀크티 등 다양한 메뉴들을 선보이고 있습니다.","연중무휴",
//                        "https://www.instagram.com/uyubudan/","064-796-2033",33.347639660561946,126.32808265183853,"제주 제주시 한림읍 금악동길 38","아이스크림", imagePaths[4]);
//
//                createDessertData("미르오메기떡","07:30","18:30", "제주 전통 오메기떡을 오랜 노하우로 재해석하여 보기도 좋은떡을 맛도 좋게~\n" +
//                                "어르신들부터 아이들까지 남녀노소 즐겨먹는 간식이 될 수 있도록 노력하겠습니다!\n","토요일",
//                        "https://mirromegi.com/","064-726-4003",33.505879366003605,126.52872075499302,"제주 제주시 민오름길 14","카페", imagePaths[5]);
//
//                createDessertData("블랑로쉐","11:00","17:00", "반려견 동반은 외부 좌석에 한해 가능합니다.","연중무휴",
//                        "https://www.instagram.com/blancrocher_udo","0507-1398-9154",33.51568360202809,126.95788529275667,"제주 제주시 우도면 우도해안길 783 1층","아이스크림", imagePaths[6]);
//
//                createDessertData("새빌","09:00","19:00", "제주3대 오름인 새별오름이 바로 눈앞에서 펼쳐지는 특별한 뷰를 만나보실 수 있으며, 네이버에서 대한민국 6대 이색 카페에 선정될 정도로 제주를 대표하는 애월 카페입니다.\n","연중무휴",
//                        "http://www.instagram.com/saebilcafe","0507-1315-0080",33.36431114555293,126.36297023760353,"제주 제주시 애월읍 평화로 1529","카페", imagePaths[7]);
//
//                createDessertData("우도샌드","10:00","18:00", "우도샌드는 .\n" +
//                                "우도땅콩,제주 한라봉,구좌당근,무화과 ,빅토리아 등\n" +
//                                "다양한 재료를 요리조리 만들어\n" +
//                                "선물할수있는 선물세트와 디저트를 판매하고있습니다.\n" +
//                                "샤샤의 심쿵애교는 덤이에요-^-^-","연중무휴",
//                        "https://uujj.modoo.at/","0507-1358-4121",33.51627857703866,126.95764389836917,"제주 제주시 우도면 우도해안길 780-2 1호","아이스크림", imagePaths[8]);
//
//                createDessertData("콜로세움","09:00","23:00", "제주시내권에 위치하여 대중교통 및 자차로 방문이 용이합니다.\n" +
//                                "다른 카페에서 맛볼 수 없는 콜로세움제주 만의 디저트, 브런치, 화덕피자, 국내 외 생맥주를 맛보실 수 있습니다.\n","연중무휴",
//                        "http://instagram.com/cafe_colosseum","0507-1428-8891",33.480429622758585,126.50163491386128,"제주 제주시 민오름길 14","베이커리", imagePaths[9]);
//
//                createDessertData("포레스트제이","10:00","18:00", "귤 밭 속 우사와 돌창고를 개조한 서귀포 안덕카페 포레스트제이카우셰드 입니다.\n" +
//                                "\n" +
//                                "제주 자연 속에 있는 듯한 느낌으로 따뜻하고 여유로운 공간이 준비되어 있습니다.","연중무휴",
//                        "https://www.instagram.com/cafe_forestj_jeju","064-792-9909",33.2554173162072,126.331182516555,"제주 서귀포시 안덕면 화순서서로64번길 16 포레스트제이카우셰드","카페", imagePaths[10]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    private void createDessertData(String title, String businessHoursStart,String businessHoursEnd, String description,String closedDay,
//                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
//        MultipartFile thumbnail = getMultipartFile(imagePath);
//
//        // Dessert 데이터 생성
//        dessertService.create(
//                title,
//                businessHoursStart, // 영업 시작 시간
//                businessHoursEnd, // 영업 종료 시간
//                description,
//                title,
//                closedDay, // 휴무일
//                websiteUrl,
//                phoneNumber,
//                thumbnail,
//                latitude, // 예시 위도
//                longitude, // 예시 경도
//                "디저트", // 카테고리
//                address, // 주소
//                subCategory // 서브 카테고리
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