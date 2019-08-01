package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import styles.zonetech.net.styles.Activities.FullscreenGallery;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.RoundedDrawable;
import styles.zonetech.net.styles.ViewHolders.ShopViewHolder;

public class GalleryAdapter extends RecyclerView.Adapter<ShopViewHolder>  {

    ArrayList<String> galleryUrl;
    int givenLayoutType;
    Context mContext;
    float density;
    RoundedDrawable roundedDrawable;
    Bitmap galleryPic;
    public static final int GALLERY_LAYOUT=1;



    public GalleryAdapter(ArrayList<String> galleryUrl, int givenLayoutType, Context mContext) {
        this.galleryUrl = galleryUrl;
        this.givenLayoutType = givenLayoutType;
        this.mContext = mContext;
        density = mContext.getResources().getDisplayMetrics().density;
        galleryPic = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.placeholder);
        roundedDrawable =new RoundedDrawable(galleryPic,density,40 , 400 , 350,true, mContext);

    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout= LayoutInflater.from(mContext).inflate(R.layout.shops_list_item,null,false);
        return new ShopViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopViewHolder holder, int i) {
        switch (givenLayoutType){
                case GALLERY_LAYOUT:
                holder.nameRatingView.setVisibility(View.GONE);
                holder.galleryImg.setVisibility(View.VISIBLE);
                holder.profileImg.setVisibility(View.GONE);
                final StringBuilder Url=new StringBuilder(Common.galleryImagesUrl);
                Url.append(Common.currentSaloon.getSaloonId());
                Url.append("/");
                Url.append(galleryUrl.get(i));

                     Target target = new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            holder.galleryImg.setImageBitmap(bitmap);
                            holder.galleryImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(mContext, FullscreenGallery.class);
                                    intent.putExtra("BitmapImage",Url.toString());
                                  mContext.startActivity(intent);
                                }
                            });
                        }

                         @Override
                         public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                         }

                         @Override
                         public void onPrepareLoad(final Drawable placeHolderDrawable) {
                             holder.galleryImg.setImageDrawable(placeHolderDrawable);
                             holder.galleryImg.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent=new Intent(mContext, FullscreenGallery.class);
                                     intent.putExtra("NoImage", (Parcelable[]) null);
                                       mContext.startActivity(intent);
                                 }
                             });
                         }

                     };
                    holder.galleryImg.setTag(target);

                    final int radius = 30;
                    final int margin = 5;
                    final Transformation transformation = new RoundedCornersTransformation(radius, margin);

                    Picasso.get()
                            .load(Url.toString())
                            .transform(transformation)
                            .placeholder(roundedDrawable)
                            .resize(450,400)
                            .into(target);
                break;


        }

    }

    @Override
    public int getItemCount() {
        return galleryUrl.size();
    }



}
