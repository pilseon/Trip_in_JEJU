package com.example.Trip_In_Jeju.food.service;

import com.example.Trip_In_Jeju.food.entity.Food;
import com.example.Trip_In_Jeju.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    public List<Food> getList() {
        return foodRepository.findAll();
    }

    public void create(String title, String content) {
        Food p = new Food();
        p.setTitle(title);
        p.setContent(content);
        p.setCreateDate(LocalDateTime.now());
        foodRepository.save(p);
    }
}
