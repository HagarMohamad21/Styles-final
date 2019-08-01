package styles.zonetech.net.styles.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import styles.zonetech.net.styles.Activities.HairDresserActivity;
import styles.zonetech.net.styles.Activities.LoginActivity;
import styles.zonetech.net.styles.Activities.MapsActivity;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.EmptyListInitiator;
import styles.zonetech.net.styles.Listeners.ChainedCallbackForHairdresserTotal;
import styles.zonetech.net.styles.Listeners.OnLoginClicked;
import styles.zonetech.net.styles.Listeners.SendRatingCallback;
import styles.zonetech.net.styles.Listeners.onCancelBtnClicked;
import styles.zonetech.net.styles.Listeners.onCloseBtnClicked;
import styles.zonetech.net.styles.Listeners.onPersonTotalComputed;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.Utils.Common;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import styles.zonetech.net.styles.Adapters.DialogAdapter;
import styles.zonetech.net.styles.Listeners.DialogDismissListener;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Listeners.DialogListener;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Utils.ItemDecoration;


public class ListsDialog extends Dialog implements DialogListener  {


OnLoginClicked onLoginClicked;
onCancelBtnClicked onCancelBtnClicked;
onCloseBtnClicked onCloseBtnClicked;

    public void setOnCancelBtnClicked(styles.zonetech.net.styles.Listeners.onCancelBtnClicked onCancelBtnClicked) {
        this.onCancelBtnClicked = onCancelBtnClicked;
    }

    public void setOnCloseBtnClicked(styles.zonetech.net.styles.Listeners.onCloseBtnClicked onCloseBtnClicked) {
        this.onCloseBtnClicked = onCloseBtnClicked;
    }

    public void setOnLoginClicked(OnLoginClicked onLoginClicked) {
        this.onLoginClicked = onLoginClicked;
    }

SendRatingCallback sendRatingCallback;

    public void setSendRatingCallback(SendRatingCallback sendRatingCallback) {
        this.sendRatingCallback = sendRatingCallback;
    }

    private final ArrayList<Models> services;
    DialogAdapter adapter;
    private static int givenLayoutType;
    private final Context context;
    RelativeLayout root;
    RecyclerView dialogList;
    TextView closeBtn,dialogNameTxt,message,sendBtn;
    Button addBtn,yesBtn,noBtn;
    ConstraintLayout commentView,cancelDialogView;
    ArrayList<Models> provinces=new ArrayList<>();
    ArrayList<String> currentServices; HashMap<String,String> currentOffers;
    ArrayList<Models> ratings=new ArrayList<>();
    ArrayList<Models> orderDetails=new ArrayList<>();

    public void setOrderDetails(ArrayList<Models> orderDetails) {
        this.orderDetails = orderDetails;
    }

    ArrayList<Models>images=new ArrayList<>();
    LinearLayout linearRoot;
    EditText messageEditTxt;
    EmptyListInitiator emptyListInitiator;
    int pos;
    int orderPosition;
    DialogDismissListener dialogDismissListener;
     String isChild;
int personTotal;
    onPersonTotalComputed onPersonTotalComputed;

    public void setOnPersonTotalComputed(onPersonTotalComputed onPersonTotalComputed) {
        this.onPersonTotalComputed = onPersonTotalComputed;
    }
    EditTextValidator validator;



    public ListsDialog(@NonNull Context context, int givenLayoutType, int pos, ArrayList<Models> offers, ArrayList<String>services,String isChild, int personTotal,
                       ArrayList<String> currentServices,HashMap<String,String> currentOffers
    ) {
        super(context);
        orderPosition=pos;
        this.personTotal=personTotal;
        this.isChild=isChild;
        this.givenLayoutType=givenLayoutType;
        this.context=context;
        this.services=offers;
        if(context instanceof HairDresserActivity&&givenLayoutType==-100&&pos==-1){

        }
        initRating();
        initImages();

        this.currentOffers=currentOffers;
        this.currentServices=currentServices;
    }




