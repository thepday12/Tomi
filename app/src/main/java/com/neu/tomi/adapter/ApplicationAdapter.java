package com.neu.tomi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.neu.tomi.R;
import com.neu.tomi.object.ApplicationObject;

import java.util.List;

/**
 * Created by Thep on 10/23/2015.
 */
public class ApplicationAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ApplicationObject> mLstMenu;
    private ViewHoler mHoler;

    public ApplicationAdapter(Context context, List<ApplicationObject> lstMenu) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLstMenu = lstMenu;
    }

    @Override
    public int getCount() {
        return mLstMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return mLstMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        if (view == null) {
            view = mInflater.inflate(R.layout.application_row, null);
            mHoler = new ViewHoler();
            mHoler.tvApplicationName = (TextView) view.findViewById(R.id.tvApplicationName);
            mHoler.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            view.setTag(mHoler);
        } else {
            mHoler = (ViewHoler) view.getTag();
        }
        ApplicationObject object = mLstMenu.get(position);
        mHoler.ivIcon.setImageDrawable(object.getIcon());
        mHoler.tvApplicationName.setText(object.getName());
        return view;
    }

    private class ViewHoler {
        ImageView ivIcon;
        TextView tvApplicationName;
    }
}
