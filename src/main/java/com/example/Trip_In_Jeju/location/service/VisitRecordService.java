package com.example.Trip_In_Jeju.location.service;

import com.example.Trip_In_Jeju.kategorie.food.entity.Food;
import com.example.Trip_In_Jeju.kategorie.food.repository.FoodRepository;
import com.example.Trip_In_Jeju.location.dto.VisitRequest;
import com.example.Trip_In_Jeju.location.entity.VisitRecord;
import com.example.Trip_In_Jeju.location.repository.VisitRecordRepository;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void registerVisit(Long memberId, Long foodId) {
        // 서비스 메서드 진입 로그
        System.out.println("VisitRecordService: registerVisit 메서드 호출됨");

        // memberId로 회원 조회 로그
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        System.out.println("Member found: " + member.getUsername());

        // foodId로 음식점 조회 로그
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food not found"));
        System.out.println("Food found: " + food.getTitle());

        // 방문 기록 생성 및 저장 로그
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setMember(member);
        visitRecord.setFood(food);
        visitRecord.setVisitTime(LocalDateTime.now());

        System.out.println("VisitRecordService: visitRecord 저장 시도");
        visitRecordRepository.save(visitRecord);
        System.out.println("VisitRecordService: visitRecord 저장 성공");
    }


    public List<VisitRecord> getVisitRecords(String username) {
        // username을 사용하여 Member를 찾음
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 해당 Member의 방문 기록을 조회
        return visitRecordRepository.findByMember(member);
    }

    public void saveVisitRecord(VisitRequest visitRequest) {
        VisitRecord visitRecord = new VisitRecord();

        // VisitRequest에서 memberId와 foodId를 사용하여 Member와 Food 객체를 가져옵니다.
        Member member = memberRepository.findById(visitRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        Food food = foodRepository.findById(visitRequest.getFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid food ID"));

        // VisitRecord에 Member와 Food를 설정합니다.
        visitRecord.setMember(member);
        visitRecord.setFood(food);
        visitRecord.setVisitTime(LocalDateTime.now());  // 현재 시간을 방문 시간으로 설정

        // VisitRecord를 저장합니다.
        visitRecordRepository.save(visitRecord);
    }

    public void addVisitRecord(Long memberId, Long foodId) {
        // memberId로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // foodId로 Food 조회
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        // 이미 존재하는 VisitRecord인지 확인
        boolean exists = visitRecordRepository.existsByMemberAndFood(member, food);
        if (exists) {
            throw new RuntimeException("This visit record already exists.");
        }

        // 새로운 VisitRecord 생성
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setMember(member);
        visitRecord.setFood(food);
        visitRecord.setVisitTime(LocalDateTime.now());

        // VisitRecord 저장
        visitRecordRepository.save(visitRecord);
    }

    public List<VisitRecord> getVisitRecordsByMemberId(Long memberId) {
        return visitRecordRepository.findByMemberId(memberId);
    }
    public boolean hasVisited(Long memberId, Long foodId) {
        return visitRecordRepository.existsByMemberIdAndFoodId(memberId, foodId);
    }
}