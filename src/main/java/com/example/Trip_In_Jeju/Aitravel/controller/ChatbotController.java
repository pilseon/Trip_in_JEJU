package com.example.Trip_In_Jeju.Aitravel.controller;

import com.example.Trip_In_Jeju.Aitravel.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/AI")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/openai")
    @ResponseBody // JSON 응답을 반환함
    public String sendMessage(@RequestBody String message) {
        return chatbotService.getChatbotResponse(message);
    }

    @GetMapping("/openai")
    public String getOpenAiPage() {
        // "AI/openai"는 src/main/resources/templates/AI/openai.html을 찾음
        return "AI/openai";
    }
}