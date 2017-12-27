package org.dai1678.returntimesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;

public class SettingProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        //Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolbar);
        toolbar.setTitle("profile設定");
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //ListView
        ListView listView = (ListView)findViewById(R.id.profileList);

        ArrayList<CustomProfileListItem> listItems = new ArrayList<>();
        String[] profileHint = {"Title","Map","Mail"};
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);     //TODO それぞれ適切な画像をセット

        //0番目 : 行き先  1番目 : Map 2番目 : メアド入力
        for (String aProfileHint : profileHint) {
            CustomProfileListItem item = new CustomProfileListItem(bmp, aProfileHint);
            listItems.add(item);
        }

        CustomProfileListAdapter adapter = new CustomProfileListAdapter(this,R.layout.profile_item,listItems);
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
                startActivity(intent);
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
                startActivity(intent);
                break;
        }
    }

    //プレイスオートコンプリートの結果取得
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this,data);
                //TODO 必要な情報を取得して、帰宅時間算出に使う
                Log.i("Place",place.getName().toString());
                Log.i("Place",place.getAddress().toString());
                Log.i("Place",place.getLatLng().toString());
            }else if(resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this,data);
                Log.i("Place",status.getStatusMessage());
            }else if(resultCode == RESULT_CANCELED){
                Log.i("Place","failed");
            }
        }
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
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}