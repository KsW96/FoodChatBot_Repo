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
	// 
	
	// ai가 알고있는 단어인지 word table에서 체크하는 테이블
	public WordCategory selectFromWords(String word) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM words WHERE word = ?;");
			stmt.setString(1, word);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String category = rs.getNString("category");
				WordCategory wc = new WordCategory(word, category);
				return wc;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
		return null;
	}

	// null이 아닌 객체를 받으면 다음 음식들을 체크
	public List<WordFoodCount> selectWhereBetween(WordCategory wc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<WordFoodCount> list = null;
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM person WHERE word = ?");
			String wcWord = wc.getWord();
			stmt.setString(1, wcWord);
			rs = stmt.executeQuery();
			list = new ArrayList<WordFoodCount>();
			while (rs.next()) {
				String food = rs.getString("food");
				int count = rs.getInt("count");
				WordFoodCount wfc = new WordFoodCount(wcWord, food, count);
				list.add(wfc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(stmt);
			DBUtil.close(conn);
		}
		return list;
	}
}
