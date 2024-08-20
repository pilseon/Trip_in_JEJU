package com.example.Trip_In_Jeju.game.controller;

import com.example.Trip_In_Jeju.game.entity.Game;
import com.example.Trip_In_Jeju.game.service.GameService;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final MemberService memberService;


    @GetMapping("/index")
    public String showGamePage(Model model) {
        Member currentMember = memberService.getCurrentMember();
        List<Game> topScores = gameService.getTopScores();
        topScores.forEach(game -> {
            if (game.getMember().getThumbnailImg() == null || game.getMember().getThumbnailImg().isEmpty()) {
                game.getMember().setThumbnailImg("https://i.ibb.co/mJYXKqb/images.jpg"); // 기본 이미지 설정
            }
        });
        model.addAttribute("topScores", topScores);
        model.addAttribute("member", currentMember);
        return "game/index"; // game.html 파일이 열림
    }
    // 점수 저장
    @PostMapping("/score")
    public ResponseEntity<Map<String, Object>> saveScore(@RequestBody Map<String, Object> requestData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // username으로 Member 객체 조회
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        int score = Integer.parseInt(requestData.get("score").toString());

        Game savedGame = gameService.saveScore(member.getId(), score);

        String profileImageUrl = "/imagefile/post/post/" + member.getThumbnailImg(); // 프로필 이미지 URL

        Map<String, Object> response = new HashMap<>();
        response.put("game", savedGame);
        response.put("profileImageUrl", profileImageUrl); // 프로필 이미지 URL을 반환

        return ResponseEntity.ok(response);
    }

    // 상위 점수 조회
    @GetMapping("/top-scores")
    public ResponseEntity<List<Game>> getTopScores() {
        List<Game> topScores = gameService.getTopScores();
        return ResponseEntity.ok(topScores);
    }
}
