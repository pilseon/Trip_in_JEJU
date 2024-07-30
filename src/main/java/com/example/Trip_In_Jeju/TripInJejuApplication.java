package com.example.Trip_In_Jeju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TripInJejuApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripInJejuApplication.class, args);
	}

}
