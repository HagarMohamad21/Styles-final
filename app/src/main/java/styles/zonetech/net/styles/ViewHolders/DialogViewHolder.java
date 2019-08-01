package styles.zonetech.net.styles.ViewHolders;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import styles.zonetech.net.styles.Listeners.ChainedCallbackForHairdresserTotal;
import styles.zonetech.net.styles.Listeners.DialogListener;
import styles.zonetech.net.styles.Listeners.RatingSelectedListener;
import styles.zonetech.net.styles.Listeners.onPersonTotalComputed;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.RatingConfiguration;

import static styles.zonetech.net.styles.Helpers.Fonts.SELECT_LANGUAGE;

public class DialogViewHolder extends RecyclerView.ViewHolder

{

    RatingSelectedListener ratingSelectedListener;




int orderPosition;
    TextView itemNameTxt, rightMostTxt, itemDetailsTxt,itemIcon;
    CheckBox itemCheckBox;
    DialogListener dialogListener;
    Context mContext;
    public ConstraintLayout root;
    Fonts fonts;
    String lang;
    private Models dialogItem;
    ArrayList<String> currentServices; HashMap<String,String> currentOffers;
  int personTotal;

  ChainedCallbackForHairdresserTotal chainedCallbackForHairdresserTotal;

    public void setChainedCallbackForHairdresserTotal(ChainedCallbackForHairdresserTotal chainedCallbackForHairdresserTotal) {
        this.chainedCallbackForHairdresserTotal = chainedCallbackForHairdresserTotal;
    }

