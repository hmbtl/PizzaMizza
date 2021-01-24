package az.nms.pizzamizza;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import az.nms.pizzamizza.activities.LoadingScreenActivity;
import az.nms.pizzamizza.activities.MyOrdersActivity;
import az.nms.pizzamizza.tools.Utilites;
import az.nms.pizzamizza.tools.JSONParser;
import az.nms.pizzamizza.tools.PizzaMizzaDatabase;

/**
 * Created by anar on 5/11/15.
 */
public class PizzaMizzaService extends Service {

    private final int UPDATE_TIME_PERIOD = 10 * 1000  ;
    private JSONParser jParser = new JSONParser();
    private OrderUpdater orderUpdater;

    private final int TYPE_ORDER = 1;
    private final int TYPE_MESSAGE = 0;

    private PizzaMizzaDatabase db;

    private Thread thread ;


    private Handler orderHandler;

    private Context context;

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

        this.db = new PizzaMizzaDatabase(this);

        this.context = getApplicationContext();

        SharedData.init(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        orderHandler.removeCallbacks(orderUpdater);
    }



    class OrderUpdater implements Runnable
    {
        //run method
        public void run()
        {
            try {


                if(Utilites.isNetworkAvailable(context)) {

                    List<NameValuePair> params = new ArrayList<>();

                    params.add(new BasicNameValuePair("regid", SharedData.getRegId()));

                    JSONObject json = jParser.makeHttpRequest(Constants.MESSAGE_LISTEN_URL, "POST", params);


                    JSONObject notification = json.getJSONObject("notification");


                    Log.d("json notification", json.toString());

                    if (!notification.isNull("statusupdate")) {

                        JSONArray statusUpdates = notification.getJSONArray("statusupdate");

                        for (int i = 0; i < statusUpdates.length(); i++) {
                            JSONObject statusUpdate = statusUpdates.getJSONObject(i);

                            int order_id = statusUpdate.getInt("order_id");
                            int status = statusUpdate.getInt("status");
                            int notify = statusUpdate.getInt("notify");

                            db.updateMyOrder(order_id, status);

                            sendBroadcast(new Intent("ORDER_UPDATES"));


                            if(notify == 1)
                                    showNotification(Utilites.getStatusUpdateNotification(context, status, order_id),TYPE_ORDER,order_id);

                        }
                    }

                    if (!notification.isNull("messages")) {

                        JSONArray messages = notification.getJSONArray("messages");

                        for (int i = 0; i < messages.length(); i++) {
                            JSONObject message = messages.getJSONObject(i);

                            String msg = message.getString("message");


                            showNotification(msg,TYPE_MESSAGE,0);

                        }


                    }
                }

            } catch (Exception te) {
                Log.e(" exception ", "Exception: " + te);
            }


            //delay fetching new updates
            orderHandler.postDelayed(new Runnable() {
                public void run() {
                    new Thread(orderUpdater).start();
                }
            }, UPDATE_TIME_PERIOD);

        }
    }






    public void showNotification(String message,int type, int order_id){

        int notifyID;

        String title;

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notify_sound);

        // intent triggered, you can add other intent for other actions
        Intent intent;
        switch (type){
            case TYPE_MESSAGE:
                intent = new Intent(this, LoadingScreenActivity.class);
                title = "Pizza Mizza";
                notifyID = NotificationID.getID();
                break;
            case TYPE_ORDER :
                intent = new Intent(this, MyOrdersActivity.class);
                title = getResources().getString(R.string.order_status_updated);
                notifyID = order_id;
                break;
            default:
                intent = new Intent(this, LoadingScreenActivity.class);
                title = "Pizza Mizza";
                notifyID = NotificationID.getID();
                break;
        }


        PendingIntent pIntent = PendingIntent.getActivity(this,notifyID, intent, 0);

        Log.d("message", message);

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = new Notification.Builder(this)

                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_pizzamizza)
                .setContentIntent(pIntent)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))

                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotification.flags = Notification.FLAG_AUTO_CANCEL;


        notificationManager.notify(notifyID, mNotification);
    }

    public static class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(0);
        public static int getID() {
            return c.incrementAndGet();
        }
    }

}
