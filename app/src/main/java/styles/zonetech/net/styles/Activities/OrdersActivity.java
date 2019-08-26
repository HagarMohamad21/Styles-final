package styles.zonetech.net.styles.Activities;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Adapters.OrdersAdapter;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.EmptyListInitiator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;

public class OrdersActivity extends AppCompatActivity  {

RecyclerView ordersList;

ArrayList<Models> orders=new ArrayList<>();
    private Context mContext=OrdersActivity.this;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    LinearLayout linearRoot;
EmptyListInitiator emptyListInitiator;
IServer server;
Parser parser;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    ConstraintLayout rootSnack;
    private EditTextValidator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        parser=new Parser(mContext);
        server= Common.getAPI();
        if (Common.currentUser==null)
        isUserLogged();
       initViews();
        videoLoader=new VideoLoader(mContext,loaderLayout);
        validator=new EditTextValidator(mContext);
       getOrders();
        setListeners();

    }

    private void getOrders() {
        videoLoader.load();
        server.getOrders(String.valueOf(Common.currentUser.getUserId())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                parser.parse(response.body());
                orders=parser.getOrders();

                if(orders.size()==0){
                    emptyListInitiator.setMessage(getString(R.string.no_orders));
                }
                else {
                    emptyListInitiator.hideMessage();
                    populateOrdersList();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
validator.showSnackbar(rootSnack,false,"");
            }
        });

    }

    private void populateOrdersList() {
   ordersList.setLayoutManager(new GridLayoutManager(mContext,1));


        OrdersAdapter adapter=new OrdersAdapter(orders,mContext);
    ordersList.setAdapter(adapter);
    }

    private void initViews() {
        ordersList=findViewById(R.id.ordersList);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.orders));
        linearRoot=findViewById(R.id.linearRoot);
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.root);

    }

    private void setListeners() {
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonMethods.showMenu();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }
    private boolean isUserLogged(){
        boolean isLogged=false;
        Gson gson = new Gson();
        SharedPreferences userPref=getSharedPreferences(getPackageName(),0);;
        String userJson = userPref.getString(Common.CURRENT_USER, null);
        if(userJson!=null){
            Common.currentUser = gson.fromJson(userJson, Models.class);
            isLogged=true;
        }
        else {
            isLogged=false;
        }
        return isLogged;

    }

}

