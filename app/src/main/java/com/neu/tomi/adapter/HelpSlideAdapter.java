package com.neu.tomi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.neu.tomi.view.fragment.HelpSlideFragment;

/**
 * @author Vlonjat Gashi (vlonjatg)
 */
public class HelpSlideAdapter extends FragmentPagerAdapter {

    private int size;
    private Context mContext;

    public HelpSlideAdapter(Context context, FragmentManager fm, @NonNull int size) {
        super(fm);
        this.size = size;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return HelpSlideFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return size;
    }

}
