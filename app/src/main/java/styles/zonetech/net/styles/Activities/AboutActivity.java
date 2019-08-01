package styles.zonetech.net.styles.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Listeners.OnLoginClicked;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;


public class AboutActivity extends AppCompatActivity{

Context mContext=AboutActivity.this;
    Button menuBtn,backBtn,contactBtn;
    TextView toolbarTitleTxt,textView16,textView17,textView22;
    FrameLayout loaderLayout;
    CommonMethods commonMethods;
    VideoLoader videoLoader;
ConstraintLayout rootSnack;
  EditTextValidator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
      videoLoader=new VideoLoader(mContext,loaderLayout);
       validator=new EditTextValidator(mContext);
    commonMethods=new CommonMethods(mContext);
    commonMethods.setupFont(findViewById(android.R.id.content));
    commonMethods.setupMenu();
setListeners();



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


        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.currentUser!=null){
                mStartActivity(mContext,new MessagesActivity());}
                else {
                    final ListsDialog dialog=new ListsDialog(mContext,Common.DIALOG_LAYOUT_LOGIN,-1, null, null, "",
                            -1,null,null);
                    dialog.setOnLoginClicked(new OnLoginClicked() {
                        @Override
                        public void onLoginClicked() {
                            dialog.dismiss();
                            mStartActivity(mContext,new LoginActivity());
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void initViews() {
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.about));
        loaderLayout=findViewById(R.id.loaderLayout);
        textView16=findViewById(R.id.textView16);
        textView17=findViewById(R.id.textView17);
        textView22=findViewById(R.id.textView22);
        contactBtn=findViewById(R.id.contactBtn);
        rootSnack=findViewById(R.id.rootSnack);

    }



    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }


    




@Override
public void onDestroy() {
    super.onDestroy();
    commonMethods.destroyMenu();
}

}
