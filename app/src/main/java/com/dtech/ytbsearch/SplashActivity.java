package com.dtech.ytbsearch;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.race604.drawable.wave.WaveDrawable;

import java.io.FileOutputStream;

import static com.dtech.ytbsearch.R.id.imgsplash;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;
    WaveDrawable mWaveDrawable;
    ImageView imgsplash;
    DatabaseReference refSecTitle, refSecVid, refMainTitle, refMainVid;
    String prefsectitle, prefsecvid, prefmaintitle, prefmainvid;
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
        mWaveDrawable = new WaveDrawable(this, R.drawable.ic_launcherr);
        imgsplash.setImageDrawable(mWaveDrawable);
        mWaveDrawable.setLevel(5000);
        mWaveDrawable.setIndeterminate(true);

        new Loading().execute();
    }

    public class Loading extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            refSecTitle = FirebaseDatabase.getInstance().getReference().child("ytb").child("vallen").child("title");
            refMainTitle = FirebaseDatabase.getInstance().getReference().child("ytb").child("vallen").child("titlemain");
            refSecVid = FirebaseDatabase.getInstance().getReference().child("ytb").child("vallen").child("vidartis");
            refMainVid = FirebaseDatabase.getInstance().getReference().child("ytb").child("vallen").child("vidmain");

            refSecTitle.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    prefsectitle = String.valueOf(dataSnapshot.getValue());
                    prefManager.setSecTitle(prefsectitle);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            refMainTitle.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    prefmaintitle = String.valueOf(dataSnapshot.getValue());
                    prefManager.setMainTitle(prefmaintitle);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            refSecVid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    prefsecvid = String.valueOf(dataSnapshot.getValue());
                    prefManager.setSecVid(prefsecvid);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            refMainVid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    prefmainvid = String.valueOf(dataSnapshot.getValue());
                    prefManager.setMainVid(prefmainvid);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //if (prefmainvid != null) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            //}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(SplashActivity.this, Main3Activity.class);
            startActivity(intent);
            finish();
        }
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
