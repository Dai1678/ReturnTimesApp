package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AlertDialogFragment extends DialogFragment implements ImageButton.OnClickListener {

    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitude;
    private double speed;
    private double bearing;


    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog dialog;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        alert.setTitle("連絡をします");

        HomeActivity homeActivity = new HomeActivity();

        View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_layout,null);

        //TODO 現在位置の取得 (別クラスで処理したほうがいいかも)
        //TODO 所要時間、予想到着時間の表示

        //homeActivity.stopLocationUpdates();

        //homeActivity.fuseData = new double[6];

        this.latitude = homeActivity.getFuseData(0);
        this.longitude = homeActivity.getFuseData(1);
        this.accuracy = homeActivity.getFuseData(2);
        this.altitude = homeActivity.getFuseData(3);
        this.speed = homeActivity.getFuseData(4);
        this.bearing = homeActivity.getFuseData(5);

        //

        TextView latitudeText = alertView.findViewById(R.id.latitude);
        latitudeText.setText("緯度 : " + this.latitude);
        TextView longitudeText = alertView.findViewById(R.id.longitude);
        longitudeText.setText("経度 : " + this.longitude);
        TextView accuracyText = alertView.findViewById(R.id.accuracy);
        accuracyText.setText("正確性 : " + this.accuracy);
        TextView altitude = alertView.findViewById(R.id.altitude);
        altitude.setText("高度 : " + this.altitude);
        TextView speedText = alertView.findViewById(R.id.speed);
        speedText.setText("速度 : " + this.speed);
        TextView bearingText = alertView.findViewById(R.id.bearing);
        bearingText.setText("ペアリング : " + this.bearing);

        ImageView mailButton = alertView.findViewById(R.id.mailButton);
        mailButton.setOnClickListener(this);
        ImageView lineButton = alertView.findViewById(R.id.lineButton);
        lineButton.setOnClickListener(this);

        alert.setView(alertView);

        dialog = alert.create();
        dialog.show();

        return dialog;
    }

    @Override
    public void onClick(View view) {
        if (isInstallLine()){
            startActivity(getIntentSend(view));
        }else{
            Toast.makeText(getActivity(), "Lineがインストールされていません", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        }
    }

    public boolean isInstallLine(){
        PackageManager packageManager = getActivity().getPackageManager();
        boolean appInstalled;

        try{
            packageManager.getPackageInfo("jp.naver.line.android",PackageManager.GET_ACTIVITIES);
            appInstalled = true;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appInstalled = false;
        }

        return appInstalled;
    }

    public Intent getIntentSend(View view){

        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.mailButton:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:example@gmail.com"));  //TODO 送り先のメールアドレスを取得して設定
                intent.putExtra(Intent.EXTRA_SUBJECT,"帰宅連絡");
                intent.putExtra(Intent.EXTRA_TEXT,"帰宅します");     //TODO 送る本文を設定
                break;

            case R.id.lineButton:
                intent.setAction(Intent.ACTION_VIEW);
                //TODO 送り先のユーザーを事前に設定できるようにしたい
                //TODO 送るメッセージを取得
                intent.setData(Uri.parse("line://msg/text/" + "test"));
                break;
        }

        return intent;
    }
}
