package styles.zonetech.net.styles.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import styles.zonetech.net.styles.Dialogs.ListsDialog;
import styles.zonetech.net.styles.Helpers.EditTextValidator;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.Helpers.NetworkValidator;
import styles.zonetech.net.styles.Helpers.Parser;
import styles.zonetech.net.styles.Listeners.DialogDismissListener;
import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Server.IServer;
import styles.zonetech.net.styles.Utils.Common;
import styles.zonetech.net.styles.Utils.CommonMethods;
import static styles.zonetech.net.styles.Utils.Common.DIALOG_LAYOUT_TYPE_CITY;
import static styles.zonetech.net.styles.Utils.Common.DIALOG_LAYOUT_TYPE_PROVINCE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DialogDismissListener {
    private boolean SearchIsOpen=false;

    private Context mContext=MapsActivity.this;
    private GoogleMap mMap;
    TextView locationBtn;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Button areaBtn,browseBtn;
    TextView provinceDialog,cityDialog,dropicon1,dropicon,toolbarTitleTxt;
    Button menuBtn,backBtn;
    LinearLayout mapView;
    ListsDialog dialog;
    RadioGroup radioGroup;
    int givenLayoutType;
    private CommonMethods commonMethods;
    LatLng Location=null;
     Marker marker;
    FrameLayout loaderLayout;
    Parser parser;
    EditTextValidator validator;
    String cityId=null,provinceid=null,categoryid,subCategory;
    RadioButton maleRadio,femaleRadio;
    ConstraintLayout rootSnack;
    boolean FEMALE=false,MALE=false;
    IServer server;
    private static final String TAG = "MapsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);
            commonMethods=new CommonMethods(mContext);
            commonMethods.setupFont(findViewById(android.R.id.content));
        commonMethods.setupMenu();
            server=Common.getAPI();
           Common.orderDetailModels.clear();
            getCities(false);
            parser=new Parser(mContext);
            validator=new EditTextValidator(mContext);
            initView();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            setListeners();
        subCategory=getIntent().getStringExtra("subCategory");

        }


     private void initView() {
        locationBtn=findViewById(R.id.locationBtn);
        areaBtn=findViewById(R.id.areaBtn);
        browseBtn=findViewById(R.id.browseBtn);
        mapView=findViewById(R.id.mapView);
         provinceDialog =findViewById(R.id.cityDialog);
        cityDialog=findViewById(R.id.areaDialog);
         menuBtn=findViewById(R.id.menuBtn);
         backBtn=findViewById(R.id.backBtn);
        dropicon1=findViewById(R.id.dropicon1);
        dropicon=findViewById(R.id.dropicon);
        radioGroup=findViewById(R.id.radioGroup);
        loaderLayout=findViewById(R.id.loaderLayout);
        femaleRadio=findViewById(R.id.femaleRadio);
        maleRadio=findViewById(R.id.maleRadio);
        rootSnack=findViewById(R.id.rootSnack);
         toolbarTitleTxt=findViewById(R.id.toolbarTitleTxt);
        toolbarTitleTxt.setText(getString(R.string.browsing));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));
        //get location
        buildLocationRequests();
        buildLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        requestLocationUpdates();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latlng) {
               mMap.clear();

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latlng)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                System.out.println(latlng);
                Location=latlng;

            }
        });

    }

    private void buildLocationRequests() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                displayLocation(locationResult);


            }
        };


    }

    private void displayLocation(LocationResult locationResult) {
        if(mMap!=null)
            mMap.clear();
        LatLng userLocation = new LatLng(locationResult.getLastLocation().getLatitude()
                , locationResult.getLastLocation().getLongitude());
        Location=userLocation;
        MarkerOptions markerOptions=new
                MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title(getString(R.string.user_location))
                .position(userLocation)
                .draggable(true);
        if(mMap!=null){
            mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17.0f));}
    }

    @Override
    protected void onStop() {
        if (locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (locationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onPause();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        requestLocationUpdates();

        super.onResume();
    }

    private void setListeners() {

        areaBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Location=null;
                //change text color to white
                areaBtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                // change  areaBtn drawable to yellow
                areaBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.right_button_rect_yellow));

                // change  locationBtn drawable to yellow
                locationBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.left_button_rect_grey));
                //change locationBtn icon color to black
                locationBtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                //hide the map
                mapView.setVisibility(View.INVISIBLE);
                //show spinners
                provinceDialog.setVisibility(View.VISIBLE);
                cityDialog.setVisibility(View.VISIBLE);
                dropicon1.setVisibility(View.VISIBLE);
                dropicon.setVisibility(View.VISIBLE);

            }
        });
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                requestLocationUpdates();
                provinceid=null;cityId=null;
                provinceDialog.setText(getString(R.string.select_city));
                cityDialog.setText(getString(R.string.select_area));
                // change  areaBtn drawable to yellow
                locationBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.left_button_rect_yellow));

                // change  locationBtn drawable to yellow
                areaBtn.setBackground(ContextCompat.getDrawable(mContext,R.drawable.right_button_rect_grey));
                locationBtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));

                //change text color to black
                areaBtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                //request location updates

                //show the map
                mapView.setVisibility(View.VISIBLE);
                //hide spinners
                provinceDialog.setVisibility(View.INVISIBLE);
                cityDialog.setVisibility(View.INVISIBLE);
                dropicon1.setVisibility(View.INVISIBLE);
                dropicon.setVisibility(View.INVISIBLE);

            }
        });
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SearchIsOpen){

                }
                else{
                    if(FEMALE||MALE){
                        if((provinceid==null&&cityId==null)||provinceid==null||cityId==null){

                            //means user didn't select province so try location


                            if(Location!=null){

                                Intent intent=new Intent(mContext,HairDressersActivity.class);
                                intent.putExtra("callingMethod","browseByLocation");
                                intent.putExtra("latitude",Location.latitude);
                                 intent.putExtra("longitude",Location.longitude);
                                intent.putExtra("gender",categoryid);
                                intent.putExtra("subCategory",subCategory);
                                startActivity(intent);

                            }
                            else if(Location==null) {
                                if((provinceid==null&&cityId==null)||provinceid==null||cityId==null){
                                    validator.ShowToast(getString(R.string.validation_string));
                                }


                            }
                        }

                        else if(cityId!=null&provinceid!=null) {
                            Intent intent=new Intent(mContext,HairDressersActivity.class);
                            intent.putExtra("callingMethod","browseByProvince");
                            intent.putExtra("cityId",cityId);
                            intent.putExtra("gender",categoryid);
                            intent.putExtra("subCategory",subCategory);

                            startActivity(intent);
                        }
                    }
                    else{

                        EditTextValidator validator=new EditTextValidator(mContext);
                        validator.ShowToast(getString(R.string.validation_string));

                    }
                }


            }
        });


        provinceDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new ListsDialog(mContext, DIALOG_LAYOUT_TYPE_PROVINCE,-1, null, null, "", -1,null,null);
                givenLayoutType= DIALOG_LAYOUT_TYPE_PROVINCE;
                dialog.setDialogDismissListener(MapsActivity.this);
                if(Common.cities.size()==0)
                getCities(true);
                else {
                    dialog.show();
                }
            }
        });

        cityDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new ListsDialog(mContext, DIALOG_LAYOUT_TYPE_CITY,-1, null, null, "", -1,null,null);
                givenLayoutType= DIALOG_LAYOUT_TYPE_CITY;
                dialog.setDialogDismissListener(MapsActivity.this);
                if(Common.cities.size()==0)
                    getCities(true);
                else {
                    dialog.show();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.maleRadio:
                        MALE=true;
                        categoryid="1";
                        break;
                    case R.id.femaleRadio:
                        FEMALE=true;
                        categoryid="2";
                        break;

                }
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
//                Common.orderDetailModels.clear();
                finish();
            }
        });
    }

    public void mStartActivity(Context context, Activity activity){
        Intent intent=new Intent(context,activity.getClass());
       startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(){
        if (locationCallback != null){

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

    }

    @Override
    public void onDialogDismissed(Models item,int pos) {

        if(givenLayoutType== DIALOG_LAYOUT_TYPE_PROVINCE ){
            provinceDialog.setText(item.getLocation());

             provinceid = String.valueOf(pos + 1);

        }


        else if(givenLayoutType== DIALOG_LAYOUT_TYPE_CITY){
            if(Fonts.isArabic){
                cityDialog.setText(item.getCityArName());
            }
            else{
                cityDialog.setText(item.getCityEnName());
            }
            cityId= String.valueOf(pos+1);

        }



    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        commonMethods.destroyMenu();
    }


    public void getCities(final boolean fromDialog){
        Common.cities.clear();
        server.getCities().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body()!=null){
                    Parser parser=new Parser(mContext);
                    parser.parse(response.body());
                   if(fromDialog){
                       if(!dialog.isShowing())
                       dialog.show();
                   }

                }

            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
              if(fromDialog)
              {
                  validator.showSnackbar(rootSnack,false, "");
              }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        categoryid=null;
    }
}
