package com.neu.tomi.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.adapter.OurMerchantsAdapter;
import com.neu.tomi.object.OurMerchants;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.RecyclerItemClickListener;
import com.neu.tomi.view.VisitWebsiteActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 02/02/2016.
 */
public class OurMerchantsFragment extends Fragment {
    private RecyclerView rvMerchants;
    private TextView tvNodata;
    private ProgressBar pbGetOurMerchantsProgress;
    private Context mContext;
    List<OurMerchants> ourMerchantses;
    private Dialog dialog;
    public static final OurMerchantsFragment newInstance() {
        OurMerchantsFragment homeMostFragment = new OurMerchantsFragment();
        return homeMostFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_our_merchants, container, false);
        mContext=rootView.getContext();
       init(rootView);
        return rootView;
    }
    private void init(View rootView) {
        rvMerchants = (RecyclerView) rootView.findViewById(R.id.rvMerchants);
        tvNodata = (TextView) rootView.findViewById(R.id.tvNoData);
        pbGetOurMerchantsProgress = (ProgressBar) rootView.findViewById(R.id.pbGetOurMerchantsProgress);
        rvMerchants.setHasFixedSize(true);
        rvMerchants.setLayoutManager(new LinearLayoutManager(mContext));
        new GetOurMerchants().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class GetOurMerchants extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            ourMerchantses=new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            return DataItems.getOurMerchants(mContext);
        }

        @Override
        protected void onPostExecute(JSONArray jsonElements) {
            pbGetOurMerchantsProgress.setVisibility(View.GONE);
            if(jsonElements!=null){
                if(jsonElements.length()>0){

                    for(int i=0;i<jsonElements.length();i++){
                        try {
                            ourMerchantses.add(new OurMerchants(jsonElements.getJSONObject(i)));
                        } catch (JSONException e) {
                        }
                    }

                    rvMerchants.setAdapter(new OurMerchantsAdapter(ourMerchantses));
                    rvMerchants.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    try {

                                        dialogDetail( ourMerchantses.get(position));
                                    } catch (Exception ex) {
                                    }
                                }
                            })
                    );
                }else{
                    tvNodata.setVisibility(View.VISIBLE);
                }
            }else {
                tvNodata.setText(getString(R.string.error_null_value));
                tvNodata.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(jsonElements);
        }
    }

    private void dialogDetail(final OurMerchants ourMerchants) {
        boolean isReady = true;
        if (dialog != null) {
            if (dialog.isShowing()) {
                isReady = false;
            }
        }
        if (isReady) {
            dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_detail_merchants);

            // set the custom dialog components - text, image and button
            ImageView ivLogo = (ImageView) dialog.findViewById(R.id.ivLogo);
            final ProgressBar pbImageProgress = (ProgressBar) dialog.findViewById(R.id.pbImageProgress);
            TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
            TextView tvDescription = (TextView) dialog.findViewById(R.id.tvDescription);
            Button btOk = (Button) dialog.findViewById(R.id.btOk);
            Button btHomePage = (Button) dialog.findViewById(R.id.btHomePage);

            Picasso.with(getContext())
                    .load(ourMerchants.getImageLink())
                    .into(ivLogo,new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            pbImageProgress.setVisibility(View.GONE);
                            super.onSuccess();
                        }
                    });
            tvName.setText(ourMerchants.getName());
            tvDescription.setText(ourMerchants.getDescription());

            btHomePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url =ourMerchants.getUrl();
                    if(!url.isEmpty()) {
                        Intent intent = new Intent(getContext(), VisitWebsiteActivity.class);
                        intent.putExtra(DataItems.PROMOTION_REDIRECT_KEY,url );
                        intent.putExtra(DataItems.STATUS_REDIRECT_KEY,true);
                        startActivity(intent);
                    }
                }
            });
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }
}