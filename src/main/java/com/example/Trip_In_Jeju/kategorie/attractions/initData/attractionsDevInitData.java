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
//@Profile("prod")
@Profile("dev")
@RequiredArgsConstructor
public class attractionsDevInitData implements BeforeIntiData {

    private final AttractionsService attractionsService;
    // 다른 서비스들도 동일하게 주입


    @Bean(name = "attractionsInit")
    public ApplicationRunner init() {
        return args -> {
            try {
                // 현재 작업 디렉토리 출력
                String currentDir = System.getProperty("user.dir");
                System.out.println("현재 작업 디렉토리: " + currentDir);

                // 여러 이미지 파일 경로 설정 (프로젝트 내부 경로로 수정)
//                String[] imagePaths = {
//                        "/app/resources/static/images/attractions/그리스신화박물관.jpeg",
//                        "/app/resources/static/images/attractions/항파두리항몽유적지.jpeg",
//                        "/app/resources/static/images/attractions/한라산.jpeg",
//                        "/app/resources/static/images/attractions/삼성혈.jpeg",
//                        "/app/resources/static/images/attractions/넥슨컴퓨터박물관.jpeg",
//                        "/app/resources/static/images/attractions/새별오름.jpeg",
//                        "/app/resources/static/images/attractions/루나폴.jpeg",
//                        "/app/resources/static/images/attractions/용눈이오름.jpeg",
//                        "/app/resources/static/images/attractions/피규어뮤지엄제주.jpeg",
//                        "/app/resources/static/images/attractions/신화테마파크.jpeg"
//                };

                String[] imagePaths = {
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/그리스신화박물관.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/항파두리항몽유적지.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/한라산.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/삼성혈.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/넥슨컴퓨터박물관.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/새별오름.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/루나폴.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/용눈이오름.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/피규어뮤지엄제주.jpeg",
                        "C:/work/Trip_In_Jeju/src/main/resources/static/images/attractions/신화테마파크.jpeg"
                };

                // 파일 경로를 절대 경로로 변경하여 로깅
                File file = new File(imagePaths[0]);
                System.out.println("이미지 파일 절대 경로: " + file.getAbsolutePath());

                // 여러 Attractions 데이터 생성
                createAttractionsData("그리스신화박물관","09:00","18:00", "\u200B\u200B상상력과 창의력의 뿌리, 3,000년 역사의 지혜이자 현대인의 필수 교양인문인 그리스신화를 트릭아이 작품으로 제주에서 만나는 신개념의 그리스신화 박물관이다. 루브르, 바티칸박물관 명화와 대리석 조각상 200점 재현되어 있다. \u200B\u200B\u200B\u200B\u200B\u200B\u200B관람객 모두가 그리스인으로 변신하는 '리틀그리스' 그리스 신화를 주제로 한 트릭아이 작품과 함께 아테네 여신, 헤라클레스 등 신화 속 주인공이 되어보는 새로운 예술 체험 등 서양문명의 근원이 되는 그리스신화를 직접 체득하면서 자연스럽게 익힐 수 있다.\u200B\u200B","월요일",
                        "https://blog.naver.com/greek_trick","064-773-5800",33.34861301710886,126.35713527034747,"제주 제주시 한림읍 광산로 942","박물관/유적지", imagePaths[0]);
                createAttractionsData("항파두리항몽유적지","09:00","18:00", "1997년 4월 18일 사적으로 지정되었다. 면적은 1,135,476㎡이다. 1270년(원종 11) 2월 고려 조정이 몽골의 침입으로 굴욕적인 강화를 맺고 강화에서 개경으로 환도하자, 이에 맞서 김통정을 총수로 한 삼별초가 고려의 김방경과 몽골의 흔도가 이끄는 여몽연합군에 최후까지 항쟁하다 1273년 전원이 순의한 삼별초의 마지막 보루이다.\n" +
                                "\n" +
                                "해발 190∼215m 지점에 있는 항파두리 토성은 1271년 여몽연합군에 대항하던 삼별초군이 진도에 용장성을 쌓고 주둔하며 활동하다가 여몽연합군에게 패배하고, 같은 해 9월에 제주특별자치도로 들어와 군사력을 재정비하는 시기에 축성한 것이다. 본래 토성(높이 5m, 너비3.4m)으로 총길이 6km에 이르는 외성을 쌓고 안에 다시 석성으로 800m의 내성을 쌓은 이중 성곽이었으며, 각종 방어시설뿐 아니라 궁궐과 관아까지 갖춘 요새였다.","연중무휴",
                        "http://www.jeju.go.kr/hangpadori/index.htm","064-710-6726",33.4527478729655,126.407922584204,"제주 제주시 애월읍 항파두리로 50","박물관/유적지", imagePaths[1]);
                createAttractionsData("한라산","00:00","00:00", "한라산은 신령스러운 산이라 하여 조정에서 해마다 산정에서 국태민안을 비는 산제(山祭)를 지냈는데, 산제를 지내러 갔던 백성들이 동사하기도 하였다. 이에 1469년(예종 1) 목사 이약동(李約東)은 지금의 산천단(山泉壇)에 산신묘를 세우고 이곳에서 산제를 지내도록 하여 그 석단이 지금까지 남아 있다.\n" +
                                "\n" +
                                "한라산은 제주도의 전역을 지배하며, 동심원상의 등고선을 나타내어 순상화산(楯狀火山)에 속한다. 한라산은 약 360개의 측화산(側火山)과 정상부의 백록담, 해안지대의 폭포와 주상절리(柱狀節理: 다각형 기둥모양의 금) 등의 화산지형, 난대성기후의 희귀식물 및 고도에 따른 식생대(植生帶)의 변화 등 남국적(南國的)인 정서를 짙게 풍겨 세계적인 관광지로 발전할 수 있는 자원을 갖추고 있다. 그리하여 1970년에 한라산국립공원으로 지정되었다.","연중무휴",
                        "www.jeju.go.kr/hallasan","064-713-9950",33.361424638771474,126.52941927591584,"제주 서귀포시 토평동 산15-1","자연경관", imagePaths[2]);
                createAttractionsData("삼성혈","09:00","18:00", "처음 조성될 당시만 해도 개소리, 닭소리가 들리지 않는 인가(人家)와 멀리 떨어진 신성한 곳으로서 탐라국 시조를 모셔 제사를 지내기에 적소였다. 그러나 이제는 주변에 광양성당, 광양초등학교, 보성시장과 주택가가 자리하는 곳으로 변하였다. 사적으로 지정된 지역은 모두 3만 3,833㎡에 이르며 돌담이 둘러쳐진 가운데 전체적으로 원형을 이루고 있다.\n" +
                                "\n" +
                                "원형의 돌담은 각석을 겹담으로 쌓아 둘렀다. 삼성혈은 조선시대 1526년(중종 21) 목사 이수동(李壽童)이 돌 울타리를 쌓고 혈(穴) 북쪽에 홍문(紅門)과 혈비(穴碑)를 세워 후손들에게 혈제(穴祭)를 지내게 함으로써 성역화되었고 1772년(영조 48)에 양세현(梁世絢) 목사가 바깥 담장을 쌓아 소나무를 심게 하고 제전(祭田)을 마련하여 향청(鄕廳)으로 하여금 제사를 지내게 하였다. 삼성혈을 자세히 살펴보면 입구, 제의 준비처, 제의처, 전시관 등으로 4개 지역으로 구성되었음을 알 수 있다.","연중무휴",
                        "http://samsunghyeol.or.kr","064-722-3315",33.50442796435783,126.52876306017112,"제주 제주시 삼성로 22","박물관/유적지", imagePaths[3]);
                createAttractionsData("넥슨컴퓨터박물관","10:00","22:00", "","월요일",
                        "http://example.com","064-123-4567",33.5101,126.5215,"제주특별자치도 제주시 연동","자연경관", imagePaths[4]);
                createAttractionsData("새별오름","00:00","00:00", "해발 519.3m, 높이 119m 인 기생화산으로 분화구의 형태는 복합형이다. 오름을 오르는 입구에서 약 30분 정도면 정상에 도착할 수 있다. 오래전부터 가축을 방목하였으며 겨울이면 들불을 놓았다. 이런 이유로 이곳에서는 들불축제가 열렸다. 오름엔 들불을 놓은 후 자란 풀이 고운 풀밭을 이루고 있으며 정상의 5개의 봉우리는 서로 이어지면서 근처의 오름으로 연결되어 있다. 오름의 서쪽 등성이는 매우 가파르다. 고려말 새별오름에서 '목호의 난' 이 일어났으며 최영장군의 토벌대가 난을 진압했다는 기록이 전해지고 있다.","연중무휴",
                        "#","000-0000-0000",33.366277591139294,126.3577306657398,"제주 제주시 애월읍 봉성리 산59-8","자연경관", imagePaths[5]);
                createAttractionsData("루나폴","20:30","00:00", "루나폴은 12만평 규모의 나이트 디지털 테마파크 입니다. '예로부터 사람들은 달에 소원을 빌었고, 소원이 가득 쌓여 무거워진 달이 제주에 떨어졌다' 라는 스토리를 기반으로 밤길을 걸으며 즐기는 몰입형 체험 콘텐츠를 제공합니다. 루나폴만의 세계관과 제주의 청정자연, 최정상 실감미디어 기술력이 만난 루나폴 판타지를 즐겨보세요.","연중무휴",
                        "https://www.instagram.com/lunafall_jeju","064-794-9680",33.254147796962805,126.32234760295749,"제주 서귀포시 안덕면 일주서로 1836 루나폴","테마파크", imagePaths[6]);
                createAttractionsData("용눈이오름","00:00","00:00", "해발 247.8m, 높이 88m, 둘레 2,685m, 면적 40만 4264㎡이다. 송당에서 성산 쪽으로 가는 중산간도로(16번 국도) 3㎞ 지점에 있다. 오름이란 자그마한 산을 뜻하는 제주특별자치도 방언으로 개개의 분화구를 갖고 있고 화산쇄설물로 이루어져 있으며 화산구의 형태를 갖추고 있는 한라산 산록의 기생화산구를 의미한다. 용이 누워 있는 모양이라고도 하고 산 한가운데가 크게 패어 있는 것이 용이 누웠던 자리 같다고도 하고 위에서 내려다 보면 화구의 모습이 용의 눈처럼 보인다 하여 붙여진 이름으로, 한자로는 용와악(龍臥岳)이라고 표기한다.\n" +
                                "\n" +
                                "용암 형설류의 언덕이 산재해 있는 복합형 화산체로, 정상에 원형분화구 3개가 연이어 있고 그 안에는 동서쪽으로 조금 트인 타원형의 분화구가 있다. 전체적으로 산체가 동사면 쪽으로 얕게 벌어진 말굽형 화구를 이룬다. 오름기슭은 화산체가 형성된 뒤 용암류의 유출로 산정의 화구륜 일부가 파괴되면서 용암류와 함께 흘러내린 토사가 이동하여 퇴적된 용암암설류의 언덕이 산재해 있다. 전사면이 잔디와 풀밭으로 덮여 있고 미나리아재비·할미꽃 등이 서식하고 있다. 정상의 분화구를 돌며 손자봉·다랑쉬오름·동거미오름 등을 볼 수 있으며 성산일출봉·우도·바다 등을 조망할 수 있다.","연중무휴",
                        "#","000-0000-0000",33.45988023593872,126.83268298189506,"제주 제주시 구좌읍 종달리 산28","자연경관", imagePaths[7]);
                createAttractionsData("피규어뮤지엄제주","10:00","18:00", "\u200B\u200B\u200B\u200B\u200B\u200B\u200B\u200B넥슨 컴퓨터 박물관은 게임회사 넥슨이 만든 컴퓨터 관련 박물관으로 컴퓨터와 게임의 역사를 만나볼 수 있다. 스티브 잡스가 맨 처음 만든 컴퓨터 APPLE1 을 비롯한 첫 세대 퍼스날 컴퓨터부터, 역사 속의 모니터, 키보드 등 컴퓨터 부속부품을 만나 볼 수 있다. 레이저를 이용한 빔 타자기와 VR 등 아직은 보편화 되지 않은 새로운 컴퓨터 용품을 체험해 보는 재미도 있다.\u200B\u200B \u200B 어른과 아이 모두 즐길 수 있는 추억의 오락실 게임과 최첨단 VR 게임을 접해볼 수 도 있다. 미리 신청하면 어린이들을 위한 코딩 프로그램에 참여할 수 있으며 1층에는 키보드 모양 와플이 유명한 레스토랑 '인트'가 운영된다.\u200B\u200B\u200B","연중무휴",
                        "https://computermuseum.nexon.com/","064-745-1994",33.47176036535081,126.4849881396272,"제주 제주시 1100로 3198-8 넥슨컴퓨터박물관","박물관/유적지", imagePaths[8]);
                createAttractionsData("신화테마파크","10:00","21:00", "스릴 넘치는 놀이기구와 즐거운 이벤트가 펼쳐지는 신화테마파크\n" +
                                "\n" +
                                "제주 유일 롤러코스터를 포함한 다양한 어트랙션, 귀엽고 신비로운 공연과 맛있는 식음 매장까지 모두 즐겨보세요.","연중무휴",
                        "https://shinhwaworld.com/article.aspx?lang=KR&type=897","1670-1188",33.30499182173955,126.31517828651978,"제주 서귀포시 안덕면 신화역사로304번길 98 신화테마파크","테마파크", imagePaths[9]);
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