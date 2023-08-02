package chatBot.service;

import java.util.ArrayList;
import java.util.List;

public class ChatBotAnswerList {
	List<String> list = null;

	public ChatBotAnswerList(List<String> list) {
		this.list = list;
	}
	
	private List<String> listFill(){
		List<String> list = new ArrayList<String>();
		
		list.add("내가 추천하는 음식은 이거야!");
		list.add("이걸 먹어보는 건 어때?");
		list.add("그냥 이거 무조건 먹어라.");
		
		return list;
	}
	
}
