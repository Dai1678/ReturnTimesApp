package org.dai1678.returntimesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/***************************************************************************************************
 MainActivity    初期表示画面
 機能：    連絡先の選択
 現在地から帰宅先までの所要時間・予想到着時間の表示
 メールorLINEへの移動
 ***************************************************************************************************/

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    private FusedLocationProviderClient fusedLocationProviderClient;
    protected Location location;

    double latitude;
    double longitude;

    List<String> addressNameList;
    List<String> destinationList;
    List<String> addressMailList;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Realm init
        Realm.init(this);
        //RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        //Realm.setDefaultConfiguration(configuration);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.homeToolbar);
        toolbar.setTitle("連絡先を選択してください");

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

        ArrayList<CustomHomeListItem> listItems = new ArrayList<>();
        //ToDO RealmからListItem情報を取得して表示
        //ListItem情報： 場所画像id、宛先名、行き先、メアド
        String[] addressNameArray = {"母親"};
        String[] destinationArray = {"自宅"};
        String[] addressMailArray = {"example@gmail.com"};

        /*
        RealmResults<Profiles> results = null;

        results = getProfileInfo();

        for(final Profiles model : results){
            model.getItems().toString();
        }
        */
        for (int i = 0; i < addressNameArray.length; i++) {
            //TODO 行き先によって画像変更  int型の画像idでswitchしたほうがいいかも
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_house);
            //TODO ↓が1つのリストに入る情報なので、: 後を変数管理していく
            CustomHomeListItem item = new CustomHomeListItem(bmp, "宛先 : " + addressNameArray[i], "行き先 : " + destinationArray[i], "E-mail : " + addressMailArray[i]);
            listItems.add(item);
        }


        CustomHomeListAdapter adapter = new CustomHomeListAdapter(this, R.layout.main_list_item, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

    }

    @Override
    public void onStart(){
        super.onStart();
        getLastLocation();
    }

    //ListViewクリック処理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //TODO プログレスバーで待機しながら、現在地から目的地までの所要時間と予想到着時間を算出 -> メールとLINEの選択

        ListView listView = (ListView) adapterView;
        CustomHomeListItem item = (CustomHomeListItem)listView.getItemAtPosition(position);

        FragmentManager fragmentManager = getFragmentManager();

        ReturnTimeDialogFragment dialogFragment = new ReturnTimeDialogFragment(latitude,longitude);
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

    //Listに表示する情報の取得
    private RealmResults<Profiles> getProfileInfo(){

        RealmQuery<Profiles> query = realm.where(Profiles.class);

        return query.findAll();

    }

    //端末の位置情報取得
    @SuppressWarnings("MissingPermission")
    private void getLastLocation(){
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            location = task.getResult();

                            latitude = location.getLatitude();  //緯度
                            longitude = location.getLongitude();  //経度
                        }
                    }
                });
    }

}
