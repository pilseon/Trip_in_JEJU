package com.example.Trip_In_Jeju.location.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitRequest {
    private Long foodId;
    private Long memberId;  // memberId 필드 추가

    // 기본 생성자
    public VisitRequest() {}

    // 매개변수 있는 생성자 (필요한 경우)
    public VisitRequest(Long foodId, Long memberId) {
        this.foodId = foodId;
        this.memberId = memberId;
    }
}