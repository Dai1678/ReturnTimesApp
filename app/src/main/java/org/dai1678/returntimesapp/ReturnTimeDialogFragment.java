package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import java.util.concurrent.Executor;

@SuppressLint("ValidFragment")
public class ReturnTimeDialogFragment extends DialogFragment implements ImageButton.OnClickListener {

    private String requiredTime;
    private String arrivalTIme;
    private String mailAddress;

    public ReturnTimeDialogFragment(String requiredTime, String arrivalTime, String mailAddress){
        this.requiredTime = requiredTime;
        this.arrivalTIme = arrivalTime;
        this.mailAddress = mailAddress;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog dialog;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
        alert.setTitle("連絡をします");

        View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_layout,null);

        TextView requiredTimeText = alertView.findViewById(R.id.requiredTime);
        requiredTimeText.setText("所要時間 : " + requiredTime);
        TextView arrivalTimeText = alertView.findViewById(R.id.arrivalTime);
        arrivalTimeText.setText("予想到着時刻 : " + arrivalTIme);

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
            Toast.makeText(getActivity(), "LineアプリをインストールするとLineを使って連絡ができます！", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        }
    }

    //Lineアプリのインストール確認
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
                intent.setData(Uri.parse("mailto:" + mailAddress));
                intent.putExtra(Intent.EXTRA_SUBJECT,"帰宅連絡");
                intent.putExtra(Intent.EXTRA_TEXT,arrivalTIme + "に帰宅します");
                break;

            case R.id.lineButton:
                intent.setAction(Intent.ACTION_VIEW);
                //TODO 送り先のユーザーを事前に設定できるようにしたい

                intent.setData(Uri.parse("line://msg/text/" + arrivalTIme + "に帰宅します"));
                break;
        }

        return intent;
    }
}
