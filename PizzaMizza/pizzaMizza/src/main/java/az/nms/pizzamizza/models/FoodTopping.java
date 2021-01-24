package az.nms.pizzamizza.models;

public class FoodTopping {

	private int id, foodId, topId;

	public FoodTopping() {

	}

	public FoodTopping(int id, int foodId, int topId){
		this.id = id;
		this.topId = topId;
		this.foodId = foodId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getTopId() {
		return topId;
	}

	public void setTopId(int topId) {
		this.topId = topId;
	}
}
