package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class AlertDialogFragment extends DialogFragment implements ImageButton.OnClickListener {

    protected Context context;
    HomeActivity homeActivity;

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
        switch (view.getId()){
            case R.id.mailButton:
                getDialog().dismiss();
                break;

            case R.id.lineButton:
                //getDialog().dismiss();

                if (isInstallLine()){
                    startActivity(getIntentSendLine());
                }else{
                    Toast.makeText(getActivity(), "Lineがインストールされていません", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
                break;
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

    public Intent getIntentSendLine(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //TODO 送り先のユーザーを事前に設定できるようにしたい
        //TODO 送るメッセージを取得
        intent.setData(Uri.parse("line://msg/text/" + "test"));
        return intent;
    }
}
