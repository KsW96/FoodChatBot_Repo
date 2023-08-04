package chatBot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
