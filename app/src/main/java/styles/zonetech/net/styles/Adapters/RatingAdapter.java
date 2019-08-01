package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Utils.RatingConfiguration;
import styles.zonetech.net.styles.ViewHolders.RatingViewHolder;

public class RatingAdapter extends RecyclerView.Adapter<RatingViewHolder> {
ArrayList<Models> ratingList;
Context mContext;
Fonts fonts;

    public RatingAdapter(ArrayList<Models> ratingList, Context mContext) {
        this.ratingList = ratingList;
        this.mContext = mContext;
        fonts=new Fonts(mContext);
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.rating_list_item,viewGroup,false);
        return new RatingViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder ratingViewHolder, int i) {
        RatingConfiguration ratingConfiguration=new RatingConfiguration(mContext);
        Spannable spannable=ratingConfiguration.setupText(Float.parseFloat(ratingList.get(i).getReviewRating()));
        ratingViewHolder.bind(ratingList.get(i),fonts);
        ratingViewHolder.ratingBar.setText(spannable);

    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }
}
