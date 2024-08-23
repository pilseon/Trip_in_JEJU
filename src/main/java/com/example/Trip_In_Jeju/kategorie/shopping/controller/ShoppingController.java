package com.example.Trip_In_Jeju.kategorie.shopping.controller;


import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.kategorie.shopping.service.ShoppingService;
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
@RequestMapping("/shopping")
public class ShoppingController {
    private final ShoppingService shoppingService;
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
        Page<Shopping> paging = shoppingService.getList(page, subCategory);
        model.addAttribute("paging", paging);
        model.addAttribute("subCategory", subCategory);
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        return "shopping/list";
    }

    @GetMapping("/detail/{id}")
    public String getShoppingDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Shopping shopping = shoppingService.getShoppingById(id);
        List<Rating> ratings = ratingService.getRatings(id, "shopping");
        double averageScore = ratingService.calculateAverageScore(id, "shopping");
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
        model.addAttribute("shopping", shopping);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", shopping.getTitle());
        return "shopping/detail";
    }

    @GetMapping("/review/{id}")
    public String getReviewPage(@PathVariable("id") Long id, Model model) {
        Shopping shopping = shoppingService.getShoppingById(id);
        List<Rating> ratings = ratingService.getRatings(id, "shopping");
        double averageScore = ratingService.calculateAverageScore(id, "shopping");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);

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

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getNickname();
        } else if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Shopping shopping = shoppingService.getShoppingById(id);
        String categoryTitle = shopping.getTitle();

        ratingService.saveRating(id, score, ratingId, comment, username, thumbnail, "shopping", categoryTitle);
        return "redirect:/shopping/detail/" + id;
    }


    @GetMapping("/review/edit/{ratingId}")
    public String getEditPage(@PathVariable("ratingId") Long ratingId, Model model) {
        // 특정 리뷰를 가져오기
        Rating rating = ratingService.getRatingById(ratingId);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }

        // 리뷰에 대한 디저트 정보를 가져오기
        Shopping shopping = shoppingService.getShoppingById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "shopping");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "shopping");

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
        model.addAttribute("shopping", shopping);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", shopping.getTitle());

        // 리뷰 수정 페이지로 이동
        return "shopping/edit";
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
        Shopping shopping = shoppingService.getShoppingById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "shopping");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "shopping");

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
        model.addAttribute("shopping", shopping);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", shopping.getTitle());

        // 업데이트된 리뷰 페이지로 리다이렉트
        return "redirect:/shopping/detail/" + rating.getItemId();
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

    @PostMapping("/scrap/{id}")
    public String toggleScrap(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return "redirect:/shopping/detail/" + id;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        Member member = memberService.findByUsername(username).orElseThrow(() -> new RuntimeException("Member not found"));

        Shopping shopping = getShoppingById(id);
        boolean isScraped = scrapService.toggleScrap(shopping, member);

        return "redirect:/shopping/detail/" + id + (isScraped ? "?scraped=true" : "?scraped=false");
    }

    private Shopping getShoppingById(Long id) {
        return shoppingService.getShoppingById(id);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteShopping(@PathVariable("id") Long id, Model model) {

        shoppingService.deleteShopping(id);

        Member member = memberService.getCurrentMember();

        model.addAttribute("nickname", member);
        // 음식 목록 페이지로 리다이렉트
        return "redirect:/shopping/list";
    }


}
