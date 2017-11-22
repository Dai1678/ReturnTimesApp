package org.t_robop.returntimesapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetMapAddressActivity extends AppCompatActivity implements PlaceSelectionListener, OnMapReadyCallback, View.OnClickListener {

    private static final int LOCATION_UPDATE_MIN_TIME = 0;  // 更新時間(目安)

    private static final int LOCATION_UPDATE_MIN_DISTANCE = 0;  // 更新距離(目安)

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_map_address);

        Toolbar toolbar = (Toolbar)findViewById(R.id.setMapToolbar);
        toolbar.setTitle("行き先の設定");     //TODO 意味考えて、適切な文章に変更

        ButtonFloat buttonFloat = (ButtonFloat)findViewById(R.id.setMapFloatingButton);
        if(buttonFloat != null){
            buttonFloat.setDrawableIcon(getResources().getDrawable(R.mipmap.ic_float_right));
        }
        assert buttonFloat != null;
        buttonFloat.setOnClickListener(this);

        //TODO 入力フォームバグ すぐに消えてしまう
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO 現在地付近からマップ描画
        LatLng sydney = new LatLng(35.68,139.75);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Japan")); //マーカーはできてる
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    //TODO 場所の選択、選択した場所の住所を保存

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SetMapAddressActivity.this,SetMailDetailActivity.class);
        startActivity(intent);
    }
}
