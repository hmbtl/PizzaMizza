package az.nms.pizzamizza;

/**
 * Created by anar on 5/16/15.
 */
public final class Constants {

    public static final String SERVER_URL = "http://95.85.52.43:30291" ;
    public static final String SERVER_GET = "http://95.85.52.43:30291/get.php";

    public static final String SERVER_IMAGE_URL = SERVER_URL + "/images/";

    public static final String SERVER_SET = "http://95.85.52.43:30291/set.php";

    public static final String STORE_OPEN = SERVER_URL + "/store_open.php";

    public static final String REGISTER_URL = SERVER_URL + "/register.php";
    public static final String  qMESSAGE_LISTEN_URL = SERVER_URL + "/message.php";


    public static final int STATUS_NEW = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_MAKING = 2;
    public static final int STATUS_READY = 3;
    public static final int STATUS_DELIVERING = 4;
    public static final int STATUS_DELIVERED = 5;
    public static final int STATUS_TAKEN = 6;
    public static final int STATUS_DECLINED = -1;


}
