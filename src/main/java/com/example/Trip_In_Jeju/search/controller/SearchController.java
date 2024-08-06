package com.example.Trip_In_Jeju.search.controller;

import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.search.entity.Search;
import com.example.Trip_In_Jeju.search.service.CategorySearchService;
import com.example.Trip_In_Jeju.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final CategorySearchService categorySearchService;

    @GetMapping("/searchForm")
    public String showSearchPage() {
        return "search/searchForm"; // 검색 폼 페이지로 이동
    }

    @PostMapping
    public String performSearch(@RequestParam("query") String query, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        searchService.saveSearchTerm(member, query);
        return "redirect:/search/results?term=" + query; // 검색 결과 페이지로 리다이렉트
    }

    @GetMapping("/results")
    public String searchByTerm(@RequestParam("term") String searchTerm, Model model) {
        List<Search> searchResults = searchService.getSearchResultsByTerm(searchTerm);
        model.addAttribute("searchResults", searchResults);
        return "search/searchResults"; // 결과를 보여줄 뷰 이름
    }

    private Member getMemberFromPrincipal(Principal principal) {
        // 실제 Member 객체를 조회하는 로직 구현 필요
        // return memberService.findByLoginId(principal.getName());
        return new Member(); // 임시 객체 반환
    }

    @GetMapping("/suggestions")
    public String getSuggestions(@RequestParam("query") String query, Model model) {
        List<Map<String, String>> suggestions = categorySearchService.findSuggestions(query);
        model.addAttribute("suggestions", suggestions);
        return "search/suggestions"; // 반환할 템플릿의 경로
    }
}