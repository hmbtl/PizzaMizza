package az.nms.pizzamizzaforserver.storage;

import java.util.Date;

/**
 * Created by anar on 5/8/15.
 */
public class Customer {

    private int id;
    private String name, address,location,phone, email;
    private Date lastOrder;
    private double totalOrders;


    public Customer(int id,String name,String address,String phone,String location,String email,double totalOrders,Date lastOrder){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.email = email;
        this.lastOrder = lastOrder;
        this.totalOrders = totalOrders;
    }

    public Customer(int id,String name,String address,String phone,Date lastOrder){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.lastOrder = lastOrder;
    }


    public double getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(double totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(Date lastOrder) {
        this.lastOrder = lastOrder;
    }
}
