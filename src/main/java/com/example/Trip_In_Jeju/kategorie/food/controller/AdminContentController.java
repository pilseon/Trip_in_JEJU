package com.example.Trip_In_Jeju.kategorie.food.controller;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/content")
public class AdminContentController {
    private final FoodService foodService;
    private final DessertService dessertService;

    @GetMapping("/create")
    public String create(Model model) {
        List<Food> foodList = foodService.getList();
        List<Dessert> dessertList = dessertService.getList();

        model.addAttribute("foodList", foodList);
        model.addAttribute("dessertList", dessertList);

        return "adm/content/create";
    }
    @PostMapping("/create")
    public String createContent(
            @RequestParam("title") String title,
            @RequestParam("businessHoursStart") String businessHoursStart,
            @RequestParam("businessHoursEnd") String businessHoursEnd,
            @RequestParam("content") String content,
            @RequestParam("place") String place,
            @RequestParam("closedDay") String closedDay,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            @RequestParam("websiteUrl") String websiteUrl,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("hashtags") String hashtags,
            @RequestParam("category") String category,
            @RequestParam(value = "subCategory", required = false) String subCategory,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("periodStart") String periodStart,
            @RequestParam("periodEnd") String periodEnd) {

        if (category.equals("food")) {
            foodService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        } else if (category.equals("dessert")) {
            dessertService.create(title, businessHoursStart, businessHoursEnd, content, place, closedDay, websiteUrl, phoneNumber, hashtags, thumbnail, latitude, longitude, subCategory);
        }

        return "redirect:/adm/content/create";
    }
}

//    @PostMapping("/createFood")
//    public String createFoodContent(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("place") String place, @RequestParam("closedDay") String closedDay, @RequestParam("thumbnail") MultipartFile thumbnail) {
//        foodService.create(title, content, place, closedDay, thumbnail);
//        return "redirect:/adm/food/create";
//    }
//
//    @PostMapping("/createDessert")
//    public String createDessertContent(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("place") String place, @RequestParam("closedDay") String closedDay,  @RequestParam("thumbnail") MultipartFile thumbnail) {
//        dessertService.create(title, content, place, closedDay, thumbnail);
//        return "redirect:/adm/food/create";
//    }