    public DialogViewHolder(@NonNull View itemView, Context mContext, final DialogListener dialogListener,
                            RatingSelectedListener ratingSelectedListener, final int orderPosition, String service , final int personTotal) {

        super(itemView);

         this.personTotal=personTotal;
        this.ratingSelectedListener=ratingSelectedListener;
        this.mContext=mContext;
        this.orderPosition=orderPosition;
        itemNameTxt =itemView.findViewById(R.id.itemNameTxt);
        rightMostTxt = itemView.findViewById(R.id.rightMostTxt);
        itemCheckBox =itemView.findViewById(R.id.itemCheckBox);
        itemDetailsTxt =itemView.findViewById(R.id.serviceDetailsTxt);
        root=itemView.findViewById(R.id.root);
        itemIcon=itemView.findViewById(R.id.itemIcon);
        Locale language= Resources.getSystem().getConfiguration().locale;
        SharedPreferences languagePref =mContext.getSharedPreferences(mContext.getPackageName(),0);
        lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());
        this.dialogListener=dialogListener;
        fonts=new Fonts(mContext);
        fonts.setTypeFce(itemView);
        ///////////making static variables  null
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=getAdapterPosition();
                if(dialogListener!=null){
                    int pos=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        dialogListener.onClickRow(pos);

                        if(itemCheckBox.getVisibility()==View.VISIBLE){
                            toggleCheckBox(pos,orderPosition);
                        }

                    }
                }
            }
        });
    }





    private void toggleCheckBox(int pos,int orderPosition) {


        if(itemCheckBox.isChecked()){

            itemCheckBox.setChecked(false);
            if(dialogItem.getServiceType().equals("1")){
                if(Fonts.isArabic){
                    Common.orderDetailModels.get(orderPosition).getOrderServices().remove(dialogItem.getServiceNameAr());
                    Common.servicesPrices.remove(dialogItem.getServiceNameAr());
                }
                else {
                    Common.orderDetailModels.get(orderPosition).getOrderServices().remove(dialogItem.getServiceNameEn());
                    Common.servicesPrices.remove(dialogItem.getServiceNameEn());

                }

                float price=Common.orderDetailModels.get(orderPosition).getServicesPrice();
                Common.orderDetailModels.get(orderPosition).setServicesPrice(price-dialogItem.getServicePrice());

            }


            else if(dialogItem.getServiceType().equals("0")) {
                if(Fonts.isArabic){
                    Common.orderDetailModels.get(orderPosition).getOrderOffers().remove(dialogItem.getServiceNameAr());
                    Common.servicesPrices.remove(dialogItem.getServiceNameAr());
                }
                else {
                    Common.orderDetailModels.get(orderPosition).getOrderOffers().remove(dialogItem.getServiceNameEn());
                    Common.servicesPrices.remove(dialogItem.getServiceNameEn());
                }

                float price=Common.orderDetailModels.get(orderPosition).getOffersPrice();
                Common.orderDetailModels.get(orderPosition).setOffersPrice(price-dialogItem.getServicePrice());
            }


        }

        else  if(!itemCheckBox.isChecked()){
            itemCheckBox.setChecked(true);
            if(dialogItem.getServiceType().equals("1")){

                if(Fonts.isArabic){
                    Common.orderDetailModels.get(orderPosition).getOrderServices().add(dialogItem.getServiceNameAr());
                    Common.servicesPrices.put(dialogItem.getServiceNameAr(),dialogItem.getServicePrice());
                }
                else {
                    Common.orderDetailModels.get(orderPosition).getOrderServices().add(dialogItem.getServiceNameEn());
                    Common.servicesPrices.put(dialogItem.getServiceNameEn(),dialogItem.getServicePrice());


                }

                float price=Common.orderDetailModels.get(orderPosition).getServicesPrice();
                Common.orderDetailModels.get(orderPosition).setServicesPrice(price+dialogItem.getServicePrice());
            }


            else if(dialogItem.getServiceType().equals("0")){
                if(Fonts.isArabic){
            Common.orderDetailModels.get(orderPosition).getOrderOffers().put(dialogItem.getServiceNameAr(),dialogItem.getOfferNamesAr());
            Common.servicesPrices.put(dialogItem.getServiceNameAr(),dialogItem.getServicePrice());

                }
            else
            {
            Common.orderDetailModels.get(orderPosition).getOrderOffers().put(dialogItem.getServiceNameEn(),dialogItem.getOfferNamesEn());
            Common.orderDetailModels.get(orderPosition).getOrderOffers().put(dialogItem.getServiceNameEn(),dialogItem.getOfferNamesEn());


            Common.servicesPrices.put(dialogItem.getServiceNameEn(),dialogItem.getServicePrice());

            }
            float price=Common.orderDetailModels.get(orderPosition).getOffersPrice();
             Common.orderDetailModels.get(orderPosition).setOffersPrice(price+dialogItem.getServicePrice());
            }

        }

    }




    public void bind(Models dialogItem, boolean last, int givenLayoutType,ArrayList<String> currentServices, HashMap<String,String> currentOffers){
        this.dialogItem=dialogItem;
        this.currentOffers=currentOffers;
        this.currentServices=currentServices;
        switch (givenLayoutType){

            case  Common.DIALOG_LAYOUT_TYPE_CITY:
                if(Fonts.isArabic){
                    itemNameTxt.setText(dialogItem.getCityArName());
                }
                else
                itemNameTxt.setText(dialogItem.getCityEnName());
                itemCheckBox.setVisibility(View.GONE);
                rightMostTxt.setVisibility(View.GONE);
                itemDetailsTxt.setVisibility(View.GONE);
                break;
            case Common.DIALOG_LAYOUT_TYPE_PROVINCE:
                itemNameTxt.setText(dialogItem.getLocation());
                itemCheckBox.setVisibility(View.GONE);
                rightMostTxt.setVisibility(View.GONE);
                itemDetailsTxt.setVisibility(View.GONE);
                break;

            case  Common.DIALOG_LAYOUT_TYPE_RATING:
                itemDetailsTxt.setVisibility(View.GONE);
                rightMostTxt.setTypeface(fonts.iconsFont);
                itemNameTxt.setText(dialogItem.getRatingTxt());
                itemCheckBox.setVisibility(View.GONE);
                RatingConfiguration ratingConfiguration=new RatingConfiguration(mContext);
                Spannable spannable=ratingConfiguration.setupText(dialogItem.getRating());
               
               rightMostTxt.setText(spannable);

                if (Common.checkedPosition == -1) {
                    root.setSelected(false);

                } else {
                    if (Common.checkedPosition == getAdapterPosition()) {

                        root.setSelected(true);
                    } else {
                        root.setSelected(false);

                    }
                    

                }


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogListener.onClickRow(getAdapterPosition());
                        root.setSelected(true);
                        if (Common.checkedPosition != getAdapterPosition()) {
                            ratingSelectedListener.invalidateRating(Common.checkedPosition);
                            Common.checkedPosition = getAdapterPosition();
                        }
                    }
                });

                break;


            case  Common.DIALOG_LAYOUT_IMAGE:
                itemDetailsTxt.setVisibility(View.GONE);
                itemCheckBox.setVisibility(View.GONE);
                itemNameTxt.setText(dialogItem.getLocation());
                rightMostTxt.setVisibility(View.GONE);
                break;


            case  Common.DIALOG_LAYOUT_TYPE_OFFER:
                if(Fonts.isArabic){
                    itemNameTxt.setText(dialogItem.getServiceNameAr());
                    rightMostTxt.setText(String.valueOf(dialogItem.getServicePrice())+"EGP");
                    String offerDetails=dialogItem.getOfferNamesAr();
                    itemDetailsTxt.setText(offerDetails);
                    if(offerDetails.equals("0")){
                    itemDetailsTxt.setVisibility(View.GONE);
                    }
                    if(Common.orderDetailModels.get(orderPosition).getOrderOffers().containsKey(dialogItem.getServiceNameAr())){
                        itemCheckBox.setChecked(true);
                    }
                }
               else{
                    itemNameTxt.setText(dialogItem.getServiceNameEn());
                    rightMostTxt.setText(String.valueOf(dialogItem.getServicePrice())+"EGP");
                    String offerDetails=dialogItem.getOfferNamesEn();
                    itemDetailsTxt.setText(offerDetails);
                    if(offerDetails.equals("0")){
                        itemDetailsTxt.setVisibility(View.GONE);
                    }

                    if(Common.orderDetailModels.get(orderPosition).getOrderOffers().containsKey(dialogItem.getServiceNameEn())){
                        itemCheckBox.setChecked(true);
                    }
                }


                break;


            case  Common.DIALOG_LAYOUT_TYPE_CONFIRM_ORDER:
             itemCheckBox.setVisibility(View.GONE);

                if(dialogItem.isInHome()) {
                    itemDetailsTxt.setVisibility(View.GONE);
                    itemNameTxt.setText(mContext.getString(R.string.in_house_cost));
                    rightMostTxt.setText(String.valueOf((int) dialogItem.getHomePrice())+"EGP");
                }

                else if(!dialogItem.isInHome()&&!dialogItem.isTotal()){
                    itemDetailsTxt.setText(dialogItem.getFormattedStrigForOrderConfirmation());
                    itemNameTxt.setText(dialogItem.getOrderPersonType().equals("adult")? mContext.getString(R.string.adult)+String.valueOf(dialogItem.getOrderId())
                            :mContext.getString(R.string.child)+String.valueOf(dialogItem.getOrderId()));
                    rightMostTxt.setText(String.valueOf((int)dialogItem.getOrderPrice())+"EGP");
                }



                if(givenLayoutType== Common.DIALOG_LAYOUT_TYPE_CONFIRM_ORDER&&dialogItem.isTotal()){
                    if(dialogItem.isInHome()){
                        rightMostTxt.setText(String.valueOf((int)Common.totalPrice)+"EGP");
                    }
                    itemNameTxt.setText(mContext.getString(R.string.total));
                    itemDetailsTxt.setVisibility(View.GONE);
                    rightMostTxt.setText(String.valueOf((int)Common.totalPrice)+"EGP");
                }

                break;

            case  Common.DIALOG_LAYOUT_TYPE_SERVICE:
                if(Fonts.isArabic){
                    itemNameTxt.setText(dialogItem.getServiceNameAr());
                    itemDetailsTxt.setVisibility(View.GONE);
                    rightMostTxt.setText(String.valueOf(dialogItem.getServicePrice())+"EGP");
                    if(Common.orderDetailModels.get(orderPosition).getOrderServices().contains(dialogItem.getServiceNameAr())){
                        itemCheckBox.setChecked(true);
                    }

                }
                else{
                    itemNameTxt.setText(dialogItem.getServiceNameEn());
                    itemDetailsTxt.setVisibility(View.GONE);
                    rightMostTxt.setText(String.valueOf(dialogItem.getServicePrice())+"EGP");
                    if(Common.orderDetailModels.get(orderPosition).getOrderServices().contains(dialogItem.getServiceNameEn())){
                        itemCheckBox.setChecked(true);
                    }

                }

                break;


            case  Common.DIALOG_LAYOUT_TYPE_MENU:
                itemNameTxt.setText(dialogItem.getMenuName());
                itemNameTxt.setTypeface(fonts.mediumFont);
                itemDetailsTxt.setVisibility(View.GONE);
                rightMostTxt.setVisibility(View.GONE);
                itemCheckBox.setVisibility(View.INVISIBLE);
                itemIcon.setVisibility(View.VISIBLE);
                itemIcon.setText(dialogItem.getIconName());
                break;


            case Common.DIALOG_LAYOUT_TYPE_ORDER_DETAILS:
             itemCheckBox.setVisibility(View.GONE);
             String person=dialogItem.getGetOrderDetailsPerson();
             if (person.equals("1"))
             itemNameTxt.setText(mContext.getString(R.string.children));
             else{
                 itemNameTxt.setText(mContext.getString(R.string.adults));
             }
             itemDetailsTxt.setText(dialogItem.getOrderDetailsServices());
             rightMostTxt.setText(dialogItem.getOrderDetailsTotal());
                break;
        }



    }



}
