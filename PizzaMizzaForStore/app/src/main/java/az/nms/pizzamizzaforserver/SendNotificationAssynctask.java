package az.nms.pizzamizzaforserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import az.nms.pizzamizzaforserver.Utitilies.AppTools;
import az.nms.pizzamizzaforserver.Utitilies.Constants;
import az.nms.pizzamizzaforserver.Utitilies.JSONParser;

/**
 * Created by anar on 5/13/15.
 */
public class SendNotificationAssynctask extends AsyncTask<String, String, Integer> {


    private Activity context;

    private String message ;
    private JSONParser jParser = new JSONParser();
    private ProgressDialog dialog;

    public SendNotificationAssynctask(Activity context, String message) {

        this.context = context;
        this.message = message;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setTitle("Processing");
        dialog.setMessage("Please wait, while we are processing this order.");
        dialog.show();

    }

    @Override
    protected Integer doInBackground(String... args) {

        try {

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("message",message));
            params.add(new BasicNameValuePair("db","message"));

            Log.d("message",message);


            JSONObject json = jParser.makeHttpRequest(Constants.SERVER_SET_URL, "POST", params);

            if(json != null)
               return json.getInt("success");
            else
                return  0;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        dialog.dismiss();


            AppTools.showMessageToast(context, result);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {


                }
            }, 3000);

        }

}
