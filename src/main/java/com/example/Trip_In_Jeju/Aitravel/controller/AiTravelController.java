package com.example.Trip_In_Jeju.Aitravel.controller;

import com.example.Trip_In_Jeju.Aitravel.service.AiTravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/AI")
public class AiTravelController {

    private final AiTravelService aiTravelService;

    @GetMapping("/recommendations")
    public String getRecommendationsPage() {
        return "AI/recommendations"; // recommendations.html 템플릿 반환
    }

    @PostMapping("/recommendations")
    public String getRecommendations(@RequestParam("question") String question, Model model) {
        try {
            String response = aiTravelService.getAiResponse(question);
            model.addAttribute("question", question);
            model.addAttribute("response", response);
        } catch (Exception e) {
            model.addAttribute("question", question);
            model.addAttribute("response", "추천을 생성하는 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
        return "AI/recommendations"; // recommendations.html 템플릿 반환
    }
}