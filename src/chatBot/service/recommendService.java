package chatBot.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import chatBot.dao.ChatBotDAO;
import chatBot.model.FoodCount;
import util.DBUtil;

public class recommendService {
	ChatBotDAO dao = new ChatBotDAO();
	List<String> exceptionList = null;
	
	public String recommendFoodName(List<String> knownList) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			List<FoodCount> fcl = dao.getFoodList(conn);
			for (String knownWord : knownList) {
				String category = dao.getCategory(conn, knownWord);
				List<FoodCount> wfcl = new ArrayList<FoodCount>();  
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		return null;
	}

	// excption테이블에 있는 필요없는 단어 제거해서 넘겨주기
	public List<String> removeException(List<String> list) {
		List<String> words = new ArrayList<>();
		List<String> exception = exceptionList;

		words.addAll(list);
		words.removeAll(exception);
		System.out.println(words);

		return words;
	}
}
