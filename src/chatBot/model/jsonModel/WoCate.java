package chatBot.model.jsonModel;

public class WoCate {
	private String word;
	private String category;

	public WoCate(String word, String category) {
		super();
		this.word = word;
		this.category = category;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "WoCate [word=" + word + ", category=" + category + "]";
	}

}
