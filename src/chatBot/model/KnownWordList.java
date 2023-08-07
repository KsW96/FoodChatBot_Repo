package chatBot.model;

import java.util.ArrayList;
import java.util.List;

public class KnownWordList {
	private static List<String> knownWordList = new ArrayList<String>();

	public static List<String> getKnownWordList() {
		return knownWordList;
	}

	public static void setKnownWordList(List<String> knownWordLists) {
		knownWordList = knownWordLists;
	}

	
}
