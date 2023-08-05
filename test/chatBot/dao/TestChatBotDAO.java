package chatBot.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chatBot.model.WordCategory;
import util.DBUtil;

public class TestChatBotDAO {
	ChatBotDAO dao = new ChatBotDAO();
	Connection conn = null;
	
	@Test
	public void test1() {
		try {
			conn = DBUtil.getConnection();
			List<WordCategory> fcList = new ArrayList<WordCategory>();
			fcList.add(new WordCategory("아기", "person"));
			String foodName = dao.getFoodName(conn, fcList);
			System.out.println("음식추천 : "+foodName);
			assertNotNull(foodName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
	}
	
	@Test
	public void test2() {
		try {
			conn = DBUtil.getConnection();
			List<String> words = new ArrayList<String>();
			words.add("아기");
			List<String> list = dao.unknownWords(words, conn);
			System.out.println(list.toString());
			assertNotNull(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
	}
	
	@Test
	public void test3() {
		try {
			conn = DBUtil.getConnection();
			String knownWord = "아기";
			String category = dao.getCategory(conn, knownWord);
			System.out.println(category);
			assertEquals(category, "person");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
	}
	
	@Test
	public void test4() {
		try {
			conn = DBUtil.getConnection();
			
			// isertWord 테스트
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
	}
}
