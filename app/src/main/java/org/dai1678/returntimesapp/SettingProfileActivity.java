package org.dai1678.returntimesapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;

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
    private int imageMipmap;
    private String placeName;
    private double latitude;
    private double longitude;
    private String contact;
    private String address;

    int savedCheckPoint = 0;
    Boolean savedCheckFlag = false;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("設定項目一覧");
        }

        //ListView
        ListView listView = findViewById(R.id.profileList);

        profileResult = new ArrayList<>();
        profileResult.add("行き先名の設定");
        profileResult.add("住所の設定");
        profileResult.add("連絡先の設定");
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
                Log.i("Destination", String.valueOf(data.getIntExtra("imageMipmap",0)));

                this.destinationName = data.getStringExtra("destinationName");
                this.imageMipmap = data.getIntExtra("imageMipmap",0);

                profileResult.set(0, destinationName);
                savedCheckPoint += 1;
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
                Log.i("MAIL", data.getStringExtra("contact"));
                Log.i("MAIL", data.getStringExtra("address"));

                this.contact = data.getStringExtra("contact");
                this.address = data.getStringExtra("address");

                profileResult.set(2, address);
                savedCheckPoint += 1;
            }
        }

        listItems.clear();

        //Listの更新
        for (String result : profileResult) {
            CustomProfileListItem item = new CustomProfileListItem(bmp, result);
            listItems.add(item);
        }

        savedCheckFlag = savedCheckProfiles();

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

            if(savedCheckFlag){
                Toast.makeText(SettingProfileActivity.this,"SAVED!",Toast.LENGTH_SHORT).show();
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

    //保存データのnullチェック
    private Boolean savedCheckProfiles(){
        return savedCheckPoint >= 3;
    }

    //データベースへ保存
    private void savedProfiles(){
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        ProfileItems items = realm.createObject(ProfileItems.class);

        items.setProfileId(getNextProfileItemsId());
        items.setDestinationName(destinationName);
        items.setImageMipmap(imageMipmap);
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
