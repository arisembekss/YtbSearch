package com.dtech.ytbsearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dtech.ytbsearch.adapter.VideoList;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.data.DataJson;
import com.dtech.ytbsearch.preference.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String prefResponse;
    SharedPreferences sharedPreferences;
    PrefManager prefManager;
    ProgressDialog loading;
    TextView textView;

    RecyclerView recyclerHist;
    List<DataJson> dataJson = new ArrayList<>();
    VideoList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        prefManager = new PrefManager(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*if (!fileExistance(Config.FIRST_TIME)) {
            launchSplash();
            finish();
        } else {
            iniUI();
        }*/
        //grabData();
        iniUI();

    }

    private boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private void launchSplash() {
        Intent splash = new Intent(this, SplashActivity.class);
        startActivity(splash);
    }

    private void iniUI() {
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);
        prefResponse = (sharedPreferences.getString(Config.RESPONSE, ""));
        recyclerHist = (RecyclerView) findViewById(R.id.rechisto);
        Log.d("pref Response", prefResponse);
        if (prefResponse == "") {
            launchSplash();
            finish();
        } else {
            //grabData();
            showJSON(prefResponse);
        }
    }

    private void grabData() {
        //loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        loading = new ProgressDialog(this);
        loading.setMessage("Preparing data");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loading.show();
        String url = "http://samimi.web.id/ytb/index.php?q=via vallenn&&maxResults=50";
        StringRequest stringRequest = new StringRequest(Config.URL_REQ, new Response.Listener<String>() {
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

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            String nxt = result.getString("nextPageToken");

            JSONArray items = result.getJSONArray("items");

            for (int k = 0; k < items.length(); k++) {
                JSONObject itemData = items.getJSONObject(k);
                JSONObject itemId = itemData.getJSONObject("id");
                JSONObject itemSnippet = itemData.getJSONObject("snippet");
                JSONObject snippetThumbnail = itemSnippet.getJSONObject("thumbnails");
                JSONObject thumbnailDefault = snippetThumbnail.getJSONObject("medium");
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
        adapter = new VideoList(MainActivity.this, dataJson);

        adapter.notifyDataSetChanged();
        recyclerHist.setAdapter(adapter);

        recyclerHist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
