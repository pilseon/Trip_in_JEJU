package com.example.Trip_In_Jeju.kategorie.attractions.repository;

import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionsRepository extends JpaRepository<Attractions, Long> {
    Page<Attractions> findBySubCategory(String subCategory, Pageable pageable);
}
