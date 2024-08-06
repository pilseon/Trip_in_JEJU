package com.example.Trip_In_Jeju.search.service;

import com.example.Trip_In_Jeju.kategorie.activity.repository.ActivityRepository;
import com.example.Trip_In_Jeju.kategorie.attractions.repository.AttractionsRepository;
import com.example.Trip_In_Jeju.kategorie.dessert.repository.DessertRepository;
import com.example.Trip_In_Jeju.kategorie.festivals.repository.FestivalsRepository;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
import com.example.Trip_In_Jeju.kategorie.other.repository.OtherRepository;
import com.example.Trip_In_Jeju.kategorie.shopping.repository.ShoppingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategorySearchService {
    private final ActivityRepository activityRepository;
    private final AttractionsRepository attractionsRepository;
    private final DessertRepository dessertRepository;
    private final FestivalsRepository festivalsRepository;
    private final FoodRepository foodRepository;
    private final OtherRepository otherRepository;
    private final ShoppingRepository shoppingRepository;

    public List<Map<String, String>> findSuggestions(String query) {
        List<Map<String, String>> suggestions = new ArrayList<>();

        List<Object[]> activityResults = activityRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : activityResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        List<Object[]> attractionsResults = attractionsRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : attractionsResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        List<Object[]> dessertResults = dessertRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : dessertResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        List<Object[]> festivalsResults = festivalsRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : festivalsResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        List<Object[]> foodResults = foodRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : foodResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        List<Object[]> otherResults = otherRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : otherResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        List<Object[]> shoppingResults = shoppingRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : shoppingResults) {
            Map<String, String> map = new HashMap<>();
            map.put("title", (String) result[0]);
            map.put("place", (String) result[1]);
            suggestions.add(map);
        }

        System.out.println("Suggestions found: " + suggestions);

        return suggestions;
    }
}
