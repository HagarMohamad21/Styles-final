package styles.zonetech.net.styles.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;

public class RatingViewHolder extends RecyclerView.ViewHolder {
       public TextView nameTxt,descriptionTxt,dateTxt;
   public TextView ratingBar;
    public RatingViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTxt=itemView.findViewById(R.id.nameTxt);
        descriptionTxt=itemView.findViewById(R.id.descriptionTxt);
        dateTxt=itemView.findViewById(R.id.dateTxt);
        ratingBar=itemView.findViewById(R.id.ratingBar);
    }

    public void bind(Models rate, Fonts fonts){
        nameTxt.setText(rate.getRatingTxt());
       descriptionTxt.setText(rate.getRatingDescription());
        dateTxt.setText(rate.getRatingDate());


        nameTxt.setTypeface(fonts.lightFont);
        descriptionTxt.setTypeface(fonts.lightFont);
        dateTxt.setTypeface(fonts.lightFont);
        ratingBar.setTypeface(fonts.iconsFont);

        dateTxt.setText(rate.getReviewDate());
        descriptionTxt.setText(rate.getReviewDescription());
        nameTxt.setText(rate.getReviewName());


    }
}
