package com.example.Trip_In_Jeju.calendar.controller;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private final CalendarService calendarService;

    @GetMapping("/list")
    public String viewCalendar(Model model) {
        model.addAttribute("calendars", calendarService.getAllCalendars());
        model.addAttribute("calendar", new Calendar());
        return "calendar/list";
    }

    @PostMapping
    public String addEvent(@ModelAttribute Calendar calendar) {
        calendarService.saveCalendar(calendar);
        return "redirect:/calendar/list";
    }

    @GetMapping("/events")
    @ResponseBody
    public List<Calendar> getEvents() {
        return calendarService.getAllCalendars();
    }
}