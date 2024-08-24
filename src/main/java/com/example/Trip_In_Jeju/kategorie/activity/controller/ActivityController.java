package com.example.Trip_In_Jeju.kategorie.activity.controller;

import com.example.Trip_In_Jeju.kategorie.activity.dto.ActivityLocationDto;
import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.service.ActivityService;
import com.example.Trip_In_Jeju.like.LikeService;
import com.example.Trip_In_Jeju.location.dto.LocationRequest;
import com.example.Trip_In_Jeju.location.service.VisitRecordService;
import com.example.Trip_In_Jeju.member.CustomUserDetails;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import com.example.Trip_In_Jeju.rating.entity.Rating;
import com.example.Trip_In_Jeju.rating.service.RatingService;
import com.example.Trip_In_Jeju.scrap.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final RatingService ratingService;
    private final MemberService memberService;
    private final ScrapService scrapService;
    private final LikeService likeService;
    private final VisitRecordService visitRecordService;

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
    public String getDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Activity activity = activityService.getActivityById(id);
        List<Rating> ratings = ratingService.getRatings(id, "activity");
        double averageScore = ratingService.calculateAverageScore(id, "activity");
        Member currentMember = memberService.getCurrentMember();
        model.addAttribute("member", currentMember);
        String username = null;
        boolean canWriteReview = false; // 방문 확인
        boolean hasWrittenReview = false;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }

        if (currentMember != null) {
            canWriteReview = visitRecordService.hasVisited(currentMember.getId(), "activity", id);  // category 추가
            hasWrittenReview = ratingService.hasUserWrittenReview(currentMember.getUsername(), id, "activity");
        }

        model.addAttribute("activity", activity);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("canWriteReview", canWriteReview);
        model.addAttribute("hasWrittenReview", hasWrittenReview); // 방문 확인
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

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            username = ((CustomUserDetails) principal).getNickname();
        } else if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Activity activity = activityService.getActivityById(id);
        String categoryTitle = activity.getTitle();

        ratingService.saveRating(id, score, ratingId, comment, username, thumbnail, "activity", categoryTitle);
        return "redirect:/activity/detail/" + id;
    }

    @GetMapping("/review/edit/{ratingId}")
    public String getEditPage(@PathVariable("ratingId") Long ratingId, Model model) {
        // 특정 리뷰를 가져오기
        Rating rating = ratingService.getRatingById(ratingId);
        if (rating == null) {
            throw new RuntimeException("Rating not found");
        }

        // 리뷰에 대한 디저트 정보를 가져오기
        Activity activity = activityService.getActivityById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "activity");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "activity");

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
        model.addAttribute("activity", activity);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", activity.getTitle());

        // 리뷰 수정 페이지로 이동
        return "activity/edit";
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
        Activity activity = activityService.getActivityById(rating.getItemId());

        // 해당 디저트에 대한 모든 리뷰를 가져오기
        List<Rating> ratings = ratingService.getRatings(rating.getItemId(), "activity");

        // 평균 점수 계산
        double averageScore = ratingService.calculateAverageScore(rating.getItemId(), "activity");

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
        model.addAttribute("activity", activity);
        model.addAttribute("ratings", ratings);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("username", username);
        model.addAttribute("categoryTitle", activity.getTitle());

        // 업데이트된 리뷰 페이지로 리다이렉트
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

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteActivity(@PathVariable("id") Long id, Model model) {
        // 해당 음식 ID로 음식 정보를 가져옵니다.
        Activity activity = activityService.getActivityById(id);

        activityService.deleteActivity(id);

        Member member = memberService.getCurrentMember();

        model.addAttribute("nickname", member);
        // 음식 목록 페이지로 리다이렉트
        return "redirect:/activity/list";
    }

    @PostMapping("/check-visit")
    public ResponseEntity<?> checkVisit(@RequestParam("memberId") Long memberId, @RequestBody LocationRequest locationRequest) {
        activityService.processActivityLocation(memberId, locationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/locations")
    public ResponseEntity<List<ActivityLocationDto>> getActivityLocations() {
        List<ActivityLocationDto> locations = activityService.getAllActivityLocations();
        return ResponseEntity.ok(locations);
    }
}
