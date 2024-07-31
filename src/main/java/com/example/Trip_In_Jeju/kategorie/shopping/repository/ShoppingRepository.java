package com.example.Trip_In_Jeju.kategorie.shopping.repository;

import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShoppingRepository extends JpaRepository<Shopping, Long> {
    Page<Shopping> findBySubCategory(String subCategory, Pageable pageable);

    @Query("SELECT f FROM Shopping f WHERE (f.calendar.periodStart <= :end AND f.calendar.periodEnd >= :start)")
    List<Shopping> findByCalendarPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
