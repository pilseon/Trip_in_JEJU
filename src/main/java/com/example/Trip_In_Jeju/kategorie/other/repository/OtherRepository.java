package com.example.Trip_In_Jeju.kategorie.other.repository;

import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OtherRepository extends JpaRepository<Other, Long> {
    Page<Other> findBySubCategory(String subCategory, Pageable pageable);

    @Query("SELECT f FROM Other f WHERE (f.calendar.periodStart <= :end AND f.calendar.periodEnd >= :start)")
    List<Other> findByCalendarPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
