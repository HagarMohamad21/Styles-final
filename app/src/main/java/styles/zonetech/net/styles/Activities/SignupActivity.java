package styles.zonetech.net.styles.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Dialogs.MenuDialog;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Listeners.DialogDismissListener;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Helpers.ImagesHelper;
import styles.zonetech.net.styles.Utils.CommonMethods;
import styles.zonetech.net.styles.Utils.RoundedDrawable;

public class SignupActivity extends AppCompatActivity implements DialogDismissListener {
    static final int REQUEST_IMAGE_CAPTURE  = 1;
     static final int REQUEST_IMAGE_GALLERY = 2;
    File  file;
    private static final int PERMISSION_CODE =2000 ;
    private static final int DIALOG_LAYOUT_IMAGE=9;
    String Action=null;
    EditTextValidator validator;
    float density;
    int givenLayoutType;
    Context mContext=SignupActivity.this;
    Button menuBtn,backBtn,registerBtn;
    TextView toolbarTitleTxt,profileImg,updateProfileTxt;
    private CommonMethods commonMethods;
    EditText nameEditTxt,userNameEditTxt,mobileEditTxt,passwordEditTxt;
    IServer server;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    Parser parser;
    private boolean IMAGE_SELECTED=false;
    private Uri SELECTED_IMAGE_Uri=null;
 boolean HasBundle=false;
    Bundle extras;
    ConstraintLayout rootSnack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        server= Common.getAPI();
        parser=new Parser(mContext);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        initViews();
        getBundle();
        validator=new EditTextValidator(mContext);
        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
        density = mContext.getResources().getDisplayMetrics().density;
    }

    private void setListeners() {
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog menuDialog=new MenuDialog(mContext);
                commonMethods.showMenu();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open picker
                ListsDialog dialog=new ListsDialog(mContext,DIALOG_LAYOUT_IMAGE,-1, null, null, "", -1,null,null);
                givenLayoutType=DIALOG_LAYOUT_IMAGE;
                dialog.setDialogDismissListener(SignupActivity.this);
                dialog.show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator.validate(passwordEditTxt)
                        &&validator.validate(userNameEditTxt)
                        &&validator.validate(mobileEditTxt)
                        &&validator.validate(nameEditTxt)){

                    String userName=nameEditTxt.getText().toString();
                    String useremail=userNameEditTxt.getText().toString();
                    String userphone=mobileEditTxt.getText().toString();
                    String password=passwordEditTxt.getText().toString();
                    //register
                    if(!IMAGE_SELECTED)
                   register("android","0",userName,useremail,userphone,password);
                    else registerWithImage("android","0",userName,useremail,userphone,password);
                }
                else{
                    //show toast
                    validator.ShowToast(getString(R.string.validation_string));
                }

            }
        });

}



    private void initViews() {
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        registerBtn=findViewById(R.id.saveBtn);
        profileImg=findViewById(R.id.profileImg);
        nameEditTxt=findViewById(R.id.nameEditTxt);
        userNameEditTxt=findViewById(R.id.userNameEditTxt);
        mobileEditTxt=findViewById(R.id.mobileEditTxt);
        passwordEditTxt=findViewById(R.id.passwordEditTxt);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.create_account));
        updateProfileTxt=findViewById(R.id.updateProfileTxt);
        loaderLayout=findViewById(R.id.loaderLayout);
        rootSnack=findViewById(R.id.rootSnack);
    }







    @Override
    public void onDialogDismissed(Models item,int pos) {
         Action=item.getLocation();
        if(givenLayoutType==DIALOG_LAYOUT_IMAGE){

            if(askForPermission()){
                openIntent(Action);
            }
        }


    }

    private void openIntent(String Action) {
        ImagesHelper helper=new ImagesHelper(mContext);
        Intent PickIntent=helper.selectImage(Action);
        if(Common.ACTION_REQUEST==REQUEST_IMAGE_CAPTURE)
        {startActivityForResult(PickIntent,REQUEST_IMAGE_CAPTURE);}
        else if(Common.ACTION_REQUEST==REQUEST_IMAGE_GALLERY){
            startActivityForResult(PickIntent,REQUEST_IMAGE_GALLERY);
        }

    }


    private boolean askForPermission()
    {
        //first check for permission if not granted ask for it
        if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(this,new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CODE);

            return false;
        }


        else{
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openIntent(Action);

            }
            else if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED){

            }
    }}



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
          // SELECTED_IMAGE_Uri= data.getData();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
           SELECTED_IMAGE_Uri = getImageUri(getApplicationContext(), imageBitmap);
            setupProfileImage(imageBitmap);

        }

        else if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){
            //data.getData returns the content URI for the selected Image
            SELECTED_IMAGE_Uri = data.getData();
            try {
                if(SELECTED_IMAGE_Uri!=null){
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), SELECTED_IMAGE_Uri);
                    setupProfileImage(imageBitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void setupProfileImage(Bitmap imageBitmap){
        profileImg.setText("");
        updateProfileTxt.setText(getString(R.string.updateIcon));
        RoundedDrawable roundedDrawable =new RoundedDrawable(imageBitmap,density,50,100,100,true, mContext);
        profileImg.setBackground(roundedDrawable);
        //now begin uploading
        IMAGE_SELECTED=true;


    }


    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        startActivity(intent);
    }


    private void register(String userdevice, final String usertoken, final String userName, final String useremail,
                          final String userphone, String password) {
        videoLoader.load();

            server.register(userdevice,usertoken,userName,useremail,userphone,password).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    videoLoader.stop();
                    if(response.body()!=null){
                        Common.currentUser=new Models();
                        Common.currentUser.setUserEmail(useremail);
                        Common.currentUser.setUserPhone(userphone);
                        Common.currentUser.setUserName(userName);

                        parser.parse(response.body());
                        if(parser.getStatus().equals("success")){
                            validator.ShowToast(getString(R.string.register_message));
                            if(HasBundle){
                                gotoCallingActivity();
                            }
                            else{
                                Intent intent=new Intent(mContext,MapsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        }
                        else if(parser.getStatus().equals("error")){
                            validator.ShowToast(parser.getCodeMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    videoLoader.stop();
                   validator.showSnackbar(rootSnack,false,"");
                }
            });
        }

    private void registerWithImage(String userdevice, final String usertoken, final String userName, final String useremail,
                                   final String userphone, String password){
        videoLoader.load();
        if(SELECTED_IMAGE_Uri!=null){
            String path=getPath(SELECTED_IMAGE_Uri);
             file=new File(path); }
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("userimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        RequestBody userDeviceRequest = RequestBody.create(MediaType.parse("text/plain"),userdevice);
        RequestBody userTokenRequset = RequestBody.create(MediaType.parse("text/plain"), usertoken);
        RequestBody userNameRequest = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody userEmailRequst = RequestBody.create(MediaType.parse("text/plain"), useremail);
        RequestBody userPhoneRequest = RequestBody.create(MediaType.parse("text/plain"), userphone);
        RequestBody passwordRequest = RequestBody.create(MediaType.parse("text/plain"), password);


        server.registerWithImage(userDeviceRequest,userTokenRequset,userNameRequest,userEmailRequst,userPhoneRequest,passwordRequest,filePart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    Common.currentUser=new Models();
                    Common.currentUser.setUserEmail(useremail);
                    Common.currentUser.setUserPhone(userphone);
                    Common.currentUser.setUserName(userName);

                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        validator.ShowToast(getString(R.string.register_message));
                        if(HasBundle){
                            gotoCallingActivity();
                        }
                        else{
                            Intent intent=new Intent(mContext,MapsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    else if(parser.getStatus().equals("error")){
                        validator.ShowToast(parser.getCodeMessage());
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

 public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private void getBundle() {
        extras = getIntent().getExtras();
        if(extras!=null){
            HasBundle=true;
        }


    }

    private void gotoCallingActivity() {
        Common.orderDetailModels.clear();
        if(extras!=null){
            String callingActivity=extras.getString(Common.EXTRA_CALLING_ACTIVITY_MESSAGE);
            ArrayList<Models> orderDetails  = extras.getParcelableArrayList(Common.EXTRA_ORDER_ARRAY_MESSAGE);
            String time=extras.getString(Common.EXTRA_TIME_MESSAGE);
            String date=extras.getString(Common.EXTRA_DATE_MESSAGE);


            if(callingActivity.equals(Common.EXTRA_CALLING_ACTIVITY_CONFIRM_ORDER)){
                Intent intent = new Intent( mContext , ConfirmOrderActivity.class);
                Bundle bun =extras;
                intent.putExtras(bun);
                startActivity(intent);
                finish();
            }


        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }
}
