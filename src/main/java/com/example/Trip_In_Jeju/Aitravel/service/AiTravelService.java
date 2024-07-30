package com.example.Trip_In_Jeju.Aitravel.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiTravelService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public AiTravelService(@Value("${openai.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public String getAiResponse(String question) {
        String url = "https://api.openai.com/v1/engines/davinci/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = buildRequestBody(question);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    private String buildRequestBody(String question) {
        return "{\"prompt\": \"" + question + "\", \"max_tokens\": 150}";
    }
}