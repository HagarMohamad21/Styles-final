package styles.zonetech.net.styles.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Dialogs.MenuDialog;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;

public class LoginActivity extends AppCompatActivity {

    TextView personIcon;
    EditTextValidator validator;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt,forgotPasswordTxt;
    EditText passwordEditTxt,userNameEditTxt;
    Button loginBtn,createAccountBtn,continueWithFacebookBtn;
    Context mContext=LoginActivity.this;
    private CommonMethods commonMethods;
    IServer server;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    Parser parser;
    boolean HasBundle=false;
    Bundle extras;
   CallbackManager callbackManager;
    ConstraintLayout rootSnack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        parser=new Parser(mContext);
        validator=new EditTextValidator(mContext);
        server=Common.getAPI();
        initView();

        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
       getBundle();
    }

    private void getBundle() {
         extras = getIntent().getExtras();
        if(extras!=null){
            HasBundle=true;
        }


    }


    private void initView() {
        userNameEditTxt=findViewById(R.id.userNameEditTxt);
        passwordEditTxt=findViewById(R.id.passwordEditTxt);
        loginBtn=findViewById(R.id.loginBtn);
        createAccountBtn=findViewById(R.id.createAccountBtn);
        personIcon=findViewById(R.id.personIcon);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.login));
        forgotPasswordTxt=findViewById(R.id.forgotPasswordTxt);
        loaderLayout=findViewById(R.id.loaderLayout);
        continueWithFacebookBtn=findViewById(R.id.continueWithFacebookBtn);
        rootSnack=findViewById(R.id.rootSnack);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //validate Edit texts are not empty
               if(validator.validate(passwordEditTxt)&&validator.validate(userNameEditTxt)){
                   String username=userNameEditTxt.getText().toString();
                   String password=passwordEditTxt.getText().toString();
                   //login in
                   login("android","0",username,password);
               }
                  else{
                   //show toast
                   validator.ShowToast(getString(R.string.validation_string));
                  }
            }
        });

       createAccountBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //navigate to signup activity
               if(HasBundle){
                  deliverBundle();
               }
               else{
                   mStartActivity(mContext,new SignupActivity());
                   finish();
               }

           }
       });


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


        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartActivity(mContext,new ForgotPasswordActivity());
            }
        });



        callbackManager=CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name=object.getString("name");
                                    String id=object.getString("id");
                                    String imageURL = "http://graph.facebook.com/"+id+"/picture?type=large";
                                    Common.FacebookProfileImageUrl=imageURL;
                                    String password=id.substring(8);
                                    register("android","0",name,email,password,password);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();

                    }
                }
            }
        });


    }




    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }


    private void login(String userdevice, String usertoken, String email, String password) {
        videoLoader.load();
        server.login(userdevice,usertoken,email,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        validator.ShowToast(getString(R.string.login_message));
                        if(HasBundle){
                            gotoCallingActivity();
                        }
                        else{
                            Intent intent=new Intent(mContext,MapsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                    else if(parser.getStatus().equals("error")){
                        validator.ShowToast(parser.getCodeMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
               validator.showSnackbar(rootSnack,false, "");
            }
        });

    }

    private void gotoCallingActivity() {
        if(extras!=null){

            String callingActivity=extras.getString(Common.EXTRA_CALLING_ACTIVITY_MESSAGE);
            if(callingActivity.equals(Common.EXTRA_CALLING_ACTIVITY_CONFIRM_ORDER)){

                Intent intent = new Intent( mContext , ConfirmOrderActivity.class);
                Bundle bun =extras;
                intent.putExtras(bun);
                startActivity(intent);
                finish();
            }

            else if(callingActivity.equals(Common.EXTRA_CALLING_ACTIVITY_MESSAGES)){
                Intent intent=new Intent(mContext,MessagesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

            else if(callingActivity.equals(Common.EXTRA_CALLING_ACTIVITY_ORDERS)){
                Intent intent=new Intent(mContext,OrdersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else if(callingActivity.equals(Common.EXTRA_CALLING_ACTIVITY_USER_ACCOUNT)){
                Intent intent=new Intent(mContext,UserAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }


        }
    }

    private void deliverBundle(){
        Intent intent = new Intent( mContext , SignupActivity.class);
        Bundle bun =extras;
        intent.putExtras(bun);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void facebookLogin(View view) {
       LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }



    private void register(final String userdevice, final String usertoken, final String userName, final String useremail,
                          final String userphone, final String password) {
           videoLoader.load();
        server.register(userdevice,usertoken,userName,useremail,userphone,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    Common.currentUser=new Models();
                    Common.currentUser.setUserEmail(useremail);
                    Common.currentUser.setUserPhone(userphone);
                    Common.currentUser.setUserName(userName);

                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        validator.ShowToast(getString(R.string.register_message));
                        if(HasBundle){
                            gotoCallingActivity();
                        }
                        else{
                            Intent intent=new Intent(mContext,MapsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }


                    }
                    else if(parser.getStatus().equals("error")){

                        if (parser.getCodeMessage().equals("Duplicate email or phone")){
                            login(userdevice,usertoken,useremail,password);
                        }
                        else {
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
               validator.showSnackbar(rootSnack,false, "");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      commonMethods.destroyMenu();
    }

}
