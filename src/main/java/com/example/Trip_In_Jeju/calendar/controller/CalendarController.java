package com.example.Trip_In_Jeju.calendar.controller;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/list")
    public String viewCalendar(Model model) {
        model.addAttribute("calendars", calendarService.getAllCalendars());
        model.addAttribute("calendar", new Calendar());
        return "calendar/list";
    }

    @PostMapping("/calendars")
    public String addEvent(@ModelAttribute Calendar calendar) {
        calendarService.saveCalendar(calendar);
        return "redirect:/calendar/list";
    }

    @GetMapping("/events")
    public List<Calendar> getEvents() {
        return calendarService.getAllCalendars();
    }

    @GetMapping("/week")
    public String viewWeeklyCalendar(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }

        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(LocalTime.MAX);

        List<Calendar> weeklyCalendars = calendarService.getCalendarsBetween(startDateTime, endDateTime);

        List<LocalDate> weekDates = startOfWeek.datesUntil(endOfWeek.plusDays(1)).collect(Collectors.toList());

        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);
        model.addAttribute("weekDates", weekDates);
        model.addAttribute("weeklyCalendars", weeklyCalendars);
        model.addAttribute("currentDate", date);

        return "calendar/weekCalendar";
    }
}