package com.example.Trip_In_Jeju.response.repository;

import com.example.Trip_In_Jeju.response.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResponseRepository extends JpaRepository<Response, Long> {
}
