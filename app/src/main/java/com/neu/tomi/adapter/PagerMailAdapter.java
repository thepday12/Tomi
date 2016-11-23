package com.neu.tomi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.neu.tomi.view.fragment.MailFragment;
import com.neu.tomi.object.MailObject;

import java.util.List;

/**
 * @author Vlonjat Gashi (vlonjatg)
 */
public class PagerMailAdapter extends FragmentPagerAdapter {

    private final List<MailObject> mMailObjects;
    private Context mContext;

    public PagerMailAdapter(Context context, FragmentManager fm, @NonNull List<MailObject> mailObjects) {
        super(fm);
        this.mMailObjects = mailObjects;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return MailFragment.newInstance(mMailObjects.get(position));
    }

    @Override
    public int getCount() {
        return this.mMailObjects.size();
    }

}
