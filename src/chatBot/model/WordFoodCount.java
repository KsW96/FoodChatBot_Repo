package chatBot.model;

public class WordFoodCount {
	private String word;
	private String food;
	private int count;
	public WordFoodCount(String word, String food, int count) {
		super();
		this.word = word;
		this.food = food;
		this.count = count;
	}
	public String getWord() {
		return word;
	}
	public String getFood() {
		return food;
	}
	public int getCount() {
		return count;
	}
	@Override
	public String toString() {
		return "WordFoodCount [word=" + word + ", food=" + food + ", count=" + count + "]";
	}
	
}
