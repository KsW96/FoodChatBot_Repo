package chatBot.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class TestChatBotDAO {
	ChatBotDAO dao = new ChatBotDAO();
	
	@Test
	public void testSelectByKeyword() {
		String keyword = "따뜻";
		List<String> list = dao.selectFormKeyword(keyword);
		
		assertNotNull(list);
	}
}
