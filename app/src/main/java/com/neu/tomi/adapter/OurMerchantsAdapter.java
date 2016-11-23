package com.neu.tomi.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.OurMerchants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OurMerchantsAdapter extends RecyclerView
        .Adapter<OurMerchantsAdapter
        .DataObjectHolder> {
    private List<OurMerchants> mDataset;
    private Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle;
        ImageView ivLogo;
        ProgressBar pbImageProgress;
        public DataObjectHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
            pbImageProgress = (ProgressBar)itemView.findViewById(R.id.pbImageProgress);
        }

    }


    public OurMerchantsAdapter(List<OurMerchants> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_our_merchants, parent, false);
        mContext=view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        OurMerchants ourMerchants =mDataset.get(position);
        holder.tvTitle.setText(ourMerchants.getName());
        Log.e("RESPONSE_SERVER",ourMerchants.getImageLink());
        Picasso.with(mContext).load(ourMerchants.getImageLink()).into(holder.ivLogo, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                holder.pbImageProgress.setVisibility(View.GONE);
                super.onSuccess();
            }
        });
    }

    public void addItem(OurMerchants dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}