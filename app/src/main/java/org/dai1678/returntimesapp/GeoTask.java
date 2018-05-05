package org.dai1678.returntimesapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GeoTask extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Geo geo;

    GeoTask(Context mContext) {
        this.context = mContext;
        geo = (Geo) mContext;
    }

    //バックグラウンド処理開始前にUIスレッドで実行 ダイアログ生成処理
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //バックグラウンド処理終了後に実行
    @Override
    protected void onPostExecute(String aDouble) {
        super.onPostExecute(aDouble);

        if (aDouble != null) {
            geo.calculationTime(aDouble);
            progressDialog.dismiss();  //ダイアログ閉じる
        } else
            Toast.makeText(context, "API Error! 適切な値をいれてください", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
    }

    //バックグラウンド処理
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int statusCode = httpURLConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                String json = stringBuilder.toString();

                Log.d("JSON", json);
                JSONObject root = new JSONObject(json);
                JSONArray array_rows = root.getJSONArray("rows");
                Log.d("JSON", "array_rows:" + array_rows);
                JSONObject object_rows = array_rows.getJSONObject(0);
                Log.d("JSON", "object_rows:" + object_rows);
                JSONArray array_elements = object_rows.getJSONArray("elements");
                Log.d("JSON", "array_elements:" + array_elements);
                JSONObject object_elements = array_elements.getJSONObject(0);
                Log.d("JSON", "object_elements:" + object_elements);
                JSONObject object_duration = object_elements.getJSONObject("duration");
                JSONObject object_distance = object_elements.getJSONObject("distance");

                Log.d("JSON", "object_duration:" + object_duration);


                return object_duration.getString("value") + "," + object_distance.getString("value");
            }
        } catch (MalformedURLException e) {
            Log.d("error", "error1");
        } catch (IOException e) {
            Log.d("error", "error2");
        } catch (JSONException e) {
            Log.d("error", "error3");
        }

        return null;
    }

    interface Geo {
        void calculationTime(String min);
    }

}