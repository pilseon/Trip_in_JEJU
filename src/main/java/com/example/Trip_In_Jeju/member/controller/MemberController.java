package com.example.Trip_In_Jeju.member.controller;

import com.example.Trip_In_Jeju.DataNotFoundException;
import com.example.Trip_In_Jeju.email.service.EmailService;
import com.example.Trip_In_Jeju.email.service.VerificationCodeService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.entity.MemberRole;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final EmailService emailService;
    private final MemberService memberService;
    private final VerificationCodeService verificationCodeService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
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
                         HttpSession session) {

        String verificationCode = verificationCodeService.generateVerificationCode(email);
        String subject = "Trip_In_JEJU";
        String body = "회원님의 가입을 축하드리며 저희 Trip_In_JEJU 에서 좋은 추억 남기셨으면 좋겠습니다! " ;
        emailService.send(email, subject, body);

        session.setAttribute("verificationCode", verificationCode);
        memberService.signup(username, nickname, password,  email, thema, MemberRole.USER);

        return "redirect:/member/login";
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


    // 아이디 찾기 시작
    @GetMapping("/find-username")
    public String showFindUsernamePage(Model model) {
        model.addAttribute("username", null); // 초기화: 아이디 결과 없음
        model.addAttribute("error", null); // 초기화: 에러 메시지 없음
        return "member/find-username"; // templates/user/find-username.html 경로에 파일이 있어야 함
    }

    // 아이디 찾기 기능 처리
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
}
