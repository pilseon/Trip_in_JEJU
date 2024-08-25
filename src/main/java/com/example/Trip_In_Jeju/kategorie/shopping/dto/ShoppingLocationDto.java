package com.example.Trip_In_Jeju.kategorie.shopping.dto;

import lombok.Data;

@Data
public class ShoppingLocationDto {
    private Long id;  // 여기에 ID 필드 추가
    private String title;
    private double latitude;
    private double longitude;
    private String category;
    private String place;

    public ShoppingLocationDto(Long id, String title, double latitude, double longitude, String category, String place) {
        this.id = id;  // ID 필드 초기화
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.place = place;
    }
}