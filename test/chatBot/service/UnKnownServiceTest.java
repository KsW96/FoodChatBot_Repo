package chatBot.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chatBot.model.RememberWordList;

public class UnKnownServiceTest {
	UnKnownService us = new UnKnownService();
	
	@Test
	public void test() {
		List<String> test = new ArrayList<>();
		test.add("노인");
		test.add("선지");
		test.add("아기");
		test.add("모름");
		
		String word = us.unknownWord(test);
		assertEquals("모름", word);
		
		List<String> words = RememberWordList.getKnownWordList();
		assertEquals("노인", words.get(0));
		
	}
}
