package com.example.Trip_In_Jeju.inquiry.service;

import com.example.Trip_In_Jeju.inquiry.entity.Inquiry;
import com.example.Trip_In_Jeju.inquiry.repository.InquiryRepository;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.entity.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InquiryService {
    @Autowired
    private InquiryRepository inquiryRepository;


    public Inquiry createInquiry(Inquiry inquiry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;

        // Authentication 객체에서 현재 사용자의 닉네임을 가져오기
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentUsername = userDetails.getUsername(); // 사용자 이름 가져오기 (닉네임이 사용자 이름이라고 가정)
        } else {
            // 인증 정보가 UserDetails가 아닌 경우 (예: AnonymousUser)
            currentUsername = authentication.getName();
        }

        inquiry.setAuthor(currentUsername); // 작성자 설정
        inquiry.setCreatedAt(LocalDateTime.now()); // 작성 시간 설정
        return inquiryRepository.save(inquiry);
    }

    public Inquiry getInquiry(Long id) {
        return inquiryRepository.findById(id).orElse(null);
    }

    public void addResponse(Long inquiryId, String content) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId).orElse(null);
        if (inquiry != null) {
            inquiry.setAnswer(content);
            inquiry.setStatus("ANSWERED");
            inquiryRepository.save(inquiry);
        }
    }

    public List<Inquiry> listInquiries() {
        return inquiryRepository.findAll();
    }

    public void deleteInquiry(Long id, Member currentUser) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry ID: " + id));


        if (currentUser.getRole() == MemberRole.ADMIN || inquiry.getAuthor().equals(currentUser.getUsername())) {
            inquiryRepository.deleteById(id);
        } else {
            throw new SecurityException("You do not have permission to delete this inquiry.");
        }
    }

}
