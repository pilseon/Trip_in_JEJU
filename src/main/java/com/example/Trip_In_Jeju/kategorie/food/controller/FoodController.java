package com.example.Trip_In_Jeju.kategorie.food.controller;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Food> paging = foodService.getList(page);

        model.addAttribute("paging", paging);
        return "food/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Food food = foodService.getFood(id);

        model.addAttribute("food", food);

        System.out.println(food.toString());

        return "food/detail";
    }
}