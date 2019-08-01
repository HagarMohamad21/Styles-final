package styles.zonetech.net.styles.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Fonts {
    Context mContext;
    public Typeface  iconsFont,lightFont,mediumFont;

 public static boolean isArabic;
    public static final String SELECT_LANGUAGE="Locale.Helper.Selected.Language";

    public Fonts(Context mContext) {
        this.mContext = mContext;
        Locale language= Resources.getSystem().getConfiguration().locale;
        SharedPreferences langPref=mContext.getSharedPreferences(mContext.getPackageName(),0);
        String lang=langPref.getString(SELECT_LANGUAGE,language.getLanguage());

        if(lang.equals("ar")){
            isArabic=true;
            mediumFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/Cairo-Bold.ttf");
            lightFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/Cairo-Regular.ttf");
        }
      else {
          isArabic=false;
            mediumFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/englishlight.ttf");
            lightFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/englishbold.ttf");
        }

        iconsFont=Typeface.createFromAsset(mContext.getAssets(),"fonts/styles.ttf");
    }


    public  void setTypeFce(View view){
    if(view instanceof ViewGroup)
    {
        ViewGroup viewgroup = (ViewGroup) view;
        for (int i = 0 ; i < viewgroup.getChildCount() ; i ++)
        {
            View child = viewgroup.getChildAt(i);
            setTypeFce(child);
        }
    }
        else if(view instanceof TextView){


        if(view.getTag() != null)
        {
            String tag = view.getTag().toString();
            if(tag.equals("icon"))
            {
                ((TextView) view).setTypeface(iconsFont);

            }
            else if(tag.equals("regularFont"))
            {
                ((TextView) view).setTypeface(lightFont);

            }
            else if(tag.equals("bold")){
                ((TextView) view).setTypeface(mediumFont);

            }

        }
    }

}

}
