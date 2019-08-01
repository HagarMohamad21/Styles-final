package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import styles.zonetech.net.styles.Activities.HairDresserActivity;
import styles.zonetech.net.styles.Listeners.EditOrderCallback;
import styles.zonetech.net.styles.Listeners.ChainedCallbackForHairdresserTotal;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.ViewHolders.OrderDetailViewHolder;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailViewHolder>
        implements EditOrderCallback ,ChainedCallbackForHairdresserTotal  {
float price;
    ChainedCallbackForHairdresserTotal chainedCallbackForHairdresserTotal;

    public void setChainedCallbackForHairdresserTotal(ChainedCallbackForHairdresserTotal chainedCallbackForHairdresserTotal) {
        this.chainedCallbackForHairdresserTotal = chainedCallbackForHairdresserTotal;
    }



     ArrayList<Models> orders;
    Context mContext;





    public OrderDetailsAdapter(ArrayList<Models> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
        try{
            if(mContext instanceof HairDresserActivity)
            ((HairDresserActivity)mContext).setEditOrderCallback(this);}
        catch (Exception e){

        }

    }


    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Layout= LayoutInflater.from(mContext).inflate(R.layout.orders_details_list_item,null,false);
        OrderDetailViewHolder orderDetailViewHolder =new OrderDetailViewHolder(Layout,mContext);
        orderDetailViewHolder.setChainedCallbackForHairdresserTotal(this);
        return orderDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder orderDetailViewHolder, int i) {
     orderDetailViewHolder.bind(orders.get(i),i);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    @Override
    public void addChild(int count) {
        Common.childCount=count;
        Models childOrder=new Models();
        childOrder.setOrderPersonType("child");
        childOrder.setOrderId(count);
        addAt(childOrder,orders.size());


    }

    @Override
    public void removeChild(int count) {
        Common.childCount=count;
        for (int i=orders.size()-1;i>=0;i--){
            if(orders.get(i).getOrderPersonType().equals("child")){
                removeAt(i);
                break;}
        }

    }

    @Override
    public void addAdult(int count) {
        Common.adultCount=count;
        Models childOrder=new Models();
        childOrder.setOrderPersonType("adult");
        childOrder.setOrderId(count);
        addAt(childOrder,orders.size());
    }

    @Override
    public void removeAdult(int count) {
        Common.adultCount=count;

        for (int i=orders.size()-1;i>=0;i--){
            if(orders.get(i).getOrderPersonType().equals("adult")){
                removeAt(i);
                break;}
        }

    }


    public void removeAt(int position) {

        orders.remove(position);
       notifyItemRemoved(position);
      //notifyItemRangeChanged(position, orders.size());
          //don't recycle the holder after this position
        if(Common.orderDetailModels.size()!=0){
            float temp=Common.orderDetailModels.get(position).getOrderPrice();
            Common.orderDetailModels.remove(position);
            chainedCallbackForHairdresserTotal.getTotal(temp,false);
        }

    }
    
    public void addAt(Models order,int position) {
        orders.add(order);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, orders.size());
        Common.orderDetailModels.add(order);
    }


    @Override
    public void getTotal(float total,boolean add) {
        chainedCallbackForHairdresserTotal.getTotal(total,add);
    }
}

