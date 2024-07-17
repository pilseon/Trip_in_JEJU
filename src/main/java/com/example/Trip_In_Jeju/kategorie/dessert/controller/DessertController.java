package com.example.Trip_In_Jeju.kategorie.dessert.controller;


import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dessert")
public class DessertController {
    private final DessertService dessertService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Dessert> dessertList = dessertService.getList();
        model.addAttribute("dessertList", dessertList);
        return "dessert/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Dessert dessert = dessertService.getDessert(id);

        model.addAttribute("dessert", dessert);

        return "dessert/detail";
    }
}
