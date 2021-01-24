package az.nms.pizzamizza.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import az.nms.pizzamizza.R;
import az.nms.pizzamizza.SharedData;
import az.nms.pizzamizza.models.OrderList;
import az.nms.pizzamizza.models.Top;

public class Utilites {

    public static final int ACTION_BAR_HOME = 1;
    public static final int ACTION_BAR_EDIT = 2;


    public static final int ACTION_FINISH_ACTIVITY = 0;
    public static final int ACTION_CLOSE_DIALOG = 1;

    public static final String LANGUAGE_AZ = "az";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_RU = "ru";

    public Utilites() {
        // TODO Auto-generated constructor stub
    }

    /**
     * ****************** ROUND VALUE ********************
     */


    public static String encrypt(String strToEncrypt, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, new SecretKeySpec(key.getBytes(), "AES"));
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes()), 0);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String strToDecrypt, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(2, new SecretKeySpec(key.getBytes(), "AES"));
            return new String(cipher.doFinal(Base64.decode(strToDecrypt.getBytes(), 0)));
        } catch (Exception e) {
            return null;
        }
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void initAppBarFont(Activity context) {
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/BebasNeueRegular.ttf");
        TextView myTextView = (TextView) context
                .findViewById(R.id.action_title);
        myTextView.setTypeface(myTypeface);

    }


    public static void hideButton(Activity context, int type) {
        switch (type) {
            case 1:
                ImageButton img = (ImageButton) context
                        .findViewById(R.id.home_button);
                img.setVisibility(View.GONE);
                break;
            case 2:
                TextView txt = (TextView) context.findViewById(R.id.appbar_action);
                txt.setVisibility(View.GONE);
                break;
        }

    }






    public static boolean checkEmail(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(email);

        return matcher.matches();
    }



    public static Bitmap flipImage(Context context, int src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        Bitmap bit = BitmapFactory.decodeResource(context.getResources(), src);
        matrix.preScale(-1.0f, 1.0f);
        // return transformed image
        return Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(),
                matrix, true);
    }

    public static ImageView flipImage(Context context, ImageView imageView) {
        Matrix matrix = new Matrix();
        imageView.setScaleType(ScaleType.MATRIX); // required
        matrix.preScale(-1.0f, 1.0f);
        imageView.setImageMatrix(matrix);
        return imageView;
    }


    public static Dialog messageDialog(Activity context, String message, int button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = context.getLayoutInflater();

        boolean clicked = false;

        View view = inflater.inflate(R.layout.dialog_custom_layout, null);
//
//        TextView titleTextView = (TextView) view.findViewById(R.id.dialog_app_bar);
//        titleTextView.setText(title);

        TextView messageTextView = (TextView) view.findViewById(R.id.dialog_message);
        messageTextView.setText(message);

        Button closeButton = (Button) view.findViewById(R.id.dialog_close_button);
        closeButton.setText(button);
        builder.setView(view);

        final AlertDialog alert = builder.create();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();

            }
        });

        return alert;
    }



    public static MaterialDialog.Builder messageDialog(Activity context, int message, int title, int button){

       MaterialDialog.Builder dialog =  new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
               .titleColorRes(R.color.app_base_color)
               .contentColorRes(R.color.dialog_text_color)
               .positiveColorRes(R.color.app_base_color)
                .positiveText(button)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }
                });

        return dialog;
    }

    public static MaterialDialog.Builder messageDialog(Activity context, String message, String title, String button){

        MaterialDialog.Builder dialog =  new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .titleColorRes(R.color.app_base_color)
                .contentColorRes(R.color.dialog_text_color)
                .positiveColorRes(R.color.app_base_color)
                .positiveText(button)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }
                });

        return dialog;
    }




    public static boolean showDialog(final Activity context, int message, int action, int button, boolean cancel) {
        return Utilites.showDialog(context, context.getResources().getString(message), action, button, cancel);
    }


    public static boolean showDialog(final Activity context, String message, final int action, int button, boolean cancel) {

        Dialog dialog = Utilites.messageDialog(context, message, button);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                switch (action) {
                    case ACTION_FINISH_ACTIVITY:
                        context.finish();
                        break;
                    case ACTION_CLOSE_DIALOG:

                        break;
                }

            }
        });


        dialog.setCanceledOnTouchOutside(cancel);

        dialog.show();

        if (dialog.isShowing())
            return false;
        else
            return true;

    }


    public static double getTotalPriceOfOrders(){

        double rslt = 0.0;

        for (int i = 0; i< OrderList.orders.size();i++){
            rslt = rslt + OrderList.orders.get(i).getTotalPrice();
        }

        return rslt;
    }


    public static void setLocale(Activity context, String language){
        Locale mLocale = new Locale(language);
        Locale.setDefault(mLocale);
        Configuration config = context.getBaseContext().getResources().getConfiguration();
        if (!config.locale.equals(mLocale))
        {
            config.locale = mLocale;
            context.getBaseContext().getResources().updateConfiguration(config, null);
        }
        SharedData.setLanguage(language);
    }


    public static String getTopSizeString(Context context,int size){
        switch (size){
            case 0:
                return "";
            case 1:
                return "(" + context.getResources().getString(R.string.extra) + ")";
            case -1:
                return "(" + context.getResources().getString(R.string.none) + ")";
        }
        return "";
    }

