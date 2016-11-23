package com.neu.tomi.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.neu.tomi.image_slide.AppTourDialog;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.SqliteHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlonjat Gashi (vlonjatg)
 */
public class PromotionDialog extends AppTourDialog {
    private List<PromtionObject> promtionObjects;
    boolean isRestart;
    private static List<ViewPromotionObject> viewPromotionObjectList;
    private DataItems dataItems;
    public static void addViewPromotion(PromtionObject promtionObject) {
        viewPromotionObjectList.add(new ViewPromotionObject(promtionObject));
    }

    public static boolean promotionExist(String id) {
        for (ViewPromotionObject object : viewPromotionObjectList) {
            if (object.getPromotionId().equals(id))
                return true;
        }
        return false;
    }

    public void init(Bundle savedInstanceState) {
        viewPromotionObjectList = new ArrayList<>();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenheight = (int) (metrics.heightPixels * 0.85);
        getWindow().setLayout(screenWidth, screenheight);
        Intent intent = getIntent();
        boolean isTreat = intent.getBooleanExtra("TREAT", false);
        boolean isMiss = intent.getBooleanExtra("MISS", false);
        isRestart = intent.getBooleanExtra("RESTART", false);
        String beaconInfo = intent.getStringExtra("INFO");
        int wid = intent.getIntExtra("WID", 0);
        setTreat(isTreat);
         dataItems = new DataItems(PromotionDialog.this);
        if (isTreat) {
            new LoadPromotionsGrab(PromotionDialog.this).execute();
        } else {
//            dataItems.addBeaconActive(beaconInfo);
//            setDefautDetectBeacon();
                new LoadPromotionsBeacon(PromotionDialog.this, beaconInfo, wid,isMiss).execute();
        }

    }

    @Override
    protected void onDestroy() {
        int sizeSave = viewPromotionObjectList.size();
        if (sizeSave > 0) {
            String viewActionSave = viewPromotionObjectList.get(0).getSaveViewPromotionString();
            if (sizeSave > 1) {
                for (int i = 1; i < sizeSave; i++) {
                    viewActionSave += ";" + viewPromotionObjectList.get(i).getSaveViewPromotionString();
                }
            }
            dataItems.addViewActionToList(viewActionSave);
        }
        viewPromotionObjectList.clear();
        super.onDestroy();
    }

    @Override
    public void onDonePressed() {
        finish();
    }

    private void setDefautDetectBeacon() {
//        Global.isBeaconDetected=false;
//        Global.currentAction = "";
//        Intent intent = new Intent(this.getApplicationContext(), TomiProvider.class);
//        intent.setAction(Global.TOMI_ACTION_NONE);
//        sendBroadcast(intent);
    }

    class LoadPromotionsGrab extends AsyncTask<Void, Void, List<PromtionObject>> {
        private Context mContext;
        private ProgressDialog mDialog;
        private SqliteHelper mSqliteHelper;

        public LoadPromotionsGrab(Context context) {
            mContext = context;
            promtionObjects = new ArrayList<>();
            mSqliteHelper = new SqliteHelper(context);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
        }

        @Override
        protected List<PromtionObject> doInBackground(Void... params) {
            return mSqliteHelper.getAllPromotion();
        }

        @Override
        protected void onPostExecute(List<PromtionObject> promtionObjects) {


            if (promtionObjects.size() > 0) {
                for (PromtionObject item : promtionObjects) {
                    addSlide(item, Global.getColor());
                }
                setDot(promtionObjects.size());
            } else {
                finish();
                if (!isRestart)
                    Toast.makeText(mContext, "Promotion not found", Toast.LENGTH_SHORT).show();
            }
            mDialog.dismiss();
            super.onPostExecute(promtionObjects);
        }
    }

    class LoadPromotionsBeacon extends AsyncTask<Void, Void, JSONArray> {
        private Context mContext;
        private ProgressDialog mDialog;
        private String mBeaconInfo;
        private int mWid;
private boolean mIsMiss;
        public LoadPromotionsBeacon(Context context, String beaconInfo, int wid,boolean isMiss) {
            mContext = context;
            mBeaconInfo = beaconInfo;
            mWid = wid;
            mIsMiss=isMiss;
            promtionObjects = new ArrayList<>();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
//            Toast.makeText(mContext,"BEACONID: "+mBeaconInfo,Toast.LENGTH_LONG).show();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                if(mIsMiss){
                    return DataItems.getDataOfMissBeaCon(mBeaconInfo, mWid, mContext,dataItems.getSQLiteHelper());
                }else {
                    return DataItems.getDataOfBeaCon(mBeaconInfo, mWid, mContext,dataItems.getSQLiteHelper());
                }
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            mDialog.dismiss();
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    try {
                        PROMOTION_SELECTED = new PromtionObject(jsonArray.getJSONObject(0)).getPromotionId();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            PromtionObject promtionObject = new PromtionObject(jsonArray.getJSONObject(i));
                            promtionObjects.add(promtionObject);
                            addSlide(promtionObject, Global.getColor());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    setDot(jsonArray.length());
                } else {
                    finish();
                    if(mIsMiss){
                        Toast.makeText(mContext, "You did not miss anything", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, "No promotion", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                finish();
                Toast.makeText(mContext, "Connect to server failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonArray);
        }
    }

    static class ViewPromotionObject {
        public String getPromotionId() {
            return promotionId;
        }

        private String promotionId;
        private String beaconId;
        private String dateView;

        public ViewPromotionObject(PromtionObject promtionObject) {
            setPromotionId(promtionObject.getPromotionId());
            setBeaconId(promtionObject.getBeaconID());
            setDateView(Global.getTime());
        }


        public String getSaveViewPromotionString() {
            return promotionId + "_" + dateView + "_" + beaconId;
        }


        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
        }


        public void setBeaconId(String beaconId) {
            this.beaconId = beaconId;
        }


        public void setDateView(String dateView) {
            this.dateView = dateView;
        }
    }
}
