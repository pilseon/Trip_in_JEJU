package com.example.Trip_In_Jeju.kategorie.shopping.repository;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingRepository extends JpaRepository<Shopping, Long> {
    Page<Shopping> findBySubCategory(String subCategory, Pageable pageable);
}
