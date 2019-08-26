package styles.zonetech.net.styles.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;

import styles.zonetech.net.styles.Activities.AboutActivity;
import styles.zonetech.net.styles.Activities.HomeActivity;
import styles.zonetech.net.styles.Activities.LoginActivity;
import styles.zonetech.net.styles.Activities.MapsActivity;
import styles.zonetech.net.styles.Activities.MessagesActivity;
import styles.zonetech.net.styles.Activities.OrdersActivity;
import styles.zonetech.net.styles.Activities.UserAccountActivity;
import styles.zonetech.net.styles.Adapters.DialogAdapter;
import styles.zonetech.net.styles.Listeners.DialogListener;
import styles.zonetech.net.styles.Listeners.OnLoginClicked;
import styles.zonetech.net.styles.Listeners.onCancelBtnClicked;
import styles.zonetech.net.styles.Listeners.onCloseBtnClicked;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Activities.TermsActivity;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.ItemDecoration;

import static styles.zonetech.net.styles.Utils.Common.MenuItemAbout;
import static styles.zonetech.net.styles.Utils.Common.MenuItemAccount;
import static styles.zonetech.net.styles.Utils.Common.MenuItemHome;
import static styles.zonetech.net.styles.Utils.Common.MenuItemMessages;
import static styles.zonetech.net.styles.Utils.Common.MenuItemOrders;
import static styles.zonetech.net.styles.Utils.Common.MenuItemTerms;

public class MenuDialog extends Dialog implements DialogListener  {

    Context context;
RecyclerView menuList;

    private static final int  DIALOG_LAYOUT_TYPE_MENU=10;
    private static int givenLayoutType;
ArrayList<Models>list=new ArrayList<>();
    public MenuDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_layout);
        initViews();
       initMenuList();
        getWindow().getAttributes().windowAnimations = R.style.dialogsAnimationStyle;
         givenLayoutType=DIALOG_LAYOUT_TYPE_MENU;
         setupRecyclerView(list);

    }


    private void initViews() {
        menuList=findViewById(R.id.menuList);
 }



    private void initMenuList(){
        Models itemOne=new Models(context.getString(R.string.home),context.getString(R.string.homeIcon));
        Models itemFour=new Models(context.getString(R.string.orders),context.getString(R.string.ordersIcon));
        Models itemThree=new Models(context.getString(R.string.account),context.getString(R.string.personIcon));
        Models itemTwo=new Models(context.getString(R.string.messages),context.getString(R.string.messagesIcon));
        Models itemFive=new Models(context.getString(R.string.about),context.getString(R.string.infoIcon));
        Models itemSix=new Models(context.getString(R.string.terms),context.getString(R.string.termsIcon));

      list.add(itemOne);list.add(itemFour);list.add(itemThree);list.add(itemTwo);list.add(itemFive);list.add(itemSix);
    }




    private void setupRecyclerView(ArrayList<Models> list) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        ItemDecoration itemDecoration=new ItemDecoration(context);
        DialogAdapter adapter;
        adapter=new DialogAdapter(context,list,givenLayoutType,-1, -1,null,null);
        adapter.setDialogListener(this);
        menuList.setAdapter(adapter);
        menuList.setLayoutManager(layoutManager);
        menuList.addItemDecoration(itemDecoration);


    }

    @Override
    public void onClickRow(int position) {
      //open corresponding activity

       switch (position){

           case MenuItemHome:
               if(!(context instanceof HomeActivity)){
                   mStartActivity(context,new HomeActivity());
                   dismiss();}

               else {dismiss();}

               break;


           case MenuItemMessages:
               if(Common.currentUser!=null){
                   if(!(context instanceof MessagesActivity)){
                       mStartActivity(context,new MessagesActivity());
                       dismiss();}
                   else {
                       dismiss();
                   }
               }
               else {
                   final ListsDialog dialog=new ListsDialog(context,Common.DIALOG_LAYOUT_LOGIN,-1, null, null, "", -1,null,null);
                   dialog.setOnLoginClicked(new OnLoginClicked() {
                       @Override
                       public void onLoginClicked() {
                           Bundle bun=setupBundle(Common.EXTRA_CALLING_ACTIVITY_MESSAGES);
                           Intent intent = new Intent( context , LoginActivity.class);
                           intent.putExtras(bun);
                           context.startActivity(intent);
                           dialog.dismiss();
                           dismiss();
                           if(context instanceof LoginActivity){
                               ((Activity)context).finish();
                           }

                       }
                   });


                   dialog.show();
               }
               break;


           case MenuItemAccount:
               if(Common.currentUser!=null){
                   if(!(context instanceof UserAccountActivity)){
                       mStartActivity(context,new UserAccountActivity());
                       dismiss();}

                   else {
                       dismiss();
                   }
               }
               else {
                   final ListsDialog dialog=new ListsDialog(context,Common.DIALOG_LAYOUT_LOGIN,-1, null, null, "", -1,null,null);
                   dialog.setOnLoginClicked(new OnLoginClicked() {
                       @Override
                       public void onLoginClicked() {
                           Bundle bun=setupBundle(Common.EXTRA_CALLING_ACTIVITY_USER_ACCOUNT
                           );
                           Intent intent = new Intent( context , LoginActivity.class);
                           intent.putExtras(bun);
                           context.startActivity(intent);
                           dialog.dismiss();
                           dismiss();
                           if(context instanceof LoginActivity){
                               ((Activity)context).finish();
                           }

                       }
                   });
                   dialog.show();
               }


               break;


           case MenuItemOrders:
               if(Common.currentUser!=null){
                   if(!(context instanceof OrdersActivity)){
                       mStartActivity(context,new OrdersActivity());
                       dismiss();}

                   else {
                       dismiss();
                   }
               }

               else {
                   final ListsDialog dialog=new ListsDialog(context,Common.DIALOG_LAYOUT_LOGIN,-1, null, null, "", -1,null,null);
                   dialog.setOnLoginClicked(new OnLoginClicked() {
                       @Override
                       public void onLoginClicked() {
                           Bundle bun=setupBundle(Common.EXTRA_CALLING_ACTIVITY_ORDERS);
                           Intent intent = new Intent( context , LoginActivity.class);
                           intent.putExtras(bun);
                           context.startActivity(intent);
                           dialog.dismiss();
                           dismiss();
                           if(context instanceof LoginActivity){
                               ((Activity)context).finish();
                           }

                       }
                   });
                   dialog.show();
               }

               break;

           case MenuItemAbout:
               if(!(context instanceof AboutActivity)){
                   mStartActivity(context,new AboutActivity());
                   dismiss();}
               else {
                   dismiss();
               }
               break;


           case MenuItemTerms:
               if(!(context instanceof TermsActivity)){
                   mStartActivity(context,new TermsActivity());
                   dismiss();}
               else {
                   dismiss();
               }
               break;


       }
    }

    private Bundle setupBundle(String callingActivity) {

            Bundle bun = new Bundle();
            bun.putString(Common.EXTRA_CALLING_ACTIVITY_MESSAGE, callingActivity);
            return bun;

        }



    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        context.startActivity(intent);
    }


}
