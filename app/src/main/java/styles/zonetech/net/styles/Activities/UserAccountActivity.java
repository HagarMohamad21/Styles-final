package styles.zonetech.net.styles.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.ImagesHelper;
import styles.zonetech.net.styles.Helpers.NetworkValidator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Helpers.VideoLoader;
import styles.zonetech.net.styles.Listeners.DialogDismissListener;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Server.ImageDownloader;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;
import styles.zonetech.net.styles.Utils.RoundedDrawable;



public class UserAccountActivity extends AppCompatActivity implements DialogDismissListener {

    private static final String SELECT_LANGUAGE="Locale.Helper.Selected.Language";
    static final int REQUEST_IMAGE_CAPTURE  = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    File file;
    String Action=null;
    private static final int PERMISSION_CODE =2000 ;
    private static final int DIALOG_LAYOUT_IMAGE=9;
    int givenLayoutType;
    private boolean IMAGE_SELECTED=false;
    private Uri SELECTED_IMAGE_Uri=null;
Context mContext=UserAccountActivity.this;
    Button menuBtn,backBtn,saveBtn,logoutBtn;
    TextView toolbarTitleTxt,profileImg;
    SwitchCompat languageSwitch;
    boolean Arabic;
    float density;
    private CommonMethods commonMethods;
    EditText nameEditTxt,userNameEditTxt,mobileEditTxt,passwordEditTxt;
    private EditTextValidator validator;
   IServer server;
    FrameLayout loaderLayout;
    VideoLoader videoLoader;
    Parser parser;
    String URL=null;
    NetworkValidator networkValidator;
    LoaderManager.LoaderCallbacks loaderCallbacks= new LoaderManager.LoaderCallbacks<Bitmap>() {
        @NonNull
        @Override
        public Loader<Bitmap> onCreateLoader(int i, @Nullable Bundle bundle) {

            return new ImageDownloader(mContext,URL);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap bitmap) {
            if(bitmap!=null){
                profileImg.setText("");
                RoundedDrawable roundedDrawable =new RoundedDrawable(bitmap,density,50,100,100,true, mContext);
                profileImg.setBackground(roundedDrawable);}

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Bitmap> loader) {

        }
    };
    ConstraintLayout rootSnack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        commonMethods=new CommonMethods(mContext);
        commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
        parser=new Parser(mContext);
        server=Common.getAPI();
        validator=new EditTextValidator(mContext);
        networkValidator=new NetworkValidator(mContext);
        density = mContext.getResources().getDisplayMetrics().density;
        initViews();
        videoLoader=new VideoLoader(mContext,loaderLayout);
        setListeners();
        initUserData();
    }

    private void initUserData() {

        Models currentUser=Common.currentUser;
        if(currentUser!=null){
            userNameEditTxt.setText(currentUser.getUserEmail());
            nameEditTxt.setText(currentUser.getUserName());
            if(currentUser.getUserPhone().length()<=9){
                mobileEditTxt.setText("");
            }
            else{
                mobileEditTxt.setText(currentUser.getUserPhone());
            }
            passwordEditTxt.setText(currentUser.getUserPassword());
if(Common.currentUser.getUserFacebookProfileImage()==null){
    //downloadImage
    URL=Common.currentUser.getUserProfileImage();
    if(URL!=null)
    getImage();
}

else{
    //downloadImage
    URL=Common.currentUser.getUserFacebookProfileImage();
    getImage();
}
        }

    }

    private void getImage() {
        if(networkValidator.isNetworkAvailable())
        getSupportLoaderManager().initLoader(1,null,loaderCallbacks);
        else{
            validator.showSnackbar(rootSnack,false, getString(R.string.image_failure));
        }
    }



    private void initViews() {
        menuBtn=findViewById(R.id.menuBtn);
        backBtn=findViewById(R.id.backBtn);
        toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.account));
        languageSwitch=findViewById(R.id.languageSwitch);
        profileImg=findViewById(R.id.profileImg);
        saveBtn=findViewById(R.id.saveBtn);
        nameEditTxt=findViewById(R.id.nameEditTxt);
        userNameEditTxt=findViewById(R.id.userNameEditTxt);
        mobileEditTxt=findViewById(R.id.mobileEditTxt);
        passwordEditTxt=findViewById(R.id.passwordEditTxt);
        loaderLayout=findViewById(R.id.loaderLayout);
        logoutBtn=findViewById(R.id.logoutBtn);
        Locale language= Resources.getSystem().getConfiguration().locale;
        SharedPreferences languagePref =getSharedPreferences(getPackageName(),0);
        String lang=languagePref.getString(SELECT_LANGUAGE,language.getLanguage());
        rootSnack=findViewById(R.id.rootSnack);
        if(lang.equals("ar")){
           Arabic=true;
        }
    }


    private void setListeners() {
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open picker
                ListsDialog dialog=new ListsDialog(mContext,DIALOG_LAYOUT_IMAGE,-1, null, null, "", -1,null
                ,null);
                givenLayoutType=DIALOG_LAYOUT_IMAGE;
                    dialog.setDialogDismissListener(UserAccountActivity.this);
                dialog.show();
            }
        });

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

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //save arabic language for app
                    if(!Arabic){
                        SharedPreferences languagePref =getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=languagePref.edit();
                        editor.putString(SELECT_LANGUAGE,"ar");
                        editor.apply();
                        mStartActivity(mContext,new SplashActivity());
                    }



                    else if(Arabic){
                        SharedPreferences languagePref =getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=languagePref.edit();
                        editor.putString(SELECT_LANGUAGE,"en");
                        editor.apply();
                        mStartActivity(mContext,new SplashActivity());
                    }

                }


            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator.validate(passwordEditTxt)
                        &&validator.validate(userNameEditTxt)
                        &&validator.validate(mobileEditTxt)
                        &&validator.validate(nameEditTxt)){

                   //edit account
                    String userName=nameEditTxt.getText().toString();
                    String useremail=userNameEditTxt.getText().toString();
                    String userphone=mobileEditTxt.getText().toString();
                    String password=passwordEditTxt.getText().toString();
                    if(!IMAGE_SELECTED)
                        editAccount(userName,useremail,userphone,password);
                    else editAccountWithImage(userName,useremail,userphone,password);
                }
                else{
                    //show toast
                    validator.ShowToast(getString(R.string.validation_string));
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                givenLayoutType=Common.DIALOG_LAYOUT_LOGOUT;
                ListsDialog listsDialog=new ListsDialog(mContext,givenLayoutType,-1,null,null,"",
                        0,null,null);
                listsDialog.show();
            }
        });
    }



    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

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

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            SELECTED_IMAGE_Uri = getImageUri(getApplicationContext(), imageBitmap);
            setupProfileImage(imageBitmap);

        }

        else if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){
            //data.getData returns the content URI for the selected Imageشلا
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
        RoundedDrawable roundedDrawable =new RoundedDrawable(imageBitmap,density,50,100,100,true, mContext);
        profileImg.setBackground(roundedDrawable);
        //now begin uploading
        IMAGE_SELECTED=true;


    }



    private void editAccount(final String userName, final String useremail, final String userphone, String password) {

        videoLoader.load();
        final String CurrentId= String.valueOf(Common.currentUser.getUserId());
        server.editAccount(CurrentId,userName,useremail,userphone,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    Common.currentUser=new Models();
                    Common.currentUser.setUserId(Integer.parseInt(CurrentId));
                    Common.currentUser.setUserEmail(useremail);
                    Common.currentUser.setUserPhone(userphone);
                    Common.currentUser.setUserName(userName);

                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        validator.ShowToast(getString(R.string.updateAccoutText));
                    }
                    else if(parser.getStatus().equals("error")){
                        validator.ShowToast(parser.getCodeMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
            }
        });

    }

    private void editAccountWithImage(final String userName, final String useremail, final String userphone, String password) {
        videoLoader.load();
        final String CurrentId= String.valueOf(Common.currentUser.getUserId());
        if(SELECTED_IMAGE_Uri!=null){
            String path=getPath(SELECTED_IMAGE_Uri);
            file=new File(path); }
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("userimage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody userIdRequest=RequestBody.create(MediaType.parse("text/plain"),CurrentId);
        RequestBody userNameRequest = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody userEmailRequst = RequestBody.create(MediaType.parse("text/plain"), useremail);
        RequestBody userPhoneRequest = RequestBody.create(MediaType.parse("text/plain"), userphone);
        RequestBody passwordRequest = RequestBody.create(MediaType.parse("text/plain"), password);


        server.editAccountWithImage(userIdRequest,userNameRequest,userEmailRequst,userPhoneRequest,passwordRequest,filePart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                videoLoader.stop();
                if(response.body()!=null){
                    Common.currentUser=new Models();
                    Common.currentUser.setUserId(Integer.parseInt(CurrentId));
                    Common.currentUser.setUserEmail(useremail);
                    Common.currentUser.setUserPhone(userphone);
                    Common.currentUser.setUserName(userName);

                    parser.parse(response.body());
                    if(parser.getStatus().equals("success")){
                        validator.ShowToast(getString(R.string.updateAccoutText));

                    }
                    else if(parser.getStatus().equals("error")){
                        validator.ShowToast(parser.getCodeMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                videoLoader.stop();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }
}
