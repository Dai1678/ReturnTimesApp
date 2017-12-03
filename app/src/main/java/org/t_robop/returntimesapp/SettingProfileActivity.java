package org.t_robop.returntimesapp;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

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

        Intent intent = null;

        switch (position){
            case 0:
                intent = new Intent(this.getApplicationContext(),SetDestinationActivity.class);
                break;

            case 1:
                intent = new Intent(this.getApplicationContext(),SetMailDetailActivity.class);
                break;

            case 2:
                intent = new Intent(this.getApplicationContext(),SetMapAddressActivity.class);
                break;
        }
        startActivity(intent);
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
