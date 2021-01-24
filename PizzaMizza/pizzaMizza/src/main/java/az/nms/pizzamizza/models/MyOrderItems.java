package az.nms.pizzamizza.models;

/**
 * Created by anar on 4/27/15.
 */
public class MyOrderItems {


    private int id, myOrderId, foodId, amount;
    private String foodTops, size;
    private boolean doubleCheese;

    public MyOrderItems(int id, int myOrderId, int foodId, String foodTops, String size, int amount,boolean doubleCheese ){

        this.id = id;
        this.myOrderId = myOrderId;
        this.foodId = foodId;
        this.foodTops = foodTops;
        this.size = size;
        this.amount = amount;
        this.doubleCheese = doubleCheese;

    }

    public boolean isDoubleCheese() {
        return doubleCheese;
    }

    public void setDoubleCheese(boolean doubleCheese) {
        this.doubleCheese = doubleCheese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMyOrderId() {
        return myOrderId;
    }

    public void setMyOrderId(int myOrderId) {
        this.myOrderId = myOrderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFoodTops() {
        return foodTops;
    }

    public void setFoodTops(String foodTops) {
        this.foodTops = foodTops;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }



/*  var id,myOrderId,foodId,amount: Int!
    var size,foodTops: String!

    init(Id id: Int, MyOrderId myOrderId: Int, FoodId foodId:Int, FoodTops foodTops: String, Size size:String, Amount amount:Int){
        self.id = id
        self.myOrderId = myOrderId
        self.foodId = foodId
        self.amount = amount
        self.size = size
        self.foodTops = foodTops
    }

    */

}
