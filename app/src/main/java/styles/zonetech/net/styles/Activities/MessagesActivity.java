package styles.zonetech.net.styles.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Adapters.MessagesAdapter;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.EmptyListInitiator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;



public class MessagesActivity extends AppCompatActivity  {
RecyclerView messagesList;
EditText messageEditTxt;
TextView sendBtn;
Context mContext=MessagesActivity.this;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    EditTextValidator validator;
LinearLayout linearRoot;
EmptyListInitiator emptyListInitiator;
MessagesAdapter adapter;
String message;
IServer server;
Parser parser;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    ConstraintLayout rootSnack;


    ArrayList<Models> messages=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupMenu();
        commonMethods.setupFont(findViewById(android.R.id.content));
        validator=new EditTextValidator(mContext);
        if (Common.currentUser==null)
            isUserLogged();
        parser=new Parser(mContext);
        initViews();
       Models models=new Models();
        models.setMessageSender("1");
        models.setMessageDescription("How can we help you sir");
        messages.add(models);
        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
        server= Common.getAPI();
        getMessages();
    }



    private void populateMessagesList() {
        messagesList.setLayoutManager(new GridLayoutManager(mContext,1));
        adapter=new MessagesAdapter(mContext, messages);
        messagesList.setAdapter(adapter);
    }


    private void initViews() {
        linearRoot=findViewById(R.id.linearRoot);
        messagesList=findViewById(R.id.messagesList);
        messageEditTxt=findViewById(R.id.messageEditTxt);
        sendBtn=findViewById(R.id.sendBtn);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.messages));
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.rootSnack);


    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }

    private void setListeners() {
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MenuDialog menuDialog=new MenuDialog(mContext);
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
               if(validator.validate(messageEditTxt)){
                      message=messageEditTxt.getText().toString();
                          emptyListInitiator.hideMessage();
                          sendMessage(messageEditTxt.getText().toString());
                          messageEditTxt.setText("");

                  

               }


           }
       });
    }


    private void getMessages() {
        videoLoader.load();
        messages.clear();
        if(Common.currentUser!=null){
            String userId= String.valueOf(Common.currentUser.getUserId());
            server.getMessages(userId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    videoLoader.stop();
                    if(response.body()!=null){
                        parser.parse(response.body());

                        if(parser.getStatus().equals("success")){
                            messages=parser.getMessages();
                            //show messages
                            if(messages.size()>0){
                                //populate list
                                emptyListInitiator.hideMessage();
                                populateMessagesList();
                            }
                            else {
                                populateMessagesList();
                                emptyListInitiator.setMessage(getString(R.string.no_messages));
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

    }

    private  void sendMessage(final String message){
        if(Common.currentUser!=null){
            String userId= String.valueOf(Common.currentUser.getUserId());
            server.sendMessage(userId,message).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body()!=null){
                        parser.parse(response.body());

                        if(parser.getStatus().equals("success")){
                            if(parser.MessageSent){
                                getMessages();
                                if(adapter!=null)
                                    adapter.notifyDataSetChanged();
                                else{
                                    getMessages();
                                }
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
            });}

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

