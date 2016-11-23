package com.neu.tomi.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.neu.tomi.ultity.Global;

/**
 * Created by Nga on 14/10/2015.
 */
public class RestartService extends BroadcastReceiver {

    private static final String TAG = "RestartService";

    public RestartService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "RESTART", Toast.LENGTH_SHORT).show();
        if(Global.serviceNotRun(context)){
            Intent mIntent = new Intent(context, TomiService.class);
            context.startService(mIntent);
        }
    }
}