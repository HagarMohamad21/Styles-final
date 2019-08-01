package styles.zonetech.net.styles.ViewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import styles.zonetech.net.styles.Activities.HairDresserActivity;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.RatingConfiguration;
import styles.zonetech.net.styles.Utils.RoundedDrawable;

public class ShopViewHolder extends RecyclerView.ViewHolder {

   public ImageView profileImg,galleryImg;
    public TextView  nameTxt;
    public TextView ratingBar,paymentTxt,homeServiceTxt,homeServiceTypeTxt,dollarIcon,cardIcon;
    public  ConstraintLayout shopRoot;
   public LinearLayout nameRatingView;

    
    public ShopViewHolder(@NonNull View itemView ) {
        super(itemView);
        profileImg=itemView.findViewById(R.id.profileImg);
        galleryImg=itemView.findViewById(R.id.galleryImg);
        nameTxt=itemView.findViewById(R.id.nameTxt);
        ratingBar=itemView.findViewById(R.id.ratingBar);
        shopRoot=itemView.findViewById(R.id.shopRoot);
        nameRatingView=itemView.findViewById(R.id.nameRatingView);
        dollarIcon=nameRatingView.findViewById(R.id.dollarIcon);
        cardIcon=nameRatingView.findViewById(R.id.cardIcon);
        paymentTxt=nameRatingView.findViewById(R.id.paymentTxt);
        homeServiceTxt=nameRatingView.findViewById(R.id.homeServiceTxt);
        homeServiceTypeTxt=nameRatingView.findViewById(R.id.homeServiceTypeTxt);

    }



    



}
