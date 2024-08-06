package com.example.Trip_In_Jeju.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Long id;
    private String title;
    private String place;
    private String thumbnailImg;
    private String content;
    private String category;

    public Result(Long id, String title, String place, String thumbnailImg, String content) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.thumbnailImg = thumbnailImg;
        this.content = content;
    }
}