package com.example.Trip_In_Jeju.kategorie.other.repository;

import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherRepository extends JpaRepository<Other, Long> {
    Page<Other> findBySubCategory(String subCategory, Pageable pageable);
}
