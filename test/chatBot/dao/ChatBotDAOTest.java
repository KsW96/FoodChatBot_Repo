package chatBot.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		System.out.println(list);
		
		boolean flag = list.contains("인기");
		System.out.println(flag);
		assertTrue(flag);
	}
	
	@After
	public void close() {
		DBUtil.close(conn);
	}
	
	
}
