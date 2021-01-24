package az.nms.pizzamizzaforserver.storage;

import java.util.Date;

/**
 * Created by anar on 5/8/15.
 */
public class Order {

    private int id, status;
    private Date date;
    private Customer customer;
    private double totalPrice;
    private String type, notes;
    private String regid;
    private String lang;

    public Order (int id,Customer customer,double totalPrice, String type, Date date, String notes,String regid,int status, String lang){
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.type = type;
        this.notes = notes;
        this.status = status;
        this.regid = regid;
        this.lang = lang;
    }


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public int getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
