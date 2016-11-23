package com.neu.tomi.view.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.ItemQTY;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.SqliteHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 02/02/2016.
 */
public class DailyLoginFragment extends Fragment {

    private TextView tvError, tvGetGift;
    private ImageView ivDay1Checked, ivDay2Checked, ivDay3Checked, ivDay4Checked, ivDay5Checked;
    private ProgressBar pbLoginDaily;
    private LinearLayout llFrameCheck;
    private Button btCollect;
    private DataItems mDataItems;
    //dialog
    private Dialog dialog;
    TextView mTextView;
    private ImageView ivGift1, ivGift2, ivGift3, ivGift4, ivGift5, ivGift6;
    private JSONObject mResult;
    private boolean isSelected = false;

    public static final DailyLoginFragment newInstance(String name) {
        DailyLoginFragment homeMostFragment = new DailyLoginFragment();
        return homeMostFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dailylogin, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        Context context = rootView.getContext();
        mDataItems = new DataItems(context);

        ivDay1Checked = (ImageView) rootView.findViewById(R.id.ivDay1Checked);
        ivDay2Checked = (ImageView) rootView.findViewById(R.id.ivDay2Checked);
        ivDay3Checked = (ImageView) rootView.findViewById(R.id.ivDay3Checked);
        ivDay4Checked = (ImageView) rootView.findViewById(R.id.ivDay4Checked);
        ivDay5Checked = (ImageView) rootView.findViewById(R.id.ivDay5Checked);
        tvError = (TextView) rootView.findViewById(R.id.tvError);

        tvGetGift = (TextView) rootView.findViewById(R.id.tvGetGift);
        pbLoginDaily = (ProgressBar) rootView.findViewById(R.id.pbLoginDaily);
        llFrameCheck = (LinearLayout) rootView.findViewById(R.id.llFrameCheck);
        btCollect = (Button) rootView.findViewById(R.id.btCollect);

        //comingson start
        pbLoginDaily.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText("Daily Login is \ncoming soon!");

//        if (!mDataItems.getLastDayShowDailyLogin().equals(Global.getDate())) {
//            new CheckDailyLogin(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//            setDayLoginView(mDataItems.getConsecutiveDays());
//        }
//end
    }

    private void setDayLoginView(int days) {
        mDataItems.setManualConsecutiveDays(days);
        llFrameCheck.setVisibility(View.VISIBLE);
        switch (days) {
            case 1:
                ivDay1Checked.setVisibility(View.VISIBLE);
                ivDay2Checked.setVisibility(View.GONE);
                ivDay3Checked.setVisibility(View.GONE);
                ivDay4Checked.setVisibility(View.GONE);
                ivDay5Checked.setVisibility(View.GONE);
                break;
            case 2:
                ivDay1Checked.setVisibility(View.VISIBLE);
                ivDay2Checked.setVisibility(View.VISIBLE);
                ivDay3Checked.setVisibility(View.GONE);
                ivDay4Checked.setVisibility(View.GONE);
                ivDay5Checked.setVisibility(View.GONE);
                break;
            case 3:
                ivDay1Checked.setVisibility(View.VISIBLE);
                ivDay2Checked.setVisibility(View.VISIBLE);
                ivDay3Checked.setVisibility(View.VISIBLE);
                ivDay4Checked.setVisibility(View.GONE);
                ivDay5Checked.setVisibility(View.GONE);
                break;
            case 4:
                ivDay1Checked.setVisibility(View.VISIBLE);
                ivDay2Checked.setVisibility(View.VISIBLE);
                ivDay3Checked.setVisibility(View.VISIBLE);
                ivDay4Checked.setVisibility(View.VISIBLE);
                ivDay5Checked.setVisibility(View.GONE);

                break;
            case 5:
                ivDay1Checked.setVisibility(View.VISIBLE);
                ivDay2Checked.setVisibility(View.VISIBLE);
                ivDay3Checked.setVisibility(View.VISIBLE);
                ivDay4Checked.setVisibility(View.VISIBLE);
                ivDay5Checked.setVisibility(View.VISIBLE);

                break;
            default:
                ivDay1Checked.setVisibility(View.GONE);
                ivDay2Checked.setVisibility(View.GONE);
                ivDay3Checked.setVisibility(View.GONE);
                ivDay4Checked.setVisibility(View.GONE);
                ivDay5Checked.setVisibility(View.GONE);
                break;
        }
        setDayGetGift(days);
    }

