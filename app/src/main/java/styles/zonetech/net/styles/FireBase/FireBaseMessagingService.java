package styles.zonetech.net.styles.FireBase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import styles.zonetech.net.styles.R;

public class FireBaseMessagingService  extends FirebaseMessagingService  {


    private static int count = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional


//Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData());
    }
    //This method is only generating push notification

    private void sendNotification(String messageTitle, String messageBody, Map<String, String> row) {
            PendingIntent contentIntent = null;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(count, notificationBuilder.build());
            count++;
    }
}
