package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

@SuppressLint("ValidFragment")
public class DeleteDialogFragment extends DialogFragment {

    ArrayList<CustomHomeListItem> item;
    CustomHomeListAdapter adapter;
    private int deletePosition;

    @SuppressLint("ValidFragment")
    public DeleteDialogFragment(ArrayList<CustomHomeListItem> item, CustomHomeListAdapter adapter, int deletePosition){
        this.item = item;
        this.adapter = adapter;
        this.deletePosition = deletePosition;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("削除確認")
                .setMessage("削除しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProfileData();
                        deleteListItem();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //データベースの削除
    public void deleteProfileData(){
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<ProfileItems> results = realm.where(ProfileItems.class).findAll();
        ProfileItems item = results.get(deletePosition);
        assert item != null;
        item.deleteFromRealm();
        realm.commitTransaction();
    }

    //リストの削除
    public void deleteListItem(){
        item.remove(deletePosition);
        adapter.notifyDataSetChanged();
    }
}
