package styles.zonetech.net.styles.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;

public class Parser  {

public boolean MessageSent=false;
    String status,action,codeMessage;
    Context mContext;

     ArrayList<Models> messages=new ArrayList<>();

    public ArrayList<Models> getMessages() {
        return messages;
    }

    int code=-1;
    String[] errorcodes;
    ArrayList<Models>services=new ArrayList<>();
    ArrayList<String> gallery=new ArrayList<>();
    ArrayList<String> slides=new ArrayList<>();
    ArrayList<Models>reviews=new ArrayList<>();
    ArrayList<Models> saloons=new ArrayList<>();
    ArrayList<Models> orders=new ArrayList<>();

    public ArrayList<String> getSlides() {
        return slides;
    }

    public ArrayList<Models> getOrders() {
        return orders;
    }

    Models terms=new Models();

    public Models getTerms() {
        return terms;
    }


    public ArrayList<Models> getSaloons() {
        return saloons;
    }

    public ArrayList<Models> getReviews() {
        return reviews;
    }
    public ArrayList<Models> getServices() {
        return services;
    }
    public Parser(Context mContext) {

        this.mContext=mContext;
        initErrorsArray();
    }

    private void initErrorsArray() {
        Resources res = mContext.getResources();
        errorcodes = res.getStringArray(R.array.errorcodes);
    }

