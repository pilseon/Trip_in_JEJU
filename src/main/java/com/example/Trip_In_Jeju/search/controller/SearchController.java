package com.example.Trip_In_Jeju.search.controller;

import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.search.dto.Result;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final CategorySearchService categorySearchService;

    @GetMapping("/searchForm")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            List<Result> results = searchService.searchByQuery(query);
            model.addAttribute("results", results);
        }
        model.addAttribute("query", query);
        return "search/searchForm";
    }

    @PostMapping
    public String performSearch(@RequestParam("query") String query, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        searchService.saveSearchTerm(member, query);
        return "redirect:/search/results?term=" + query;
    }

    @GetMapping("/results")
    public String searchByTerm(@RequestParam("term") String searchTerm, Model model) {
        List<Search> searchResults = searchService.getSearchResultsByTerm(searchTerm);
        model.addAttribute("searchResults", searchResults);
        return "search/searchResults";
    }

    private Member getMemberFromPrincipal(Principal principal) {
        // Implementation for retrieving a Member from Principal
        // This needs to be replaced with actual code to fetch the logged-in user
        return new Member();
    }

    @GetMapping("/suggestions")
    public String getSuggestions(@RequestParam("query") String query, Model model) {
        List<Result> suggestions = categorySearchService.findSuggestions(query);
        model.addAttribute("suggestions", suggestions);
        return "search/suggestions";
    }
}