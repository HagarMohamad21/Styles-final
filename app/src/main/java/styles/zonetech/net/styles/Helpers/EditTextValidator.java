package styles.zonetech.net.styles.Helpers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import styles.zonetech.net.styles.Listeners.NetworkResponse;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.RoundedDrawable;

public class EditTextValidator {

    NetworkResponse networkResponse;

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    Context mContext;
    boolean valid=false;
    Fonts fonts;
    public EditTextValidator(Context mContext) {
        this.mContext = mContext;
        fonts=new Fonts(mContext);
    }

    public boolean validate(TextView editText){

        if(!TextUtils.isEmpty(editText.getText().toString())){
          valid=true;
        }
else{
    // show Toast
           valid=false;
        }
       return valid; }



    public void ShowToast(String message)   {
           show(message);
    }

    private void show(String message) {
        // Get the custom layout view.
        try{
            View toastView = LayoutInflater.from(mContext).inflate(R.layout.custom_toast_view, null);
            TextView toastTxt=toastView.findViewById(R.id.toastTxt);
            toastTxt.setTypeface(fonts.lightFont);
            toastTxt.setText(message);
            // Initiate the Toast instance.
            Toast toast = new Toast(mContext);
            // Set custom view in toast.
            toast.setView(toastView);
            toast.setDuration(Toast.LENGTH_LONG);
            float yOffset= RoundedDrawable.dpTopixel(mContext,100);
           toast.setGravity(Gravity.CENTER, 0, (int) yOffset);
            toast.show();
        }
        catch (Exception e){
        }
    }

   public void showSnackbar(View rootSnack, boolean hide, String message){
       Snackbar snackbar;
        if(!message.equals("")){
            snackbar=Snackbar.make(rootSnack,message,Snackbar.LENGTH_LONG);
        }

        else{
           snackbar=Snackbar.make(rootSnack,mContext.getString(R.string.connection_validation),Snackbar.LENGTH_LONG);
        }
        if(!hide){


//            snackbar.setAction("RETRY", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // we should here retry network connection
//                    networkResponse.onRetryClicked(identifier);
//                }
//            });

            View sbView = snackbar.getView();
            sbView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.custom_snackbar_background));
            TextView textView =  sbView.findViewById(android.support.design.R.id.snackbar_text);
            TextView snackbarActionTextView =sbView.findViewById( android.support.design.R.id.snackbar_action );
            snackbarActionTextView.setTypeface(fonts.mediumFont);
            textView.setTypeface(fonts.lightFont);
            snackbar.show();
        }



    }


}