    private void setDayGetGift(int days) {

        if (days < 5) {
            tvGetGift.setVisibility(View.VISIBLE);
            btCollect.setVisibility(View.GONE);
        } else {
            tvGetGift.setVisibility(View.GONE);
            btCollect.setVisibility(View.VISIBLE);
            btCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialogGetGift();
                }
            });
        }
    }

    private void showDialogGetGift() {
        new CollectBonus(getContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void dialogCollect() {
        setDayLoginView(0);
        boolean isReady = true;
        if (dialog != null) {
            if (dialog.isShowing()) {
                isReady = false;
            }
        }
        if (isReady) {
            isSelected = false;
            dialog = new Dialog(getContext());
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_get_gift);

            // set the custom dialog components - text, image and button
            mTextView = (TextView) dialog.findViewById(R.id.tvAutoOpen);
            ivGift1 = (ImageView) dialog.findViewById(R.id.ivGift1);
            ivGift2 = (ImageView) dialog.findViewById(R.id.ivGift2);
            ivGift3 = (ImageView) dialog.findViewById(R.id.ivGift3);
            ivGift4 = (ImageView) dialog.findViewById(R.id.ivGift4);
            ivGift5 = (ImageView) dialog.findViewById(R.id.ivGift5);
            ivGift6 = (ImageView) dialog.findViewById(R.id.ivGift6);
            TimeOut timeOut = new TimeOut();
            timeOut.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            ivGift1.setOnClickListener(myClick(getContext(), timeOut, 0));
            ivGift2.setOnClickListener(myClick(getContext(), timeOut, 1));
            ivGift3.setOnClickListener(myClick(getContext(), timeOut, 2));
            ivGift4.setOnClickListener(myClick(getContext(), timeOut, 3));
            ivGift5.setOnClickListener(myClick(getContext(), timeOut, 4));
            ivGift6.setOnClickListener(myClick(getContext(), timeOut, 5));

            dialog.show();
        }
    }

    private View.OnClickListener myClick(final Context context, final AsyncTask timeOut, final int position) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected) {
                    isSelected = true;
                    timeOut.cancel(true);

                    showIconGift(context, position);

                }
            }
        };
        return onClickListener;
    }

    private void showIconGift(final Context context, final int position) {
        switch (position) {
            case 0:
                ivGift1.setBackgroundResource(R.drawable.button_gift_selected);
                break;
            case 1:
                ivGift2.setBackgroundResource(R.drawable.button_gift_selected);
                break;
            case 2:
                ivGift3.setBackgroundResource(R.drawable.button_gift_selected);
                break;
            case 3:
                ivGift4.setBackgroundResource(R.drawable.button_gift_selected);
                break;
            case 4:
                ivGift5.setBackgroundResource(R.drawable.button_gift_selected);
                break;
            default:
                ivGift6.setBackgroundResource(R.drawable.button_gift_selected);
                break;
        }
        mTextView.setVisibility(View.GONE);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                showGift(context, position);
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void showGift(Context context, int position) {
        if (mResult != null) {
            try {
                JSONArray images = mResult.getJSONArray("images");
                Picasso.with(context).load(images.getString(0)).into(ivGift1);
                Picasso.with(context).load(images.getString(1)).into(ivGift2);
                Picasso.with(context).load(images.getString(2)).into(ivGift3);
                Picasso.with(context).load(images.getString(3)).into(ivGift4);
                Picasso.with(context).load(images.getString(4)).into(ivGift5);
                Picasso.with(context).load(images.getString(5)).into(ivGift6);
                showMessage(context, position);
            } catch (JSONException e) {
            }

        }

//        dialog.dismiss();
    }

    private void showMessage(final Context context, final int position) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dialogMessage(context, position);
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void dialogMessage(Context context, int position) {
        final Dialog dialogMessage = new Dialog(context);
        dialogMessage.setCanceledOnTouchOutside(false);
        dialogMessage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMessage.setContentView(R.layout.dialog_daily_collect);

        // set the custom dialog components - text, image and button
        TextView tvDescription = (TextView) dialogMessage.findViewById(R.id.tvDescription);
        ImageView ivItemUnlock = (ImageView) dialogMessage.findViewById(R.id.ivItemUnlock);
        Button btOk = (Button) dialogMessage.findViewById(R.id.btOk);

        String text = "";
        int point = 0, xp = 0;
        JSONArray items = null;
        //,,
        try {
            point = mResult.getInt("point");
            xp = mResult.getInt("XP");
            items = mResult.getJSONArray("items");
            mDataItems.addPoint(point, xp);
            List<ItemQTY> itemQTYList = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                JSONObject object = items.getJSONObject(i);
                ItemObject itemObject = new ItemObject(object, true);
                itemQTYList.add(new ItemQTY(itemObject, object.getInt("qty")));
            }
            new DownloadItem(itemQTYList, context).execute();
        } catch (JSONException e) {
        }
        if (point > 0) {
            text += "+" + point + "PTS";
        }
        if (xp > 0) {
            if (text.isEmpty())
                text += "+" + xp + "XP";
            else
                text += ", +" + xp + "XP";
        }
        if (items != null) {
            if (items.length() > 0) {
                try {
                    int i = 0;
                    if (text.isEmpty()) {

                        JSONObject object = items.getJSONObject(i);
                        int qty = object.getInt("qty");
                        if (qty > 1)
                            text += "+" + qty + " " + object.getString("name") + "s";
                        else
                            text += "+" + qty + " " + object.getString("name");
                        i = 1;
                    }
                    for (; i < items.length(); i++) {
                        JSONObject object = items.getJSONObject(i);
                        int qty = object.getInt("qty");
                        if (qty > 1)
                            text += ", +" + qty + " " + object.getString("name") + "s";
                        else
                            text += ", +" + qty + " " + object.getString("name");
                        i = 1;
                    }
                } catch (JSONException e) {

                }
            }
        }

        tvDescription.setText(text);
        try {
            Picasso.with(context).load(mResult.getJSONArray("images").getString(position)).into(ivItemUnlock);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                dialog.dismiss();
            }
        });

        dialogMessage.show();
    }

    class CheckDailyLogin extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;

        public CheckDailyLogin(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            pbLoginDaily.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.checkDailyLogin(mContext);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            pbLoginDaily.setVisibility(View.GONE);
            if (jsonObject != null) {
                mDataItems.setLastDayShowDailyLogin();
                try {
                    if (jsonObject.getBoolean("state")) {
                        setDayLoginView(jsonObject.getInt("day"));
                    } else {
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText(jsonObject.getString("error_description"));
                    }
                } catch (JSONException e) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.error_null_value));
                }
            } else {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(getString(R.string.error_null_value));
            }
            super.onPostExecute(jsonObject);
        }
    }

    class CollectBonus extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;

        public CollectBonus(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            pbLoginDaily.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.collectBonusDailyLogin(mContext);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            pbLoginDaily.setVisibility(View.GONE);
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
                        int day = jsonObject.getInt("day");
                        if (day < 5) {
                            Toast.makeText(mContext, "Please collect bonus later", Toast.LENGTH_SHORT).show();
                            setDayLoginView(day);
                        } else {
                            setDayLoginView(0);
                            mResult = jsonObject;
                            dialogCollect();
                        }
                    } else {
                        Toast.makeText(mContext, jsonObject.getString("error_description"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, getString(R.string.error_null_value), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, getString(R.string.error_null_value), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class TimeOut extends AsyncTask<Void, Integer, Void> {
        private int maxTime = 10;


        @Override
        protected void onPreExecute() {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText("Auto open after " + maxTime + " seconds");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (maxTime > 0) {
                try {
                    Thread.sleep(1000);
                    maxTime--;
                    publishProgress(maxTime);
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (maxTime > 1)
                mTextView.setText("Auto open after " + maxTime + " seconds");
            else
                mTextView.setText("Auto open after " + maxTime + " second");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            endDialog(mTextView);
            int randomGift = new Random().nextInt(6);

            showIconGift(getContext(), randomGift);
            super.onPostExecute(aVoid);
        }


        @Override
        protected void onCancelled(Void aVoid) {
            endDialog(mTextView);
            super.onCancelled(aVoid);
        }
    }

    private void endDialog(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    class DownloadItem extends AsyncTask<Void, Void, Void> {
        private List<ItemQTY> mItemQTyList;
        private Context mContext;
        ProgressDialog mDialog;

        public DownloadItem(List<ItemQTY> itemQTy, Context context) {
            this.mItemQTyList = itemQTy;
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Get item..", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (ItemQTY item : mItemQTyList) {
                //add to list item
                mDataItems.addItemForGrab(item);
                SqliteHelper mSqliteHelper = mDataItems.getSQLiteHelper();
                String itemType = item.getItemObject().getActionId();
                if (itemType.equals(Global.TYPE_TOMI_ACTION_DAILY)) {
                    mDataItems.updateDailyDate(item.getItemObject().getId());
                    mDataItems.updateDailyAction(item.getItemObject().getId(), item.getQty());
                }
                if (mSqliteHelper.isItemSave(item.getItemObject().getId())) {
                    continue;
                } else {
                    //insert item to db
                    mSqliteHelper.insertItemFromShop(item.getItemObject());
                    Global.saveIconItem(mSqliteHelper, item.getItemObject().getIconUrl(), item.getItemObject().getId());
                    if (itemType.equals(Global.TYPE_TOMI_THEME) || itemType.equals(Global.TYPE_TOMI_ACTION_EAT) || itemType.equals(Global.TYPE_TOMI_ACTION_DAILY)) {
                        for (String s : item.getItemObject().getImageList()) {
                            Global.saveImageItem(s, item.getItemObject().getId());
                        }
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}