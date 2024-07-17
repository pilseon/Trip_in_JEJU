package com.example.Trip_In_Jeju.kategorie.dessert.repository;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;


@ResponseBody
public interface DessertRepository extends JpaRepository<Dessert, Long> {

}
