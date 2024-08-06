package com.example.Trip_In_Jeju.kategorie.festivals.repository;

import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FestivalsRepository extends JpaRepository<Festivals, Long> {
    Page<Festivals> findBySubCategory(String subCategory, Pageable pageable);
    List<Festivals> findByCalendarId(Long calendarId);


    @Query("SELECT a.id, a.title, a.place, a.thumbnailImg, a.content FROM Festivals a WHERE a.title LIKE %:query% OR a.place LIKE %:query%")
    List<Object[]> findByTitleAndPlaceContaining(@Param("query") String query);
}
