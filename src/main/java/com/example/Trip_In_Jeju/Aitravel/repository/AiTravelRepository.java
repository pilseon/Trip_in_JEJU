package com.example.Trip_In_Jeju.Aitravel.repository;

import com.example.Trip_In_Jeju.Aitravel.entity.AiTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiTravelRepository extends JpaRepository<AiTravel, Long> {
    List<AiTravel> findByCategory(String category);
}