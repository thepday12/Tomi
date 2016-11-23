package com.neu.tomi.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.BonusItemObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.ultity.DataItems;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemBonusAdapter extends RecyclerView
        .Adapter<ItemBonusAdapter
        .DataObjectHolder> {
    private List<BonusItemObject> mDataset;
    private  DataItems mDataItems;
    private Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
    {
        ImageView ivItem;
        TextView tvQtyItem;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvQtyItem = (TextView) itemView.findViewById(R.id.tvQtyItem);

        }

    }


    public ItemBonusAdapter(List<BonusItemObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_bonus, parent, false);
        mContext=view.getContext();
        mDataItems=new DataItems(mContext);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        BonusItemObject bonusItemObject=mDataset.get(position);
        ItemObject itemObject = mDataItems.getItem(bonusItemObject.getItemId());
        if(itemObject==null){
            Picasso.with(mContext).load(bonusItemObject.getImageURL()).into(holder.ivItem);
        }else {
            if (itemObject.isResource()) {
                holder.ivItem.setImageResource(itemObject.getIconResouce());
            } else {
                Picasso.with(mContext).load(DataItems.uriIconItem(itemObject, mContext)).into(holder.ivItem);
            }
        }
        holder.tvQtyItem.setText("+" + bonusItemObject.getItemBonus());
    }

    public void addItem(BonusItemObject dataObj, int index) {
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