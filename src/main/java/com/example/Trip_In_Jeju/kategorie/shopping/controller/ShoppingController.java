package com.example.Trip_In_Jeju.kategorie.shopping.controller;


import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.kategorie.shopping.service.ShoppingService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shopping")
public class ShoppingController {
    private final ShoppingService shoppingService;
    private final RatingService ratingService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "subCategory", defaultValue = "all") String subCategory
    ) {
        Page<Shopping> paging = shoppingService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        return "shopping/list";
    }

    @GetMapping("/detail/{id}")
    public String getShoppingDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Shopping shopping = shoppingService.getShoppingById(id);
        List<Rating> ratings = ratingService.getRatings(id, "shopping");
        double averageScore = ratingService.calculateAverageScore(id, "shopping");
        String nickname = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                nickname = ((UserDetails) principal).getUsername();
            } else {
                nickname = principal.toString();
            }
        }
        model.addAttribute("shopping", shopping);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("nickname", nickname);
        return "shopping/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Shopping shopping = shoppingService.getShoppingById(id);
        List<Rating> ratings = ratingService.getRatings(id, "shopping");
        double averageScore = ratingService.calculateAverageScore(id, "shopping");

        model.addAttribute("shopping", shopping);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        return "shopping/review";
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
            return "redirect:/shopping/detail/" + id;
        }
        String nickname = ((UserDetails) authentication.getPrincipal()).getUsername();
        ratingService.saveRating(id, score, ratingId, comment, nickname, thumbnail, "shopping");
        return "redirect:/shopping/detail/" + id;
    }

    @PostMapping("/review/edit/{id}")
    public String updateRating(
            @PathVariable("id") Long id,
            @RequestParam("ratingId") Long ratingId,
            @RequestParam("score") Integer score,
            @RequestParam("comment") String comment
    ) {
        ratingService.updateRating(ratingId, score, comment);
        return "redirect:/shopping/detail/" + id;
    }

    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, @RequestParam("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/shopping/detail/" + id;
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/shopping/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Member> memberOptional = memberService.findByUsername(username);

        if (!memberOptional.isPresent()) {
            return "redirect:/shopping/detail/" + id + "?error=memberNotFound";
        }

        Member member = memberOptional.get();
        boolean liked = shoppingService.toggleLike(id, member);

        if (!liked) {
            return "redirect:/shopping/detail/" + id + "?error=alreadyLiked";
        }

        return "redirect:/shopping/detail/" + id;
    }



}
