package com.example.Trip_In_Jeju.home;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.service.EventService;
import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.service.AttractionsService;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.other.service.OtherService;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
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
    private final FestivalsService festivalsService;

    @GetMapping("/")
    public String index(Model model) {
        // 현재 회원 정보 가져오기
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);


        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);

        if (currentMember == null) {
            model.addAttribute("member", null);
            // 로그인하지 않은 상태에서 사용할 기본 데이터를 설정합니다.


            return "home/main"; // 비회원 페이지로 리턴
        }


        List<Food> randomFoodsForAll = foodService.getRandomFoods(1);
        List<Dessert> randomDessertsForAll = dessertService.getRandomDesserts(1);
        List<Activity> randomActivitiesForAll = activityService.getRandomActivities(1);
        List<Attractions> randomAttractionsForAll = attractionsService.getRandomAttractions(1);
        List<Festivals> randomFestivalsForAll = festivalsService.getRandomFestivals(1);
        List<Shopping> randomShoppingsForAll = shoppingService.getRandomShoppings(1);
        List<Other> randomOthersForAll = otherService.getRandomOthers(1);

        model.addAttribute("randomFoodsForAll", randomFoodsForAll);
        model.addAttribute("randomDessertsForAll", randomDessertsForAll);
        model.addAttribute("randomActivitiesForAll", randomActivitiesForAll);
        model.addAttribute("randomAttractionsForAll", randomAttractionsForAll);
        model.addAttribute("randomFestivalsForAll", randomFestivalsForAll);
        model.addAttribute("randomShoppingsForAll", randomShoppingsForAll);
        model.addAttribute("randomOthersForAll", randomOthersForAll);



        // 회원의 테마 정보에 따라 다르게 데이터 설정
        String thema = currentMember.getThema();

        if (thema != null) {
            switch (thema.toLowerCase()) {
                case "음식점":
                    List<Food> randomFoods = foodService.getRandomFoods(10);
                    model.addAttribute("randomFoods", randomFoods);
                    break;
                case "디저트":
                    List<Dessert> randomDesserts = dessertService.getRandomDesserts(10);
                    model.addAttribute("randomDesserts", randomDesserts);
                    break;
                case "엑티비티":
                    List<Activity> randomActivitys = activityService.getRandomActivities(10);
                    model.addAttribute("randomActivitys", randomActivitys);
                    break;
                case "관광지":
                    List<Attractions> randomAttractionss = attractionsService.getRandomAttractions(10);
                    model.addAttribute("randomAttractionss", randomAttractionss);
                    break;
                case "축제":
                    List<Festivals> randomFestivalss = festivalsService.getRandomFestivals(10);
                    model.addAttribute("randomFestivalss", randomFestivalss);
                    break;
                case "쇼핑":
                    List<Shopping> randomShoppings = shoppingService.getRandomShoppings(10);
                    model.addAttribute("randomShoppings", randomShoppings);
                    break;
                case "기타":
                    List<Other> randomOthers = otherService.getRandomOthers(10);
                    model.addAttribute("randomOthers", randomOthers);
                    break;

            }
        } else {
            // thema가 없는 경우 기본 데이터를 설정
            List<Food> defaultFoods = foodService.getRandomFoods(0);
            model.addAttribute("randomFoods", defaultFoods);
        }

        return "home/main";
    }


    // 특정 카테고리가 회원의 카테고리 목록에 포함되어 있는지 확인하는 메서드
    private boolean memberCategoryContains(String memberThema, String thema) {
        return memberThema != null && memberThema.contains(thema);
    }

}
