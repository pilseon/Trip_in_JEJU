package com.example.Trip_In_Jeju.kategorie.activity.repository;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findBySubCategory(String subCategory, Pageable pageable);

    @Query("SELECT f FROM Activity f WHERE (f.calendar.periodStart <= :end AND f.calendar.periodEnd >= :start)")
    List<Activity> findByCalendarPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a.id, a.title, a.place, a.thumbnailImg, a.content FROM Activity a WHERE a.title LIKE %:query% OR a.place LIKE %:query%")
    List<Object[]> findByTitleAndPlaceContaining(@Param("query") String query);
}
