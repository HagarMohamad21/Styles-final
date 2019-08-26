package styles.zonetech.net.styles.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import styles.zonetech.net.styles.Adapters.DialogAdapter;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Helpers.DatePicker;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Listeners.OnLoginClicked;
import styles.zonetech.net.styles.Listeners.RemoveItemsCallback;
import styles.zonetech.net.styles.Listeners.onCancelBtnClicked;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;
import styles.zonetech.net.styles.Utils.ItemDecoration;



public class ConfirmOrderActivity extends AppCompatActivity  {
    private boolean inShop=true;
Button continueBtn;
Context mContext=ConfirmOrderActivity.this;

RecyclerView orderDetailsList;
    private static int givenLayoutType;
    Button inHomeBtn,inShopBtn;
    DatePicker datePicker=new DatePicker();
    RemoveItemsCallback removeItemsCallback;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt,locationIcon;
    TextView dateTxt,timeTxt;
    EditText locationEditTxt;
    ArrayList<Models> orderDetails=new ArrayList<>();
    private CommonMethods commonMethods;
    EditTextValidator validator;

String orderScheduleFormat;
private boolean inShopClicked=true;
boolean HasBundle=false;
String constructedOrder="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        givenLayoutType= Common.DIALOG_LAYOUT_TYPE_CONFIRM_ORDER;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        validator=new EditTextValidator(mContext);
        initViews();
        getBundle();
        setListeners();
        if(Common.currentSaloon.getHouse()==0){
            inHomeBtn.setEnabled(false);
        }
        if(!HasBundle){
            DatePicker.isDateSet=false;
            DatePicker.isTimeSet=false;
            Common.totalPrice=0;
            setupList();
            setupRecyclerView();
        }

    }


    private void setupList() {
        float total=0;

       orderDetails= (ArrayList<Models>) Common.orderDetailModels.clone();

        for(int i=0;i<orderDetails.size();i++){

    StringBuilder s=new StringBuilder();
    StringBuilder prices=new StringBuilder();

    total+=orderDetails.get(i).getOrderPrice();
   String servicesFormatted=serviceFormat(orderDetails.get(i).getOrderServices());
   String priceFormatted=pricesFormat(orderDetails.get(i).getOrderServices());
        prices.append(priceFormatted);
       s.append(servicesFormatted);
         if(orderDetails.get(i).getOrderOffers().size()>0){
    //means we have offers
    String offersFormatted=offerFormat(orderDetails.get(i).getOrderOffers());
    String offersPrices=offerPriceFormat(orderDetails.get(i).getOrderOffers());
    if(orderDetails.get(i).getOrderServices().size()>0){
        s.append(',');
        prices.append(',');
    }

   s.append(offersFormatted);
    prices.append(offersPrices);

}
    orderDetails.get(i).setFormattedPriceString(prices.toString());
    orderDetails.get(i).setFormattedStrigForOrderConfirmation(s.toString());

}
Common.totalPrice=total;
Models totalModel=new Models(Common.totalPrice,true);

orderDetails.add(totalModel);
    }

    private String offerPriceFormat(HashMap<String, String> orderOffers) {
        StringBuilder prices=new StringBuilder();
        int i=0;
        for (Map.Entry<String, String> entry : orderOffers.entrySet()) {
            String key = entry.getKey();

            if(Common.servicesPrices.containsKey(key)){
                i++;

                prices.append(Common.servicesPrices.get(key));

                if(i>0&&(i!=orderOffers.size())){
                    prices.append(',');

                }
            }


        }


    return prices.toString();}

    private String pricesFormat(ArrayList<String> orderServices) {
        StringBuilder formattedPrices=new StringBuilder();
        for(int i=0;i<orderServices.size();i++){
            if(Common.servicesPrices.containsKey(orderServices.get(i))){
                formattedPrices.append(Common.servicesPrices.get(orderServices.get(i)));
            }
            if(i!=orderServices.size()-1){
                formattedPrices.append(',');
            }
        }
    return formattedPrices.toString();}

    public void setRemoveItemsCallback(RemoveItemsCallback removeItemsCallback) {
        this.removeItemsCallback = removeItemsCallback;
    }

    private void setListeners() {
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constructOrderArray();
                if(inShop){
                    if((datePicker.isTimeSet)&&(datePicker.isDateSet)){
                        //if user not signed in ? show a login dialog and navigate to login activity
                        if(Common.currentUser!=null){
                            //else if shop accepts credit payment navigate to bill activity
                            formatSchedule();
                            Intent intent=new Intent(mContext,BillActivity.class);
                            intent.putExtra("orderScheduleFormat",orderScheduleFormat);
                            intent.putExtra("constructedOrder",constructedOrder);
                            startActivity(intent);
                        }
                        else {
                            final ListsDialog dialog=new ListsDialog(mContext,Common.DIALOG_LAYOUT_LOGIN,-1, null, null, "", -1,null,null);
                            dialog.setOnLoginClicked(new OnLoginClicked() {
                                @Override
                                public void onLoginClicked() {
                                   dialog.dismiss();
                                    Intent intent = new Intent( mContext , LoginActivity.class);
                                   Bundle bun =setupBundle();
                                    intent.putExtras(bun);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            dialog.setOnCancelBtnClicked(new onCancelBtnClicked() {
                                @Override
                                public void onCancelBtnClicked() {
                                    finish();
                                }
                            });
                            dialog.show();
                        }






                    }


                    else{
                        validator.ShowToast(getString(R.string.validation_string));
                    }
                }

            else{
                    if(validator.validate(locationEditTxt)&&(datePicker.isTimeSet)&&(datePicker.isDateSet)){
                        //if user not signed in ? show a login dialog and navigate to login activity
                        if(Common.currentUser!=null){
                            //else if shop accepts credit payment navigate to bill activity
                            mStartActivity(mContext,new BillActivity());

                        }
                        else {
                            final ListsDialog dialog=new ListsDialog(mContext,Common.DIALOG_LAYOUT_LOGIN,-1, null, null, "", -1,null,null);
                            dialog.setOnLoginClicked(new OnLoginClicked() {
                                @Override
                                public void onLoginClicked() {
                                    dialog.dismiss();
                                    Intent intent = new Intent( mContext , LoginActivity.class);
                                    Bundle bun =setupBundle();
                                    intent.putExtras(bun);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            dialog.show();
                        }


                        formatSchedule();



                    }

                    else{
                        validator.ShowToast(getString(R.string.validation_string));
                    }
                }

            }
        });
        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker=new DatePicker(mContext,dateTxt,false,true);
               
            }
        });
        
        timeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              datePicker=new DatePicker(mContext,timeTxt,false,false);
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



    inShopBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!inShopClicked){
                inShopBtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                inShopBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.right_button_rect_yellow));
                inHomeBtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                inHomeBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.left_button_rect_grey));
                locationEditTxt.setVisibility(View.GONE);
                locationIcon.setVisibility(View.GONE);
                inShop=true;
                //remove in home list item
                if(removeItemsCallback!=null)
                    removeItemsCallback.removeItem();
                inShopClicked=true;
            }

        }
    });




    inHomeBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(inShopClicked){
                inHomeBtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                inHomeBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.left_button_rect_yellow));
                inShopBtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                inShopBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.right_button_rect_grey));
                locationEditTxt.setVisibility(View.VISIBLE);
                locationIcon.setVisibility(View.VISIBLE);
                inShop=false;
                //add in home list item
                if(removeItemsCallback!=null)
                    removeItemsCallback.addItem();

           inShopClicked=false; }

            }

    });







    }

    private Bundle setupBundle() {
        Bundle bun = new Bundle();
        bun.putString(Common.EXTRA_CALLING_ACTIVITY_MESSAGE, Common.EXTRA_CALLING_ACTIVITY_CONFIRM_ORDER);
        bun.putString(Common.EXTRA_DATE_MESSAGE, dateTxt.getText().toString());
        bun.putString(Common.EXTRA_TIME_MESSAGE,timeTxt.getText().toString());
        bun.putParcelableArrayList(Common.EXTRA_ORDER_ARRAY_MESSAGE, orderDetails);
        return bun;

    }

    private void initViews() {
        continueBtn=findViewById(R.id.continueBtn);
        dateTxt=findViewById(R.id.dateTxt);
        timeTxt=findViewById(R.id.timeTxt);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        inHomeBtn=findViewById(R.id.withoutOffersBtn);
        inShopBtn=findViewById(R.id.inShopBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.confirm_order));
        orderDetailsList=findViewById(R.id.orderDetailsList);
        locationEditTxt=findViewById(R.id.locationEditTxt);
        locationIcon=findViewById(R.id.locationIcon);
        if(inShop){
            locationEditTxt.setVisibility(View.GONE);
            locationIcon.setVisibility(View.GONE);
        }
    }
    
    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        ItemDecoration itemDecoration=new ItemDecoration(mContext);
        DialogAdapter adapter;
        adapter=new DialogAdapter(mContext,orderDetails,givenLayoutType,-1, -1,null,null);
        orderDetailsList.setAdapter(adapter);
        orderDetailsList.setLayoutManager(layoutManager);
        orderDetailsList.addItemDecoration(itemDecoration);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     orderDetails.clear();

    }



    private void formatSchedule() {

        orderScheduleFormat=dateTxt.getText().toString()+" "+timeTxt.getText().toString();


    }



    private String serviceFormat(ArrayList<String> services){
        StringBuilder formattedString=new StringBuilder("");
        for(int i=0;i<services.size();i++ ){
            formattedString.append(services.get(i));
            if(i!=services.size()-1){
                formattedString.append(',');
            }
        }
return formattedString.toString();}

