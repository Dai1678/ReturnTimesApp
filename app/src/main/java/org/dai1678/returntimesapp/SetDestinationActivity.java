package org.dai1678.returntimesapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

/***************************************************************************************************
 SetDestinationActivity     第一設定画面
 機能：    連絡相手のメールアドレス入力・データベース保存
          行き先の選択(家、レストラン、病院、銀行、郵便局、駅)　場所名と画像idをデータベースに保存
***************************************************************************************************/

public class SetDestinationActivity extends AppCompatActivity {

    MaterialEditText destinationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);   //自動でキーボードが起動するのを防ぐ

        Toolbar toolbar = (Toolbar)findViewById(R.id.setDestinationToolbar);
        toolbar.setTitle("行き先名の設定");
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        destinationName = (MaterialEditText)findViewById(R.id.destinationEdit);

        ArrayList<GridItem> gridItems = new ArrayList<>();

        //TODO データベースなどから取得する
        String[] itemNameArray = {"家","レストラン","病院","銀行","郵便局","駅"};
        Integer[] itemImageArray = {R.mipmap.ic_house,R.mipmap.ic_restaurant,R.mipmap.ic_hospital,R.mipmap.ic_bank,R.mipmap.ic_postoffice,R.mipmap.ic_station};

        for(int i=0; i<itemNameArray.length; i++){
            GridItem item = new GridItem(itemNameArray[i],itemImageArray[i]);
            gridItems.add(item);
        }

        GridView gridView = (GridView)findViewById(R.id.gridView);
        GridItemAdapter adapter = new GridItemAdapter(this,R.layout.grid_item,gridItems);
        gridView.setAdapter(adapter);

    }

    //GridView上の各itemのクリック処理はGridItemAdapterに書いてある

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
            Toast.makeText(SetDestinationActivity.this,"SAVED!",Toast.LENGTH_SHORT).show();
            //TODO 行き先名と選択したGridItemをデータベースorSharedPreferenceに保存
            Log.i("Destination",destinationName.getText().toString());
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
