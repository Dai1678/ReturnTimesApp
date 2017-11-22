package org.t_robop.returntimesapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;

import java.util.ArrayList;

/***************************************************************************************************
HomeActivity    初期表示画面
 機能：    連絡先の選択
          現在地から帰宅先までの所要時間・予想到着時間の表示
          メールorLINEへの移動
***************************************************************************************************/

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar)findViewById(R.id.homeToolbar);
        toolbar.setTitle("連絡先を選択してください");   //TODO 意味考えて、適切な言葉に変更

        ButtonFloat buttonFloat = (ButtonFloat)findViewById(R.id.homeFloatingButton);
        if(buttonFloat != null){
            buttonFloat.setDrawableIcon(getResources().getDrawable(R.drawable.ic_float_button));
        }
        assert buttonFloat != null;
        buttonFloat.setOnClickListener(this);

        ListView listView = (ListView)findViewById(R.id.list);
        TextView emptyView = (TextView)findViewById(R.id.emptyTextView);
        listView.setEmptyView(emptyView);

        ArrayList<CustomHomeListItem> listItems = new ArrayList<>();
        //ToDO RealmからListItem情報を取得して表示
        //ListItem情報： 行き先、宛先(名前orメールアドレス)、場所画像id

        for(int i = 0; i<1; i++){
            //TODO 行き先によって画像変更  int型の画像idでswitchしたほうがいいかも
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_house);
            CustomHomeListItem item = new CustomHomeListItem(bmp,"行き先 : 自宅", "宛先 : example@gmail.com");
            listItems.add(item);
        }


        CustomHomeListAdapter adapter = new CustomHomeListAdapter(this,R.layout.custom_list_item,listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ListView listView = (ListView) adapterView;
        CustomHomeListItem item = (CustomHomeListItem)listView.getItemAtPosition(position);

        FragmentManager fragmentManager = getFragmentManager();

        DialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(fragmentManager,"alert dialog");
    }

    //TODO onItemLongClickで削除処理の実装


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,SetDestinationActivity.class);
        startActivity(intent);
    }

    public static class AlertDialogFragment extends DialogFragment{
        private AlertDialog dialog;
        private AlertDialog.Builder alert;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            alert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
            alert.setTitle("連絡をします");

            View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_layout,null);

            //TODO 現在位置の取得 (別クラスで処理したほうがいいかも)
            //TODO 所要時間、予想到着時間の表示

            ImageView mailButton = (ImageView)alertView.findViewById(R.id.mailButton);
            mailButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Gmailへ
                    getDialog().dismiss();
                }
            });

            ImageView lineButton = (ImageView)alertView.findViewById(R.id.lineButton);
            lineButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Lineへ
                    getDialog().dismiss();
                }
            });

            alert.setView(alertView);

            dialog = alert.create();
            dialog.show();

            return dialog;
        }
    }
}
