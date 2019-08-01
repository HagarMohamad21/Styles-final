package styles.zonetech.net.styles.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Utils.CommonMethods;

public class FullscreenGallery extends AppCompatActivity {

ImageView galleryImg;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    Context mContext=FullscreenGallery.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_gallery);
        commonMethods=new CommonMethods(mContext);

        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.gallery));
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        galleryImg=findViewById(R.id.galleryImg);
        String bitmap=getIntent().getStringExtra("BitmapImage");
        if(bitmap!=null){
            Picasso.get().load(bitmap).into(galleryImg);

        }

        else{
           Bitmap profilePic= BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.placeholder);
           galleryImg.setImageBitmap(profilePic);
        }
         setListeners();
    }

    private void setListeners() {
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonMethods.showMenu();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
