package styles.zonetech.net.styles.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.TextPatternFormatter;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;



public class BillActivity extends AppCompatActivity {

    Button submitBtn;
    private Context mContext=BillActivity.this;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt, totalTxt,dateIcon,cvvIcon,personIcon,keypadIcon;
    CommonMethods commonMethods;
    TextPatternFormatter formatter;
    EditText cardNameEditTxt,cardNumEditTxt,cardCVVEditTxt,cardExpEditTxt,couponEditTxt;
    EditTextValidator validator;
    RadioButton cashRadioBtn,cardRadioBtn;
    boolean creditEnabled=false;
    ConstraintLayout root;
    VideoLoader loader;
    FrameLayout loaderLayout;
    IServer server;

    String  constructedOrder="",orderScheduleFormat,coupon="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initViews();
        validator=new EditTextValidator(mContext);
        loader=new VideoLoader(mContext,loaderLayout);
        formatter=new TextPatternFormatter();
        watch();
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        server=Common.getAPI();
        getBundle();
        setListeners();
        toggleCardFields(false);
        if(Common.currentSaloon.getCredit()==0){
            cardRadioBtn.setEnabled(false);
        }

    }

    private void getBundle() {
        Intent intent=getIntent();
        orderScheduleFormat=intent.getStringExtra("orderScheduleFormat");
        constructedOrder=intent.getStringExtra("constructedOrder");
    }

    private void initViews() {
        submitBtn=findViewById(R.id.submitBtn);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        cardExpEditTxt=findViewById(R.id.cardExpEditTxt);
        cardNumEditTxt=findViewById(R.id.cardNumEditTxt);
        cardCVVEditTxt=findViewById(R.id.cardCVVEditTxt);
        cardNameEditTxt=findViewById(R.id.cardNameEditTxt);
        cardRadioBtn=findViewById(R.id.cardRadioBtn);
        cashRadioBtn=findViewById(R.id.cashRadioBtn);
        totalTxt=findViewById(R.id.totalTxt);
        couponEditTxt=findViewById(R.id.couponEditTxt);
        personIcon=findViewById(R.id.personIcon);
        keypadIcon=findViewById(R.id.keypadIcon);
        cvvIcon=findViewById(R.id.cvvIcon);
        dateIcon=findViewById(R.id.dateIcon);
      totalTxt.setText(String.valueOf((int)Common.totalPrice)+"EGP");
        toolbarTitleTxt.setText(getString(R.string.your_bill));
        root=findViewById(R.id.root);
        loaderLayout=findViewById(R.id.loaderLayout);
    }

    private void setListeners() {
        cashRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disable cash
                cardRadioBtn.setChecked(false);
                toggleCardFields(false);
                creditEnabled=false;
            }
        });

        cardRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              cashRadioBtn.setChecked(false);
              toggleCardFields(true);
              creditEnabled=true;
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
                finish();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditEnabled){
                    if(validator.validate(cardNameEditTxt)
                            &&validator.validate(cardNumEditTxt)
                            &&validator.validate(cardCVVEditTxt)&&validator.validate(cardExpEditTxt)) {

                        //now we can pay money
                        coupon=couponEditTxt.getText().toString();
                        sendOrder();

                    }
                    else{
                        //show toast
                        validator.ShowToast(getString(R.string.validation_string));
                    }

                }
                else {
                    //send order with or without coupon
                    coupon=couponEditTxt.getText().toString();
                    sendOrder();                }
                }

        });
    }

    private void toggleCardFields(boolean Enabled) {
        cardExpEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardNumEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardCVVEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardNameEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardNumEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardCVVEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardNameEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cardNameEditTxt.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        dateIcon.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        personIcon.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        keypadIcon.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);
        cvvIcon.setVisibility(Enabled?View.VISIBLE:View.INVISIBLE);


        if(!Enabled){
            cardNameEditTxt.setText("");
            cardNumEditTxt.setText("");
            cardCVVEditTxt.setText("");
           cardExpEditTxt.setText("");

        }
    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }








    public void watch(){
        cardNumEditTxt.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!formatter.isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), formatter.buildCorrectString(formatter.getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }


        });


        cardCVVEditTxt.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 3; // size of pattern 0000-0000-0000-0000
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!formatter.isInputCorrect(s, TOTAL_SYMBOLS)) {
                    s.replace(0, s.length(), formatter.buildCorrectString(formatter.getDigitArray(s,TOTAL_SYMBOLS)));
                }
            }


        });

        cardExpEditTxt.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 5; // YY/MM
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if ('/' == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }
                if (editable.length() > 0 && (editable.length() % 3) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf("/")).length <= 2) {
                        editable.insert(editable.length() - 1, String.valueOf("/"));
                    }
                }
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }


    private void sendOrder() {
loader.load();

        server.sendOrder(String.valueOf(Common.currentUser.getUserId()),
                String.valueOf(Common.currentSaloon.getSaloonId()),
                String.valueOf(Common.totalPrice),
                orderScheduleFormat,
                constructedOrder,String.valueOf(Common.currentSaloon.getCredit())
                ,String.valueOf(Common.currentSaloon.getHouse())," ",coupon)

                .enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        loader.stop();

                        validator.ShowToast(getString(R.string.order_placed));
                        mStartActivity(mContext,new OrdersActivity());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        loader.stop();
                        validator.showSnackbar(root,false,"");
                    }
                });
    }
}
