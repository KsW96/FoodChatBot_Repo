package chatBot.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import chatBot.dao.ChatBotDAO;

public class checkedNewListService {
	ChatBotDAO dao = new ChatBotDAO();

	// 음식들 업데이트가 최신인지 카운트로 체크하고 아니라면 새 리스트로 업데이트
	public void checkFoodList(List<String> list, Connection conn) throws SQLException {
		int foodCount = dao.getFoodCount(conn);
		if (list.size() != foodCount && foodCount != -1) {
			list = dao.getFood(conn);
		}
	}

	// 단어들 업데이트가 최신인지 카운트로 체크하고 아니라면 새 리스트로 업데이트
	public void checkWordList(List<String> list, Connection conn) throws SQLException {
		int wordsCount = dao.getWordsCount(conn);
		if (list.size() != wordsCount && wordsCount != -1) {
			list = dao.getWords(conn);
		}
	}
}
