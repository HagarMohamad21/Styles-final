package styles.zonetech.net.styles.FireBase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireBaseInstanceIDService  extends FirebaseInstanceIdService {

        @Override
        public void onTokenRefresh() {

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
         //Displaying token on logcat
        }
    }

