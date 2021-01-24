package az.nms.pizzamizzaforserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizzaforserver.Utitilies.Settings;
import az.nms.pizzamizzaforserver.storage.Order;
import az.nms.pizzamizzaforserver.storage.PizzaServerDatabase;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Order> orders = new LinkedList<>();
    private OrderUpdateReceiver orderUpdateReceiver;
    private  OrderListAdapter orderListAdapter;
    private RecyclerView orderList;
    private PizzaServerDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Settings.init(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        
        db = new PizzaServerDatabase(this);

        setupOrders();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(orderUpdateReceiver);
    }

    private void setupOrders(){


        orderList = (RecyclerView)findViewById(R.id.orderList);
        orderList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        orderList.setLayoutManager(llm);


        orders = db.getAllOrders();

        orderListAdapter = new OrderListAdapter(MainActivity.this, orders);
        orderList.setAdapter(orderListAdapter);

        orderList.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        Order order = orders.get(position);

                        Intent intent = new Intent(MainActivity.this, OrderDetailActivity.class);
                        intent.putExtra("ORDER_ID",order.getId());
                        intent.putExtra("CUSTOMER_NAME",order.getCustomer().getName());
                        intent.putExtra("CUSTOMER_ADDRESS",order.getCustomer().getAddress());
                        intent.putExtra("CUSTOMER_PHONE",order.getCustomer().getPhone());
                        intent.putExtra("ORDER_TYPE",order.getType());
                        intent.putExtra("ORDER_DATE",order.getDate());
                        intent.putExtra("ORDER_TOTAL",order.getTotalPrice());
                        intent.putExtra("ORDER_NOTES",order.getNotes());
                        intent.putExtra("ORDER_REGID",order.getRegid());
                        intent.putExtra("ORDER_STATUS",order.getStatus());
                        startActivity(intent);

                        finish();

                    }
                })
        );

        orderListAdapter.notifyDataSetChanged();

        //instantiate receiver class for finding out when new updates are available

        orderUpdateReceiver = new OrderUpdateReceiver();


        //register for updates
        registerReceiver(orderUpdateReceiver, new IntentFilter("ORDER_UPDATES"));

        //start the service for updates now
        this.getApplicationContext().startService(new Intent(this.getApplicationContext(), OrderListenerService.class));

    }


    class OrderUpdateReceiver extends BroadcastReceiver
    {
        /**
         * When new updates are available, a broadcast is received here
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            //delete db rows
            
            orders = db.getAllOrders();
            
            orderListAdapter = new OrderListAdapter(MainActivity.this, orders);
            orderList.setAdapter(orderListAdapter);

            orderListAdapter.notifyDataSetChanged();

        }
    }


}
