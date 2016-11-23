package com.neu.tomi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.neu.tomi.view.fragment.AboutFragment;
import com.neu.tomi.view.fragment.DailyLoginFragment;
import com.neu.tomi.view.fragment.InboxFragment;
import com.neu.tomi.view.fragment.OurMerchantsFragment;


/**
 * Created by Administrator on 02/02/2016.
 */
public class DailyPaperAdapter extends FragmentPagerAdapter {
    public DailyPaperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return InboxFragment.newInstance();
            case 2:
                return OurMerchantsFragment.newInstance();
            case 3:
                return AboutFragment.newInstance();
            default:
                return DailyLoginFragment.newInstance("0");

        }

    }

    @Override
    public int getCount() {
        return 4;
    }




}
