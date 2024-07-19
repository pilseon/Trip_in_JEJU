package com.example.Trip_In_Jeju.kategorie.dessert.controller;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dessert")
public class DessertController {
    private final DessertService dessertService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        Page<Dessert> paging = dessertService.getList(page);

        model.addAttribute("paging", paging);
        return "dessert/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Dessert dessert = dessertService.getDessert(id);

        model.addAttribute("dessert", dessert);

        System.out.println(dessert.toString());

        return "dessert/detail";
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id) {
        dessertService.incrementLikes(id);
        return "redirect:/dessert/detail/" + id;
    }
}