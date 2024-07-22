package com.example.Trip_In_Jeju.rating.controller;

import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/review/{dessertId}")
    public String getReviewPage(@PathVariable Long dessertId, Model model) {
        // 수정된 부분: dessertId를 전달하여 모든 리뷰와 평균 점수를 가져옴
        model.addAttribute("allRatings", ratingService.getAllRatings(dessertId));
        model.addAttribute("averageScore", ratingService.calculateAverageScore(dessertId));
        return "rating/review";
    }

    @PostMapping("/review/save")
    public String saveRating(@RequestParam Long dessertId, @RequestParam Integer score, @RequestParam String comment, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/dessert/detail/" + dessertId;
        }
        String nickname = ((UserDetails) authentication.getPrincipal()).getUsername();
        // 수정된 부분: dessertId를 전달하여 새로운 리뷰를 저장
        ratingService.saveRating(dessertId, score, comment, nickname);
        return "redirect:/dessert/detail/" + dessertId;
    }
}