    public void parse(String response){

        try {
            JSONArray jsonArray=new JSONArray(response);
            JSONObject jsonObject=jsonArray.getJSONObject(0);

            status=jsonObject.getString("status");
            if(status.equals("error")){
                code=jsonObject.getInt("code");
            }
            else if(status.equals("success")){
                action=jsonObject.getString("action");
               switch (action){


                   case "logintext":
                       JSONArray modelArray=jsonArray.getJSONArray(1);
                       JSONObject userObject=modelArray.getJSONObject(0);
                       getUser(userObject);
                       saveUser();
                       break;

                       case "registertext":
                       int id= (int) jsonArray.get(1);
                       getUserId(id);
                       saveUser();

                       break;

                   case "saloonstext":
                       modelArray=jsonArray.getJSONArray(1);
                       getSaloons(modelArray);
                       break;


                   case "messagestext":
                       modelArray=jsonArray.getJSONArray(1);
                       getMessages(modelArray);
                       break;
                   case "sendtext":
                       MessageSent=true;
                       break;

                   case "updatetext" :
                       break;
                       
                   case "galleriestext":
                       getGalleryUrls(jsonArray);
                       break;

                   case "citiestext":
                       modelArray=jsonArray.getJSONArray(1);
                       getCities(modelArray);
                       break;

                   case "reviewstext":
                       modelArray=jsonArray.getJSONArray(1);
                       getReviews(modelArray);
                       break;

                   case "detailstext":
                       modelArray = jsonArray.getJSONArray(1);
                       getServices(modelArray);
                       break;


                   case "recovertext":
                      action="recovertext";
                       break;

                   case "termstext":
                       JSONObject termsJson = jsonArray.getJSONObject(1);
                       getTerms(termsJson);
                       break;

                   case "orderstext":
                       modelArray=jsonArray.getJSONArray(1);
                       getOrders(modelArray);
                       break;

                   case "ordertext":
                       break;

                   case "slidestext":
                       getSlides(jsonArray);
                       break;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "Parser";
    private void getSlides(JSONArray jsonArray) {
        try {

            for(int i=0;i<jsonArray.length();i++){
                Log.d(TAG, "getSlides: "+jsonArray.length());
                Log.d(TAG, "getSlides: "+jsonArray.getJSONArray(1).get(i).toString());
                slides.add(jsonArray.getJSONArray(1).get(i).toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOrders(JSONArray modelArray) {
        JSONObject orderJson;
        for (int i=0;i<modelArray.length();i++){
            try {
                  Models order=new Models();
                  orderJson=modelArray.getJSONObject(i);

               order.setOrderId(orderJson.getInt("id"));
               order.setOrderSaloonId(orderJson.getString("saloonid"));
               order.setOrderStatus(orderJson.getString("status"));
               order.setOrderSaloonArName(orderJson.getString("arname"));
               order.setOrderSaloonEnName(orderJson.getString("enname"));
               order.setOrderTotal(orderJson.getInt("total"));
               order.setOrderSchedule(orderJson.getString("schedule"));
               order.setOrderAddress(orderJson.getString("address"));
               order.setOrderReasons(orderJson.getString("reasons"));
               order.setOrderItems(orderJson.getString("items"));

               orders.add(order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getTerms(JSONObject termsJson){
        try {
            terms.setTermArabicName(termsJson.getString("ardescription"));
            terms.setTermEnglishName(termsJson.getString("endescription"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getReviews(JSONArray modelArray) {
        for (int i=0;i<modelArray.length();i++){
            Models review=new Models();
            try {
                JSONObject reviewJson=modelArray.getJSONObject(i);
                review.setReviewName(reviewJson.getString("name"));
                review.setReviewRating(reviewJson.getString("rating"));
                review.setReviewDescription(reviewJson.getString("description"));
                review.setReviewDate(reviewJson.getString("date"));
                reviews.add(review);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private  void getCities(JSONArray jsonArray) {
        Common.cities.clear();

        for(int i=0;i<jsonArray.length();i++){
            Models city=new Models();
            try {
                JSONObject cityJson=jsonArray.getJSONObject(i);
                city.setCityid(cityJson.getString("cityid"));
                city.setProvinceid(cityJson.getString("provinceid"));
                city.setCityArName(cityJson.getString("arname"));
                city.setCityEnName(cityJson.getString("enname"));
                Common.cities.add(city);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    public void getGalleryUrls(JSONArray jsonArray) {

        try {

            for(int i=0;i<jsonArray.length();i++){
                gallery.add(jsonArray.getJSONArray(1).get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getGallery() {
        return gallery;
    }

    public String getStatus() {
        return status;
    }

    public String getAction() {
        return action;
    }

    public String getCodeMessage() {

        switch (code){
            case 0:
                codeMessage=errorcodes[code];
                break;
            case 1:
                codeMessage=errorcodes[code];
                break;
            case 2:
                codeMessage=errorcodes[code];
                break;
            case 3:
                codeMessage=errorcodes[code];
                break;
            case 4:
                codeMessage=errorcodes[code];
                break;
            case 5:
                codeMessage=errorcodes[code];
                break;
            case 6:
                codeMessage=errorcodes[code];
                break;
            case 7:
                codeMessage=errorcodes[code];
            case 8:
                codeMessage=errorcodes[code];
                break;
        }
        return codeMessage;
    }

    private void getUser(JSONObject userObject){
        Models user=new Models();
        try {
            user.setUserId(userObject.getInt("id"));
            user.setUserName(userObject.getString("username"));
            user.setUserPhone(userObject.getString("userphone"));
            user.setUserEmail(userObject.getString("useremail"));
            user.setUserPassword(userObject.getString("password"));
            Common.currentUser=user;
            if(Common.FacebookProfileImageUrl==null)
            user.setUserProfileImage(Common.userImageUrl+Common.currentUser.getUserId()+".jpg");
            else user.setUserProfileImage(Common.FacebookProfileImageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getUserId(int id) {

            Common.currentUser.setUserId(id);
    }

    private void getSaloons(JSONArray jsonArray){
        Models models;
        JSONObject jsonObject;
        for(int i=0;i<jsonArray.length();i++){
            try {

                jsonObject=jsonArray.getJSONObject(i);
                models=new Models();
                models.setSaloonId(jsonObject.getInt("id"));
                models.setSaloonArName(jsonObject.getString("arname"));
                models.setSaloonEnName(jsonObject.getString("enname"));
                models.setHouse(jsonObject.getInt("house"));
                models.setCredit(jsonObject.getInt("credit"));
                models.setLatitude(jsonObject.getString("latitude"));
                models.setLongitude(jsonObject.getString("longitude"));
                models.setSaloonRating(jsonObject.getDouble("rating"));
                models.setSaloonProvince(jsonObject.getString("provinceid"));
                models.setSaloonCity(jsonObject.getString("cityid"));
                models.setSaloonUserName(jsonObject.getString("username"));
                models.setSaloonPhone(jsonObject.getString("userphone"));
                models.setSaloonEmail(jsonObject.getString("useremail"));
                models.setSaloonOffers(jsonObject.getString("offers"));
                models.setSaloonCategory(jsonObject.getString("categoryid"));
                models.setSaloonSubCategory(jsonObject.getString("subcategoryid"));
                saloons.add(models);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUser() {
        
       SharedPreferences userPerf= mContext.getSharedPreferences(mContext.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=userPerf.edit();
        Gson gson = new Gson();
        if(Common.currentUser!=null){
            String user = gson.toJson(Common.currentUser);
            editor.putString(Common.CURRENT_USER,user);
            editor.apply();

        }
        else
        {

        }
       

    }


    private void getMessages(JSONArray jsonArray){

        Models models;
        JSONObject jsonObject;
        for(int i=0;i<jsonArray.length();i++){
            try {
                jsonObject=jsonArray.getJSONObject(i);
                models=new Models();
                models.setMessageId(jsonObject.getString("id"));
                models.setMessageSender(jsonObject.getString("sender"));
                models.setMessageDescription(jsonObject.getString("description"));
                models.setMessageTime(jsonObject.getString("time"));
               messages.add(models);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void getServices(JSONArray modelArray) {

        for(int i=0;i<modelArray.length();i++){
            try {
                Models service=new Models();
                JSONObject jsonObject=modelArray.getJSONObject(i);
                service.setServiceType(jsonObject.getString("serviceid"));
                service.setServicePersonType(jsonObject.getString("child"));
                service.setServicePrice(jsonObject.getInt("value"));
                service.setServiceNameEn(jsonObject.getString("enname"));
                service.setOfferNamesEn(jsonObject.getString("ennames"));
                service.setServiceNameAr(jsonObject.getString("arname"));
                service.setOfferNamesAr(jsonObject.getString("arnames"));
                service.setServiceId(jsonObject.getString("id"));
                services.add(service);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
