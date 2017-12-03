package org.dai1678.returntimesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SettingMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Marker mMarker;
    SupportMapFragment SmF;

    double lat;
    double lng;

    String text;  //自宅情報(緯度経度)

    private SharedPreferences dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_maps);

        dataStore = getSharedPreferences("DataStore",MODE_PRIVATE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        //現在位置情報
        lat = intent.getDoubleExtra("lat",0);
        lng = intent.getDoubleExtra("lng",0);
    }

    //自宅情報送信
    public void sendClick(View view){

        //text=editText.getText().toString();

        Intent intent = new Intent(SettingMapsActivity.this,MainActivity.class);
        intent.putExtra("data",text);

        SharedPreferences.Editor editor = dataStore.edit();
        editor.putString("input",text);  //inputというkeyに紐付け
        editor.commit();

        if(text != null){  //文字数判定
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"適切な入力をしてください",Toast.LENGTH_SHORT).show();
        }
    }

    //キャンセルボタン
    public void cancelClick(View view){
        Intent intent = new Intent(SettingMapsActivity.this,MainActivity.class);
        startActivity(intent);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //現在位置を設定
        LatLng from = new LatLng(lat, lng);
        // マーカーの設定
        MarkerOptions options = new MarkerOptions();
        options.position(from);
        options.snippet(from.toString());
        // マップに初期マーカーを追加
        mMarker = mMap.addMarker(options);
        //現在位置のカメラを移動
        mMap.moveCamera(CameraUpdateFactory.newLatLng(from));

        cameraZoom(from);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                String nowPo=latLng.latitude+","+latLng.longitude;

                text=nowPo;



                if(mMarker!=null) {
                    mMarker.remove();
                }

                // マーカーの設定
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.snippet(latLng.toString());

                // マップにマーカーを追加
                mMarker = mMap.addMarker(options);
            }
        });
    }

    private void cameraZoom( LatLng location ) {
        float zoom = 17.0f; //ズームレベル
        float tilt = 0.0f; // 0.0 - 90.0  //チルトアングル
        float bearing = 0.0f; //向き
        CameraPosition pos = new CameraPosition(location, zoom, tilt, bearing); //CameraUpdate
        CameraUpdate camera = CameraUpdateFactory.newCameraPosition(pos);
        mMap.moveCamera(camera);
    }

}
