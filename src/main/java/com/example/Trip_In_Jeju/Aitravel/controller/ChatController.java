package com.example.Trip_In_Jeju.Aitravel.controller;

import com.example.Trip_In_Jeju.Aitravel.service.OpenAiChatService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {


    private final OpenAiChatService openAiChatService;
    private final MemberService memberService;

    @GetMapping("/openai")
    public String openaiPage(Model model) {
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        return "AI/openai";
    }

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> requestBody, Model model) {
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        String prompt = requestBody.get("prompt");
        String responseText = openAiChatService.getChatResponse(prompt);
        return ResponseEntity.ok(responseText);
    }
}