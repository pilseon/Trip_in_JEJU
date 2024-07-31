package com.example.Trip_In_Jeju.Aitravel.controller;

import com.example.Trip_In_Jeju.Aitravel.dto.OpenAIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private OpenAIConfig openAIConfig;

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @GetMapping("/openai")
    public String openaiPage() {
        return "AI/openai"; // templates/AI/openai.html 파일을 렌더링합니다.
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("prompt") String prompt, Model model) {
        String apiKey = openAIConfig.getApiKey();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(openAIConfig.getApiKey());

        String requestBody = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"temperature\": 0.8, \"max_tokens\": 1024, \"top_p\": 1, \"frequency_penalty\": 0.5, \"presence_penalty\": 0.5}", prompt);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
            String responseBody = response.getBody();
            System.out.println("API 응답: " + responseBody); // 디버깅용 로그
            model.addAttribute("response", responseBody != null ? responseBody : "응답이 없습니다");
        } catch (Exception e) {
            System.out.println("API 호출 오류: " + e.getMessage()); // 오류 로그
            model.addAttribute("response", "API 호출 중 오류 발생");
        }

        model.addAttribute("prompt", prompt);
        return "AI/openai"; // 결과를 템플릿에 전달
    }
}