package com.neu.tomi.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.adapter.ShopAdapter;
import com.neu.tomi.model.ShopModel;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.SqliteHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ShopDialog extends Activity implements Observer, View.OnClickListener {

    private ImageButton btCloseShop;
    private GridView gvShopItem;
    private TextView tvTypeFood, tvTypeBag, tvTypeDailyAction, tvTypeTheme, tvVoucher, tvLoadDataShopFail, tvBalance;
    private ProgressBar pbShopProgress;
    private List<ItemObject> itemObjects;
    private String TYPE_SELECTED = Global.TYPE_TOMI_ACTION_EAT;
    private static boolean isStart = false;
    private int screenWidth;
    private DataItems mDataItems;
    private ProgressDialog mDialog;
    private Context mContext;
    private SqliteHelper mSqliteHelper;
    private ShopModel shopModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shop);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenheight = (int) (metrics.heightPixels * 0.85);
        getWindow().setLayout(screenWidth, screenheight);
        init();
        isStart = true;
    }


    @Override
    protected void onResume() {
        if (isStart) {
            tvBalance.setText(new DataItems(mContext).getBalancePoint());
            setListItemShop(TYPE_SELECTED);
        }
        super.onResume();
    }

    private void init() {
        mContext = ShopDialog.this;
        mSqliteHelper = SqliteHelper.getInstanceSQLiteHelper(mContext);
        shopModel = new ShopModel();
        shopModel.addObserver(ShopDialog.this);
        mDataItems = new DataItems(mContext);
        btCloseShop = (ImageButton) findViewById(R.id.btCloseShop);
        gvShopItem = (GridView) findViewById(R.id.gvShopItem);
        tvTypeFood = (TextView) findViewById(R.id.tvTypeFood);
        tvTypeBag = (TextView) findViewById(R.id.tvTypeBag);
        tvTypeDailyAction = (TextView) findViewById(R.id.tvTypeDailyAction);
        tvTypeTheme = (TextView) findViewById(R.id.tvTypeTheme);
        tvVoucher = (TextView) findViewById(R.id.tvVoucher);
        tvLoadDataShopFail = (TextView) findViewById(R.id.tvLoadDataShopFail);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        pbShopProgress = (ProgressBar) findViewById(R.id.pbShopProgress);

        new LoadShopData().execute();

        tvTypeFood.setOnClickListener(this);
        tvTypeBag.setOnClickListener(this);
        tvTypeTheme.setOnClickListener(this);
        tvTypeDailyAction.setOnClickListener(this);
        tvVoucher.setOnClickListener(this);

        btCloseShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE_SELECTED = Global.TYPE_TOMI_ACTION_EAT;
                finish();
            }
        });

        gvShopItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemObject itemObject = (ItemObject) parent.getAdapter().getItem(position);
                if(itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_VOUCHER)&&itemObject.getLimit()==0){
                    Toast.makeText(ShopDialog.this, "This voucher has been fully redeemed", Toast.LENGTH_SHORT).show();
                }else {
                    showDialogConfirm(itemObject);
                }
            }


        });
    }

    private void showDialogConfirm(final ItemObject itemObject) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_shop_confirm);
        dialog.setCanceledOnTouchOutside(false);
        // set the custom dialog components - text, image and button
        Button btBuyItem = (Button) dialog.findViewById(R.id.btBuyItem);
        Button btCloseConfirm = (Button) dialog.findViewById(R.id.btCloseConfirm);
        TextView tvItemName = (TextView) dialog.findViewById(R.id.tvItemName);
        TextView tvItemDescription = (TextView) dialog.findViewById(R.id.tvItemDescription);
        TextView tvUse = (TextView) dialog.findViewById(R.id.tvUse);
        TextView tvItemXP = (TextView) dialog.findViewById(R.id.tvItemXP);
        LinearLayout llXpPoint = (LinearLayout) dialog.findViewById(R.id.llXpPoint);
        TextView tvItemPts = (TextView) dialog.findViewById(R.id.tvItemPts);
        TextView tvMaxAction = (TextView) dialog.findViewById(R.id.tvMaxAction);
        ImageView ivItemIcon = (ImageView) dialog.findViewById(R.id.ivItemIcon);
        dialog.getWindow().setLayout(screenWidth, WindowManager.LayoutParams.WRAP_CONTENT);
        if (itemObject.getActionId().equals(Global.TYPE_TOMI_THEME)) {
            String themeList = mDataItems.getTheme();
            if (!themeList.isEmpty()) {
                String[] themes = themeList.split(";");
                int itemId = itemObject.getId();

                for (String item : themes) {
                    if (itemId == Integer.valueOf(item)) {
                        tvUse.setVisibility(View.VISIBLE);
                        tvUse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Global.isChangeTheme = true;
                                Global.idThemeItem = itemObject.getId();
                                dialog.dismiss();
                                finish();
                            }
                        });
                        break;
                    }
                }
            }
        }

        if (itemObject.isResource()) {
            ivItemIcon.setImageResource(itemObject.getIconResouce());
        } else {
            Picasso.with(mContext).load(DataItems.uriIconItem(itemObject, mContext)).into(ivItemIcon);
        }
        tvItemName.setText(itemObject.getName());
        tvItemDescription.setText(itemObject.getDescription());
        if (itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_DAILY)) {
            tvMaxAction.setText("Max action: " + itemObject.getMaxAction());
        } else if (itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_EAT)) {
            tvMaxAction.setText("Max action: 1");
        } else {
            tvMaxAction.setVisibility(View.GONE);
        }
        int pts = itemObject.getPoint();
        int xp = itemObject.getPointType();
        if(xp+pts>0) {
            llXpPoint.setVisibility(View.VISIBLE);
            if (pts > 0)
                tvItemPts.setText(String.valueOf(pts));
            if (xp > 0)
                tvItemXP.setText(String.valueOf(xp));
        }else{
            llXpPoint.setVisibility(View.INVISIBLE);
        }

        btBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataItems.getIntBalancePoint() >= itemObject.getPrice()) {
                    List<ItemObject> itemObjects = new ArrayList<ItemObject>();
                    if (itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_DAILY))
                        itemObjects = mDataItems.getDailyItemList();
                    else if (itemObject.getActionId().equals(Global.TYPE_TOMI_BAG))
                        itemObjects = mDataItems.getBagItemList();
                    else {
                        try {
                            String items[] = mDataItems.getTheme().split(";");
                            for (String item : items) {
                                itemObjects.add(mDataItems.getItem(Integer.valueOf(item)));
                            }
                        } catch (Exception e) {
                        }
                    }
                    for (ItemObject item : itemObjects) {
                        if (itemObject.getId() == item.getId()) {
                            Toast.makeText(mContext, "Item exist!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    new BuyItem(itemObject).execute();
                } else {
                    Toast.makeText(mContext, "Not enough points!!", Toast.LENGTH_SHORT).show();
                }
//            }
            }
        });
        btCloseConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTypeBag:
                tvTypeBag.setBackgroundResource(R.drawable.bg_shop_type_selected);
                tvTypeTheme.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeFood.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeDailyAction.setBackgroundResource(R.drawable.bg_shop_type);
                tvVoucher.setBackgroundResource(R.drawable.bg_shop_type);
                TYPE_SELECTED = Global.TYPE_TOMI_BAG;
                break;
            case R.id.tvTypeFood:
                tvTypeBag.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeTheme.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeFood.setBackgroundResource(R.drawable.bg_shop_type_selected);
                tvTypeDailyAction.setBackgroundResource(R.drawable.bg_shop_type);
                TYPE_SELECTED = Global.TYPE_TOMI_ACTION_EAT;
                tvVoucher.setBackgroundResource(R.drawable.bg_shop_type);
                break;
            case R.id.tvTypeTheme:
                tvTypeBag.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeTheme.setBackgroundResource(R.drawable.bg_shop_type_selected);
                tvTypeFood.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeDailyAction.setBackgroundResource(R.drawable.bg_shop_type);
                tvVoucher.setBackgroundResource(R.drawable.bg_shop_type);
                TYPE_SELECTED = Global.TYPE_TOMI_THEME;
                break;
            case R.id.tvTypeDailyAction:
                tvTypeBag.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeTheme.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeFood.setBackgroundResource(R.drawable.bg_shop_type);
                tvVoucher.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeDailyAction.setBackgroundResource(R.drawable.bg_shop_type_selected);
                TYPE_SELECTED = Global.TYPE_TOMI_ACTION_DAILY;
                break;
            case R.id.tvVoucher:
                tvTypeBag.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeTheme.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeFood.setBackgroundResource(R.drawable.bg_shop_type);
                tvTypeDailyAction.setBackgroundResource(R.drawable.bg_shop_type);
                tvVoucher.setBackgroundResource(R.drawable.bg_shop_type_selected);
                TYPE_SELECTED = Global.TYPE_TOMI_ACTION_VOUCHER;
                break;
        }
        setListItemShop(TYPE_SELECTED);
    }

    private void setListItemShop(String type) {
        List<ItemObject> items = new ArrayList<>();
        if (itemObjects != null) {
            for (ItemObject itemObject : itemObjects) {
                if (itemObject.getActionId().equals(type)) {
                    items.add(itemObject);
                }
            }
        }
        gvShopItem.setAdapter(new ShopAdapter(mContext, items));
    }

    @Override
    public void update(Observable observable, Object data) {
        boolean isChange = (boolean) data;
        if (isChange) {
            tvBalance.setText(new DataItems(mContext).getBalancePoint());
            setListItemShop(TYPE_SELECTED);
        }
    }

    class LoadShopData extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            pbShopProgress.setVisibility(View.VISIBLE);
            tvLoadDataShopFail.setVisibility(View.GONE);
            itemObjects = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.getShopData(mContext);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            pbShopProgress.setVisibility(View.GONE);
            if (jsonObject != null) {
                DataItems dataItems = new DataItems(mContext);
                dataItems.clearActionList();
                dataItems.clearUseItemList();
                try {
                    if (jsonObject.getBoolean("state")) {
                        dataItems.setPoint(jsonObject.getInt("point_xp"), jsonObject.getInt("xp"), jsonObject.getInt("sub_point"));
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                itemObjects.add(new ItemObject(jsonArray.getJSONObject(i)));
                            }
                            tvBalance.setText(dataItems.getBalancePoint());
                            setListItemShop(TYPE_SELECTED);
                        } else {
                            tvLoadDataShopFail.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvLoadDataShopFail.setVisibility(View.VISIBLE);
                }
            } else {
                tvLoadDataShopFail.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(jsonObject);
        }
    }

    class BuyItem extends AsyncTask<Void, Void, JSONObject> {

        private ItemObject mItemObject;
        private int mPosition;

        public BuyItem(ItemObject itemObject) {
            mItemObject = itemObject;
            for (int i = 0; i < itemObjects.size(); i++) {
                if (itemObject.getId() == itemObjects.get(i).getId()) {
                    mPosition = i;
                    break;
                }
            }

        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.BuyItem(mContext, mItemObject.getId());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    String errDescription  =jsonObject.getString("error_description");
                    if (jsonObject.getBoolean("state")) {
//                        mDataItems.addPoint(mItemObject.getPrice() * -1, 0);
                        mDataItems.setPoint(0,jsonObject.getInt("XP"),jsonObject.getInt("point")*-1);
                        int limit = jsonObject.getInt("limited");
                        mItemObject.setLimit(limit);
                        itemObjects.set(mPosition, mItemObject);
                        setListItemShop(TYPE_SELECTED);

                        if (mItemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_VOUCHER)) {
                            PromtionObject item = new PromtionObject(jsonObject.getJSONObject("promotions"));
                            new SavePromotion(item,errDescription).execute();
                        } else {
                            if (!mItemObject.isResource()) {
                                mItemObject.setScript(jsonObject.getString("script_list"));
                                mItemObject.setTextScript(jsonObject.getString("script_text"));
                                JSONArray jsonArray = jsonObject.getJSONArray("image_list");
                                List<String> imageList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    imageList.add(jsonArray.getString(i));
                                }
                                mItemObject.setImageList(imageList);
                            }
                            new DownloadResourceItem(errDescription).execute(mItemObject);
                        }

                    } else {
                        mDialog.dismiss();
                        Toast.makeText(mContext, errDescription, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    mDialog.dismiss();
                    Toast.makeText(mContext, "Connect to server failed!", Toast.LENGTH_LONG).show();
                }
            } else {
                mDialog.dismiss();
                Toast.makeText(mContext, "Connect to server failed!", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class DownloadResourceItem extends AsyncTask<ItemObject, Void, Boolean> {

        private String errDescription = "";
        public DownloadResourceItem(String errDescription){
            this.errDescription=errDescription;
        }
        @Override
        protected Boolean doInBackground(ItemObject... params) {
            ItemObject itemObject = params[0];
            boolean isExist = mSqliteHelper.isItemSave(itemObject.getId());

            String itemType = itemObject.getActionId();
            if (itemType.equals(Global.TYPE_TOMI_BAG)) {
                mDataItems.addItemToList(DataItems.BAG_ITEM_KEY, itemObject.getId());
            } else if (itemType.equals(Global.TYPE_TOMI_THEME)) {
                mDataItems.addItemToList(DataItems.THEME_ITEM_KEY, itemObject.getId());
            } else if (itemType.equals(Global.TYPE_TOMI_ACTION_EAT)) {
                mDataItems.addItemToList(DataItems.EAT_ITEM_KEY, itemObject.getId());
            } else {
                mDataItems.addItemToList(DataItems.DAILY_ITEM_KEY, itemObject.getId());
            }


            if (!isExist) {
                mSqliteHelper.insertItemFromShop(itemObject);
                //icon
                if (Global.saveIconItem(mSqliteHelper, itemObject.getIconUrl(), itemObject.getId())) {
                    if (itemType.equals(Global.TYPE_TOMI_THEME) || itemType.equals(Global.TYPE_TOMI_ACTION_EAT) || itemType.equals(Global.TYPE_TOMI_ACTION_DAILY)) {
                        for (String item : itemObject.getImageList()) {

                            if (Global.saveImageItem(item, itemObject.getId())) continue;
                            else {
                                return false;
                            }
                        }
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            mDialog.dismiss();
            Toast.makeText(mContext, errDescription, Toast.LENGTH_SHORT).show();
            shopModel.changeInfoShop();
            super.onPostExecute(aVoid);
        }
    }

    class SavePromotion extends AsyncTask<Void, Void, Boolean> {
        private PromtionObject mPromtionObject;
private String errDescription;

        public SavePromotion(PromtionObject promtionObject,String errDescription) {
            mPromtionObject = promtionObject;
            this.errDescription=errDescription;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Global.buyPromotion(mPromtionObject, mSqliteHelper);

        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            mDialog.dismiss();
            if (aVoid) {
                Toast.makeText(mContext, errDescription, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Save file error!", Toast.LENGTH_SHORT).show();
            }
            shopModel.changeInfoShop();
            super.onPostExecute(aVoid);
        }
    }
}
