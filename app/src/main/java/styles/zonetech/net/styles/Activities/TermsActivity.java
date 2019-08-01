package styles.zonetech.net.styles.Activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;

public class TermsActivity extends AppCompatActivity {

    Button menuBtn,backBtn;
    TextView toolbarTitleTxt,termsTxt;
    Context mContext= TermsActivity.this;
    Models terms=new Models();
    IServer server;
    Parser parser;
    FrameLayout loaderLayout;
    CommonMethods commonMethods;
    VideoLoader videoLoader;
    ConstraintLayout rootSnack;
    EditTextValidator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        initViews();
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        videoLoader=new VideoLoader(mContext,loaderLayout);
        validator=new EditTextValidator(mContext);
        setListeners();
        parser=new Parser(mContext);
        server= Common.getAPI();
        getTerms();
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

    private void getTerms() {
        videoLoader.load();
       server.terms().enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, Response<String> response) {
               parser.parse(response.body());
               terms=parser.getTerms();
               videoLoader.stop();
               if(Fonts.isArabic){
                   termsTxt.setText(terms.getTermArabicName());
               }
               else
               termsTxt.setText(terms.getTermEnglishName());
           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {
videoLoader.stop();

           }
       });
    }


    private void initViews() {
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.terms));
        termsTxt=findViewById(R.id.termsTxt);
        loaderLayout=findViewById(R.id.loaderLayout);
    }
}
