package org.dai1678.returntimesapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/***************************************************************************************************
 HomeActivity    初期表示画面
 機能：    連絡先の選択
 現在地から帰宅先までの所要時間・予想到着時間の表示
 メールorLINEへの移動
 ***************************************************************************************************/

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    //Fused Location Provider API
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Location Settings APIs
    private SettingsClient settingsClient;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location location;

    private String lastUpdateTime;
    private Boolean requestingLocationUpdates;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private int priority = 0;

    double fuseData[] = new double[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        toolbar.setTitle("連絡先を選択してください");

        //右下フロートボタン
        ButtonFloat buttonFloat = (ButtonFloat) findViewById(R.id.homeFloatingButton);
        if (buttonFloat != null) {
            buttonFloat.setDrawableIcon(getResources().getDrawable(R.drawable.ic_float_button));
        }
        assert buttonFloat != null;
        buttonFloat.setOnClickListener(this);

        //ListView処理
        ListView listView = findViewById(R.id.homeList);
        TextView emptyView = findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyView);

        ArrayList<CustomHomeListItem> listItems = new ArrayList<>();
        //ToDO RealmからListItem情報を取得して表示
        //ListItem情報： 場所画像id、宛先名、行き先、メアド
        String[] addressNameArray = {"母親"};
        String[] destinationArray = {"自宅"};
        String[] addressMailArray = {"example@gmail.com"};

        for (int i = 0; i < addressNameArray.length; i++) {
            //TODO 行き先によって画像変更  int型の画像idでswitchしたほうがいいかも
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_house);
            //TODO ↓が1つのリストに入る情報なので、: 後を変数管理していく
            CustomHomeListItem item = new CustomHomeListItem(bmp, "宛先 : " + addressNameArray[i], "行き先 : " + destinationArray[i], "E-mail : " + addressMailArray[i]);
            listItems.add(item);
        }


        CustomHomeListAdapter adapter = new CustomHomeListAdapter(this, R.layout.home_list_item, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        //位置情報


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        priority = 0;

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

        startLocationUpdates();
    }

    //ListViewクリック処理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //TODO プログレスバーで待機しながら、現在地から目的地までの所要時間と予想到着時間を算出 -> メールとLINEの選択

        ListView listView = (ListView) adapterView;
        CustomHomeListItem item = (CustomHomeListItem)listView.getItemAtPosition(position);

        FragmentManager fragmentManager = getFragmentManager();

        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(fragmentManager,"alert dialog");
    }

    //TODO onItemLongClickで削除処理の実装
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        return false;
    }

    //フロートボタンのクリック処理
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,SettingProfileActivity.class);
        startActivity(intent);
    }

    //locationのコールバック取得
    public void createLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                super.onLocationResult(locationResult);

                location = locationResult.getLastLocation();

                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void updateLocationUI(){
        //getLastLocation()からの情報がある場合のみ
        if(location != null){

            fuseData[0] = location.getLatitude();
            fuseData[1] = location.getLongitude();
            fuseData[2] = location.getAccuracy();
            fuseData[3] = location.getAltitude();
            fuseData[4] = location.getSpeed();
            fuseData[5] = location.getBearing();

        }
    }

    public Double getFuseData(int position){
        return fuseData[position];
    }

    private void createLocationRequest(){
        locationRequest = new LocationRequest();

        if(priority == 0){
            //高い精度での位置情報    インターバル:5000msec
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }else if(priority == 1){
            //バッテリー消費を抑えたい時 インターバル:100msec
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }else if(priority == 2){
            //バッテリー消費をさらに抑えたい時
            locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }else{
            //他のアプリで得られた位置情報を使う時
            locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
        }

        locationRequest.setInterval(60000);

        locationRequest.setFastestInterval(5000);
    }

    //端末で測位できる状態か確認する   WIFI,GPSがOFFになってるときにエラーダイアログを出す
    private void buildLocationSettingsRequest(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        Log.i("debug","User agreed to make required location settings");
                        break;

                    case Activity.RESULT_CANCELED:
                        Log.i("debug","User chose not to make required location settings");
                        requestingLocationUpdates = false;
                        break;
                }
        }
    }

    //FusedLocationApiによるlocation updateをリクエスト
    private void startLocationUpdates(){
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("debug","Allo location settings are satisfied");

                        //permission check
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();

                        switch (statusCode){
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("debug", "Location settings are not satisfied");

                                try{
                                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(HomeActivity.this,REQUEST_CHECK_SETTINGS);
                                }catch (IntentSender.SendIntentException sie){
                                    Log.i("debug","PendingIntent unable to execute request");
                                }
                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be fiexed here. Fix in Settings";
                                Log.e("debug", errorMessage);

                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                                requestingLocationUpdates = false;
                        }
                    }
                });

        requestingLocationUpdates = true;
    }



    public void stopLocationUpdates(){
        if(!requestingLocationUpdates){
            Log.d("debug","stopLocationUpdates: updates never requested, no-op");
            return;
        }

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        requestingLocationUpdates = false;
                    }
                });
    }

    @Override
    protected void onPause(){
        super.onPause();

        stopLocationUpdates();
    }


}
