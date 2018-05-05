package org.dai1678.returntimesapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<SettingListModel> listItems = new ArrayList<>();
    SettingListAdapter adapter;

    private final int SET_DESTINATION_REQUEST_CODE = 1;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private final int SET_MAIL_DETAIL_REQUEST_CODE = 3;

    private String destinationName;
    private int imageDrawableId;
    private String placeName;
    private double latitude;
    private double longitude;
    private String contact;
    private String address;

    int savedCheckPoint = 0;    //各SettingActivityで入力されると値が増える
    Boolean savedCheckFlag = false; //savedCheckPointが3になるとtrue

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("設定項目一覧");
        }

        //ListView
        ListView listView = findViewById(R.id.profileList);

        SettingListModel listModel = null;
        for (int i=0; i<3; i++){
            if (i==0) listModel = new SettingListModel((BitmapFactory.decodeResource(getResources(), R.drawable.ic_walk)), "行き先名の設定");
            if (i==1) listModel = new SettingListModel((BitmapFactory.decodeResource(getResources(), R.drawable.ic_location)), "住所の設定");
            if (i==2) listModel = new SettingListModel((BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_mail)), "連絡先の設定");
            listItems.add(listModel);
        }

        adapter = new SettingListAdapter(this, R.layout.profile_item, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent intent;

        switch (position){
            case 0:
                intent = new Intent(this.getApplicationContext(),SettingDestinationActivity.class);
                startActivityForResult(intent, SET_DESTINATION_REQUEST_CODE);
                break;

            case 1:
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);    //プレイスオートコンプリートへ
                    startActivityForResult(intent,PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                intent = new Intent(this.getApplicationContext(),SettingMailAddressActivity.class);
                startActivityForResult(intent, SET_MAIL_DETAIL_REQUEST_CODE);
                break;
        }
    }

    //各設定Activityからの結果取得
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == SET_DESTINATION_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                this.destinationName = data.getStringExtra("destinationName");
                this.imageDrawableId = data.getIntExtra("imageDrawableId",0);

                SettingListModel destinationList = new SettingListModel((BitmapFactory.decodeResource(getResources(), imageDrawableId)), destinationName);
                listItems.set(0, destinationList);

                savedCheckPoint += 1;
            }
        }

        else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this,data);

                this.placeName = place.getName().toString();
                this.latitude = place.getLatLng().latitude;
                this.longitude = place.getLatLng().longitude;

                SettingListModel placeList = new SettingListModel((BitmapFactory.decodeResource(getResources(), R.drawable.ic_location)), placeName);
                listItems.set(1, placeList);

                savedCheckPoint += 1;

            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
                Log.i("Place",status.getStatusMessage());
            }else if(resultCode == RESULT_CANCELED){    //何も入力せずに戻ってきた時
                Log.i("Place","failed");
            }
        }

        else if (requestCode == SET_MAIL_DETAIL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                this.contact = data.getStringExtra("contact");
                this.address = data.getStringExtra("address");

                SettingListModel addressList = new SettingListModel((BitmapFactory.decodeResource(getResources(), R.drawable.ic_contact_mail)), address);
                listItems.set(2, addressList);

                savedCheckPoint += 1;
            }
        }

        adapter.notifyDataSetChanged();

        savedCheckFlag = savedCheckProfiles();
    }

    //保存データのnullチェック
    private Boolean savedCheckProfiles(){
        return savedCheckPoint >= 3;
    }

    //データベースへ保存
    private void savedProfiles(){

        realm.beginTransaction();
        ProfileRealmModel items = realm.createObject(ProfileRealmModel.class);

        items.setProfileId(getNextProfileItemsId());
        items.setDestinationName(destinationName);
        items.setImageDrawable(imageDrawableId);
        items.setPlaceName(placeName);
        items.setLatitude(latitude);
        items.setLongitude(longitude);
        items.setContact(contact);
        items.setMail(address);

        realm.commitTransaction();
    }

    //id設定
    public Integer getNextProfileItemsId(){
        Integer nextItemsId = 1;

        //idの最大値を取得
        Number maxItemsId = realm.where(ProfileRealmModel.class).max("profileId");

        //NULLチェック
        if(maxItemsId != null){
            nextItemsId = maxItemsId.intValue() + 1;
        }

        return nextItemsId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if(item.getItemId() == R.id.save_profile){

            if(savedCheckFlag){
                Toast.makeText(SettingActivity.this,"SAVED!",Toast.LENGTH_SHORT).show();
                savedProfiles();
                finish();
                return  true;
            }else{
                final int ALERT_SAVED = 4;
                FragmentManager fragmentManager = getFragmentManager();

                AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_SAVED);
                alertDialogFragment.show(fragmentManager, "alertDialog");
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
