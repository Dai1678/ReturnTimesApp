package org.t_robop.returntimesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        //Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolbar);
        toolbar.setTitle("profile設定");   //TODO 意味考えて、適切な言葉に変更

        //ListView
        ListView listView = (ListView)findViewById(R.id.profileList);

        ArrayList<CustomProfileListItem> listItems = new ArrayList<>();

        //0番目 : 行き先  1番目 : Map 2番目 : メアド入力
        for(int i = 0; i<3; i++){
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            String[] profileHint = {"Title","Map","Mail"};
            CustomProfileListItem item = new CustomProfileListItem(bmp,profileHint[i]);
            listItems.add(item);
        }

        CustomProfileListAdapter adapter = new CustomProfileListAdapter(this,R.layout.profile_item,listItems);
        listView.setAdapter(adapter);

    }
}
