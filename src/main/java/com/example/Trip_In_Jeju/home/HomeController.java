package com.example.Trip_In_Jeju.home;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.service.EventService;
import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
import com.example.Trip_In_Jeju.kategorie.attractions.service.AttractionsService;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import com.example.Trip_In_Jeju.kategorie.other.service.OtherService;
import com.example.Trip_In_Jeju.kategorie.shopping.service.ShoppingService;
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
    private final FoodService foodService;
    private final DessertService dessertService;
    private final ShoppingService shoppingService;
    private final AttractionsService attractionsService;
    private final ActivityService activityService;
    private final OtherService otherService;

    @GetMapping("/")
    public String index(Model model) {
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);

        return "home/main";
    }


}
