    package com.example.Trip_In_Jeju.location.controller;


    import com.example.Trip_In_Jeju.location.dto.VisitRequest;
    import com.example.Trip_In_Jeju.location.service.VisitRecordService;
    import com.example.Trip_In_Jeju.member.entity.Member;
    import com.example.Trip_In_Jeju.member.servcie.MemberService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;

    @Slf4j
    @RestController
    @RequestMapping("/api")
    @RequiredArgsConstructor
    public class VisitRecordController {

        private final VisitRecordService visitRecordService;

        private final MemberService memberService;

        @PostMapping("/visitRecord")
        public ResponseEntity<?> registerVisit(@RequestBody VisitRequest visitRequest) {
            log.info("User is authenticated: {}", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
            try {
                System.out.println("VisitRecordController: registerVisit 메서드 호출됨");

                if (visitRequest.getMemberId() == null || visitRequest.getFoodId() == null) {
                    System.out.println("VisitRecordController: 잘못된 입력 데이터");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input data");
                }

                System.out.println("Received visit request with foodId: " + visitRequest.getFoodId() + " and memberId: " + visitRequest.getMemberId());

                visitRecordService.registerVisit(visitRequest.getMemberId(), visitRequest.getFoodId());
                System.out.println("VisitRecordController: 서비스 메서드 호출 완료");

                return ResponseEntity.ok("방문 기록 저장 완료");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("방문 기록 저장 실패");
            }
        }


        @GetMapping("/getMemberId")
        public ResponseEntity<Long> getMemberId() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication.getName() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String username = authentication.getName();
            Member member = memberService.findByUsername(username)
                    .orElse(null);

            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(member.getId());
        }
    }