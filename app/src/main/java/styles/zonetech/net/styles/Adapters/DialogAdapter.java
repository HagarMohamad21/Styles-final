package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.HashMap;

import styles.zonetech.net.styles.Activities.ConfirmOrderActivity;
import styles.zonetech.net.styles.Listeners.ChainedCallbackForHairdresserTotal;
import styles.zonetech.net.styles.Listeners.DialogListener;
import styles.zonetech.net.styles.Listeners.RatingSelectedListener;
import styles.zonetech.net.styles.Listeners.RemoveItemsCallback;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.ViewHolders.DialogViewHolder;



public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        RatingSelectedListener, RemoveItemsCallback   {



    ArrayList<String> currentServices; HashMap<String,String> currentOffers;


    DialogListener dialogListener;
    int givenLayoutType;
    private Context mContext;
    ArrayList<Models> items;
    Models home;
int orderPosition;




    int personTotal;
       public DialogAdapter(Context mContext, ArrayList<Models> items, int givenLayoutType, int orderPosition, int personTotal,ArrayList<String>
               currentServices, HashMap<String,String> currentOffers) {
         this.personTotal=personTotal;
        this.mContext = mContext;
        this.items = items;
        this.givenLayoutType=givenLayoutType;
        this.orderPosition=orderPosition;
           this.currentOffers=currentOffers;
           this.currentServices=currentServices;
        try {
            if(mContext instanceof ConfirmOrderActivity)
                ((ConfirmOrderActivity) mContext).setRemoveItemsCallback(this);




        } catch (Exception e) {
            // ignore
        }

    }


    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View Layout;
              DialogViewHolder dialogViewHolder;

        Layout= LayoutInflater.from(mContext).inflate(R.layout.dialog_list_item,viewGroup,false);
        if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_SERVICE){
            dialogViewHolder=new DialogViewHolder(Layout,mContext,dialogListener,this,orderPosition,"SERVICE",personTotal);
            return dialogViewHolder;
        }

        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_OFFER){
             dialogViewHolder=new DialogViewHolder(Layout,mContext,dialogListener,this,orderPosition,"OFFER",personTotal);
            return dialogViewHolder;
        }


   else {
            dialogViewHolder=new DialogViewHolder(Layout,mContext,dialogListener,this,orderPosition,"",-1);
        }return dialogViewHolder; }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        switch (holder.getItemViewType()){
            case Common.DIALOG_LAYOUT_TYPE_SERVICE:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;

            case Common.DIALOG_LAYOUT_TYPE_OFFER:

                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true, givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false, givenLayoutType,currentServices,currentOffers);
                }
                break;

            case Common.DIALOG_LAYOUT_TYPE_PROVINCE:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;

            case Common.DIALOG_LAYOUT_TYPE_CITY:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;

            case Common.DIALOG_LAYOUT_TYPE_RATING:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }

                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }



                break;

            case Common.DIALOG_LAYOUT_TYPE_CONFIRM_ORDER:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;


            case Common.DIALOG_LAYOUT_IMAGE:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;


            case Common.DIALOG_LAYOUT_TYPE_MENU:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;

                 case Common.DIALOG_LAYOUT_TYPE_ORDER_DETAILS:
                if(i== items.size()-1){
                    ((DialogViewHolder) holder).bind(items.get(i),true,givenLayoutType,currentServices,currentOffers);
                }
                else {
                    ((DialogViewHolder) holder).bind(items.get(i),false,givenLayoutType,currentServices,currentOffers);
                }
                break;






        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_SERVICE){
            return Common.DIALOG_LAYOUT_TYPE_SERVICE;
        }

        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_OFFER){
            return Common.DIALOG_LAYOUT_TYPE_OFFER;
        }
        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_PROVINCE) {
            return Common.DIALOG_LAYOUT_TYPE_PROVINCE;
        }
        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_CITY){
            return Common.DIALOG_LAYOUT_TYPE_CITY;
        }
        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_RATING){
            return Common.DIALOG_LAYOUT_TYPE_RATING;
        }

        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_CONFIRM_ORDER){
            return Common.DIALOG_LAYOUT_TYPE_CONFIRM_ORDER;
        }

        else if(givenLayoutType==Common.DIALOG_LAYOUT_IMAGE){
            return Common.DIALOG_LAYOUT_IMAGE;
        }

        else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_MENU){
            return Common.DIALOG_LAYOUT_TYPE_MENU;
        }

else if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_ORDER_DETAILS)
return Common.DIALOG_LAYOUT_TYPE_ORDER_DETAILS;
        else return 0;



    }


    @Override
    public void invalidateRating(int checkedPos) {
        notifyItemChanged(checkedPos);
    }



    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    public void addAt(int position) {
        if(home!=null)
        items.add(position,home);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, items.size());
    }

    @Override
    public void removeItem() {
            if(home!=null){
                removeAt(items.size()-2);
             Common.totalPrice-=home.homePrice;
            }

        }

        @Override
        public void addItem() {
            home=new Models(true,50);
            Common.totalPrice+=home.homePrice;
            addAt(items.size()-1);
    }



}
