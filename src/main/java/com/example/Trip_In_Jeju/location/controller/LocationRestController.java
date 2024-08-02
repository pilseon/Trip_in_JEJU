package com.example.Trip_In_Jeju.location.controller;

import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.service.LocationService;
import com.example.Trip_In_Jeju.soical.kakao.MyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationRestController {

    private final LocationService locationService;

    private final MyService myService; // MyService를 주입받음

    @GetMapping
    public List<Location> getLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/apikey")
    public Map<String, String> getKakaoMapApiKey() {
        Map<String, String> response = new HashMap<>();
        response.put("kakaoMapApiKey", myService.getKakaoMapAppKey());
        return response;
    }
}