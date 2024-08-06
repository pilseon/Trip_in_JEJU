package com.example.Trip_In_Jeju.kategorie.dessert.repository;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DessertRepository extends JpaRepository<Dessert, Long> {
    Page<Dessert> findBySubCategory(String subCategory, Pageable pageable);

    @Query("SELECT f FROM Dessert f WHERE (f.calendar.periodStart <= :end AND f.calendar.periodEnd >= :start)")
    List<Dessert> findByCalendarPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a.title, a.place FROM Dessert a WHERE a.title LIKE %:query% OR a.place LIKE %:query%")
    List<Object[]> findByTitleAndPlaceContaining(@Param("query") String query);
}
