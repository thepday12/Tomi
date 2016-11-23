package com.neu.tomi.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.adapter.DailyPaperAdapter;

public class DailyActivity extends FragmentActivity {

    private TextView tvTypeDailyLogin, tvTypeInbox, tvTypeOurMerchants, tvTypeAbout;
    private ViewPager viewPager;
    private int CURRENT_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenHeight = (int) (metrics.heightPixels * 0.85);
        getWindow().setLayout(screenWidth, screenHeight);
        init();
    }

    private void init() {
        tvTypeDailyLogin = (TextView) findViewById(R.id.tvTypeDailyLogin);
        tvTypeInbox = (TextView) findViewById(R.id.tvTypeInbox);
        tvTypeOurMerchants = (TextView) findViewById(R.id.tvTypeOurMerchants);
        tvTypeAbout = (TextView) findViewById(R.id.tvTypeAbout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);//Số trang trái phải giữ lại cache
        DailyPaperAdapter adapter = new DailyPaperAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        selectTag(CURRENT_POSITION);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTag(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvTypeDailyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWithTab(0);

            }
        });
        tvTypeInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWithTab(1);
            }
        });
        tvTypeOurMerchants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWithTab(2);
            }
        });
        tvTypeAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWithTab(3);
            }
        });

    }

    private void changeWithTab(int position) {
        selectTag(position);
        viewPager.setCurrentItem(position);
    }

    private void selectTag(int position) {
        tvTypeDailyLogin.setBackgroundResource(R.drawable.bg_daily_type);
        tvTypeInbox.setBackgroundResource(R.drawable.bg_daily_type);
        tvTypeOurMerchants.setBackgroundResource(R.drawable.bg_daily_type);
        tvTypeAbout.setBackgroundResource(R.drawable.bg_daily_type);
        switch (position) {
            case 0:
                tvTypeDailyLogin.setBackgroundResource(R.drawable.bg_daily_type_selected);
                break;
            case 1:
                tvTypeInbox.setBackgroundResource(R.drawable.bg_daily_type_selected);
                break;
            case 2:
                tvTypeOurMerchants.setBackgroundResource(R.drawable.bg_daily_type_selected);
                break;
            default:
                tvTypeAbout.setBackgroundResource(R.drawable.bg_daily_type_selected);
                break;
        }
    }
}
