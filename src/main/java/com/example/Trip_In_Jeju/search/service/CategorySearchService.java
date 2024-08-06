package com.example.Trip_In_Jeju.search.service;

import com.example.Trip_In_Jeju.kategorie.activity.repository.ActivityRepository;
import com.example.Trip_In_Jeju.kategorie.attractions.repository.AttractionsRepository;
import com.example.Trip_In_Jeju.kategorie.dessert.repository.DessertRepository;
import com.example.Trip_In_Jeju.kategorie.festivals.repository.FestivalsRepository;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
import com.example.Trip_In_Jeju.kategorie.other.repository.OtherRepository;
import com.example.Trip_In_Jeju.kategorie.shopping.repository.ShoppingRepository;
import com.example.Trip_In_Jeju.search.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Result> findSuggestions(String query) {
        List<Result> suggestions = new ArrayList<>();

        List<Object[]> activityResults = activityRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : activityResults) {
            Result res = new Result();
            res.setId((Long) result[0]); // Assuming id is the first element
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("activity");
            suggestions.add(res);
        }

        List<Object[]> attractionsResults = attractionsRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : attractionsResults) {
            Result res = new Result();
            res.setId((Long) result[0]);
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("attraction");
            suggestions.add(res);
        }

        List<Object[]> dessertResults = dessertRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : dessertResults) {
            Result res = new Result();
            res.setId((Long) result[0]);
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("dessert");
            suggestions.add(res);
        }

        List<Object[]> festivalsResults = festivalsRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : festivalsResults) {
            Result res = new Result();
            res.setId((Long) result[0]);
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("festivals");
            suggestions.add(res);
        }

        List<Object[]> foodResults = foodRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : foodResults) {
            Result res = new Result();
            res.setId((Long) result[0]);
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("food");
            suggestions.add(res);
        }

        List<Object[]> otherResults = otherRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : otherResults) {
            Result res = new Result();
            res.setId((Long) result[0]);
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("other");
            suggestions.add(res);
        }

        List<Object[]> shoppingResults = shoppingRepository.findByTitleAndPlaceContaining(query);
        for (Object[] result : shoppingResults) {
            Result res = new Result();
            res.setId((Long) result[0]);
            res.setTitle((String) result[1]);
            res.setPlace((String) result[2]);
            res.setThumbnailImg(result.length > 3 ? (String) result[3] : "");
            res.setContent(result.length > 4 ? (String) result[4] : "");
            res.setCategory("shopping");
            suggestions.add(res);
        }

        return suggestions;
    }
}
