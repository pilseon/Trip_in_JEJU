package com.example.Trip_In_Jeju.scrap;


import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
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
            scrap.setContent(food.getContent());
            scrap.setTitle(food.getTitle());
            scrap.setThumbnailImg(food.getThumbnailImg());
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

    @Transactional
    public boolean toggleScrap(Activity activity, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByActivityAndMember(activity, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByActivityAndMember(activity, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setActivity(activity);
            scrap.setMember(member);
            scrapRepository.save(scrap);
            return true; // 스크랩 추가됨
        }
    }

    @Transactional
    public boolean toggleScrap(Attractions attractions, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByAttractionsAndMember(attractions, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByAttractionsAndMember(attractions, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setAttractions(attractions);
            scrap.setMember(member);
            scrapRepository.save(scrap);
            return true; // 스크랩 추가됨
        }
    }

    @Transactional
    public boolean toggleScrap(Other other, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByOtherAndMember(other, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByOtherAndMember(other, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setOther(other);
            scrap.setMember(member);
            scrapRepository.save(scrap);
            return true; // 스크랩 추가됨
        }
    }

    @Transactional
    public boolean toggleScrap(Shopping shopping, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByShoppingAndMember(shopping, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByShoppingAndMember(shopping, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setShopping(shopping);
            scrap.setMember(member);
            scrapRepository.save(scrap);
            return true; // 스크랩 추가됨
        }
    }

    @Transactional
    public boolean toggleScrap(Festivals festivals, Member member) {
        Optional<Scrap> existingScrap = scrapRepository.findByFestivalsAndMember(festivals, member);

        if (existingScrap.isPresent()) {
            scrapRepository.deleteByFestivalsAndMember(festivals, member);
            return false; // 스크랩 삭제됨
        } else {
            Scrap scrap = new Scrap();
            scrap.setFestivals(festivals);
            scrap.setPeriodStart(festivals.getPeriodStart());
            scrap.setPeriodEnd(festivals.getPeriodEnd());
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

    public int getScrapCount(Activity activity) {
        return scrapRepository.countByActivity(activity);
    }
    public int getScrapCount(Festivals festivals) {
        return scrapRepository.countByFestivals(festivals);
    }

    public int getScrapCount(Attractions attractions) {
        return scrapRepository.countByAttractions(attractions);
    }

    public int getScrapCount(Other other) {
        return scrapRepository.countByOther(other);
    }

    public int getScrapCount(Shopping shopping) {
        return scrapRepository.countByShopping(shopping);
    }

    public List<Scrap> getScrapsByMember(Member member) {
        return scrapRepository.findByMember(member);
    }

    @Transactional
    public void removeAllScrapsForItem(Food food) {
        scrapRepository.deleteByFoodId(food.getId());
    }

    @Transactional
    public void removeAllScrapsForItem(Festivals festivals) {
        scrapRepository.deleteByFestivalsId(festivals.getId());
    }

    @Transactional
    public void removeAllScrapsForItem(Activity activity) {
        scrapRepository.deleteByActivityId(activity.getId());
    }

    @Transactional
    public void removeAllScrapsForItem(Attractions attractions) {
        scrapRepository.deleteByAttractionsId(attractions.getId());
    }
    @Transactional
    public void removeAllScrapsForItem(Dessert dessert) {
        scrapRepository.deleteByDessertId(dessert.getId());
    }
    @Transactional
    public void removeAllScrapsForItem(Other other) {
        scrapRepository.deleteByOtherId(other.getId());
    }
    @Transactional
    public void removeAllScrapsForItem(Shopping shopping) {
        scrapRepository.deleteByShoppingId(shopping.getId());
    }

}