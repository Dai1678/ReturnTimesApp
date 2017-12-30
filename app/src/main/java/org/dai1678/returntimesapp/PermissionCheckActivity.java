package org.dai1678.returntimesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PermissionCheckActivity extends AppCompatActivity  {

    private final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //TODO No Dataが映ってしまっているので、空のXMLかロゴタイトル画面を表示させる (初回起動時のみ発生)

        if(Build.VERSION.SDK_INT >= 23){
            checkPermission();
        }else{
            StartApplication();
        }

    }

    public void checkPermission() {
        //すでに許可している場合
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            StartApplication();
        }else{  //拒否していた場合
            requestLocationPermission();
        }
    }

    public void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION);
        }else{
            //Toast.makeText(this, "アプリを実行できません", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_PERMISSION){
            //使用が許可された
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                StartApplication();
            }else{
                Toast.makeText(this, "アプリを実行できません", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartApplication() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

}