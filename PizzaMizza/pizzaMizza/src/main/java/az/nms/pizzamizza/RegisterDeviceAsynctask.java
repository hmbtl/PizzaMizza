package az.nms.pizzamizza;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import az.nms.pizzamizza.tools.JSONParser;
import az.nms.pizzamizza.tools.UIHelper;

/**
 * Created by anar on 5/20/15.
 */

public class RegisterDeviceAsynctask extends AsyncTask<String, String, Integer> {


    private Context context;

    private String regid = "";

    public RegisterDeviceAsynctask(Context context){
        this.context = context;
    }

    @Override
    protected void onPostExecute(Integer rslt) {

        if (rslt == 1) {
            SharedData.setRegId(regid);
            //start the service for updates now
            SharedData.setFirstRunFalse();

        } else {
            onCreateDialog();
        }

    }

    @Override
    protected Integer doInBackground(String... args) {

        try {


            Log.d("registration", "try");
            JSONParser jsonParser = new JSONParser();

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("register", "true"));

            JSONObject json = jsonParser.makeHttpRequest(Constants.REGISTER_URL, "POST", params);

            if (json != null) {
                Log.d("json", json.toString());

                regid = json.getString("regid");

                Log.d("registration", "registered");

                return 1;
            }


        } catch (JSONException e) {
            e.printStackTrace();

        }
        return 0;
    }

    private MaterialDialog.Builder onCreateDialog() {


        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                .title(R.string.connection)
                .content(R.string.unable_to_connect_toast)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .negativeColorRes(R.color.app_base_accent)
                .positiveText(R.string.re_try)
                .negativeText(R.string.exit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        new RegisterDeviceAsynctask(context).execute();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);

                        UIHelper.killApp(true);
                    }
                });

        return dialog;
    }
}
