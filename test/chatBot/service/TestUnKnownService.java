package chatBot.service;

import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chatBot.dao.ChatBotDAO;
import chatBot.model.RememberWordList;
import util.DBUtil;

public class TestUnKnownService {
	RememberWordList knownList = new RememberWordList();

	public String unknownWord(List<String> wordList) {
		ChatBotDAO dao = new ChatBotDAO();

		Connection conn = null;
		try {
			conn = DBUtil.getConnection();

			List<String> unKnownList = dao.unknownWords(wordList, conn);
			wordList.removeAll(unKnownList);
			knownList.setKnownWordList(wordList);
			if (unKnownList.size() > 0) {
				return unKnownList.get(0);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(conn);
		}
		return null;
	}
	
	@Test
	public void test1() {
		List<String> wordList = new ArrayList<String>();
		wordList.add("아기");
		String unknownWord = unknownWord(wordList);
		assertNull(unknownWord);
		
	}
}
