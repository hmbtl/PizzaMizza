package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.models.MyOrders;
import az.nms.pizzamizza.adapters.MyOrdersListAdapter;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Azad on 1/22/15.
 */
public class MyOrdersActivity extends Activity {

    private ExpandableStickyListHeadersListView ordersList;
    private MyOrdersListAdapter adapter;

    private List<MyOrders> orders = new LinkedList<MyOrders>();

    private PizzaMizzaDatabase db;

    private ImageButton home;

    private OrderUpdateReceiver orderUpdateReceiver;

    private LinearLayout noOrdersText;

    private Button getStarted;

    private TextView remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorders_layout);

        Utilites.initAppBarFont(this);
        Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);

        db = new PizzaMizzaDatabase(this);

        initLayout();

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(MyOrdersActivity.this, MenuScreenActivity.class);
                startActivity(i);
                finish();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        orderUpdateReceiver = new OrderUpdateReceiver();

        registerReceiver(orderUpdateReceiver, new IntentFilter("ORDER_UPDATES"));

    }

    @Override
    protected void onResume() {
        super.onResume();

        orders = db.getMyOrders();



        if (orders.isEmpty() || orders == null) {
            getStarted.setVisibility(View.VISIBLE);
            noOrdersText.setVisibility(View.VISIBLE);
            ordersList.setVisibility(View.GONE);
        } else {
            noOrdersText.setVisibility(View.GONE);
            ordersList.setVisibility(View.VISIBLE);
            getStarted.setVisibility(View.GONE);
        }

        adapter = new MyOrdersListAdapter(this, R.layout.myorder_list, orders);
        ordersList.setAdapter(adapter);




        adapter.notifyDataSetChanged();

        ordersList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(ordersList.isHeaderCollapsed(headerId)){
                    ordersList.expand(headerId);
                    header.findViewById(R.id.myorders_header_shadow).setVisibility(View.GONE);
                }else {
                    ordersList.collapse(headerId);
                    header.findViewById(R.id.myorders_header_shadow).setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initLayout() {
        getStarted = (Button) findViewById(R.id.myorders_getstarted);
        ordersList = (ExpandableStickyListHeadersListView) findViewById(R.id.myorders_list);
        noOrdersText = (LinearLayout) findViewById(R.id.myorders_empty);
        remove = (TextView) findViewById(R.id.appbar_action);
        home = (ImageButton) findViewById(R.id.home_button);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(orderUpdateReceiver);
    }

    class OrderUpdateReceiver extends BroadcastReceiver
    {
        /**
         * When new updates are available, a broadcast is received here
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            //delete db rows

            orders = db.getMyOrders();

            adapter = new MyOrdersListAdapter(MyOrdersActivity.this, R.layout.myorder_list, orders);
            ordersList.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
