package org.t_robop.returntimesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetActivity extends AppCompatActivity {

    String text;  //自宅情報(今は緯度経度)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        EditText editText = (EditText)findViewById(R.id.editHome);

        text = editText.getText().toString();
    }

    //自宅情報送信
    public void sendClick(View view){
        Intent intent = new Intent(SetActivity.this,MainActivity.class);
        intent.putExtra("data",text);
        startActivity(intent);
    }
}
