package com.example.Trip_In_Jeju.kategorie.activity.repository;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findBySubCategory(String subCategory, Pageable pageable);
}
