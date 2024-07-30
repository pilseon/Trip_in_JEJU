package com.example.Trip_In_Jeju.calendar.repository;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    @Query("SELECT c FROM Calendar c LEFT JOIN FETCH c.festivals WHERE (c.periodStart <= :end AND c.periodEnd >= :start)")
    List<Calendar> findCalendarsWithFoodsBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
    List<Calendar> findAllByPeriodStartBetweenOrPeriodEndBetween(LocalDate startDate, LocalDate endDate, LocalDate startDate2, LocalDate endDate2);
}