package com.neu.tomi.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;

import com.neu.tomi.MainActivity;
import com.neu.tomi.R;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.SqliteHelper;

import java.util.Calendar;
import java.util.List;


public class GetOnScreenBroadcast extends BroadcastReceiver {
    private DataItems dataItems;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            dataItems = new DataItems(context);
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                restartService(context);

//                sendActionListToServer(context, dataItems);
            } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                restartService(context);
            } else if (intent.getAction().equals(
                    "android.net.conn.CONNECTIVITY_CHANGE")) {
                restartService(context);
//                sendActionListToServer(context, dataItems);


            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                restartService(context);
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Global.isBeaconDetected = false;
                        Global.currentAction = "";
//                        Global.clearNotificationDetectBeacon(context);
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

    private void restartService(Context context) {
        try {
            if (Global.serviceNotRun(context)) {
                Intent mIntent = new Intent(context, TomiService.class);
                context.stopService(mIntent);
                context.startService(mIntent);
            }
        } catch (Exception ex) {

        }
    }
















}
