package chatBot.model;

public class WordCategory {
	private String word;
	private String category;
	public WordCategory(String word, String category) {
		super();
		this.word = word;
		this.category = category;
	}
	@Override
	public String toString() {
		return "WordCategory [word=" + word + ", category=" + category + "]";
	}
	public String getWord() {
		return word;
	}
	public String getCategory() {
		return category;
	}
}
