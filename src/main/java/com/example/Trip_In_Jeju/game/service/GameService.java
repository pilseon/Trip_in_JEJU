    package com.example.Trip_In_Jeju.game.service;

    import com.example.Trip_In_Jeju.game.entity.Game;
    import com.example.Trip_In_Jeju.game.repository.GameRepository;
    import com.example.Trip_In_Jeju.member.entity.Member;
    import com.example.Trip_In_Jeju.member.repository.MemberRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class GameService {
        private final GameRepository gameRepository;
        private final MemberRepository memberRepository; // MemberRepository 주입

        public Game saveScore(Long memberId, int score) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

            // 기존 점수 조회
            Game existingGame = gameRepository.findTopByMemberOrderByScoreDesc(member)
                    .orElse(null);

            if (existingGame != null && score > existingGame.getScore()) {
                // 기존 기록보다 높은 점수일 경우 업데이트
                existingGame.setScore(score);
                existingGame.setCreatedAt(LocalDateTime.now());
                return gameRepository.save(existingGame);
            } else if (existingGame == null) {
                // 새로운 기록일 경우 저장
                Game game = new Game();
                game.setMember(member);
                game.setScore(score);
                game.setCreatedAt(LocalDateTime.now());
                return gameRepository.save(game);
            }

            return existingGame; // 기존 점수가 더 높을 경우 아무것도 하지 않음
        }

        public List<Game> getTopScores() {
            return gameRepository.findTop10ByOrderByScoreDesc();
        }
    }
