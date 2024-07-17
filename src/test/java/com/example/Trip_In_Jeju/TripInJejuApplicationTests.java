package com.example.Trip_In_Jeju;

import com.example.Trip_In_Jeju.kategorie.dessert.service.DessertService;
import com.example.Trip_In_Jeju.kategorie.food.service.FoodService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TripInJejuApplicationTests {

	@Autowired
	private FoodService foodService;
	@Autowired
	private DessertService dessertService;

	@Test@DisplayName("음식점")
	void test1() {
		for ( int i = 1; i <= 200; i++ ) {
			String title = String.format("필선귤:[%03d]", i);
			String content = String.format("내용:[%03d]", i);
			foodService.create(title, content);
		}
	}

	@Test@DisplayName("디저트")
	void test2() {
		for ( int i = 1; i <= 200; i++ ) {
			String title = String.format("카페:[%03d]", i);
			String content = String.format("내용:[%03d]", i);
			dessertService.create(title, content);
		}
	}

}
