package com.dtech.ytbsearch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.dtech.ytbsearch.adapter.GridMenu;

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
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen,
            R.drawable.vallen
    };

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        initUi();
    }

    private void initUi() {
        gridView = (GridView) findViewById(R.id.grid);

        GridMenu adapter = new GridMenu(this, gridTitle, gridQuery, gridImage);
        gridView.setAdapter(adapter);

    }

}
