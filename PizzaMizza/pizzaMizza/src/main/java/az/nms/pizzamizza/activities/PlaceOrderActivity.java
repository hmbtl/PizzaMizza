package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.models.OrderList;
import az.nms.pizzamizza.adapters.PlaceOrderListAdapter;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.models.MyOrders;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by anar on 4/21/15.
 */
public class PlaceOrderActivity extends Activity {

    private TextView order_type, order_date, order_id, actionbar_item;

    private ListView orderList;

    private boolean isDelivery;

    private ImageButton actionbar_home;


    private int orderId;
    private String orderDate, orderType, customerName, customerPhone;
    private ImageView callButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_order_layout);


        Utilites.initAppBarFont(this);
        Utilites.hideButton(this, Utilites.ACTION_BAR_HOME);


        //initialize layout
        initLayout();

        Intent intent = getIntent();

        isDelivery = intent.getBooleanExtra(DeliveryAddressActivity.ACTION_DELIVERY, false);


        orderDate = intent.getStringExtra("orderDate");
        orderId = intent.getIntExtra("orderId", 0);
        orderType = isDelivery ? "delivery" : "carry out";
        customerName = intent.getStringExtra("customerName");
        customerPhone = intent.getStringExtra("customerPhone");

        order_date.setText(getResources().getString(R.string.date) + " " + orderDate);
        order_id.setText(getResources().getString(R.string.order) + " #" + String.valueOf(orderId));


        order_type.setText((orderType.equalsIgnoreCase("delivery") ? getResources().getString(R.string.delivery).toLowerCase() : getResources().getString(R.string.carry_out).toLowerCase()));

        actionbar_item.setText(R.string.close);

        actionbar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });

        PlaceOrderListAdapter adapter = new PlaceOrderListAdapter(this, R.layout.placeorder_list_item, OrderList.getOrders());

        orderList.setAdapter(adapter);


        //Clear basket to show new orders

        storeOrderToDatabase();

        callButton = (ImageView) findViewById(R.id.placeorder_call_button);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:*1415"));
                startActivity(callIntent);

            }
        });

    }


    private void storeOrderToDatabase() {

        PizzaMizzaDatabase db = new PizzaMizzaDatabase(this);

        List<OrderItems> orders = new LinkedList<>();

        for (int i = 0; i < OrderList.orders.size(); i++) {

            OrderItems order = OrderList.orders.get(i);

            orders.add(new OrderItems(order.getId(), order.getFood().getId(),
                    Utilites.foodToppingsWithSizeToString(this, order.getFood().getTops()),
                    order.getTotalPrice(), order.getOrderSize(), order.getQuantity(), order.isDoubleCheese()));
        }

        db.addMyOrders(new MyOrders(0, orderId, orders,
                Utilites.getTotalPriceOfOrders(), orderDate, orderType, customerName, customerPhone, 0));

    }


    private void initLayout() {

        order_type = (TextView) findViewById(R.id.placeorder_type);
        order_date = (TextView) findViewById(R.id.placeorder_date);
        order_id = (TextView) findViewById(R.id.placeorder_id);

        orderList = (ListView) findViewById(R.id.placeorder_list);


        actionbar_item = (TextView) findViewById(R.id.appbar_action);
        actionbar_home = (ImageButton) findViewById(R.id.home_button);


    }


    public void goMain() {

        OrderList.orders.clear();
        Intent i = new Intent(PlaceOrderActivity.this, HomeScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        finish();
        startActivity(i);
        // overridePendingTransition(R.anim.slide_in_bottom,
        // R.anim.slide_out_bottom);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        goMain();
    }
}
