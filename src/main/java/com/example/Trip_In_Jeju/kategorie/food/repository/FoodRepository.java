package com.example.Trip_In_Jeju.kategorie.food.repository;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;


@ResponseBody
public interface FoodRepository extends JpaRepository<Food, Long> {

}
