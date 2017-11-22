package org.t_robop.returntimesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFloat;

public class SetMailDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mail_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.setMailDetailToolbar);
        toolbar.setTitle("宛先とメール本文の設定");

        ButtonFloat buttonFloat = (ButtonFloat)findViewById(R.id.setMailDetailFloatingButton);
        if(buttonFloat != null){
            buttonFloat.setDrawableIcon(getResources().getDrawable(R.mipmap.ic_float_check));
        }
        buttonFloat.setOnClickListener(this);

        ListView listView = (ListView)findViewById(R.id.templateTextList);
        final String[] templateText = {"◯時◯分に帰宅します","◯時◯分に到着します","◯時◯分に家を出ます"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,templateText);

        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SetMailDetailActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
