package com.example.Trip_In_Jeju.location.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class MapController {

    @GetMapping("/map")
    public String showMap() {
        return "map/map"; // map.html 파일을 반환
    }
}