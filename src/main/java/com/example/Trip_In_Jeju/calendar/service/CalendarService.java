package com.example.Trip_In_Jeju.calendar.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public void saveCalendar(Calendar calendar) {
        calendarRepository.save(calendar);
    }

    public List<Calendar> getCalendarsBetween(LocalDate start, LocalDate end) {
        return calendarRepository.findByPeriodStartBetween(start, end);
    }

    public List<Calendar> findCalendarsBetween(LocalDate start, LocalDate end) {
        return calendarRepository.findByPeriodStartBetween(start, end);
    }

    public List<Calendar> findCalendarsWithFoodsBetween(LocalDate start, LocalDate end) {
        return calendarRepository.findCalendarsWithFoodsBetween(start, end);
    }
}