package com.example.Trip_In_Jeju.member.controller;

import com.example.Trip_In_Jeju.DataNotFoundException;
import com.example.Trip_In_Jeju.calendar.service.CalendarService;
import com.example.Trip_In_Jeju.email.service.EmailService;
import com.example.Trip_In_Jeju.email.service.VerificationCodeService;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

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

        // 스크랩 기록 가져오기
        List<Scrap> scraps = scrapService.getScrapsByMember(currentMember);
        model.addAttribute("scraps", scraps);

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

    @GetMapping("/signup")
    public String signForm(Model model) {
        return "member/signup"; // signup.html을 반환
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("thema") String thema,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                         HttpSession session) {

        // 인증 코드 생성 및 이메일 전송
        String verificationCode = verificationCodeService.generateVerificationCode(email);
        String subject = "Trip_In_JEJU 회원 가입 인증";
        String body = String.format(
                "회원님의 가입을 축하드립니다!<br><br>" +
                        "아래 코드를 입력하여 이메일 인증을 완료해주세요.<br><br>" +
                        "인증 코드: <b>%s</b><br><br>" +
                        "감사합니다.<br><br>" +
                        "Trip_In_JEJU 팀",
                verificationCode
        );
        emailService.send(email, subject, body);

        // 인증 코드 세션에 저장
        session.setAttribute("verificationCode", verificationCode);

        // 프로필 사진 저장 및 회원가입
        String imageFileName = null;
        if (thumbnail != null && !thumbnail.isEmpty()) {
            imageFileName = storeProfilePicture(thumbnail);
        }

        memberService.signup(username, nickname, password, email, thema, thumbnail, MemberRole.MEMBER);

        return "redirect:/member/login";
    }


    public String storeProfilePicture(MultipartFile profilePicture) {
        // 이미지 저장 디렉토리 경로
        String uploadDir = "C:\\work\\Trip_In_Jeju\\src\\main\\resources\\static\\images\\profile";

        // 디렉토리가 존재하지 않으면 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create upload directory", e);
            }
        }


        String fileName = UUID.randomUUID().toString();
        String imageFileName = fileName + ".jpg";


        try {
            Path filePath = uploadPath.resolve(imageFileName);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image file", e);
        }

        // 저장된 파일의 상대 경로를 반환합니다.
        return "/images/profile/" + imageFileName;
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

    @PostMapping("/modify")
    public String modify(@RequestParam("username") String username,
                         @RequestParam("nickname") String nickname,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         @RequestParam("thema") String thema,
                         @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) {

        // 현재 로그인된 회원 정보를 가져옵니다.
        Member member = memberService.getCurrentMember();
        if (member == null) {
            // 사용자 정보가 없는 경우 로그인 페이지로 리다이렉트
            return "redirect:/member/login";
        }

        // 회원 정보를 수정합니다.
        memberService.modify(member, nickname, password, email, thema, thumbnail);

        // 수정 완료 후 마이페이지로 리다이렉트
        return "redirect:/member/myPage";
    }


    private final RatingService ratingService;
    @GetMapping("/myPage/{username}")
    public String viewMemberProfile(@PathVariable("username") String username, Model model) {
        Member member = memberService.getMemberByNickname(username);
        if (member == null) {
            return "error/404"; // 사용자 정보를 찾을 수 없는 경우 404 페이지로 리다이렉트
        }
        List<Rating> ratings = ratingService.getRatingsByMember(member);

        // 평균 점수 계산
        double averageScore = ratings.stream()
                .mapToInt(Rating::getScore)
                .average()
                .orElse(0.0);

        String formattedAverageScore = String.format("%.2f", averageScore);

        model.addAttribute("member", member);
        model.addAttribute("Ratings", ratings);
        model.addAttribute("averageScore", formattedAverageScore);
        // 평균 점수를 모델에 추가

        return "member/memberPage";
    }

    @PostMapping("/login")
    public String loginSuccess(HttpServletRequest request, HttpServletResponse response) {
        log.info("Current Session ID: {}", request.getSession().getId());
        return "redirect:/";
    }
}
