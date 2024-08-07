package com.example.Trip_In_Jeju.inquiry.controller;

import com.example.Trip_In_Jeju.inquiry.entity.Inquiry;
import com.example.Trip_In_Jeju.inquiry.service.InquiryService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {
    @Autowired
    private final InquiryService inquiryService;

    @Autowired
    private final MemberService memberService;

    @GetMapping("/new")
    public String newInquiryForm(Model model) {
        model.addAttribute("inquiry", new Inquiry());
        return "inquiry-form";
    }

    @PostMapping
    public String createInquiry(@ModelAttribute Inquiry inquiry) {
        inquiryService.createInquiry(inquiry);
        return "redirect:/inquiries";
    }

    @GetMapping
    public String listInquiries(Model model) {
        model.addAttribute("inquiries", inquiryService.listInquiries());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("currentMemberName", currentUsername);
        return "inquiry-list";
    }

    @GetMapping("/{id}")
    public String viewInquiry(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Member) {
            Member currentUser = (Member) authentication.getPrincipal();
            String currentUsername = currentUser.getNickname();
            model.addAttribute("currentUsername", currentUsername);
        }
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        Inquiry inquiry = inquiryService.getInquiry(id);
        model.addAttribute("inquiry", inquiry);
        model.addAttribute("inquiries", inquiryService.listInquiries());
        return "inquiry-list";
    }


    @PostMapping("/{id}/responses")
    public String addResponse(@PathVariable("id") Long id, @RequestParam("content") String content) {
        inquiryService.addResponse(id, content);
        return "redirect:/inquiries/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteInquiry(@PathVariable("id") Long id) {
        inquiryService.deleteInquiry(id);
        return "redirect:/inquiries"; // 삭제 후 목록 페이지로 리디렉션
    }
}
