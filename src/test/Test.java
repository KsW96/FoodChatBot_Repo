package test;

import java.util.List;

import chatBot.dao.ChatBotDAO;

public class Test {
	public static void main(String[] args) {
		ChatBotDAO dao = new ChatBotDAO();
		List<String> list = dao.selectFormKeyword("따뜻");
		
		System.out.println(list);
		
	}
}
