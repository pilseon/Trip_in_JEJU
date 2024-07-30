package com.example.Trip_In_Jeju.Aitravel.service;

import com.example.Trip_In_Jeju.Aitravel.entity.Message;
import com.example.Trip_In_Jeju.Aitravel.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Service
public class ChatbotService {

    @Autowired
    private MessageRepository messageRepository;

    private final String apiKey = "${openai.api-key}";
    private final String apiUrl = "https://api.openai.com/v1/chat/completions";

    public String getChatbotResponse(String userMessage) {
        // OpenAI API 호출을 위한 로직 작성
        // RestTemplate 또는 WebClient를 사용할 수 있습니다.

        // 메시지를 저장하는 로직 추가
        Message message = new Message();
        message.setSender("user");
        message.setContent(userMessage);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);

        // API 요청 후 응답 처리
        // 예시를 위해 RestTemplate을 사용한 API 호출 구현

        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("key", apiKey)
                .queryParam("message", userMessage);

        String response = restTemplate.getForObject(builder.toUriString(), String.class);

        // 챗봇 응답 저장
        Message botResponse = new Message();
        botResponse.setSender("bot");
        botResponse.setContent(response);
        botResponse.setTimestamp(LocalDateTime.now());
        messageRepository.save(botResponse);

        return response;
    }
}