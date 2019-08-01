package styles.zonetech.net.styles.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.util.Locale;

import styles.zonetech.net.styles.R;

import static styles.zonetech.net.styles.Helpers.Fonts.SELECT_LANGUAGE;

public class RatingConfiguration {
    private Context mContext;

    public RatingConfiguration(Context mContext) {
        this.mContext = mContext;
    }

    public Spannable setupText(float rating){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(mContext.getString(R.string.ratingIcon)+mContext.getString(R.string.ratingIcon)+mContext.getString(R.string.ratingIcon)
                +mContext.getString(R.string.ratingIcon)+mContext.getString(R.string.ratingIcon));
        Spannable spannable;
        spannable = new SpannableString(stringBuilder);
        float ratingPure=  rating;
        int ratingRounded=Math.round(ratingPure);
        int notRatingRounded=Math.abs(ratingRounded-5);
        Locale language= Resources.getSystem().getConfiguration().locale;


        SharedPreferences languagePref =mContext.getSharedPreferences(mContext.getPackageName(),0);

        String lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());

        if(lang.equals("ar")){

            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.lightGrey)), 0, notRatingRounded, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.colorAccent)), notRatingRounded, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
 }
        else if(lang.equals("en")){
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.colorAccent)), 0, ratingRounded, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.lightGrey)), ratingRounded, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }




        return spannable;
    }
}
