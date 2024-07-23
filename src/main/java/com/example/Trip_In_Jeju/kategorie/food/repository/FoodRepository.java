package com.example.Trip_In_Jeju.kategorie.food.repository;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findBySubCategory(String subCategory, Pageable pageable);
}
