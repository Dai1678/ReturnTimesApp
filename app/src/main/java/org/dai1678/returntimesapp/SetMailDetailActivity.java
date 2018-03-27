package org.dai1678.returntimesapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SetMailDetailActivity extends AppCompatActivity {

    TextInputEditText contactEdit;
    TextInputEditText addressEdit;
    //ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mail_detail);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("連絡先の設定");
        }

        contactEdit = findViewById(R.id.contactEdit);
        addressEdit = findViewById(R.id.addressEdit);

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
            String contact = contactEdit.getText().toString();
            String address = addressEdit.getText().toString();

            if(!contact.equals("") && !address.equals("")){
                Intent intent = new Intent();
                intent.putExtra("contact", contact);
                intent.putExtra("address", address);    //メールアドレスをSettingProfileActivityへ送る
                setResult(RESULT_OK, intent);

                finish();
            }else{
                if(contact.equals("")){
                    final int ALERT_CONTACT = 2;

                    FragmentManager fragmentManager = getFragmentManager();

                    AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_CONTACT);
                    alertDialogFragment.show(fragmentManager,"alertDialog");    //警告アラート表示
                }

                if(address.equals("")) {
                    int ALERT_ADDRESS = 3;
                    FragmentManager fragmentManager = getFragmentManager();

                    AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_ADDRESS);
                    alertDialogFragment.show(fragmentManager, "alertDialog");    //警告アラート表示

                }
            }

            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
