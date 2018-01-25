package tk.ckknight.productqueue.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tk.ckknight.productqueue.R;
import tk.ckknight.productqueue.network.model.StockProductData;

/**
 * Created by sauyee on 25/1/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    private Context mContext;
    private Activity mActivity;
    private List<StockProductData> mDataList;

    public ProductAdapter(Context context, Activity activity, ArrayList<StockProductData> dataList, Bundle bundle){
        mActivity = activity;
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_item, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class ProductHolder extends RecyclerView.ViewHolder {

        public View view;
        FrameLayout poster_expired_overlay;
        LinearLayout feedContainer, portrait, downloadPlayLayout, noticeLayout;
        ImageView imagePortrait;
        ImageView btnDownloadImg;
        TextView assetType, assetTitle, duration, summary, btnDownloadText, btnNoticeText;
        RelativeLayout btnDelete, btnPlay, btnDownload, expireOverlay;

        public ProductHolder(View view) {
            super(view);
            this.view = view;
            poster_expired_overlay = (FrameLayout) view.findViewById(R.id.poster_expired_overlay);
            btnDelete = (RelativeLayout) view.findViewById(R.id.btnDelete);
            btnPlay = (RelativeLayout) view.findViewById(R.id.btnPlay);
            btnDownload = (RelativeLayout) view.findViewById(R.id.btnDownload);
            btnDownloadImg = (ImageView) view.findViewById(R.id.btnDownloadImg);
            btnDownloadText = (TextView) view.findViewById(R.id.btnDownloadText);

            feedContainer = (LinearLayout) view.findViewById(R.id.feedContainer);
            portrait = (LinearLayout) view.findViewById(R.id.portrait);
            imagePortrait = (ImageView) view.findViewById(R.id.imagePortrait);

            assetType = (TextView) view.findViewById(R.id.assetType);
            assetTitle = (TextView) view.findViewById(R.id.assetTitle);
            duration = (TextView) view.findViewById(R.id.duration);
            summary = (TextView) view.findViewById(R.id.summary);
            btnNoticeText = (TextView) view.findViewById(R.id.btnNoticeText);

            downloadPlayLayout = (LinearLayout) view.findViewById(R.id.downloadPlayLayout);
            noticeLayout = (LinearLayout) view.findViewById(R.id.noticeLayout);
            expireOverlay = (RelativeLayout) view.findViewById(R.id.expireOverlay);
        }
    }
}
