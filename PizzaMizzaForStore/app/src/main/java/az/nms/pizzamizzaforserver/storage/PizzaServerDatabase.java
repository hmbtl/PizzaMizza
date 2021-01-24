package az.nms.pizzamizzaforserver.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizzaforserver.Utitilies.Constants;

/**
 * Created by anar on 5/17/15.
 */
public class PizzaServerDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "PizzaMizzaServer";

    public PizzaServerDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS orders (order_id INTEGER PRIMARY KEY, date_added DATE, total DOUBLE" +
                ", type TEXT, customer_id INTEGER, name TEXT, phone TEXT, address TEXT, notes TEXT, regid TEXT, status INTEGER, lang TEXT)";

        db.execSQL(CREATE_ORDER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Order> getOrders(int order_status){

        List<Order> orders = new LinkedList<>();

        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT * FROM ORDERS WHERE status = '"+ order_status +"' ORDER BY order_id DESC";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            do{

                int order_id  = cursor.getInt(0);
                String date = cursor.getString(1);


                Date date_added = null;
                try {
                    date_added = Constants.formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Double total = cursor.getDouble(2);

                String type = cursor.getString(3);

                int customer_id = cursor.getInt(4);

                String name = cursor.getString(5);
                String phone = cursor.getString(6);

                String address = cursor.getString(7);
                String notes = cursor.getString(8);
                String regid = cursor.getString(9);
                String lang = cursor.getString(10);

                int status = cursor.getInt(10);

                Customer customer = new Customer(customer_id, name, address, phone, date_added);

                Order order = new Order(order_id, customer, total, type, date_added, notes, regid, status,lang);

                orders.add(order);

            } while(cursor.moveToNext());

        }

        cursor.close();

        db.close();

        return orders;
    }


    public List<Order> getOrders(int order_status1, int order_status2){

        List<Order> orders = new LinkedList<>();

        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT * FROM ORDERS WHERE status = '"+ order_status1 +"' OR status = '" + order_status2 + "' ORDER BY order_id DESC";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            do{

                int order_id  = cursor.getInt(0);
                String date = cursor.getString(1);


                Date date_added = null;
                try {
                    date_added = Constants.formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Double total = cursor.getDouble(2);

                String type = cursor.getString(3);

                int customer_id = cursor.getInt(4);

                String name = cursor.getString(5);
                String phone = cursor.getString(6);

                String address = cursor.getString(7);
                String notes = cursor.getString(8);
                String regid = cursor.getString(9);
                String lang = cursor.getString(10);

                int status = cursor.getInt(10);

                Customer customer = new Customer(customer_id, name, address, phone, date_added);

                Order order = new Order(order_id, customer, total, type, date_added, notes, regid, status,lang);

                orders.add(order);

            } while(cursor.moveToNext());

        }

        cursor.close();

        db.close();

        return orders;
    }

    public int getOrderStatus (int order_id){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT status FROM orders WHERE order_id = '"+ order_id +"'";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            return cursor.getInt(0);
        }

        cursor.close();

        db.close();
        return 0;
    }


    public List<Order> getAllOrders(){

        List<Order> orders = new LinkedList<>();

        SQLiteDatabase db = getReadableDatabase();


        String query = "SELECT * FROM ORDERS ORDER BY order_id DESC";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            do{

                int order_id  = cursor.getInt(0);
                String date = cursor.getString(1);


                Date date_added = null;
                try {
                    date_added = Constants.formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Double total = cursor.getDouble(2);

                String type = cursor.getString(3);

                int customer_id = cursor.getInt(4);

                String name = cursor.getString(5);
                String phone = cursor.getString(6);

                String address = cursor.getString(7);
                String notes = cursor.getString(8);
                String regid = cursor.getString(9);
                String lang = cursor.getString(10);

                int status = cursor.getInt(10);

                Customer customer = new Customer(customer_id, name, address, phone, date_added);

                Order order = new Order(order_id, customer, total, type, date_added, notes, regid, status,lang);

                orders.add(order);

            } while(cursor.moveToNext());

        }

        cursor.close();

        db.close();

        return orders;
    }


    public void addOrder(Order order){

        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put("order_id",order.getId());
        values.put("date_added",Constants.formatter.format(order.getDate()));
        values.put("total",order.getTotalPrice());
        values.put("type",order.getType());

        Customer customer = order.getCustomer();

        values.put("customer_id",customer.getId());
        values.put("name",customer.getName());
        values.put("phone",customer.getPhone());
        values.put("address",customer.getAddress());
        values.put("notes",order.getNotes());
        values.put("regid",order.getRegid());
        values.put("status",order.getStatus());
        values.put("lang",order.getLang());


        db.replace("ORDERS", null, values);


        db.close();


    }


    public void deleteOrders(){

        SQLiteDatabase db = getWritableDatabase();

        String query = "DELETE FROM orders";

        db.execSQL(query);

        db.close();

    }

    public void deleteOrder(int order_id){

        SQLiteDatabase db = getWritableDatabase();

        String query = "DELETE FROM orders WHERE order_id = '" + order_id + "'";

        db.execSQL(query);

        db.close();

    }

    public void updateOrder(int order_id, int status){

        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE orders SET status = '" + status + "' WHERE order_id = '" + order_id + "'";

        db.execSQL(query);

        db.close();

    }




    public int orderCount(int status){

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT COUNT(*) FROM orders WHERE status = '" + status + "'";

        Cursor cursor = db.rawQuery(query, null);

        int count = 0;

        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }


    public int orderCount(){

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT COUNT(*) FROM orders";

        Cursor cursor = db.rawQuery(query, null);

        int count = 0;

        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    public int orderCount(int status1, int status2){

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT COUNT(*) FROM orders WHERE status = '" + status1 + "' OR status = '" + status2 + "'";

        Cursor cursor = db.rawQuery(query,null);

        int count = 0;

        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }


}
