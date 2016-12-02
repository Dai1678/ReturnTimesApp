package org.t_robop.returntimesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SetActivity extends AppCompatActivity {

    String text;  //自宅情報(今は緯度経度)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        editText = (EditText)findViewById(R.id.editHome);

        text = editText.getText().toString();
    }

    //自宅情報送信
    public void sendClick(View view){
        Intent intent = new Intent(SetActivity.this,MainActivity.class);
        intent.putExtra("data",text);

        if(editText.length() != 0){  //文字数判定
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"適切な入力をしてください",Toast.LENGTH_SHORT).show();
        }
    }

    //キャンセルボタン
    public void cancelClick(View view){
        Intent intent = new Intent(SetActivity.this,MainActivity.class);
        startActivity(intent);
    }


}
