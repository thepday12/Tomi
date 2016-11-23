package com.neu.tomi.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.neu.tomi.R;
import com.neu.tomi.adapter.MailAdapter;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.ultity.RecyclerItemClickListener;
import com.neu.tomi.ultity.SqliteHelper;

import java.util.List;

/**
 * Created by Thep on 12/1/2015.
 */

public class MailActivity extends FragmentActivity {
    private RecyclerView rvMail;
    private Button btCloseMailList;
    private boolean isResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenheight = (int) (metrics.heightPixels * 0.85);
        setContentView(R.layout.activity_mail);
        getWindow().setLayout(screenWidth, screenheight);
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        isResume = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isResume) {
            final SqliteHelper sqliteHelper = new SqliteHelper(MailActivity.this);
            final List<MailObject> mailObjects = sqliteHelper.getAllMail();
            rvMail.setAdapter(new MailAdapter(mailObjects));
        }
        super.onResume();
    }

    private void init() {
        rvMail = (RecyclerView) findViewById(R.id.rvMail);
        btCloseMailList = (Button) findViewById(R.id.btCloseMailList);

        rvMail.setHasFixedSize(true);
        rvMail.setLayoutManager(new LinearLayoutManager(this));
        final SqliteHelper sqliteHelper = new SqliteHelper(MailActivity.this);
        final List<MailObject> mailObjects = sqliteHelper.getAllMail();
        if (mailObjects.size() > 0) {
            rvMail.setAdapter(new MailAdapter(mailObjects));
            rvMail.addOnItemTouchListener(new RecyclerItemClickListener(MailActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            try {
                                Intent intent = new Intent(MailActivity.this, MailDetailActivity.class);
                                intent.putExtra("MAIL_ID", mailObjects.get(position).getId());
                                startActivity(intent);
                            } catch (Exception ex) {
                            }
                        }
                    })
            );
        }
        btCloseMailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
