package org.t_robop.returntimesapp;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements GeoTask.Geo, LocationListener {
    Button buttonGet;  //帰宅時間計算ボタン
    String strFrom;  //現在位置の緯度経度
    String strTo; //自宅の緯度経度
    TextView tvResult1, tvResult2, tvResult3, place;  //帰宅時間,距離,現在位置,緯度経度

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LOCATION_UPDATE_MIN_TIME = 0;  // 更新時間(目安)

    private static final int LOCATION_UPDATE_MIN_DISTANCE = 0;  // 更新距離(目安)

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);

        initialize();  //ID設定

        Intent intent = getIntent();
        strTo = intent.getStringExtra("data");  //自宅情報代入
        //TODO:デバッグ用
        strTo = "35.681298,139.7640582";  //テスト(東京駅)

        buttonGet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(strTo != null){

                    requestLocationUpdates();  //現在地情報取得

                    Log.d("test", strFrom);
                    Log.d("test", strTo);


                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + strFrom + "&destinations=" + strTo + "&mode=train&language=ja&avoid=tolls&key=AIzaSyCRr1HoHvxqLabvjWwWe6SyYZViUuvQreo";  //API処理
                    new GeoTask(MainActivity.this).execute(url);  //JSONデータ処理

                }
                else{
                    Toast.makeText(getApplicationContext(),"自宅設定がおこなわれていません",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //表示テキスト計算
    @Override
    public void setDouble(String result) {
        String res[] = result.split(",");
        Double min = Double.parseDouble(res[0]) / 60;
        int dist = Integer.parseInt(res[1]) / 1000;
        tvResult1.setText("帰宅時間: " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tvResult2.setText("距離: " + dist + " kilometers");
        tvResult3.setText("現在位置:" + GeoTask.getFromPo());

    }

    //ID設定
    public void initialize() {
        buttonGet = (Button) findViewById(R.id.button_get);
        tvResult1 = (TextView) findViewById(R.id.textView_result1);
        tvResult2 = (TextView) findViewById(R.id.textView_result2);
        tvResult3 = (TextView) findViewById(R.id.textView_result3);
        place = (TextView) findViewById(R.id.placeText);

    }

    // Called when the location has changed.
    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged.");
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //現在地情報取得
    private void requestLocationUpdates() {
        Log.d(TAG, "requestLocationUpdates()");
        //showProvider(LocationManager.NETWORK_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //showNetworkEnabled(isNetworkEnabled);
        if (isNetworkEnabled) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //パーミッション許可時

                //パーミッション未許可時
            } else {
                // Show rationale and request permission.
            }
            //ここで取得
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE,
                    this);
            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                showLocation(location);
            }
        } else {
            Toast.makeText(getApplicationContext(),"Networkが向こうになっています",Toast.LENGTH_SHORT).show();
        }
    }

    //現在地の緯度経度取得
    private void showLocation(Location location) {
        double longitude = location.getLongitude();  //経度取得
        double latitude = location.getLatitude();  //緯度取得
        long time = location.getTime();

        String place = String.valueOf(latitude)+ "," + String.valueOf(longitude);  //緯度経度連結

        TextView placeTextView = (TextView) findViewById(R.id.placeText);
        placeTextView.setText("緯度,経度 = " + String.valueOf(place));

        strFrom = place;  //代入
    }

    //自宅情報取得ボタン
    public void setClick(View view){
        Intent intent = new Intent(getApplicationContext(),SetActivity.class);
        startActivity(intent);
    }
}

