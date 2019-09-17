package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import styles.zonetech.net.styles.Listeners.OnCategoryClicked;
import styles.zonetech.net.styles.R;

public class BottomAdapter extends RecyclerView.Adapter<BottomAdapter.BottomHolder> {
   Context context;
   ArrayList<String> Links;

    public BottomAdapter(Context context, ArrayList<String> links) {
        this.context = context;
        Links = links;
    }
    OnCategoryClicked onCategoryClicked;

    public void setOnCategoryClicked(OnCategoryClicked onCategoryClicked) {
        this.onCategoryClicked = onCategoryClicked;
    }

    @NonNull
    @Override
    public BottomHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.bottom_list_item,viewGroup,false);
        return new BottomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomHolder bottomHolder, int i) {
      bottomHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        //return Links.size();
        return 4;
    }

    class BottomHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public BottomHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
        }

        public void bind(int i) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            params.width = getPX(100);
            params.height=getPX(100);

            if(i==0){

                 params.setMarginStart(getPX(0));
                imageView.setLayoutParams(params);
            }
            else if(i==3){
                params.setMarginEnd(getPX(0));
            }


          setListeners(i);




        }

        private void setListeners(final int i) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCategoryClicked.onCategoryClicked(i+3);
                }
            });

        }

        private int getPX(int dp){
            Resources r = context.getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    r.getDisplayMetrics()
            );
       return px; }
    }
}
