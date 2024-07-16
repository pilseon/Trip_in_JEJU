package com.example.Trip_In_Jeju.food.controller;


import com.example.Trip_In_Jeju.food.entity.Food;
import com.example.Trip_In_Jeju.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Food> foodList = foodService.getList();
        model.addAttribute("foodList", foodList);
        return "food/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {

        return "food/detail";
    }
}
