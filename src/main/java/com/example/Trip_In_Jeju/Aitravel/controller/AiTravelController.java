package com.example.Trip_In_Jeju.Aitravel.controller;

import com.example.Trip_In_Jeju.Aitravel.service.AiTravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/AI")
public class AiTravelController {

    private final AiTravelService aiTravelService;

    @GetMapping("/recommendations")
    public String getRecommendationsPage() {
        return "AI/recommendations";
    }

    @PostMapping("/recommendations")
    public String getRecommendations(@RequestParam("question") String question, Model model) {
        Mono<String> response = aiTravelService.getAiResponse(question);
        model.addAttribute("question", question);
        model.addAttribute("response", response.block());
        return "AI/recommendations";
    }
}