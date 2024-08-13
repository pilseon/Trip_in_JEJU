package com.example.Trip_In_Jeju.event.controller;

import com.example.Trip_In_Jeju.event.entity.Event;
import com.example.Trip_In_Jeju.event.service.EventService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final MemberService memberService;

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

        return "redirect:/event/list";
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
    // 수정 폼을 보여주는 메서드 (기존 코드)
    @GetMapping("/edit/{id}")
    public String editEventForm(@PathVariable("id") Long id, Model model) {
        Event event = eventService.findEventById(id);
        model.addAttribute("event", event);
        return "adm/event/edit";
    }

    // 실제 이벤트를 업데이트하는 메서드
    @PostMapping("/update/{id}")
    public String updateEvent(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("periodStart") LocalDate periodStart,
            @RequestParam("periodEnd") LocalDate periodEnd,
            @RequestParam("thumbnailImg") MultipartFile thumbnailImg,
            @RequestParam("steps") MultipartFile[] steps  // 'steps' 배열로 모든 이미지를 받음
    ) {
        Member currentMember = memberService.getCurrentMember();
        if (currentMember == null || !currentMember.getUsername().equals("admin")) {
            throw new SecurityException("You do not have permission to modify this event.");
        }

        // 업데이트 로직
        Event updatedEvent = eventService.findEventById(id);
        updatedEvent.setTitle(title);
        updatedEvent.setContent(content);
        updatedEvent.setPeriodStart(periodStart);
        updatedEvent.setPeriodEnd(periodEnd);

        // 썸네일 이미지와 스텝 이미지가 선택되었을 경우에만 업데이트
        eventService.updateEvent(updatedEvent, thumbnailImg, steps);

        return "redirect:/event/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEventById(id);  // 이벤트 삭제 로직
        return "redirect:/event/list";
    }
}
