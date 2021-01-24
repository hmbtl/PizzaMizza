package az.nms.pizzamizzaforserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import az.nms.pizzamizzaforserver.Utitilies.Constants;
import az.nms.pizzamizzaforserver.Utitilies.JSONParser;
import az.nms.pizzamizzaforserver.Utitilies.Settings;
import az.nms.pizzamizzaforserver.storage.Customer;
import az.nms.pizzamizzaforserver.storage.Order;
import az.nms.pizzamizzaforserver.storage.PizzaServerDatabase;

/**
 * Created by anar on 5/11/15.
 */
public class OrderListenerService extends Service {

    private final int UPDATE_TIME_PERIOD = 1 * 10 * 1000;
    private JSONParser jParser = new JSONParser();
    private OrderUpdater orderUpdater;
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
    private int numMessages = 0;

    private Thread thread;


    PizzaServerDatabase db;

    private Handler orderHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        orderHandler = new Handler();

        orderUpdater = new OrderUpdater();

        thread = new Thread(orderUpdater);

        thread.start();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Settings.init(getApplicationContext());

        db = new PizzaServerDatabase(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        orderHandler.removeCallbacks(orderUpdater);
    }


    class OrderUpdater implements Runnable {
        //run method
        public void run() {


            int lastOrderId = Settings.getLastOrderId();

            //check for updates - assume none
            boolean statusChanges = false;

            try {


                List<Order> orders = new LinkedList<>();

                List<NameValuePair> params = new ArrayList<>();

                params.add(new BasicNameValuePair("db", "orders"));
                params.add(new BasicNameValuePair("status", "0"));
                params.add(new BasicNameValuePair("last_order_id", String.valueOf(Settings.getLastOrderId())));


                JSONObject json = jParser.makeHttpRequest(Constants.SERVER_GET_URL, "GET", params);

                if (json != null) {

                    // get food array from database

                    int success = json.getInt("success");

                    if (success == 1) {


                        JSONArray jsonOrders = json.getJSONArray("orders");

                        for (int i = 0; i < jsonOrders.length(); i++) {

                            JSONObject jsonOrdersObject = jsonOrders.getJSONObject(i);

                            int order_id = jsonOrdersObject.getInt("order_id");
                            String date_added = jsonOrdersObject.getString("date_added");
                            double total = jsonOrdersObject.getDouble("total");
                            String type = jsonOrdersObject.getString("type");
                            int customer_id = jsonOrdersObject.getInt("customer_id");
                            String customer_name = jsonOrdersObject.getString("name");
                            String customer_phone = jsonOrdersObject.getString("phone");
                            String customer_address = jsonOrdersObject.getString("address");
                            String notes = jsonOrdersObject.getString("notes");
                            String regid = jsonOrdersObject.getString("regid");
                            int status = jsonOrdersObject.getInt("status");
                            String lang = jsonOrdersObject.getString("lang");


                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;
                            try {
                                date = formatter.parse(date_added);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Customer customer = new Customer(customer_id, customer_name, customer_address, customer_phone, date);


                            Order order = new Order(order_id, customer, total, type, date, notes, regid, status,lang);


                            db.addOrder(order);

                            showNotification(order);

                            if (i == 0)
                                lastOrderId = order_id;

                        }
                        statusChanges = true;

                    } else {

                        statusChanges = false;

                    }

                }

            } catch (Exception te) {
                Log.e(" exception ", "Exception: " + te);
            }


            //if we have new updates, send a broadcast
            if (statusChanges) {
                //this should be received in the main timeline class

                Settings.setLastOrderId(lastOrderId);
                sendBroadcast(new Intent("ORDER_UPDATES"));
            }


            //delay fetching new updates
            orderHandler.postDelayed(new Runnable() {
                public void run() {
                    new Thread(orderUpdater).start();
                }
            }, UPDATE_TIME_PERIOD);


        }
    }


    protected void cancelNotification() {
        Log.i("Cancel", "notification");
        mNotificationManager.cancel(notificationID);
    }

    protected void updateNotification() {
        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        mBuilder.setContentTitle("New Order");
        mBuilder.setContentText("You've received new order.");
        mBuilder.setTicker("New Order Alert!");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(true);


        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notify_sound);
        mBuilder.setSound(alarmSound);

      /* Increase notification number every time a new notification arrives */
        mBuilder.setNumber(++numMessages);


      /* Add Big View Specific Configuration */
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String[] events = new String[6];

        int k = 0;

//        for (int i = 0; i < StoredData.orders.size(); i++) {
//
//
//            if (StoredData.orders.get(i).getId() > Settings.getLastOrderId()) {
//
//                Order order = StoredData.orders.get(i);
//
//                String message = order.getType() + " to address " + order.getCustomer().getAddress() + " by " + order.getCustomer().getName() + " ( "
//                        + order.getCustomer().getPhone() + " ) total " + order.getTotalPrice() + " AZN";
//                Log.d("message", message);
//
//                events[k] = message;
//
//                k++;
//            }
//
//
//        }


        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Big Title Details:");
        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {

            inboxStyle.addLine(events[i]);
        }
        mBuilder.setStyle(inboxStyle);


      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID, mBuilder.build());

    }


    public void showNotification(Order order) {


        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notify_sound);


        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(this, OrderDetailActivity.class);

        intent.putExtra("ORDER_ID", order.getId());
        intent.putExtra("CUSTOMER_NAME", order.getCustomer().getName());
        intent.putExtra("CUSTOMER_ADDRESS", order.getCustomer().getAddress());
        intent.putExtra("CUSTOMER_PHONE", order.getCustomer().getPhone());
        intent.putExtra("ORDER_TYPE", order.getType());
        intent.putExtra("ORDER_DATE", order.getDate());
        intent.putExtra("ORDER_TOTAL", order.getTotalPrice());
        intent.putExtra("ORDER_NOTES", order.getNotes());

        intent.putExtra("ORDER_REGID", order.getRegid());
        intent.putExtra("ORDER_STATUS", order.getStatus());
        intent.putExtra("ORDER_LANG", order.getLang());

        PendingIntent pIntent = PendingIntent.getActivity(this, order.getId(), intent, 0);


        String uri = "tel:" + "+994" + order.getCustomer().getPhone();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));

        PendingIntent pIntentCall = PendingIntent.getActivity(this, order.getId(), callIntent, 0);

        String message = "Order #" + order.getId() + " by " + order.getCustomer().getName() + ". Contact " + order.getCustomer().getPhone();
        Log.d("message", message);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle("New Order")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_pizzamizza)
                .setContentIntent(pIntent)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))


                .addAction(0, "Call", pIntentCall)
                .addAction(0, "View", pIntent)

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(order.getId(), mNotification);
    }


}
