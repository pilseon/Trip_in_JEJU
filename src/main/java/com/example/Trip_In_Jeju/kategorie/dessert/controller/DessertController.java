package com.example.Trip_In_Jeju.kategorie.dessert.controller;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dessert")
public class DessertController {
    private final DessertService dessertService;
    private final RatingService ratingService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Dessert> paging = dessertService.getList(page);
        model.addAttribute("paging", paging);
        return "dessert/list";
    }

    @GetMapping("/detail/{id}")
    public String getDessertDetail(@PathVariable Long id, Model model, Authentication authentication) {
        Dessert dessert = dessertService.getDessertById(id);
        List<Rating> ratings = ratingService.getRatings(id);
        double averageScore = ratingService.calculateAverageScore(id);
        String nickname = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                nickname = ((UserDetails) principal).getUsername();
            } else {
                nickname = principal.toString();
            }
        }
        model.addAttribute("dessert", dessert);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("nickname", nickname);
        return "dessert/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable Long id, Model model) {
        Dessert dessert = dessertService.getDessertById(id);
        List<Rating> ratings = ratingService.getRatings(id);
        double averageScore = ratingService.calculateAverageScore(id);

        model.addAttribute("dessert", dessert);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        return "dessert/review";
    }

    @PostMapping("/review/{id}")
    public String submitRating(@PathVariable Long id, @RequestParam Integer score, @RequestParam String comment, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/dessert/detail/" + id;
        }
        String nickname = ((UserDetails) authentication.getPrincipal()).getUsername();
        ratingService.saveRating(id, score, comment, nickname);
        return "redirect:/dessert/detail/" + id;
    }

    @PostMapping("/review/edit/{id}")
    public String updateRating(@PathVariable Long id, @RequestParam Long ratingId, @RequestParam Integer score, @RequestParam String comment) {
        ratingService.updateRating(ratingId, score, comment);
        return "redirect:/dessert/detail/" + id;
    }

    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable Long id, @RequestParam Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/dessert/detail/" + id;
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id) {
        dessertService.incrementLikes(id);
        return "redirect:/dessert/detail/" + id;
    }
}
