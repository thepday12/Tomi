package com.neu.tomi.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.ultity.DataItems;
import com.squareup.picasso.Picasso;

public class ShowDetailImageActivity extends Activity {
    private TextView tvVisitWebsite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_image);
        tvVisitWebsite=(TextView)findViewById(R.id.tvVisitWebsite);
        RelativeLayout rlDetailPromotion = (RelativeLayout) findViewById(R.id.rlDetailImage);
        Intent intent= getIntent();

        final String redirectLink=intent.getStringExtra(DataItems.PROMOTION_REDIRECT_KEY);
        final int showUrl =intent.getIntExtra(DataItems.PROMOTION_SHOW_URL_KEY, 1);
        Log.e("REDIRECT_LINK",redirectLink);
        if(redirectLink.length()>0){
            tvVisitWebsite.setVisibility(View.VISIBLE);
            tvVisitWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent intent;
                        if(showUrl ==0){
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(redirectLink));

                        }else{
                            intent = new Intent(getBaseContext(), VisitWebsiteActivity.class);
                            intent.putExtra(DataItems.PROMOTION_REDIRECT_KEY,redirectLink);
                        }
                        startActivity(intent);
                    } catch (Exception ex) {

                    }
                }
            });

        }else{
            tvVisitWebsite.setVisibility(View.GONE);
        }

        Picasso.with(this).load(intent.getStringExtra("IMAGE_URL")).error(R.drawable.ic_image_error).into((ImageView) findViewById(R.id.ivDetailImage));
        rlDetailPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        super.onCreate(savedInstanceState);
    }
}
