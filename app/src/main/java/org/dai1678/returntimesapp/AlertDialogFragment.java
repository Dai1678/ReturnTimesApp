package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by daipc on 17/12/28.
 */

public class AlertDialogFragment extends DialogFragment {

    private String alertDestination = "行き先名が設定されていません！\nアイコンを設定していない場合は「家」が自動設定されます。";

    private String alertMailAddress = "メールアドレスが設定されていません！";

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
                alert.setMessage(alertDestination);
                break;

            case 2:
                alert.setMessage(alertMailAddress);
        }

        alert.setPositiveButton("OK",null);

        dialog = alert.create();
        dialog.show();

        return dialog;
    }

}
