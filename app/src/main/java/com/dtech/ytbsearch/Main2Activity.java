package com.dtech.ytbsearch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.dtech.ytbsearch.adapter.GridMenu;
import com.dtech.ytbsearch.config.Config;
import com.dtech.ytbsearch.preference.PrefManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

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

        adRequest = new AdRequest.Builder().addTestDevice("D1CB1A0F81471E6BF7A338ECB8C9A2C7").build();
        adView.loadAd(adRequest);
    }

    private void initUi() {
        gridView = (GridView) findViewById(R.id.grid);

        GridMenu adapter = new GridMenu(this, gridTitle, gridQuery, gridImage);
        gridView.setAdapter(adapter);

    }

}
