package org.t_robop.returntimesapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.location.LocationManager.GPS_PROVIDER;

/*Replace YOUR_API_KEY in String url with your API KEY obtained by registering your application with google.

 */
public class MainActivity extends AppCompatActivity implements GeoTask.Geo, LocationListener {
    Button btn_get;
    String str_from;  //現在位置の緯度経度
    String str_to; //自宅の緯度経度
    TextView tv_result1, tv_result2, tv_result3, place;

    private static final String TAG = MainActivity.class.getSimpleName();
    // 更新時間(目安)
    private static final int LOCATION_UPDATE_MIN_TIME = 0;
    // 更新距離(目安)
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 0;

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);

        initialize();

        Intent intent = getIntent();
        str_to = intent.getStringExtra("data");

        btn_get.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestLocationUpdates();

                Log.d("test", str_from);
                Log.d("test", str_to);

                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + str_from + "&destinations=" + str_to + "&mode=train&language=ja&avoid=tolls&key=AIzaSyCRr1HoHvxqLabvjWwWe6SyYZViUuvQreo";
                new GeoTask(MainActivity.this).execute(url);
            }
        });
    }

    @Override
    public void setDouble(String result) {
        String res[] = result.split(",");
        Double min = Double.parseDouble(res[0]) / 60;
        int dist = Integer.parseInt(res[1]) / 1000;
        tv_result1.setText("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tv_result2.setText("Distance= " + dist + " kilometers");
        tv_result3.setText("現在位置:" + GeoTask.getFromPo());

    }

    public void initialize() {
        btn_get = (Button) findViewById(R.id.button_get);
        tv_result1 = (TextView) findViewById(R.id.textView_result1);
        tv_result2 = (TextView) findViewById(R.id.textView_result2);
        tv_result3 = (TextView) findViewById(R.id.textView_result3);
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

    private void requestLocationUpdates() {
        Log.e(TAG, "requestLocationUpdates()");
        //showProvider(LocationManager.NETWORK_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //showNetworkEnabled(isNetworkEnabled);
        if (isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
            String message = "Networkが無効になっています。";
            //showMessage(message);
        }
    }

    private void showLocation(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        String place = String.valueOf(latitude)+ "," + String.valueOf(longitude);

        TextView placeTextView = (TextView) findViewById(R.id.placeText);
        placeTextView.setText("緯度,経度 = " + String.valueOf(place));

        str_from = place;
    }

    public void setClick(View view){
        Intent intent = new Intent(getApplicationContext(),SetActivity.class);
        startActivity(intent);
    }
}

