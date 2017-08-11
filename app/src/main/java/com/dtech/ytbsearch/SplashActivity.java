package com.dtech.ytbsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.preference.PrefManager;
import com.race604.drawable.wave.WaveDrawable;

import java.io.FileOutputStream;

import static com.dtech.ytbsearch.R.id.imgsplash;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;
    WaveDrawable mWaveDrawable;
    ImageView imgsplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prefManager = new PrefManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        imgsplash = (ImageView) findViewById(R.id.imgsplash);
        mWaveDrawable = new WaveDrawable(this, R.drawable.android_robot);
        imgsplash.setImageDrawable(mWaveDrawable);
        mWaveDrawable.setLevel(5000);
        mWaveDrawable.setIndeterminate(true);

        grabData();
    }

    private void grabData() {

        String url = "http://samimi.web.id/gitbuku/index.php";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prefManager.setJsonResponse(response);
                createFile();
                //sharedResponse = response;
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loading.dismiss();
                        Log.d("eror json: ", error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void createFile() {
        String string = "first time = false";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(Config.FIRST_TIME, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

}
