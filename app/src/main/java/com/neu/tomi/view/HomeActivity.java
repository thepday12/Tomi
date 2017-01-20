package com.neu.tomi.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.neu.tomi.R;
import com.neu.tomi.adapter.BeaconAdapter;
import com.neu.tomi.adapter.ItemBagAdapter;
import com.neu.tomi.adapter.ItemBonusAdapter;
import com.neu.tomi.adapter.ItemDailyAdapter;
import com.neu.tomi.adapter.ItemFoodAdapter;
import com.neu.tomi.gcm.RegistrationIntentService;
import com.neu.tomi.object.BeaconObject;
import com.neu.tomi.object.BonusItemObject;
import com.neu.tomi.object.FoodObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.ItemQTY;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.HttpRequest;
import com.neu.tomi.ultity.RecyclerItemClickListener;
import com.neu.tomi.ultity.SqliteHelper;
import com.neu.tomi.view.dialog.InfoDialog;
import com.neu.tomi.view.dialog.ProfileDialog;
import com.neu.tomi.view.dialog.PromotionDialog;
import com.neu.tomi.view.dialog.ShopDialog;
import com.neu.tomi.widget.TomiProvider;
import com.neu.tomi.widget.TomiService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class HomeActivity extends Activity {

    private RecyclerView rvBags, rvEat, rvDaily, rvBeaconMiss;
    private ImageView ivBag, ivOkButton, ivTreasureBox, ivMonkeyProfile, ivMissPromotion, ivShop, ivMailBox, ivGame, ivShowSlideHelp, ivIsCheckDailyLogin;
    private TextView tvPoint, tvResult, tvNewMail;
    private DataItems mDataItems;
    private RelativeLayout rlMissBeacon, rlBgTheme, rlMailBox, rlDaily;
    private ImageButton btCloseMissBeacon;
    private ProgressBar pbGetListBeacon;
    private List<BeaconObject> beaconObjects;
    private int EXTRA_APPWIDGET_ID = -1;
    private String EXTRA_CURENT_ACTION = "", EXTRA_BEACON_INFO = "";
    private boolean EXTRA_EXPIRE_ACTION = false,EXTRA_IS_LOGIN=false;
    private static int itemIdForBag;
    private static boolean isStart = false;
    private boolean isStop = false;
    private ProgressDialog mDialog;
    private SqliteHelper mSqliteHelper;
    private AlertDialog.Builder alertDialogBuilder = null;
    private AlertDialog alertDialog = null;

    private int maxSlideHelp = 7;
    private int currentSlideHelp = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Global.isLayoutHomeActive = true;
//        Global.clearNotificationDetectBeacon(HomeActivity.this);


        setContentView(R.layout.activity_home);


        mDataItems = new DataItems(HomeActivity.this);
        mSqliteHelper = mDataItems.getSQLiteHelper();
        beaconObjects = new ArrayList<>();
        getExtra();
        if (EXTRA_EXPIRE_ACTION) {
            clearNotificationExpire();
            showGrab();
        }
        init();

        if (mDataItems.isLoadFirstRun()) {
            if (!mDataItems.getFirstShow()) {
                showDialogFirstRun();
            }
        }
        if (!mDataItems.isFirstSetup()) {
            if (HttpRequest.isOnline(HomeActivity.this)) {
                new SetupFirstTime().execute();
            }
        }
        if (Global.isBeaconDetected) {
            EXTRA_BEACON_INFO = Global.lastBeaconInfo;
        }
        if (EXTRA_BEACON_INFO != null && !EXTRA_BEACON_INFO.isEmpty()) {
            if (HttpRequest.isOnline(HomeActivity.this)) {
                Intent intent = new Intent(HomeActivity.this, PromotionDialog.class);
                intent.putExtra("TREAT", false);
                intent.putExtra("WID", EXTRA_APPWIDGET_ID);
                intent.putExtra("INFO", EXTRA_BEACON_INFO);
                startActivity(intent);
            } else {
                HttpRequest.showDialogInternetSetting(HomeActivity.this);
                new CheckConnect().execute();
            }
        }


        if (mDataItems.isLostData()) {
                showDialogSyncData();
        }
        isStart = true;
        //-update
//        if (HttpRequest.isOnline(HomeActivity.this)) {
//            if (!mDataItems.getLastDayShowDailyLogin().equals(Global.getDate())) {
//                new ShowDailyLogin().execute();
//            }
//        }
//        checkIsDailyLoginChecked();
        rlDaily.setVisibility(View.GONE);
//        showSlideHelp();
//        restoreImagePromotions();
        showDialogInfo();
    }

    /***
     * TRuong hop du dieu kien nhung chua nhap thong tin
     */
    private void showDialogInfo(){
        if(!mDataItems.isUpdateInfo()) {
            Intent i = new Intent(HomeActivity.this, InfoDialog.class);
            startActivity(i);
        }
    }
    private void showDialogSyncData() {
        if (alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

            // Setting Dialog Title
            alertDialogBuilder.setTitle("Restore");
            // Setting Dialog Message
            alertDialogBuilder.setMessage("Restore old data of Tomi.\nIt will usually take a few minutes");
            alertDialogBuilder.setPositiveButton("Restore now",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (HttpRequest.isOnline(HomeActivity.this)) {
                                new RestoreData().execute();
                                dialog.cancel();
                            } else {
                                Toast.makeText(HomeActivity.this, "Please! Turn on internet connection and restart app", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
            alertDialogBuilder.setNeutralButton("Never show again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDataItems.setServerDetectLostData(false);
                    mDataItems.deleteFileCheck();
                    dialog.cancel();
                }
            });
            // Setting Negative "NO" Button
            alertDialogBuilder.setNegativeButton("Later",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
//        alertDialog.show();
            alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
            alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            alertDialog = alertDialogBuilder.create();
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void reload() {
        List<FoodObject> foodList = getListFoodItem();
        rvEat.setAdapter(new ItemFoodAdapter(foodList));
        rvBags.setAdapter(new ItemBagAdapter(getListBagItem()));
        rvDaily.setAdapter(new ItemDailyAdapter(getListDailyItem()));
        rvBeaconMiss.setAdapter(new BeaconAdapter(beaconObjects));
        tvPoint.setText(mDataItems.getBalancePoint() + " pts");
        checkIsDailyLoginChecked();
    }

    private void checkIsDailyLoginChecked() {
        if (!mDataItems.getLastDateConsecutive().equals(mDataItems.getCurrentDate()))
            ivIsCheckDailyLogin.setVisibility(View.VISIBLE);
        else
            ivIsCheckDailyLogin.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        Global.isLayoutHomeActive = true;
        super.onResume();
        if (isStart) {
            reload();
            if (Global.isChangeTheme) {
                if (mDataItems.getItem(Global.idThemeItem).isResource()) {
                    changeThemeResource();
                } else {
                    new ChangeTheme().execute();
                }
                Global.isChangeTheme = false;
            }
            tvPoint.setText(mDataItems.getBalancePoint() + " pts");
//            if (mDataItems.isLostData()) {
//                showDialogSyncData();
//            }
        }
//        if (isStop) {
//            isStop = false;
//            if (Global.isBeaconDetected) {
//                if (HttpRequest.isOnline(HomeActivity.this)) {
//                    Intent intent = new Intent(HomeActivity.this, PromotionDialog.class);
//                    intent.putExtra("TREAT", false);
//                    intent.putExtra("WID", EXTRA_APPWIDGET_ID);
//                    intent.putExtra("INFO", Global.lastBeaconInfo);
//                    startActivity(intent);
//                    startActivity(intent);
//                } else {
//                    HttpRequest.showDialogInternetSetting(HomeActivity.this);
//                    new CheckConnect().execute();
//                }
//            }
//        }
    }

    private void changeThemeResource() {
        switch (Global.idThemeItem) {
            case DataItems.themeId1:
                rlBgTheme.setBackgroundResource(R.drawable.bg);
                ivMissPromotion.setImageResource(R.drawable.ic_spade);
                ivMailBox.setImageResource(R.drawable.ic_crab);
                ivShop.setImageResource(R.drawable.ic_properties);
                ivOkButton.setImageResource(R.drawable.ic_shell);
                ivTreasureBox.setImageResource(R.drawable.treasurebox);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStop = true;
        Global.isLayoutHomeActive = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.isLayoutHomeActive = false;
//        mDataItems.removeBeaconWithID(EXTRA_BEACON_INFO);
        Context context = getBaseContext();
        if (Global.serviceNotRun(context)) {
            Intent mIntent = new Intent(context, TomiService.class);
            context.startService(mIntent);
        }
    }

    private void startDialogHelp() {
//        Intent intent = new Intent(HomeActivity.this, HelpSlideActivity.class);
//        startActivity(intent);

        showHelpTarget();
    }

    private void showSlideHelp() {
        if (!mDataItems.isHelpFirst()&&!mDataItems.isLostData()) {
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    startDialogHelp();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {

                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }else{
            if (!mDataItems.getLastDayShowDailyLogin().equals(Global.getDate())) {
//                startDailyActivity();
            }
        }
    }

    private void init() {
        long timeBonus = mDataItems.getLastTimeAddBananaBonus();
        if (timeBonus == 0) {
            mDataItems.setLastTimeAddBananaBonus(System.currentTimeMillis());
        } else if (timeBonus > 0) {
            mDataItems.addBananaBonus();
        }
        ivBag = (ImageView) findViewById(R.id.ivBag);
        ivTreasureBox = (ImageView) findViewById(R.id.ivTreasureBox);
        ivShowSlideHelp = (ImageView) findViewById(R.id.ivShowSlideHelp);
        ivIsCheckDailyLogin = (ImageView) findViewById(R.id.ivIsCheckDailyLogin);
        ivOkButton = (ImageView) findViewById(R.id.ivOkButton);
        ivMonkeyProfile = (ImageView) findViewById(R.id.ivMonkeyProfile);
        ivMissPromotion = (ImageView) findViewById(R.id.ivMissPromotion);
        ivMailBox = (ImageView) findViewById(R.id.ivMailBox);
        ivGame = (ImageView) findViewById(R.id.ivGame);
        ivShop = (ImageView) findViewById(R.id.ivShop);
        rvBags = (RecyclerView) findViewById(R.id.rvBags);
        rvDaily = (RecyclerView) findViewById(R.id.rvDaily);
        rvEat = (RecyclerView) findViewById(R.id.rvEat);
        rvBeaconMiss = (RecyclerView) findViewById(R.id.rvBeaconMiss);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvNewMail = (TextView) findViewById(R.id.tvNewMail);
        rlMissBeacon = (RelativeLayout) findViewById(R.id.rlMissBeacon);
        rlMailBox = (RelativeLayout) findViewById(R.id.rlMailBox);
        rlDaily = (RelativeLayout) findViewById(R.id.rlDaily);
        rlBgTheme = (RelativeLayout) findViewById(R.id.rlBgTheme);
        btCloseMissBeacon = (ImageButton) findViewById(R.id.btCloseMissBeacon);
        pbGetListBeacon = (ProgressBar) findViewById(R.id.pbGetListBeacon);

        itemIdForBag = mDataItems.getBagWidget(EXTRA_APPWIDGET_ID);
        tvPoint.setText(mDataItems.getBalancePoint() + " pts");
        ItemObject.setImage(getBaseContext(), itemIdForBag, ivBag);

        loadNewMail();

        ivShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpRequest.isOnline(HomeActivity.this)) {
                    Intent intent = new Intent(HomeActivity.this, ShopDialog.class);
                    startActivity(intent);
                } else {
                    HttpRequest.showDialogInternetSetting(HomeActivity.this);
                }
            }
        });

        btCloseMissBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlMissBeacon.setVisibility(View.GONE);
                tvResult.setVisibility(View.GONE);
                pbGetListBeacon.setVisibility(View.VISIBLE);
            }
        });
        ivMissPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beaconInfo = mDataItems.getListBeaconDetect();
