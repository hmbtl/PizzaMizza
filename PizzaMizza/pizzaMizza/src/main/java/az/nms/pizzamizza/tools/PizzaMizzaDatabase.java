package az.nms.pizzamizza.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.models.DeliveryArea;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.models.FoodTopping;
import az.nms.pizzamizza.models.MyOrderStatus;
import az.nms.pizzamizza.models.MyOrders;
import az.nms.pizzamizza.models.Top;

public class PizzaMizzaDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "Pizza Mizza";

    private static final String TABLE_MYORDERS = "MYORDERS";
    private static final String TABLE_MYORDER_ITEMS = "MYORDERITEMS";

    public PizzaMizzaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        String CREATE_TABLE_FOOD = "CREATE TABLE IF NOT EXISTS food (food_id INTEGER PRIMARY KEY, icon TEXT," +
                " name TEXT, price_small DOUBLE, price_medium DOUBLE, price_large DOUBLE, type TEXT)";

        String CREATE_TABLE_TOP = "CREATE TABLE IF NOT EXISTS top (top_id INTEGER PRIMARY KEY, name_az TEXT, name_en TEXT, name_ru TEXT, editable INTEGER)";

        String CREATE_TABLE_FOOD_TOPPING = "CREATE TABLE IF NOT EXISTS food_topping (food_topping_id INTEGER PRIMARY KEY, food_id INTEGER, top_id INTEGER)";

        String CREATE_TABLE_DELIVERY_AREA = "CREATE TABLE IF NOT EXISTS delivery_area (delivery_area_id INTEGER PRIMARY KEY, name TEXT, min_order INTEGER)";

        String CREATE_MYORDERS_TABLE = "CREATE TABLE IF NOT EXISTS MYORDERS" +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ORDERID INTEGER," +
                " ORDERDATE TEXT, ORDERTYPE TEXT, TOTALPRICE DOUBLE, CUSTOMERNAME TEXT, CUSTOMERPHONE TEXT, STATUS INTEGER)";

        String CREATE_MYORDERITEMS_TABLE = "CREATE TABLE IF NOT EXISTS" +
                " MYORDERITEMS (ID INTEGER PRIMARY KEY AUTOINCREMENT, ORDERID " +
                "INTEGER, FOODID INTEGER, FOODTOPS TEXT, SIZE INTEGER, AMOUNT INTEGER, DOUBLECHEESE INTEGER," +
                " PRICE DOUBLE)";

        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_TOP);
        db.execSQL(CREATE_TABLE_FOOD_TOPPING);
        db.execSQL(CREATE_TABLE_DELIVERY_AREA);
        db.execSQL(CREATE_MYORDERS_TABLE);
        db.execSQL(CREATE_MYORDERITEMS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS food");
        db.execSQL("DROP TABLE IF EXISTS top");
        db.execSQL("DROP TABLE IF EXISTS food_topping");
        db.execSQL("DROP TABLE IF EXISTS delivery_area");
        db.execSQL("DROP TABLE IF EXISTS MYORDERS");
        db.execSQL("DROP TABLE IF EXISTS MYORDERITEMS");
        this.onCreate(db);
    }


    public void addMyOrders(MyOrders myOrder) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("ORDERID", myOrder.getOrderId());
        values.put("ORDERDATE", myOrder.getDate());
        values.put("ORDERTYPE", myOrder.getType());
        values.put("TOTALPRICE", myOrder.getPrice());
        values.put("CUSTOMERNAME", myOrder.getCustomerName());
        values.put("CUSTOMERPHONE", myOrder.getCustomerPhone());
        values.put("STATUS", myOrder.getStatus());

        db.replace(TABLE_MYORDERS, null, values);
        for (int i = 0; i < myOrder.getOrders().size(); i++) {
            OrderItems order = myOrder.getOrders().get(i);

            ContentValues valuesItems = new ContentValues();
            valuesItems.put("ORDERID", myOrder.getOrderId());
            valuesItems.put("FOODID", order.getFoodId());
            valuesItems.put("FOODTOPS", order.getToppings());
            valuesItems.put("SIZE", order.getOrderSize());
            valuesItems.put("AMOUNT", order.getQuantity());
            valuesItems.put("DOUBLECHEESE", order.isDoubleCheese() ? 1 : 0);
            valuesItems.put("PRICE", order.getTotalPrice());

            db.replace(TABLE_MYORDER_ITEMS, null, valuesItems);

        }

        Log.d("MyOrderPlaced", "all settled");

        db.close();

    }


    public List<MyOrderStatus> getMyOrderStatus() {

        List<MyOrderStatus> orderStatus = new LinkedList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT ORDERID, STATUS FROM MYORDERS WHERE STATUS NOT IN ('6','3')";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            do {

                orderStatus.add(new MyOrderStatus(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1))));

            } while (cursor.moveToNext());


        cursor.close();

        db.close();

        return orderStatus;

    }


    public List<MyOrders> getMyOrders() {


        Log.d("Orders ", "started");

        List<MyOrders> myOrders = new LinkedList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM MYORDERS ORDER BY ORDERDATE DESC";

        Cursor cursor = db.rawQuery(query, null);


        if (cursor.moveToFirst())
            do {

                int myOrderId = Integer.parseInt(cursor.getString(0));
                int orderId = Integer.parseInt(cursor.getString(1));
                String orderDate = cursor.getString(2);
                String orderType = cursor.getString(3);
                double orderPrice = Double.parseDouble(cursor.getString(4));
                String customerName = cursor.getString(5);
                String customerPhone = cursor.getString(6);
                int status = cursor.getInt(7);


                Log.d("myorder values", myOrderId + " : " + orderId + " : " + orderDate + " : " + orderType + " : " + " : " + orderPrice);

                query = "SELECT ID, FOODID, FOODTOPS, SIZE, AMOUNT, DOUBLECHEESE, PRICE FROM MYORDERITEMS WHERE ORDERID = '" + orderId + "'";

                Cursor cursorItems = db.rawQuery(query, null);

                List<OrderItems> orders = new LinkedList<>();

                if (cursorItems.moveToFirst())
                    do {
                        int id = cursorItems.getInt(0);
                        int foodId = cursorItems.getInt(1);
                        String foodTop = cursorItems.getString(2);
                        int size = cursorItems.getInt(3);
                        int amount = cursorItems.getInt(4);
                        boolean doubleCheese = cursorItems.getInt(5) == 1;
                        double price = cursorItems.getDouble(6);

                        orders.add(new OrderItems(id, foodId, foodTop, price, size, amount, doubleCheese));
                    } while (cursorItems.moveToNext());

                myOrders.add(new MyOrders(myOrderId, orderId, orders, orderPrice, orderDate, orderType, customerName, customerPhone, status));

                cursorItems.close();


                Log.d(" myorders get ", myOrders.toString());

            } while (cursor.moveToNext());


        cursor.close();


        db.close();


        return myOrders;
    }


    public String updateMyOrder(int orderId, int status) {

        SQLiteDatabase db = getWritableDatabase();

        String type = "";

        String query = "UPDATE MYORDERS SET STATUS = '" + status + "' WHERE ORDERID = '" + orderId + "'";

        Log.d("query", query);

        db.execSQL(query);

        query = "SELECT ORDERTYPE FROM MYORDERS WHERE ORDERID = '" + orderId + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            type = cursor.getString(0);


        cursor.close();
        db.close();
        return type;
    }

    /* ------------FOOD------------*/

    public List<Food> getFoods() {

        SQLiteDatabase db = getReadableDatabase();

        List<Food> foods = new LinkedList<>();

        String query = "SELECT * FROM food";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String icon = cursor.getString(1);
                String name = cursor.getString(2);
                double priceSmall = cursor.getDouble(3);
                double priceMedium = cursor.getDouble(4);
                double priceLarge = cursor.getDouble(5);
                String type = cursor.getString(6);

                foods.add(new Food(id, icon, name, priceSmall, priceMedium, priceLarge, type));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return foods;
    }

    public Food getFood(int foodId) {

        SQLiteDatabase db = getReadableDatabase();

        Food food = new Food();

        String query = "SELECT * FROM food WHERE food_id = '" + foodId + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(0);
            String icon = cursor.getString(1);
            String name = cursor.getString(2);
            double priceSmall = cursor.getDouble(3);
            double priceMedium = cursor.getDouble(4);
            double priceLarge = cursor.getDouble(5);
            String type = cursor.getString(6);

            food = new Food(id, icon, name, priceSmall, priceMedium, priceLarge, type);
        }

        cursor.close();
        db.close();

        return food;
    }

    public List<Food> getFoods(String foodType) {

        SQLiteDatabase db = getReadableDatabase();

        List<Food> foods = new LinkedList<>();

        String query = "SELECT * FROM food WHERE type LIKE'%" + foodType + "' ORDER BY price_medium, price_small, price_large";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String icon = cursor.getString(1);
                String name = cursor.getString(2);
                double priceSmall = cursor.getDouble(3);
                double priceMedium = cursor.getDouble(4);
                double priceLarge = cursor.getDouble(5);
                String type = cursor.getString(6);

                foods.add(new Food(id, icon, name, priceSmall, priceMedium, priceLarge, type));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return foods;
    }


    public void add(Food food) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("food_id", food.getId());
        values.put("icon", food.getImage());
        values.put("name", food.getName());
        values.put("price_small", food.getPriceSmall());
        values.put("price_medium", food.getPriceMedium());
        values.put("price_large", food.getPriceLarge());
        values.put("type", food.getType());

        db.replace("food", null, values);
        db.close();
    }

    /* ------------TOP------------*/

    public List<Top> getTops() {

        SQLiteDatabase db = getReadableDatabase();

        List<Top> tops = new LinkedList<>();

        String query = "SELECT * FROM top";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String name_az = cursor.getString(1);
                String name_en = cursor.getString(2);
                String name_ru = cursor.getString(3);
                int editable = cursor.getInt(4);

                tops.add(new Top(id, name_az, name_en, name_ru, editable));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tops;
    }


    public Top getTop(int topId) {

        SQLiteDatabase db = getReadableDatabase();

        Top top = new Top();

        String query = "SELECT * FROM top WHERE top_id ='" + topId + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(0);
            String name_az = cursor.getString(1);
            String name_en = cursor.getString(2);
            String name_ru = cursor.getString(3);
            int editable = cursor.getInt(4);

            top = new Top(id, name_az, name_en, name_ru, editable);
        }

        cursor.close();
        db.close();

        return top;
    }


    public void add(Top top) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("top_id", top.getId());
        values.put("name_az", top.getNameAz());
        values.put("name_en", top.getNameEn());
        values.put("name_ru", top.getNameRu());
        values.put("editable", top.getEditable());

        db.replace("top", null, values);
        db.close();
    }

     /* ------------FOOD_TOPPING------------*/

    public void add(FoodTopping foodTopping) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("food_topping_id", foodTopping.getId());
        values.put("food_id", foodTopping.getFoodId());
        values.put("top_id", foodTopping.getTopId());

        db.replace("food_topping", null, values);
        db.close();
    }

    public List<Top> getFoodToppings(int foodId) {
        SQLiteDatabase db = getReadableDatabase();

        List<Top> tops = new LinkedList<>();

        String query = "SELECT top.top_id, top.name_az, top.name_en, top.name_ru, top.editable " +
                "FROM top,food_topping WHERE top.top_id = food_topping.top_id AND food_topping.food_id = '" + foodId + "' ORDER BY top.top_id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String name_az = cursor.getString(1);
                String name_en = cursor.getString(2);
                String name_ru = cursor.getString(3);
                int editable = cursor.getInt(4);

                tops.add(new Top(id, name_az, name_en, name_ru, editable));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tops;
    }

    public String getFoodToppingsAsString(int foodId, String language) {
        SQLiteDatabase db = getReadableDatabase();

        String toppings = "";

        String nameLanguage = "name_az";

        if (language.equals("en"))
            nameLanguage = "name_en";
        else if (language.equals("az"))
            nameLanguage = "name_az";
        else if (language.equals("ru"))
            nameLanguage = "name_ru";


        String query = "SELECT GROUP_CONCAT(top." + nameLanguage + ", ', ') FROM food, top, food_topping WHERE " +
                "food.food_id = food_topping.food_id AND top.top_id = food_topping.top_id AND food.food_id = '" + foodId + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            toppings = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return toppings;
    }

    /* ------------DELIVERY_AREA------------*/

    public List<DeliveryArea> getDeliveryAreas() {
        SQLiteDatabase db = getReadableDatabase();

        List<DeliveryArea> deliveryAreas = new LinkedList<>();

        String query = "SELECT * FROM delivery_area";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int minOrder = cursor.getInt(2);

                deliveryAreas.add(new DeliveryArea(id, name, minOrder));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return deliveryAreas;
    }

    public DeliveryArea getDeliveryArea(int deliveryAreaId) {
        SQLiteDatabase db = getReadableDatabase();

        DeliveryArea deliveryArea = new DeliveryArea();

        String query = "SELECT * FROM delivery_area WHERE delivery_area_id = '" + deliveryAreaId + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int minOrder = cursor.getInt(2);

            deliveryArea = new DeliveryArea(id, name, minOrder);

        }
        cursor.close();
        db.close();

        return deliveryArea;
    }

    public void add(DeliveryArea deliveryArea) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("delivery_area_id", deliveryArea.getId());
        values.put("name", deliveryArea.getName());
        values.put("min_order", deliveryArea.getMinOrder());

        db.replace("delivery_area", null, values);

        db.close();
    }

    public int getMinimumOrderOfArea(int deliveryAreaId) {
        SQLiteDatabase db = getReadableDatabase();

        int minimumOrder = 0;

        String query = "SELECT min_order FROM delivery_area WHERE delivery_area_id = '" + deliveryAreaId + "'";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            minimumOrder = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return minimumOrder;
    }


    public List<Food> findFood(String keyword) {
        SQLiteDatabase db = getReadableDatabase();

        List<Food> foods = new LinkedList<>();

        String query;

        if (keyword.isEmpty())
            query = "SELECT * FROM food";
        else
            query = "SELECT food.food_id, food.icon, food.name, food.price_small, food.price_medium, food.price_large, food.type  from food " +
                    "left join food_topping on food_topping.food_id = food.food_id left join top on food_topping.top_id = top.top_id WHERE"+
                    " top.name_az LIKE '%" + keyword + "%' OR top.name_ru LIKE '%" + keyword + "%' OR top.name_en LIKE '%" + keyword +
                    "%' OR food.name LIKE '%" + keyword + "%' OR food.type LIKE '%" + keyword + "%' GROUP BY 1";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String icon = cursor.getString(1);
                String name = cursor.getString(2);
                double priceSmall = cursor.getDouble(3);
                double priceMedium = cursor.getDouble(4);
                double priceLarge = cursor.getDouble(5);
                String type = cursor.getString(6);

                foods.add(new Food(id, icon, name, priceSmall, priceMedium, priceLarge, type));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return foods;
    }


}
