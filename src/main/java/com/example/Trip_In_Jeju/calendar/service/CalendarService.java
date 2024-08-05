package com.example.Trip_In_Jeju.calendar.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.repository.FestivalsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service

public class CalendarService {

    private  final CalendarRepository calendarRepository;
    private final FestivalsRepository festivalsRepository;


    public CalendarService(CalendarRepository calendarRepository, FestivalsRepository festivalsRepository) {
        this.calendarRepository = calendarRepository;
        this.festivalsRepository = festivalsRepository;
    }



    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public void saveCalendar(Calendar calendar) {
        calendarRepository.save(calendar);
    }

    public List<Calendar> findCalendarsWithFoodsBetween2(LocalDate start, LocalDate end) {
        List<Calendar> calendars = calendarRepository.findCalendarsWithFoodsBetween(start, end);
        // Fetch festivals for each calendar
        for (Calendar calendar : calendars) {
            List<Festivals> festivals = festivalsRepository.findByCalendarId(calendar.getId());
            calendar.setFestivals(festivals);
        }
        return calendars;
    }

    public List<Calendar> findCalendarsWithFoodsBetween(LocalDate start, LocalDate end) {
        List<Calendar> calendars = calendarRepository.findAllByPeriodStartBetweenOrPeriodEndBetween(start, end, start, end);
        // Fetch festivals for each calendar
        for (Calendar calendar : calendars) {
            List<Festivals> festivals = festivalsRepository.findByCalendarId(calendar.getId());
            calendar.setFestivals(festivals);
        }
        return calendars;
    }


}