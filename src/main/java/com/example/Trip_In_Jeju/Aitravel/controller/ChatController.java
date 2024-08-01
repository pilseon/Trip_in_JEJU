package com.example.Trip_In_Jeju.Aitravel.controller;

import com.example.Trip_In_Jeju.Aitravel.service.OpenAiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiChatService openAiChatService;

    @GetMapping("/openai")
    public String openaiPage() {
        return "AI/openai";
    }

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> requestBody) {
        String prompt = requestBody.get("prompt");
        String responseText = openAiChatService.getChatResponse(prompt);
        return ResponseEntity.ok(responseText);
    }
}