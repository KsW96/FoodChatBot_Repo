package chatBot.service;

import static org.junit.Assert.assertEquals;

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
		List<String> list = new ArrayList<String>();
		
		rs.removeException(list);
	}
	
}
