package com.example.Trip_In_Jeju.location.controller;

import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationRestController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<Location> getLocations() {
        return locationService.getAllLocations();
    }

    // 필요에 따라 API 키를 반환하는 엔드포인트 추가
    @GetMapping("/apikey")
    public String getApiKey() {
        return locationService.getApiKey();
    }
}