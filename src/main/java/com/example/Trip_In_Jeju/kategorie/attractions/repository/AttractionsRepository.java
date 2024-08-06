package com.example.Trip_In_Jeju.kategorie.attractions.repository;

import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttractionsRepository extends JpaRepository<Attractions, Long> {
    Page<Attractions> findBySubCategory(String subCategory, Pageable pageable);

    @Query("SELECT f FROM Attractions f WHERE (f.calendar.periodStart <= :end AND f.calendar.periodEnd >= :start)")
    List<Attractions> findByCalendarPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a.id, a.title, a.place, a.thumbnailImg, a.content FROM Attractions a WHERE a.title LIKE %:query% OR a.place LIKE %:query%")
    List<Object[]> findByTitleAndPlaceContaining(@Param("query") String query);
}
