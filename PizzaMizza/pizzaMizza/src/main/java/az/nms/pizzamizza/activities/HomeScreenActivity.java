package az.nms.pizzamizza.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.adapters.DrawerAdapter;
import az.nms.pizzamizza.models.DrawerItems;
import az.nms.pizzamizza.adapters.OrderAdapter;
import az.nms.pizzamizza.models.OrderList;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.JSONParser;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreenActivity extends ActionBarActivity {

    public static LinearLayout menu, coupons, basket_empty, checkout_button;

    private static final double MIN_ORDER = 2.0;
    private static final String NO_ORDERS = " 0.00 AZN";

    private LinearLayout menuBarBackground;
    private ListView checkout_list;
    public static TextView remove_order, total_price;
    private SlidingMenu sm;
    public static ListView mDrawerList;
    public PizzaMizzaDatabase db;
    public static OrderAdapter adapter;
    private ImageButton home;
    private boolean isDelivery;
    private double minOrder;
    private static double totalPrice;


    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen_layout);

        // Initialise the font of the custom action bar title
        Utilites.initAppBarFont(this);

        // Initialise the database
        db = new PizzaMizzaDatabase(this);

        // Define whether it is delivery or carryout
        isDelivery = getIntent().getBooleanExtra(DeliveryAddressActivity.ACTION_DELIVERY, false);

        if (isDelivery)
            minOrder = db.getMinimumOrderOfArea(SharedData.getZoneId());
        else
            minOrder = MIN_ORDER;

        //Initialise preferences of left behind drawer
        sm = new SlidingMenu(this);
        sm.setMode(SlidingMenu.LEFT);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setMenu(R.layout.nav_drawer_left);

        initialiseLayout();

    }


    private void initialiseLayout() {

        menuBarBackground = (LinearLayout) findViewById(R.id.menu_coupon_bar_bg);
        basket_empty = (LinearLayout) findViewById(R.id.basket_empty);

        home = (ImageButton) findViewById(R.id.home_button);
        home.setImageResource(R.drawable.navdrawer);
        home.setOnClickListener(onClick);

        // Define the action button of the app bar
        remove_order = (TextView) findViewById(R.id.appbar_action);
        remove_order.setVisibility(View.GONE);
        remove_order.setText(R.string.edit);
        remove_order.setOnClickListener(onClick);


        checkout_button = (LinearLayout) findViewById(R.id.home_price_lr);
        checkout_button.setOnClickListener(onClick);

        menu = (LinearLayout) findViewById(R.id.menu_click);
        menu.setOnClickListener(onClick);
        menu.setOnTouchListener(onTouch);

        coupons = (LinearLayout) findViewById(R.id.coupons_click);
        coupons.setOnTouchListener(onTouch);

        //If currently there is no selected item in the basket then hide "edit" button
        if (!OrderList.getOrders().isEmpty())
            basket_empty.setVisibility(View.GONE);

        DrawerItems items_data[] = new DrawerItems[]{
                new DrawerItems(R.drawable.orders, R.string.my_orders),
                new DrawerItems(R.drawable.search, R.string.search),
                new DrawerItems(R.drawable.about, R.string.about)};

        DrawerAdapter adapter_drawer = new DrawerAdapter(this,
                R.layout.drawer_list_item, items_data);


        mDrawerList = (ListView) sm.findViewById(R.id.leftlist);
        mDrawerList.setAdapter(adapter_drawer);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if(SharedData.getStoreOpen() == 0)
            checkout_button.setEnabled(false);
    }


    public static void updatePrice() {
        if (OrderList.orders.isEmpty()) {
            total_price.setText(NO_ORDERS);
            setTotalPrice(0.0);
        } else {
            double total = 0.0;
            for (int i = 0; i < OrderList.orders.size(); i++) {
                total += OrderList.orders.get(i).getTotalPrice();
            }
            setTotalPrice(Utilites.round(total, 2));
            total_price.setText(String.valueOf(getTotalPrice())
                    + " AZN");
        }
    }

    public static double getTotalPrice() {
        return HomeScreenActivity.totalPrice;
    }

    public static void setTotalPrice(double totalPrice) {
        HomeScreenActivity.totalPrice = totalPrice;

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        new CheckStoreIsOpen().execute();

        if (checkout_list == null) {
            checkout_list = (ListView) findViewById(R.id.checkout_list);
            adapter = new OrderAdapter(this, R.layout.checkout_list_item,
                    OrderList.getOrders());


            total_price = (TextView) findViewById(R.id.home_price);

            checkout_list.setAdapter(adapter);
        } else if (OrderList.getOrders().isEmpty() || OrderList.getOrders() == null)
            disableAppBarEdit();

        updateAdapter();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(this, MainPageActivity.class);
        startActivity(i);
        finish();
    }

    public static void updateAdapter() {

        if (!OrderList.getOrders().isEmpty())
            remove_order.setVisibility(View.VISIBLE);

        Log.d("Items", OrderList.getOrders().toString());
        adapter.notifyDataSetChanged();
        if (OrderList.orders.isEmpty())
            basket_empty.setVisibility(View.VISIBLE);
        updatePrice();
    }


    View.OnClickListener onClick = new View.OnClickListener() {
        public void onClick(View v) {

            Intent i;
            // it was the 2nd button
            switch (v.getId()) {
                case R.id.menu_click:
                    disableAppBarEdit();
                    i = new Intent(HomeScreenActivity.this, MenuScreenActivity.class);
                    startActivity(i);

                    break;

                case R.id.home_button:

                    sm.toggle();
                    break;
                case R.id.appbar_action:
                    toggleAppBarEdit();
                    break;

                case R.id.home_price_lr:


                    if (getTotalPrice() >= minOrder) {
                        i = new Intent(HomeScreenActivity.this,
                                CheckoutActivity.class);
                        i.putExtra(DeliveryAddressActivity.ACTION_DELIVERY, isDelivery);
                        startActivity(i);
                    } else {


                        Utilites.messageDialog(HomeScreenActivity.this, getResources().getText(R.string.minOrder_toast).toString() +
                                " " + minOrder + " AZN", getResources().getString(R.string.minorder), getResources().getString(R.string.ok)).show();
                    }
                    break;
                default:
                    break;
            }


        }
    };


    View.OnTouchListener onTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (v.getId()) {

                case R.id.coupons_click:

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        menuBarBackground.setBackgroundResource(R.drawable.appbar_green);
                    } else {
                        menuBarBackground.setBackgroundResource(R.drawable.appbar_green_right_selected);
                    }
                    break;
                case R.id.menu_click:
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        menuBarBackground.setBackgroundResource(R.drawable.appbar_green);
                    } else {
                        menuBarBackground.setBackgroundResource(R.drawable.appbar_green_left_selected);
                    }

                    break;

            }

            return false;
        }
    };


    public void toggleAppBarEdit() {
        if (remove_order.getText().toString().equalsIgnoreCase(getResources().getString(R.string.edit)))
            enableAppBarEdit();
        else
            disableAppBarEdit();

    }

    public void disableAppBarEdit() {

        remove_order.setText(R.string.edit);

        if (!OrderList.getOrders().isEmpty()) {
            adapter.setEditable(false);

            remove_order.setVisibility(View.VISIBLE);
        } else
            remove_order.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();

    }

    public void enableAppBarEdit() {
        remove_order.setText(R.string.done);

        if (!OrderList.getOrders().isEmpty()) {
            adapter.setEditable(true);
        }
        adapter.notifyDataSetChanged();

    }


    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            selectItem(position);
            Intent i;
            switch (position) {
                case 1:
                    i = new Intent(HomeScreenActivity.this, SearchActivity.class);
                    startActivity(i);
                    break;
                case 0:
                    i = new Intent(HomeScreenActivity.this, MyOrdersActivity.class);
                    startActivity(i);
                    break;
                case 2:
                    i = new Intent(HomeScreenActivity.this, AboutActivity.class);
                    startActivity(i);
                    break;
            }

            sm.toggle();


        }
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);

    }

    class CheckStoreIsOpen extends AsyncTask<String, String, Integer> {

        private JSONParser jParser = new JSONParser();

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                List<NameValuePair> params = new ArrayList<>();

                JSONObject json = jParser.makeHttpRequest(Constants.STORE_OPEN, "GET", params);

                if (json != null) {

                    Log.d("store open",json.toString());

                    return json.getInt("isopen");

                } else
                    return -1;


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 1 && SharedData.getStoreOpen() != integer){
                SharedData.setStoreOpen(integer);
                onCreateDialog(R.string.we_are_open, R.string.store_open).show();
                checkout_button.setEnabled(true);
            }

            if (integer == 0 && SharedData.getStoreOpen() != integer){
                SharedData.setStoreOpen(integer);
                onCreateDialog(R.string.we_are_closed, R.string.store_close).show();
                checkout_button.setEnabled(false);
            }

        }
    }


    private MaterialDialog.Builder onCreateDialog(int title, int content) {

        MaterialDialog.Builder dialog = new MaterialDialog.Builder(this)
                .content(content)
                .title(title)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .positiveText(R.string.ok)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }

                });

        return dialog;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
