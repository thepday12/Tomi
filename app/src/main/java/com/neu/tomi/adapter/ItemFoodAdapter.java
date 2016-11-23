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
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.FoodObject;
import com.neu.tomi.object.ItemObject;

import java.util.List;

public class ItemFoodAdapter extends RecyclerView
        .Adapter<ItemFoodAdapter
        .DataObjectHolder> {
    private List<FoodObject> mDataset;
    private Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView ivItemAction;
        TextView tvSizeItems;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ivItemAction = (ImageView) itemView.findViewById(R.id.ivItemAction);
            tvSizeItems = (TextView) itemView.findViewById(R.id.tvSizeItems);
        }

    }


    public ItemFoodAdapter(List<FoodObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_eat, parent, false);
        mContext = view.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        FoodObject foodObject=mDataset.get(position);
        ItemObject.setImage(mContext,foodObject.getItemObject().getId(),holder.ivItemAction);
        holder.tvSizeItems.setText(String.valueOf(foodObject.getQty()));
    }

    public void addItem(FoodObject dataObj, int index) {
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

    class ConvertFormatDataset {
        private ItemObject itemObject;
        private int size;
        public ConvertFormatDataset (ItemObject itemObject){
            setItemObject(itemObject);
            setSize(1);
        }
        public ItemObject getItemObject() {
            return itemObject;
        }

        public void setItemObject(ItemObject itemObject) {
            this.itemObject = itemObject;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}