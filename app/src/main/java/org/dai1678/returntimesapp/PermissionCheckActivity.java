package org.dai1678.returntimesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PermissionCheckActivity extends AppCompatActivity  {

    private final int REQUEST_MULTI_PERMISSIONS = 101;

    public boolean isAllowedLocation = false;
    public boolean isAllowedReadContact = false;
    public boolean isAllowedReadStorage = false;
    public boolean isAllowedCallPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //TODO No Dataが映ってしまっているので、空のXMLかロゴタイトル画面を表示させる (初回起動時のみ発生)

        if(Build.VERSION.SDK_INT >= 23){
            checkMultiPermissions();
        }else{
            StartApplication();
        }

    }

    private void checkMultiPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionReadContact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCallPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        List<String> requestPermissions = new ArrayList<>();

        //Permissionの許可確認
        if (permissionLocation == PackageManager.PERMISSION_GRANTED){
            isAllowedLocation = true;
        }else{
            requestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionReadContact == PackageManager.PERMISSION_GRANTED){
            isAllowedReadContact = true;
        }else{
            requestPermissions.add(Manifest.permission.READ_CONTACTS);
        }

        if (permissionReadStorage == PackageManager.PERMISSION_GRANTED){
            isAllowedReadStorage = true;
        }else{
            requestPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionCallPhone == PackageManager.PERMISSION_GRANTED){
            isAllowedCallPhone = true;
        }else{
            requestPermissions.add(Manifest.permission.CALL_PHONE);
        }

        if (!requestPermissions.isEmpty()){
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), REQUEST_MULTI_PERMISSIONS);
        }else{
            StartApplication();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int allowedNum = 0;

        //使用が許可されたかチェック
        if (requestCode == REQUEST_MULTI_PERMISSIONS){
            if (grantResults.length > 0){
                for (int i=0; i<permissions.length; i++){
                    switch (permissions[i]) {
                        case Manifest.permission.ACCESS_FINE_LOCATION:
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                isAllowedLocation = true;
                                allowedNum += 1;
                            } else {
                                Toast.makeText(this, "位置情報への許可が無いため、起動できません", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case Manifest.permission.READ_CONTACTS:
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                isAllowedReadContact = true;
                                allowedNum += 1;
                            } else {
                                Toast.makeText(this, "連絡先への許可がないため、起動できません", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                isAllowedReadStorage = true;
                                allowedNum += 1;
                            } else {
                                Toast.makeText(this, "ストレージへの許可がないため、起動できません", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case Manifest.permission.CALL_PHONE:
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                isAllowedCallPhone = true;
                                allowedNum += 1;
                            } else {
                                Toast.makeText(this, "電話への許可がないため、起動できません", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }

                if (allowedNum == 4){
                    StartApplication();
                }

            }
        }
    }

    private void StartApplication() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);

        finish();
    }

}