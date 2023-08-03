package chatBot.service;

import java.util.List;

import chatBot.dao.ChatBotDAO;
import chatBot.model.WordCategory;
import chatBot.model.WordFoodCount;

public class ChatBotService {
	ChatBotDAO dao = new ChatBotDAO();
	
	// 단어 리스트를 받으면 반복적으로 체크하고 처리하는 메소드
	public void test(List<String> list) {
		
	}
	
	public void test2(String word) {
		WordCategory wc = dao.selectFromWords(word);
		List<WordFoodCount> wfc =  dao.selectWhereBetween(wc);
		
		
	}
}
