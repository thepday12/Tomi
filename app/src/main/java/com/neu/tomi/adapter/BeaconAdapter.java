package com.neu.tomi.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.BeaconObject;
import com.neu.tomi.object.ItemObject;

import java.util.List;

public class BeaconAdapter extends RecyclerView
        .Adapter<BeaconAdapter
        .DataObjectHolder> {
    private List<BeaconObject> mDataset;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
    {
        TextView tvBeaconName;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvBeaconName = (TextView) itemView.findViewById(R.id.tvBeaconName);
        }

    }


    public BeaconAdapter(List<BeaconObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_beacon, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.tvBeaconName.setText(mDataset.get(position).getName());
    }

    public void addItem(BeaconObject dataObj, int index) {
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