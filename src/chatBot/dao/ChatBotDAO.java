package chatBot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import chatBot.model.FoodCount;
import chatBot.model.RememberWordList;
import chatBot.model.WordCategory;
import chatBot.model.jsonModel.WoCate;
import util.DBUtil;

public class ChatBotDAO {
	public String getFoodName(Connection conn, List<WordCategory> wcList) throws SQLException {
		List<String> test = RememberWordList.getRefusalList();

		String testStr = "";

		if (test.size() >= 1) {
			testStr += "WHERE food <> '" + test.get(0) + "' ";
			if (test.size() >= 2) {
				for (int i = 1; i < test.size(); i++) {
					testStr += " AND food <> '" + test.get(i) + "' ";
				}
			}
		}

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String plusSQL = "";
			String union = "UNION ALL ";
			for (int i = 0; i < wcList.size(); i++) {
				String tableName = wcList.get(i).getCategory();
				String word = "'" + wcList.get(i).getWord() + "'";
				if (i != 0) {
					plusSQL += union;
				}
				plusSQL += "SELECT food, count FROM " + tableName + " where word = " + word;
			}

			String li = "SELECT food, SUM(count) AS result FROM (" + plusSQL + ") AS combined_table " + testStr
					+ "GROUP BY food order by result DESC LIMIT 1;";

//			String li = "SELECT food, SUM(count) AS result FROM ("+plusSQL+") AS combined_table GROUP BY food order by result DESC LIMIT 1;";
			System.out.println("dao에서 쿼리문 : " + li);
			stmt = conn.prepareStatement(li);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String food = rs.getString("food");
				return food;
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return null;
	}

	// 아는 단어인지 모르는 단어인지 체크하고 모르는 단어만 리스트로써 반환하는 메소드
	// 물어볼 단어만 반환
	public List<String> unknownWords(List<String> words, Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		System.out.println("words리스트 : " + words);

		try {
			for (String word : words) {
				stmt = conn.prepareStatement("SELECT * FROM foodchat.words WHERE word = ?;");
				stmt.setString(1, word);
				rs = stmt.executeQuery();
				if (!rs.next()) {
					list.add(word);
				}
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return list;
	}

	public String getCategory(Connection conn, String knownWord) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FoodCount> list = new ArrayList<>();

		try {
			stmt = conn.prepareStatement("SELECT * FROM foodchat.words where word = ?;");
			stmt.setString(1, knownWord);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String category = rs.getString("category");
				return category;
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return null;
	}

	public List<String> getExceptions(Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();

		try {
			stmt = conn.prepareStatement("SELECT * FROM foodchat.exceptions");
			rs = stmt.executeQuery();
			while (!rs.next()) {
				String word = rs.getString("word");
				list.add(word);
			}
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return list;
	}

	public void insertWord(Connection conn, String word, String category) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("insert into words (word, category) values (?,?);");
			stmt.setString(1, word);
			stmt.setString(2, category);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally

		{
			DBUtil.close(stmt);
		}
	}

	public void insertCategory(Connection conn, String category, String word, String food) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("insert into " + category + " (word, food, count) values (?,?,100);");
//			stmt.setString(1, category);
			stmt.setString(1, word);
			stmt.setString(2, food);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally

		{
			DBUtil.close(stmt);
		}
	}

	public void updateByCount(Connection conn,int count, String category, String word, String food) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("UPDATE "+category+" SET `count` = (`count` + ?) WHERE (`word` = ?) and (`food` = ?);");
			stmt.setInt(1, count);
			stmt.setString(2, word);
			stmt.setString(3, food);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(stmt);
		}
	}

	public boolean searchFood(Connection conn, String food) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM food where food = ?;");
			stmt.setString(1, food);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return false;
	}

	public List<String> searchAllFood(Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();

		try {
			stmt = conn.prepareStatement("SELECT food FROM foodchat.food");
			rs = stmt.executeQuery();
			while (rs.next()) {
				String word = rs.getString("food");
				list.add(word);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return list;
	}

	public List<WoCate> searchAllWord(Connection conn) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<WoCate> list = new ArrayList<>();

		try {
			stmt = conn.prepareStatement("SELECT * FROM foodchat.words;");
			rs = stmt.executeQuery();
			while (rs.next()) {
				String word = rs.getString("word");
				String category = rs.getString("category");
				WoCate wordAndCategory = new WoCate(word, category);
				list.add(wordAndCategory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
		return list;
	}

	public int insertFood(Connection conn, String food) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("insert into food (food) values (?);");
			stmt.setString(1, food);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally

		{
			DBUtil.close(stmt);
		}
		return -1;
	}
}
