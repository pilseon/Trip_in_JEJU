package com.example.Trip_In_Jeju.kategorie.dessert.service;

import com.example.Trip_In_Jeju.kategorie.dessert.entity.Dessert;
import com.example.Trip_In_Jeju.kategorie.dessert.repository.DessertRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertRepository dessertRepository;
    public List<Dessert> getList() {
        return dessertRepository.findAll();
    }

    public void create(String title, String content) {
        Dessert d = new Dessert();
        d.setTitle(title);
        d.setContent(content);
        d.setCreateDate(LocalDateTime.now());
        dessertRepository.save(d);
    }

    public Dessert getDessert(Long id) {
        Optional<Dessert> dessert = dessertRepository.findById(id);
        return dessert.get();
    }
}
