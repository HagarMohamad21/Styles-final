package styles.zonetech.net.styles.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Adapters.GalleryAdapter;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.EmptyListInitiator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;

public class GalleryActivity extends AppCompatActivity {

    public static final int GALLERY_LAYOUT=1;
    ArrayList<String> gallery=new ArrayList();
    RecyclerView galleryList;
    Context mContext=GalleryActivity.this;
    Button menuBtn,backBtn;
    TextView toolbarTitleTxt;
    private CommonMethods commonMethods;
    LinearLayout linearRoot;
    EmptyListInitiator emptyListInitiator;
    IServer server;
    Parser parser;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
ConstraintLayout rootSnack;
    EditTextValidator validator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initViews();
        videoLoader=new VideoLoader(mContext,loaderLayout);
        validator=new EditTextValidator(mContext);
        parser=new Parser(mContext);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        server= Common.getAPI();
        getGalleryList();
        setListeners();


    }

    private void populateGalleryList() {
        galleryList.setLayoutManager(new GridLayoutManager(mContext,2));
        GalleryAdapter adapter=new GalleryAdapter(gallery,GALLERY_LAYOUT,mContext);
        galleryList.setAdapter(adapter);

    }


    private void initViews() {
        galleryList=findViewById(R.id.galleryList);
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.gallery));
        linearRoot=findViewById(R.id.linearRoot);
        emptyListInitiator=new EmptyListInitiator(linearRoot);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.rootSnack);

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



    private void getGalleryList() {
        videoLoader.load();
        server.getGallery(String.valueOf(Common.currentSaloon.getSaloonId())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                parser.parse(response.body());
                if((parser.getStatus().equals("success"))){
                 gallery=parser.getGallery();
                 if(gallery.size()>0){
                     emptyListInitiator.hideMessage();
                     populateGalleryList();
                 }

                   else{
                         emptyListInitiator.setMessage(getString(R.string.no_images));
                     }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
                validator.showSnackbar(rootSnack,false, "");
            }
        });
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }

}
