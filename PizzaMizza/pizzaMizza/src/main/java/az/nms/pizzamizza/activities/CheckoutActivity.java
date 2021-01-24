package az.nms.pizzamizza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import az.nms.pizzamizza.Constants;
import az.nms.pizzamizza.models.OrderItems;
import az.nms.pizzamizza.models.OrderList;
import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.models.MyOrders;
import az.nms.pizzamizza.models.Top;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.JSONParser;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckoutActivity extends Activity {


    private EditText eName, eMobile,
            ePostCode, eNotes, eApt, eStreet;

    private EditText sZone;

    private PizzaMizzaDatabase db;

    public static ScrollView scroll;

    private Button checkout;

    private boolean isDelivery;

    private LinearLayout address_info_view;

    private String numberRegEx = "\\s*?(((40|50|51|55|70|77)([- ])?([2-9]))|(12)([- ])?([3-5]))(\\d{2})([- ])?(\\d{2})([- ])?\\d{2}\\s*?$";

    private MyOrders myOrder = new MyOrders();


    private int orderId;
    private String orderDate;


    private JSONParser jParser = new JSONParser();

    public CheckoutActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_orders_layout);

        db = new PizzaMizzaDatabase(this);

        Utilites.initAppBarFont(this);
        Utilites.hideButton(this, Utilites.ACTION_BAR_EDIT);
        Utilites.hideButton(this, Utilites.ACTION_BAR_HOME);

        isDelivery = getIntent().getBooleanExtra(DeliveryAddressActivity.ACTION_DELIVERY, false);

        initLayout();

        if (isDelivery) {
            loadAddress();
            address_info_view.setVisibility(View.VISIBLE);
        } else
            address_info_view.setVisibility(View.GONE);


        checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isAllFilled(new EditText[]{eName, eMobile})) {


                    phoneConfirmationDialog().show();




//                    customer.name = eName.getText().toString() + " " +
//                            eFamilyName.getText().toString();
//
//                    customer.phone = eMobile.getText().toString();
//                    customer.postcode = ePostCode.getText().toString();
//                    customer.email = "username@example.com";
//                    customer.notes = eNotes.getText().toString();
//                    customer.address = eApt.getText().toString() + " " +
//                            eStreet.getText().toString() + ", " +
//                            sZone.getText().toString();
//
//
//                    new PutOrdersAsynctask(CheckoutOrders.this,
//                            getJSON(customer), myOrder).execute();
                }


            }


        });

    }


    private void initLayout() {

        eName = (EditText) findViewById(R.id.checkout_name);
        eApt = (EditText) findViewById(R.id.checkout_apt);
        eMobile = (EditText) findViewById(R.id.checkout_mobile);
        eNotes = (EditText) findViewById(R.id.checkout_notes);
        eStreet = (EditText) findViewById(R.id.checkout_street);
        ePostCode = (EditText) findViewById(R.id.checkout_postcode);

        scroll = (ScrollView) findViewById(R.id.checkout_scroll);

        address_info_view = (LinearLayout) findViewById(R.id.delivery_address);


        eName.requestFocus();

        sZone = (EditText) findViewById(R.id.checkout_zone);

        eApt.setEnabled(false);
        eStreet.setEnabled(false);
        sZone.setEnabled(false);
        ePostCode.setEnabled(true);


        checkout = (Button) findViewById(R.id.checkout_place_order);
//        checkout.setEnabled(false);


    }

    private void loadAddress() {
        eApt.setText(SharedData.getAptNumber());
        eStreet.setText(SharedData.getStreetName());
        sZone.setText(db.getDeliveryArea(SharedData.getZoneId() + 1).getName());

    }


    private MaterialDialog.Builder phoneConfirmationDialog(){

        MaterialDialog.Builder dialog =  new MaterialDialog.Builder(this)
                .title("+994" + eMobile.getText().toString())
                .content(R.string.phone_confirmation)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .negativeColorRes(R.color.app_base_accent)
                .positiveText(R.string.yes_i_confirm)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        new PutOrders().execute();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                });



        return dialog;
    }


    private boolean isAllFilled(EditText[] edit) {
        for (int i = 0; i < edit.length; i++) {
            if (edit[i].getText().toString().matches("") || edit[i] == null) {
                switch (edit[i].getId()) {
                    case R.id.checkout_name:
                        Utilites.messageDialog(this, R.string.fill_name_message, R.string.name, R.string.ok).show();

                        break;

                    case R.id.checkout_mobile:
                        Utilites.messageDialog(this, R.string.fill_phone_message, R.string.phone, R.string.ok).show();

                        break;
                }
                edit[i].requestFocus();
                return false;
            } else if (edit[i].getId() == R.id.checkout_mobile){
                if(!edit[i].getText().toString().matches(numberRegEx)){
                       Utilites.messageDialog(this, R.string.wrong_number_format_msg, R.string.wrong_number_format, R.string.ok).show();
                    edit[i].requestFocus();
                    return false;
                }

            }

        }
        return true;

    }


    private JSONObject createJSON()  {

        try {

        // initilaize total price of orders
        double totalPrice = 0.0;

        // define food array and object
        JSONArray itemsArray = new JSONArray();
        JSONObject itemsObject = new JSONObject();


        JSONObject addonsObject = new JSONObject();

        JSONObject orderObject = new JSONObject();


        JSONObject customerObject = new JSONObject();

        for (int i = 0; i < OrderList.orders.size();i++){

            OrderItems order = OrderList.orders.get(i);

            List<Top> tops = order.getFood().getTops();

            JSONArray addonsArray = new JSONArray();

            itemsObject = new JSONObject();

            for (int k = 0; k < tops.size(); k++){

                if(tops.get(k).getSize() != 0){

                    addonsObject = new JSONObject();

                    addonsObject.put("topsId",tops.get(k).getId());
                    addonsObject.put("topsPosition",0);
                    addonsObject.put("topsSize",tops.get(k).getSize());

                    addonsArray.put(addonsObject);

                }

            }


            itemsObject.put("amount", order.getQuantity());
            itemsObject.put("foodId", order.getFood().getId());
            itemsObject.put("foodSize",order.getOrderSize());
            itemsObject.put("totalPrice",order.getTotalPrice());
            itemsObject.put("toppings",addonsArray);
            itemsObject.put("double_cheese",order.isDoubleCheese() ? 1 : 0);

            itemsArray.put(itemsObject);

            totalPrice += order.getTotalPrice();

        }


        orderObject.put("paymentBy","cash");
        orderObject.put("totalPrice",totalPrice);
        orderObject.put("regid", SharedData.getRegId());
        orderObject.put("lang", SharedData.getLanguage());


        if(isDelivery){
            orderObject.put("orderType","delivery");
        } else {
            orderObject.put("orderType","carryout");
        }

        if(eNotes.getText().equals(""))
            customerObject.put("notes","");
        else
            customerObject.put("notes",eNotes.getText().toString());


        if (isDelivery){
            customerObject.put("address",eApt.getText().toString() + ", " + eStreet.getText().toString() + ", " + sZone.getText().toString());
        } else {
            customerObject.put("address","");
        }


        customerObject.put("phone","+994" + eMobile.getText().toString());
        customerObject.put("email","for@example.com");
        customerObject.put("name",eName.getText().toString());


        JSONObject generalObject = new JSONObject();

        generalObject.put("order",orderObject);
        generalObject.put("orderitems",itemsArray);
        generalObject.put("customer",customerObject);

        return generalObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


//    static class Customer {
//
//        public String name, phone, address, postcode, notes, email, orderType;
//
//    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }



    class PutOrders extends AsyncTask<String,String,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SharedData.init(CheckoutActivity.this);
        }

        @Override
        protected Integer doInBackground(String... args) {

            try {

                String ordersJson = createJSON().toString();

                Log.d("json", ordersJson);

                List<NameValuePair> params = new ArrayList<>();

                params.add(new BasicNameValuePair("db","order_json"));
                params.add(new BasicNameValuePair("order_json",ordersJson));

                JSONObject json = jParser.makeHttpRequest(Constants.SERVER_SET,"POST",params);

                if (json == null)
                    return null;
                else {

                    JSONArray jsonOrder = json.getJSONArray("order");

                    for (int i = 0; i < jsonOrder.length(); i++){

                        JSONObject jsonOrderObject = jsonOrder.getJSONObject(i);

                        orderId = jsonOrderObject.getInt("id");
                        orderDate = jsonOrderObject.getString("date");
                     }

                    return 1;
                }


                } catch (JSONException e) {
                    e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);


            if (s!=null){




                Intent i  = new Intent(CheckoutActivity.this, PlaceOrderActivity.class);
                i.putExtra("orderId",orderId);
                i.putExtra("orderDate",orderDate);
                i.putExtra("customerName",eName.getText().toString());
                i.putExtra("customerPhone",eMobile.getText().toString());
                i.putExtra(DeliveryAddressActivity.ACTION_DELIVERY,isDelivery);

                startActivity(i);
                finish();
            } else{

                onCreateDialog().show();
            }


        }
    }




    private MaterialDialog.Builder onCreateDialog() {


        MaterialDialog.Builder dialog =  new MaterialDialog.Builder(this)
                .title(R.string.connection)
                .content(R.string.unable_to_connect_toast)
                .titleColor(R.color.app_base_color)
                .contentColor(R.color.dialog_text_color)
                .positiveColor(R.color.app_base_color)
                .positiveText(R.string.re_try)
                .negativeColorRes(R.color.app_base_accent)
                .negativeText(R.string.close)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        new PutOrders().execute();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
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
