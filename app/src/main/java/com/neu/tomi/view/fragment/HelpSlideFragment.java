package com.neu.tomi.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.view.HelpSlideActivity;

public class HelpSlideFragment extends Fragment {

    private ImageView ivSlideHelp, ivTreasureBox, ivC1, ivC2, ivC3, ivC4, ivC5, ivC6, ivC7, ivC8, ivC9, ivC10;
    private TextView tvOverlay;
    private RelativeLayout rlMainHelp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static HelpSlideFragment newInstance(int position) {
        HelpSlideFragment helpSlideFragment = new HelpSlideFragment();
        Bundle args = new Bundle();
        args.putInt("POSITION", position);
        helpSlideFragment.setArguments(args);
        return helpSlideFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help_slide, container, false);
        rlMainHelp = (RelativeLayout) rootView.findViewById(R.id.rlMainHelp);
        ivC1 = (ImageView) rootView.findViewById(R.id.ivC1);
        ivC2 = (ImageView) rootView.findViewById(R.id.ivC2);
        ivC3 = (ImageView) rootView.findViewById(R.id.ivC3);
        ivC4 = (ImageView) rootView.findViewById(R.id.ivC4);
        ivC5 = (ImageView) rootView.findViewById(R.id.ivC5);
        ivC6 = (ImageView) rootView.findViewById(R.id.ivC6);
        ivC7 = (ImageView) rootView.findViewById(R.id.ivC7);
        ivC8 = (ImageView) rootView.findViewById(R.id.ivC8);
        ivC9 = (ImageView) rootView.findViewById(R.id.ivC9);
        ivC10 = (ImageView) rootView.findViewById(R.id.ivC10);
        tvOverlay = (TextView) rootView.findViewById(R.id.tvOverlay);
        ivSlideHelp = (ImageView) rootView.findViewById(R.id.ivSlideHelp);

        int position = getArguments().getInt("POSITION");
        setSelected(position);
        createView(position, rootView);
        ivSlideHelp.setImageBitmap(HelpSlideActivity.BGBitmap);
        return rootView;
    }

    private void loadImageHelpUseWidget(int position) {
        tvOverlay.setVisibility(View.GONE);
        ivSlideHelp.setScaleType(ImageView.ScaleType.FIT_XY);
        switch (position) {
            case 6:
                ivSlideHelp.setImageResource(R.drawable.ic_slide1);
                break;
            case 7:
                ivSlideHelp.setImageResource(R.drawable.ic_slide2);
                break;
            case 8:
                ivSlideHelp.setImageResource(R.drawable.ic_slide3);
                break;
            case 9:
                ivSlideHelp.setImageResource(R.drawable.ic_slide4);
                break;
        }

    }

    private void setSelected(int position) {

        switch (position) {
            case 0:
                ivC1.setImageResource(R.drawable.hx);
                break;
            case 1:
                ivC2.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 2:
                ivC3.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 3:
                ivC4.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 4:
                ivC5.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 5:
                ivC6.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 6:
                ivC7.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 7:
                ivC8.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 8:
                ivC9.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
            case 9:
                ivC10.setImageResource(R.drawable.hx);
                ivC1.setImageResource(R.drawable.ho);
                break;
        }
    }

    private void createView(int position, View rootView) {

        switch (position) {
            case 0:
                rootView.findViewById(R.id.tvHelp1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tvTalk1).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivMonkey1).setVisibility(View.VISIBLE);
                break;
            case 1:
                rootView.findViewById(R.id.tvHelp2).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget2).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM2).setVisibility(View.VISIBLE);
                break;
            case 2:
                rootView.findViewById(R.id.tvHelp3).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget3).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM3).setVisibility(View.VISIBLE);
                break;
            case 3:
                TextView tvHelp4 = (TextView) rootView.findViewById(R.id.tvHelp4);
                tvHelp4.setText(Html.fromHtml("The <b>Crab</b> stores information and uses its giant claws to bring all the important news to you! Be careful though…"));
                tvHelp4.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget4).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM4).setVisibility(View.VISIBLE);
                break;
            case 4:
                rootView.findViewById(R.id.tvHelp5_0).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget5_0).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM5_0).setVisibility(View.VISIBLE);
                break;
            case 5:
                TextView tvHelp5 = (TextView) rootView.findViewById(R.id.tvHelp5);
                tvHelp5.setText(Html.fromHtml("Visit the <b>Starfish</b> store to get more goodies for TOMI! Keep calm and shop!"));
                tvHelp5.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget5).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM5).setVisibility(View.VISIBLE);
                break;
            case 6:
                rootView.findViewById(R.id.tvHelp6).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivTarget6).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivM6).setVisibility(View.VISIBLE);
                break;
            case 7:
                rootView.findViewById(R.id.tvHelp7).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivMonkey7).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivThink7).setVisibility(View.VISIBLE);
                break;
            case 8:
                rootView.findViewById(R.id.tvHelp8).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivMonkey8).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivThink8).setVisibility(View.VISIBLE);
                break;
            case 9:
                rootView.findViewById(R.id.tvHelp9B).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ivMonkey9).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tvHelp9T).setVisibility(View.VISIBLE);
                break;
        }
    }
}

