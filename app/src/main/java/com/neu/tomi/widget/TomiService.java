package com.neu.tomi.widget;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.neu.tomi.MainActivity;
import com.neu.tomi.R;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TomiService extends Service {

    SharedPreferences tomiPreferences;
    private static final Region TOMI_REGION = new Region("rid", null, null, null);
    public static Handler mMyServiceHandler = null;
    public static Boolean mIsServiceRunning = false;
    private static long TIME_DETECT = -1;
    private static long TIME_CLEAR_NOTIFICATION = -1;
    private static long TIME_SHOW_EFFECT_NOTIFICATION = -1;
    private BeaconManager beaconManager;
    private Handler handler;
    //    private final String TOMI_BEACON_UUID = "8286F122-6385-2F0C-9A8A-E9CFD08C3B73";
    private final int TOMI_BEACON_MAJORID = 28788;
    private DataItems dataItems;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        /*
        * thep 13/10/2015
		 */
        dataItems = new DataItems(getBaseContext());
//        startNotificationListener(getBaseContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        BroadcastReceiver receiver = new GetOnScreenBroadcast();
        registerReceiver(receiver, filter);

        tomiPreferences = getSharedPreferences("MONKEY_WIDGET_PARAMS", Context.MODE_PRIVATE);
        handler = new Handler();
        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {

                int size = beacons.size();
                String sTmp = "";
                for (int i = 0; i < size && i < 10; i++) {
                    Beacon b = beacons.get(i);
                    String id = b.getMajor() + "_" + b.getMinor();
//                    sTmp += (sTmp.isEmpty() ? "" : ",") + id;//.getMajor());
//                    Toast.makeText(mContext, b.getProximityUUID(), Toast.LENGTH_LONG).show();
                    if (b.getMajor() == TOMI_BEACON_MAJORID) {
                        sTmp += (sTmp.isEmpty() ? "" : ",") + id;//.getMajor());
                    }
//                    sTmp += (sTmp.isEmpty() ? "" : ",") + id;//.getMajor());
                }
                if (!sTmp.isEmpty()) {

                    Global.isBeaconDetected = true;

                    if (!Global.currentAction.equals(Global.TOMI_ACTION_PROMO)) {
                        //kiem tra ton tai truoc
                        boolean existBeacon =false;
                        String items[] = sTmp.split(",");
                        int lengItem =items.length;
                        if(lengItem>1){
                            String items2[] = Global.lastBeaconInfo.split(",");
                            if(lengItem!=items2.length){
                                existBeacon=false;
                            }else{
                                int check=0;
                                for(int i=0;i<lengItem;i++){
                                    String tmp =items[i];
                                    for(int j=0;j<lengItem;j++){
                                        if(tmp.equals(items2[j])){
                                            check++;
                                        }
                                    }
                                }
                                existBeacon=(check==lengItem);
                            }
                        }else {
                            existBeacon=Global.lastBeaconInfo.equals(sTmp);
                        }
                        //Dat lại beaconInfo
                        Global.lastBeaconInfo = sTmp;
                        long currentTime =System.currentTimeMillis();
                        //Notification bi ngat hoac da detect beacon moi
                        if (TIME_CLEAR_NOTIFICATION < 0||!existBeacon) {
                            TIME_CLEAR_NOTIFICATION = currentTime;
                            showNotificationDetectBeacon(getBaseContext());
                            if((currentTime-TIME_SHOW_EFFECT_NOTIFICATION)>1200000){
                                TIME_SHOW_EFFECT_NOTIFICATION=currentTime;
                                effectNotificationDetectBeacon(getBaseContext());
                            }
                        }

                        TIME_DETECT = currentTime;
                        if(TIME_SHOW_EFFECT_NOTIFICATION<0){
                            TIME_SHOW_EFFECT_NOTIFICATION =currentTime;
                        }
                        //Tomi show bang
                        showPromoSign();
                    }
                }

                if (Global.currentAction.equals(Global.TOMI_ACTION_PROMO)) {
                    long currentTime = System.currentTimeMillis();

                    if (TIME_DETECT > 0) {
                        long betweenTime = (currentTime - TIME_DETECT);
                        if (betweenTime > 30000) {
                            onLostBeacon();
                            TIME_DETECT = -1;

                        }
                    }
                }
            }
        });