//                String beaconInfo = "28788_888";
                if (beaconInfo.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "You did not miss anything", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(HomeActivity.this, PromotionDialog.class);
                    intent.putExtra("TREAT", false);
                    intent.putExtra("MISS", true);
                    intent.putExtra("WID", EXTRA_APPWIDGET_ID);
                    intent.putExtra("INFO", beaconInfo);
                    startActivity(intent);
                }
            }
        });

        rlMailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, MailActivity.class);

                tvNewMail.setVisibility(View.GONE);
               startDailyActivity();
            }
        });

        ivGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameSelectActivity.class);
                startActivity(intent);
            }
        });
        ivTreasureBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGrab();
            }
        });

        ivShowSlideHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogHelp();
            }
        });


        ivMonkeyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileDialog.class);
                intent.putExtra("WID", EXTRA_APPWIDGET_ID);
                startActivityForResult(intent, 34);
//                Intent intent= new Intent(HomeActivity.this, PromotionDialog.class);
//                intent.putExtra("TREAT",false);
//                intent.putExtra("WID",EXTRA_APPWIDGET_ID);
//                intent.putExtra("INFO",EXTRA_BEACON_INFO);
//                startActivity(intent);
            }
        });

        ivOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        rvBags.setHasFixedSize(true);
        rvEat.setHasFixedSize(true);
        rvDaily.setHasFixedSize(true);
        rvBeaconMiss.setHasFixedSize(true);

        rvBags.setLayoutManager(new LinearLayoutManager(this));
        rvBeaconMiss.setLayoutManager(new LinearLayoutManager(this));
        rvEat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDaily.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setDefaultAdapter();
