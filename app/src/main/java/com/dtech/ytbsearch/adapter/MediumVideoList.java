package com.dtech.ytbsearch.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtech.ytbsearch.R;
import com.dtech.ytbsearch.config.CustomClickInterface;
import com.dtech.ytbsearch.data.DataJson;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by lenovo on 10/08/2017.
 */

public class MediumVideoList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_PER_AD = 6;
    private Context context;
    private LayoutInflater inflater;
    private List<Object> data = Collections.emptyList();
    private static CustomClickInterface clickListener;
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The Native Express ad view type.
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;

    public MediumVideoList(Context context, List<Object> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                View viewad = inflater.inflate(R.layout.medium_ad_list_layout, parent, false);
                return new NativeExpressAdViewHolder(viewad);
            case MENU_ITEM_VIEW_TYPE:
            default:
                View view = inflater.inflate(R.layout.medium_list_layout, parent, false);
                return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                MyHolder myHolder = (MyHolder) holder;
                DataJson current = (DataJson) data.get(position);
                myHolder.listTv.setText(current.getVideoId());
                myHolder.listTitle.setText(current.getTitleVid());
                /*myHolder.listUrl.setText(current.urlVid);*/
                Picasso.with(context).load(current.getUrlVid())
                        .error(R.mipmap.ic_launcher_round)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(myHolder.imgUrl);
                break;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
            default:

                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) data.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position % ITEM_PER_AD == 0) ? NATIVE_EXPRESS_AD_VIEW_TYPE
                : MENU_ITEM_VIEW_TYPE;
    }

    public void setClickListener(CustomClickInterface itemClickListener) {
        clickListener = itemClickListener;
    }

    private class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listTv, listTitle, listUrl;
        ImageView imgUrl;
        CardView cardView;


        public MyHolder(View itemView) {
            super(itemView);

            listTv = (TextView) itemView.findViewById(R.id.listVideoId);
            listTitle =(TextView) itemView.findViewById(R.id.listTitle);
            /*listUrl = (TextView) itemView.findViewById(R.id.listUrl);*/
            imgUrl = (ImageView) itemView.findViewById(R.id.imgUrl);
            cardView = (CardView) itemView.findViewById(R.id.card);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String vidId = listTv.getText().toString();
            //String titileVid = listTitle.getText().toString();

            if (clickListener != null) {
                clickListener.onClick(view, vidId);
            }
            /*Intent playVid = new Intent(context, PlayActivity.class);
            playVid.putExtra("id", vidId);
            playVid.putExtra("title", titileVid);
            context.startActivity(playVid);*/
        }
    }
}
