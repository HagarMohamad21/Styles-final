package styles.zonetech.net.styles.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import styles.zonetech.net.styles.Adapters.OrderDetailsAdapter;
import styles.zonetech.net.styles.Adapters.RoundedCornersTransformation;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Listeners.EditOrderCallback;
import styles.zonetech.net.styles.Listeners.ChainedCallbackForHairdresserTotal;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;
import styles.zonetech.net.styles.Utils.RatingConfiguration;
import styles.zonetech.net.styles.Utils.RoundedDrawable;



public class HairDresserActivity extends AppCompatActivity implements ChainedCallbackForHairdresserTotal {
    RecyclerView orderDetailsList;
    Context mContext=HairDresserActivity.this;
    ArrayList<Models> orders=new ArrayList<>();
    TextView galleryBtn,ratingBtn,locationBtn,ratingBar;
    RelativeLayout submitBtn;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt ,addAdultBtn,removeAdultBtn,removeChildBtn,addChildBtn,adultsNumTxt,childrenNumTxt,totalPriceTxt,nameTxt;
    ImageView profileImg;
    RoundedDrawable roundedDrawable;
    float density;
    private CommonMethods commonMethods;
    EditOrderCallback editOrderCallback;
    int childCount=0,adultCount=0;
    int maxAdultNum=10,minAdultNum=0;
    boolean hasBackground=false;
    EditTextValidator validator;
    int Count=0;
    float total=0;
    Bitmap placeHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.servicesPrices.clear();
        Common.orderDetailModels.clear();
        setContentView(R.layout.activity_hair_dresser);
        commonMethods = new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        Common.childCount = 0;
        Common.adultCount = 0;
        placeHolder=BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.placeholder);
        validator = new EditTextValidator(mContext);
        density = mContext.getResources().getDisplayMetrics().density;
        Count = 0;
        initViews();
        populateShopList();
        setListeners();
        initData();

    }



    public void setEditOrderCallback(EditOrderCallback editOrderCallback) {
        this.editOrderCallback = editOrderCallback;
    }

    private void setListeners() {
        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartActivity(mContext,new RatingActivity());
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            Count=0;
                if(Common.orderDetailModels.size()==0){
                    validator.ShowToast(getString(R.string.validation_string));
                }
                else{
                    for(int i=0;i<Common.orderDetailModels.size();i++){

                        if(Common.orderDetailModels.get(i).getOrderOffers().size()>0&&
                                Common.orderDetailModels.get(i).getOrderServices().size()>0){
                            Count++;
                        }
                        else if(Common.orderDetailModels.get(i).getOrderOffers().size()>0&&
                                Common.orderDetailModels.get(i).getOrderServices().size()==0){
                            Count++;
                        }
                        else if(Common.orderDetailModels.get(i).getOrderOffers().size()==0&&
                                Common.orderDetailModels.get(i).getOrderServices().size()>0){
                            Count++;
                        }
else{



}

                    }
                    if(Count==Common.orderDetailModels.size()){
                        //then all users have at least one service or one offer
                        mStartActivity(mContext,new ConfirmOrderActivity());
                    }
                    else{
                        validator.ShowToast(getString(R.string.order_validation));

                    }
                }


            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartActivity(mContext,new GalleryActivity());
            }
        });


        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonMethods.showMenu();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Common.orderDetailModels.clear();
                finish();
            }
        });

        addChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasBackground){
                    hasBackground=true;
                    orderDetailsList.setBackground(ContextCompat.getDrawable(mContext,R.drawable.order_details_list));
                }
                if(!(childCount>=maxAdultNum)){
                    childCount++;
                    Common.childCount++;
                    if(editOrderCallback!=null)
                        editOrderCallback.addChild(childCount);


                }


                childrenNumTxt.setText(String.valueOf(childCount));

            }
        });

        addAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!hasBackground){
                    hasBackground=true;
                    orderDetailsList.setBackground(ContextCompat.getDrawable(mContext,R.drawable.order_details_list));
                }
                if(!(adultCount>=maxAdultNum)){
                    adultCount++;
                    Common.adultCount++;
                    if(editOrderCallback!=null)
                        editOrderCallback.addAdult(adultCount);

                }

                adultsNumTxt.setText(String.valueOf(adultCount));

            }
        });

        removeChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childCount!=minAdultNum){
                    Common.childCount--;
                    childCount--;
                    if(editOrderCallback!=null)
                        editOrderCallback.removeChild(childCount);

                }
                childrenNumTxt.setText(String.valueOf(childCount));



            }
        });


        removeAdultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adultCount!=minAdultNum){
                    Common.adultCount--;
                    adultCount--;
                    if(editOrderCallback!=null)
                        editOrderCallback.removeAdult(adultCount);

                }


                adultsNumTxt.setText(String.valueOf(adultCount));



            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open map
                if(Common.currentSaloon!=null){
                    //String uri = String.format(Locale.ENGLISH, "geo:latitude,longitude", Common.currentSaloon.getLatitude(),
                    // Common.currentSaloon.getLongitude(), 15.f, Common.currentSaloon.getLatitude(), Common.currentSaloon.getLongitude(), "Saloon is here");
//            Log.d(TAG, "onClick: "+Common.currentSaloon.getLatitude());
//            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(Common.currentSaloon.getLatitude()), Float.parseFloat(Common.currentSaloon.getLongitude()));
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//            startActivity(intent);
                    String uri = "http://maps.google.com/maps?daddr=" + 30.0271585 + "," + 31.489537 + ","  +" (" + "Saloon is here" + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });
    }

    private void populateShopList() {
        OrderDetailsAdapter adapter=new OrderDetailsAdapter(orders,mContext);
        adapter.setChainedCallbackForHairdresserTotal(this);
        orderDetailsList.setLayoutManager(new GridLayoutManager(mContext,1));
        orderDetailsList.setAdapter(adapter);
        orderDetailsList.setNestedScrollingEnabled(false);
    }

    public void initViews(){
        nameTxt=findViewById(R.id.nameTxt);
        orderDetailsList=findViewById(R.id.orderDetailsList);
        galleryBtn=findViewById(R.id.galleryBtn);
        ratingBtn=findViewById(R.id.ratingBtn);
        locationBtn=findViewById(R.id.locationBtn);
        submitBtn=findViewById(R.id.submitBtn);
        totalPriceTxt=submitBtn.findViewById(R.id.totalPriceTxt);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.details));
        profileImg=findViewById(R.id.profileImg);
        profileImg.setImageDrawable(roundedDrawable);
        ratingBar=findViewById(R.id.ratingBar);
        addAdultBtn=findViewById(R.id.addAdultBtn);
        addChildBtn=findViewById(R.id.addChildBtn);
        removeAdultBtn=findViewById(R.id.removeAdultBtn);
        removeChildBtn=findViewById(R.id.removeChildBtn);
        childrenNumTxt=findViewById(R.id.childrenNumTxt);
        adultsNumTxt=findViewById(R.id.adultsNumTxt);


    }

    public void initData(){
        if(Common.currentSaloon!=null){
            if(Fonts.isArabic)
                nameTxt.setText(Common.currentSaloon.getSaloonArName());
            else
                nameTxt.setText(Common.currentSaloon.getSaloonEnName());
        }

        RatingConfiguration ratingConfiguration=new RatingConfiguration(mContext);
        Spannable spannable=ratingConfiguration.setupText((float) Common.currentSaloon.getSaloonRating());
        ratingBar.setText(spannable);
        String bitmap=getIntent().getStringExtra("BitmapImage");
        if(bitmap!=null){
            final int radius = 30;
            final int margin = 2;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            roundedDrawable =new RoundedDrawable(placeHolder ,density,40 , 400 , 350,true, mContext);
            Picasso.get().load(bitmap).
                    transform(transformation).
                    resize(400,350).
                    placeholder(roundedDrawable).
                    into(profileImg);


        }


    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }



    @Override
    public void getTotal(float total,boolean add) {
       this.total=0;
        for(int i=0;i< Common.orderDetailModels.size();i++) {

            this.total += Common.orderDetailModels.get(i).getOrderPrice();
        }
                totalPriceTxt.setText(getString(R.string.total_cost_with_price)+String.valueOf((int)this.total)+"EGP");

}
    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }
}