//        showSlideHelp();


        rvBags.addOnItemTouchListener(new RecyclerItemClickListener(HomeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            ItemObject itemObject = getListBagItem().get(position);
                            itemIdForBag = itemObject.getId();
                            if (itemObject.isResource()) {
                                ivBag.setImageResource(itemObject.getIconResouce());
                            } else {
                                Picasso.with(HomeActivity.this).load(itemObject.getIconUrl()).into(ivBag);
                            }
                            setBagWidget();
                        } catch (Exception ex) {
                        }
                    }
                })
        );
        rvEat.addOnItemTouchListener(new RecyclerItemClickListener(HomeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            FoodObject foodObject = getListFoodItem().get(position);
                            ItemObject itemObject = foodObject.getItemObject();
                            if (itemObject.getId() == DataItems.bananasId && foodObject.getQty() >= 10) {
                                mDataItems.setLastTimeAddBananaBonus(System.currentTimeMillis());
                            }

                            monkeyEat(itemObject);
                        } catch (Exception ex) {
                        }
                    }
                })
        );
        rvDaily.addOnItemTouchListener(new RecyclerItemClickListener(HomeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            ItemObject itemObject = getListDailyItem().get(position);
                            monkeyActionDaily(itemObject);
                        } catch (Exception ex) {

                        }
                    }
                })
        );
        Log.e("RESPONSE_SERVER",mDataItems.isRegisterGCM()+""+mDataItems.getKeyGCM());

        if(!mDataItems.getUserId().isEmpty()&&!mDataItems.isRegisterGCM())
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intentRegisterGCM = new Intent(HomeActivity.this, RegistrationIntentService.class);
                startService(intentRegisterGCM);
            }
    }

    private void startDailyActivity() {
        Intent intent = new Intent(HomeActivity.this, DailyActivity.class);
        startActivity(intent);
    }

    private void loadNewMail() {

        List<MailObject> mailObjects = mSqliteHelper.getAllMailNoCheck();
        int sizeMail = mailObjects.size();
        if (sizeMail > 0) {
            tvNewMail.setVisibility(View.VISIBLE);
            tvNewMail.setText(formatMail(sizeMail));
        }
    }

    private String formatMail(int sizeMail) {
        if (sizeMail > 9)
            return "9+";
        return String.valueOf(sizeMail);
    }


    private void getExtra() {
        Intent currentIntent = getIntent();
        EXTRA_APPWIDGET_ID = currentIntent.getIntExtra("EXTRA_APPWIDGET_ID", -1);
        EXTRA_BEACON_INFO = currentIntent.getStringExtra("EXTRA_BEACON_INFO");
        EXTRA_CURENT_ACTION = currentIntent.getStringExtra("EXTRA_CURENT_ACTION");
        EXTRA_EXPIRE_ACTION = currentIntent.getBooleanExtra("EXPIRE", false);
        EXTRA_IS_LOGIN = currentIntent.getBooleanExtra("IS_LOGIN", false);
        if(EXTRA_IS_LOGIN){
            new RestoreData().execute();
        }
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            EXTRA_APPWIDGET_ID = extras.getInt("EXTRA_APPWIDGET_ID");
//            EXTRA_BEACON_INFO = extras.getString("EXTRA_BEACON_INFO");
//            EXTRA_CURENT_ACTION = extras.getString("EXTRA_CURENT_ACTION");
//            EXTRA_EXPIRE_ACTION = extras.getBoolean("EXPIRE");
//        }
//        if (Global.isBeaconDetected) {
//            EXTRA_BEACON_INFO = Global.lastBeaconInfo;
//            EXTRA_CURENT_ACTION = Global.currentAction;
//
//        }


    }


    private void setDefaultAdapter() {
        rvBags.setAdapter(new ItemBagAdapter(getListBagItem()));
        rvEat.setAdapter(new ItemFoodAdapter(getListFoodItem()));
        rvDaily.setAdapter(new ItemDailyAdapter(getListDailyItem()));
    }

    private List<ItemObject> getListBagItem() {
        return mDataItems.getBagItemList();
    }

    private List<FoodObject> getListFoodItem() {
        return mDataItems.getFoodItemList();
    }

    private List<ItemObject> getListDailyItem() {
        return mDataItems.getDailyItemList();
    }

    private void setBagWidget() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.main);
        ItemObject itemObject = mDataItems.getItem(itemIdForBag);
        if (itemObject.isResource()) {
            remoteViews.setImageViewResource(R.id.imgBag, itemObject.getIconResouce());
        } else {

            Uri uri = Uri.parse(itemObject.getIconUrl());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                remoteViews.setImageViewBitmap(R.id.imgBag, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(HomeActivity.this);
        mDataItems.setLastBagSelect(itemObject.getId());
        if (EXTRA_APPWIDGET_ID > 0) {
            appWidgetManager.updateAppWidget(EXTRA_APPWIDGET_ID, remoteViews);
            mDataItems.setBagWidget(EXTRA_APPWIDGET_ID, itemObject.getId());
        } else {
            int[] listId= Global.getListWidgetId(HomeActivity.this);
            if (listId != null) {
                if (listId.length > 0) {
                    appWidgetManager.updateAppWidget(listId[0], remoteViews);
                    mDataItems.setBagWidget(listId[0], itemObject.getId());
                }
            }
        }
    }

    private void monkeyEat(ItemObject itemObject) {

        eatItem(itemObject);


    }

    public void eatItem(ItemObject itemObject) {

        int itemId = itemObject.getId();
        FoodObject foodObject = mSqliteHelper.getFoodItem(itemId);
        if (foodObject.getQty() > 0) {
            mSqliteHelper.updateFood(itemId, -1);
            mDataItems.updatePointUseItem(itemObject);
            Intent intent = new Intent(HomeActivity.this, TomiProvider.class);
            intent.putExtra("EXTRA_ITEM_ID", itemId);
            intent.putExtra("EXTRA_APPWIDGET_ID", EXTRA_APPWIDGET_ID);
            intent.putExtra("EXTRA_IS_RESOURCE", itemObject.isResource());
            intent.putExtra("EXTRA_TEXT_SCRIPT", itemObject.getTextScript());
            intent.putExtra("EXTRA_SCRIPT", itemObject.getScript());
            intent.setAction(itemObject.getActionId());
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(HomeActivity.this, "Can't use item", Toast.LENGTH_SHORT).show();
        }
    }

    private void monkeyActionDaily(ItemObject itemObject) {
        if (itemObject.getMaxAction() > mDataItems.getActionDaily(itemObject.getId())) {
            mDataItems.updateDailyAction(itemObject.getId());
            mDataItems.updatePointUseItem(itemObject);
            Intent intent = new Intent(HomeActivity.this, TomiProvider.class);
            intent.putExtra("EXTRA_ITEM_ID", itemObject.getId());
            intent.putExtra("EXTRA_APPWIDGET_ID", EXTRA_APPWIDGET_ID);
            intent.putExtra("EXTRA_IS_RESOURCE", itemObject.isResource());
            intent.putExtra("EXTRA_TEXT_SCRIPT", itemObject.getTextScript());
            intent.putExtra("EXTRA_SCRIPT", itemObject.getScript());
            intent.setAction(itemObject.getActionId());
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(HomeActivity.this, "Please use the next day!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 34) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra("SIGN_OUT", false);
                if (result) {
                    finish();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void showGrab() {
        Intent intent = new Intent(HomeActivity.this, PromotionDialog.class);
        intent.putExtra("TREAT", true);
        startActivity(intent);
    }

    private void clearNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Global.NOTIFICATION_ID);
        } catch (Exception ex) {

        }
    }

    private void clearNotificationExpire() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Global.NOTIFICATION_EXPIRE_ID);
            notificationManager.cancel(Global.NOTIFICATION_LAST_LOGIN_3D);
        } catch (Exception ex) {

        }
    }


    class CheckConnect extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            int count = 0;
            while (!HttpRequest.isOnline(HomeActivity.this) && count < 60) {
                try {
                    Thread.sleep(300);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count >= 60)
                return false;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent intent = new Intent(HomeActivity.this, PromotionDialog.class);
                intent.putExtra("TREAT", false);
                intent.putExtra("WID", EXTRA_APPWIDGET_ID);
                intent.putExtra("INFO", EXTRA_BEACON_INFO);
                startActivity(intent);
            } else {
                finish();
            }
            super.onPostExecute(result);
        }
    }


    private void showDialogFirstRun() {
        try {
            JSONObject jsonObject = new JSONObject(mDataItems.getDataFirstRun());
            int point = jsonObject.getInt("point");
            JSONArray jsonArray = jsonObject.getJSONArray("item");
            List<BonusItemObject> bonusItemObjects = new ArrayList<>();
            mDataItems.addPoint(point, 0);
            mDataItems.setFirstShow();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                bonusItemObjects.add(new BonusItemObject(object));
                mDataItems.addItemToList(DataItems.EAT_ITEM_KEY, object.getInt("item_type"), object.getInt("item_qty"));
            }

            final Dialog dialog = new Dialog(HomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_first_run);
            dialog.setCanceledOnTouchOutside(false);
            // set the custom dialog components - text, image and button
            LinearLayout llFirstDialogLayout = (LinearLayout) dialog.findViewById(R.id.llFirstDialogLayout);
            Button btOkFirst = (Button) dialog.findViewById(R.id.btOkFirst);
            TextView tvPointFirst = (TextView) dialog.findViewById(R.id.tvPointFirst);
            RecyclerView rvItemBonusFirst = (RecyclerView) dialog.findViewById(R.id.rvItemBonusFirst);
            rvItemBonusFirst.setHasFixedSize(true);
            rvItemBonusFirst.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

            rvItemBonusFirst.setAdapter(new ItemBonusAdapter(bonusItemObjects));
            tvPointFirst.setText("Points: +" + point);
            btOkFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                int color = Global.getColor();
                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{color, color});
                gd.setCornerRadius(Global.convertDpToPixel(16, HomeActivity.this));
                llFirstDialogLayout.setBackground(gd);
            }
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    reload();
                }
            });
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ChangeTheme extends AsyncTask<Void, Void, List<Bitmap>> {

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(HomeActivity.this, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected List<Bitmap> doInBackground(Void... params) {
            List<Bitmap> bitmaps = new ArrayList<>();
            Uri uriBG = DataItems.getDefaultUriScript(Global.idThemeItem, "bg");
            Uri uriMiss = DataItems.getDefaultUriScript(Global.idThemeItem, "miss");
            Uri uriMail = DataItems.getDefaultUriScript(Global.idThemeItem, "mail");
            Uri uriShop = DataItems.getDefaultUriScript(Global.idThemeItem, "shop");
            Uri uriOk = DataItems.getDefaultUriScript(Global.idThemeItem, "ok");
            Uri uriTreasure = DataItems.getDefaultUriScript(Global.idThemeItem, "treasure");
            try {
                bitmaps.add(0, MediaStore.Images.Media.getBitmap(getContentResolver(), uriBG));
                bitmaps.add(1, MediaStore.Images.Media.getBitmap(getContentResolver(), uriMiss));
                bitmaps.add(2, MediaStore.Images.Media.getBitmap(getContentResolver(), uriMail));
                bitmaps.add(3, MediaStore.Images.Media.getBitmap(getContentResolver(), uriShop));
                bitmaps.add(4, MediaStore.Images.Media.getBitmap(getContentResolver(), uriOk));
                bitmaps.add(5, MediaStore.Images.Media.getBitmap(getContentResolver(), uriTreasure));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmaps;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {
            mDialog.dismiss();
            try {
                Bitmap bg = bitmaps.get(0);
                Bitmap miss = bitmaps.get(1);
                Bitmap mail = bitmaps.get(2);
                Bitmap shop = bitmaps.get(3);
                Bitmap ok = bitmaps.get(4);
                Bitmap treasure = bitmaps.get(5);
                BitmapDrawable drawableBg = new BitmapDrawable(getResources(), bg);
                rlBgTheme.setBackgroundDrawable(drawableBg);
                ivMissPromotion.setImageBitmap(miss);
                ivMailBox.setImageBitmap(mail);
                ivShop.setImageBitmap(shop);
                ivOkButton.setImageBitmap(ok);
                ivTreasureBox.setImageBitmap(treasure);
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, "Load theme error", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(bitmaps);
        }
    }

    class SetupFirstTime extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(HomeActivity.this, null,
                    "Performing first time setup..\n" +
                            "Do not turn off internet connection", true);
            mDataItems.setFirstSetup();
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            return DataItems.getPromotionsFirstTime(HomeActivity.this);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray != null) {
                if (jsonArray.length() > 0) {
                    List<PromtionObject> promtionObjects = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            promtionObjects.add(new PromtionObject(object));
                        } catch (JSONException e) {
                        }
                    }
                    new SavePromotionFirstTime(mDialog, promtionObjects).execute();
                } else {
                    mDialog.dismiss();
                }
            } else {
                mDialog.dismiss();
            }
            if(!EXTRA_IS_LOGIN)
            showSlideHelp();
            super.onPostExecute(jsonArray);
        }
    }

    class SavePromotionFirstTime extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;
        private List<PromtionObject> mPromtionObjects;

        public SavePromotionFirstTime(ProgressDialog progressDialog, List<PromtionObject> promtionObjects) {
            mDialog = progressDialog;
            mPromtionObjects = promtionObjects;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (PromtionObject promtionObject : mPromtionObjects) {
                try {
                    Global.savePromotion(promtionObject, mSqliteHelper,0,0);
                } catch (Exception e) {
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

    class RestoreData extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(HomeActivity.this, null,
                    "Download data..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.GetDataRestore(HomeActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    //update name
                    String userName = jsonObject.getString("user_name");
                    if (userName != null) {
                        if (userName.length() > 0) {
                            mDataItems.setNameOfUser(userName);
                        }
                    }
                    //update xp&pts
                    mDataItems.setPoint(0, jsonObject.getInt("xp"), jsonObject.getInt("pts") * -1);
                    //Update item
                    List<ItemQTY> itemQTYList = new ArrayList<>();
                    JSONArray itemArray = jsonObject.getJSONArray("item");
                    for (int i = 0; i < itemArray.length(); i++) {
                        JSONObject object = itemArray.getJSONObject(i);
                        ItemObject itemObject = new ItemObject(object, true);
                        itemQTYList.add(new ItemQTY(itemObject, object.getInt("qty")));
                    }
                    //Update promotion
                    List<PromtionObject> promtionObjects = new ArrayList<>();
                    JSONArray promotionArray = jsonObject.getJSONArray("promotions");
                    for (int i = 0; i < promotionArray.length(); i++) {
                        JSONObject object = promotionArray.getJSONObject(i);
                        promtionObjects.add(new PromtionObject(object));
                    }
                    new DownloadItem(itemQTYList, promtionObjects).execute();
                } catch (JSONException e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Restore failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                mDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Cannot restore because connection to the server failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class DownloadItem extends AsyncTask<Void, Void, Void> {
        private List<ItemQTY> mItemQTyList;
        private List<PromtionObject> mPromtionObjects;

        public DownloadItem(List<ItemQTY> itemQTy, List<PromtionObject> promtionObjects) {
            this.mItemQTyList = itemQTy;
            this.mPromtionObjects = promtionObjects;

        }

        @Override
        protected void onPreExecute() {
            mDialog.dismiss();
            mDialog = ProgressDialog.show(HomeActivity.this, null,
                    "Updating..", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (ItemQTY item : mItemQTyList) {
                //add to list item
                mDataItems.addItemForRestore(item);
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

            for (PromtionObject item : mPromtionObjects) {
                Global.savePromotion(item, mSqliteHelper,0,0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDialog.dismiss();
            mDataItems.clearUseItemList();
            mDataItems.setServerDetectLostData(false);
            mDataItems.deleteFileCheck();
            reload();
            super.onPostExecute(aVoid);
        }
    }

    private void sendActionListToServer(Context context) {

        if (HttpRequest.isOnline(context)) {
            if (mDataItems.checkSendActionValid()) {
                new SendActionToServer(context).execute();
            }
        }

    }

    class SendActionToServer extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;

        public SendActionToServer(Context context) {
            mContext = context;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.sendActionPost();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
//                        mDataItems.clearActionList();
//                        mDataItems.clearUseItemList();
//                        mDataItems.clearViewActionList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(HomeActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {

            return false;
        }
        return true;
    }





    private void showHelpTarget() {
//        boolean isShow = true;
//        if (showcaseView == null) {
//            isShow = false;
//        } else {
//            isShow = showcaseView.isShowing();
//        }
//        if (!isShow) {
//            nextSlideHelp();
//        }
        new LoadBitmap().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    class LoadBitmap extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return DataItems.viewToBitmap(rlBgTheme);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                HelpSlideActivity.BGBitmap = bitmap;
                Intent intent = new Intent(HomeActivity.this, HelpSlideActivity.class);
                startActivity(intent);
            } else
                Toast.makeText(HomeActivity.this, "Can't load image", Toast.LENGTH_SHORT).show();
            super.onPostExecute(bitmap);
        }


    }


}