    private void initImages() {
        images.add(new Models(context.getString(R.string.gallery)));
        images.add(new Models(context.getString(R.string.camera)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.list_dialog_layout);
        setupFont(findViewById(android.R.id.content));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initViews();
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        initProvinces();
        populateList();

    }

    private void initViews() {
        linearRoot=findViewById(R.id.linearRoot);
        root=findViewById(R.id.root);
        dialogList=findViewById(R.id.dialogList);
        closeBtn=findViewById(R.id.closeBtn);
        addBtn=findViewById(R.id.addBtn);
        commentView=findViewById(R.id.commentView);
        cancelDialogView=findViewById(R.id.cancelDialogView);
        message=cancelDialogView.findViewById(R.id.message);
        yesBtn=cancelDialogView.findViewById(R.id.yesBtn);
        noBtn=cancelDialogView.findViewById(R.id.noBtn);
        messageEditTxt=findViewById(R.id.messageEditTxt);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (givenLayoutType){
                    case Common.DIALOG_LAYOUT_TYPE_SERVICE:
                        Common.orderDetailModels.get(orderPosition).getOrderServices().clear();
                        Common.orderDetailModels.get(orderPosition).setServicesPrice(0);
                        onPersonTotalComputed.onPersonTotalComputed(Common.orderDetailModels.get(orderPosition).getOrderPrice());
                        dismiss();
                        break;
                    case Common.DIALOG_LAYOUT_TYPE_OFFER:
                        Common.orderDetailModels.get(orderPosition).getOrderOffers().clear();
                        Common.orderDetailModels.get(orderPosition).setOffersPrice(0);
                        onPersonTotalComputed.onPersonTotalComputed(Common.orderDetailModels.get(orderPosition).getOrderPrice());
                        dismiss();
                        break;
                    default:
                        if(onCloseBtnClicked!=null)
                        onCloseBtnClicked.onCloseBtnClicked();
                        dismiss();

                }

            }
        });
        dialogNameTxt=findViewById(R.id.dialogNameTxt);

        sendBtn=commentView.findViewById(R.id.sendBtn);

        switch (givenLayoutType){

            case Common.DIALOG_LAYOUT_TYPE_SERVICE:
                dialogNameTxt.setText(getContext().getString(R.string.choose_your_service));
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPersonTotalComputed.onPersonTotalComputed(Common.orderDetailModels.get(orderPosition).getOrderPrice());
                        dismiss();
                    }
                });

                break;

            case Common.DIALOG_LAYOUT_TYPE_OFFER:
                dialogNameTxt.setText(getContext().getString(R.string.choose_your_offer));
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPersonTotalComputed.onPersonTotalComputed(Common.orderDetailModels.get(orderPosition).getOrderPrice());
                      dismiss();

                    }
                });
                break;

            case  Common.DIALOG_LAYOUT_TYPE_CITY:
                dialogNameTxt.setText(getContext().getString(R.string.select_area));
                addBtn.setVisibility(View.GONE);
                if(Common.cities.size()>1)
                root.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_corners_dialog_back_with_padding));
                break;

            case Common.DIALOG_LAYOUT_TYPE_PROVINCE:
                dialogNameTxt.setText(getContext().getString(R.string.select_city));
                addBtn.setVisibility(View.GONE);
                if(provinces.size()>1)
                root.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_corners_dialog_back_with_padding));
                break;

            case Common.DIALOG_LAYOUT_TYPE_RATING:
                dialogNameTxt.setText(getContext().getString(R.string.give_rating));
                addBtn.setVisibility(View.GONE);
                commentView.setVisibility(View.VISIBLE);
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendRatingCallback.onSendBtnClicked(pos,messageEditTxt.getText().toString());
                        dismiss();

                    }
                });
                break;

            case Common.DIALOG_LAYOUT_TYPE_ORDER_DETAILS:
                dialogNameTxt.setText(getContext().getString(R.string.order_details));
                addBtn.setVisibility(View.GONE);
                break;


            case Common.DIALOG_LAYOUT_TYPE_ORDER_CANCEL:
                dialogNameTxt.setText(context.getString(R.string.cancel_order));
                dialogList.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);
                cancelDialogView.setVisibility(View.VISIBLE);
                message.setText(context.getString(R.string.confirm_cancel));
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          onCancelBtnClicked.onCancelBtnClicked();
                        dismiss();

                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

            case Common.DIALOG_LAYOUT_TYPE_ORDER_REJECTION:
                dialogNameTxt.setText(context.getString(R.string.rejected_order));
                dialogList.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);
                message.setText(isChild);
                noBtn.setVisibility(View.GONE);
                yesBtn.setVisibility(View.GONE);
                cancelDialogView.setVisibility(View.VISIBLE);
                break;

            case Common.DIALOG_LAYOUT_IMAGE:
                dialogNameTxt.setText(context.getString(R.string.choose_image));
                addBtn.setVisibility(View.GONE);
                break;

            case Common.DIALOG_LAYOUT_LOGIN:
                dialogNameTxt.setText(context.getString(R.string.login));
                dialogList.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);
                cancelDialogView.setVisibility(View.VISIBLE);
                yesBtn.setText(context.getString(R.string.login));
                noBtn.setText(context.getString(R.string.cancel));
                message.setText(context.getString(R.string.confirm_login));
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(onLoginClicked!=null)
                           onLoginClicked.onLoginClicked();


                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onCancelBtnClicked!=null)
                            onCancelBtnClicked.onCancelBtnClicked();
                        dismiss();
                    }
                });
                break;


                case Common.DIALOG_LAYOUT_LOGOUT:
                dialogNameTxt.setText(context.getString(R.string.logout));
                dialogList.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);
                cancelDialogView.setVisibility(View.VISIBLE);
                yesBtn.setText(context.getString(R.string.logout));
                noBtn.setText(context.getString(R.string.cancel));
                message.setText(context.getString(R.string.confirm_logout));
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete saved user
                        SharedPreferences userPref=context.getSharedPreferences(context.getPackageName(),0);
                        SharedPreferences.Editor editor=userPref.edit();
                        editor.putString(Common.CURRENT_USER,null);
                        editor.apply();
                        Common.currentUser=null;
                        Common.orderDetailModels.clear();

                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();

                        }

                        Intent intent=new Intent(context, MapsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);


                        dismiss();

                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

        }




   }

    /*
     * set up icons font*/
    public void setupFont(View view){
        Fonts fonts=new Fonts(context);
        fonts.setTypeFce(view);
    }

    public void populateList(){

        switch (givenLayoutType){

            case Common.DIALOG_LAYOUT_TYPE_SERVICE:

                    setupRecyclerView(services,orderPosition);
               break;


            case Common.DIALOG_LAYOUT_TYPE_OFFER:

                    setupRecyclerView(services,orderPosition);





                break;


           case  Common.DIALOG_LAYOUT_TYPE_PROVINCE:
               setupRecyclerView(provinces,orderPosition);
               break;
            case Common.DIALOG_LAYOUT_TYPE_CITY:


                    setupRecyclerView(Common.cities,orderPosition);


                break;

            case  Common.DIALOG_LAYOUT_TYPE_RATING:
                setupRecyclerView(ratings,orderPosition);
                break;

            case Common.DIALOG_LAYOUT_TYPE_ORDER_DETAILS:

                setupRecyclerView(orderDetails,orderPosition);
                break;

            case Common.DIALOG_LAYOUT_TYPE_ORDER_CANCEL:

                break;
            case Common.DIALOG_LAYOUT_IMAGE:
                setupRecyclerView(images,orderPosition);
                break;

        }






    }



   private void setupRecyclerView(ArrayList<Models> list, int orderPosition) {

           // this means it's either a child or an adult
           if(list.size()==0){
               addBtn.setVisibility(View.GONE);
               if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_SERVICE)
               emptyListInitiator.setMessage(context.getString(R.string.no_service_message));
               else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_OFFER)
                   emptyListInitiator.setMessage(context.getString(R.string.no_offers_message));


       }
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        ItemDecoration itemDecoration=new ItemDecoration(context);
        adapter=new DialogAdapter(context,list,givenLayoutType,orderPosition,personTotal,currentServices,currentOffers);
        adapter.setDialogListener(this);
        dialogList.setAdapter(adapter);
        dialogList.setLayoutManager(layoutManager);
        dialogList.addItemDecoration(itemDecoration);
