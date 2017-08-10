package com.dtech.ytbsearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtech.ytbsearch.R;
import com.dtech.ytbsearch.data.DataJson;

import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 10/08/2017.
 */

public class VideoList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataJson> data = Collections.emptyList();

    public VideoList(Context context, List<DataJson> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_layout, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        DataJson current = data.get(position);
        myHolder.listTv.setText(current.videoId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView listTv;
        public MyHolder(View itemView) {
            super(itemView);

            listTv = (TextView) itemView.findViewById(R.id.listTv);
        }
    }
}
