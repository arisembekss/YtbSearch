package com.dtech.ytbsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dtech.ytbsearch.adapter.GridMenu;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.preference.PrefManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    public static String[] gridTitle = {
            "Nella Kharisma",
            "Via Vallen",
            "Ratna Antika",
            "Anjar Agustin",
            "Uut Selly",
            "Yeyen Vivia",
            "Cupi Cupita",
            "Lynda Moy",
            "Kiki Syarah",
            "Lina Marlina",
            "Wika Salim",
            "Mela Barbie",
            "Ana Velisa",
            "Sasha Aneska",
            "Lia Capucino"
    };

    public static String[] gridQuery = {
            "nella+kharisma",
            "via+vallen",
            "Ratna+Antika",
            "Anjar+Agustin",
            "Uut+Selly",
            "Yeyen+Vivia",
            "Cupi+Cupita",
            "Lynda+Moy",
            "Kiki+Syarah+hot",
            "Lina+Marlina",
            "Wika+Salim+konser",
            "Mela+Barbie",
            "Ana+Velisa",
            "Sasha+Aneska",
            "Lia+Capucino"
    };

    public static int[] gridImage = {
            R.drawable.nella,
            R.drawable.vallen,
            R.drawable.ratna,
            R.drawable.anjar,
            R.drawable.uut,
            R.drawable.yeyen,
            R.drawable.cupi,
            R.drawable.lynda,
            R.drawable.kiki,
            R.drawable.lina,
            R.drawable.wika,
            R.drawable.mela,
            R.drawable.ana,
            R.drawable.sasha,
            R.drawable.lia
    };

    /*String[] idVid = {"rncPjUkqXeA", "9J3UJxnnsng", "SeMiC8QGL0w", "UtjFu8c_goE", "tzlz2ZVpCXo"};*/
    GridView gridView;
    AdRequest adRequest;
    PrefManager prefManager;
    SharedPreferences sharedPreferences;
    NativeExpressAdView adView;
    String valueAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        prefManager = new PrefManager(this);
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        valueAd = (sharedPreferences.getString(Config.VAL_AD, ""));

        adView = (NativeExpressAdView)findViewById(R.id.nativeadView);
        if (valueAd.contains("1")) {
            adView.setVisibility(View.VISIBLE);
            initAds();
        } else {
            adView.setVisibility(View.INVISIBLE);
        }
        initUi();
    }

    public void initAds() {

        adRequest = new AdRequest.Builder()/*.addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7")*/.build();
        adView.loadAd(adRequest);
    }

    private void initUi() {
        gridView = (GridView) findViewById(R.id.grid);

        GridMenu adapter = new GridMenu(this, gridTitle, gridQuery, gridImage);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tquery = (TextView) view.findViewById(R.id.tQuery);
                prosesPilih(tquery.getText().toString());
            }
        });

    }

    private void prosesPilih(String query) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("Preparing data");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        /*loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
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
            /*JSONObject itemSnippet = itemData.getJSONObject("snippet");
            JSONObject snippetThumbnail = itemSnippet.getJSONObject("thumbnails");
            JSONObject thumbnailDefault = snippetThumbnail.getJSONObject("high");*/
            //JSONObject videoId = itemId.getJSONObject("videoId");
            if (itemId.has("videoId")) {

                vidId = itemId.getString("videoId");
                Intent intent = new Intent(Main2Activity.this, PlayActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("id", vidId);
                intent.putExtra("response", response);
                startActivity(intent);
                finish();
                //dataJson.add(dataDump);
            } else {
                Toast.makeText(this, "Currently Unavailable", Toast.LENGTH_SHORT).show();
            }

            //}


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        startActivity(intent);
        finish();
    }
}
