package az.nms.pizzamizza.models;

import java.util.List;

/**
 * Created by Azad on 1/22/15.
 */
public class MyOrders {

    private int id,orderId;
    private double price;
    private String date,type,customerName,customerPhone;
    private List<OrderItems> orders;
    private int status;


    public MyOrders() {

    }

    public MyOrders(int id, int orderId, List<OrderItems> orders, double price, String date) {
        this.id = id;
        this.orderId = orderId;
        this.price = price;
        this.orders = orders;
        this.date = date;

    }



    public MyOrders(int id, int orderId, List<OrderItems> orders, double price, String date,String type,String customerName, String customerPhone, int status) {
        this.id = id;
        this.orderId = orderId;
        this.price = price;
        this.orders = orders;
        this.date = date;
        this.type = type;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.status = status;

    }

    public MyOrders(int orderId, int status){
        this.orderId = orderId;
        this.status = status;

    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public List<OrderItems> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItems> orders) {
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double total) {
        this.price = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return orderId + " - " + type;
    }
}
