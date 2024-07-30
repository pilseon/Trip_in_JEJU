package com.example.Trip_In_Jeju.calendar.controller;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

        LocalDate startOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);

        List<Calendar> weeklyCalendars = calendarService.findCalendarsWithFoodsBetween2(startOfWeek, endOfWeek);

        // 디버그 로그 추가
        System.out.println("Start of Week: " + startOfWeek);
        System.out.println("End of Week: " + endOfWeek);
        weeklyCalendars.forEach(event -> System.out.println("Event: " + event));

        Map<LocalDate, List<Calendar>> eventsByDate = new HashMap<>();
        for (LocalDate d = startOfWeek; !d.isAfter(endOfWeek); d = d.plusDays(1)) {
            LocalDate finalD = d;
            List<Calendar> eventsForDate = weeklyCalendars.stream()
                    .filter(event -> !event.getPeriodStart().isAfter(finalD) && !event.getPeriodEnd().isBefore(finalD))
                    .collect(Collectors.toList());
            eventsByDate.put(d, eventsForDate);
        }

        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);
        model.addAttribute("weekDates", List.of(startOfWeek, startOfWeek.plusDays(1), startOfWeek.plusDays(2), startOfWeek.plusDays(3), startOfWeek.plusDays(4), startOfWeek.plusDays(5), startOfWeek.plusDays(6)));
        model.addAttribute("currentDate", date);
        model.addAttribute("eventsByDate", eventsByDate);

        return "calendar/weekCalendar";  // 뷰 이름 확인
    }
}