package chatBot.service;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chatBot.dao.ChatBotDAO;
import chatBot.model.WordCategory;
import util.DBUtil;

public class TestRecommendService {
	ChatBotDAO dao = new ChatBotDAO();
	
	public String recommendFoodName(List<String> knownList) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			List<WordCategory> fcList = new ArrayList<>();
			for (String knownWord : knownList) {
				String category = dao.getCategory(conn, knownWord);
				fcList.add(new WordCategory(knownWord, category));
			}
			String foodName = dao.getFoodName(conn, fcList);
			return foodName;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// excption테이블에 있는 필요없는 단어 제거해서 넘겨주기
	public List<String> removeException(List<String> list) {
		Connection conn = null;
		List<String> exceptionList = new ArrayList<>();
		List<String> words = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			exceptionList = dao.getExceptions(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		words.addAll(list);
		words.removeAll(exceptionList);

		return words;
	}
	
	@Test
	public void test1() {
		List<String> knownList = new ArrayList<String>();
		knownList.add("아기");
		String foodName = recommendFoodName(knownList);
		assertNotNull(foodName);
		System.out.println(foodName);
	}
	
	@Test
	public void test2() {
		List<String> knownList = new ArrayList<String>();
		knownList.add("오늘");
		knownList.add("나는");
		List<String> foodName = removeException(knownList);
		assertNotNull(foodName);
		System.out.println(foodName);
	}
	
}
