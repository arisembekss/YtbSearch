package com.dtech.ytbsearch.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dtech.ytbsearch.Main3Activity;
import com.dtech.ytbsearch.PlayActivity;
import com.dtech.ytbsearch.R;
import com.dtech.ytbsearch.config.Config;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 02/09/2017.
 */

public class GridMenu extends BaseAdapter {

    Context context;
    private final String[] title;
    private final String[] query;
    private final int[] image;

    public GridMenu(Context context, String[] title, String[] query, int[] image) {
        this.context = context;
        this.title = title;
        this.query = query;
        this.image = image;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View grid;

        if (view == null) {
            //grid = new View(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.item_grid, viewGroup, false);

            /*img.setBackgroundResource(image[i]);*/
            /*ImageView img = (ImageView)grid.findViewById()*/
        } else {
            grid = view;
        }

        LinearLayout linmain = (LinearLayout) grid.findViewById(R.id.linmain);
        TextView tTitle = (TextView) grid.findViewById(R.id.tTitle);
        final TextView tQuery = (TextView) grid.findViewById(R.id.tQuery);
        CircularImageView img = (CircularImageView) grid.findViewById(R.id.imgGrid);
        img.setBorderColor(R.color.grey);
        img.setBorderWidth(4);
        img.addShadow();
        img.setSelectorStrokeWidth(10);

        tTitle.setText(title[i]);
        tQuery.setText(query[i]);
        img.setImageResource(image[i]);

        linmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prosesPilih(tQuery.getText().toString());
            }
        });
        return grid;
    }

    private void prosesPilih(String query) {
        final ProgressDialog loading = new ProgressDialog(context);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("id", vidId);
                intent.putExtra("response", response);
                context.startActivity(intent);
                //dataJson.add(dataDump);
            } else {
                Toast.makeText(context, "Currently Unavailable", Toast.LENGTH_SHORT).show();
            }

            //}


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
