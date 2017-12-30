package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/***************************************************************************************************
 MainActivity    初期表示画面
 機能：    連絡先の選択
 現在地から帰宅先までの所要時間・予想到着時間の表示
 メールorLINEへの移動
 ***************************************************************************************************/

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, GeoTask.Geo {

    private FusedLocationProviderClient fusedLocationProviderClient;
    protected Location location;

    //ListItem情報： 場所画像id、宛先名、行き先、メアド
    ArrayList<String> destinationList = null;
    ArrayList<String> placeList = null;
    ArrayList<String> addressNameList = null;
    ArrayList<String> addressMailList = null;

    ArrayList<CustomHomeListItem> listItems;
    CustomHomeListAdapter adapter;

    String nowLatLong;  //現在位置の緯度,経度
    ArrayList<String> destinationLatLong; //行き先の緯度,経度

    String requiredTime; //帰宅所要時間
    String arrivalTime; //予想到着時刻

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Realm init
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        //Realm.deleteRealm(realmConfig);
        realm = Realm.getInstance(realmConfig);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        toolbar.setTitle("連絡先を選択してください");
        setSupportActionBar(toolbar);

        //右下フロートボタン
        ButtonFloat buttonFloat = findViewById(R.id.homeFloatingButton);
        if (buttonFloat != null) {
            buttonFloat.setDrawableIcon(getResources().getDrawable(R.drawable.ic_float_button));
        }
        assert buttonFloat != null;
        buttonFloat.setOnClickListener(this);

        //位置情報
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //ListView処理
        ListView listView = findViewById(R.id.homeList);
        TextView emptyView = findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyView);

        destinationList = new ArrayList<>();
        placeList = new ArrayList<>();
        addressNameList = new ArrayList<>();
        addressMailList = new ArrayList<>();

        destinationLatLong = new ArrayList<>();

        listItems = new ArrayList<>();

        RealmResults<ProfileItems> results = realm.where(ProfileItems.class).findAll();
        ProfileItems profileItems;

        if(results.size() > 0){
            for (int i = 0; i < results.size(); i++) {

                profileItems = results.get(i);
                assert profileItems != null;
                Log.i("ID", profileItems.getProfileId().toString());
                destinationList.add(profileItems.getDestinationName());
                placeList.add(profileItems.getPlaceName());
                Log.i("LATITUDE", String.valueOf(profileItems.getLatitude()));
                Log.i("LONGITUDE", String.valueOf(profileItems.getLongitude()));
                destinationLatLong.add(String.valueOf(profileItems.getLatitude()) + "," + String.valueOf(profileItems.getLongitude()));
                addressNameList.add(profileItems.getContact());
                addressMailList.add(profileItems.getMail());

                //TODO 行き先によって画像変更
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_house);

                CustomHomeListItem item = new CustomHomeListItem(bmp, "行き先 : " + destinationList.get(i), "場所 : " + placeList.get(i)
                        , "宛先" + addressNameList.get(i), "メール" + addressMailList.get(i));
                listItems.add(item);
            }
        }

        adapter = new CustomHomeListAdapter(this, R.layout.main_list_item, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        lastLocation();
    }

    @Override
    public void onResume(){
        super.onResume();

        destinationList.clear();
        placeList.clear();
        addressNameList.clear();
        addressMailList.clear();
        listItems.clear();

        RealmResults<ProfileItems> results = realm.where(ProfileItems.class).findAll();
        ProfileItems profileItems;

        if(results.size() > 0){
            for (int i = 0; i < results.size(); i++) {

                profileItems = results.get(i);
                assert profileItems != null;
                Log.i("ID", profileItems.getProfileId().toString());
                destinationList.add(profileItems.getDestinationName());
                placeList.add(profileItems.getPlaceName());
                Log.i("LATITUDE", String.valueOf(profileItems.getLatitude()));
                Log.i("LONGITUDE", String.valueOf(profileItems.getLongitude()));
                destinationLatLong.add(String.valueOf(profileItems.getLatitude()) + "," + String.valueOf(profileItems.getLongitude()));
                addressNameList.add(profileItems.getContact());
                addressMailList.add(profileItems.getMail());

                //TODO 行き先によって画像変更
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_house);

                CustomHomeListItem item = new CustomHomeListItem(bmp, "行き先 : " + destinationList.get(i), "場所 : " + placeList.get(i)
                        , "宛先 : " + addressNameList.get(i), "メール : " + addressMailList.get(i));
                listItems.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }

    //ListViewクリック処理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        //TODO ListViewクリック時だとAPI処理が追いつかずにnull表示になるため、プログレスバーの導入
        String API_KEY = getString(R.string.google_maps_distance_matrix_key);
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + nowLatLong + "&destinations=" + destinationLatLong.get(position) + "&transit_mode=rail&language=ja&avoid=tolls&key=" + API_KEY;  //API処理
        new GeoTask(MainActivity.this).execute(url);

        FragmentManager fragmentManager = getFragmentManager();

        ReturnTimeDialogFragment dialogFragment = new ReturnTimeDialogFragment(requiredTime, arrivalTime, addressMailList.get(position));
        dialogFragment.show(fragmentManager,"alert dialog");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO Listの削除と該当データベースの削除
        return false;
    }

    //フロートボタンのクリック処理
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,SettingProfileActivity.class);
        startActivity(intent);
    }

    //端末の位置情報取得
    @SuppressWarnings("MissingPermission")
    private void lastLocation(){
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            location = task.getResult();

                            Double nowLatitude = location.getLatitude();  //緯度
                            Double nowLongitude = location.getLongitude();  //経度

                            nowLatLong = String.valueOf(nowLatitude) + "," + String.valueOf(nowLongitude);
                            Log.i("nowPosition", nowLatLong);
                        }
                    }
                });
    }

    //GeoTaskクラスのインタフェースメソッド
    @Override
    public void setDouble(String result){

        String res[] = result.split(","); //res[0] = 帰宅にかかる時間　res[1] = 距離
        Double min = Double.parseDouble(res[0]) / 60;  //APIから秒単位で送られているので、60で割って分単位に変えている
        int dist = Integer.parseInt(res[1]) / 1000;
        int times = Integer.parseInt(res[0]);

        int HH = times / 3600;  //帰宅にかかる時間の時間部分抽出
        int mm = times % 3600 / 60;  //帰宅にかかる時間の分部分抽出

        requiredTime = (int) (min / 60) + " 時間 " + (int) (min % 60) + " 分";

        Log.i("requiredTime", "所要時間 : " + requiredTime);
        Log.i("dist", "距離: " + dist + " キロメートル");

        arriveTime(HH, mm);  //自宅到着時刻の算出

    }

    public void arriveTime(int addHour, int addMinute){

        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
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

        arrivalTime = sdf.format(calendar.getTime());
        Log.i("arrivalTime", "予想到着時刻 : " + arrivalTime);

    }
}
