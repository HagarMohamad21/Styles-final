package styles.zonetech.net.styles.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import styles.zonetech.net.styles.Listeners.OnSwipeItemClicked;
import styles.zonetech.net.styles.R;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private static final String TAG = "SliderAdapter";
    ArrayList<String>imageUrls;
     OnSwipeItemClicked onSwipeItemClicked;

    public void setOnSwipeItemClicked(OnSwipeItemClicked onSwipeItemClicked) {
        this.onSwipeItemClicked = onSwipeItemClicked;
    }

    public SliderAdapter(ArrayList<String>imageUrls) {

      this.imageUrls=imageUrls;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
                Picasso.get()
                        .load(imageUrls.get(position))
                        .into(viewHolder.imageViewBackground);
                
                
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      onSwipeItemClicked.onItemClicked();
                    }
                });
        }



    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return imageUrls.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        
        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}