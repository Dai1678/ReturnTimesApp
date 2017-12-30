package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class AlertDialogFragment extends DialogFragment {

    private int strType;

    @SuppressLint("ValidFragment")
    public AlertDialogFragment(int num){
        this.strType = num;
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog dialog;
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        switch (strType){
            case 1:
                String alertDestination = "行き先名が設定されていません！\nアイコンを設定していない場合は「家」が自動設定されます。";
                alert.setMessage(alertDestination);
                break;

            case 2:
                String alertContact = "宛先名が設定されていません！";
                alert.setMessage(alertContact);
                break;

            case 3:
                String alertMailAddress = "メールアドレスが設定されていません！";
                alert.setMessage(alertMailAddress);
                break;

            case 4:
                String alertSavedProfiles = "未設定項目があります！";
                alert.setMessage(alertSavedProfiles);
                break;
        }

        alert.setPositiveButton("OK",null);

        dialog = alert.create();
        dialog.show();

        return dialog;
    }

}
