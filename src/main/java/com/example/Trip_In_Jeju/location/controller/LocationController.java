package com.example.Trip_In_Jeju.location.controller;

import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping
    public Location addLocation(@RequestBody Location location) {
        return locationService.addLocation(location);
    }
}