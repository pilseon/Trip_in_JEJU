package com.example.Trip_In_Jeju.search.service;

import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.search.entity.Search;
import com.example.Trip_In_Jeju.search.ripository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;

    // 검색어 저장 메서드
    public void saveSearchTerm(Member member, String searchTerm) {
        Search search = new Search();
        search.setMember(member);
        search.setSearchTerm(searchTerm);
        search.setSearchTime(LocalDateTime.now());
        searchRepository.save(search);
    }

    // 특정 검색어로 검색 기록을 조회하는 메서드
    public List<Search> getSearchResultsByTerm(String searchTerm) {
        return searchRepository.findBySearchTerm(searchTerm);
    }
}