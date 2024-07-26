package com.example.Trip_In_Jeju.Aitravel.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AiTravelService {

    private final WebClient webClient;

    public AiTravelService(@Value("${openai.api-key}") String apiKey) {
        System.out.println("Loaded API Key: " + apiKey); // API 키가 올바르게 로드되는지 확인
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json") // 추가된 헤더
                .build();
    }

    public Mono<String> getAiResponse(String question) {
        return webClient.post()
                .uri("engines/davinci/completions")
                .bodyValue(buildRequestBody(question))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    System.out.println("Client Error: " + response.statusCode());
                    return Mono.error(new RuntimeException("Client Error: " + response.statusCode()));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    System.out.println("Server Error: " + response.statusCode());
                    return Mono.error(new RuntimeException("Server Error: " + response.statusCode()));
                })
                .bodyToMono(String.class);
    }

    private String buildRequestBody(String question) {
        return "{\"prompt\": \"" + question + "\", \"max_tokens\": 150}";
    }
}