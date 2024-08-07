package com.example.Trip_In_Jeju.scrap;


import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;

    @Transactional
    public boolean toggleScrap(Food food, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByFoodAndMember(food, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByFoodAndMember(food, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setFood(food);
            scrap.setMember(member);
            scrapRepository.save(scrap);
            return true; // 스크랩 추가됨
        }
    }

    @Transactional
    public boolean toggleScrap(Dessert dessert, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByDessertAndMember(dessert, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByDessertAndMember(dessert, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setDessert(dessert);
            scrap.setMember(member);
            scrapRepository.save(scrap);
            return true; // 스크랩 추가됨
        }
    }

    public int getScrapCount(Food food) {
        return scrapRepository.countByFood(food); // Food에 대한 스크랩 수 카운트
    }

    public int getScrapCount(Dessert dessert) {
        return scrapRepository.countByDessert(dessert); // Dessert에 대한 스크랩 수 카운트
    }

    public List<Scrap> getScrapsByMember(Member member) {
        return scrapRepository.findByMember(member);
    }
}