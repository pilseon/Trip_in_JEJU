package com.example.Trip_In_Jeju.Aitravel.service;

import com.example.Trip_In_Jeju.Aitravel.dto.OpenAIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiChatService {

    private final RestTemplate restTemplate;
    private final OpenAIConfig openAIConfig;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public OpenAiChatService(OpenAIConfig openAIConfig, RestTemplate restTemplate) {
        this.openAIConfig = openAIConfig;
        this.restTemplate = restTemplate;
    }

    public String getChatResponse(String prompt) {
        // Create request payload
        String payload = String.format("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"temperature\": 0.8, \"max_tokens\": 1024, \"top_p\": 1, \"frequency_penalty\": 0.5, \"presence_penalty\": 0.5}", prompt);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBearerAuth(openAIConfig.getApiKey());

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // Send request
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while calling OpenAI API.";
        }
    }
}