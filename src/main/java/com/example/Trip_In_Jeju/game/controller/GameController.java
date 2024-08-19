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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final MemberService memberService;


    @GetMapping("/index")
    public String showGamePage() {
        return "game/index"; // `game.html` 파일이 `src/main/resources/templates`에 있어야 합니다.
    }

    // 점수 저장
    @PostMapping("/score")
    public ResponseEntity<Game> saveScore(@RequestBody Map<String, Object> requestData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // username으로 Member 객체 조회
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        int score = Integer.parseInt(requestData.get("score").toString());

        Game savedGame = gameService.saveScore(member.getId(), score);
        return ResponseEntity.ok(savedGame);
    }

    // 상위 점수 조회
    @GetMapping("/top-scores")
    public ResponseEntity<List<Game>> getTopScores() {
        List<Game> topScores = gameService.getTopScores();
        return ResponseEntity.ok(topScores);
    }
}
