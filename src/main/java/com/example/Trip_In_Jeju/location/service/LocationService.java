package com.example.Trip_In_Jeju.location.service;

import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    @Getter
    @Value("${kakao.api.key}")
    private String apiKey;

    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}