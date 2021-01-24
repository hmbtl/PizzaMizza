package az.nms.pizzamizzaforserver.Utitilies;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static SharedPreferences prefs;


    private static final String PREFS_FILENAME = "az.nms.pizzamizza";

    private static final String PREFS_FIRST_RUN = "firstrun";


    private static final String PREFS_LAST_ORDER_ID = "lastorderid";




    public static void init(Context con) {

        if (prefs == null) {
            prefs = con.getSharedPreferences(PREFS_FILENAME, con.MODE_PRIVATE);


            if (prefs.getBoolean(PREFS_FIRST_RUN, true)) {

                prefs.edit().putBoolean(PREFS_FIRST_RUN, false).commit();
            }
        }


    }


    public static int getLastOrderId() {
        return prefs.getInt(PREFS_LAST_ORDER_ID,0);
    }

    public static void setLastOrderId(int lastorderid) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(Settings.PREFS_LAST_ORDER_ID, lastorderid);
        edit.commit();
    }

}
