package com.example.Trip_In_Jeju.kategorie.activity.controller;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
import com.example.Trip_In_Jeju.member.CustomUserDetails;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import com.example.Trip_In_Jeju.scrap.ScrapService;
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
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final RatingService ratingService;
    private final MemberService memberService;
    private final ScrapService scrapService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "subCategory", defaultValue = "all") String subCategory
    ) {
        Page<Activity> paging = activityService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "activity/list";
    }

    @GetMapping("/detail/{id}")
    public String getActivityDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Activity activity = activityService.getActivityById(id);
        List<Rating> ratings = ratingService.getRatings(id, "activity");
        double averageScore = ratingService.calculateAverageScore(id, "activity");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        String nickname = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                nickname = ((UserDetails) principal).getUsername();
            } else {
                nickname = principal.toString();
            }
        }
        model.addAttribute("activity", activity);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("nickname", nickname);
        return "activity/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Activity activity = activityService.getActivityById(id);
        List<Rating> ratings = ratingService.getRatings(id, "activity");
        double averageScore = ratingService.calculateAverageScore(id, "activity");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        model.addAttribute("activity", activity);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        return "activity/review";
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
            return "redirect:/activity/detail/" + id;
        }

        String nickname;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            nickname = ((CustomUserDetails) principal).getNickname();
        } else if (principal instanceof UserDetails) {
            nickname = ((UserDetails) principal).getUsername();
        } else {
            nickname = principal.toString();
        }

        ratingService.saveRating(id, score, ratingId, comment, nickname, thumbnail, "activity");
        return "redirect:/activity/detail/" + id;
    }


    @GetMapping("/review/edit/{ratingId}")
    public String getEditPage(@PathVariable("ratingId") Long ratingId, Model model) {
        Rating rating = ratingService.getRatingById(ratingId);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }
        model.addAttribute("rating", rating);
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "activity/edit";
    }

    @PostMapping("/review/edit/{ratingId}")
    public String updateRating(
            @PathVariable("ratingId") Long ratingId,
            @RequestParam("score") Integer score,
            @RequestParam("comment") String comment,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        ratingService.updateRating2(ratingId, score, comment, thumbnail);
        Rating rating = ratingService.getRatingById(ratingId);
        return "redirect:/activity/detail/" + rating.getItemId();
    }


    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, @RequestParam("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/activity/detail/" + id;
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/activity/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Member> memberOptional = memberService.findByUsername(username);

        if (!memberOptional.isPresent()) {
            return "redirect:/activity/detail/" + id + "?error=memberNotFound";
        }

        Member member = memberOptional.get();
        boolean liked = activityService.toggleLike(id, member);

        if (!liked) {
            return "redirect:/activity/detail/" + id + "?error=alreadyLiked";
        }

        return "redirect:/activity/detail/" + id;
    }

    @PostMapping("/scrap/{id}")
    public String toggleScrap(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/activity/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Member member = memberService.findByUsername(username).orElseThrow(() -> new RuntimeException("Member not found"));

        Activity activity = activityService.getActivityById(id);
        boolean isScraped = scrapService.toggleScrap(activity, member);

        return "redirect:/activity/detail/" + id + (isScraped ? "?scraped=true" : "?scraped=false");
    }


}
