package com.example.Trip_In_Jeju.location.service;

import com.example.Trip_In_Jeju.kategorie.activity.entity.Activity;
import com.example.Trip_In_Jeju.kategorie.activity.repository.ActivityRepository;
import com.example.Trip_In_Jeju.kategorie.attractions.entity.Attractions;
import com.example.Trip_In_Jeju.kategorie.attractions.repository.AttractionsRepository;
import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.repository.DessertRepository;
import com.example.Trip_In_Jeju.kategorie.festivals.entity.Festivals;
import com.example.Trip_In_Jeju.kategorie.festivals.repository.FestivalsRepository;
import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
import com.example.Trip_In_Jeju.kategorie.other.entity.Other;
import com.example.Trip_In_Jeju.kategorie.other.repository.OtherRepository;
import com.example.Trip_In_Jeju.kategorie.shopping.entity.Shopping;
import com.example.Trip_In_Jeju.kategorie.shopping.repository.ShoppingRepository;
import com.example.Trip_In_Jeju.location.dto.VisitRequest;
import com.example.Trip_In_Jeju.location.entity.VisitRecord;
import com.example.Trip_In_Jeju.location.repository.VisitRecordRepository;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitRecordService {

    private final VisitRecordRepository visitRecordRepository;
    private final MemberRepository memberRepository;
    private final FoodRepository foodRepository;
    private final ActivityRepository activityRepository;
    private final AttractionsRepository attractionsRepository;
    private final DessertRepository dessertRepository;
    private final FestivalsRepository festivalsRepository;
    private final OtherRepository otherRepository;
    private final ShoppingRepository shoppingRepository;

    private <T> T findEntityById(Long id, JpaRepository<T, Long> repository, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " not found"));
    }

    @Transactional
    public void registerVisit(VisitRequest visitRequest) {
        Member member = memberRepository.findById(visitRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setMember(member);
        visitRecord.setVisitTime(LocalDateTime.now());

        if (visitRequest.getFoodId() != null) {
            visitRecord.setFood(findEntityById(visitRequest.getFoodId(), foodRepository, "Food"));
        }

        if (visitRequest.getActivityId() != null) {
            visitRecord.setActivity(findEntityById(visitRequest.getActivityId(), activityRepository, "Activity"));
        }

        if (visitRequest.getAttractionsId() != null) {
            visitRecord.setAttractions(findEntityById(visitRequest.getAttractionsId(), attractionsRepository, "Attractions"));
        }

        if (visitRequest.getDessertId() != null) {
            visitRecord.setDessert(findEntityById(visitRequest.getDessertId(), dessertRepository, "Dessert"));
        }

        if (visitRequest.getFestivalsId() != null) {
            visitRecord.setFestivals(findEntityById(visitRequest.getFestivalsId(), festivalsRepository, "Festivals"));
        }

        if (visitRequest.getOtherId() != null) {
            visitRecord.setOther(findEntityById(visitRequest.getOtherId(), otherRepository, "Other"));
        }

        if (visitRequest.getShoppingId() != null) {
            visitRecord.setShopping(findEntityById(visitRequest.getShoppingId(), shoppingRepository, "Shopping"));
        }

        visitRecordRepository.save(visitRecord);
    }


    public List<VisitRecord> getVisitRecords(String username) {
        // username을 사용하여 Member를 찾음
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 해당 Member의 방문 기록을 조회
        return visitRecordRepository.findByMember(member, Sort.by(Sort.Direction.ASC, "visitTime"));
    }

    public void saveVisitRecord(VisitRequest visitRequest) {
        VisitRecord visitRecord = new VisitRecord();

        Member member = memberRepository.findById(visitRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        visitRecord.setMember(member);
        visitRecord.setVisitTime(LocalDateTime.now());

        if (visitRequest.getFoodId() != null) {
            visitRecord.setFood(findEntityById(visitRequest.getFoodId(), foodRepository, "Food"));
        }

        if (visitRequest.getActivityId() != null) {
            visitRecord.setActivity(findEntityById(visitRequest.getActivityId(), activityRepository, "Activity"));
        }

        if (visitRequest.getAttractionsId() != null) {
            visitRecord.setAttractions(findEntityById(visitRequest.getAttractionsId(), attractionsRepository, "Attractions"));
        }

        if (visitRequest.getDessertId() != null) {
            visitRecord.setDessert(findEntityById(visitRequest.getDessertId(), dessertRepository, "Dessert"));
        }

        if (visitRequest.getFestivalsId() != null) {
            visitRecord.setFestivals(findEntityById(visitRequest.getFestivalsId(), festivalsRepository, "Festivals"));
        }

        if (visitRequest.getOtherId() != null) {
            visitRecord.setOther(findEntityById(visitRequest.getOtherId(), otherRepository, "Other"));
        }

        if (visitRequest.getShoppingId() != null) {
            visitRecord.setShopping(findEntityById(visitRequest.getShoppingId(), shoppingRepository, "Shopping"));
        }

        visitRecordRepository.save(visitRecord);
    }

    public void addVisitRecord(Long memberId, String category, Long itemId) {
        // memberId로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setMember(member);
        visitRecord.setVisitTime(LocalDateTime.now());

        boolean exists = false;

        switch (category.toLowerCase()) {
            case "food":
                Food food = foodRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Food not found"));
                exists = visitRecordRepository.existsByMemberAndFood(member, food);
                if (!exists) {
                    visitRecord.setFood(food);
                }
                break;
            case "activity":
                Activity activity = activityRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Activity not found"));
                exists = visitRecordRepository.existsByMemberAndActivity(member, activity);
                if (!exists) {
                    visitRecord.setActivity(activity);
                }
                break;
            case "attractions":
                Attractions attractions = attractionsRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Attractions not found"));
                exists = visitRecordRepository.existsByMemberAndAttractions(member, attractions);
                if (!exists) {
                    visitRecord.setAttractions(attractions);
                }
                break;
            case "dessert":
                Dessert dessert = dessertRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Dessert not found"));
                exists = visitRecordRepository.existsByMemberAndDessert(member, dessert);
                if (!exists) {
                    visitRecord.setDessert(dessert);
                }
                break;
            case "festivals":
                Festivals festivals = festivalsRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Festivals not found"));
                exists = visitRecordRepository.existsByMemberAndFestivals(member, festivals);
                if (!exists) {
                    visitRecord.setFestivals(festivals);
                }
                break;
            case "other":
                Other other = otherRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Other not found"));
                exists = visitRecordRepository.existsByMemberAndOther(member, other);
                if (!exists) {
                    visitRecord.setOther(other);
                }
                break;
            case "shopping":
                Shopping shopping = shoppingRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Shopping not found"));
                exists = visitRecordRepository.existsByMemberAndShopping(member, shopping);
                if (!exists) {
                    visitRecord.setShopping(shopping);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        if (exists) {
            throw new RuntimeException("This visit record already exists.");
        }

        visitRecordRepository.save(visitRecord);
    }

    public List<VisitRecord> getVisitRecordsByMemberId(Long memberId, String category) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        switch (category.toLowerCase()) {
            case "food":
                return visitRecordRepository.findByMemberAndFoodIsNotNull(member);
            case "activity":
                return visitRecordRepository.findByMemberAndActivityIsNotNull(member);
            case "attractions":
                return visitRecordRepository.findByMemberAndAttractionsIsNotNull(member);
            case "dessert":
                return visitRecordRepository.findByMemberAndDessertIsNotNull(member);
            case "festivals":
                return visitRecordRepository.findByMemberAndFestivalsIsNotNull(member);
            case "other":
                return visitRecordRepository.findByMemberAndOtherIsNotNull(member);
            case "shopping":
                return visitRecordRepository.findByMemberAndShoppingIsNotNull(member);
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

    public boolean hasVisited(Long memberId, String category, Long itemId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        switch (category.toLowerCase()) {
            case "food":
                return visitRecordRepository.existsByMemberIdAndFoodId(memberId, itemId);
            case "activity":
                return visitRecordRepository.existsByMemberIdAndActivityId(memberId, itemId);
            case "attractions":
                return visitRecordRepository.existsByMemberIdAndAttractionsId(memberId, itemId);
            case "dessert":
                return visitRecordRepository.existsByMemberIdAndDessertId(memberId, itemId);
            case "festivals":
                return visitRecordRepository.existsByMemberIdAndFestivalsId(memberId, itemId);
            case "other":
                return visitRecordRepository.existsByMemberIdAndOtherId(memberId, itemId);
            case "shopping":
                return visitRecordRepository.existsByMemberIdAndShoppingId(memberId, itemId);
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

    public List<VisitRecord> getAllVisitRecordsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return visitRecordRepository.findByMember(member);
    }
}