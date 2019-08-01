package styles.zonetech.net.styles.Activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Adapters.RatingAdapter;
import styles.zonetech.net.styles.Dialogs.MenuDialog;
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


public class RatingActivity extends AppCompatActivity {
RecyclerView ratingList;
ArrayList<Models>reviews=new ArrayList<>();
Context mContext=RatingActivity.this;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    LinearLayout linearRoot;
    EmptyListInitiator emptyListInitiator;
    IServer server;
    VideoLoader loader;
    FrameLayout loaderLayout;
    EditTextValidator validator;
    ConstraintLayout rootSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        validator=new EditTextValidator(mContext);
        initViews();
        loader=new VideoLoader(mContext,loaderLayout);
        server= Common.getAPI();
        getReviews();
       //
        setListeners();
    }

    private void getReviews() {
        loader.load();
        server.getReviews(String.valueOf(Common.currentSaloon.getSaloonId())).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loader.stop();
                Parser parser=new Parser(mContext);
                parser.parse(response.body());
                reviews=parser.getReviews();

                if(reviews.size()==0){
                    emptyListInitiator.setMessage(getString(R.string.no_ratings));
                }
                else {
                    emptyListInitiator.hideMessage();
                }
                populateRatingList();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loader.stop();
                validator.showSnackbar(rootSnack,false, "");
            }
        });
    }

    private void populateRatingList() {
        ratingList.setLayoutManager(new LinearLayoutManager(mContext));
        RatingAdapter adapter=new RatingAdapter(reviews,mContext);
        ratingList.addItemDecoration(new ItemDecoration(mContext));
        ratingList.setAdapter(adapter);
    }

    private void initViews() {
        ratingList=findViewById(R.id.ratingList);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.rating));
        linearRoot=findViewById(R.id.linearRoot);
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.rootSnack);

    }

    private void setListeners() {
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog menuDialog=new MenuDialog(mContext);
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
}
