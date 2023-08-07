package chatBot.service;

import java.sql.Connection;

import chatBot.dao.ChatBotDAO;

public class InsertService {
	ChatBotDAO dao = new ChatBotDAO();

	public void insert(Connection conn, String word, String category) {
		dao.insertWord(conn, word, category);
	}

	public void insertCategory(Connection conn, String category, String word, String food) {
		dao.insertCategory(conn, category, word, food);
	}
}
