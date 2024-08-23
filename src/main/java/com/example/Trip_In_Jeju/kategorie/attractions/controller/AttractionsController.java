package com.example.Trip_In_Jeju.kategorie.attractions.controller;


import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.service.AttractionsService;
import com.example.Trip_In_Jeju.like.LikeService;
import com.example.Trip_In_Jeju.member.CustomUserDetails;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import com.example.Trip_In_Jeju.scrap.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attractions")
public class AttractionsController {
    private final AttractionsService attractionsService;
    private final RatingService ratingService;
    private final MemberService memberService;
    private final ScrapService scrapService;
    private final LikeService likeService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "subCategory", defaultValue = "all") String subCategory
    ) {
        Page<Attractions> paging = attractionsService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "attractions/list";
    }

    @GetMapping("/detail/{id}")
    public String getAttractionsDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Attractions attractions = attractionsService.getAttractionsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "attractions");
        double averageScore = ratingService.calculateAverageScore(id, "attractions");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        String username = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }
        model.addAttribute("attractions", attractions);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", attractions.getTitle());
        return "attractions/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Attractions attractions = attractionsService.getAttractionsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "attractions");
        double averageScore = ratingService.calculateAverageScore(id, "attractions");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

        model.addAttribute("attractions", attractions);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        return "attractions/review";
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
            return "redirect:/attractions/detail/" + id;
        }

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getNickname();
        } else if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Attractions attractions = attractionsService.getAttractionsById(id);
        String categoryTitle = attractions.getTitle();

        ratingService.saveRating(id, score, ratingId, comment, username, thumbnail, "attractions", categoryTitle);
        return "redirect:/attractions/detail/" + id;
    }



    @GetMapping("/review/edit/{ratingId}")
    public String getEditPage(@PathVariable("ratingId") Long ratingId, Model model) {
        // 특정 리뷰를 가져오기
        Rating rating = ratingService.getRatingById(ratingId);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }

        // 리뷰에 대한 디저트 정보를 가져오기
        Attractions attractions = attractionsService.getAttractionsById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "attractions");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "attractions");

        // 현재 로그인된 사용자 정보 가져오기
        Member currentMember = memberService.getCurrentMember();

        // 사용자 인증 정보 가져오기
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                username = ((CustomUserDetails) principal).getNickname();
            } else if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }

        // Model에 필요한 데이터 추가
        model.addAttribute("rating", rating);
        model.addAttribute("member", currentMember);
        model.addAttribute("attractions", attractions);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", attractions.getTitle());

        // 리뷰 수정 페이지로 이동
        return "attractions/edit";
    }


    @PostMapping("/review/edit/{ratingId}")
    public String updateRating(
            @PathVariable("ratingId") Long ratingId,
            @RequestParam("score") Integer score,
            @RequestParam("comment") String comment,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,

            Model model // Model을 파라미터로 추가
    ) {
        // 리뷰 업데이트 처리
        ratingService.updateRating2(ratingId, score, comment, thumbnail);

        // 업데이트 후 해당 리뷰의 디저트 정보를 가져오기
        Rating rating = ratingService.getRatingById(ratingId);
        Attractions attractions = attractionsService.getAttractionsById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "attractions");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "attractions");

        // 현재 로그인된 사용자 정보 가져오기
        Member currentMember = memberService.getCurrentMember();

        // 사용자 인증 정보 가져오기
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                username = ((CustomUserDetails) principal).getNickname();
            } else if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }

        // Model에 필요한 데이터 추가
        model.addAttribute("member", currentMember);
        model.addAttribute("attractions", attractions);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", attractions.getTitle());

        // 업데이트된 리뷰 페이지로 리다이렉트
        return "redirect:/attractions/detail/" + rating.getItemId();
    }


    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, @RequestParam("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/attractions/detail/" + id;
    }


    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/attractions/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Member> memberOptional = memberService.findByUsername(username);

        if (!memberOptional.isPresent()) {
            return "redirect:/attractions/detail/" + id + "?error=memberNotFound";
        }

        Member member = memberOptional.get();
        boolean liked = attractionsService.toggleLike(id, member);

        if (!liked) {
            return "redirect:/attractions/detail/" + id + "?error=alreadyLiked";
        }

        return "redirect:/attractions/detail/" + id;
    }
    @PostMapping("/scrap/{id}")
    public String toggleScrap(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/attractions/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Member member = memberService.findByUsername(username).orElseThrow(() -> new RuntimeException("Member not found"));

        Attractions attractions = attractionsService.getAttractionsById(id);
        boolean isScraped = scrapService.toggleScrap(attractions, member);

        return "redirect:/attractions/detail/" + id + (isScraped ? "?scraped=true" : "?scraped=false");
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteAttractions(@PathVariable("id") Long id, Model model) {

        attractionsService.deleteAttractions(id);

        Member member = memberService.getCurrentMember();

        model.addAttribute("nickname", member);
        // 음식 목록 페이지로 리다이렉트
        return "redirect:/attractions/list";
    }
}
