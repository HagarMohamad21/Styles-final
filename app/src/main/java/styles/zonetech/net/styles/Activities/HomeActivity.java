package styles.zonetech.net.styles.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Adapters.SliderAdapter;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Listeners.OnSwipeItemClicked;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;
import styles.zonetech.net.styles.Utils.RoundedDrawable;

public class HomeActivity extends AppCompatActivity {

    TextView searchBtn, menuBtn;
    EditText searchEditTxt;
    ImageView individualImage, saloonImage;
    SliderView sliderView;
    LinearLayout saloonsLayout, individualLayout;
    String subCategory;
    private Context mContext = HomeActivity.this;
    InputMethodManager imm;
    RoundedDrawable roundedDrawable;
    float density;
    CommonMethods commonMethods;
    Button loginBtn;
    IServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        commonMethods = new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        server = Common.getAPI();
        initViews();
        getBanners();
        setListeners();
    }

    private void getBanners() {
        server.getSlidersImages().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Parser parser = new Parser(mContext);
                parser.parse(response.body());
                if (parser.getStatus().equals("success")) {
                    showBanners(parser.getSlides());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void showBanners(ArrayList<String> slides) {

        final SliderAdapter adapter = new SliderAdapter(slides);
        adapter.setOnSwipeItemClicked(new OnSwipeItemClicked() {
            @Override
            public void onItemClicked() {
//                sliderView.setAutoCycle(false);
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sliderView.setAutoCycle(true);
//                        sliderView.startAutoCycle();
//                    }
//                }, 2000);
            }
        });
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);


        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

    }

    private void initViews() {
        searchBtn = findViewById(R.id.searchBtn);
        searchEditTxt = findViewById(R.id.searchEditTxt);
        menuBtn = findViewById(R.id.menuBtn);
        sliderView = findViewById(R.id.sliderView);
        individualLayout = findViewById(R.id.individualLayout);
        saloonsLayout = findViewById(R.id.saloonsLayout);
        saloonImage = findViewById(R.id.saloonImage);
        individualImage = findViewById(R.id.individualImage);
        density = mContext.getResources().getDisplayMetrics().density;
        Bitmap profilePic = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.placeholder);
        roundedDrawable = new RoundedDrawable(profilePic, density, 20, 250, 350, false, mContext);
        individualImage.setImageDrawable(roundedDrawable);
        saloonImage.setImageDrawable(roundedDrawable);
        loginBtn = findViewById(R.id.loginBtn);
        if (isUserLogged()) {
            //hide login btn
            loginBtn.setVisibility(View.GONE);
        }
    }


    private void setListeners() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open an edit text in tool bar and hide info and search
                searchEditTxt.setVisibility(View.VISIBLE);
                searchEditTxt.requestFocus();
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditTxt, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commonMethods.showMenu();
            }
        });

        searchEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditTxt.requestFocus();
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditTxt, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        saloonsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategory = "1";
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra("subCategory", subCategory);
                startActivity(intent);
            }
        });


        individualLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategory = "2";
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra("subCategory", subCategory);
                startActivity(intent);
            }
        });


        searchEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(searchEditTxt.getText().toString())) {

                        Intent intent = new Intent(mContext, HairDressersActivity.class);
                        intent.putExtra("callingMethod", "search");
                        intent.putExtra("keyword", searchEditTxt.getText().toString());
                        //close keyboard
                        View view = HomeActivity.this.getCurrentFocus();
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        if (view == null) {
                            view = new View(HomeActivity.this);
                        }
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        searchEditTxt.getText().clear();
                        searchEditTxt.setVisibility(View.INVISIBLE);
                        searchBtn.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                    }

                    return true;
                }
                return false;
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to loginActivity
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private boolean isUserLogged() {
        boolean isLogged = false;
        Gson gson = new Gson();
        SharedPreferences userPref = getSharedPreferences(getPackageName(), 0);
        ;
        String userJson = userPref.getString(Common.CURRENT_USER, null);
        if (userJson != null) {
            Common.currentUser = gson.fromJson(userJson, Models.class);
            isLogged = true;
        } else {
            isLogged = false;
        }
        return isLogged;

    }


}