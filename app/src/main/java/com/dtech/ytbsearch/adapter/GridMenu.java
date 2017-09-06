package com.dtech.ytbsearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dtech.ytbsearch.R;
import com.pkmmte.view.CircularImageView;

/**
 * Created by lenovo on 02/09/2017.
 */

public class GridMenu extends BaseAdapter {

    private Context context;
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

        //RelativeLayout linmain = (RelativeLayout) grid.findViewById(R.id.linemain);
        TextView tTitle = (TextView) grid.findViewById(R.id.tTitle);
        final TextView tQuery = (TextView) grid.findViewById(R.id.tQuery);
        CircularImageView img = (CircularImageView) grid.findViewById(R.id.imgGrid);
        /*img.setBorderColor(R.color.grey);*/
        img.setBorderWidth(4);
        //img.addShadow();
        //img.setSelectorStrokeWidth(10);

        tTitle.setText(title[i]);
        tQuery.setText(query[i]);
        img.setImageResource(image[i]);

        /*linmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        return grid;
    }


}
