package com.example.Trip_In_Jeju.email.repository;

import com.example.Trip_In_Jeju.email.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public  interface VerificationRepository extends JpaRepository<Verification, String> {

    Optional<Verification> findByEmailAndVerificationCode(String email, String verificationCode);
}
