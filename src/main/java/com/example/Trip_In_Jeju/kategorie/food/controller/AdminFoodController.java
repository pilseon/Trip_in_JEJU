package com.example.Trip_In_Jeju.kategorie.food.controller;


import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/food")
public class AdminFoodController {
    private final FoodService foodService;

    @GetMapping("/create")
    public String create() {
        return "adm/food/create";
    }

    @PostMapping("/create")
    public String createFoodContent(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("place") String place) {
        foodService.create(title, content, place);

        return "/adm/food/create";
    }
}
