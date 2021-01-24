package az.nms.pizzamizzaforserver.storage;

/**
 * Created by anar on 5/12/15.
 */
public class OrderItem {



    private int order_item_id,food_quantity,food_id ;
    private String food_icon , food_name , food_toppings , food_size ;
    private double food_total;
    private boolean double_cheese;
    
    public OrderItem(int order_item_id, int food_id, String food_icon, String food_name, String food_toppings, String food_size, int food_quantity, Double food_total, boolean double_cheese){
        this.order_item_id = order_item_id;
        this.food_icon = food_icon;
        this.food_id = food_id;
        this.food_name = food_name;
        this.food_quantity = food_quantity;
        this.food_size = food_size;
        this.food_toppings = food_toppings;
        this.food_total = food_total;
        this.double_cheese = double_cheese;
    }


    public boolean isDouble_cheese() {
        return double_cheese;
    }

    public void setDouble_cheese(boolean double_cheese) {
        this.double_cheese = double_cheese;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(int food_quantity) {
        this.food_quantity = food_quantity;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_icon() {
        return food_icon;
    }

    public void setFood_icon(String food_icon) {
        this.food_icon = food_icon;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_toppings() {
        return food_toppings;
    }

    public void setFood_toppings(String food_toppings) {
        this.food_toppings = food_toppings;
    }

    public String getFood_size() {
        return food_size;
    }

    public void setFood_size(String food_size) {
        this.food_size = food_size;
    }

    public double getFood_total() {
        return food_total;
    }

    public void setFood_total(double food_total) {
        this.food_total = food_total;
    }
}
