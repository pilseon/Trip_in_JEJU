package com.example.Trip_In_Jeju.calendar.controller;

import com.example.Trip_In_Jeju.calendar.entity.Calendar;
import com.example.Trip_In_Jeju.calendar.service.CalendarService;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final FestivalsService festivalsService;

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
    public String viewWeeklyCalendar(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {


        if (id == null) {
            id = getDefaultFestivalId();
            if (id == null) {
                // 기본값이 없을 경우에 대한 예외 처리
                throw new IllegalArgumentException("Festival ID is required but not provided");
            }
        }

        Festivals festivals = festivalsService.getFestivalsById(id);
        if (festivals == null) {

            throw new IllegalArgumentException("No festival found for ID: " + id);
        }

        // date 파라미터가 없을 경우 오늘 날짜로 설정
        if (date == null) {
            date = LocalDate.now();
        }

        LocalDate startOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);

        List<Calendar> weeklyCalendars = calendarService.findCalendarsWithFoodsBetween2(startOfWeek, endOfWeek);

        // 중복된 Calendar 객체를 제거
        Set<Calendar> uniqueCalendars = new HashSet<>(weeklyCalendars);

        // 디버그 로그 추가
        System.out.println("Start of Week: " + startOfWeek);
        System.out.println("End of Week: " + endOfWeek);
        uniqueCalendars.forEach(event -> System.out.println("Event: " + event));

        Map<LocalDate, List<Calendar>> eventsByDate = new HashMap<>();
        for (LocalDate d = startOfWeek; !d.isAfter(endOfWeek); d = d.plusDays(1)) {
            LocalDate finalD = d;
            List<Calendar> eventsForDate = uniqueCalendars.stream()
                    .filter(event -> !event.getPeriodStart().isAfter(finalD) && !event.getPeriodEnd().isBefore(finalD))
                    .collect(Collectors.toList());
            eventsByDate.put(d, eventsForDate);
        }


        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);
        model.addAttribute("weekDates", List.of(startOfWeek, startOfWeek.plusDays(1), startOfWeek.plusDays(2), startOfWeek.plusDays(3), startOfWeek.plusDays(4), startOfWeek.plusDays(5), startOfWeek.plusDays(6)));
        model.addAttribute("currentDate", date);
        model.addAttribute("eventsByDate", eventsByDate);
        model.addAttribute("festivals", festivals);

        return "calendar/weekCalendar";
    }


    private Long getDefaultFestivalId() {
        // 기본값으로 설정할 ID를 결정하는 로직 작성
        // 예를 들어, 최근 등록된 축제의 ID를 반환할 수 있습니다.
        List<Festivals> festivalsList = festivalsService.getAllFestivals();
        return festivalsList.isEmpty() ? null : festivalsList.get(0).getId();
    }

    @GetMapping("/day")
    public String viewDailyCalendar(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        LocalDate startOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        LocalDate endOfWeek = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 7);

        // Festival ID를 확인하고, 기본 ID를 설정합니다.
        if (id == null) {
            id = getDefaultFestivalId();
        }

        Festivals festivals = null;
        if (id != null) {
            festivals = festivalsService.getFestivalsById(id);
            if (festivals == null) {
                // ID로 페스티벌을 찾지 못하면 기본적으로 null로 설정
                System.out.println("No festival found for ID: " + id);
            }
        }

        List<Calendar> dailyCalendars = calendarService.findCalendarsWithFoodsBetween2(date, date);
        List<Calendar> weeklyCalendars = calendarService.findCalendarsWithFoodsBetween2(startOfWeek, endOfWeek);

        // 중복된 Calendar 객체를 제거
        Set<Calendar> uniqueCalendars = new HashSet<>(dailyCalendars);

        // 디버그 로그 추가
        System.out.println("Selected Date: " + date);
        uniqueCalendars.forEach(event -> System.out.println("Event: " + event));

        Map<LocalDate, List<Calendar>> eventsByDate = new HashMap<>();
        eventsByDate.put(date, new ArrayList<>(uniqueCalendars));

        model.addAttribute("weekStart", startOfWeek);
        model.addAttribute("weekEnd", endOfWeek);
        model.addAttribute("weekDates", List.of(startOfWeek, startOfWeek.plusDays(1), startOfWeek.plusDays(2), startOfWeek.plusDays(3), startOfWeek.plusDays(4), startOfWeek.plusDays(5), startOfWeek.plusDays(6)));
        model.addAttribute("currentDate", date);
        model.addAttribute("eventsByDate", eventsByDate);
        model.addAttribute("festivals", festivals); // festivals가 null일 수 있음

        return "calendar/dailyCalendar";
    }

}
