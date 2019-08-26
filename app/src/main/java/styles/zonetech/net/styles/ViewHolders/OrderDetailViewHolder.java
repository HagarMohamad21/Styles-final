package styles.zonetech.net.styles.ViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Listeners.ChainedCallbackForHairdresserTotal;
import styles.zonetech.net.styles.Listeners.onPersonTotalComputed;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;

public class OrderDetailViewHolder extends RecyclerView.ViewHolder    {

    public Button offersBtn,servicesBtn;
    public TextView priceTxt,personTxt;
    IServer server;
    Context mContext;
ChainedCallbackForHairdresserTotal chainedCallbackForHairdresserTotal;

    public void setChainedCallbackForHairdresserTotal(ChainedCallbackForHairdresserTotal chainedCallbackForHairdresserTotal) {
        this.chainedCallbackForHairdresserTotal = chainedCallbackForHairdresserTotal;
    }

    ArrayList<String>currentServices=new ArrayList<>();
    HashMap<String,String> currentOffers=new HashMap<>();

    ArrayList<Models>servicesAdult=new ArrayList<>();
    ArrayList<Models>servicesChild=new ArrayList<>();
    ArrayList<Models>offersAdult=new ArrayList<>();
    ArrayList<Models> offersChild=new ArrayList<>();
    ArrayList<Models>DialogArray=new ArrayList<>();

        public OrderDetailViewHolder(@NonNull View itemView, final Context mContext) {
        super(itemView);
        offersBtn=itemView.findViewById(R.id.offersBtn);
        servicesBtn=itemView.findViewById(R.id.servicesBtn);
        priceTxt=itemView.findViewById(R.id.priceTxt);
        personTxt=itemView.findViewById(R.id.personTxt);
        this.mContext=mContext;
        Fonts fonts=new Fonts(mContext);
        fonts.setTypeFce(itemView);
        server=Common.getAPI();
        loadServices();




        }

    private void loadServices() {
        server.getServices(String.valueOf(Common.currentSaloon.getSaloonId())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Parser parser=new Parser(mContext);
                parser.parse(response.body());
                getSaloonServicesAndOffers(parser.getServices());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }



    public void setListeners(final Models orderDetailModel){
        servicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show Services Dialog

                String orderType=orderDetailModel.getOrderPersonType();
                if(orderType.equals("child")){
                    DialogArray=servicesChild;
                }
                else if(orderType.equals("adult")){
                    DialogArray=servicesAdult;
                }
                ListsDialog listsDialog=new ListsDialog(mContext,Common.DIALOG_LAYOUT_TYPE_SERVICE,getAdapterPosition(),DialogArray,null,"",0,currentServices,currentOffers);
                listsDialog.setOnPersonTotalComputed(new onPersonTotalComputed() {
                    @Override
                    public void onPersonTotalComputed(float personTotal) {
                        priceTxt.setText(String.valueOf((int)personTotal)+"EGP");
                  chainedCallbackForHairdresserTotal.getTotal(personTotal,true);
                        if(personTotal==0){
                            priceTxt.setText("0EGP");
                        }
                    }
                });
                listsDialog.show();

            }
        });

        offersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show Services Dialog
                String orderType=orderDetailModel.getOrderPersonType();
                if(orderType.equals("child")){
                    DialogArray=offersChild;
                }
                else if(orderType.equals("adult")){
                    DialogArray=offersAdult;
                }
    ListsDialog listsDialog=new ListsDialog(mContext,Common.DIALOG_LAYOUT_TYPE_OFFER,getAdapterPosition(),DialogArray,null,"",0,currentServices,currentOffers);
                listsDialog.setOnPersonTotalComputed(new onPersonTotalComputed() {
                    @Override
                    public void onPersonTotalComputed(float personTotal) {
                        priceTxt.setText(String.valueOf((int)personTotal)+"EGP");
                        chainedCallbackForHairdresserTotal.getTotal(personTotal,true);
                        if(personTotal==0){
                            priceTxt.setText("0EGP");
                        }
                    }
                });

                listsDialog.show();

            }
        });
    }



    public void bind(Models orderDetailModel, int pos) {



    personTxt.setText(orderDetailModel.getOrderPersonType().equals("adult")? mContext.getString(R.string.adult)+String.valueOf(orderDetailModel.getOrderId())
            :mContext.getString(R.string.child)+String.valueOf(orderDetailModel.getOrderId()));

    priceTxt.setText("0EGP");
    setListeners(orderDetailModel);




    }





    private void getSaloonServicesAndOffers(ArrayList<Models> services) {
        for(int i=0;i<services.size();i++){
            if(services.get(i).getServiceType().equals("1")){
                //Log.d(TAG, "getSaloonServicesAndOffers: this is a service");
                //this is a service
                //now check if it's an adult or child service
                if(services.get(i).getServicePersonType().equals("0")){
                    //this is a service that belongs to an adult
                    servicesAdult.add(services.get(i));

                }
                else if(services.get(i).getServicePersonType().equals("1")){
                    //this is a service that belongs to a child
                    servicesChild.add(services.get(i));

                }
            }

            else if(services.get(i).getServiceType().equals("0")){
                //Log.d(TAG, "getSaloonServicesAndOffers: this is an offer");
                //this is an offer
                //now check if it's an adult or child offer
                if(services.get(i).getServicePersonType().equals("0")){
                    //this is a service that belongs to an adult
                    offersAdult.add(services.get(i));

                }
                else if(services.get(i).getServicePersonType().equals("1")){
                    //this is a service that belongs to a child
                    offersChild.add(services.get(i));

                }
            }



        }



    }


}


