package com.neu.tomi.view.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.ultity.Global;

public class UnlockDialog extends Activity {
    private LinearLayout llUnlockLayout;
    private ImageView ivItemUnlock;
    private TextView tvItemUnlock,tvDescriptionUnlock;
    private Button btOkUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenheight = (int) (metrics.heightPixels * 0.85);
        getWindow().setLayout(screenWidth, screenheight);
        setContentView(R.layout.dialog_unlock);
        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int color = Color.parseColor("#FFA000");
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{color, color});
            gd.setCornerRadius(Global.convertDpToPixel(16, UnlockDialog.this));
            llUnlockLayout.setBackground(gd);
        }


    }


    private void init() {
        Intent intent=getIntent();
        String textUnlock="";
        try {
         textUnlock = intent.getStringExtra("TEXT_UNLOCK");
        }catch (Exception e){

        }
        String textDescription="";
        try {
            textDescription = getString(intent.getIntExtra("TEXT_DESCRIPTION", 0));
        }catch (Exception e){

        }

        llUnlockLayout = (LinearLayout) findViewById(R.id.llUnlockLayout);
        ivItemUnlock = (ImageView) findViewById(R.id.ivItemUnlock);
        tvItemUnlock = (TextView) findViewById(R.id.tvItemUnlock);
        btOkUnlock = (Button) findViewById(R.id.btOkUnlock);
        tvDescriptionUnlock = (TextView) findViewById(R.id.tvDescriptionUnlock);

        if(textUnlock==null){
            int imageResource =  intent.getIntExtra("IMAGE_ITEM", 0);
            ivItemUnlock.setImageResource(imageResource);
        }else {
            ivItemUnlock.setVisibility(View.GONE);
            tvItemUnlock.setVisibility(View.VISIBLE);
            tvItemUnlock = (TextView) findViewById(R.id.tvItemUnlock);
            tvItemUnlock.setText(textUnlock);
        }
        tvDescriptionUnlock.setText(textDescription);

        btOkUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

