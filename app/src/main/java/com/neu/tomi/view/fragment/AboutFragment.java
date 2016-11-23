package com.neu.tomi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neu.tomi.R;


/**
 * Created by Administrator on 02/02/2016.
 */
public class AboutFragment extends Fragment {

    private TextView tvCity;

    public static final AboutFragment newInstance() {
        AboutFragment homeMostFragment = new AboutFragment();
        return homeMostFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        TextView tvTitle= (TextView) rootView.findViewById(R.id.tvTitle);

        tvTitle.setText(Html.fromHtml(String.format(getString(R.string.about_title))));
        return rootView;
    }


}