private String offerFormat(HashMap<String,String> offers){
    StringBuilder formattedString=new StringBuilder("");
int i=0;
    for (Map.Entry<String, String> entry : offers.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        formattedString.append(key);
        formattedString.append('(');
        formattedString.append(value);
        formattedString.append(')');
        if(i!=offers.size()-1){
            formattedString.append(',');
            i++;
        } }

    return formattedString.toString();}

    private void getBundle() {

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            HasBundle=true;
            ArrayList<Models> bundledOrder  = extras.getParcelableArrayList(Common.EXTRA_ORDER_ARRAY_MESSAGE);
            String time=extras.getString(Common.EXTRA_TIME_MESSAGE);
            String date=extras.getString(Common.EXTRA_DATE_MESSAGE);


            initData(time,date,bundledOrder);
        }

    }

    private void initData(String time, String date, ArrayList<Models> bundledOrder) {
        timeTxt.setText(time);   timeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        dateTxt.setText(date);   dateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        orderDetails= (ArrayList<Models>) bundledOrder.clone();
        setupRecyclerView();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }


    private void constructOrderArray(){
        String personType;
        JSONArray orders = new JSONArray();
        for(int i=0;i<Common.orderDetailModels.size();i++){
            JSONObject order = new JSONObject();
            try {
                order.put("orderid",i+1);
                order.put("total",Common.orderDetailModels.get(i).getOrderPrice());
                 personType=Common.orderDetailModels.get(i).getOrderPersonType();
                 if(personType.equals("child"))
                order.put("child",1);
                 else
                     order.put("child",0);
                order.put("services",Common.orderDetailModels.get(i).getFormattedStrigForOrderConfirmation());
                order.put("prices",Common.orderDetailModels.get(i).getFormattedPriceString());
                orders.put(order);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

       constructedOrder=orders.toString();
    }





}
