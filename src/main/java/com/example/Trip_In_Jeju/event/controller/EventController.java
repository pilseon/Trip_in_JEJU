package com.example.Trip_In_Jeju.event.controller;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.service.EventService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String viewEventList(Model model) {
        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);
        return "event/list";
    }

    @GetMapping("/detail/{id}")
    public String viewEventDetail(@PathVariable("id") Long id, Model model) {
        Event event = eventService.findEventById(id);
        Member currentMember = memberService.getCurrentMember(); // 현재 로그인된 사용자 정보
        model.addAttribute("event", event);
        model.addAttribute("member", currentMember); // 모델에 사용자 정보 추가
        return "event/detail";
    }

    @GetMapping("/main")
    public String viewEventMain(Model model) {
        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);
        return "event/main";
    }

    @GetMapping("/game")
    public String viewGame() {
        return "event/game";
    }
}
