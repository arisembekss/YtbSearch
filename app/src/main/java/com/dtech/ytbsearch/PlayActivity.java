package com.dtech.ytbsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dtech.ytbsearch.adapter.MediumVideoList;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.config.CustomClickInterface;
import com.dtech.ytbsearch.data.DataJson;
import com.dtech.ytbsearch.preference.PrefManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayActivity extends YouTubeBaseActivity implements   CustomClickInterface, YouTubePlayer.OnInitializedListener {

    private static final int ITEMS_PER_AD = 6;
    YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;

    RecyclerView rcPlay;
    String id, title, jsonResponse;
    ArrayList<Object> dataJson = new ArrayList<>();
    MediumVideoList adapter;

    YouTubePlayer player;
    //String mVidId;
    AdView adView;
    //InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    String valueAd;

    /*public String getmVidId() {
        return mVidId;
    }

    public void setmVidId(String mVidId) {
        this.mVidId = mVidId;
    }*/


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.helpbtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Beberapa video tidak bisa dimainkan dikarenakan Kebijakan dari masing-masing penyedia video", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        adView = (AdView) findViewById(R.id.adView);
        if (valueAd.contains("1")) {
            adView.setVisibility(View.INVISIBLE);
            initAds();
        } else {
            adView.setVisibility(View.INVISIBLE);
        }

        initUi();
    }

    private void initAds() {

        adRequest = new AdRequest.Builder()/*.addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7")*/.build();
        adView.loadAd(adRequest);

        /*init interstitial*/
        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Config.INTERS_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7").build());*/
    }

    public void initUi() {
        rcPlay = (RecyclerView) findViewById(R.id.rcPlay);
        showResponse(jsonResponse);
        addNativeExpressAds();
        setUpAndLoadNativeExpressAds();
    }

    private void setUpAndLoadNativeExpressAds() {
        rcPlay.post(new Runnable() {
            @Override
            public void run() {
                final float scale = PlayActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                if (valueAd.contains("1")) {
                    for (int i = 0; i <= dataJson.size(); i += ITEMS_PER_AD) {
                        final NativeExpressAdView adView =
                                (NativeExpressAdView) dataJson.get(i);
                        final CardView cardView = (CardView) findViewById(R.id.adcard);

                        final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                                - cardView.getPaddingRight();
                        AdSize adSize = new AdSize((int) (adWidth / scale), 80);
                        adView.setAdSize(adSize);
                        adView.setAdUnitId(Config.NATIVE_REC);
                        /*adView.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                Log.d("onAdLoaded: ", "success");
                            }
                        });*/
                        //adView.loadAd(new AdRequest.Builder().build());

                    }

                    // Load the first Native Express ad in the items list.

                    loadNativeExpressAd(0);

                } else {
                    CardView cardView = (CardView) findViewById(R.id.adcard);
                    cardView.setVisibility(View.GONE);
                }

            }
        });
    }

    private void loadNativeExpressAd(final int index) {
        if (index >= dataJson.size()) {
            return;
        }

        Object item = dataJson.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder()/*.addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7")*/.build());
    }

    private void addNativeExpressAds() {
        for (int i = 0; i <= dataJson.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(PlayActivity.this);
            dataJson.add(i, adView);
        }
    }

    public void showResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            //String nxt = result.getString("nextPageToken");

            JSONArray items = result.getJSONArray("items");
            String etag, videoId, channelId, titleVid, urlVid;
            for (int k = 0; k < items.length(); k++) {
                JSONObject itemData = items.getJSONObject(k);
                JSONObject itemId = itemData.getJSONObject("id");
                JSONObject itemSnippet = itemData.getJSONObject("snippet");
                JSONObject snippetThumbnail = itemSnippet.getJSONObject("thumbnails");
                JSONObject thumbnailDefault = snippetThumbnail.getJSONObject("default");
                //JSONObject videoId = itemId.getJSONObject("videoId");
                if (itemId.has("videoId")) {

                    videoId = itemId.getString("videoId");
                    titleVid = itemSnippet.getString("title");
                    urlVid = thumbnailDefault.getString("url");
                    DataJson dataDump = new DataJson("", videoId, "", titleVid, urlVid);
                    dataJson.add(dataDump);
                } else {
                    channelId = "Channel Session";
                    DataJson dataDump = new DataJson("", "", channelId, "", "");
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
        //setmVidId(idv);
        //youTubeView.initialize(Config.API_YTB, this);
       try {
            this.player.loadVideo(
                    idv
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (player != null) {
            Log.d("checkinityutub", "player!=null ");
        } else {
            Log.d("checkinityutub", "player==null ");
        }
        this.player = player;
        if (!wasRestored) {
            player.cueVideo(id); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
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

        /*Random random = new Random();
        int index =random.nextInt(idVid.length);
        Log.d("index vid", String.valueOf(index));
        String smain = idVid[index];
        this.player.loadVideo(smain);
        finish();*/
        Intent intent = new Intent(PlayActivity.this, Main3Activity.class);
        intent.putExtra("vidid", "id");
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }
}
