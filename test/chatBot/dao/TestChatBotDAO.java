package chatBot.dao;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import chatBot.model.WordCategory;
import chatBot.model.WordFoodCount;
import util.DBUtil;

public class TestChatBotDAO {
	ChatBotDAO dao = new ChatBotDAO();
	Connection conn = null;
	
	@Test
	public void test1() {
		try {
			conn = DBUtil.getConnection();
			List<String> getFood = dao.getFood(conn);
			assertNotNull(getFood);
			
			List<String> getWords = dao.getWords(conn);
			assertNotNull(getWords);
			
			int getFoodCount = dao.getFoodCount(conn);
			assertNotNull(getFoodCount);
			
			int getWordsCount = dao.getWordsCount(conn);
			assertNotNull(getWordsCount);
			
			
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
			// db word에 있는 단어
			String word = "아기";
			WordCategory wc = dao.selectFromWords(word, conn);
			assertNotNull(wc);
			
			List<WordFoodCount> wfcList = dao.selectFromTable(wc, conn);
			assertNotNull(wfcList);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
	}
}
