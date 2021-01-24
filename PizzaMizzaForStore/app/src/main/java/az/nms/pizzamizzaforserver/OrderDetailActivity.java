package az.nms.pizzamizzaforserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizzaforserver.Utitilies.AppTools;
import az.nms.pizzamizzaforserver.Utitilies.Constants;
import az.nms.pizzamizzaforserver.Utitilies.JSONParser;
import az.nms.pizzamizzaforserver.storage.OrderItem;
import az.nms.pizzamizzaforserver.storage.PizzaServerDatabase;

/**
 * Created by anar on 5/12/15.
 */
public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private JSONParser jParser = new JSONParser();
    private int order_id = 0;
    private List<OrderItem> orderItems;
    private ProgressDialog dialog;
    private ListView orderList;
    private OrderDetailAdapter adapter;
    private Button accept, decline;
    private LinearLayout acceptDeclineLL;
    private View footerView, headerView;

    private String regid;

      TextView orderId, customerName, customerAddress, customerPhone;
      TextView orderType, orderDate, orderTotal, orderNotes, messageSend, statusTitle;

    private EditText messageToSend;

    private PizzaServerDatabase db ;

    private int status;

    private String type ;

    public  TextView step1, step2, step3, step3co, step4, step1Text, step2Text, step3Text,step3coText,  step4Text;
     public  ImageView step1Indicator, step2Indicator, step3Indicator, step1success, step2success, step3success, step4success, step3cosuccess;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_layout);

        db= new PizzaServerDatabase(this);

        order_id = getIntent().getIntExtra("ORDER_ID", 0);
        regid = getIntent().getStringExtra("ORDER_REGID");
        status = db.getOrderStatus(order_id);
        type = getIntent().getStringExtra("ORDER_TYPE");

        initLayout();


        Log.d("activity regid", regid);


        orderList = (ListView) findViewById(R.id.orderdetail_list);
        orderItems = new LinkedList<>();


        orderList.addHeaderView(headerView);

        if (status != Constants.STATUS_NEW) {
            acceptDeclineLL.setVisibility(View.GONE);

            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) orderList
                    .getLayoutParams();

            mlp.setMargins(0, 0, 0, 0);

            orderList.setLayoutParams(mlp);
        }

        if (status >= Constants.STATUS_ACCEPTED  && status <= Constants.STATUS_TAKEN) {
            orderList.addFooterView(footerView);
            initLayoutsSteps();
        }
        new GetOrderDetail().execute();


    }




    public  void setSteps(int statusSelected){

        this.status = statusSelected;


        switch (statusSelected) {
            case Constants.STATUS_ACCEPTED:
                step1.setTextColor(Color.parseColor("#de1623"));
                step2.setTextColor(Color.parseColor("#686868"));
                step3.setTextColor(Color.parseColor("#686868"));
                step4.setTextColor(Color.parseColor("#686868"));
                step3co.setTextColor(Color.parseColor("#686868"));



                step1Text.setEnabled(true);
                step2Text.setEnabled(true);
                step3Text.setEnabled(true);
                step4Text.setEnabled(true);
                step3coText.setEnabled(true);

                step1Indicator.setVisibility(View.VISIBLE);
                step2Indicator.setVisibility(View.GONE);
                step3Indicator.setVisibility(View.GONE);

                step1success.setVisibility(View.GONE);
                step2success.setVisibility(View.GONE);
                step3success.setVisibility(View.GONE);
                step3cosuccess.setVisibility(View.GONE);
                step4success.setVisibility(View.GONE);

                break;
            case Constants.STATUS_MAKING:
                step1.setTextColor(Color.parseColor("#686868"));
                step2.setTextColor(Color.parseColor("#de1623"));
                step3.setTextColor(Color.parseColor("#686868"));
                step4.setTextColor(Color.parseColor("#686868"));
                step3co.setTextColor(Color.parseColor("#686868"));



                step1Text.setEnabled(false);
                step2Text.setEnabled(true);
                step3Text.setEnabled(true);
                step4Text.setEnabled(true);
                step3coText.setEnabled(true);

                step1Indicator.setVisibility(View.GONE);
                step2Indicator.setVisibility(View.VISIBLE);
                step3Indicator.setVisibility(View.GONE);


                step1success.setVisibility(View.VISIBLE);
                step2success.setVisibility(View.GONE);
                step3success.setVisibility(View.GONE);
                step3cosuccess.setVisibility(View.GONE);
                step4success.setVisibility(View.GONE);


                break;
            case Constants.STATUS_READY:


                step1.setTextColor(Color.parseColor("#686868"));
                step2.setTextColor(Color.parseColor("#686868"));
                step3.setTextColor(Color.parseColor("#de1623"));
                step3co.setTextColor(Color.parseColor("#686868"));
                step4.setTextColor(Color.parseColor("#686868"));



                step1Text.setEnabled(false);
                step2Text.setEnabled(false);
                step3Text.setEnabled(true);
                step3coText.setEnabled(true);
                step4Text.setEnabled(true);

                step1Indicator.setVisibility(View.GONE);
                step2Indicator.setVisibility(View.GONE);
                step3Indicator.setVisibility(View.VISIBLE);


                step1success.setVisibility(View.VISIBLE);
                step2success.setVisibility(View.VISIBLE);
                step3success.setVisibility(View.GONE);
                step3cosuccess.setVisibility(View.GONE);
                step4success.setVisibility(View.GONE);


                break;
            case Constants.STATUS_DELIVERING:

                step1.setTextColor(Color.parseColor("#686868"));
                step2.setTextColor(Color.parseColor("#686868"));
                step3.setTextColor(Color.parseColor("#686868"));
                step3co.setTextColor(Color.parseColor("#de1623"));
                step4.setTextColor(Color.parseColor("#de1623"));



                step1Text.setEnabled(false);
                step2Text.setEnabled(false);
                step3Text.setEnabled(false);
                step3coText.setEnabled(true);
                step4Text.setEnabled(true);

                step1Indicator.setVisibility(View.GONE);
                step2Indicator.setVisibility(View.GONE);
                step3Indicator.setVisibility(View.GONE);


                step1success.setVisibility(View.VISIBLE);
                step2success.setVisibility(View.VISIBLE);
                step3success.setVisibility(View.VISIBLE);
                step3cosuccess.setVisibility(View.GONE);
                step4success.setVisibility(View.GONE);

                break;

            case Constants.STATUS_DELIVERED:

                step1.setTextColor(Color.parseColor("#686868"));
                step2.setTextColor(Color.parseColor("#686868"));
                step3.setTextColor(Color.parseColor("#686868"));
                step3co.setTextColor(Color.parseColor("#686868"));
                step4.setTextColor(Color.parseColor("#de1623"));



                step1Text.setEnabled(false);
                step2Text.setEnabled(false);
                step3Text.setEnabled(false);
                step3coText.setEnabled(true);
                step4Text.setEnabled(false);

                step1Indicator.setVisibility(View.GONE);
                step2Indicator.setVisibility(View.GONE);
                step3Indicator.setVisibility(View.GONE);


                step1success.setVisibility(View.VISIBLE);
                step2success.setVisibility(View.VISIBLE);
                step3success.setVisibility(View.VISIBLE);
                step3cosuccess.setVisibility(View.GONE);
                step4success.setVisibility(View.VISIBLE);

                break;

            case Constants.STATUS_TAKEN:

                step1.setTextColor(Color.parseColor("#686868"));
                step2.setTextColor(Color.parseColor("#686868"));
                step3.setTextColor(Color.parseColor("#686868"));
                step3co.setTextColor(Color.parseColor("#de1623"));
                step4.setTextColor(Color.parseColor("#686868"));



                step1Text.setEnabled(false);
                step2Text.setEnabled(false);
                step3Text.setEnabled(false);
                step3coText.setEnabled(false);
                step4Text.setEnabled(false);

                step1Indicator.setVisibility(View.GONE);
                step2Indicator.setVisibility(View.GONE);
                step3Indicator.setVisibility(View.GONE);


                step1success.setVisibility(View.VISIBLE);
                step2success.setVisibility(View.VISIBLE);
                step3success.setVisibility(View.VISIBLE);
                step3cosuccess.setVisibility(View.VISIBLE);
                step4success.setVisibility(View.VISIBLE);

                break;

        }
    }


    private void initLayout() {


        footerView = View.inflate(this, R.layout.order_detail_list_footer, null);
        headerView = View.inflate(this, R.layout.order_detail_list_header, null);


        statusTitle = (TextView) headerView.findViewById(R.id.orderdetail_status);


        orderId = (TextView) headerView.findViewById(R.id.orderdetail_id);
        customerName = (TextView) headerView.findViewById(R.id.orderdetail_name);
        customerAddress = (TextView) headerView.findViewById(R.id.orderdetail_address);
        customerPhone = (TextView) headerView.findViewById(R.id.orderdetail_phone);
        orderType = (TextView) headerView.findViewById(R.id.orderdetail_type);
        orderDate = (TextView) headerView.findViewById(R.id.orderdetail_date);
        orderTotal = (TextView) headerView.findViewById(R.id.orderdetail_price);
        orderNotes = (TextView) headerView.findViewById(R.id.orderdetail_notes);

        acceptDeclineLL = (LinearLayout) findViewById(R.id.orderdetail_accept_decline_lr);


        accept = (Button) findViewById(R.id.orderdetail_accept);
        decline = (Button) findViewById(R.id.orderdetail_decline);

        accept.setOnClickListener(this);
        decline.setOnClickListener(this);


        orderId.setText("Order #" + getIntent().getIntExtra("ORDER_ID", 0));
        customerAddress.setText(getIntent().getStringExtra("CUSTOMER_ADDRESS"));
        customerName.setText(getIntent().getStringExtra("CUSTOMER_NAME"));
        customerPhone.setText(getIntent().getStringExtra("CUSTOMER_PHONE"));
        orderType.setText(getIntent().getStringExtra("ORDER_TYPE"));


        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) getIntent().getSerializableExtra("ORDER_DATE"));

        orderDate.setText(sdf.format(calendar.getTime()));

        orderTotal.setText(getIntent().getDoubleExtra("ORDER_TOTAL", 0.00) + " AZN");
        orderNotes.setText(getIntent().getStringExtra("ORDER_NOTES"));


        if (orderNotes.getText().toString().equals("") || orderNotes.getText().toString().isEmpty())
            orderNotes.setVisibility(View.GONE);


        if (customerAddress.getText().toString().equals("") || customerAddress.getText().toString().isEmpty())
            customerAddress.setVisibility(View.GONE);


        statusTitle.setText(AppTools.getStatus(status));


        messageSend = (TextView) footerView.findViewById(R.id.orderdetail_text_send_button);
        messageToSend = (EditText) footerView.findViewById(R.id.orderdetail_text_to_send);


        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("meesageTosend", messageToSend.getText().toString());

                new SendNotificationAssynctask(OrderDetailActivity.this,
                        AppTools.generateMessage(messageToSend.getText().toString(), Arrays.asList(regid))).execute();

            }
        });


    }


    private void initLayoutsSteps() {

        step1 = (TextView) footerView.findViewById(R.id.step1);
        step2 = (TextView) footerView.findViewById(R.id.step2);
        step3 = (TextView) footerView.findViewById(R.id.step3);
        step3co = (TextView) footerView.findViewById(R.id.step3_co);
        step4 = (TextView) footerView.findViewById(R.id.step4);

        step1Text = (TextView) footerView.findViewById(R.id.step1_button);
        step2Text = (TextView) footerView.findViewById(R.id.step2_button);
        step3Text = (TextView) footerView.findViewById(R.id.step3_button);
        step3coText = (TextView) footerView.findViewById(R.id.step3_co_button);
        step4Text = (TextView) footerView.findViewById(R.id.step4_button);

        step1Indicator = (ImageView) footerView.findViewById(R.id.step1_indicator);
        step2Indicator = (ImageView) footerView.findViewById(R.id.step2_indicator);
        step3Indicator = (ImageView) footerView.findViewById(R.id.step3_indicator);

        step1success = (ImageView) footerView.findViewById(R.id.step1_success);
        step2success = (ImageView) footerView.findViewById(R.id.step2_success);
        step3success = (ImageView) footerView.findViewById(R.id.step3_success);
        step3cosuccess = (ImageView) footerView.findViewById(R.id.step3_co_success);
        step4success = (ImageView) footerView.findViewById(R.id.step4_success);


        step1Text.setOnClickListener(this);
        step2Text.setOnClickListener(this);
        step3Text.setOnClickListener(this);
        step3coText.setOnClickListener(this);
        step4Text.setOnClickListener(this);

        if(type.equalsIgnoreCase("delivery")){
            findViewById(R.id.step3_co_rl).setVisibility(View.GONE);
            findViewById(R.id.step3co_shadow).setVisibility(View.GONE);
            findViewById(R.id.step4_rl).setVisibility(View.VISIBLE);
            findViewById(R.id.step3_rl).setVisibility(View.VISIBLE);
            findViewById(R.id.step3_shadow).setVisibility(View.VISIBLE);
            findViewById(R.id.step4_shadow).setVisibility(View.VISIBLE);
        }else{

            findViewById(R.id.step3_co_rl).setVisibility(View.VISIBLE);
            findViewById(R.id.step3co_shadow).setVisibility(View.VISIBLE);
            findViewById(R.id.step4_rl).setVisibility(View.GONE);
            findViewById(R.id.step3_rl).setVisibility(View.GONE);
            findViewById(R.id.step3_shadow).setVisibility(View.GONE);
            findViewById(R.id.step4_shadow).setVisibility(View.GONE);
        }

       setSteps(status);

    }


    @Override
    protected void onResume() {
        super.onResume();

//        initLayoutsSteps();
    }




    public void onClick(View v) {

        int statusToSet = 0;

        if (v == accept) {
            statusToSet = 1;
        } else if (v == decline) {
            statusToSet = -1;
        } else if (v == step1Text){
            statusToSet = 2;
        }else if (v == step2Text){
            statusToSet = 3;
        }else if (v == step3Text){
            statusToSet = 4;
        }else if (v == step4Text){
            statusToSet = 5;
        } else if (v == step3coText){
            statusToSet = 6;

        }


        new SetOrderStatusAssynctask(OrderDetailActivity.this, order_id, regid, statusToSet).execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OrderListActivity.class);
        intent.putExtra("current_position",getIntent().getIntExtra("current_position",0));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    class GetOrderDetail extends AsyncTask<String, String, List<OrderItem>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(OrderDetailActivity.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait, loading order details");
            dialog.show();


        }

        @Override
        protected List<OrderItem> doInBackground(String... args) {


            try {

                List<OrderItem> orderItems = new LinkedList<>();

                List<NameValuePair> params = new ArrayList<>();

                params.add(new BasicNameValuePair("db", "order_detail"));
                params.add(new BasicNameValuePair("order_id", String.valueOf(order_id)));

                Log.d(" order id ", order_id + "");

                JSONObject json = jParser.makeHttpRequest(Constants.SERVER_GET_URL, "GET", params);

                if (json != null) {

                    // get food array from database
                    JSONArray jsonOrders = json.getJSONArray("order_detail");

                    for (int i = 0; i < jsonOrders.length(); i++) {

                        JSONObject jsonOrdersObject = jsonOrders.getJSONObject(i);

                        int order_item_id = jsonOrdersObject.getInt("order_item_id");
                        int food_id = jsonOrdersObject.getInt("food_id");
                        String food_icon = jsonOrdersObject.getString("food_icon");
                        String food_name = jsonOrdersObject.getString("food_name");
                        String food_toppings = jsonOrdersObject.getString("food_toppings").equalsIgnoreCase("null") ? "" : jsonOrdersObject.getString("food_toppings");
                        String food_size = jsonOrdersObject.getString("food_size");
                        int food_quantity = jsonOrdersObject.getInt("food_quantity");
                        double food_total = jsonOrdersObject.getDouble("food_total");

                        boolean double_cheese = false;
                        if(!jsonOrdersObject.isNull("double_cheese"))
                         double_cheese= jsonOrdersObject.getInt("double_cheese") == 1;

                        orderItems.add(new OrderItem(order_item_id, food_id, food_icon, food_name, food_toppings, food_size, food_quantity, food_total,double_cheese));

                        Log.d("id", order_item_id + "");

                    }

                    return orderItems;

                }


            } catch (Exception te) {
                Log.e(" exception ", "Exception: " + te);
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<OrderItem> orderItemsResult) {
            super.onPostExecute(orderItems);

            dialog.dismiss();


            adapter = new OrderDetailAdapter(OrderDetailActivity.this, R.layout.order_detail_list_item, orderItemsResult);

            orderList.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }
    }
}
