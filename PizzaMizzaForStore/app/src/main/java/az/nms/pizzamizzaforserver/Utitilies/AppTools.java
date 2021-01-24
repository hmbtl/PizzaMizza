package az.nms.pizzamizzaforserver.Utitilies;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import az.nms.pizzamizzaforserver.R;

/**
 * Created by anar on 5/13/15.
 */
public class AppTools {

    public static void showStatusToast(Activity context, int status, int orderId) {

        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) context.findViewById(R.id.toast_background));

        switch (status) {
            case 1:

                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + " successfully accepted.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);


                break;
            case -1:
                // Inflate the Layout
                layout.setBackgroundColor(Color.parseColor("#ffffb292"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Declined");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#de1623"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + " declined and will be removed.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.decline);

                break;
            case 2:
                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + "  changed status to MAKING.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);



                break;
            case 3:
                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + " changed status to READY TO DELIVER.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);
                break;
            case 4:
                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + " changed status to SEND TO DELIVER.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);
                break;

            case 5:
                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + " changed status to DELIVERED.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);
                break;

            case 6:
                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Order #" + orderId + " changed status to TAKEN.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);
                break;


        }

        // Create Custom Toast
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    public static void showMessageToast(Activity context, int success) {

        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) context.findViewById(R.id.toast_background));

        switch (success) {
            case 1:

                layout.setBackgroundColor(Color.parseColor("#d4ffcd"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Success");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#54a846"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("Your message successfully send to user.");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.success);


                break;
            case 0:
                // Inflate the Layout
                layout.setBackgroundColor(Color.parseColor("#ffffb292"));

                ((TextView) layout.findViewById(R.id.toast_title)).setText("Unsuccess");
                ((TextView) layout.findViewById(R.id.toast_title)).setTextColor(Color.parseColor("#de1623"));
                ((TextView) layout.findViewById(R.id.toast_subtitle)).setText("We unable to send your message to user. Please try again");
                ((ImageView) layout.findViewById(R.id.toast_image)).setImageResource(R.drawable.decline);

                break;

        }

        // Create Custom Toast
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    public static String generateMessage(String message, List<String> regIds) {

        try {

            JSONObject messageJSON = new JSONObject();



            messageJSON.put("message", message);

            messageJSON.put("to", new JSONArray(regIds));

            return messageJSON.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getStatus(int status){

        switch (status){
            case Constants.STATUS_NEW: return "NEW";

            case Constants.STATUS_DECLINED: return "DECLINED";

            case Constants.STATUS_ACCEPTED: return "ACCEPTED";

            case Constants.STATUS_MAKING: return "MAKING";

            case Constants.STATUS_READY: return "READY";


            case Constants.STATUS_DELIVERING: return "DELIVERING";

            case Constants.STATUS_DELIVERED: return "DELIVERED";
            case Constants.STATUS_TAKEN: return "TAKEN";

        }

        return "";
    }


}
