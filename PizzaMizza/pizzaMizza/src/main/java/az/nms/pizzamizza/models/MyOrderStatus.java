package az.nms.pizzamizza.models;

/**
 * Created by anar on 5/13/15.
 */
public class MyOrderStatus {

    private int orderId, status;

    public MyOrderStatus(int orderId, int status){
        this.orderId = orderId;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
