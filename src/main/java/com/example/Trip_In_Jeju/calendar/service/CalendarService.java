package com.example.Trip_In_Jeju.calendar.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Calendar> getCalendarsBetween(LocalDateTime start, LocalDateTime end) {
        return calendarRepository.findByStartBetween(start, end);
    }
}