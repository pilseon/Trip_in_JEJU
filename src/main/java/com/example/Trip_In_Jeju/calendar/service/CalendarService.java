package com.example.Trip_In_Jeju.calendar.service;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public void saveCalendar(Calendar calendar) {
        calendarRepository.save(calendar);
    }

    // 추가로 필요하다면 start나 end로 조회하는 메서드를 추가합니다.
    public List<Calendar> findByStart(LocalDateTime start) {
        return calendarRepository.findByStart(start);
    }

    public List<Calendar> findByEnd(LocalDateTime end) {
        return calendarRepository.findByEnd(end);
    }
}