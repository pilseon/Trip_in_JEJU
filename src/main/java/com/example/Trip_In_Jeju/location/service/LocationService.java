package com.example.Trip_In_Jeju.location.service;

import com.example.Trip_In_Jeju.location.entity.Location;
import com.example.Trip_In_Jeju.location.repository.LocationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    @Getter
    @Value("${kakao.api.key}")
    private String apiKey;

    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location saveLocation(Location location) {
        // 주소를 기반으로 위도와 경도 값 설정
        String address = location.getAddress();
        System.out.println("주소: " + address);  // 디버깅: 주소 출력

        double[] latLng = getLatLngFromAddress(address);
        if (latLng.length == 0 || (latLng[0] == 0.0 && latLng[1] == 0.0)) {
            System.out.println("유효하지 않은 주소이거나, 좌표를 찾을 수 없습니다: " + address);
            // 오류 처리 로직 추가
        } else {
            location.setLatitude(latLng[0]);
            location.setLongitude(latLng[1]);
        }

        // 주소를 다시 설정하고 확인
        location.setAddress(address);
        System.out.println("설정된 주소: " + location.getAddress());  // 디버깅: 설정된 주소 출력

        return locationRepository.save(location);
    }

    private double[] getLatLngFromAddress(String address) {
        // Kakao Map API 요청 URL 구성
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        // RestTemplate 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        String jsonString = response.getBody();

        double latitude = 0.0;
        double longitude = 0.0;

        try {
            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode documents = rootNode.path("documents");

            if (documents.isArray() && documents.size() > 0) {
                JsonNode firstResult = documents.get(0);
                latitude = firstResult.path("y").asDouble();
                longitude = firstResult.path("x").asDouble();
            } else {
                throw new IllegalArgumentException("No results found for the given address.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Kakao Map API response", e);
        }

        return new double[]{latitude, longitude};
    }
}