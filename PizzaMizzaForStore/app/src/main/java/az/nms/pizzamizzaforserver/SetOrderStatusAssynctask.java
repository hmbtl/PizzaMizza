package az.nms.pizzamizzaforserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import az.nms.pizzamizzaforserver.Utitilies.AppTools;
import az.nms.pizzamizzaforserver.Utitilies.Constants;
import az.nms.pizzamizzaforserver.Utitilies.JSONParser;
import az.nms.pizzamizzaforserver.storage.PizzaServerDatabase;

/**
 * Created by anar on 5/13/15.
 */
public class SetOrderStatusAssynctask extends AsyncTask<String, String, Integer> {


    private OrderDetailActivity activity;
    private int orderId;
    private int status;
    private String regid;

    private JSONParser jParser = new JSONParser();
    private ProgressDialog dialog;

    public SetOrderStatusAssynctask(OrderDetailActivity activity, int orderId, String regid, int status) {

        this.activity = activity;
        this.orderId = orderId;
        this.regid = regid;
        this.status = status;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(activity);
        dialog.setTitle("Processing");
        dialog.setMessage("Please wait, while we are processing this order.");
        dialog.show();

    }

    @Override
    protected Integer doInBackground(String... args) {

        try {

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("order_id", String.valueOf(orderId)));
            params.add(new BasicNameValuePair("regid", regid));
            params.add(new BasicNameValuePair("status", String.valueOf(status)));
            params.add(new BasicNameValuePair("db", "order_status"));

            Log.d("asynctask regid", regid);

            JSONObject json = jParser.makeHttpRequest(Constants.SERVER_SET_URL, "POST", params);

            if (json != null)
                return json.getInt("success");
            else
                return 0;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        dialog.dismiss();

        if (result == 1) {

            new PizzaServerDatabase(activity).updateOrder(orderId, status);

            AppTools.showStatusToast(activity, status, orderId);

            Intent intent;

            if (status == Constants.STATUS_ACCEPTED || status == Constants.STATUS_DECLINED) {
                intent = new Intent(activity, OrderListActivity.class);
                activity.finish();
                activity.startActivity(intent);
                activity.findViewById(R.id.orderdetail_accept_decline_lr).setVisibility(View.GONE);

            }else {
                activity.setSteps(status);
            }


        }
    }
}
