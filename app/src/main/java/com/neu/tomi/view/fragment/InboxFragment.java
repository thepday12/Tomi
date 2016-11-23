package com.neu.tomi.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.adapter.MailAdapter;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.ultity.RecyclerItemClickListener;
import com.neu.tomi.ultity.SqliteHelper;
import com.neu.tomi.view.MailDetailActivity;

import java.util.List;


/**
 * Created by Administrator on 02/02/2016.
 */
public class InboxFragment extends Fragment {

    private RecyclerView rvMail;
    private TextView tvNoData;
    private boolean isResume = false;
    private Context mContext;

    public static final InboxFragment newInstance() {
        InboxFragment homeMostFragment = new InboxFragment();
        return homeMostFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_mail, container, false);
        mContext = rootView.getContext();
        init(rootView);
        return rootView;
    }

    @Override
    public void onPause() {
        isResume = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        if (isResume) {
            final SqliteHelper sqliteHelper = new SqliteHelper(mContext);
            final List<MailObject> mailObjects = sqliteHelper.getAllMail();
            rvMail.setAdapter(new MailAdapter(mailObjects));
        }
        super.onResume();
    }

    private void init(View rootView) {
        rvMail = (RecyclerView) rootView.findViewById(R.id.rvMail);
        tvNoData = (TextView) rootView.findViewById(R.id.tvNoData);

        rvMail.setHasFixedSize(true);
        rvMail.setLayoutManager(new LinearLayoutManager(mContext));
        final SqliteHelper sqliteHelper = new SqliteHelper(mContext);
        final List<MailObject> mailObjects = sqliteHelper.getAllMail();
        if (mailObjects.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            rvMail.setAdapter(new MailAdapter(mailObjects));
            rvMail.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            try {
                                Intent intent = new Intent(mContext, MailDetailActivity.class);
                                intent.putExtra("MAIL_ID", mailObjects.get(position).getId());
                                startActivity(intent);
                            } catch (Exception ex) {
                            }
                        }
                    })
            );
        }else{
            tvNoData.setVisibility(View.VISIBLE);
        }
    }


}