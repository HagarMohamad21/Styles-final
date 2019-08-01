package styles.zonetech.net.styles.Helpers;


import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import styles.zonetech.net.styles.R;

public class EmptyListInitiator {

    TextView errorMessageTxt,errorIconTxt;
LinearLayout linearRoot;

    public EmptyListInitiator(LinearLayout linearRoot) {
        this.linearRoot = linearRoot;
    }

    public  void setMessage(String message){
        linearRoot.setVisibility(View.VISIBLE);
        errorMessageTxt =linearRoot.findViewById(R.id.errorMessageTxt);
        errorIconTxt=linearRoot.findViewById(R.id.errorIconTxt);
        errorIconTxt.setVisibility(View.VISIBLE);
        errorMessageTxt.setText(message);
    }

    public void hideMessage(){
        linearRoot.setVisibility(View.GONE);

    }
}
