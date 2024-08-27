package com.example.Trip_In_Jeju.kategorie.food.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("tests")
public class TestIntiData implements BeforeIntiData {
    @Bean
    CommandLineRunner initData() {
        return args -> {
            beforeInit();
        };
    }
}