package az.nms.pizzamizza.models;

public class OrderItems {

	private Food food;
	private int orderSize;
	private int quantity;
	private double totalPrice;

    private String toppings;
    private int foodId,id;
	private boolean doubleCheese;

	public OrderItems() {

	}

	public OrderItems(Food food, int orderSize, int quantity, double totalPrice, boolean doubleCheese) {
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.food = food;
		this.orderSize = orderSize;
		this.doubleCheese = doubleCheese;
	}


    public OrderItems(int id, int foodId, String toppings, double totalPrice, int orderSize,int quantity, boolean doubleCheese ){
        this.id = id;
        this.foodId = foodId;
        this.toppings = toppings;
        this.totalPrice = totalPrice;
        this.orderSize = orderSize;
        this.quantity = quantity;
		this.doubleCheese = doubleCheese;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public String getToppings() {
        return toppings;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public int getOrderSize() {
		return orderSize;
	}

	public void setOrderSize(int orderSize) {
		this.orderSize = orderSize;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public boolean isDoubleCheese() {
		return doubleCheese;
	}

	public void setDoubleCheese(boolean doubleCheese) {
		this.doubleCheese = doubleCheese;
	}

	@Override
	public boolean equals(final Object o) {
		// TODO Auto-generated method stub
		if (o == null || o == this || !(o instanceof OrderItems))
			return false;

		OrderItems otherOrder = (OrderItems) o;

		if (!otherOrder.getFood().equals(this.food))
			return false;
		if(otherOrder.isDoubleCheese() != this.doubleCheese)
			return false;
		if (otherOrder.getOrderSize() != this.orderSize)
			return false;
		return true;
	}

}
