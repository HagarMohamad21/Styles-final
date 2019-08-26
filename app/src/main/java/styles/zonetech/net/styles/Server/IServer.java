package styles.zonetech.net.styles.Server;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IServer {

     @FormUrlEncoded
     @POST("saloons")
     Call<String> getSaloonsByArea(@Field("type") String type,
                            @Field("categoryid") String categoryid,
                            @Field("cityid") String cityid,
                            @Field("subcategoryid") String subCategory);


     @FormUrlEncoded
     @POST("saloons")
     Call<String> getSaloonsByMap(@Field("type") String type,
                            @Field("categoryid") String categoryid,
                            @Field("latitude") String latitude,
                            @Field("longitude") String longitude,
                            @Field("subcategoryid") String subCategory

     );

     @POST("cities")
     Call<String>getCities();



     @FormUrlEncoded
     @POST("search")
     Call<String> search(@Field("keyword") String keyword);


     @FormUrlEncoded
     @POST("prices")
     Call<String> getServices(@Field("saloonid") String saloonid);


     @FormUrlEncoded
     @POST("reviews")
     Call<String> getReviews(@Field("saloonid") String saloonid);

     @FormUrlEncoded
     @POST("order")
     Call<String> sendOrder(   @Field("userid") String userid,
                               @Field("saloonid") String saloonid,
                               @Field("total") String total,
                               @Field("schedule") String schedule,
                               @Field("items") String items,
                               @Field("credit")String credit,
                               @Field("house")String house,
                               @Field("address") String address,
                               @Field("couponid") String couponid
                              );


      @FormUrlEncoded
      @POST("items")
      Call<String> getItems(@Field("orderid") String orderid);


      @FormUrlEncoded
      @POST("cancel")
      Call<String> cancelOrder(@Field("orderid") String orderid);


      @FormUrlEncoded
      @POST("review")
      Call<String> sendReview(@Field("userid") String userid,
                               @Field("saloonid") String saloonid,
                               @Field("rating") String rating,
                               @Field("description") String description);



      @FormUrlEncoded
      @POST("messages")
      Call<String> getMessages(@Field("userid") String userid);


      @FormUrlEncoded
      @POST("send")
      Call<String> sendMessage(@Field("userid") String userid,
                               @Field("description") String description);

      @FormUrlEncoded
      @POST("login")
      Call<String>login(@Field("userdevice") String userdevice,
                        @Field("usertoken") String usertoken,
                        @Field("useremail") String email,
                        @Field("password") String password);
      @FormUrlEncoded
      @POST("register")
      Call<String>register(@Field("userdevice") String userdevice,
                           @Field("usertoken") String usertoken,
                           @Field("username") String username,
                           @Field("useremail") String useremail,
                           @Field("userphone") String userphone,
                           @Field("password") String password);
      @FormUrlEncoded
    @POST("galleries")
    Call<String>getGallery( @Field("saloonid") String saloonid);



    @Multipart
    @POST("register")
    Call<String>registerWithImage(@Part("userdevice") RequestBody email,
                                  @Part("usertoken") RequestBody usertoken,
                                  @Part("username") RequestBody username,
                                  @Part("useremail") RequestBody useremail,
                                  @Part("userphone") RequestBody userphone,
                                  @Part("password") RequestBody password
                                  ,@Part MultipartBody.Part file);


    @Multipart
    @POST("account")
    Call<String>editAccountWithImage(@Part("userid") RequestBody userid,
                                  @Part("username") RequestBody username,
                                  @Part("useremail") RequestBody useremail,
                                  @Part("userphone") RequestBody userphone,
                                  @Part("password") RequestBody password
                                  ,@Part MultipartBody.Part file);

     @FormUrlEncoded
    @POST("account")
    Call<String>editAccount      (@Field("userid") String userid,
                                  @Field("username") String username,
                                  @Field("useremail") String useremail,
                                  @Field("userphone") String userphone,
                                  @Field("password") String password);

  @FormUrlEncoded
    @POST("recover")
    Call<String>recoverPassword  (@Field("useremail") String useremail,@Field("language")String language);



    @POST("terms")
    Call<String>terms();


    @FormUrlEncoded
    @POST("orders")
    Call<String>getOrders  (@Field("userid") String userid);





}