if(list!=null){
    if(list.size()<=Common.DIALOG_LIST_DEFAULT_SIZE){
        ViewGroup.LayoutParams params=dialogList.getLayoutParams();
        params.height= WindowManager.LayoutParams.WRAP_CONTENT;
        dialogList.setLayoutParams(params);
    }
}


    }



    @Override
    public void onClickRow(int position) {
        pos = position;
        setOnDismissListener();

    }

       public void setDialogDismissListener(DialogDismissListener dialogDismissListener) {
               this.dialogDismissListener = dialogDismissListener;
    }





    public void setOnDismissListener() {

        if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_CITY){

            if(dialogDismissListener!=null)
                dialogDismissListener.onDialogDismissed(Common.cities.get(pos),pos);
            dismiss();
        }

         if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_PROVINCE){

            if(dialogDismissListener!=null)
                dialogDismissListener.onDialogDismissed(provinces.get(pos),pos);
            dismiss();
        }


        else if(givenLayoutType==Common.DIALOG_LAYOUT_IMAGE){
          dialogDismissListener.onDialogDismissed(images.get(pos),pos);
            dismiss();
        }

        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_RATING){
         }
      
    }

  ///////////////////////////////////////////////////////////////// TEMPORARY DATA /////////////////////////////////////////////////////////////////////////////////////////



    private void initProvinces() {
HashMap<String ,Integer> provincesHash=new HashMap<>();
     String[] states = context.getResources().getStringArray(R.array.statesArray);
       for(int i =0;i<states.length;i++){
          for(Models city:Common.cities){
              if(i==Integer.parseInt(city.getProvinceid())-1){
                  provincesHash.put(states[i],1);
              }

          }

       }
        for (Map.Entry<String, Integer> entry : provincesHash.entrySet()) {
            String key = entry.getKey();
            provinces.add(new Models(key));
       }


    }
    

    private void initRating(){
        Models rating1=new Models(context.getString(R.string.excellent),"","",5);
        ratings.add(rating1);
        Models rating2=new Models(context.getString(R.string.very_good2),"","",4);
        ratings.add(rating2);
        Models rating3=new Models(context.getString(R.string.good),"","",3);
        ratings.add(rating3);
        Models rating4=new Models(context.getString(R.string.okay),"","",2);
        ratings.add(rating4);
        Models rating5=new Models(context.getString(R.string.bad),"","",1);
        ratings.add(rating5);

    }


    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        context.startActivity(intent);
    }



}
