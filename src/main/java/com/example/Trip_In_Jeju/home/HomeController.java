package com.example.Trip_In_Jeju.home;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.service.EventService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final EventService eventService;

    @GetMapping("/")
    public String index(Model model) {
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);

        return "home/main";
    }

}
