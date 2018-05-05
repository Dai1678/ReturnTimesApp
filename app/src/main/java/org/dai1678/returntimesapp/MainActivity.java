package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    ArrayList<ProfileListModel> listItems = new ArrayList<>();
    ProfileListAdapter adapter;

    int clickPosition;  //onItemClickでクリックしたListViewのposition

    String nowLatLong;  //現在位置の緯度,経度
    ArrayList<String> LatLongList = new ArrayList<>();  //目的地の緯度経度
    ArrayList<String> mailAddressList = new ArrayList<>();  //連絡相手のメールアドレス

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("連絡先を選択してください");
        }

        FloatingActionButton buttonFloat = findViewById(R.id.homeFloatingButton);
        if (buttonFloat != null) {
            buttonFloat.setOnClickListener(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        ListView listView = findViewById(R.id.homeList);
        TextView emptyView = findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyView);

        pullListItem(); //RealmからListに表示する情報取得

        adapter = new ProfileListAdapter(this, R.layout.main_list_item, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

    }

    @SuppressWarnings("MissingPermission")  //TODO ここでPermissionDispatcher起動したい
    @Override
    public void onStart(){
        super.onStart();

        //端末の位置情報取得
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            Location location = task.getResult();

                            Double nowLatitude = location.getLatitude();  //緯度
                            Double nowLongitude = location.getLongitude();  //経度

                            nowLatLong = String.valueOf(nowLatitude) + "," + String.valueOf(nowLongitude);
                        }
                    }
                });
    }

    @Override
    public void onResume(){
        super.onResume();

        listItems.clear();
        pullListItem();
        adapter.notifyDataSetChanged();
    }

    private void pullListItem(){

        ProfileListModel listModel = null;
        RealmResults<ProfileRealmModel> results = realm.where(ProfileRealmModel.class).findAll();

        if(results.size() > 0){
            for (int i = 0; i < results.size(); i++) {

                ProfileRealmModel realmModel = results.get(i);

                if (realmModel != null){
                    listModel = new ProfileListModel(BitmapFactory.decodeResource(getResources(), realmModel.getImageDrawable()),
                            "行き先 : " + realmModel.getDestinationName(),
                            "場所 : " + realmModel.getPlaceName(),
                            "宛先 : " + realmModel.getContact(),
                            "メール : " + realmModel.getMail());

                    LatLongList.add(String.valueOf(realmModel.getLatitude()) + "," + String.valueOf(realmModel.getLongitude()));
                    mailAddressList.add(realmModel.getMail());
                }

                listItems.add(listModel);

            }
        }
    }

    //ListViewクリック処理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        clickPosition = position;

        String API_KEY = getString(R.string.google_maps_distance_matrix_key);
        String transmitMode = "rail";  //TODO 交通手段の設定項目を追加

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + nowLatLong + "&destinations=" + LatLongList.get(position) + "&transit_mode=" + transmitMode + "&language=ja&avoid=tolls&key=" + API_KEY;  //API処理
        new GeoTask(MainActivity.this).execute(url);
    }

    //GeoTaskクラスのインタフェースメソッド
    @Override
    public void calculationTime(String result){

        //所要帰宅時間の算出
        String res[] = result.split(","); //res[0] = 帰宅にかかる時間　res[1] = 距離
        Double min = Double.parseDouble(res[0]) / 60;  //APIから秒単位で送られているので、60で割って分単位に変えている
        //int dist = Integer.parseInt(res[1]) / 1000;
        int times = Integer.parseInt(res[0]);

        int HH = times / 3600;  //帰宅にかかる時間の時間部分抽出
        int mm = times % 3600 / 60;  //帰宅にかかる時間の分部分抽出

        String requiredTime = (int) (min / 60) + " 時間 " + (int) (min % 60) + " 分";

        //自宅到着時刻の算出
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH時mm分");  //フォーマット初期化

        //現在時刻取得
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //現在時刻に帰宅にかかる時間を足す
        calendar.set(hour,minute,second);
        calendar.add(Calendar.HOUR_OF_DAY, HH);
        calendar.add(Calendar.MINUTE, mm);

        String arrivalTime = simpleDateFormat.format(calendar.getTime());

        showReturnTimeDialog(requiredTime, arrivalTime); //ダイアログの表示

    }

    //帰宅時間結果と連絡方法選択ダイアログの表示
    private void showReturnTimeDialog(String requiredTime, String arrivalTime){
        FragmentManager fragmentManager = getFragmentManager();

        ReturnTimeDialogFragment dialogFragment = new ReturnTimeDialogFragment(requiredTime, arrivalTime, mailAddressList.get(clickPosition));
        dialogFragment.show(fragmentManager,"returnTimeDialog");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long id) {
        //削除確認ダイアログの表示
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("削除確認")
                .setMessage("削除しますか?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listItems.remove(position);
                        deleteRealmData(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(true);

        builder.show();
        return true;
    }

    //フロートボタンのクリック処理
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    private void deleteRealmData(int position){
        //Realm realm = Realm.getDefaultInstance();

        RealmResults<ProfileRealmModel> results = realm.where(ProfileRealmModel.class).findAll();
        if (results.size() > 0){
            ProfileRealmModel item = results.get(position);

            if (item != null) {
                realm.beginTransaction();
                item.deleteFromRealm();
                realm.commitTransaction();
            }
        }
    }

}
