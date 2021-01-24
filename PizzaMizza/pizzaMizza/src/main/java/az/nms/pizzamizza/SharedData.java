package az.nms.pizzamizza;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedData {

    private static SharedPreferences prefs;


    private static String regid;

    public static int  timestamp;

    private static final String PREFS_FILENAME = "az.nms.pizzamizza";

    private static final String PREFS_TIMESTAMP = "timestamp";

    private static final String PREFS_LANGUAGE = "language";

    private static final String PREFS_FIRST_RUN = "firstrun";

    private static final String PREFS_ZONE_ID = "zoneid";

    private static final String PREFS_STREET_NAME = "street";

    private static final String PREFS_APT_NUMBER = "apartment";

    private static final String PREFS_REG_ID = "userregid";

    private static final String PREFS_SAVE_DETAILS = "savedetails";


    private static final String PREFS_STORE_OPEN = "storeopen";
    private static final String PREFS_LAST_UPDATE_DATE = "lastupdatedate";





    private static  Context context;


    public static void init(Context con) {

        String currentLanguage = con.getResources().getConfiguration().locale.getLanguage();
        Log.d("currentLang", currentLanguage);
        SharedData.context = con;

        if (prefs == null) {
            prefs = con.getSharedPreferences(PREFS_FILENAME, con.MODE_PRIVATE);

            timestamp = prefs.getInt(PREFS_TIMESTAMP, 0);
        }


    }

    public static boolean isFirstRun(){
        return prefs.getBoolean(SharedData.PREFS_FIRST_RUN,true);
    }

    public static void setFirstRunFalse(){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(SharedData.PREFS_FIRST_RUN, false);
        edit.commit();
    }


    public static void setLanguage(String lang) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_LANGUAGE, lang);
        edit.commit();
    }

    public static String getLanguage() {
        return prefs.getString(SharedData.PREFS_LANGUAGE, "az");
    }

    public static void setSaveDetails(boolean save) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(SharedData.PREFS_SAVE_DETAILS, save);
        edit.commit();
    }

    public static boolean getSaveDetails() {
        return prefs.getBoolean(SharedData.PREFS_SAVE_DETAILS, false);
    }



    public static String getStreetName() {
        return prefs.getString(PREFS_STREET_NAME, "");
    }

    public static void setStreetName(String streetName) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_STREET_NAME, streetName);
        edit.commit();
    }

    public static int getZoneId() {
        return prefs.getInt(PREFS_ZONE_ID, 0);
    }

    public static void setZoneId(int zoneId) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(SharedData.PREFS_ZONE_ID, zoneId);
        edit.commit();
    }

    public static int getStoreOpen() {
        return prefs.getInt(PREFS_STORE_OPEN, 0);
    }

    public static void setStoreOpen(int storeOpen) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(SharedData.PREFS_STORE_OPEN, storeOpen);
        edit.commit();
    }


    public static String getAptNumber() {
        return prefs.getString(PREFS_APT_NUMBER, "");
    }

    public static void setAptNumber(String aptNumber) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_APT_NUMBER, aptNumber);
        edit.commit();
    }

    public static String getLastUpdateDate() {
        return prefs.getString(PREFS_LAST_UPDATE_DATE, "0");
    }

    public static void setLastUpdateDate(String lastUpdateDate) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_LAST_UPDATE_DATE, lastUpdateDate);
        edit.commit();
    }



    public static String getRegId() {
        return prefs.getString(PREFS_REG_ID, "0");
    }

    public static void setRegId(String regId) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_REG_ID, regId);
        edit.commit();
    }



}
