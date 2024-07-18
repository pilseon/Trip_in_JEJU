package com.example.Trip_In_Jeju.calendar.repository;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByStartBetween(LocalDateTime start, LocalDateTime end);
}