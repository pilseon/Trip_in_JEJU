package com.example.Trip_In_Jeju.home;

import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/adm")
@AllArgsConstructor
public class AdmHomeController {

    private final MemberService memberService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')") // admin 이라는 권한을 가지고 있는가?
    public String index(){
        return "redirect:/adm/home/main";
    }
    @GetMapping("/home/main")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMain(){
        return "adm/home/main";
    }
    @GetMapping("/members")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAllMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("pageName", "회원 목록");
        return "adm/member/members";
    }
}
