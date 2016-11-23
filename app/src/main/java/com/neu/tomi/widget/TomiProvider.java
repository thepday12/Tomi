package com.neu.tomi.widget;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.neu.tomi.MainActivity;
import com.neu.tomi.R;
import com.neu.tomi.gcm.RegistrationIntentService;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.HttpRequest;
import com.neu.tomi.ultity.SqliteHelper;
import com.neu.tomi.view.MailDetailActivity;
import com.neu.tomi.view.dialog.PromotionDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TomiProvider extends AppWidgetProvider {
    static final String TAG = "NeublickTomiWidget";
    static final String PNAME = TomiProvider.class.getPackage().toString();
    public static final String ACTION_CLICK = PNAME + ".ACTION_CLICK";
    public static final String EXTRA_CLICK_APPWIDGET_ID = PNAME + ".CLICK_APPWIDGET_ID";
    public static final String EXTRA_CLICK_VIEW_ID = PNAME + ".CLICK_VIEW_ID";
    AppWidgetManager _appWidgetManager;
    RemoteViews _currentViews;
    Context _currentContext;
    Handler _handler;

    Handler getHandler() {
        if (_handler == null) _handler = new Handler();
        return _handler;
    }

    RemoteViews getViews() {
        if (_currentViews == null)
            _currentViews = new RemoteViews(getContext().getPackageName(), R.layout.main);
        return _currentViews;
    }

    Context getContext() {
        return _currentContext;
    }

    void setContext(Context context) {
        _currentContext = context;
    }

    AppWidgetManager getAppWidgetManager() {
        if (_appWidgetManager == null)
            _appWidgetManager = AppWidgetManager.getInstance(getContext().getApplicationContext());
        return _appWidgetManager;
    }

    void setAppWidgetManager(AppWidgetManager appWidgetManager) {
        _appWidgetManager = appWidgetManager;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        DataItems dataItems = new DataItems(context);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            dataItems.setBagWidget(appWidgetId, DataItems.bag1Id);
        }

        super.onDeleted(context, appWidgetIds);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        setContext(context);
        setAppWidgetManager(appWidgetManager);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            le("appWidgetId=" + appWidgetId);
//            setOnClickListener(appWidgetId, R.id.widget);

            RemoteViews remoteViews = getViews();
//			remoteViews.setImageViewResource(R.id.imgBag, Global.bags[bag_index]);
            PendingIntent pendingIntent = pendingIntentForActionClick(context, appWidgetId);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
        if (Global.serviceNotRun(context)) {
            Intent mIntent = new Intent(context, TomiService.class);
            context.startService(mIntent);
        }
    }

    private PendingIntent pendingIntentForActionClick(Context context, int appWidgetId) {
        int idIntent = 101 + appWidgetId;
        //Clear
        Intent intentForClear = new Intent(context, MainActivity.class);
        intentForClear.putExtra("EXTRA_APPWIDGET_ID", appWidgetId);
        intentForClear.putExtra("EXTRA_BEACON_INFO", Global.lastBeaconInfo);
        intentForClear.putExtra("EXTRA_CURENT_ACTION", Global.currentAction);
        intentForClear.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentForClear = PendingIntent.getActivity(context, idIntent, intentForClear, PendingIntent.FLAG_CANCEL_CURRENT);
        pendingIntentForClear.cancel();
        //Run
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("EXTRA_APPWIDGET_ID", appWidgetId);
        intent.putExtra("EXTRA_BEACON_INFO", Global.lastBeaconInfo);
        intent.putExtra("EXTRA_CURENT_ACTION", Global.currentAction);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, idIntent, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sendActionListToServer(context);
        DataItems dataItems=new DataItems(context);
        if(System.currentTimeMillis()- dataItems.getLastTimeLogin()>=259200000){

            showNotificationLogin(context);
            dataItems.setLastTimeLoginTimeOut();
        }
        checkExpire(context);
//        if (dataItems.checkEventValid()) {
//            new CheckEvent(context).execute();
//        }
//        rename(context);
        try {
            String action = intent.getAction();
            setContext(context);
            if (action.equals(ACTION_CLICK)) {
                int appWidgetId = intent.getIntExtra(EXTRA_CLICK_APPWIDGET_ID, -1);
                showMainActivity(intent.getIntExtra(EXTRA_CLICK_APPWIDGET_ID, appWidgetId));
            }
            if (Global.CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {
                // Get the widget manager and ids for this widget provider, then call the shared
                // clock update method.
                if (Global.serviceNotRun(context)) {
                    Intent mIntent = new Intent(context, TomiService.class);
                    context.startService(mIntent);
                }

            } else if (action.equals(Global.TOMI_ACTION_SPEECH)) {
                updateSpeech();
            } else if (action.equals(Global.TYPE_TOMI_ACTION_EAT) || action.equals(Global.TYPE_TOMI_ACTION_DAILY)) {
                int itemId = intent.getIntExtra("EXTRA_ITEM_ID", -1);
                int appWidgetId = intent.getIntExtra("EXTRA_APPWIDGET_ID", -1);
                if (appWidgetId < 0) {
                    int[] listId= Global.getListWidgetId(context);
                    if (listId != null) {
                        if (listId.length > 0) {
                            appWidgetId = listId[0];
                        }
                    }
                }
                boolean isResource = intent.getBooleanExtra("EXTRA_IS_RESOURCE", false);
                String textScript = intent.getStringExtra("EXTRA_TEXT_SCRIPT");
                String[] scriptSpeech = textScript.split(";");
                String itemActionId = action + itemId;
                if (isResource) {
                    List<Integer> script = getScriptWithItemId(itemId);
                    startAnimationResource(appWidgetId, script, itemActionId, Global.getRandomText(getContext(), scriptSpeech));
                } else {
                    String scriptString = intent.getStringExtra("EXTRA_SCRIPT");
                    if (!scriptString.isEmpty()) {
                        List<Bitmap> script = new ArrayList<>();
                        String[] items = scriptString.split(";");
                        for (String item : items) {

                            Uri uri = DataItems.getDefaultUriScript(itemId, item);
                            if (uri != null) {
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                                    script.add(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                            }
                        }
                        startAnimation(appWidgetId, script, itemActionId, Global.getRandomText(getContext(), scriptSpeech));
                    }
                }
            }
//        else if(action.equals(Global.TOMI_ACTION_DANCE)) {
//            int appWidgetId= intent.getIntExtra("EXTRA_APPWIDGET_ID", -1);
//        	startAnimation(appWidgetId, Global.getDanceFrames(context), action, Global.getRandomText(getContext(), Global.text_dance));
//        }
//        else if(action.equals(Global.TOMI_ACTION_DRINK)) {
//            int appWidgetId= intent.getIntExtra("EXTRA_APPWIDGET_ID", -1);
//        	startAnimation(appWidgetId, Global.getDrinkFrames(context), action, Global.getRandomText(getContext(), Global.text_drink));
//        }
            else if (action.equals(Global.TOMI_ACTION_BAG)) {
                int bagID = intent.getIntExtra("EXTRA_BAG_ID", -1);
                updateWidget();
            } else if (action.equals(Global.TOMI_ACTION_PROMO)) {

                Global.isBeaconDetected = true;
                Global.lastBeaconInfo = intent.getStringExtra("EXTRA_BEACON_INFO");
                dataItems.addBeaconDetect(Global.lastBeaconInfo);
                new DataItems(context).addDetectToList(Global.lastBeaconInfo);
                showSign(getViews());
                updateWidget();
                if (isForeground(context, "com.neu.tomi.view.HomeActivity")) {
                    if (HttpRequest.isOnline(context)) {
                        Intent intentPromotions = new Intent(context, PromotionDialog.class);
                        intentPromotions.putExtra("TREAT", false);
                        intentPromotions.putExtra("WID", Global.lastBeaconInfo);
                        intentPromotions.putExtra("INFO", Global.lastBeaconInfo);
                        intentPromotions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentPromotions);
                    }
                } else {
                }
//                new ShowMesssageDetechBeacon(context).execute();
            } else if (action.equals(Global.TOMI_ACTION_NONE)) {
                Global.isBeaconDetected = false;
                Global.lastBeaconInfo = "";
                Global.currentAction = "";
                showDefaultTomi(getViews());
                updateWidget();
            } else
                super.onReceive(context, intent);// Handle normal widget intent
        } catch (Exception ex) {

        }
    }

    private void showNotificationLogin(Context context) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.monkey_icon)
                        .setContentTitle("Tomi")
                        .setSound(soundUri)
                        .setAutoCancel(true)
                        .setContentText("I'm hungry!!!");

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Global.NOTIFICATION_LAST_LOGIN_3D, mBuilder.build());
    }

    private List<Integer> getScriptWithItemId(int itemId) {
        List<Integer> script = new ArrayList<>();
        switch (itemId) {
            case DataItems.bananasId:
                return Global.getBananaEatFrames(getContext());
            case DataItems.bottleId:
                return Global.getDrinkFrames(getContext());
            case DataItems.tambourineId:
                return Global.getDanceFrames(getContext());
            case DataItems.fourLeafCloverId:
                return Global.getFourLeafFrames(getContext());
            case DataItems.orangeId:
                return Global.getOrangeFrames(getContext());
            case DataItems.iceCreamId:
                return Global.getIceCreamFrames(getContext());
            case DataItems.peanutId:
                return Global.getPeanutFrames(getContext());
            case DataItems.watermelonId:
                return Global.getWatermelonFrames(getContext());
        }
        return script;
    }

    String getRandomText(String[] ss) {
        return Global.getRandomText(getContext(), ss);
    }

    Runnable currentRunner;
    int aniIndex = 0;

    public void startAnimationResource(final int appWidgetId, final List<Integer> frames, final String action, String speech) {
        Global.currentAction = action;
        setSpeech(speech);
        updateWidget();
        le("startAnimation action=" + Global.currentAction);
        aniIndex = 0;
        if (currentRunner != null) getHandler().removeCallbacks(currentRunner);
        currentRunner = new Runnable() {
            public void run() {
                if (!Global.currentAction.equals(action))
                    return;
                le("startAnimation aniIndex=" + aniIndex);
                //Must get new views, can not use getViews() because animation will too slowly, dont know why!?
                RemoteViews views = new RemoteViews(getContext().getPackageName(), R.layout.main);
                if (aniIndex < frames.size()) {
                    views.setImageViewResource(R.id.imgAni, frames.get(aniIndex));
                } else {
                    onAnimationFinished(views);
                }
                getAppWidgetManager().updateAppWidget(appWidgetId, views);
                if (aniIndex < frames.size()) {
                    aniIndex++;
                    getHandler().postDelayed(this, 500);
                }
            }
        };
        currentRunner.run();
    }

    public void startAnimation(final int appWidgetId, final List<Bitmap> frames, final String action, String speech) {
        Global.currentAction = action;
        setSpeech(speech);
        updateWidget();
        le("startAnimation action=" + Global.currentAction);
        aniIndex = 0;
        if (currentRunner != null) getHandler().removeCallbacks(currentRunner);
        currentRunner = new Runnable() {
            public void run() {
                if (!Global.currentAction.equals(action))
                    return;
                le("startAnimation aniIndex=" + aniIndex);
                //Must get new views, can not use getViews() because animation will too slowly, dont know why!?
                RemoteViews views = new RemoteViews(getContext().getPackageName(), R.layout.main);
                if (aniIndex < frames.size()) {
                    views.setImageViewBitmap(R.id.imgAni, frames.get(aniIndex));
                } else {
                    onAnimationFinished(views);
                }
                getAppWidgetManager().updateAppWidget(appWidgetId, views);
                if (aniIndex < frames.size()) {
                    aniIndex++;
                    getHandler().postDelayed(this, 500);
                }
            }
        };
        currentRunner.run();
    }

    void onAnimationFinished(RemoteViews views) {
        le("onAnimationFinished isBeaconDetected=" + Global.isBeaconDetected);
        if (Global.isBeaconDetected) {
            showSign(views);
        } else
            showDefaultTomi(views);
    }

    void showSign(RemoteViews views) {
        Global.currentAction = Global.TOMI_ACTION_PROMO;
        views.setImageViewResource(R.id.imgAni, R.drawable.monkey_sign);
        views.setViewVisibility(R.id.textSpeech, View.GONE);
        views.setTextViewText(R.id.textSign, Global.getRandomText(getContext(), Global.text_beacon_trigger));
        views.setViewVisibility(R.id.textSign, View.VISIBLE);
    }

    void showDefaultTomi(RemoteViews views) {
        Global.currentAction = "";
        views.setImageViewResource(R.id.imgAni, R.drawable.monkey_default);
        views.setViewVisibility(R.id.textSign, View.GONE);
        setSpeech(Global.lastText);
        updateWidget();
    }

    void updateSpeech() {
        if (Global.currentAction.isEmpty() || Global.currentAction.equals(Global.TOMI_ACTION_SPEECH)) {
            Global.currentAction = Global.TOMI_ACTION_SPEECH;
        } else
            return;
        Date d = new Date(); //"now"
        int h = d.getHours();
        String s = "";
        String[] ss;
        if (h > 6 && h < 9)
            ss = Global.speech_time_1;
        else if (h >= 9 && h < 12)
            ss = Global.speech_time_2;
        else if (h >= 12 && h < 14)
            ss = Global.speech_time_3;
        else if (h >= 14 && h < 18)
            ss = Global.speech_time_4;
        else if (h >= 18 && h < 20)
            ss = Global.speech_time_5;
        else if (h >= 20 && h <= 24)
            ss = Global.speech_time_6;
        else
            ss = Global.speech_time_0;

        s = Global.getRandomText(getContext(), ss);
        Global.lastText = s;
        setSpeech(s);
        updateWidget();
    }

    void setSpeech(String s) {
        le("setSpeech = " + s);
        getViews().setImageViewResource(R.id.imgAni, R.drawable.monkey_speech);
        getViews().setTextViewText(R.id.textSpeech, s);
        getViews().setViewVisibility(R.id.textSpeech, View.VISIBLE);
        getViews().setViewVisibility(R.id.textSign, View.GONE);
    }

    void updateWidget() {
        le("updateWidget");
        ComponentName n = new ComponentName(getContext(), TomiProvider.class);
        getAppWidgetManager().updateAppWidget(n, getViews());
    }

    void showMainActivity(int appWidgetId) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra("EXTRA_APPWIDGET_ID", appWidgetId);
        intent.putExtra("EXTRA_BEACON_INFO", Global.lastBeaconInfo);
        intent.putExtra("EXTRA_CURENT_ACTION", Global.currentAction);
        intent.setClass(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
    }
    private void checkExpire(Context context) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour >= 8) {
            if (new DataItems(context).checkTimeExpireValid()) {
                new LoadPromotionsGrab(context).execute();
            }
        }
    }

    void setOnClickListener(int appWidgetId, int viewId) {
//        Clear old intent
        Intent activeForClear = new Intent(getContext().getApplicationContext(), TomiProvider.class);
        activeForClear.setAction(ACTION_CLICK);
        activeForClear.putExtra(EXTRA_CLICK_APPWIDGET_ID, appWidgetId);
        activeForClear.putExtra(EXTRA_CLICK_VIEW_ID, viewId);
        activeForClear.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activeForClear.setData(Uri.parse(appWidgetId + ":" + viewId));

        PendingIntent contentIntent_forclear = PendingIntent.getBroadcast(
                getContext().getApplicationContext(), 13417, activeForClear, PendingIntent.FLAG_CANCEL_CURRENT);
        contentIntent_forclear.cancel();
//		send intent
        Intent active = new Intent(getContext().getApplicationContext(), TomiProvider.class);
        active.setAction(ACTION_CLICK);
        active.putExtra(EXTRA_CLICK_APPWIDGET_ID, appWidgetId);
        active.putExtra(EXTRA_CLICK_VIEW_ID, viewId);
        active.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // This is tricky, be aware that you can only have one set of extras for
        // any given PendingIntent action+data+category+component pair.
        active.setData(Uri.parse(appWidgetId + ":" + viewId));

        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(
                getContext().getApplicationContext(), 13417, active, PendingIntent.FLAG_CANCEL_CURRENT);
        getViews().setOnClickPendingIntent(viewId, actionPendingIntent);
    }

    static void le(String s) {
        //android.util.Log.e(TAG, s);
    }


    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        new DataItems(context).setStateHelp(false);
