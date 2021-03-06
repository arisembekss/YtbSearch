package com.dtech.ytbsearch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtech.ytbsearch.PlayActivity;
import com.dtech.ytbsearch.R;
import com.dtech.ytbsearch.SplashActivity;
import com.dtech.ytbsearch.data.DataJson;
import com.squareup.picasso.Picasso;

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

       /* MyHolder myHolder = (MyHolder) holder;
        DataJson current = data.get(position);
        myHolder.listTv.setText(current.videoId);
        myHolder.listTitle.setText(current.titleVid);
        myHolder.listUrl.setText(current.urlVid);
        Picasso.with(context).load(current.urlVid)
                .error(R.mipmap.ic_bussy)
                .placeholder(R.mipmap.ic_text_loading)
                .into(myHolder.imgUrl);*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listTv, listTitle, listUrl;
        ImageView imgUrl;
        CardView cardView;
        public MyHolder(View itemView) {
            super(itemView);

            listTv = (TextView) itemView.findViewById(R.id.listVideoId);
            listTitle =(TextView) itemView.findViewById(R.id.listTitle);
            listUrl = (TextView) itemView.findViewById(R.id.listUrl);
            imgUrl = (ImageView) itemView.findViewById(R.id.imgUrl);
            cardView = (CardView) itemView.findViewById(R.id.card);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String vidId = listTv.getText().toString();
            String titileVid = listTitle.getText().toString();
            Intent playVid = new Intent(context, PlayActivity.class);
            playVid.putExtra("id", vidId);
            playVid.putExtra("title", titileVid);
            context.startActivity(playVid);
        }
    }
}
