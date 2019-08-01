package styles.zonetech.net.styles.Activities;

import android.content.Context;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView toolbarTitleTxt;
    Button menuBtn,backBtn,sendBtn;
    EditText userNameEditTxt;
    private Context mContext=ForgotPasswordActivity.this;
    private CommonMethods commonMethods;
    EditTextValidator validator;
    ConstraintLayout rootSnack;
    IServer server;
    String language;
    Parser parser;
    VideoLoader videoLoader;
    FrameLayout loaderLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        server= Common.getAPI();
        validator=new EditTextValidator(mContext);
        parser=new Parser(mContext);
        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
    if(Fonts.isArabic){
        language="ar";

    }
    else{
        language="en";
    }
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

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validator.validate(userNameEditTxt)){
                          videoLoader.load();
                   server.recoverPassword(userNameEditTxt.getText().toString(),language).enqueue(new Callback<String>() {
                       @Override
                       public void onResponse(Call<String> call, Response<String> response) {
                           parser.parse(response.body());
                           if(parser.getAction().equals("recovertext")){
                               videoLoader.stop();
                              validator.ShowToast(getString(R.string.email_recovery));
                              finish();
                           }
                       }

                       @Override
                       public void onFailure(Call<String> call, Throwable t) {
                           videoLoader.stop();
                     validator.showSnackbar(rootSnack,false,"");
                       }
                   });

               }
               else{
                   validator.ShowToast(getString(R.string.validation_string));
               }
            }
        });
    }

    private void initView() {
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.recovery));
        userNameEditTxt=findViewById(R.id.userNameEditTxt);
        sendBtn=findViewById(R.id.sendBtn);
        rootSnack=findViewById(R.id.rootSnack);
        loaderLayout=findViewById(R.id.loaderLayout);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }

}