/*
    public static String foodToppingsToString(Context context, int foodId){

        String rslt = "";

        PizzaMizzaDatabase db = new PizzaMizzaDatabase(context);

        List<Top> tops = db.getFoodTops(foodId);

        for (int i = 0; i < tops.size(); i++){
            rslt += tops.get(i).getName() + (i != tops.size() - 1 ? ", " : "");
        }

        return rslt;
    }


*/
    public static String foodToppingsWithSizeToString(Context context, List<Top> tops) {
        String rslt = "";

        PizzaMizzaDatabase db = new PizzaMizzaDatabase(context);
        for (int i = 0; i < tops.size(); i++) {

            String topName = tops.get(i).getName();

            rslt += topName + getTopSizeString(context, tops.get(i).getSize())
                                          + (i != tops.size() - 1 ? ", " : "");
        }
        return rslt;
    }


    public static String getStatus(Context context, int status){
        switch (status){
            case 0: return context.getResources().getString(R.string.status_new);
            case 1: return context.getResources().getString(R.string.status_accepted);
            case 2: return context.getResources().getString(R.string.status_making);
            case 3: return context.getResources().getString(R.string.status_ready);
            case 4: return context.getResources().getString(R.string.status_send_to_deliver);
            case 5: return context.getResources().getString(R.string.status_delivered);
            case 6: return context.getResources().getString(R.string.status_taken);
            case -1: return context.getResources().getString(R.string.status_declined);


        }

        return "";
    }


    public static String getTopSizeAsString(Context context,int size){

        switch (size){
            case -1: return context.getResources().getString(R.string.none);
            case 0: return context.getResources().getString(R.string.normal);
            case 1: return context.getResources().getString(R.string.extra);
            default: return context.getResources().getString(R.string.normal);
        }
    }


    public static String getStatusUpdateNotification(Context context, int status, int order_id){

        String notification = "#xx";

        switch (status){
            case 1:

                notification = context.getResources().getString(R.string.status_accepted_text);
                break;
            case 2:
                notification =  context.getResources().getString(R.string.status_making_text);
                break;
            case 3:
                notification =  context.getResources().getString(R.string.status_ready_text);
                break;
            case 4:
                notification =  context.getResources().getString(R.string.status_on_the_way_text);
                break;
            case 5:
                notification =  context.getResources().getString(R.string.status_delivered_text);
                break;
            case 6:
                notification =  context.getResources().getString(R.string.status_taken_text);
                break;
            case -1:
                notification =  context.getResources().getString(R.string.status_declined_text);
                break;
        }

        notification = notification.replace("xx",String.valueOf(order_id));

        return notification;

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static int getSizeString(int size){
        switch (size){
            case 0: return R.string.small;
            case 1: return R.string.medium;
            case 2: return R.string.large;
        }
        return R.string.medium;
    }

}
