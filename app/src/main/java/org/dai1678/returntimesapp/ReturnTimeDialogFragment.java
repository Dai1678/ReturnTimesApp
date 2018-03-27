package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        //TODO ダイアログのレイアウト修正
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.return_time_dialog, null );

        builder.setView(view)
                .setTitle("連絡をします")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReturnTimeDialogFragment.this.getDialog().cancel();
                    }
                });

        TextView requiredTimeText = view.findViewById(R.id.requiredTime);
        requiredTimeText.setText("所要時間 : " + requiredTime);
        TextView arrivalTimeText = view.findViewById(R.id.arrivalTime);
        arrivalTimeText.setText("予想到着時刻 : " + arrivalTIme);

        ImageView mailButton = view.findViewById(R.id.mailButton);
        mailButton.setOnClickListener(this);
        ImageView lineButton = view.findViewById(R.id.lineButton);
        lineButton.setOnClickListener(this);

        return builder.create();
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

        //TODO 送信文章の編集をできるようにしたい
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
