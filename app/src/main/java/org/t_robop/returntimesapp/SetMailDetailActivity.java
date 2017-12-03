package org.t_robop.returntimesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;

public class SetMailDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mail_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.setMailDetailToolbar);
        toolbar.setTitle("宛先とメール本文の設定");
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ListView listView = (ListView)findViewById(R.id.templateTextList);
        final String[] templateText = {"◯時◯分に帰宅します","◯時◯分に到着します","◯時◯分に家を出ます"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templateText);

        listView.setAdapter(arrayAdapter);
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
            Toast.makeText(SetMailDetailActivity.this,"SAVED!",Toast.LENGTH_SHORT).show();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
