package com.example.Trip_In_Jeju.location.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitRequest {
    private Long memberId;
    private Long foodId;
    private Long activityId;
    private Long attractionsId;
    private Long dessertId;
    private Long festivalsId;
    private Long otherId;
    private Long shoppingId;

    // 기본 생성자
    public VisitRequest() {}

    // 매개변수 있는 생성자 (필요한 경우)
    public VisitRequest(Long memberId, Long foodId, Long activityId, Long attractionsId, Long dessertId, Long festivalsId, Long otherId, Long shoppingId) {
        this.memberId = memberId;
        this.foodId = foodId;
        this.activityId = activityId;
        this.attractionsId = attractionsId;
        this.dessertId = dessertId;
        this.festivalsId = festivalsId;
        this.otherId = otherId;
        this.shoppingId = shoppingId;
    }
}