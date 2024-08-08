package com.example.Trip_In_Jeju.event.controller;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/event")
public class AdmEventController {
    private final EventService eventService;

    @GetMapping("/list")
    public String viewEventList() {
        return "event/list";
    }

    @GetMapping("/create")
    public String createEventForm() {
        return "adm/event/create";
    }

    @PostMapping("/create")
    public String createEvent(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("thumbnailImg") MultipartFile thumbnailImg,
            @RequestParam("periodStart") LocalDate periodStart,
            @RequestParam("periodEnd") LocalDate periodEnd,
            @RequestParam("steps") MultipartFile[] steps
    ) {
        // Create Event entity
        Event event = new Event();
        event.setTitle(title);
        event.setContent(content);
        event.setPeriodStart(periodStart);
        event.setPeriodEnd(periodEnd);

        eventService.saveEvent(event, thumbnailImg, steps);

        return "redirect:/";
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
