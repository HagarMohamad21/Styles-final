package styles.zonetech.net.styles.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Adapters.ShopsAdapter;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.EmptyListInitiator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;
import styles.zonetech.net.styles.Utils.ItemDecoration;

public class HairDressersActivity extends AppCompatActivity {

    public  final int SHOP_LAYOUT=2;
    private   boolean NO_OFFERS_SELECTED = true;
    Context mContext=HairDressersActivity.this;
    RecyclerView shopList;
    Button shopsBtn,shopsOfferBtn;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    LinearLayout linearRoot;
    EmptyListInitiator emptyListInitiator;
    ArrayList<Models>SaloonWithOffers=new ArrayList<>();
    ArrayList<Models>SaloonWithoutOffers=new ArrayList<>();
    ShopsAdapter adapter;
    boolean offersBtnClicked=false,noOffersBtnClicked=true;
    VideoLoader videoLoader;
    FrameLayout loaderLayout;
    IServer server;
    String latitude, longitude,categoryid="",cityId;
    ConstraintLayout rootSnack;
    Parser parser;
    String subCategory;
    EditTextValidator validator;
    ArrayList<Models> saloons=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_dressers);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        server=Common.getAPI();

        initViews();
        validator=new EditTextValidator(mContext);
        parser=new Parser(mContext);
        videoLoader=new VideoLoader(mContext,loaderLayout);
        getBundle();
        setListeners();
    }

    private void getBundle() {
          String callingMethod=getIntent().getStringExtra("callingMethod");
          switch (callingMethod){
              case "browseByLocation":
                  categoryid=getIntent().getStringExtra("gender");
                  subCategory=getIntent().getStringExtra("subCategory");
                  latitude= String.valueOf(getIntent().getDoubleExtra("latitude",0));
                  longitude= String.valueOf(getIntent().getDoubleExtra("longitude",0));
                  browseByLocation();
                  break;
              case "search":
                  String keyword=getIntent().getStringExtra("keyword");
                  search(keyword);
                  break;

              case "browseByProvince":
                  subCategory=getIntent().getStringExtra("subCategory");
                  cityId=getIntent().getStringExtra("cityId");
                  categoryid=getIntent().getStringExtra("gender");
                  browseByProvince();
                  break;
          }


    }


    private void populateShopList() {
          if(NO_OFFERS_SELECTED){
              adapter=new ShopsAdapter(mContext, SaloonWithoutOffers,SHOP_LAYOUT);
              if(SaloonWithoutOffers.size()==0){
                  emptyListInitiator.setMessage(getString(R.string.no_result_message) );

              }
              else if(SaloonWithoutOffers.size()>0) {
                      emptyListInitiator.hideMessage();
              }





          }
          else if(NO_OFFERS_SELECTED==false){
              adapter=new ShopsAdapter(mContext, SaloonWithOffers,SHOP_LAYOUT);
              if(SaloonWithOffers.size()==0){
                  emptyListInitiator.setMessage(getString(R.string.no_result_message) );

              }
              else if(SaloonWithOffers.size()>0)
                  {emptyListInitiator.hideMessage(); }


          }

        shopList.setLayoutManager(new LinearLayoutManager(this));
        shopList.addItemDecoration(new ItemDecoration(mContext));
        shopList.setAdapter(adapter);
    }

    private void intitOffers() {
        SaloonWithOffers.clear();
        SaloonWithoutOffers.clear();
        for(Models saloon:saloons){
            if(saloon.isSaloonHasOffers()){
                SaloonWithOffers.add(saloon);
            }
            else{
                SaloonWithoutOffers.add(saloon);
            }
        }
        populateShopList();

    }


    public void initViews(){
        linearRoot=findViewById(R.id.linearRoot);
        shopList=findViewById(R.id.shopList);
        shopsBtn=findViewById(R.id.withoutOffersBtn);
        shopsOfferBtn=findViewById(R.id.withOffersBtn);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.hair_dressers));
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.rootSnack);
    }
    private void setListeners() {
//**************************************************** SHOPS BUTTON CONFIGURATION ***************************************************************************************//

        shopsBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //change text color to white
                shopsBtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                shopsBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.left_button_rect_yellow));
                shopsOfferBtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                shopsOfferBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.right_button_rect_grey));
                NO_OFFERS_SELECTED=true;
                if(!noOffersBtnClicked){
                    populateShopList();
                    noOffersBtnClicked=true;
                    offersBtnClicked=false;
                }



            }
        });

//**************************************************** SHOPS WITH OFFERS BUTTON CONFIGURATION ***************************************************************************************//
        shopsOfferBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // change  areaBtn drawable to yellow
                shopsOfferBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.right_button_rect_yellow));
                shopsOfferBtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                //change text color to black
                shopsBtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                shopsBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.left_button_rect_grey));
                NO_OFFERS_SELECTED=false;

                if(!offersBtnClicked){
                    populateShopList();
                    offersBtnClicked=true;
                    noOffersBtnClicked=false;
                }





            }
        });
//*****************************************************MENU AND BACK BUTTON******************************************************************************************************

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonMethods.showMenu();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NO_OFFERS_SELECTED=true;
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NO_OFFERS_SELECTED=true;
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }

    private static final String TAG = "HairDressersActivity";
    private void browseByLocation() {
        Log.d(TAG, "browseByLocation: subCat"+subCategory);
        videoLoader.load();
        server.getSaloonsByMap("map",categoryid,latitude,longitude,subCategory).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    parser.parse(response.body());
                    Log.d(TAG, "onResponse: "+response.body());
                    if(parser.getStatus().equals("success")){
                        saloons=parser.getSaloons();
                        Log.d(TAG, "onResponse: "+response.body());
                        intitOffers();
                    }
                    else{
                        validator.ShowToast(getString(R.string.somthing_went_wrong));
                    }
                }
                else {
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
                validator.showSnackbar(rootSnack,false, "");
            }
        });
    }


        private void search(String keyword) {
        videoLoader.load();
        server.search(keyword).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
               parser.parse(response.body());
               if(parser.getStatus().equals("success")){
                   saloons=parser.getSaloons();
                   intitOffers();
               }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
                validator.showSnackbar(rootSnack,false, "");

            }
        });
    }

        private void browseByProvince() {
            Log.d(TAG, "browseByProvince: subCat"+subCategory);
        videoLoader.load();
        server.getSaloonsByArea("area",categoryid,cityId,subCategory).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                parser.parse(response.body());
                if(parser.getStatus().equals("success")){
                    saloons=parser.getSaloons();
                    intitOffers();

                }
                else{
                    validator.ShowToast(getString(R.string.somthing_went_wrong));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
                validator.showSnackbar(rootSnack,false, "");

            }
        });
    }




}
