package com.example.Trip_In_Jeju.kategorie.activity.initData;


import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
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
public class activityDevInitData implements BeforeIntiData {

    private final ActivityService activityService;
    // 다른 서비스들도 동일하게 주입


    @Bean(name = "activityInit")
    public ApplicationRunner init() {
        return args -> {
            try {
                // 현재 작업 디렉토리 출력
                String currentDir = System.getProperty("user.dir");
                System.out.println("현재 작업 디렉토리: " + currentDir);

                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
                String[] imagePaths = {
                        "/app/resources/static/images/activity/감귤카트.jpg",
                        "/app/resources/static/images/activity/엑티브파크.jpg",
                        "/app/resources/static/images/activity/와따.png",
                        "/app/resources/static/images/activity/대유수렵사격.jpg",
                        "/app/resources/static/images/activity/어생승마장.png",
                        "/app/resources/static/images/activity/해빛다이브.jpg",
                        "/app/resources/static/images/activity/감귤나무숲.jpg",
                        "/app/resources/static/images/activity/영희네농장.jpg",
                        "/app/resources/static/images/activity/해녀태왁.jpg",
                        "/app/resources/static/images/activity/981파크.jpg"


                };

                // 파일 경로를 절대 경로로 변경하여 로깅
                File file = new File(imagePaths[0]);
                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());

                // 여러 Activity 데이터 생성
                createActivityData("감귤카트","09:00","18:30", "감귤카트에 오신 것을 환영합니다!\n" +
                                "\n" +
                                "※ 구매 후 30분 인증 가능!\n" +
                                "\n" +
                                "상큼한 감귤모자를 쓰고 달리며 짜릿한 속도감을 느낄 수 있는\n" +
                                "카트레이싱 체험, 감귤카트!\n" +
                                "\n" +
                                "※ 중학생&150cm 이상 단독 탑승 가능합니다.(증빙확인, 보호자동반)\n" +
                                "※ 2인승 탑승 시 1인승 티켓에 성인 10,000원 / 청소년 소아 8,000원 현장에서 추가 결제 후 탑승 가능합니다.\n" +
                                "\n" +
                                "최고의 시설과 안전함, 무엇과도 비교할 수 없는 제주의 아름다운 자연 속에서 쌓인 스트레스를 한방에 날려버리세요.\n" +
                                "\n" +
                                "가족, 연인, 친구, 단체 누구나 체험 가능한 안전한 레저스포츠!\n" +
                                "감귤카트로 여러분을 초대합니다.\n","연중무휴",
                        "http://www.alivekart.co.kr/","064-805-0992",33.2549444,126.4078378,"제주 서귀포시 중문관광로 42 감귤카트(맥도날드 안쪽)","레저/오락", imagePaths[0]);

                createActivityData("엑티브파크 제주","09:30","18:00", "[BTS 이게맞아 촬영지 액티브파크]\n" +
                                "\n" +
                                "[제주도민 할인을 받으시려면 공식 홈페이지를 통해 예약바랍니다.]\n" +
                                "\n" +
                                "<제주 액티비티 종합편 : 액티브파크>\n" +
                                "\n" +
                                "[액티브파크 제대로 즐기기 PART 1]\n" +
                                "\n" +
                                "액티브파크 클립앤클라임은 뉴질랜드에서 시작되어 전 세계190여 개 센터로 운영되는 클라이밍 놀이 시설이며, 액티브하고 짜릿한 경험을 제공하며 활동적인 레포츠를 즐기고 싶은 분들에게 적극 추전 합니다.\n" +
                                "\n" +
                                "[액티브파크 제대로 즐기기 PART 2]\n" +
                                "\n" +
                                "액티브파크 국내에서 보기 드문 야자수와 천연 잔디로 조성된 친환경 카트 트랙 위에서 스피디 하고 바람을 가르는 짜릿한 질주 체험을 느낄 수 있는 카트 체험장이 있어 일상의 스트레스를 시원하게 날려버릴 수 있습니다!\n" +
                                "\n" +
                                "[액티브파크 제대로 즐기기 PART 3]\n" +
                                "\n" +
                                "그래도 더 놀고 싶으시다구요? 끌어 오르는 에너지를 더 발산하고 싶다구요?그렇다면 액티브파크 키즈카페 PLAY BANG으로 오세요!! 플레이방은 호기심 가득한 우리 아이들에게 꿈을 심어주는 마법 같은 공간이랍니다.\n" +
                                "\n" +
                                "어른도, 어린이도 행복한 제주 액티브파크!\n" +
                                "눈이 와도, 비가 와도\n" +
                                "이 곳에선 언제나 즐거운 웃음이 넘친답니다.\n" +
                                "\n" +
                                "다양한 체험 거리가 가득한 액티브파크,\n" +
                                "지금 도전해볼까요?\n" +
                                "\n" +
                                "* 제주 액티비티 총집합! 액티브파크 *","연중무휴",
                        "http://activepark.co.kr","0507-1461-0881",33.3812142,126.2360938,"제주 제주시 한림읍 금능남로 76 액티브파크 제주","레저/오락", imagePaths[1]);

                createActivityData("제주와따해양레저  ","10:00","18:00", "제주와따해양레저는 제주특별자치도 서귀포시\n" +
                                "표선해수욕장 안쪽에 자리잡고있습니다~^^\n" +
                                "2024년 새로운 최신장비와 해양레저체험에서 즐길수있는모든장비를 준비하고 여러분을 기다립니다\n" +
                                "현장에 인명구조요원 상시 배치로 더욱더 안전하고 즐겁고 신나고 추억에 남는 해양레저스포츠로 보답하겠습니다.\n","연중무휴",
                        "https://www.instagram.com/jeju_watta/","010-7918-1199",33.2777041 ,126.3953828,"제주특별자치도 제주시 연동","레저/오락", imagePaths[2]);

                createActivityData("대유ATV수렵사격랜드","09:00","19:00", "제주도 천혜의 자연에서 온 몸으로 스릴을 느낄 수 있는 레저 스포츠!\n" +
                                "\n" +
                                "1백만 평에 이르는 광활한 대자연을 벗 삼아 연중무휴로 즐길 수 있는 수렵과 권총(pistol), 클레이(clay), 라이플 사격(rifle), ATV (4륜 오토바이), 버기카(utv)체험할 수 있는 대유ATV수렵사격랜드입니다.","연중무휴",
                        " https://www.instagram.com/daeyooland.jeju/","064-738-0500",33.2777041 ,126.5215,"제주 서귀포시 상예로 381","스포츠", imagePaths[3]);

                createActivityData("어승생승마장","09:00","19:00", "\u200B\u200B경치 좋은 어승생승마장 제주공항과 차로 20분거리, 제주 자연속에서 다양한 체험과 편안한 휴식을 즐길수 있는 공간입니다. 해발 530m의 위치한 어승생승마장은 한라산과 바다가 한눈에 보이는 이국적인 곳입니다. 여러분에게 추억을 만들어 줄 자유롭게 뛰노는 말들과 한폭의 수채화 같이 하늘과 초원이 어우러진 풍경을 볼 수 있습니다. 우리 목장은 여러분의 마음속에 있는 스트레스를 잊어버리고, 편하게 만들어 줄 것입니다.\u200B\u200B","연중무휴",
                        "http://www.jejuhorse.com/"," 0507-1366-5538",33.425633 ,126.490871,"제주 제주시 1100로 2659","스포츠", imagePaths[4]);

                createActivityData("해빛다이브 ","00:00","24:00", "문의사항은\n" +
                                "카카오채널: 해빛다이브\n" +
                                "\n" +
                                "1. 25인승 크기의 다이빙 전용선 보유(다이버들의 편리를 위해 12분까지 탑승 지향!)\n" +
                                "2. 숙소, 맛집이 많은 서귀포 자구리공원 바다 앞 최고의 위치\n" +
                                "3. 자가장비 부럽지않은 최신 렌탈장비\n" +
                                "4. 신선한 공기를 마실 수 있게 준비한 새 콤프레셔&공기통\n" +
                                "5. 휴식시 쾌적한 환경에서 편안하게 쉴 수 있게 준비된 실내공간 & 셀프로 사용 가능한 오픈형 주방\n" +
                                "6. 전자칠판으로 진행하는 입체적 펀다이빙 브리핑\n" +
                                "7. 전자칠판을 각각 비치한 10인 규모 교육실 2실\n" +
                                "8. 개인 부스로 만들어진 넓은 샤워실\n" +
                                "9. 7대의 온수가 나오는 세척대&샤워헤드\n" +
                                "10. 개인 물건이나 귀중품을 보관할 수 있는 캐비넷\n" +
                                "11. 문섬 섶섬이 보이는 오션 뷰 테라스\n" +
                                "12. 공항에서 600번 리무진 버스타고 한 번에 올 수 있는 센터\n" +
                                "\n" +
                                "*다이빙의 새로운 기준을 제시합니다.","연중무휴",
                        "https://haevitdive.com","064-767-1970",33.2431344 ,126.5675779,"제주 서귀포시 칠십리로 123 1층 해빛다이브","스포츠", imagePaths[5]);

                createActivityData("감귤나무숲","10:00","17:00", "청정제주에서 진행하는 풋귤, 감귤 그리고 한라봉체험!\n" +
                                "\n" +
                                "아름다운 야외풍경과 예쁜 돌담, 동화같은 분위기의 아기자기하고 귀여운 소품들이 가득하고 연인,친구,가족끼리 찍기 좋은 포토존이 많아요!\n","연중무휴",
                        "https://www.instagram.com/jejudrforest/","0507-1416-7733",33.5002739392299 ,126.587130652046,"제주 제주시 도련남3길 81","체험활동", imagePaths[6]);

                createActivityData("영희네농장","10:00","16:00", "영희네농장에서는 친환경 인증 무농약으로 재배한 감귤입니다.\n" +
                                "감귤따기 체험 은 8월부터 9월15일까지 청귤따기\n" +
                                "10월말부터 2월까지 조생 온주밀감따기입니다.\n" +
                                "출발 한시간 전에 연락주세요.","연중무휴",
                        "http://jeju4546.modoo.at/","0507-1418-1014",33.4038137 ,126.900323,"제주 서귀포시 성산읍 고성리 2559-1","체험활동", imagePaths[7]);

                createActivityData("해녀태왁 ","10:30","24:00", "(예약제 입니다!)\n" +
                                "푸른 바다가 보이는 아름다운 김녕 마을. 이 곳에는 바다 물질에 평생을 바쳐온 해녀 어머니, '삼춘들'이 계십니다. 계속되는 고령화에도 이 분들이 지속적으로 일하실 수 있도록 이 '태왁 만들기' 체험이 탄생했답니다푸른 바다가 보이는 아름다운 김녕 마을. 이 곳에는 바다 물질에 평생을 바쳐온 해녀 어머니, '삼춘들'이 계십니다. 계속되는 고령화에도 이 분들이 지속적으로 일하실 수 있도록 이 '태왁 만들기' 체험이 탄생했답니다해녀복체험과.태왁만들기체험과 해녀문화와 태왁의 의미를 한군데서 모두만나보실수있는\n" +
                                "해녀체험.제주이색체험입니다~어디서도 볼수없고 만들수없는 상시체험입니다\n","연중무휴",
                        "https://m.booking.naver.com/booking/12/bizes/242281?area=pll","064-782-6347",33.5036504 ,126.4657145,"제주 제주시 구좌읍 김송로 94","체험활동", imagePaths[8]);

                createActivityData("9.81파크","09:00","18:20", "애월 바다와 한라산 사이의 스마트 놀이+터! 중력가속도(9.81m/s²)만으로 스피드를 즐기면 레이싱 기록과 고화질 영상을 받을 수 있는 짜릿한 레이싱부터 실내 레이저 서바이벌, 하늘그네, 범퍼카까지 다채로운 액티비티와 먹거리 즐길거리 가득한 테마파크\n" +
                                "\n" +
                                "_무료 셔틀 운영\n" +
                                "제주공항 > 롯데면세점 > 9.81파크\n" +
                                "9.81파크 > 롯데면세점(맞은편) > 제주공항\n" +
                                "*자세한 시간은 9.81파크 인스타그램을 참고하세요!","연중무휴",
                        "http://www.981park.com","1833-9810",33.3899391 ,126.3663248,"제주 제주시 애월읍 천덕로 880-24","엑티비티", imagePaths[9]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void createActivityData(String title, String businessHoursStart,String businessHoursEnd, String description,String closedDay,
                                String websiteUrl,String phoneNumber,double latitude, double longitude,String address, String subCategory, String imagePath) throws IOException {
        MultipartFile thumbnail = getMultipartFile(imagePath);

        // Activity 데이터 생성
        activityService.create(
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
                "액티비티", // 카테고리
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