/*
        beaconManager.setMonitoringListener(new MonitoringListener() {
	      public void onEnteredRegion(Region region, List<Beacon> beacons) {
	        onFoundBeacons(beacons);
	      }

	      public void onExitedRegion(Region region) {
	        onLostBeacon();
	      }
	    });
*/


    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        /*
        Toast.makeText(this, "Tomi onTaskRemoved", Toast.LENGTH_SHORT).show();
	    Intent restartService = new Intent(getApplicationContext(),
	            this.getClass());
	    restartService.setPackage(getPackageName());
	    PendingIntent restartServicePI = PendingIntent.getService(
	            getApplicationContext(), 1, restartService,
	            PendingIntent.FLAG_ONE_SHOT);
	    AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	    alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +1000, restartServicePI);
	    */
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Tomi started", Toast.LENGTH_SHORT).show();
//        Log.e("Received", "onStartCommand");

        mIsServiceRunning = true;
        //initBeaconScan();
        connectToService();
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        beaconManager.disconnect();
        mIsServiceRunning = false;
        /*
         * 05/10/2015
		 * Restart service 
		 */
        Context context = getBaseContext();
        Intent mIntent = new Intent(context, TomiService.class);
        context.startService(mIntent);
        Intent intent = new Intent("restartApps");
        sendBroadcast(intent);
        super.onDestroy();
    }


    boolean lastBlueToothEnabled = true;
    boolean firstTimeCheckBluetooth = true;

    void checkBluetooth() {
        boolean b = beaconManager.isBluetoothEnabled();
        if (b != lastBlueToothEnabled || firstTimeCheckBluetooth) {
            firstTimeCheckBluetooth = false;
            lastBlueToothEnabled = b;
            if (lastBlueToothEnabled) {
                //initBeaconScan();
            } else {
                onLostBeacon();
                //beaconManager.disconnect();
                //showNotification(1,"Disconnect iBeacon service");
            }
        }

    }

    long lastSpeechTime = 0;

    void updateSpeech() {
        //5 minute speech
        if ((System.currentTimeMillis() - lastSpeechTime) > 300000 && (Global.currentAction.isEmpty() || Global.currentAction.equals(Global.TOMI_ACTION_SPEECH))) {
            Global.currentAction = Global.TOMI_ACTION_SPEECH;
            lastSpeechTime = System.currentTimeMillis();
            Intent intent = new Intent(this.getApplicationContext(), TomiProvider.class);
            intent.setAction(Global.TOMI_ACTION_SPEECH);
            sendBroadcast(intent);
        }
    }

    Runnable runnableTick;

    void startTimer() {
        if (runnableTick != null) {
            handler.removeCallbacks(runnableTick);
            runnableTick = null;
        }
        runnableTick = new Runnable() {
            public void run() {
                checkBluetooth();
                updateSpeech();
                try {
                    if (TIME_CLEAR_NOTIFICATION > 0) {
                        long betweenTime = System.currentTimeMillis() - TIME_CLEAR_NOTIFICATION;
                        if (betweenTime > 1200000) {
                            TIME_CLEAR_NOTIFICATION = -1;
                            Global.clearNotificationDetectBeacon(getBaseContext());
                        }
                    }

//                    if (dataItems.checkSendActionValid()) {
//                        sendActionListToServer(getBaseContext(), dataItems);
//
//                    }
                } catch (Exception e) {
                }
                handler.postDelayed(this, 1000);
            }
        };
        runnableTick.run();
    }

    void onFoundBeacons(List<Beacon> beacons) {
        int size = beacons.size();
        String sTmp = "";
        for (int i = 0; i < size && i < 10; i++) {
            Beacon b = beacons.get(i);
            sTmp += (sTmp.isEmpty() ? "" : ",") + Integer.toString(b.getMajor());
        }
        if (!sTmp.isEmpty()) {
            Global.lastBeaconInfo = sTmp;
            Global.isBeaconDetected = true;
        }

        if (!Global.currentAction.equals(Global.TOMI_ACTION_PROMO)) {
            showPromoSign();
        }
    }

    void onLostBeacon() {
        Global.isBeaconDetected = false;
        Global.currentAction = "";
        Intent intent = new Intent(getBaseContext(), TomiProvider.class);
        intent.setAction(Global.TOMI_ACTION_NONE);
        sendBroadcast(intent);
    }

    void showPromoSign() {
        Global.currentAction = Global.TOMI_ACTION_PROMO;
        Intent intent = new Intent(this.getApplicationContext(), TomiProvider.class);
        intent.putExtra("EXTRA_BEACON_INFO", Global.lastBeaconInfo);
        intent.setAction(Global.TOMI_ACTION_PROMO);
        sendBroadcast(intent);
    }


    private void connectToService() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(TOMI_REGION);
                } catch (RemoteException e) {
//                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    void initBeaconScan() {
        try {
            beaconManager.stopMonitoring(TOMI_REGION);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        beaconManager.disconnect();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(TOMI_REGION);
                } catch (RemoteException e) {
                }
            }
        });
    }


//    private  void sendNotification(JSONObject jsonObject) {
//        if (jsonObject != null) {
//            try {
//                if (jsonObject.getBoolean("state")) {
//                    SqliteHelper sqliteHelper = new SqliteHelper(mContext);
//                    String id = jsonObject.getString("id");
//                    String title = jsonObject.getString("title");
//                    String content = jsonObject.getString("content");
//                    String link = jsonObject.getString("link");
//                    sqliteHelper.insertMessage(id, title, content, link);
//                    showNotification(mContext, title, content, id);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void showNotificationDetectBeacon(Context context) {
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.monkey_icon)
                        .setContentTitle("Tomi Treat detected")
                        .setContentText("Get promotion now!")
//                        .setSound(soundUri)
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 103,
                resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Global.NOTIFICATION_ID, mBuilder.build());
    }

    private static void effectNotificationDetectBeacon(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, soundUri);
        r.play();
    }



    private void sendActionListToServer(Context context, DataItems dataItems) {
        new SendActionToServer(context, dataItems).execute();

    }



    class SendActionToServer extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private DataItems mDataItems;

        public SendActionToServer(Context context, DataItems dataItems) {
            mContext = context;
            mDataItems = dataItems;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return dataItems.sendActionPost();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
//            if (jsonObject != null) {
//                try {
//                    if (jsonObject.getBoolean("state")) {
//                        mDataItems.clearActionList();
//                        mDataItems.clearUseItemList();
//                        mDataItems.clearViewActionList();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            super.onPostExecute(jsonObject);
        }
    }

}
