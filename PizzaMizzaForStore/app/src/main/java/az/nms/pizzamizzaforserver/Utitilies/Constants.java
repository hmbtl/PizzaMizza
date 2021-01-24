package az.nms.pizzamizzaforserver.Utitilies;

import java.text.SimpleDateFormat;

/**
 * Created by anar on 5/17/15.
 */
public final class Constants {

    public static final String SERVER_URL = "http://95.85.52.43:30291";
    public static final String IMAGES_URL = "http://95.85.52.43:30291/images/";

    public static final String SERVER_SET_URL = SERVER_URL + "/set.php";
    public static final String SERVER_GET_URL = SERVER_URL + "/get.php";

    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static final int STATUS_NEW = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_MAKING = 2;
    public static final int STATUS_READY = 3;
    public static final int STATUS_DELIVERING = 4;
    public static final int STATUS_DELIVERED = 5;
    public static final int STATUS_TAKEN = 6;
    public static final int STATUS_DECLINED = -1;

}