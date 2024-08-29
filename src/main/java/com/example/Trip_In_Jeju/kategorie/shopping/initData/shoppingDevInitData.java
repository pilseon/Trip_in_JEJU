package com.example.Trip_In_Jeju.kategorie.shopping.initData;


import com.example.Trip_In_Jeju.kategorie.shopping.service.ShoppingService;
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
public class shoppingDevInitData implements BeforeIntiData {

    private final ShoppingService shoppingService;
    // 다른 서비스들도 동일하게 주입


    @Bean(name = "shoppingInit")
    public ApplicationRunner init() {
        return args -> {
            try {
                // 현재 작업 디렉토리 출력
                String currentDir = System.getProperty("user.dir");
                System.out.println("현재 작업 디렉토리: " + currentDir);

                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
                String[] imagePaths = {
                        "/app/resources/static/images/shopping/동문시장.jpg",
                        "/app/resources/static/images/shopping/동문수산시장.jpg",
                        "/app/resources/static/images/shopping/서귀포재래시장.jpg",
                        "/app/resources/static/images/shopping/신라면세점 제주.jpg",
                        "/app/resources/static/images/shopping/롯대면세점.jpg",
                        "/app/resources/static/images/shopping/중문면세점.jpg",
                        "/app/resources/static/images/shopping/바이제주.jpg",
                        "/app/resources/static/images/shopping/선물고팡.jpg",
                        "/app/resources/static/images/shopping/제스토리.jpg",
                        "/app/resources/static/images/shopping/우디버디.png"


                };

                // 파일 경로를 절대 경로로 변경하여 로깅
                File file = new File(imagePaths[0]);
                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());

                // 여러 Shopping 데이터 생성
                createShoppingData("동문 재래시장","08:00","21:00", "동문재래시장\n" +
                                "위치●개장일시\n" +
                                "제주특별자치도 제주시 관덕로 14길 20, 매일 오픈 8시~21시\n" +
                                "주차장\n" +
                                "동문재래시장 주차장 약129대 가능 (무료 30분), 네비게이션 시장 주소 또는 동문재래시장 주차장 입력","월요일",
                        "https://map.naver.com/p/search/%EC%A0%9C%EC%A3%BC%EB%8F%84%20%EC%A0%84%ED%86%B5%EC%8B%9C%EC%9E%A5/place/11616005?placePath=?entry=pll&from=nx","064-752-3001",33.5120448 ,126.5282794,"제주 제주시 관덕로14길 20","전통시장", imagePaths[0]);

                createShoppingData("동문 수산시장","07:00","20:00", "1970년 개설된 상가건물형의 중형시장으로 주 취급품목은 제주산 은갈치, 고등어(횟감), 당일제주산 옥돔 등의 수산물.\n" +
                                "제주 동문재래수산 시장은 관광시장인 동시에 수산물 특화시장(맞춤전문시장)이다.\n" +
                                "수산시장 이미지를 홍보하기 위해 팜플렛을 제작하여 관광지에 배포하고, 공동판매 수입사업을 통해 시장자체 브랜드가 인쇄된 비닐봉투 테이프 쇼핑백 포장용 스티로폼을 제작하여 사용하고 있다.\n" +
                                "최상의 신선도를 유지하기 위해 특수 포장 후 수산시장 이용객 전용 퀵서비스로 상품을 배달하는 시스템도 갖추고 있으며, 서울 부산 인천 김해 지역에는 당일바리(당일 어획된 생선) 어종을 당일 택배 배송하여 고객의 신뢰도를 높이고 있다.\n" +
                                "시장활성화를 위해 정월대보름 복조리증정행사(1회), 경품제공 및 할인행사(년2회)를 개최하고, 스포츠교실, 부녀회 댄스교실도 운영하고 있다.","토요일,일요일",
                        "https://map.naver.com/p/search/%EC%A0%9C%EC%A3%BC%EB%8F%84%20%EC%A0%84%ED%86%B5%EC%8B%9C%EC%9E%A5/place/13347084?c=15.00,0,0,0,dh&placePath=/home","064-752-8959",33.5126937 ,126.5262065,"제주 제주시 이도1동 1349-60","전통시장", imagePaths[1]);

                createShoppingData("서귀포매일올레시장시장","07:00","20:00", "1965년 개설된 상가주택건물형의 중형시장으로 주 취급품목은 농수산물(옥돔 등 제주도 특산물) 등. 제주의 다양한 먹거리가 모여있는 곳\n" +
                                "\n" +
                                "서귀포에서 가장 큰 시장으로 60여년의 역사를 지닌 전통시장(200여개 점포/140여개 노점)이다.\n" +
                                "\n" +
                                "시장내부가 전 구간에 비가림 시설이 설치되어 있다.\n" +
                                "\n" +
                                "시장내 주차장은 500여대를 동시에 추가 가능하며 30분 무료 주차가능하다.(15분에 500원으로 1시간 2천원)\n" +
                                "\n" +
                                "시장내 물건 구입한 고객 무료 배달 택배서비스를 갖추고 있으며 청소년을 위한 야외공연장 및 공원이 있음.","월요일",
                        "https://map.naver.com/p/search/%EC%84%9C%EA%B7%80%ED%8F%AC%EB%A7%A4%EC%9D%BC%EC%98%AC%EB%9E%98%EC%8B%9C%EC%9E%A5/place/13571992?c=15.00,0,0,0,dh&placePath=/information","0507-1353-1949",33.2486815  ,126.5641127,"제주 서귀포시 서귀동 340","전통시장", imagePaths[2]);

                createShoppingData("신라면세점 제주","10:00","19:00", "신라 면세점은 세계적인 명품, 최신 유행 상품을 보다 저렴하게 만나 보실 수 있는 편안한 쇼핑 공간입니다. 품위있는 초현대식 시설, 최고 수준의 서비스로 고객 만족을 추구하고 있으며, \n" +
                                "쾌적한 매장 분위기, 넓은 주차 공간은 최고의 편안함을 선사해 드립니다.","연중무휴",
                        "https://www.shilladfs.com/estore/kr/ko/shilladfscustomercenter/store?code=03","1688-1110",33.486069 ,126.487744,"제주 제주시 노연로 69","전통시장", imagePaths[3]);

                createShoppingData("롯대면세점 제주점","10:00","19:00", "롯데시티호텔 제주 1~4층에 위치한 롯데면세점은 세계 유명 브랜드를 비롯 총 270여개 브랜드가 입점해있습니다.\n" +
                                "\n" +
                                "중소·중견기업 전용 공간은 물론 제주 특산품까지 다양한 상품을 구비하고 있어 쇼핑의 즐거움을 선사합니다.\n" +
                                "\n" +
                                "[롯데면세점 운영 안내]\n" +
                                "사회적 거리두기 완화로 부분적으로 매장을 오픈 하오니 참고 바랍니다.","연중무휴",
                        "https://kor.lottedfs.com/kr/shopmain/home","1688-3000",33.4541709 ,126.372187,"제주 제주시 도령로 83 제주연동롯데시티호텔 1~3층","면세점", imagePaths[4]);

                createShoppingData("제주 관공공사 중문면세점","10:00","19:00", "국내에서 제주 여행, 출장, 방문시 면세품 쇼핑이 가능한 제주면세점, 제주관광공사면세점 입니다.\n" +
                                "\n" +
                                "* 이용방법\n" +
                                "1. 매장이용 : 제주 서귀포 중문관광단지 제주국제컨벤션센터 내(제주관광공사 중문면세점) 방문 후 구매\n" +
                                "2. 인터넷이용 : 제주관광공사면세점 사이트(jejudfs.com) 접속 후 구매\n" +
                                "> 면세품 인도 : 제주에서 타도시 출도(항공, 선박)시 공항(항만) 인도장에서 수령\n","연중무휴",
                        "https://www.jejudfs.com/","064-805-3169",33.2413406944452,126.424429045634,"제주 서귀포시 중문관광로 224 제주국제컨벤션센터 1층","면세점", imagePaths[5]);


                createShoppingData("바이제주","09:00","21:30", "바닷가 바로 앞에 위치한 제주도 최대규모의 감성 기념품샵 바이제주입니다. 제주도 작가와 함께 만든 제주 바이브가 듬뿍 담긴 다양한 소품들을 만나보세요.\n" +
                                "\n" +
                                "바이제주의 인기상품 '우드프린트' - 제주를 여행하며 찍은 핸드폰 사진을 원목에 인화 해 드립니다.\n" +
                                "\n" +
                                "다양한 먹거리부터 캔들, 방향제, 마그넷, 엽서 등 제주스러운 기념품들을 가족, 친구, 직장동료, 사랑하는 사람들에게 선물하세요 : )\n" +
                                "\n" +
                                "* 매장에서 10만원 이상 구매 시 '무료 택배 서비스'를 제공합니다. 마음은 든든하게 몸은 가볍게 제주를 여행하세요!\n" +
                                "\n" +
                                "여행을 마치고 공항으로 가기 전, 제주를 추억할 나만의 아이템을 골라보세요! By Jeju, Buy Jeju, Bye Jeju !\n","연중무휴",
                        "https://smartstore.naver.com/enbright","064-745-1134",33.516627 ,126.5040466,"제주 제주시 서해안로 626 A, B동","기념품", imagePaths[6]);

                createShoppingData("선물고팡 제주공항점","08:00","20:00", "*선물고팡 공항점은 제주공항 3분거리 내에 있는 감성소품샵입니다:D\n" +
                                "\n" +
                                "*신선하고 특별한 제주도의 특산품, 청과품, 수산품들도 저렴하게 판매하고 있습니다.\n" +
                                "\n" +
                                "* 제주 감성을 담은 감성소품만이 아닌 선물고팡에서만 만나볼 수 있는 자체제작 귀여운 감성소품들도 다양하게 준비되어 있습니다:)\n" +
                                "\n" +
                                "-넓은 주차장 완비\n" +
                                "-전국 택배 가능","연중무휴",
                        "https://www.instagram.com/giftgopang_airport?igsh=bXcxcnFuanB4eGw4","064-805-3169",33.50289 ,126.5049275,"제주 제주시 월성로 15 선물고팡 제주공항점","기념품", imagePaths[7]);

                createShoppingData("제스토리","09:00","21:00", "서귀포 법환포구에 위치한 제스토리는 서귀포 최대규모 '감성소품샵'입니다. 제주도 작가 300팀과 함께 만든 제주 바이브가 듬뿍 담긴 다양한 소품들을 만나보세요.\n" +
                                "\n" +
                                "다양한 먹거리부터 캔들, 방향제, 마그넷, 엽서 등 제주스러운 기념품들을 가족, 친구, 직장동료, 사랑하는 사람들에게 선물하세요.\n" +
                                "\n" +
                                "* 매장에서 10만원 이상 구매 시 '무료 택배 서비스'를 제공합니다. 마음은 든든하게 몸은 가볍게 제주를 여행하세요!\n" +
                                "\n" +
                                "제스토리의 인기상품 '우드프린트'\n" +
                                "제주를 여행하며 찍은 기념사진을 소나무 원목에 인화해 드립니다. 제작 시간 단, 5분! 우리가 함께한 제주의 순간들을 원목에 담아 언제든지 제주를 추억해 보세요.\n" +
                                "\n" +
                                "1, 2층으로 이뤄진 제스토리는 2층 넓은 통창으로 아름다운 제주 남쪽 바다가 한 눈에 보입니다. 제스토리에서 나만의 제주 아이템을 찾고 바닷가를 바라보며 힐링하는 시간도 가져보세요 : )\n","연중무휴",
                        "https://www.jejubest.com/","064-738-1134",33.2361183 ,126.5149992,"제주 서귀포시 막숙포로 60","기념품", imagePaths[8]);

                createShoppingData("우디버디 ","12:00","18:00", "제주 소품샵 우디버디\n" +
                                "\n" +
                                "[ 제주공항 10분, 제주 동문시장 차로 5분 이내 ]\n" +
                                "[ 탑동광장, 칠성로 쇼핑거리 걸어서 5분이내 ]\n" +
                                "\n" +
                                "인테리어 소품, 키링 및 악세사리 등 다양한 소품을 판매하고 있습니다.\n" +
                                "자세한 상품은 인스타그램을 확인해 주세요 !!\n" +
                                "\n" +
                                "Waiting for you, My friend\n","목요일",
                        "https://www.instagram.com/woodyboddy_jeju","0507-1423-2048",33.5156677 ,126.5212036,"제주 제주시 북성로 31 1층 우디버디","기념품", imagePaths[9]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void createShoppingData(String title, String businessHoursStart,String businessHoursEnd, String description,String closedDay,
                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
        MultipartFile thumbnail = getMultipartFile(imagePath);

        // Shopping 데이터 생성
        shoppingService.create(
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
                "쇼핑", // 카테고리
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