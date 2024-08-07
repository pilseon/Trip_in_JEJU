package com.example.Trip_In_Jeju.inquiry.repository;

import com.example.Trip_In_Jeju.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
