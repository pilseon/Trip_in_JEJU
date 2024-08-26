package com.example.Trip_In_Jeju.location.controller;

import com.example.Trip_In_Jeju.location.service.LocationService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final LocationService locationService; // MyService를 주입받음
    private final MemberService memberService;
    @GetMapping("/map")
    public String showMap(Model model) {

        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        return "map/map"; // map.html 파일을 반환
    }
}