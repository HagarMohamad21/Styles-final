package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Listeners.DialogDismissListener;
import styles.zonetech.net.styles.Listeners.SendRatingCallback;
import styles.zonetech.net.styles.Listeners.onCancelBtnClicked;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    ArrayList<Models> orders;
    Context mContext;
EditTextValidator validator;
    public OrdersAdapter(ArrayList<Models> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
        validator=new EditTextValidator(mContext);

    }


    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View Layout= LayoutInflater.from(mContext).inflate(R.layout.order_list_item,viewGroup,false);
        return new OrderViewHolder(Layout,mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder orderViewHolder, int i) {

            orderViewHolder.bind(orders.get(i));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder implements DialogDismissListener {
        private static final int DIALOG_LAYOUT_TYPE_RATING=5;
        private static final int DIALOG_LAYOUT_TYPE_ORDER_DETAILS=6;
        private static final int DIALOG_LAYOUT_TYPE_ORDER_CANCEL=7;
        private static final int DIALOG_LAYOUT_TYPE_ORDER_REJECTION=8;
        Context mContext;
        Fonts fonts;
        Models order;
        public TextView dateTxt,nameTxt,priceTxt,statusTxt,calendarIcon;
        public Button detailsBtn,statusRelevantBtn;
        ConstraintLayout root;

        IServer server;
        public OrderViewHolder(@NonNull View itemView,Context mContext) {
            super(itemView);
             root=itemView.findViewById(R.id.root);
            dateTxt=itemView.findViewById(R.id.dateTxt);
            nameTxt=itemView.findViewById(R.id.nameTxt);
            priceTxt=itemView.findViewById(R.id.priceTxt);
            detailsBtn=itemView.findViewById(R.id.detailsBtn);
            statusRelevantBtn=itemView.findViewById(R.id.statusRelevantBtn);
            statusTxt=itemView.findViewById(R.id.statusTxt);
            calendarIcon=itemView.findViewById(R.id.calendarIcon);
            this.mContext=mContext;
            fonts=new Fonts(mContext);
            fonts.setTypeFce(itemView);
            server= Common.getAPI();
        }



        public void bind(Models order){
            this.order=order;
            dateTxt.setText(order.getOrderSchedule());
            priceTxt.setText(String.valueOf(order.getOrderTotal())+" EGP");
            if(Fonts.isArabic)
                nameTxt.setText(order.getOrderSaloonArName());
            else
                nameTxt.setText(order.getOrderSaloonEnName());



            Typeface iconsFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/styles.ttf");
            calendarIcon.setTypeface(iconsFont);
            switch (order.getOrderStatus()){ //  {approved,rejected,pending}    //

                case "1":

                    statusTxt.setText(mContext.getString(R.string.approved));
                    statusTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_green));
                    statusRelevantBtn.setVisibility(View.INVISIBLE);
                    break;

                case "2":
                    statusTxt.setText(mContext.getString(R.string.finished));
                    statusTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_green));
                    statusRelevantBtn.setText(R.string.rate);
                    statusRelevantBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog(DIALOG_LAYOUT_TYPE_RATING);
                        }
                    });

                    break;

                case "0":
                    statusRelevantBtn.setText(R.string.cancel);
                    statusTxt.setText(mContext.getString(R.string.pending));
                    statusRelevantBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog(DIALOG_LAYOUT_TYPE_ORDER_CANCEL);
                        }
                    });
                    statusTxt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_orange));

                    break;


                case "3":
                    statusRelevantBtn.setText(R.string.reasons);
                    statusRelevantBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog(DIALOG_LAYOUT_TYPE_ORDER_REJECTION);
                        }
                    });
                    statusTxt.setText(mContext.getString(R.string.rejected));
                    statusTxt.setBackground((ContextCompat.getDrawable(mContext,R.drawable.down_rounded_corners_red)));
                    break;

            }

            detailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open details dialog
                    openDialog(DIALOG_LAYOUT_TYPE_ORDER_DETAILS);
                }
            });


        }

        private void openDialog(int givenLayoutType) {
            ListsDialog dialog;
            if(givenLayoutType==Common.DIALOG_LAYOUT_TYPE_ORDER_REJECTION){}
            else{
                dialog=new ListsDialog(mContext,givenLayoutType,-1, null, null, order.getOrderReasons(), -1,null,null);

            }
            dialog=new ListsDialog(mContext,givenLayoutType,-1, null, null, "", -1,null,null);
            if(givenLayoutType==DIALOG_LAYOUT_TYPE_RATING){
                dialog.setSendRatingCallback(new SendRatingCallback() {
                    @Override
                    public void onSendBtnClicked(int pos, String description) {
                        sendRating(pos,description);
                    }
                });
            }

            else if(givenLayoutType==DIALOG_LAYOUT_TYPE_ORDER_CANCEL){
                dialog.setOnCancelBtnClicked(new onCancelBtnClicked() {
                    @Override
                    public void onCancelBtnClicked() {
                        cancelOrder();
                    }
                });
            }

            else if(givenLayoutType==DIALOG_LAYOUT_TYPE_ORDER_DETAILS){
                try {
                    JSONArray orderDetails=new JSONArray(order.getOrderItems());
                    JSONObject orderDetailJson;
                    Models orderDetail=new Models();
                    ArrayList<Models> orderDetailsArray=new ArrayList<>();
                    for (int i=0;i<orderDetails.length();i++){
                        orderDetailJson=orderDetails.getJSONObject(i);
                        orderDetail.setOrderDetailsTotal(orderDetailJson.getString("total"));
                        orderDetail.setOrderDetailsServices(orderDetailJson.getString("services"));
                        orderDetail.setGetOrderDetailsPerson(orderDetailJson.getString("child"));
                        orderDetailsArray.add(orderDetail);


                    }

                    dialog.setOrderDetails(orderDetailsArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            dialog.setDialogDismissListener(this);
            dialog.show();
        }

        private void cancelOrder() {
            server.cancelOrder(String.valueOf(order.getOrderId())).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    orders.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    validator.ShowToast(mContext.getString(R.string.CancelOrderMessage));
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                 validator.showSnackbar(root,false,"");
                }
            });
        }

        private void sendRating(int pos, String description) {
            int rating;
            switch (pos){
                case 0:
                    rating=5; break;

                case 1:
                    rating=4; break;
                case 2: rating=3; break;
                case 3: rating=2; break;
                case 4: rating=1; break;
                default:
                    rating=5;
            }
            server.sendReview(String.valueOf(Common.currentUser.getUserId()),order.getOrderSaloonId(), String.valueOf(rating),description).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    validator.ShowToast(mContext.getString(R.string.feedback));
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                   validator.showSnackbar(root,false,"");

                }
            });
        }


        @Override
        public void onDialogDismissed(Models item, int pos) {

        }
    }

}
