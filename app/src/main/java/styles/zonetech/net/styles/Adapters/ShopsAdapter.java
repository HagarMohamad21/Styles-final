package styles.zonetech.net.styles.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import styles.zonetech.net.styles.Activities.FullscreenGallery;
import styles.zonetech.net.styles.Activities.HairDresserActivity;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.RatingConfiguration;
import styles.zonetech.net.styles.Utils.RoundedDrawable;
import styles.zonetech.net.styles.ViewHolders.ShopViewHolder;

public class ShopsAdapter extends RecyclerView.Adapter<ShopViewHolder> {


    int givenLayoutType;
    Fonts fonts;
    Context mContext;
    ArrayList<Models> shopsList;

    float density;
    Bitmap profilePic;
    RoundedDrawable roundedDrawable;
    public static final int SHOP_LAYOUT=2;
    public ShopsAdapter(Context mContext, ArrayList<Models> shopsList, int givenLayoutType) {
        this.mContext = mContext;
        this.shopsList = shopsList;
        this.givenLayoutType=givenLayoutType;
        fonts=new Fonts(mContext);

       density = mContext.getResources().getDisplayMetrics().density;

        profilePic=BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.placeholder);
         roundedDrawable =new RoundedDrawable(profilePic,density,40 , 400 , 350,true, mContext);
    }



    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View layout= LayoutInflater.from(mContext).inflate(R.layout.shops_list_item,null,false);
    return new ShopViewHolder(layout);

    }

    @Override
    public void onBindViewHolder(@NonNull final ShopViewHolder holder, final int i) {
        switch (givenLayoutType){
            case SHOP_LAYOUT:
                holder.nameTxt.setTypeface(fonts.mediumFont);
                holder.ratingBar.setTypeface(fonts.iconsFont);
                holder.paymentTxt.setTypeface(fonts.lightFont);
                holder.homeServiceTxt.setTypeface(fonts.lightFont);
                holder.homeServiceTypeTxt.setTypeface(fonts.lightFont);
                holder.cardIcon.setTypeface(fonts.iconsFont);
                holder.dollarIcon.setTypeface(fonts.iconsFont);
                if(fonts.isArabic){
                    holder.nameTxt.setText(shopsList.get(i).getSaloonArName());
                }
                else{
                    holder.nameTxt.setText(shopsList.get(i).getSaloonEnName());
                }
                if(shopsList.get(i).getCredit()==0){
                    holder.cardIcon.setVisibility(View.GONE);
                }
                if (shopsList.get(i).getHouse()==0){
                    holder.homeServiceTypeTxt.setText(mContext.getString(R.string.no));

                }
                else {
                    holder.homeServiceTypeTxt.setText(mContext.getString(R.string.yes));
                }

                RatingConfiguration ratingConfiguration=new RatingConfiguration(mContext);
                Spannable spannable =ratingConfiguration.setupText((float) shopsList.get(i).getSaloonRating());
                holder.ratingBar.setText(spannable);
                final StringBuilder Url=new StringBuilder(Common.saloonImagesUrl);
               // Log.d(TAG, "onBindViewHolder:   **************************** "+shopsList.get(i).getSaloonEnName());

                Url.append(shopsList.get(i).getSaloonId());
                Url.append(".jpg");
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                        holder.profileImg.setImageBitmap(bitmap);
                        holder.shopRoot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Common.currentSaloon=shopsList.get(i);
                                Intent intent=new Intent(mContext, HairDresserActivity.class);
                                intent.putExtra("BitmapImage",Url.toString());
                                mContext.startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        holder.shopRoot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Common.currentSaloon=shopsList.get(i);
                                Intent intent=new Intent(mContext, HairDresserActivity.class);
                                intent.putExtra("BitmapImage",Url.toString());
                                mContext.startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        holder.profileImg.setImageDrawable(placeHolderDrawable);
                    }

                };
                holder.profileImg.setTag(target);
                final int radius = 30;
                final int margin = 2;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get()
                        .load(Url.toString())
                        .transform(transformation)
                        .resize(400,350)
                        .placeholder(roundedDrawable)
                        .into(target);
                break;


        }

    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        mContext.startActivity(intent);
    }
}
