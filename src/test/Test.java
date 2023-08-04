package test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import chatBot.dao.ChatBotDAO;
import chatBot.model.WordCategory;
import chatBot.model.WordFoodCount;
import chatBot.service.recommendService;
import util.DBUtil;

public class Test {
	public static void main(String[] args) {
		자연어 처리 ㅈ = new 자연어처리();
		
		
		recommendService rs = new recommendService();
		List<String> wordList = new ArrayList<String>();
		System.out.println(rs.getFoodList());
		wordList.add("아기");
		String food = rs.recommendFood(wordList);
		System.out.println(food);
		
	}
}


