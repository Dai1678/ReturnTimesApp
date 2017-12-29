package org.dai1678.returntimesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

public class SettingProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<String> profileResult;
    Bitmap bmp;

    ArrayList<CustomProfileListItem> listItems;
    CustomProfileListAdapter adapter;

    int SET_DESTINATION_REQUEST_CODE = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    int SET_MAIL_DETAIL_REQUEST_CODE = 3;

    private String destinationName;
    private int imagePosition;
    private String placeName;
    private double latitude;
    private double longitude;
    private String contact;
    private String address;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        toolbar.setTitle("profile設定");
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //ListView
        ListView listView = findViewById(R.id.profileList);

        profileResult = new ArrayList<>();
        profileResult.add("Title");
        profileResult.add("Map");
        profileResult.add("Mail");
        bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);     //TODO それぞれ適切な画像をセット

        listItems = new ArrayList<>();
        //0番目 : 行き先  1番目 : Map 2番目 : メアド入力
        for (String profileResult : profileResult) {
            CustomProfileListItem item = new CustomProfileListItem(bmp, profileResult);
            listItems.add(item);
        }

        adapter = new CustomProfileListAdapter(this,R.layout.profile_item,listItems);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(this);
    }

    //ListViewクリック処理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent intent;

        switch (position){
            case 0:
                intent = new Intent(this.getApplicationContext(),SetDestinationActivity.class);
                startActivityForResult(intent, SET_DESTINATION_REQUEST_CODE);
                break;

            case 1:
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);    //プレイスオートコンプリートへ
                    startActivityForResult(intent,PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.i("Place:",e.toString());e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.i("Place:",e.toString());
                }
                break;

            case 2:
                intent = new Intent(this.getApplicationContext(),SetMailDetailActivity.class);
                startActivityForResult(intent, SET_MAIL_DETAIL_REQUEST_CODE);
                break;
        }
    }

    //各設定Activityからの結果取得
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == SET_DESTINATION_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Log.i("Destination", data.getStringExtra("destinationName"));
                Log.i("Destination", String.valueOf(data.getIntExtra("imagePosition",0)));

                this.destinationName = data.getStringExtra("destinationName");
                this.imagePosition = data.getIntExtra("imagePosition",0);

                profileResult.set(0, destinationName);
            }
        }

        else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this,data);
                Log.i("Place",place.getName().toString());
                Log.i("Place",place.getAddress().toString());
                Log.i("Place", String.valueOf(place.getLatLng().latitude));
                Log.i("Place", String.valueOf(place.getLatLng().longitude));

                this.placeName = place.getName().toString();
                this.latitude = place.getLatLng().latitude;
                this.longitude = place.getLatLng().longitude;

                profileResult.set(1, placeName);

            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
                Log.i("Place",status.getStatusMessage());
            }else if(resultCode == RESULT_CANCELED){    //何も入力せずに戻ってきた時
                Log.i("Place","failed");
            }
        }

        else if (requestCode == SET_MAIL_DETAIL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Log.i("MAIL", data.getStringExtra("contact"));
                Log.i("MAIL", data.getStringExtra("address"));

                this.contact = data.getStringExtra("contact");
                this.address = data.getStringExtra("address");

                profileResult.set(2, address);
            }
        }

        listItems.clear();

        //Listの更新
        for (String result : profileResult) {
            CustomProfileListItem item = new CustomProfileListItem(bmp, result);
            listItems.add(item);
        }

        adapter.notifyDataSetChanged();
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
            //TODO すべての設定項目が入力されていないと押せないようにしたい or 設定されていない項目はHomeActivityで未設定と表示する
            Toast.makeText(SettingProfileActivity.this,"SAVED!",Toast.LENGTH_SHORT).show();
            savedProfiles();
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    //データベースへ保存
    private void savedProfiles(){
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        ProfileItems items = realm.createObject(ProfileItems.class);

        items.setProfileId(getNextProfileItemsId());
        items.setDestinationName(destinationName);
        items.setImagePosition(imagePosition);
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
        Number maxItemsId = realm.where(ProfileItems.class).max("profileId");

        //NULLチェック
        if(maxItemsId != null){
            nextItemsId = maxItemsId.intValue() + 1;
        }

        return nextItemsId;
    }

}
