package com.example.Trip_In_Jeju.soical.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyService {

    @Value("${kakao.map.appkey}")
    private String kakaoMapAppKey;

    public String getKakaoMapAppKey() {
        return kakaoMapAppKey;
    }
}
