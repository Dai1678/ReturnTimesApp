package org.dai1678.returntimesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
//import com.google.android.gms.appindexing.AppIndex;


public class MainActivity extends AppCompatActivity  { //implements GeoTask.Geo, LocationListener

    private final int REQUEST_PERMISSION = 10;

    /*
    String strFrom;  //現在位置の緯度経度
    String strTo; //自宅の緯度経度

    TextView returnTime, distance, location, home, place, arriveHome;  //帰宅時間,距離,現在位置,緯度経度

    double latitude;  //緯度

    double longitude;  //経度

    String times;  //到着時間

    private SharedPreferences dataStore;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }else{
            StartApplication();
        }

        /*

        Intent intent = getIntent();
        strTo = intent.getStringExtra("data");  //自宅情報代入

        if(strTo == null){
            strTo = dataStore.getString("input","Nothing");
            if(strTo.equals("Nothing")){
                Toast.makeText(getApplicationContext(),"自宅設定が行われていません",Toast.LENGTH_SHORT).show();
            }
        }


        //TODO:デバッグ用
        //strTo = "35.681298,139.7640582";  //テスト(東京駅)

        if (strTo != null) {

            Log.d("test", strFrom);
            Log.d("test", strTo);


            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + strFrom + "&destinations=" + strTo + "&transit_mode=rail&language=ja&avoid=tolls&key=AIzaSyCRr1HoHvxqLabvjWwWe6SyYZViUuvQreo";  //API処理
            new GeoTask(MainActivity.this).execute(url);  //JSONデータ処理

        } else {
            Toast.makeText(getApplicationContext(), "自宅設定が行われていません", Toast.LENGTH_SHORT).show();
        }
        */

    }

    public void checkPermission() {
        //すでに許可している場合
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            StartApplication();
        }else{  //拒否していた場合
            requestLocationPermission();
        }
    }

    public void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION);
        }else{
            Toast.makeText(this, "アプリを実行できません", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_PERMISSION){
            //使用が許可された
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                StartApplication();
            }else{
                Toast.makeText(this, "アプリを実行できません", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartApplication() {
        Intent intent = new Intent(getApplication(), HomeActivity.class);
        startActivity(intent);
    }


    /*
    //表示テキスト計算
    @Override
    public void setDouble(String result) {

        String res[] = result.split(","); //res[0] = 帰宅にかかる時間　res[1] = 距離
        Double min = Double.parseDouble(res[0]) / 60;  //APIから秒単位で送られているので、60で割って分単位に変えている
        int dist = Integer.parseInt(res[1]) / 1000;
        int times = Integer.parseInt(res[0]);

        int HH = times / 3600;  //帰宅にかかる時間の時間部分抽出
        int mm = times % 3600 / 60;  //帰宅にかかる時間の分部分抽出

        arriveTime(HH, mm);  //自宅到着時刻の算出

        returnTime.setText("帰宅にかかる時間: " + (int) (min / 60) + " 時間 " + (int) (min % 60) + " 分");
        distance.setText("距離: " + dist + " キロメートル");
        location.setText("現在位置:" + GeoTask.getFromPo());
        home.setText("自宅:" + GeoTask.getToPo());

    }

    //ID設定
    public void initialize() {
        returnTime = (TextView) findViewById(R.id.textView_result1);
        distance = (TextView) findViewById(R.id.textView_result2);
        location = (TextView) findViewById(R.id.textView_result3);
        home = (TextView) findViewById(R.id.textView_result4);
        place = (TextView) findViewById(R.id.placeText);
        arriveHome = (TextView) findViewById(R.id.returnResult);
        dataStore = getSharedPreferences("DataStore", MODE_PRIVATE);
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
            Toast.makeText(getApplicationContext(), "Networkが無効になっています", Toast.LENGTH_SHORT).show();
        }
    }

    //現在地の緯度経度取得
    private void showLocation(Location location) {

        latitude = location.getLatitude();  //緯度取得
        longitude = location.getLongitude();  //経度取得

        String place = String.valueOf(latitude) + "," + String.valueOf(longitude);  //緯度経度連結

        TextView placeTextView = (TextView) findViewById(R.id.placeText);
        placeTextView.setText("現在の緯度,経度 = " + String.valueOf(place));  //現在位置の緯度経度表示

        strFrom = place;  //代入
    }

    //自宅情報取得ボタン
    public void setClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingMapsActivity.class);
        //緯度経度を飛ばす
        intent.putExtra("lat", latitude);
        intent.putExtra("lng", longitude);
        startActivity(intent);
    }

    //到着時刻算出
    public void arriveTime(int addHour, int addMinute) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH時mm分");  //フォーマット初期化

        //現在時刻取得
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //現在時刻に帰宅にかかる時間を足す
        calendar.set(hour,minute,second);  //現在時刻取得
        calendar.add(Calendar.HOUR_OF_DAY, addHour);
        calendar.add(Calendar.MINUTE, addMinute);
        //calendar.add(Calendar.SECOND, addSecond);

        Log.d("到着時刻", sdf.format(calendar.getTime()));

        times = sdf.format(calendar.getTime());

        arriveHome.setText("到着時間: " + times);  //到着時間表示

    }

    public void mailClick(View view){
        if(strTo==null){
            Toast.makeText(getApplicationContext(),"自宅設定が行われていません",Toast.LENGTH_SHORT).show();
        }
        else{
            String mailTo = "";  //宛先メールアドレス

            String subject = "帰宅連絡";  //件名

            String mailText = times+"頃に帰宅します";  //メール本文

            //宛先メールアドレスが設定されていないとき
            if(mailTo == ""){
                mailTo = dataStore.getString("SaveString","Nothing");  //SaveStringから読み出し
                //未設定時
                if(mailTo.equals("Nothing")){
                    Toast.makeText(getApplicationContext(),"メールアドレスが設定されていません",Toast.LENGTH_SHORT).show();
                }
            }

            //インテントのインスタンス生成
            Intent intent = new Intent();
            //インテントにアクション及び送信情報をセット
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+mailTo));
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,mailText);

            //メール起動
            startActivity(intent);
        }
    }

    //宛先メールアドレスのダイアログ
    public void toClick(View view){
        //テキスト入力のView
        final EditText editView = new EditText(MainActivity.this);
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("宛先メールアドレス入力")
                //setViewにてビューを設定します。
                .setView(editView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //入力した文字をトースト出力する
                        Toast.makeText(MainActivity.this,
                                editView.getText().toString(),
                                Toast.LENGTH_SHORT).show();

                        String text = editView.getText().toString();  //保存用変数に格納

                        SharedPreferences.Editor editor = dataStore.edit();
                        editor.putString("SaveString",text);  //SaveStringというkeyに紐付け
                        editor.commit();

//                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        sp.edit().putString("SaveString",editView.getText().toString()).commit();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
    */
}