package com.example.Trip_In_Jeju.like;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void removeAllLikesForItem(Food food) {
        likeRepository.deleteByFoodId(food.getId());
    }
    @Transactional
    public void removeAllLikesForItem(Activity activity) {
        likeRepository.deleteByActivityId(activity.getId());
    }
    @Transactional
    public void removeAllLikesForItem(Attractions attractions) {
        likeRepository.deleteByAttractionsId(attractions.getId());
    }
    @Transactional
    public void removeAllLikesForItem(Dessert dessert) {
        likeRepository.deleteByDessertId(dessert.getId());
    }
    @Transactional
    public void removeAllLikesForItem(Other other) {
        likeRepository.deleteByOtherId(other.getId());
    }
    @Transactional
    public void removeAllLikesForItem(Shopping shopping) {
        likeRepository.deleteByShoppingId(shopping.getId());
    }

}