package com.neu.tomi.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.neu.tomi.R;
import com.neu.tomi.object.ItemObject;

import java.util.List;

public class ItemDailyAdapter extends RecyclerView
        .Adapter<ItemDailyAdapter
        .DataObjectHolder> {
    private List<ItemObject> mDataset;
    private Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView ivItemAction;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ivItemAction = (ImageView) itemView.findViewById(R.id.ivItemAction);
        }

    }


    public ItemDailyAdapter(List<ItemObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_daily, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ItemObject.setImage(mContext,mDataset.get(position).getId(),holder.ivItemAction);
    }

    public void addItem(ItemObject dataObj, int index) {
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