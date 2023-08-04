package chatBot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import chatBot.model.WordCategory;
import chatBot.model.WordFoodCount;
import util.DBUtil;

public class ChatBotDAO {
	// 아는 단어인지 모르는 단어인지 체크하고 모르는 단어만 리스트로써 반환하는 메소드
	// 물어볼 단어만 반환
	public List<String> unknownWords(List<String> words, Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();

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

//	// 음식 리스트를 반환하는 리스트
//	public List<String> getFood(Connection conn) throws SQLException {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		List<String> list = null;
//		try {
//			stmt = conn.prepareStatement("SELECT * FROM food;");
//			rs = stmt.executeQuery();
//			list = new ArrayList<String>();
//			while (rs.next()) {
//				String food = rs.getString("food");
//				list.add(food);
//			}
//		} finally {
//			DBUtil.close(rs);
//			DBUtil.close(stmt);
//		}
//		return list;
//	}
//
//	// 단어 리스트를 반환하는 메소드
//	public List<String> getWords(Connection conn) throws SQLException {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		List<String> list = null;
//		try {
//			stmt = conn.prepareStatement("SELECT * FROM words;");
//			rs = stmt.executeQuery();
//			list = new ArrayList<String>();
//			while (rs.next()) {
//				String word = rs.getString("word");
//				list.add(word);
//			}
//		} finally {
//			DBUtil.close(rs);
//			DBUtil.close(stmt);
//		}
//		return list;
//	}
//
//	// 음식의 총 수량 체크하는 메소드
//	public int getFoodCount(Connection conn) throws SQLException {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement("SELECT count(*) FROM food;");
//			rs = stmt.executeQuery();
//			if (rs.next()) {
//				int count = rs.getInt("count(*)");
//				return count;
//			}
//		} finally {
//			DBUtil.close(rs);
//			DBUtil.close(stmt);
//		}
//		return -1;
//	}
//
//	// 단어의 총 수량 체크하는 메소드
//	public int getWordsCount(Connection conn) throws SQLException {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement("SELECT count(*) FROM words;");
//			rs = stmt.executeQuery();
//			if (rs.next()) {
//				int count = rs.getInt("count(*)");
//				return count;
//			}
//		} finally {
//			DBUtil.close(rs);
//			DBUtil.close(stmt);
//		}
//		return -1;
//	}
//
//	// ai가 알고있는 단어인지 word table에서 체크하는 테이블
//	public WordCategory selectFromWords(String word, Connection conn) throws SQLException {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.prepareStatement("SELECT * FROM words WHERE word = ?;");
//			stmt.setString(1, word);
//			rs = stmt.executeQuery();
//			if (rs.next()) {
//				String category = rs.getNString("category");
//				WordCategory wc = new WordCategory(word, category);
//				return wc;
//			}
//		} finally {
//			DBUtil.close(rs);
//			DBUtil.close(stmt);
//		}
//		return null;
//	}
//
//	// null이 아닌 객체를 받으면 다음 음식들을 체크
//	public List<WordFoodCount> selectFromTable(WordCategory wc, Connection conn) throws SQLException {
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		List<WordFoodCount> list = null;
//		try {
//			stmt = conn.prepareStatement("SELECT * FROM " + wc.getCategory() + " WHERE word = ?");
//			String wcWord = wc.getWord();
//			stmt.setString(1, wcWord);
//			rs = stmt.executeQuery();
//			list = new ArrayList<WordFoodCount>();
//			while (rs.next()) {
//				String food = rs.getString("food");
//				int count = rs.getInt("count");
//				WordFoodCount wfc = new WordFoodCount(wcWord, food, count);
//				list.add(wfc);
//			}
//		} finally {
//			DBUtil.close(rs);
//			DBUtil.close(stmt);
//		}
//		return list;
//	}
}
