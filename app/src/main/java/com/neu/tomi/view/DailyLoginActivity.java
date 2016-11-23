package com.neu.tomi.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.adapter.DailyLoginAdapter;
import com.neu.tomi.object.ItemDailyLoginObject;
import com.neu.tomi.ultity.DataItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DailyLoginActivity extends Activity {
    private DataItems mDataItems;
    private List<ItemDailyLoginObject> mDailyLoginObjects;
    private GridView gvDaiLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_login);
        mDataItems= new DataItems(DailyLoginActivity.this);
        TextView tvDaily = (TextView) findViewById(R.id.tvDaily);
        gvDaiLy = (GridView) findViewById(R.id.gvDaiLy);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/font_title.TTF");
        tvDaily.setTypeface(type);
//        mDataItems.setLastDayShowDailyLogin();
        if(!mDataItems.validConsecutive()&&!mDataItems.getLastDateConsecutive().equals(mDataItems.getCurrentDate())){
            mDataItems.resetConsecutiveDays();
        }
        new LoadItems().execute();
    }

    class LoadItems extends AsyncTask<Void,Void,JSONArray>{
        private  ProgressDialog mDialog;
        @Override
        protected void onPreExecute() {
            mDailyLoginObjects=new ArrayList<>();
            mDialog = ProgressDialog.show(DailyLoginActivity.this, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            String items=mDataItems.getItemsDailyLogin();

            JSONArray jsonArray=null;
            String[] dateTime =mDataItems.getCurrentDate().split("-");
            int currentYear=Integer.valueOf(dateTime[0]);
            int currentMonth=Integer.valueOf(dateTime[1]);
            if (items.isEmpty()||mDataItems.getYearOfDailyLogin()!=currentYear||mDataItems.getMonthOfDailyLogin()!=currentMonth){
                JSONObject jsonObject=mDataItems.getDailyLoginForMonth(DailyLoginActivity.this);
                try {
                    mDataItems.setMonthOfDailyLogin(jsonObject.getInt("Month"));
                    mDataItems.setYearOfDailyLogin(jsonObject.getInt("Year"));
                    mDataItems.setEventIdOfDailyLogin(jsonObject.getInt("EventId"));
                    mDataItems.resetConsecutiveDays();
                    jsonArray=jsonObject.getJSONArray("Dailys");
                } catch (JSONException e) {

                }
            }else{
                try {
                    jsonArray= new JSONArray(items);
                } catch (JSONException e) {
                }
            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            mDialog.dismiss();
            if(jsonArray!=null){
                for(int i=0; i<jsonArray.length();i++){
                    try {
                        mDailyLoginObjects.add(new ItemDailyLoginObject(jsonArray.getJSONObject(i)));
                    } catch (JSONException e) {
                        Toast.makeText(DailyLoginActivity.this, "Can't connect to server", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }

                mDataItems.setItemsDailyLogin(jsonArray.toString());
                gvDaiLy.setAdapter(new DailyLoginAdapter(DailyLoginActivity.this,mDailyLoginObjects));
            }else{
                Toast.makeText(DailyLoginActivity.this, "Can't connect to server", Toast.LENGTH_SHORT).show();
                finish();
            }
            super.onPostExecute(jsonArray);
        }
    }
}
