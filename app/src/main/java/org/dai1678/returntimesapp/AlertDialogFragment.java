package org.dai1678.returntimesapp;

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
import android.widget.Toast;

public class AlertDialogFragment extends DialogFragment implements ImageButton.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog dialog;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        alert.setTitle("連絡をします");

        View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_layout,null);

        //TODO 現在位置の取得 (別クラスで処理したほうがいいかも)
        //TODO 所要時間、予想到着時間の表示

        ImageView mailButton = (ImageView)alertView.findViewById(R.id.mailButton);
        mailButton.setOnClickListener(this);
        ImageView lineButton = (ImageView)alertView.findViewById(R.id.lineButton);
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
