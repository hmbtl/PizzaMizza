package az.nms.pizzamizza;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizza.activities.MainPageActivity;
import az.nms.pizzamizza.models.Food;
import az.nms.pizzamizza.models.FoodTopping;
import az.nms.pizzamizza.models.DeliveryArea;
import az.nms.pizzamizza.models.Top;
import az.nms.pizzamizza.tools.JSONParser;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;
import az.nms.pizzamizza.tools.UIHelper;

/**
 * Created by anar on 5/7/15.
 */
public class InitializeMenuAsynctask extends AsyncTask<String,String, Integer> {

    private String currentLanguage;

    private JSONParser jParser = new JSONParser();

    private PizzaMizzaDatabase db;

    private Activity context;

    private List<DeliveryArea> deliveryAreas = new LinkedList<>();

    public static final int ACTION_INIT = 0;
    public static final int ACTION_RELOAD = 1;

    private int action;
    private MaterialDialog dialog;



    public InitializeMenuAsynctask(Activity context, int action){
        db = new PizzaMizzaDatabase(context);
        this.context = context;
        this.action = action;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        currentLanguage = SharedData.getLanguage();
        switch (action){
            case ACTION_RELOAD:
                dialog =  new MaterialDialog.Builder(context)
                        .title(R.string.loading_menu)
                        .titleColorRes(R.color.app_base_color)
                        .content(R.string.please_wait)
                        .progress(true, 0)
                        .show();
                break;
        }

    }

    @Override
    protected Integer doInBackground(String... args) {

        try {

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("db","all"));
            params.add(new BasicNameValuePair("lang",currentLanguage));

            JSONObject json = jParser.makeHttpRequest(Constants.SERVER_GET,"GET",params);

            if (json == null) {
                return null;
            }


            // get food array from database
            JSONArray jsonFood = json.getJSONArray("food");

            for(int i = 0; i < jsonFood.length(); i++){

                JSONObject jsonFoodObject = jsonFood.getJSONObject(i);

                int food_id = jsonFoodObject.getInt("food_id");
                String name = jsonFoodObject.getString("name");
                String icon = jsonFoodObject.getString("icon");
                double price_small = jsonFoodObject.getDouble("price_small");
                double price_medium = jsonFoodObject.getDouble("price_medium");
                double price_large = jsonFoodObject.getDouble("price_large");
                String type = jsonFoodObject.getString("type");

                db.add(new Food(food_id, icon, name, price_small, price_medium, price_large, type));

            }


            JSONArray jsonTop = json.getJSONArray("top");

            for (int i = 0; i < jsonTop.length();i++){

                JSONObject jsonTopObject = jsonTop.getJSONObject(i);

                int top_id = jsonTopObject.getInt("top_id");
                String name_az = jsonTopObject.getString("name_az");
                String name_en = jsonTopObject.getString("name_en");
                String name_ru = jsonTopObject.getString("name_ru");
                int editable = jsonTopObject.getInt("editable");

                db.add(new Top(top_id, name_az, name_en, name_ru, editable));

            }

            JSONArray jsonRef = json.getJSONArray("food_topping");

            for (int i = 0; i < jsonRef.length();i++){

                JSONObject jsonRefObject = jsonRef.getJSONObject(i);

                int food_topping_id = jsonRefObject.getInt("food_topping_id");
                int food_id = jsonRefObject.getInt("food_id");
                int top_id = jsonRefObject.getInt("top_id");

                db.add(new FoodTopping(food_topping_id, food_id, top_id));

            }


            JSONArray jsonLocation = json.getJSONArray("delivery_area");


            for (int i = 0; i < jsonLocation.length();i++){

                JSONObject jsonLocationObject = jsonLocation.getJSONObject(i);

                int id  = jsonLocationObject.getInt("delivery_area_id");
                String name = jsonLocationObject.getString("name");
                int min_order = jsonLocationObject.getInt("min_order");

                db.add(new DeliveryArea(id,name,min_order));

            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);


        if (s!=null){
            Log.d("locationfirst", deliveryAreas.toString());


            if(SharedData.isFirstRun())
                new RegisterDeviceAsynctask(context).execute();


            Intent i = new Intent(context, MainPageActivity.class);

            switch (action){
                case ACTION_INIT :
                    context.startActivity(i);
                    context.finish();
                    break;
                case ACTION_RELOAD :
                    context.finish();
                    context.startActivity(i);
                    dialog.dismiss();
                    context.overridePendingTransition(0, 0);

                    break;


            }

        } else {

            onCreateDialog().show();
        }


    }



    private MaterialDialog.Builder onCreateDialog() {


        MaterialDialog.Builder dialog =  new MaterialDialog.Builder(context)
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
                        new InitializeMenuAsynctask(context,action).execute();
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