//		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

//		alarmManager.cancel(createClockTickIntent(context));
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        new DataItems(context).setStateHelp(true);
        if (Global.serviceNotRun(context)) {
            Intent mIntent = new Intent(context, TomiService.class);
            context.startService(mIntent);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 120000, Global.createClockTickIntent(context));
    }

    class ShowMesssageDetechBeacon extends AsyncTask<Void, Void, Void> {
        private Context mContext;

        public ShowMesssageDetechBeacon(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!Global.isLayoutHomeActive) {
//                mContext.startActivity(new Intent(mContext, MessageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Global.isLayoutHomeActive = true;
            }
            super.onPostExecute(aVoid);
        }
    }



    private static boolean isForeground(Context context, String myPackage) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
//        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//        return componentInfo.getClassName().equals(myPackage);
        return false;
    }

    private void startTimer(Context context) {
//        new TimeOutNotification(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
//        new TimeOutNotification(context).execute();

    }
    private void sendActionListToServer(Context context) {
//
        DataItems dataItems = new DataItems(context);
        if (HttpRequest.isOnline(context)) {
            if (dataItems.checkSendActionValid()) {
                new SendActionToServer(dataItems).execute();
            }
        }

    }
    class SendActionToServer extends AsyncTask<Void, Void, JSONObject> {
        private DataItems mDataItems;

        public SendActionToServer( DataItems dataItems) {
            mDataItems = dataItems;
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
                        mDataItems.clearActionList();
                        mDataItems.clearUseItemList();
                        mDataItems.clearViewActionList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }


    private void rename(Context context){
        DataItems dataItems=new DataItems(context);
        if (dataItems.isNewName()) {
            if(HttpRequest.isOnline(context))
                new Rename(dataItems, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    class Rename extends AsyncTask<Void, Void, JSONObject> {
        private String mName;
        private DataItems mDataItems;
        private Context mContext;

        public Rename(DataItems dataItems, Context context) {
            mContext = context;
            mDataItems = dataItems;
            mName = dataItems.getNameOfUser();

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.Rename(mContext, mName);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
                        mDataItems.setIsNewName(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }

    class LoadPromotionsGrab extends AsyncTask<Void, Void, List<PromtionObject>> {
        private Context mContext;
        private SqliteHelper mSqliteHelper;

        public LoadPromotionsGrab(Context context) {
            mContext = context;
            mSqliteHelper = new SqliteHelper(context);

        }


        @Override
        protected List<PromtionObject> doInBackground(Void... params) {
            return mSqliteHelper.getAllPromotion();
        }

        @Override
        protected void onPostExecute(List<PromtionObject> promtionObjects) {
            if (promtionObjects.size() > 0) {
                boolean isExpire = false;
                for (PromtionObject item : promtionObjects) {
                    if (Global.checkDayLessThan(3, item.getEndTime())) {
                        if (Global.checkDayLessThan(-2, item.getEndTime())) {
                            mSqliteHelper.deletePromotion(item.getPromotionId());
                            clearNotificationExpire(mContext);
                        }else
                            isExpire = true;
                    }
                }
                if (isExpire)
                    showNotification(mContext);
            }
            super.onPostExecute(promtionObjects);
        }
    }

    private void showNotification(Context context) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.monkey_icon)
                        .setContentTitle("Expire")
                        .setSound(soundUri)
                        .setContentText("Promotion can be used less than 3 days!!!");

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.putExtra("EXPIRE", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 19,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Global.NOTIFICATION_EXPIRE_ID, mBuilder.build());
    }

    private void clearNotificationExpire(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Global.NOTIFICATION_EXPIRE_ID);
        } catch (Exception ex) {

        }
    }
}
