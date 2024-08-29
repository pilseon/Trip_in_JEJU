//package com.example.Trip_In_Jeju.kategorie.other.initData;
//
//
//import com.example.Trip_In_Jeju.kategorie.other.service.OtherService;
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
//public class otherDevInitData implements BeforeIntiData {
//
//    private final OtherService otherService;
//    // 다른 서비스들도 동일하게 주입
//
//
//    @Bean(name = "otherInit")
//    public ApplicationRunner init() {
//        return args -> {
//            try {
//                // 현재 작업 디렉토리 출력
//                String currentDir = System.getProperty("user.dir");
//                System.out.println("현재 작업 디렉토리: " + currentDir);
//
//                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
//                String[] imagePaths = {
//                        "/app/resources/static/images/other/아꼬운냥.jpeg",
//                        "/app/resources/static/images/other/다짐헬스.png",
//                        "/app/resources/static/images/other/킹스짐.jpeg",
//                        "/app/resources/static/images/other/유휘트니스.jpeg",
//                        "/app/resources/static/images/other/뽀로로앤타요.jpeg",
//                        "/app/resources/static/images/other/콩순이키즈카페.jpeg",
//                        "/app/resources/static/images/other/바닷가하우스.jpeg",
//                        "/app/resources/static/images/other/댕댕샤워.jpeg",
//                        "/app/resources/static/images/other/배콩스.jpeg",
//                        "/app/resources/static/images/other/쁘띠레브.jpeg"
//
//                };
//
//                // 파일 경로를 절대 경로로 변경하여 로깅
//                File file = new File(imagePaths[0]);
//                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());
//
//                // 여러 Other 데이터 생성
//                createOtherData("아꼬운냥","09:00","18:00", "고양이무마취미용샵 (전)미냥 (현)아꼬운냥 입니다^^","수요일",
//                        "https://blog.naver.com/akkoun__n","010-5802-1213",33.4885989758497,126.47991733783,"제주 제주시 다랑곶4길 22 1층 아꼬운냥","반려동물", imagePaths[0]);
//                createOtherData("다짐 헬스&PT 이도점","09:00","23:00", "24년 5월부터\n" +
//                                "새로운 대표가 운영하는\n" +
//                                "체육학전공 지도자들만 있는\n" +
//                                "헬스장 다짐입니다.\n" +
//                                "완벽한 트레이닝을 위해\n" +
//                                "12년동안 끊임없이 노력해왔습니다.\n" +
//                                "한분 한분 목표달성을 위해\n" +
//                                "진심으로 지도해드립니다.\n" +
//                                "아무리 좋은 운동도\n" +
//                                "지속하지 못 한다면\n" +
//                                "배우지 않은 것과 다름없습니다.\n" +
//                                "이해하기 쉽게,\n" +
//                                "지속하실 수 있도록\n" +
//                                "고객의 니즈에 맞추어 트레이닝합니다.","연중무휴",
//                        "https://blog.naver.com/dagym_jeju","0507-1303-1783",33.4966948275828,126.532285100887,"제주 제주시 중앙로 265 성우빌딩 4층","피트니스", imagePaths[1]);
//                createOtherData("킹스짐","00:00","00:00", "제주도대표 보디빌더이자, 제32회 미스터제주 그랑프리 김기원 대표가 직접 운영하며, 자타공인 각 종목별 최고의 선생님들이 여러분을 기다리고 있습니다.\n" +
//                                "또한, 최고급 웨이트 기구인 'MATRIX'가 풀셋팅 되어 있고, 최고급 태닝머신인 '에르고라인' 까지 설치 되어있는 있습니다.\n" +
//                                "저희 킹스짐은 운동과 태닝이 한곳에서 가능하며, 언제든 운동이 가능한 <제주 24시 헬스장> 입니다 !!\n","연중무휴",
//                        "https://blog.naver.com/qhtmzzz","0507-1486-3213",33.48774323351661,126.49030151390139,"제주 제주시 신광로 47 3층","피트니스", imagePaths[2]);
//                createOtherData("유휘트니스","06:00","00:00", "제주 해머스트랭스 오피셜 센터\n" +
//                                "신제주 최대규모, 호텔급 프리미엄 휘트니스\n" +
//                                "신제주 가장 중심지인 구)KBS 자리에\n" +
//                                "건물주가 직접 운영하는\n" +
//                                "청결하고, 주차 편한 헬스장이 있습니다\n" +
//                                "- 뛰어난 접근성과 700평대 주차시설 완비\n" +
//                                "- 320평대 최대규모! 호텔급 프리미엄 운동시설\n" +
//                                "- 1인 샤워부스 설치\n" +
//                                "- 운동복 / 수건 / 개인락커 완비\n" +
//                                "- 3시간 무료 주차 지원\n" +
//                                "- 해머스트렝스 오피셜센터","연중무휴",
//                        "https://blog.naver.com/ecodnc_jeju","0507-1338-7221",33.4880441442225,126.496200538548,"제주 제주시 신대로 104 유휘트니스 센트럴 연동점 2층","피트니스", imagePaths[3]);
//                createOtherData("뽀로로앤타요 테마파크","10:00","18:00", "국내 최대 규모의 캐릭터 어트랙션 테마파크가 제주에 상륙했다!\n" +
//                                "실내 2천평, 캐릭터 공원과 푸른 잔디가 펼쳐진 야외 8천 평 규모의 테마파크를 만나보세요.","연중무휴",
//                        "https://www.pororopark.com/branch/park_tour.php?uid=30&park_uid=lnb30","064-742-8555",33.30041822583184,126.37082203402295,"제주 서귀포시 안덕면 병악로 269","어린이", imagePaths[4]);
//                createOtherData("콩순이키즈카페","10:30","20:00", "아이들이 사랑하는 인기 애니메이션 케릭터인 콩순이를 테마로 꾸민 제주도 키즈카페 입니다. 아이들을 위한 교육프로그램과 놀이 공간 뿐 아니라 부모님들의 쾌적한 휴식 공간으로도 완벽하게 구성한 프리미엄 키즈카페로 행복한 시간을 약속 드리겠습니다.","목요일",
//                        "https://www.instagram.com/kongsuni_jeju/","064-805-8660",33.4822261139418,126.51127356238,"제주 제주시 연사2길 82 1층","어린이", imagePaths[5]);
//                createOtherData("제주 바닷가하우스 키즈 펜션","00:00","00:00", "전객실 리모델링 OPEN !\n" +
//                                "리모델링으로 깔끔하고 새로워진 제주 키즈 가족 바닷가하우스 펜션 !\n" +
//                                "양손 가볍게 오실 수 있는 제주 바닷가하우스 키즈 펜션에서 아이들과 즐거운 추억을 만들어보세요 :)","연중무휴",
//                        "https://seasidekids.kr/","064-794-0977",33.2122170774996,126.292050430409,"제주 서귀포시 대정읍 형제해안로 254 바닷가하우스","어린이", imagePaths[6]);
//                createOtherData("댕댕샤워 애견미용실","08:00","22:00", "애견미용/가위컷전문/애견스파/반려견 토탈케어샵입니다","연중무휴",
//                        "https://www.instagram.com/dogspaa/","0507-1477-8596",33.4827996887206,126.414759090429,"제주 제주시 애월읍 하귀9길 54-8 1층","반려동물", imagePaths[7]);
//                createOtherData("베콩스 애견호텔&놀이터","10:00","20:00", "안녕하세요.\n" +
//                                "강아지의 편안한 휴식지 베콩스 애견호텔&놀이터 입니다.\n" +
//                                "제주 함덕리에 위치한 애견호텔로\n" +
//                                "24시간 아이들과 상주하며\n" +
//                                "깨끗하고 위생적인 공간과 합리적인 가격을 제공해 드립니다.","연중무휴",
//                        "https://www.instagram.com/vacance_jeju/","0507-1323-3560",33.5435377708903,126.661624870544,"제주 제주시 조천읍 신북로 479-2 2층 베콩스","반려동물", imagePaths[8]);
//                createOtherData("쁘띠레브","08:00","20:00", "화~금 유치원 08:00 ~ 20:00 / 데이케어 09:00 ~ 19:00\n" +
//                                "토요일, 일요일, 법정공휴일은 10:00 ~ 19:00\n" +
//                                "아침 픽업 일정으로 8시30분전 등원 시 미리 말씀해주셔야 합니다.\n" +
//                                "반려동물 행동교정 , 관리 , 식품영양 자격증 취득\n" +
//                                "10kg 이하 소형견 전용 공간","월요일",
//                        "https://blog.naver.com/likedragon77","0507-1441-3222",33.4911797928058,126.493524563193,"제주 제주시 삼무로7길 29","반려동물", imagePaths[9]);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    private void createOtherData(String title, String businessHoursStart,String businessHoursEnd, String description,String closedDay,
//                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
//        MultipartFile thumbnail = getMultipartFile(imagePath);
//
//        // Other 데이터 생성
//        otherService.create(
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
//                "기타", // 카테고리
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