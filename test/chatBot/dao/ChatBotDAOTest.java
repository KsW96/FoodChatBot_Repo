package chatBot.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	@Test
	public void searchNegativeWord() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		List<String> result = new ArrayList<>();

		list.add("따뜻");
		list.add("안");
		list.add("시원");

		try {
			for (String s : list) {
				stmt = conn.prepareStatement("Select * from negative where word = ?");
				stmt.setString(1, s);
				rs = stmt.executeQuery();
				
				if (rs.next()) {
					result.clear();
				} else {
					result.add(s);
				}
			}
			System.out.println(result);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally

		{
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
	}

	@After
	public void close() {
		DBUtil.close(conn);
	}

}
