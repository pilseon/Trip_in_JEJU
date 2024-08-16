package com.example.Trip_In_Jeju.kategorie.festivals.controller;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.service.FestivalsService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/festivals")
public class FestivalsController {
    private final FestivalsService festivalsService;
    private final RatingService ratingService;
    private final MemberService memberService;
    private final ScrapService scrapService;

    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "subCategory", defaultValue = "all") String subCategory
    ) {
        Page<Festivals> paging = festivalsService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "festivals/list";
    }

    @GetMapping("/detail/{id}")
    public String getFestivalsDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Festivals festivals = festivalsService.getFestivalsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "festivals");
        double averageScore = ratingService.calculateAverageScore(id, "festivals");
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
        model.addAttribute("festivals", festivals);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", festivals.getTitle());
        return "festivals/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Festivals festivals = festivalsService.getFestivalsById(id);
        List<Rating> ratings = ratingService.getRatings(id, "festivals");
        double averageScore = ratingService.calculateAverageScore(id, "festivals");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

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

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getNickname();
        } else if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }


        Festivals festivals = festivalsService.getFestivalsById(id);
        String categoryTitle = festivals.getTitle();

        ratingService.saveRating(id, score, ratingId, comment, username, thumbnail, "festivals", categoryTitle);
        return "redirect:/festivals/detail/" + id;
    }


    @GetMapping("/review/edit/{ratingId}")
    public String getEditPage(@PathVariable("ratingId") Long ratingId, Model model) {
        // 특정 리뷰를 가져오기
        Rating rating = ratingService.getRatingById(ratingId);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }

        // 리뷰에 대한 디저트 정보를 가져오기
        Festivals festivals = festivalsService.getFestivalsById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "festivals");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "festivals");

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
        model.addAttribute("festivals", festivals);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", festivals.getTitle());

        // 리뷰 수정 페이지로 이동
        return "festivals/edit";
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
        Festivals festivals = festivalsService.getFestivalsById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "festivals");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "festivals");

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
        model.addAttribute("festivals", festivals);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", festivals.getTitle());

        // 업데이트된 리뷰 페이지로 리다이렉트
        return "redirect:/festivals/detail/" + rating.getItemId();
    }

    @GetMapping("/review/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, @RequestParam("ratingId") Long ratingId) {
        ratingService.deleteRating(ratingId);
        return "redirect:/festivals/detail/" + id;
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/festivals/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Optional<Member> memberOptional = memberService.findByUsername(username);

        if (!memberOptional.isPresent()) {
            return "redirect:/festivals/detail/" + id + "?error=memberNotFound";
        }

        Member member = memberOptional.get();
        boolean liked = festivalsService.toggleLike(id, member);

        if (!liked) {
            return "redirect:/festivals/detail/" + id + "?error=alreadyLiked";
        }

        return "redirect:/festivals/detail/" + id;
    }

    @PostMapping("/scrap/{id}")
    public String toggleScrap(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/festivals/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Member member = memberService.findByUsername(username).orElseThrow(() -> new RuntimeException("Member not found"));

        Festivals festivals = festivalsService.getFestivalsById(id);

        boolean isScraped = scrapService.toggleScrap(festivals, member);

        return "redirect:/festivals/detail/" + id + (isScraped ? "?scraped=true" : "?scraped=false");
    }

}
