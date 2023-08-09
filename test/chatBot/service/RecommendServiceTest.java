package chatBot.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RecommendServiceTest {
	RecommendService rs = new RecommendService();
	
	@Test
	public void test() {
		List<String> list = new ArrayList<String>();
		list.add("아기");
		list.add("노인");
		String food = rs.recommendFoodName(list);
		assertEquals("해장국", food);
	}
	@Test
	public void test2() {
		List<String> test = new ArrayList<String>();
		test.add("가격대");
		test.add("내일");
		test.add("메뉴");
		
		List<String> list = rs.removeException(test);
		assertNull(list);
	}
	
}
