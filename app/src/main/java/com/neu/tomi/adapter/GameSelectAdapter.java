package com.neu.tomi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.GameSelectObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thep on 9/15/2015.
 */
public class GameSelectAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<GameSelectObject> mListItem;
    private ViewHoler mHoler;

    public GameSelectAdapter(Context context, List<GameSelectObject> lstMenu) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListItem = lstMenu;
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        if (view == null) {
            view = mInflater.inflate(R.layout.row_game_select, null);
            mHoler = new ViewHoler();
            mHoler.tvGameName = (TextView) view.findViewById(R.id.tvGameName);
            mHoler.ivGameIcon = (ImageView) view.findViewById(R.id.ivGameIcon);
            view.setTag(mHoler);
        } else {
            mHoler = (ViewHoler) view.getTag();
        }
        final GameSelectObject gameSelectObject = mListItem.get(position);

        Picasso.with(mContext).load(gameSelectObject.getIcon()).into(mHoler.ivGameIcon);
        mHoler.tvGameName.setText(gameSelectObject.getName());


        return view;
    }

    private class ViewHoler {
        ImageView ivGameIcon;
        TextView tvGameName;
    }

}
