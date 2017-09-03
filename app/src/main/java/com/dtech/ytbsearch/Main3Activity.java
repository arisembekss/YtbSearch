package com.dtech.ytbsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dtech.ytbsearch.adapter.GridMainMenu;
import com.dtech.ytbsearch.config.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

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
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

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
                TextView ttitle = (TextView) grid.findViewById(R.id.tmain1);
                TextView vidId = (TextView) grid.findViewById(R.id.tmain2);

                Intent intent = new Intent(Main3Activity.this, PlayActivity.class);
                intent.putExtra("title", ttitle.getText().toString());
                intent.putExtra("id", vidId.getText().toString());
                startActivity(intent);
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
