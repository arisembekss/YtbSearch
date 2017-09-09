package com.dtech.ytbsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.preference.PrefManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Main3Activity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {

    public static String[] mainTitle /*= {
            "New Palapa Terbaru",
            "Monata Terbaru",
            "Sagita Terbaru",
            "Sera Terbaru"

    }*/;

    public static String[] mainQuery /*= {
            "pallapa+terbaru+2017",
            "monata+terbaru",
            "sagita+teerbaru",
            "sera+terbaru"
    }*/;

    String[] idVid = {"SeMiC8QGL0w", "9J3UJxnnsng", "SeMiC8QGL0w", "UtjFu8c_goE", "tzlz2ZVpCXo"};
    String main;

    //private static final int RECOVERY_REQUEST = 1;
    GridView grid;
    Button btnArtis;
    ProgressDialog loading;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    String valueInters, valueAd, prefmaintitle, prefsectitle, prefmainvid, prefsecvid;
    //YouTubePlayerView youTubePlayerView;
    YouTubePlayerFragment youTubePlayerFragment;
    YouTubePlayer player;
    DatabaseReference intersRef, adRef;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        MobileAds.initialize(this, Config.APP_ID);
        randomVidd();
        Intent intentvid = getIntent();
        if (intentvid != null) {
            Log.d("tagintent", "onCreate: intent !=null");
        } else {
            Log.d("tagintent", "onCreate: intent ==null");
        }

        prefManager = new PrefManager(this);
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        valueAd = (sharedPreferences.getString(Config.VAL_AD, ""));
        valueInters = (sharedPreferences.getString(Config.VAL_INTERS, ""));
        prefmaintitle = (sharedPreferences.getString(Config.MAIN_TITLE, ""));
        prefmainvid = (sharedPreferences.getString(Config.MAIN_VID, ""));
        mainTitle = prefmaintitle.split(",");
        mainQuery = prefmainvid.split(",");
        Log.d("prefmain", prefmaintitle+"\n"+prefmainvid);
        youTubePlayerFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.yview);
        youTubePlayerFragment.initialize(Config.API_YTB,this
                /*new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.

                        Random random = new Random();
                        int index =random.nextInt(idVid.length);
                        Log.d("index vid", String.valueOf(index));
                        String smain = idVid[index];

                        youTubePlayer.cueVideo(smain);

                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                }*/);
        youTubePlayerFragment.onResume();
        /*youTubePlayerView = (YouTubePlayerView) findViewById(R.id.yview);
        youTubePlayerView.initialize(Config.API_YTB, this);*/

        /*Random random = new Random();
        int index =random.nextInt(idVid.length);
        Log.d("index vid", String.valueOf(index));
        String smain = idVid[index];
        randomVid(smain);*/
        initFbase();
        initUi();
    }

    private String randomVidd() {
        Random random = new Random();
        int index =random.nextInt(idVid.length);
        //Log.d("index vid", String.valueOf(index));
        main = idVid[index];
        return main;
    }

    private void initFbase() {
        adRef = FirebaseDatabase.getInstance().getReference("ytb").child("vallen").child("banner-native/status");
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

        intersRef = FirebaseDatabase.getInstance().getReference("ytb").child("vallen").child("inters/status");
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
    }

    private void initUi() {

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Config.INTERS_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder()/*.addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7")*/.build());
        grid = (GridView) findViewById(R.id.griMain);
        btnArtis = (Button) findViewById(R.id.bmain);
        GridMainMenu adapter = new GridMainMenu(this, mainTitle, mainQuery);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView ttitle = (TextView) view.findViewById(R.id.tmain1);
                TextView vidId = (TextView) view.findViewById(R.id.tmain2);

                prosesPilih(vidId.getText().toString());

            }
        });

        btnArtis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void prosesPilih(String query) {
        loading = new ProgressDialog(this);
        loading.setMessage("Preparing data");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loading.show();

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
            //String nxt = result.getString("nextPageToken");

            JSONArray items = result.getJSONArray("items");

            //for (int k = 0; k < items.length(); k++) {
                JSONObject itemData = items.getJSONObject(0);
                JSONObject itemId = itemData.getJSONObject("id");
                //JSONObject itemSnippet = itemData.getJSONObject("snippet");
                //JSONObject snippetThumbnail = itemSnippet.getJSONObject("thumbnails");
                //JSONObject thumbnailDefault = snippetThumbnail.getJSONObject("high");
                //JSONObject videoId = itemId.getJSONObject("videoId");
                if (itemId.has("videoId")) {

                    vidId = itemId.getString("videoId");
                    Intent intent = new Intent(Main3Activity.this, PlayActivity.class);
                    intent.putExtra("title", "");
                    intent.putExtra("id", vidId);
                    intent.putExtra("response", response);
                    startActivity(intent);
                    finish();
                    //dataJson.add(dataDump);
                } else {
                    Toast.makeText(Main3Activity.this, "Currently Unavailable", Toast.LENGTH_SHORT).show();
                }

            //}


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void randomVid(String smain) {

        if (this.player != null) {
            this.player.loadVideo(smain);
            Log.d("sukses", "load vid");
        } else {
            Log.d("fail", "unload vid");
        }

    }*/

    /*@Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        Random random = new Random();
        int index =random.nextInt(idVid.length);
        Log.d("index vid", String.valueOf(index));
        String smain = idVid[index];

        this.player = youTubePlayer;

        if (!b) {

            Log.d("restored", "wasnot");
            randomVid(smain);
        } else {
            Log.d("restored", "wast");
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
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
       /* Random random = new Random();
        int index =random.nextInt(idVid.length);
        Log.d("index vid", String.valueOf(index));
        String smain = idVid[index];
        randomVid(smain);*/
        /*Random random = new Random();
        int index =random.nextInt(idVid.length);
        Log.d("index restart", String.valueOf(index));
        String mainresume = idVid[index];


        this.player.loadVideo(mainresume);*/
        if (player == null) {
            Log.d("ytbcek", "onRestart: player null");

        } else {
            Log.d("ytbcek", "onRestart: player !null");
            /*player = null;
            Random random = new Random();
            int index =random.nextInt(idVid.length);
            Log.d("index vid", String.valueOf(index));
            String smainn = idVid[index];
            player.cueVideo(smainn);*/
            //youTubePlayerFragment.initialize(Config.API_YTB, this);
            /*Random random = new Random();
            int index =random.nextInt(idVid.length);
            Log.d("index vid", String.valueOf(index));
            final String smainn = idVid[index];
            youTubePlayerFragment = null;
            youTubePlayerFragment=(YouTubePlayerFragment)
                    getFragmentManager().findFragmentById(R.id.yview);
            player = null;
            youTubePlayerFragment.initialize(Config.API_YTB, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    player.loadVideo(smainn, 2000);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });*/


        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (player == null) {

            Log.d("ytbcek", "onReume: player null");
        } else {
            Log.d("ytbcek", "onReume: player !null");
            //player.play();
        }
    }

    @Override
    public void onBackPressed() {


        if (mInterstitialAd.isLoaded()) {
            if (valueInters.contains("1")) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        finish();
                    }
                });
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.player = youTubePlayer;

        if (!b) {
            Random random = new Random();
            int index =random.nextInt(idVid.length);
            Log.d("index vid", String.valueOf(index));
            String smain = idVid[index];
            player.cueVideo(smain);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
