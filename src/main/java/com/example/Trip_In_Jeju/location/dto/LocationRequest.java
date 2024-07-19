package com.example.Trip_In_Jeju.location.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationRequest {

    private double latitude; // 위치의 위도 값을 저장

    private double longitude; // 위치의 경도 값을 저장

    private long timestamp; // 위치 정보가 수집된 시간을 밀리초 단위로 저장

}