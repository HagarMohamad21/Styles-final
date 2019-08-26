package styles.zonetech.net.styles.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import styles.zonetech.net.styles.Models.Models;

import styles.zonetech.net.styles.Server.Client;
import styles.zonetech.net.styles.Server.IServer;


public class Common {
    public static final String CURRENT_USER = "CURRENT_USER";

    public static int ACTION_REQUEST;


public static final int MenuItemHome=0;
public static final int MenuItemOrders=1;
public static final int MenuItemAccount=2;
public static final int MenuItemMessages=3;
public static final int MenuItemAbout=4;
public static final int MenuItemTerms=5;


public static  String profileImagePath=null;
public static float totalPrice=0;
 public static int childCount=0;
    public static int adultCount=0;
    public static final int DIALOG_LAYOUT_TYPE_MENU=10;
    public static final int DIALOG_LAYOUT_TYPE_CONFIRM_ORDER=8;
    public static final int DIALOG_LAYOUT_TYPE_SERVICE=1;
    public static final int DIALOG_LAYOUT_TYPE_OFFER=2;
    public static final int DIALOG_LAYOUT_TYPE_CITY=3;
    public static final int DIALOG_LAYOUT_TYPE_RATING=5;
    public static final int DIALOG_LAYOUT_TYPE_ORDER_DETAILS=6;
    public static final int DIALOG_LAYOUT_TYPE_ORDER_CANCEL=7;
    public static final int DIALOG_LAYOUT_TYPE_ORDER_REJECTION=8;
  public static final int   DIALOG_LAYOUT_TYPE_PROVINCE =4;
    public static final int DIALOG_LIST_DEFAULT_SIZE=4;
    public static final int DIALOG_LAYOUT_IMAGE=9;
    public static final int DIALOG_LAYOUT_LOGIN=11;
  public static final int DIALOG_LAYOUT_LOGOUT = 12;
    public static int checkedPosition = -1;

  public static ArrayList<Models>orderDetailModels=new ArrayList<>();
    public static Models currentUser=null;
    public static String domainName="https://www.stylesapps.com/app/";
    public static String galleryImagesUrl="https://www.stylesapps.com/photos/galleries/";
    public static String saloonImagesUrl="https://www.stylesapps.com/photos/saloons/";
    public static String userImageUrl="https://www.stylesapps.com/photos/users/";
    public static Models currentSaloon=null;
  public static String FacebookProfileImageUrl=null;
    public static ArrayList<Models> cities=new ArrayList<>();

    public static IServer getAPI(){
        return Client.getClient(domainName).create(IServer.class);
    }



    //***************************************************** keys to Bundles************************************
    public final static String EXTRA_DATE_MESSAGE = "date";
    public final static String EXTRA_TIME_MESSAGE = "time";
    public final static String EXTRA_ORDER_ARRAY_MESSAGE = "order";
    public final static String EXTRA_CALLING_ACTIVITY_MESSAGE = "activity";

//*****************************************************SOME SHARED DATA****************************************
public final static String EXTRA_CALLING_ACTIVITY_CONFIRM_ORDER ="ConfirmOrderActivity";
public final static String EXTRA_CALLING_ACTIVITY_MESSAGES ="MessagesActivity";
public final static String EXTRA_CALLING_ACTIVITY_USER_ACCOUNT ="UserAccountActivity";
public final static String EXTRA_CALLING_ACTIVITY_ORDERS ="OrdersActivity";


public static HashMap<String,Integer> servicesPrices=new HashMap<>();

}
