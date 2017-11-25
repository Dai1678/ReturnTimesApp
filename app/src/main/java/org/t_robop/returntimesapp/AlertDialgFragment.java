package org.t_robop.returntimesapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog dialog;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
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
