package com.neu.tomi.adapter;

/**
 * Created by Thep on 10/18/2015.
 */

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.MailObject;

import java.util.List;

public class MailAdapter extends RecyclerView
        .Adapter<MailAdapter
        .DataObjectHolder> {
    private List<MailObject> mDataset;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle, tvContent;
        ImageView ivIconMail;
        public DataObjectHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            ivIconMail = (ImageView) itemView.findViewById(R.id.ivIconMail);
        }

    }


    public MailAdapter(List<MailObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mail, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        MailObject mailObject =mDataset.get(position);
        holder.tvTitle.setText(mailObject.getTitle());
        holder.tvContent.setText(mailObject.getContent());
        if(!mailObject.isState()){
            holder.tvTitle.setTypeface(null,Typeface.BOLD);
            holder.ivIconMail.setImageResource(R.drawable.ic_mail);
        }else {
            holder.tvTitle.setTypeface(null,Typeface.NORMAL);
            holder.ivIconMail.setImageResource(R.drawable.ic_mail_open);
        }
    }

    public void addItem(MailObject dataObj, int index) {
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