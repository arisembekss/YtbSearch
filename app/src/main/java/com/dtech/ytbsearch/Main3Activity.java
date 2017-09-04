package com.dtech.ytbsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dtech.ytbsearch.adapter.GridMainMenu;
import com.dtech.ytbsearch.adapter.MediumVideoList;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.data.DataJson;
import com.dtech.ytbsearch.preference.PrefManager;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main3Activity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static String[] mainTitle = {
            "New Palapa Terbaru",
            "Monata Terbaru",
            "Sagita Terbaru",
            "Sera Terbaru"

    };

    public static String[] mainQuery = {
            "new+palapa+terbaru",
            "monata+terbaru",
            "sagita+teerbaru",
            "sera+terbaru"
    };

    private static final int RECOVERY_REQUEST = 1;
    GridView grid;
    Button btnArtis;
    ProgressDialog loading;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    String valueInters, valueAd;
    YouTubePlayerView youTubePlayerView;
    DatabaseReference intersRef, adRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        prefManager = new PrefManager(this);
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        valueAd = (sharedPreferences.getString(Config.VAL_AD, ""));
        valueInters = (sharedPreferences.getString(Config.VAL_INTERS, ""));
        adRef = FirebaseDatabase.getInstance().getReference("ytb").child("vallen").child("banner-native");
        adRef.keepSynced(true);
        adRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prefManager.setAd(String.valueOf(dataSnapshot.getValue()));
                Log.d("value-dbase-ad", String.valueOf(dataSnapshot.getValue()));
                Log.d("value-pref-ad", valueAd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        intersRef = FirebaseDatabase.getInstance().getReference("ytb").child("vallen").child("inters");
        intersRef.keepSynced(true);
        intersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prefManager.setadInters(String.valueOf(dataSnapshot.getValue()));
                Log.d("value-dbase-inter", String.valueOf(dataSnapshot.getValue()));
                Log.d("value-pref-inters", valueInters);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.yview);
        youTubePlayerView.initialize(Config.API_YTB, this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                startActivity(intent);
            }
        });*/

        initUi();
    }

    private void initUi() {

        grid = (GridView) findViewById(R.id.griMain);
        btnArtis = (Button) findViewById(R.id.bmain);
        GridMainMenu adapter = new GridMainMenu(this, mainTitle, mainQuery);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView ttitle = (TextView) view.findViewById(R.id.tmain1);
                TextView vidId = (TextView) view.findViewById(R.id.tmain2);

                prosesPilih(ttitle.getText().toString(), vidId.getText().toString());

            }
        });

        btnArtis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private void prosesPilih(String title, String query) {
        loading = new ProgressDialog(this);
        loading.setMessage("Preparing data");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loading.show();
        String url = "http://samimi.web.id/ytb/index.php?q=via vallenn&&maxResults=50";
        StringRequest stringRequest = new StringRequest(Config.URL_REQ+query+Config.URL_MAX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
                //sharedResponse = response;
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.d("eror json: ", error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        String vidId;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            String nxt = result.getString("nextPageToken");

            JSONArray items = result.getJSONArray("items");

            //for (int k = 0; k < items.length(); k++) {
                JSONObject itemData = items.getJSONObject(0);
                JSONObject itemId = itemData.getJSONObject("id");
                JSONObject itemSnippet = itemData.getJSONObject("snippet");
                JSONObject snippetThumbnail = itemSnippet.getJSONObject("thumbnails");
                JSONObject thumbnailDefault = snippetThumbnail.getJSONObject("high");
                //JSONObject videoId = itemId.getJSONObject("videoId");
                if (itemId.has("videoId")) {

                    vidId = itemId.getString("videoId");
                    Intent intent = new Intent(Main3Activity.this, PlayActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("id", vidId);
                    intent.putExtra("response", response);
                    startActivity(intent);
                    //dataJson.add(dataDump);
                } else {
                    Toast.makeText(Main3Activity.this, "Currently Unavailable", Toast.LENGTH_SHORT).show();
                }

            //}


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (!b) {
            //youTubePlayer.loadVideo("RcmZ-zn02kA");
            youTubePlayer.cueVideo("");
            //youTubePlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {

        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}
