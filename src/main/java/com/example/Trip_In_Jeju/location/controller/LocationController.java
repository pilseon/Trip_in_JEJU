package com.example.Trip_In_Jeju.location.controller;

import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final FoodService foodService;
    private final LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("location", new Location());
        return "locationForm";
    }

    @PostMapping("/save")
    public String saveLocation(@RequestBody Location location) {
        System.out.println("Received Location: " + location.toString()); // 디버깅용 출력
        // 주소를 기반으로 위도와 경도 설정
        locationService.saveLocation(location);
        return "redirect:/locations";
    }
}