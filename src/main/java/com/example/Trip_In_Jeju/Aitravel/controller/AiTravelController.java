package com.example.Trip_In_Jeju.Aitravel.controller;

//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/AI")
//public class AiTravelController {
//
//    private final AiTravelService aiTravelService;
//
//    @GetMapping("/recommendations")
//    public String getRecommendationsPage() {
//        return "AI/recommendations";
//    }
//
//    @PostMapping("/recommendations")
//    public String getRecommendations(@RequestParam("question") String question, Model model) {
//        Mono<String> response = aiTravelService.getAiResponse(question);
//        model.addAttribute("question", question);
//        model.addAttribute("response", response.block());
//        return "AI/recommendations";
//    }
//}