package org.t_robop.returntimesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SettingProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        //Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolbar);
        toolbar.setTitle("profile設定");   //TODO 意味考えて、適切な言葉に変更
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //ListView
        ListView listView = (ListView)findViewById(R.id.profileList);

        ArrayList<CustomProfileListItem> listItems = new ArrayList<>();

        //0番目 : 行き先  1番目 : Map 2番目 : メアド入力
        for(int i = 0; i<3; i++){
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            String[] profileHint = {"Title","Map","Mail"};
            CustomProfileListItem item = new CustomProfileListItem(bmp,profileHint[i]);
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
                //intent = new Intent(this.getApplicationContext(),SetMapAddressActivity.class);
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS).build();
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this,data);
                Log.i("Place",place.getName().toString());
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
            //TODO すべての設定項目が入力されていないと押せないようにしたい
            Toast.makeText(SettingProfileActivity.this,"SAVED!",Toast.LENGTH_SHORT).show();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
