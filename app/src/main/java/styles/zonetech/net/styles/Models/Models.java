package styles.zonetech.net.styles.Models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Models implements Parcelable {


    public Models() {
    }

    protected Models(Parcel in) {
        house = in.readInt();
        credit = in.readInt();
        saloonId = in.readInt();
        saloonUserName = in.readString();
        saloonPhone = in.readString();
        saloonEmail = in.readString();
        saloonArName = in.readString();
        saloonEnName = in.readString();
        saloonOffers = in.readString();
        saloonCategory = in.readString();
        saloonSubCategory = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        saloonProvince = in.readString();
        saloonCity = in.readString();
        saloonRating = in.readDouble();
        userId = in.readInt();
        userToken = in.readString();
        userName = in.readString();
        userPhone = in.readString();
        userEmail = in.readString();
        userPassword = in.readString();
        userProfileImage = in.readString();
        messageId = in.readString();
        messageSender = in.readString();
        messageDescription = in.readString();
        messageTime = in.readString();
        seen = in.readInt();
        ServiceId = in.readString();
        serviceType = in.readString();
        serviceNameEn = in.readString();
        serviceNameAr = in.readString();
        servicePrice = in.readInt();
        servicePersonType = in.readString();
        offerNamesEn = in.readString();
        offerNamesAr = in.readString();
        serviceStatus = in.readByte() != 0;
        ratingDescription = in.readString();
        ratingDate = in.readString();
        ratingTxt = in.readString();
        rating = in.readFloat();
        location = in.readString();
        menuName = in.readString();
        iconName = in.readString();
        orderPersonType = in.readString();
        orderServices = in.createStringArrayList();
        orderPrice = in.readFloat();
        OrderId = in.readInt();
        offersPrice = in.readFloat();
        servicesPrice = in.readFloat();
        formattedStrigForOrderConfirmation = in.readString();
        inHome = in.readByte() != 0;
        homePrice = in.readFloat();
        totalPrice = in.readFloat();
        isTotal = in.readByte() != 0;
        cityid = in.readString();
        provinceid = in.readString();
        cityArName = in.readString();
        cityEnName = in.readString();
        reviewName = in.readString();
        reviewRating = in.readString();
        reviewDescription = in.readString();
        reviewDate = in.readString();
    }

    public static final Creator<Models> CREATOR = new Creator<Models>() {
        @Override
        public Models createFromParcel(Parcel in) {
            return new Models(in);
        }

        @Override
        public Models[] newArray(int size) {
            return new Models[size];
        }
    };



    //***********SALOON MODEL************
    int house, credit, saloonId;
    String saloonUserName, saloonPhone, saloonEmail;
    String saloonArName, saloonEnName;
    String saloonOffers;
    String saloonCategory, saloonSubCategory;
    String latitude, longitude, saloonProvince, saloonCity;
    double saloonRating;

    public String getSaloonUserName() {
        return saloonUserName;
    }

    public void setSaloonUserName(String saloonUserName) {
        this.saloonUserName = saloonUserName;
    }

    public String getSaloonPhone() {
        return saloonPhone;
    }

    public void setSaloonPhone(String saloonPhone) {
        this.saloonPhone = saloonPhone;
    }

    public String getSaloonEmail() {
        return saloonEmail;
    }

    public void setSaloonEmail(String saloonEmail) {
        this.saloonEmail = saloonEmail;
    }

    public boolean isSaloonHasOffers() {
        String offers = getSaloonOffers();
        if (offers.equals("0")) {
            return false;
        } else return true;
    }


    public String getSaloonOffers() {
        return saloonOffers;
    }

    public void setSaloonOffers(String saloonOffers) {
        this.saloonOffers = saloonOffers;
    }

    public String getSaloonCategory() {
        return saloonCategory;
    }

    public void setSaloonCategory(String saloonCategory) {
        this.saloonCategory = saloonCategory;
    }

    public String getSaloonSubCategory() {
        return saloonSubCategory;
    }

    public void setSaloonSubCategory(String saloonSubCategory) {
        this.saloonSubCategory = saloonSubCategory;
    }

    public String getSaloonProvince() {
        return saloonProvince;
    }

    public void setSaloonProvince(String saloonProvince) {
        this.saloonProvince = saloonProvince;
    }

    public String getSaloonCity() {
        return saloonCity;
    }

    public void setSaloonCity(String saloonCity) {
        this.saloonCity = saloonCity;
    }

    public int getSaloonId() {
        return saloonId;
    }

    public double getSaloonRating() {
        return saloonRating;
    }

    public void setSaloonRating(double saloonRating) {
        this.saloonRating = saloonRating;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setSaloonId(int saloonId) {
        this.saloonId = saloonId;
    }


    public int getHouse() {
        return house;
    }

    public void setHouse(int house) {
        this.house = house;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getSaloonArName() {
        return saloonArName;
    }

    public void setSaloonArName(String saloonArName) {
        this.saloonArName = saloonArName;
    }

    public String getSaloonEnName() {
        return saloonEnName;
    }

    public void setSaloonEnName(String saloonEnName) {
        this.saloonEnName = saloonEnName;
    }


    //**************USER MODEL**************
    int userId;
    String userToken, userName, userPhone, userEmail, userPassword;
    String userProfileImage,userFacebookProfileImage=null;

    public String getUserFacebookProfileImage() {
        return userFacebookProfileImage;
    }

    public void setUserFacebookProfileImage(String userFacebookProfileImage) {
        this.userFacebookProfileImage = userFacebookProfileImage;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    //**************** MESSAGE MODEL************************

    String messageId, messageSender, messageDescription;
    String messageTime;
    int seen;

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public boolean isSender() {
        boolean isSender = false;
        if (getMessageSender().equals("user")) {
            isSender = true;
        } else {
            isSender = false;
        }
        return isSender;
    }


    //********************** SERVICE MODEL**********************

    String  ServiceId;
    String serviceType, serviceNameEn,serviceNameAr;

    int servicePrice;
    String servicePersonType;
    String offerNamesEn,offerNamesAr;
    boolean serviceStatus;

    public String getServiceNameAr() {
        return serviceNameAr;
    }

    public void setServiceNameAr(String serviceNameAr) {
        this.serviceNameAr = serviceNameAr;
    }

    public String getOfferNamesAr() {
        return offerNamesAr;
    }

    public void setOfferNamesAr(String offerNamesAr) {
        this.offerNamesAr = offerNamesAr;
    }

    public boolean isServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(boolean serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String serviceId) {
        ServiceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceNameEn() {
        return serviceNameEn;
    }

    public void setServiceNameEn(String serviceNameEn) {
        this.serviceNameEn = serviceNameEn;
    }

    public int getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServicePersonType() {
        return servicePersonType;
    }

    public void setServicePersonType(String servicePersonType) {
        this.servicePersonType = servicePersonType;
    }

    public String getOfferNamesEn() {
        return offerNamesEn;
    }

    public void setOfferNamesEn(String offerNamesEn) {
        this.offerNamesEn = offerNamesEn;
    }

    /**************Rating  MODEL********************/

    public String ratingDescription, ratingDate, ratingTxt;
    public float rating;

    public Models(String ratingTxt, String date, String description, float rating) {
        this.ratingDescription = description;
        this.ratingDate = date;
        this.ratingTxt = ratingTxt;
        this.rating = rating;

    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }

    public String getRatingTxt() {
        return ratingTxt;
    }

    public void setRatingTxt(String ratingTxt) {
        this.ratingTxt = ratingTxt;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    /*********************************************/

    /********************************************LOCATION MODEL ********************************************************************************/
    String location;

    public Models(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//****************************************************************MENU MODEL *******************************************************************

    String menuName, iconName;

    public Models(String menuName, String iconName) {
        this.menuName = menuName;
        this.iconName = iconName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }


    //****************************************************************ORDER DETAILS*******************************************************************


    String orderPersonType;
    ArrayList<String> orderServices=new ArrayList<>();
    HashMap<String,String> orderOffers=new HashMap<>();
    float orderPrice;
    int OrderId;
    float offersPrice,servicesPrice;
   String formattedStrigForOrderConfirmation="";
   String formattedPriceString="";

    public String getFormattedPriceString() {
        return formattedPriceString;
    }

    public void setFormattedPriceString(String formattedPriceString) {
        this.formattedPriceString = formattedPriceString;
    }

    public String getFormattedStrigForOrderConfirmation() {
        return formattedStrigForOrderConfirmation;
    }

    public void setFormattedStrigForOrderConfirmation(String formattedStrigForOrderConfirmation) {
        this.formattedStrigForOrderConfirmation = formattedStrigForOrderConfirmation;
    }

    public float getOffersPrice() {
        return offersPrice;
    }

    public void setOffersPrice(float offersPrice) {
        this.offersPrice = offersPrice;
    }

    public float getServicesPrice() {
        return servicesPrice;
    }

    public void setServicesPrice(float servicesPrice) {
        this.servicesPrice = servicesPrice;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getOrderPersonType() {
        return orderPersonType;
    }

    public void setOrderPersonType(String orderPersonType) {
        this.orderPersonType = orderPersonType;
    }

    public ArrayList<String> getOrderServices() {
        return orderServices;
    }

    public void setOrderServices(ArrayList<String> orderServices) {
        this.orderServices = orderServices;
    }

    public HashMap<String, String> getOrderOffers() {
        return orderOffers;
    }

    public void setOrderOffers(HashMap<String, String> orderOffers) {
        this.orderOffers = orderOffers;
    }

    public float getOrderPrice() {
        return offersPrice+servicesPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }
    //******************************************************************* HOME MODEL*******************************************************************
    public boolean inHome;
    public float homePrice;
    public float totalPrice;
    public boolean isTotal;

    public Models(boolean inHome, float homePrice) {
        this.inHome = inHome;
        this.homePrice = homePrice;
    }

    public Models(float totalPrice, boolean isTotal) {
        this.totalPrice = totalPrice;
        this.isTotal = isTotal;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public void setTotal(boolean total) {
        isTotal = total;
    }

    public boolean isInHome() {
        return inHome;
    }

    public void setInHome(boolean inHome) {
        this.inHome = inHome;
    }

    public float getHomePrice() {
        return homePrice;
    }

    public void setHomePrice(float homePrice) {
        this.homePrice = homePrice;
    }


    /************************************************************************CITIES MODEL***************************************************************************/
    String cityid, provinceid;
    String cityArName, cityEnName;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityArName() {
        return cityArName;
    }

    public void setCityArName(String cityArName) {
        this.cityArName = cityArName;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

/*********************************************************************************** REVIEWS MODEL************************************************************************/
    String reviewName,reviewRating,reviewDescription,reviewDate;

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(String reviewRating) {
        this.reviewRating = reviewRating;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(house);
        dest.writeInt(credit);
        dest.writeInt(saloonId);
        dest.writeString(saloonUserName);
        dest.writeString(saloonPhone);
        dest.writeString(saloonEmail);
        dest.writeString(saloonArName);
        dest.writeString(saloonEnName);
        dest.writeString(saloonOffers);
        dest.writeString(saloonCategory);
        dest.writeString(saloonSubCategory);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(saloonProvince);
        dest.writeString(saloonCity);
        dest.writeDouble(saloonRating);
        dest.writeInt(userId);
        dest.writeString(userToken);
        dest.writeString(userName);
        dest.writeString(userPhone);
        dest.writeString(userEmail);
        dest.writeString(userPassword);
        dest.writeString(userProfileImage);
        dest.writeString(messageId);
        dest.writeString(messageSender);
        dest.writeString(messageDescription);
        dest.writeString(messageTime);
        dest.writeInt(seen);
        dest.writeString(ServiceId);
        dest.writeString(serviceType);
        dest.writeString(serviceNameEn);
        dest.writeString(serviceNameAr);
        dest.writeInt(servicePrice);
        dest.writeString(servicePersonType);
        dest.writeString(offerNamesEn);
        dest.writeString(offerNamesAr);
        dest.writeByte((byte) (serviceStatus ? 1 : 0));
        dest.writeString(ratingDescription);
        dest.writeString(ratingDate);
        dest.writeString(ratingTxt);
        dest.writeFloat(rating);
        dest.writeString(location);
        dest.writeString(menuName);
        dest.writeString(iconName);
        dest.writeString(orderPersonType);
        dest.writeStringList(orderServices);
        dest.writeFloat(orderPrice);
        dest.writeInt(OrderId);
        dest.writeFloat(offersPrice);
        dest.writeFloat(servicesPrice);
        dest.writeString(formattedStrigForOrderConfirmation);
        dest.writeByte((byte) (inHome ? 1 : 0));
        dest.writeFloat(homePrice);
        dest.writeFloat(totalPrice);
        dest.writeByte((byte) (isTotal ? 1 : 0));
        dest.writeString(cityid);
        dest.writeString(provinceid);
        dest.writeString(cityArName);
        dest.writeString(cityEnName);
        dest.writeString(reviewName);
        dest.writeString(reviewRating);
        dest.writeString(reviewDescription);
        dest.writeString(reviewDate);
    }

    //*************************************************************TERMS MODELS*******************************************************
    String TermArabicName;
    String TermEnglishName;

    public String getTermArabicName() {
        return TermArabicName;
    }

    public void setTermArabicName(String termArabicName) {
        TermArabicName = termArabicName;
    }

    public String getTermEnglishName() {
        return TermEnglishName;
    }

    public void setTermEnglishName(String termEnglishName) {
        TermEnglishName = termEnglishName;
    }

//**********************************************************ORDER MODEL****************************************************
    String orderId,orderSaloonId,orderStatus,orderSaloonEnName,orderSaloonArName;
    int orderTotal;
    String orderSchedule,orderReasons,orderAddress,orderItems;

    public String getOrderAddress() {
        return orderAddress;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSaloonId() {
        return orderSaloonId;
    }

    public void setOrderSaloonId(String orderSaloonId) {
        this.orderSaloonId = orderSaloonId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderSaloonEnName() {
        return orderSaloonEnName;
    }

    public void setOrderSaloonEnName(String orderSaloonEnName) {
        this.orderSaloonEnName = orderSaloonEnName;
    }

    public String getOrderSaloonArName() {
        return orderSaloonArName;
    }

    public void setOrderSaloonArName(String orderSaloonArName) {
        this.orderSaloonArName = orderSaloonArName;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderSchedule() {
        return orderSchedule;
    }

    public void setOrderSchedule(String orderSchedule) {
        this.orderSchedule = orderSchedule;
    }

    public String getOrderReasons() {
        return orderReasons;
    }

    public void setOrderReasons(String orderReasons) {
        this.orderReasons = orderReasons;
    }





    //**********************************************ORDER ACTIVITY DETAILS************************************************************
    String orderDetailsTotal,getOrderDetailsPerson,orderDetailsServices;

    public String getOrderDetailsTotal() {
        return orderDetailsTotal;
    }

    public void setOrderDetailsTotal(String orderDetailsTotal) {
        this.orderDetailsTotal = orderDetailsTotal;
    }

    public String getGetOrderDetailsPerson() {
        return getOrderDetailsPerson;
    }

    public void setGetOrderDetailsPerson(String getOrderDetailsPerson) {
        this.getOrderDetailsPerson = getOrderDetailsPerson;
    }

    public String getOrderDetailsServices() {
        return orderDetailsServices;
    }

    public void setOrderDetailsServices(String orderDetailsServices) {
        this.orderDetailsServices = orderDetailsServices;
    }
}