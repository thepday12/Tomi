package com.neu.tomi.adapter;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.object.ItemDailyLoginObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.ItemQTY;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.SqliteHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Thep on 9/15/2015.
 */
public class DailyLoginAdapter extends BaseAdapter {
    private ProgressDialog mDialog;
    private boolean isNew=true;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ItemDailyLoginObject> mListItem;
    private ViewHoler mHoler;
    private DataItems mDataItems;
    private int currentAnimator=-1;

    public DailyLoginAdapter(Context context, List<ItemDailyLoginObject> lstMenu) {
        this.mContext = context;
        mDataItems = new DataItems(context);
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListItem = lstMenu;
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        isNew=false;
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        if (view == null) {
            view = mInflater.inflate(R.layout.row_daily_login, null);
            mHoler = new ViewHoler();
            mHoler.tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
            mHoler.tvDayConsecutive = (TextView) view.findViewById(R.id.tvDayConsecutive);
            mHoler.ivItemIcon = (ImageView) view.findViewById(R.id.ivItemIcon);
            mHoler.ivChecked = (ImageView) view.findViewById(R.id.ivChecked);
            mHoler.rlRowDailyLogin = (RelativeLayout) view.findViewById(R.id.rlRowDailyLogin);
            view.setTag(mHoler);
        } else {
            mHoler = (ViewHoler) view.getTag();
        }
        final ItemDailyLoginObject itemObject = mListItem.get(position);
        int itemId = itemObject.getId();
        if (itemId == 0) {
            mHoler.ivItemIcon.setImageResource(R.drawable.ic_coin);
        } else if (itemId < DataItems.ID_RESOURCE_MAX) {
            mHoler.ivItemIcon.setImageResource(mDataItems.getItem(itemId).getIconResouce());
        } else {
            Picasso.with(mContext).load(itemObject.getImageUrl()).into(mHoler.ivItemIcon);
        }
        mHoler.tvQuantity.setText(Global.formatNumber(itemObject.getQuantity()));
        if (itemObject.getDay() < 10) {
            mHoler.tvDayConsecutive.setText("0" + itemObject.getDay());
        } else {
            mHoler.tvDayConsecutive.setText(String.valueOf(itemObject.getDay()));
        }
        if (itemObject.getDay() % 7 == 0) {
            mHoler.tvDayConsecutive.setBackgroundResource(R.drawable.ic_day_special);
            mHoler.rlRowDailyLogin.setBackgroundResource(R.drawable.bg_row_dailylogin_special);
        }

        if (mDataItems.getConsecutiveDays() >= itemObject.getDay()) {
            mHoler.ivChecked.setVisibility(View.VISIBLE);
        }
        if(isNew) {
            if ((mDataItems.getConsecutiveDays() == (itemObject.getDay() - 1)) && mDataItems.validConsecutive()) {
                if (currentAnimator != position) {
                    currentAnimator = position;
                    final ObjectAnimator objAnim = (ObjectAnimator) AnimatorInflater.loadAnimator(mContext, R.animator.waring);
                    objAnim.setTarget(mHoler.rlRowDailyLogin);
                    objAnim.setEvaluator(new ArgbEvaluator());
                    objAnim.start();

                    final View finalView = view;
                    mHoler.rlRowDailyLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AddItem(objAnim, finalView, itemObject.getDay()).execute();

                        }
                    });
                }
            }
        }
        return view;
    }

    private class ViewHoler {
        RelativeLayout rlRowDailyLogin;
        ImageView ivItemIcon, ivChecked;
        TextView tvQuantity, tvDayConsecutive;
    }

    class AddItem extends AsyncTask<Void, Void, JSONObject> {
        private ObjectAnimator objAnim;
        private View finalView;
        private int day;

        public AddItem(ObjectAnimator objAnim, View finalView, int day) {
            this.objAnim = objAnim;
            this.finalView = finalView;
            this.day = day;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            return DataItems.DailyLoginCheckedIsValid(mContext, mDataItems.getEventIdOfDailyLogin());
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mDialog.dismiss();
            if (result != null) {
                try {
                    mDataItems.setManualConsecutiveDays(result.getInt("day"));
                    objAnim.removeAllListeners();
                    objAnim.end();
                    objAnim.cancel();
                    finalView.findViewById(R.id.ivChecked).setVisibility(View.VISIBLE);
                    if (day % 7 == 0) {
                        finalView.findViewById(R.id.tvDayConsecutive).setBackgroundResource(R.drawable.ic_day_special);
                        finalView.findViewById(R.id.rlRowDailyLogin).setBackgroundResource(R.drawable.bg_row_dailylogin_special);
                    } else {
                        finalView.findViewById(R.id.tvDayConsecutive).setBackgroundResource(R.drawable.ic_day_normal);
                        finalView.findViewById(R.id.rlRowDailyLogin).setBackgroundResource(R.drawable.bg_row_dailylogin);
                    }
                    if (result.getBoolean("state")) {

//                        mDataItems.setConsecutiveDays();
                        int qty = result.getInt("qty");
                        if (result.getInt("id") == 0) {
                            mDataItems.addPoint(qty, 0);
                            String endText = " PT";
                            if (qty > 1) {
                                endText += "s";
                            }
							//Successfully checked
                            Toast.makeText(mContext, "You get " + qty + endText, Toast.LENGTH_SHORT).show();
                        } else {

                            ItemObject itemObject = new ItemObject(result, true);
                            ItemQTY itemQTY = new ItemQTY(itemObject, qty);
                            new SaveItemToDevice(itemQTY).execute();
                        }
                    } else {
                        Toast.makeText(mContext, "You've been checked", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(mContext, "Can't connect to server", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Can't connect to server", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    class SaveItemToDevice extends AsyncTask<Void, Void, Boolean> {
        private ItemQTY item;

        public SaveItemToDevice(ItemQTY itemQTY) {
            this.item = itemQTY;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Add item..", true);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                SqliteHelper mSqliteHelper = mDataItems.getSQLiteHelper();
                if (!mSqliteHelper.isItemSave(item.getItemObject().getId())) {
                    //insert item to db
                    mSqliteHelper.insertItemFromShop(item.getItemObject());
                    Global.saveIconItem(mSqliteHelper, item.getItemObject().getIconUrl(), item.getItemObject().getId());
                    String itemType = item.getItemObject().getActionId();
                    if (itemType.equals(Global.TYPE_TOMI_THEME) || itemType.equals(Global.TYPE_TOMI_ACTION_EAT) || itemType.equals(Global.TYPE_TOMI_ACTION_DAILY)) {
                        for (String s : item.getItemObject().getImageList()) {
                            Global.saveImageItem(s, item.getItemObject().getId());
                        }
                    }
                }
                String itemType = item.getItemObject().getActionId();
                if (itemType.equals(Global.TYPE_TOMI_BAG)) {
                    mDataItems.addItemToList(DataItems.BAG_ITEM_KEY, item.getItemObject().getId(), item.getQty());
                } else if (itemType.equals(Global.TYPE_TOMI_THEME)) {
                    mDataItems.addItemToList(DataItems.THEME_ITEM_KEY, item.getItemObject().getId(), item.getQty());
                } else if (itemType.equals(Global.TYPE_TOMI_ACTION_EAT)) {
                    mDataItems.addItemToList(DataItems.EAT_ITEM_KEY, item.getItemObject().getId(), item.getQty());
                } else {
                    mDataItems.addItemToList(DataItems.DAILY_ITEM_KEY, item.getItemObject().getId(), item.getQty());
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mDialog.dismiss();
            if (aBoolean) {
                Toast.makeText(mContext, "You get +" + item.getQty() + " " + item.getItemObject().getName(), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aBoolean);
        }
    }
}
