package org.dai1678.returntimesapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
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

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

/***************************************************************************************************
 SetDestinationActivity     第一設定画面
 機能：    連絡相手のメールアドレス入力
          行き先の選択(家、レストラン、病院、銀行、郵便局、駅)　
          場所名と画像positionを取得
***************************************************************************************************/

public class SetDestinationActivity extends AppCompatActivity implements GridView.OnItemClickListener {

    GridView gridView;

    private MaterialEditText destinationEditText = null;
    private DestinationItem destinationItem;
    private Integer imageMipmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);   //自動でキーボードが起動するのを防ぐ

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("行き先名の設定");
        }

        destinationEditText = findViewById(R.id.destinationEdit);

        ArrayList<GridItem> gridItems = new ArrayList<>();

        destinationItem = new DestinationItem();

        for(int i=0; i<destinationItem.getItemNameList().size(); i++){
            GridItem item = new GridItem(destinationItem.getItemNameList().get(i), destinationItem.getItemImageList().get(i));
            gridItems.add(item);
        }

        gridView = findViewById(R.id.gridView);
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        GridItemAdapter adapter = new GridItemAdapter(this,R.layout.grid_item,gridItems);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(this);

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
            String destinationName = destinationEditText.getText().toString();

            if(destinationName.equals("")){
                final int ALERT_DESTINATION = 1;
                FragmentManager fragmentManager = getFragmentManager();

                AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_DESTINATION);
                alertDialogFragment.show(fragmentManager,"alertDialog");    //警告アラート表示

            }else{
                Intent intent = new Intent();
                intent.putExtra("destinationName", destinationName);    //行き先名をSettingProfileActivityへ送る
                intent.putExtra("imageMipmap", imageMipmap);    //アイコンのmipmapコードをSettingProfileActivityへ送る
                setResult(RESULT_OK, intent);

                finish();
            }

            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    //GridViewのクリック処理
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i("position",String.valueOf(position));
        this.imageMipmap = destinationItem.getItemImageList().get(position);

    }

}
