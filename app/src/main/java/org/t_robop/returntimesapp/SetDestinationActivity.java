package org.t_robop.returntimesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;

/***************************************************************************************************
 SetDestinationActivity     第一設定画面
 機能：    連絡相手のメールアドレス入力・データベース保存
          行き先の選択(家、レストラン、病院、銀行、郵便局、駅)　場所名と画像idをデータベースに保存
***************************************************************************************************/

public class SetDestinationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        Toolbar toolbar = (Toolbar)findViewById(R.id.setDestinationToolbar);
        toolbar.setTitle("連絡先名を入力してください");  //TODO 意味考えて、適切な文章に変更
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new GridItemAdapter(this));

    }

    //TODO GridView上の各itemのクリック処理

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
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
