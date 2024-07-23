package com.example.Trip_In_Jeju.kategorie.dessert.repository;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertRepository extends JpaRepository<Dessert, Long> {
    Page<Dessert> findBySubCategory(String subCategory, Pageable pageable);
}
