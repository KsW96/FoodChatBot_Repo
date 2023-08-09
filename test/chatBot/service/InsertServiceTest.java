package chatBot.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.DBUtil;

public class InsertServiceTest {
	InsertService is = new InsertService();
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
	public void test() {
		String food = "간계밥";
		int result = is.insertFood(conn, food);
		assertNotEquals(-1, result);
		int result2 = deleteFood(conn, food);
		assertNotEquals(-1, result2);
	}
	
	@Test
	public void test2() {
		List<String> list = is.searchAllFood(conn);
		boolean flag = list.isEmpty();
		assertFalse(flag);
		
	}
	
	@After
	public void close() {
		DBUtil.close(conn);
	}
	
	public void deleteFood() {
		
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
