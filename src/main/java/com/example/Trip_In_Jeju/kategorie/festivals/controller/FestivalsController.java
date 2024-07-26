package com.example.Trip_In_Jeju.kategorie.festivals.controller;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/festivals")
public class FestivalsController {
    private final FestivalsService festivalsService;
    private final RatingService ratingService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "subCategory", defaultValue = "all") String subCategory
    ) {
        Page<Festivals> paging = festivalsService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        return "festivals/list";
    }

    @GetMapping("/detail/{id}")
    public String getFestivalsDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Festivals festivals = festivalsService.getFestivalsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "festivals");
        double averageScore = ratingService.calculateAverageScore(id, "festivals");
        String nickname = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                nickname = ((UserDetails) principal).getUsername();
            } else {
                nickname = principal.toString();
            }
        }
        model.addAttribute("festivals", festivals);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("nickname", nickname);
        return "festivals/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Festivals festivals = festivalsService.getFestivalsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "festivals");
        double averageScore = ratingService.calculateAverageScore(id, "festivals");

        model.addAttribute("festivals", festivals);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        return "festivals/review";
    }

    @PostMapping("/review/{id}")
    public String submitRating(
            @PathVariable("id") Long id,
            @RequestParam("score") Integer score,
            @RequestParam(value = "ratingId", required = false) Long ratingId, // ratingId는 optional로 설정
            @RequestParam("comment") String comment,
            Authentication authentication,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/festivals/detail/" + id;
        }
        String nickname = ((UserDetails) authentication.getPrincipal()).getUsername();
        ratingService.saveRating(id, score, ratingId, comment, nickname, thumbnail, "festivals");
        return "redirect:/festivals/detail/" + id;
    }

    @PostMapping("/review/edit/{id}")
    public String updateRating(
            @PathVariable("id") Long id,
            @RequestParam("ratingId") Long ratingId,
            @RequestParam("score") Integer score,
            @RequestParam("comment") String comment
    ) {
        ratingService.updateRating(ratingId, score, comment);
        return "redirect:/festivals/detail/" + id;
    }

    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, @RequestParam("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/festivals/detail/" + id;
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id) {
        festivalsService.incrementLikes(id);
        return "redirect:/festivals/detail/" + id;
    }
}
