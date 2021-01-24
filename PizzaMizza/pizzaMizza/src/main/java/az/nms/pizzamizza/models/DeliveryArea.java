package az.nms.pizzamizza.models;

/**
 * Created by Azad on 1/8/15.
 */
public class DeliveryArea {

    private String name;
    private int id, minOrder;


    public DeliveryArea() {
    }

    public DeliveryArea(int id, String name, int minOrder) {

        this.id = id;
        this.name = name;
        this.minOrder = minOrder;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
