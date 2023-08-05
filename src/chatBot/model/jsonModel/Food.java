package chatBot.model.jsonModel;

public class Food {
	private String food;

	public Food() {}
	
	public Food(String food) {
		super();
		this.food = food;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	@Override
	public String toString() {
		return "Food [food=" + food + "]";
	}
	
	
	
}
