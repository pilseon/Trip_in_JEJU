package com.example.Trip_In_Jeju.member.controller;

import com.example.Trip_In_Jeju.DataNotFoundException;
import com.example.Trip_In_Jeju.calendar.service.CalendarService;
import com.example.Trip_In_Jeju.email.service.EmailService;
import com.example.Trip_In_Jeju.email.service.VerificationCodeService;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
import com.example.Trip_In_Jeju.location.entity.VisitRecord;
import com.example.Trip_In_Jeju.location.service.VisitRecordService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.entity.MemberRole;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import com.example.Trip_In_Jeju.scrap.Scrap;
import com.example.Trip_In_Jeju.scrap.ScrapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final EmailService emailService;
    private final MemberService memberService;
    private final VerificationCodeService verificationCodeService;
    private final CalendarService calendarService;
    private final FestivalsService festivalsService;
    private final ScrapService scrapService;
    private final VisitRecordService visitRecordService;
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("/myPage")
    public String myPage(
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "id", required = false) Long id,
            Model model) {

        Member currentMember = memberService.getCurrentMember();
        if (currentMember == null) {
            logger.warn("No current member found in myPage method");
            return "redirect:/member/login";
        }
        model.addAttribute("member", currentMember);

        if (date == null) {
            date = LocalDate.now();
        }
        LocalDate startOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);

        Festivals festivals = null;
        if (id != null) {
            festivals = festivalsService.getFestivalsById(id);
        }
        if (festivals == null) {
            id = getDefaultFestivalId();
            if (id != null) {
                festivals = festivalsService.getFestivalsById(id);
            }
        }
        model.addAttribute("festivals", festivals);

        List<com.example.Trip_In_Jeju.calendar.entity.Calendar> dailyCalendars = calendarService.findCalendarsWithFoodsBetween2(date, date);
        Set<com.example.Trip_In_Jeju.calendar.entity.Calendar> uniqueCalendars = new HashSet<>(dailyCalendars);
        System.out.println("Selected Date: " + date);
        uniqueCalendars.forEach(event -> System.out.println("Event: " + event));

        Map<LocalDate, List<com.example.Trip_In_Jeju.calendar.entity.Calendar>> eventsByDate = new HashMap<>();
        eventsByDate.put(date, new ArrayList<>(uniqueCalendars));

        // 스크랩 목록 가져오기
        List<Scrap> scraps = scrapService.getScrapsByMember(currentMember);

        // 축제만 포함하는 리스트
        List<Scrap> festivalScraps = scraps.stream()
                .filter(scrap -> scrap.getFestivals() != null)
                .collect(Collectors.toList());

        // 축제가 아닌 다른 항목들을 포함하는 리스트
        List<Scrap> nonFestivalScraps = scraps.stream()
                .filter(scrap -> scrap.getFestivals() == null)
                .collect(Collectors.toList());

        // 모델에 스크랩 리스트 추가
        model.addAttribute("festivalScraps", festivalScraps);
        model.addAttribute("nonFestivalScraps", nonFestivalScraps);

        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);
        model.addAttribute("weekDates", List.of(startOfWeek, startOfWeek.plusDays(1), startOfWeek.plusDays(2), startOfWeek.plusDays(3), startOfWeek.plusDays(4), startOfWeek.plusDays(5), startOfWeek.plusDays(6)));
        model.addAttribute("currentDate", date);
        model.addAttribute("eventsByDate", eventsByDate);

        return "member/myPage";
    }





    private Long getDefaultFestivalId() {

        List<Festivals> festivalsList = festivalsService.getAllFestivals();
        return festivalsList.isEmpty() ? null : festivalsList.get(0).getId();
    }



    @GetMapping("/admin")
    public String adminPage() {
        return "member/admin";
    }

