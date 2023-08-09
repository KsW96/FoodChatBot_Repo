package chatBot.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chatBot.model.WordCategory;
import util.DBUtil;

public class ChatBotDAOTest {
	ChatBotDAO dao = new ChatBotDAO();
	Connection conn = null;
	
	@Before
	public void getConnection() {
		DBUtil.forTest();
		try {
			conn = DBUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test() throws SQLException {
		List<WordCategory> wcList = new ArrayList<>();
		wcList.add(new WordCategory("아기", "person"));
		wcList.add(new WordCategory("노인", "person"));
		String food = dao.getFoodName(conn, wcList);
		assertEquals("해장국", food);
	}
	@Test
	public void test1() throws SQLException {
		List<String> words = new ArrayList<>();
		words.add("노인");
		words.add("선지");
		words.add("화창");
		words.add("모름");
		List<String> unknownWords = dao.unknownWords(words, conn);
		List<String> checkList = new ArrayList<String>();
		checkList.add("모름");
		assertArrayEquals(unknownWords.toArray(), checkList.toArray());
	}
	@Test
	public void test2() throws SQLException {
		String category = dao.getCategory(conn, "아기");
		assertEquals("person", category);
	}
	@Test
	public void test3() throws SQLException {
		List<String> list = dao.getExceptions(conn);
		boolean flag = list.contains("인기");
		assertTrue(flag);
	}
	@Test
	public void test4() {
		String word = "김영곤";
		String category = "person";
		
		int result = dao.insertWord(conn, word, category);
		assertNotEquals(-1, result);
		int result2 = deleteWord(conn, word);
		assertNotEquals(-1, result2	);
	}
	@Test
	public void test5() {
		String food = "갈비";
		boolean flag = dao.searchFood(conn, food);
		assertTrue(flag);
	}
	@Test
	public void test6() {
		List<String> list = dao.searchAllFood(conn);
		boolean flag = list.contains("갈비탕");
		assertTrue(flag);
	}
	@Test
	public void test7() {
		String food = "미역국";
		int result = dao.insertFood(conn, food);
		assertNotEquals(-1, result);
		int result2 = deleteFood(conn, food);
		assertNotEquals(-1, result2);
	}
	
	@After
	public void close() {
		DBUtil.close(conn);
	}
	
	public int deleteWord(Connection conn, String word) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("DELETE FROM `foodchattest`.`words` WHERE (`word` = ?);");
			stmt.setString(1, word);
			int result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally

		{
			DBUtil.close(stmt);
		}
		return -1;
	}
	
	public int deleteFood(Connection conn, String word) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("DELETE FROM `foodchattest`.`food` WHERE (`food` = ?);");
			stmt.setString(1, word);
			int result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally

		{
			DBUtil.close(stmt);
		}
		return -1;
	}
	
	
}
