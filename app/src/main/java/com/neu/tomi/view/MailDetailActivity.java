package com.neu.tomi.view;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.adapter.PagerMailAdapter;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.ultity.SqliteHelper;

import java.util.List;

/**
 * Created by Thep on 12/1/2015.
 */
public class MailDetailActivity extends FragmentActivity {
    private ViewPager vpMail;
    private TextView tvCountMail;
    private ImageButton btClose;
    private List<MailObject> mMailObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenheight = (int) (metrics.heightPixels * 0.5);
        setContentView(R.layout.activity_detail_mail);
        getWindow().setLayout(screenWidth, screenheight);
        init();
        super.onCreate(savedInstanceState);
    }

    private void init() {
        final String mailId = getIntent().getStringExtra("MAIL_ID");
        vpMail = (ViewPager) findViewById(R.id.vpMail);
        tvCountMail = (TextView) findViewById(R.id.tvCountMail);
        btClose = (ImageButton) findViewById(R.id.btClose);

        final SqliteHelper sqliteHelper =SqliteHelper.getInstanceSQLiteHelper(MailDetailActivity.this);
        mMailObjects = sqliteHelper.getAllMail();
        PagerMailAdapter pagerMailAdapter = new PagerMailAdapter(MailDetailActivity.this, getSupportFragmentManager(), mMailObjects);
        vpMail.setAdapter(pagerMailAdapter);
        int position = 0;
        final int size = mMailObjects.size();
        for (int i = 1; i < size; i++) {
            if (mailId.equals(mMailObjects.get(i).getId())) {
                position = i;
                break;
            }
        }
        clearNotification(mailId);
        sqliteHelper.updateStateMessage(mailId);
        vpMail.setCurrentItem(position);
        tvCountMail.setText((position + 1) + "/" + size);
        vpMail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String id  = mMailObjects.get(position).getId();
                clearNotification(id);
                sqliteHelper.updateStateMessage(id);
                tvCountMail.setText((position + 1) + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void clearNotification(String mailId) {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.valueOf(mailId));
        } catch (Exception ex) {

        }
    }
}
