package org.t_robop.returntimesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.gc.materialdesign.views.ButtonFloat;

/***************************************************************************************************
 SetDestinationActivity     第一設定画面
 機能：    連絡相手のメールアドレス入力・データベース保存
          行き先の選択(家、レストラン、病院、銀行、郵便局、駅)　場所名と画像idをデータベースに保存
***************************************************************************************************/

public class SetDestinationActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        Toolbar toolbar = (Toolbar)findViewById(R.id.setDestinationToolbar);
        toolbar.setTitle("連絡先名を入力してください");  //TODO 意味考えて、適切な文章に変更

        //TODO EditTextの「Destination Address」はわかりにくい

        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new GridItemAdapter(this));

        ButtonFloat buttonFloat = (ButtonFloat)findViewById(R.id.setDestinationFloatingButton);
        if(buttonFloat != null){
            buttonFloat.setDrawableIcon(getResources().getDrawable(R.mipmap.ic_float_right));
        }
        assert buttonFloat != null;
        buttonFloat.setOnClickListener(this);
    }

    //TODO GridView上の各itemのクリック処理


    //TODO フローティングアクションボタンの位置がおかしい
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(SetDestinationActivity.this, SetMapAddressActivity.class);
        startActivity(intent);
    }
}