//    @GetMapping("/signup")
//    public String signForm(Model model) {
//        return "member/signup"; // signup.html을 반환
//    }

    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("thema") String thema,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                         Model model,
                         HttpSession session) {

// 인증 코드 생성 및 이메일 전송
        String verificationCode = verificationCodeService.generateVerificationCode(email);
        String subject = "Trip_In_JEJU 회원 가입 인증";
        String body = String.format(
                "안녕하세요 %s님! Trip_In_Jeju에 가입해 주셔서 진심으로 감사드립니다.\n" +
                        "\n" +
                        "회원님께서 저희 커뮤니티의 일원이 되신 것을 환영합니다. 이제 제주도의 숨겨진 명소부터 잘 알려진 관광지까지, 다양한 여행 정보를 한곳에서 만나보실 수 있습니다. Trip_In_Jeju는 제주도의 자연과 문화, 음식 등 다채로운 매력을 소개하며, 여러분의 여행을 더욱 특별하게 만들어 드리기 위해 최선을 다하고 있습니다.\n" +
                        "\n" +
                        "저희와 함께 제주도를 탐험하며, 새로운 경험과 감동을 만나보세요. 편리한 여행 계획은 물론, 현지에서만 느낄 수 있는 색다른 즐거움까지, Trip_In_Jeju는 언제나 회원님의 멋진 여행을 응원합니다.\n" +
                        "\n" +
                        "앞으로도 저희와 함께 소중한 추억을 만들어 가시길 바라며, 많은 관심과 사랑 부탁드립니다. 궁금한 점이나 도움이 필요하신 경우 언제든지 연락해 주시면, 최선을 다해 도와드리겠습니다.\n" +
                        "\n" +
                        "다시 한번 감사드리며, 제주도에서의 멋진 여정을 기대합니다.\n" +
                        "\n" +
                        "감사합니다.\n" +
                        "\n" +
                        "Trip_In_Jeju 팀 드림",
                nickname
        );

        emailService.send(email, subject, body);

        session.setAttribute("verificationCode", verificationCode);

        memberService.signup(username, nickname, password, email, thema, thumbnail, MemberRole.MEMBER);

        return "redirect:/member/login";
    }

    @PostMapping("/modify")
    public String modify(@RequestParam("username") String username,
                         @RequestParam("nickname") String nickname,
                         @RequestParam(value = "password", required = false) String password,
                         @RequestParam("email") String email,
                         @RequestParam("thema") String thema,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                         Model model) {

        // 현재 로그인된 회원 정보를 가져옵니다.
        Member member = memberService.getCurrentMember();
        if (member == null) {
            // 사용자 정보가 없는 경우 로그인 페이지로 리다이렉트
            return "redirect:/member/login";
        }

        // 닉네임 중복 검사
        if (!member.getNickname().equals(nickname) && memberService.isNicknameDuplicate(nickname)) {
            model.addAttribute("nicknameError", "이미 사용 중인 닉네임입니다.");
            model.addAttribute("nicknameErrorColor", "red");
            model.addAttribute("member", member); // 기존 데이터 유지
            return "member/modify";
        }

        // 회원 정보를 수정합니다.
        memberService.modify(member, nickname, password, email, thema, thumbnail);

        // 수정 완료 후 마이페이지로 리다이렉트
        return "redirect:/member/myPage";
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username") String username) {
        boolean exists = memberService.usernameExists(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean exists = memberService.nicknameExists(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean exists = memberService.emailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/verifyCode")
    public String verifyCodeForm(Model model) {
        return "member/login"; // verifyCode.html을 반환
    }

    @PostMapping("/verifyCode")
    public String verifyCode(@RequestParam("verification") String verificationCode, HttpSession session) {
        String storedVerification = (String) session.getAttribute("verificationCode"); // 올바른 세션 키 사용
        if (verificationCode != null && verificationCode.equals(storedVerification)) {
            session.removeAttribute("verificationCode"); // 세션에서 올바른 키 제거
            return "member/login";
        } else {
            return "member/login";
        }
    }

    public static class LoginRequest {
        private String loginId;
        private String password;

        // Getters and setters
        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @GetMapping("/delete")
    public String showDeleteForm(Model model) {
        return "member/delete"; // 탈퇴 확인 폼 페이지로 이동
    }

    @PostMapping("/delete")
    public String deleteMember(@RequestParam("username") String username, HttpServletRequest request, HttpServletResponse response) {
        memberService.deleteMember(username);
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/deleteMember/{username}")
    public String deleteMemberByAdmin(@PathVariable("username") String username) {
        Member admin = memberService.getCurrentMember();
        if (admin == null || !memberService.isAdmin(admin)) {
            logger.warn("Unauthorized access attempt by user: {}", admin != null ? admin.getUsername() : "unknown");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }

        memberService.deleteMemberByAdmin(username);
        return "redirect:/admin/memberList";  // 회원 목록 페이지로 리다이렉트
    }

    // 아이디 찾기 시작
    @GetMapping("/find-username")
    public String showFindUsernamePage(Model model) {
        model.addAttribute("username", null); // 초기화: 아이디 결과 없음
        model.addAttribute("error", null); // 초기화: 에러 메시지 없음
        return "member/find-username"; // templates/user/find-username.html 경로에 파일이 있어야 함
    }

    @PostMapping("/find-username")
    public String processFindUsername(@RequestParam("email") String email, Model model) {
        try {
            String username = memberService.findUsernameByEmail(email); // 이메일로 아이디 찾기
            model.addAttribute("username", username); // 결과를 모델에 추가
            model.addAttribute("error", null); // 에러 메시지 초기화
        } catch (DataNotFoundException e) {
            model.addAttribute("username", null); // 아이디 결과 초기화
            model.addAttribute("error", "해당 이메일로 등록된 사용자가 없습니다."); // 에러 메시지 설정
        }
        return "member/find-username"; // 결과 표시 페이지로 이동
    }

    // 아이디 찾기 끝

    // 인증 코드 요청 폼 표시
    @GetMapping("/request-reset")
    public String showRequestResetForm() {
        return "member/request-reset";
    }

    // 인증 코드 요청 처리
    @PostMapping("/request-reset")
    public String sendVerificationCode(@RequestParam("username") String username,
                                       @RequestParam("email") String email,
                                       Model model) {
        try {
            String verificationCode = verificationCodeService.generateVerificationCode(email);
            emailService.sendVerificationCode(email, "비밀번호 재설정 인증 코드", "비밀번호를 재설정하려면 다음 인증 코드를 입력하세요:", verificationCode);

            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("message", "이메일로 인증 코드가 전송되었습니다.");
            return "member/verify-reset"; // 인증 코드 입력 폼으로 리턴
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/request-reset"; // 에러 발생 시 다시 요청 폼으로 리턴
        }
    }

    // 인증 코드 확인 및 비밀번호 재설정
    @PostMapping("/verify-reset")
    public String verifyReset(@RequestParam("username") String username,
                              @RequestParam("email") String email,
                              @RequestParam("verificationCode") String verificationCode,
                              @RequestParam("newPassword") String newPassword,
                              Model model) {
        try {
            if (verificationCodeService.verifyCode(email, verificationCode)) {
                memberService.resetPassword(username, email, newPassword);
                model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
                return "redirect:/member/login"; // 성공 시 로그인 페이지로 리다이렉트
            } else {
                model.addAttribute("error", "인증 코드가 일치하지 않습니다.");
                model.addAttribute("username", username);
                model.addAttribute("email", email);
                return "member/verify-reset"; // 인증 코드 입력 폼으로 리턴
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "member/verify-reset"; // 에러 발생 시 다시 인증 코드 입력 폼으로 리턴
        }
    }

    // 인증 코드 입력 폼 표시
    @GetMapping("/verify-reset")
    public String showVerifyResetForm(@RequestParam("username") String username,
                                      @RequestParam("email") String email,
                                      Model model) {
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        return "member/verify-reset";
    }

    @GetMapping("/modify")
    public String modifyForm(Model model) {
        Member member = memberService.getCurrentMember();
        if (member == null) {
            logger.warn("No current member found in modifyForm method");
            return "redirect:/member/login";
        }
        model.addAttribute("member", member);
        return "member/modify";
    }





    private final RatingService ratingService;
    @GetMapping("/myPage/{username}")
    public String viewMemberProfile(@PathVariable("username") String username,
                                    @RequestParam(value = "category", required = false, defaultValue = "food") String category,
                                    Model model) {
        Member member = memberService.getMemberByNickname(username);
        if (member == null) {
            return "error/404"; // 사용자 정보를 찾을 수 없는 경우 404 페이지로 리다이렉트
        }

        List<Rating> ratings = ratingService.getRatingsByMember(member);
        if (ratings == null) {
            ratings = new ArrayList<>(); // 빈 리스트로 초기화
        }
        model.addAttribute("Ratings", ratings);

        // 평균 점수 계산
        double averageScore = ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);

        String formattedAverageScore = String.format("%.2f", averageScore);

        // 특정 카테고리의 방문 기록 가져오기
        List<VisitRecord> visitRecords = visitRecordService.getAllVisitRecordsByMemberId(member.getId());

        Set<Long> reviewedItemIds = ratings.stream()
                .map(Rating::getItemId)
                .collect(Collectors.toSet());

        // 각 카테고리별로 분리된 Set 생성
        Set<Long> reviewedFoodIds = new HashSet<>();
        Set<Long> reviewedDessertIds = new HashSet<>();
        Set<Long> reviewedActivityIds = new HashSet<>();
        Set<Long> reviewedAttractionsIds = new HashSet<>();
        Set<Long> reviewedFestivalsIds = new HashSet<>();
        Set<Long> reviewedOtherIds = new HashSet<>();
        Set<Long> reviewedShoppingIds = new HashSet<>();

        // 각 카테고리별로 ID 추가
        ratings.forEach(rating -> {
            switch (rating.getCategory()) {
                case "food":
                    reviewedFoodIds.add(rating.getItemId());
                    break;
                case "dessert":
                    reviewedDessertIds.add(rating.getItemId());
                    break;
                case "activity":
                    reviewedActivityIds.add(rating.getItemId());
                    break;
                case "attractions":
                    reviewedAttractionsIds.add(rating.getItemId());
                    break;
                case "festivals":
                    reviewedFestivalsIds.add(rating.getItemId());
                    break;
                case "other":
                    reviewedOtherIds.add(rating.getItemId());
                    break;
                case "shopping":
                    reviewedShoppingIds.add(rating.getItemId());
                    break;
            }
        });

// 각 항목별로 리뷰 상태 확인 및 출력
        visitRecords.forEach(record -> {
            if (record.getFood() != null) {
                System.out.println("Food ID: " + record.getFood().getId());
                System.out.println("Reviewed: " + reviewedFoodIds.contains(record.getFood().getId()));
            }
            if (record.getActivity() != null) {
                System.out.println("Activity ID: " + record.getActivity().getId());
                System.out.println("Reviewed: " + reviewedActivityIds.contains(record.getActivity().getId()));
            }
            if (record.getAttractions() != null) {
                System.out.println("Attractions ID: " + record.getAttractions().getId());
                System.out.println("Reviewed: " + reviewedAttractionsIds.contains(record.getAttractions().getId()));
            }
            if (record.getDessert() != null) {
                System.out.println("Dessert ID: " + record.getDessert().getId());
                System.out.println("Reviewed: " + reviewedDessertIds.contains(record.getDessert().getId()));
            }
            if (record.getFestivals() != null) {
                System.out.println("Festivals ID: " + record.getFestivals().getId());
                System.out.println("Reviewed: " + reviewedFestivalsIds.contains(record.getFestivals().getId()));
            }
            if (record.getOther() != null) {
                System.out.println("Other ID: " + record.getOther().getId());
                System.out.println("Reviewed: " + reviewedOtherIds.contains(record.getOther().getId()));
            }
            if (record.getShopping() != null) {
                System.out.println("Shopping ID: " + record.getShopping().getId());
                System.out.println("Reviewed: " + reviewedShoppingIds.contains(record.getShopping().getId()));
            }
        });

        model.addAttribute("member", member);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", formattedAverageScore);
        model.addAttribute("visitRecords", visitRecords);
        model.addAttribute("reviewedFoodIds", reviewedFoodIds);
        model.addAttribute("reviewedDessertIds", reviewedDessertIds);
        model.addAttribute("reviewedActivityIds", reviewedActivityIds);
        model.addAttribute("reviewedAttractionsIds", reviewedAttractionsIds);
        model.addAttribute("reviewedFestivalsIds", reviewedFestivalsIds);
        model.addAttribute("reviewedOtherIds", reviewedOtherIds);
        model.addAttribute("reviewedShoppingIds", reviewedShoppingIds);

        return "member/memberPage";
    }


    @PostMapping("/login")
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        log.info("Current Session ID: {}", request.getSession().getId());
        return "redirect:/";
    }

    @GetMapping("/info")
    public ResponseEntity<Member> getMemberInfo(@RequestParam("username") String username) {
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(member);
    }
}
