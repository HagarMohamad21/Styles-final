package styles.zonetech.net.styles.Helpers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import styles.zonetech.net.styles.R;

public class NotificationHelper extends ContextWrapper {
    PendingIntent pendingIntent;
    public static String CHANNEL_ID="styles.zonetech.net.styles";
    public static String CHANNER_NAME="STYLES";
    NotificationManager notificationManager;

    public NotificationHelper(Context base,PendingIntent pendingIntent) {
        super(base);
        this.pendingIntent=pendingIntent;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            creatChannel();
    }



    @TargetApi(Build.VERSION_CODES.O)
    private void creatChannel() {
        NotificationChannel channel=new NotificationChannel(CHANNEL_ID
                ,CHANNER_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(channel);

    }
    public NotificationManager getNotificationManager(){
        if(notificationManager==null)
            notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return notificationManager ;}
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getBuilder(String title, String message, Uri sound){
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(sound)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(message);
    }
}