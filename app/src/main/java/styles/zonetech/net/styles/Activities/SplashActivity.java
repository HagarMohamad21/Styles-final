package styles.zonetech.net.styles.Activities;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import java.util.Locale;

import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;



public class SplashActivity extends AppCompatActivity {
Context mContext=SplashActivity.this;

    private static final String TAG = "SplashActivity";
    private static final String SELECT_LANGUAGE="Locale.Helper.Selected.Language";
private static final int PERMISSION_CODE =1800 ;

    RotateAnimation rotate;
    ImageView animation_test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2000);
        rotate.setInterpolator(new AccelerateInterpolator());
        animation_test=findViewById(R.id.animation_test);
        askPermission();
        if(askForPermission()){

         mStartActivity(mContext,new HomeActivity());
            finish();
        }



        Locale language=Resources.getSystem().getConfiguration().locale;
        SharedPreferences languagePref =getSharedPreferences(getPackageName(),0);

        String lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());
        Locale locale=null;

        if(lang.equals("ar")){


            locale=new Locale("ar");

        }
        else if(lang.equals("en")){
            locale=new Locale("en");
        }

        if(locale!=null){
           getResources().getConfiguration().locale = locale;
         getResources().getConfiguration().setLocale(locale);
           getResources().updateConfiguration(getResources().getConfiguration()
                    , getResources().getDisplayMetrics());
        }

        Common.orderDetailModels.clear();

    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }

    private boolean askForPermission() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED
        )

        {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);


            return false;
        }


        else{

            return true;

        }   }

    private void askPermission(){
        Log.d(TAG, "askPermission: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        animation_test.startAnimation(rotate);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //we have permission
                //navigate to map activity


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStartActivity(mContext,new HomeActivity());
                        finish();
                    }
                },1000);
            }
            else if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED){
                finish();
            }
        }

    }

}
