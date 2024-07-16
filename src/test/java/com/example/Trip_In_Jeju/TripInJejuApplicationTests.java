package com.example.Trip_In_Jeju;

import com.example.Trip_In_Jeju.food.service.FoodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TripInJejuApplicationTests {
	@Autowired
	private FoodService foodService;

	@Test@DisplayName("음식점")
	void test1() {
		for ( int i = 1; i <= 200; i++ ) {
			String title = String.format("필선귤:[%03d]", i);
			String content = String.format("내용:[%03d]", i);
			foodService.create(title, content);
		}
	}

}
