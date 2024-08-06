package com.example.Trip_In_Jeju.kategorie.food.repository;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Page<Food> findBySubCategory(String subCategory, Pageable pageable);

    @Query("SELECT f FROM Food f WHERE (f.calendar.periodStart <= :end AND f.calendar.periodEnd >= :start)")
    List<Food> findByCalendarPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a.title, a.place FROM Food a WHERE a.title LIKE %:query% OR a.place LIKE %:query%")
    List<Object[]> findByTitleAndPlaceContaining(@Param("query") String query);
}
