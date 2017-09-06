package com.dtech.ytbsearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dtech.ytbsearch.R;

/**
 * Created by lenovo on 02/09/2017.
 */

public class GridMainMenu extends BaseAdapter {

    private Context context;
    private final String[] title;
    private final String[] query;


    public GridMainMenu(Context context, String[] title, String[] query) {
        this.context = context;
        this.title = title;
        this.query = query;

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
            grid = inflater.inflate(R.layout.item_grid_main, viewGroup, false);

            /*img.setBackgroundResource(image[i]);*/
            /*ImageView img = (ImageView)grid.findViewById()*/
        } else {
            grid = view;
        }

        TextView tTitle = (TextView) grid.findViewById(R.id.tmain1);
        TextView tQuery = (TextView) grid.findViewById(R.id.tmain2);


        tTitle.setText(title[i]);
        tQuery.setText(query[i]);

        return grid;
    }
}
