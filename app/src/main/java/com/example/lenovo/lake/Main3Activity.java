package com.example.lenovo.lake;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main3Activity extends AppCompatActivity {
    OkHttpClient mClient;
    JSONArray jsonArray;
    ProgressBar bar;
    String uuu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        if(!isNetworkAvaible(this))
        {
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            finish();

        }
        else
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(Main3Activity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, 2500);
             }




    }
    public static boolean isNetworkAvaible(Context context)
    {
        ConnectivityManager conMan = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo()!=null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

}
