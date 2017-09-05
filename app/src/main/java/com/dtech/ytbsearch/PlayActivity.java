package com.dtech.ytbsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dtech.ytbsearch.adapter.MediumVideoList;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.config.CustomClickInterface;
import com.dtech.ytbsearch.data.DataJson;
import com.dtech.ytbsearch.preference.PrefManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayActivity extends YouTubeBaseActivity implements   CustomClickInterface, YouTubePlayer.OnInitializedListener {

    YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;

    RecyclerView rcPlay;
    String id, title, jsonResponse;
    ArrayList<DataJson> dataJson = new ArrayList<>();
    MediumVideoList adapter;

    YouTubePlayer player;
    String mVidId;
    AdView adView;
    //InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    String valueAd;

    public String getmVidId() {
        return mVidId;
    }

    public void setmVidId(String mVidId) {
        this.mVidId = mVidId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        MobileAds.initialize(this, Config.APP_ID);

        prefManager = new PrefManager(this);
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        valueAd = (sharedPreferences.getString(Config.VAL_AD, ""));
        Intent value = getIntent();
        title = value.getStringExtra("title");
        id = value.getStringExtra("id");
        jsonResponse = value.getStringExtra("response");
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.API_YTB, this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        adView = (AdView) findViewById(R.id.adView);
        if (valueAd.contains("1")) {
            adView.setVisibility(View.VISIBLE);
            initAds();
        } else {
            adView.setVisibility(View.INVISIBLE);
        }

        initUi();
    }

    private void initAds() {

        adRequest = new AdRequest.Builder().addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7").build();
        adView.loadAd(adRequest);

        /*init interstitial*/
        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Config.INTERS_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7").build());*/
    }

    public void initUi() {
        rcPlay = (RecyclerView) findViewById(R.id.rcPlay);
        showResponse(jsonResponse);
    }

    public void showResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            //String nxt = result.getString("nextPageToken");

            JSONArray items = result.getJSONArray("items");

            for (int k = 0; k < items.length(); k++) {
                JSONObject itemData = items.getJSONObject(k);
                JSONObject itemId = itemData.getJSONObject("id");
                JSONObject itemSnippet = itemData.getJSONObject("snippet");
                JSONObject snippetThumbnail = itemSnippet.getJSONObject("thumbnails");
                JSONObject thumbnailDefault = snippetThumbnail.getJSONObject("default");
                //JSONObject videoId = itemId.getJSONObject("videoId");
                if (itemId.has("videoId")) {
                    DataJson dataDump = new DataJson();
                    dataDump.videoId = itemId.getString("videoId");
                    dataDump.titleVid = itemSnippet.getString("title");
                    dataDump.urlVid = thumbnailDefault.getString("url");
                    dataJson.add(dataDump);
                } else {
                    DataJson dataDump = new DataJson();
                    dataDump.channelId = "Channel Session";
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new MediumVideoList(PlayActivity.this, dataJson);
        adapter.setClickListener(this);
        rcPlay.setAdapter(adapter);
        rcPlay.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view, String idv) {
        //final String vidid = id;
        setmVidId(idv);
        //youTubeView.initialize(Config.API_YTB, this);
        this.player.loadVideo(
                idv
        );
    }



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        if (!wasRestored) {
            this.player.cueVideo(id); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        } /*else {
            player.loadVideo(this.getmVidId());
        }*/
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

    @Override
    public void onBackPressed() {

        /*if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }*/

        super.onBackPressed();
    }
}
