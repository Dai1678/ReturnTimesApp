package org.dai1678.returntimesapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class SetMailDetailActivity extends AppCompatActivity {

    MaterialEditText editText;
    //ListView listView;

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

        editText = findViewById(R.id.contactEdit);

        //TODO メアド入力フォームの下に送信先LINEユーザー設定フォームをいれたい

        /*
        listView = (ListView)findViewById(R.id.templateTextList);
        final String[] templateText = {"◯時◯分に帰宅します","◯時◯分に到着します","◯時◯分に家を出ます"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,templateText);

        listView.setAdapter(arrayAdapter);
        */
    }

    //ListViewのチェック処理

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
            String address = editText.getText().toString();

            if(address.equals("")){
                int ALERT_ADDRESS = 2;
                FragmentManager fragmentManager = getFragmentManager();

                AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_ADDRESS);
                alertDialogFragment.show(fragmentManager,"alertDialog");    //警告アラート表示

            }else{
                Intent intent = new Intent();
                intent.putExtra("address", address);    //メールアドレスをSettingProfileActivityへ送る
                setResult(RESULT_OK, intent);

                finish();
            }
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
