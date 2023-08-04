package chatBot.service;

import java.util.ArrayList;
import java.util.List;

import chatBot.dao.ChatBotDAO;

public class recommendService {
	ChatBotDAO dao = new ChatBotDAO();
	List<String> exceptionList = null;

	// excption테이블에 있는 필요없는 단어 제거해서 넘겨주기
	public List<String> removeException(List<String> list) {
		List<String> words = new ArrayList<>();
		List<String> exception = exceptionList;

		words.addAll(list);
		words.removeAll(exception);
		System.out.println(words);

		return words;
	}
}
