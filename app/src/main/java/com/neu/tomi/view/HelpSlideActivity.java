package com.neu.tomi.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.adapter.HelpSlideAdapter;
import com.neu.tomi.ultity.DataItems;

/**
 * Created by Thep on 12/1/2015.
 */
public class HelpSlideActivity extends FragmentActivity {
    private ViewPager vpSlideHelp;
    private TextView tvStep;
    private ImageButton btClose, ivPrevIntro, ivNextIntro;
    private int currentPosition = 0;
    private boolean isClose = false;
    public static Bitmap BGBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_help_slide);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        vpSlideHelp = (ViewPager) findViewById(R.id.vpSlideHelp);
        tvStep = (TextView) findViewById(R.id.tvStep);
        btClose = (ImageButton) findViewById(R.id.btClose);
        ivPrevIntro = (ImageButton) findViewById(R.id.btPrevIntro);
        ivNextIntro = (ImageButton) findViewById(R.id.btNextIntro);

        final int size=10;
        final HelpSlideAdapter helpSlideAdapter = new HelpSlideAdapter(HelpSlideActivity.this, getSupportFragmentManager(), size);
        vpSlideHelp.setAdapter(helpSlideAdapter);
        tvStep.setText(1 + "/" + size);
//        vpSlideHelp.setOffscreenPageLimit(size);
        vpSlideHelp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvStep.setText((position + 1) + "/" + size);
                currentPosition = position;
                updateNextAndPrevButton(size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataItems dataItems = new DataItems(HelpSlideActivity.this);

                if (!dataItems.isHelpFirst()) {
                    if (isClose) {
                        finish();
                    } else {
                        isClose = true;
                        Toast.makeText(HelpSlideActivity.this, "Place widget on home screen first\nClick close again to exit", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    finish();
                }

            }
        });

        ivNextIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPosition = currentPosition + 1;
                if (newPosition < size)
                    vpSlideHelp.setCurrentItem(newPosition);
            }
        });

        ivPrevIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPosition = currentPosition - 1;
                if (newPosition >= 0)
                    vpSlideHelp.setCurrentItem(newPosition);
            }
        });
    }


    private void updateNextAndPrevButton(int size) {
        int position = currentPosition;
        if ((position - 1) < 0) {
            ivPrevIntro.setVisibility(View.GONE);
            ivNextIntro.setVisibility(View.VISIBLE);
        } else {
            if (position + 1 < size) {
                ivPrevIntro.setVisibility(View.VISIBLE);
                ivNextIntro.setVisibility(View.VISIBLE);
            } else {
                ivPrevIntro.setVisibility(View.VISIBLE);
                ivNextIntro.setVisibility(View.GONE);
            }
        }
    }
}
