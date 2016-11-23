package com.neu.tomi.view;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neu.tomi.MainActivity;
import com.neu.tomi.R;
import com.neu.tomi.ultity.Global;

public class MessageActivity extends Activity {
    private ImageButton btCloseMessageDetechBeacon;
    private TextView tvMessageDetechBeacon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibratorAndSound();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.85);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.activity_message);
        btCloseMessageDetechBeacon=(ImageButton)findViewById(R.id.btCloseMessageDetechBeacon);
        tvMessageDetechBeacon=(TextView)findViewById(R.id.tvMessageDetechBeacon);
        tvMessageDetechBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.putExtra("EXTRA_APPWIDGET_ID", 0);
                intent.putExtra("EXTRA_BEACON_INFO", Global.lastBeaconInfo);
                intent.putExtra("EXTRA_CURENT_ACTION", Global.currentAction);
                intent.setClass(MessageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                clearNotification();
                finish();
            }
        });
        btCloseMessageDetechBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNotification();
                finish();
            }
        });
        showNotification();
//        new TimeShow().execute();
    }

    private void showNotification(){
        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.monkey_icon)
                        .setContentTitle("Detect new beacon")
                        .setContentText("Get promotion now!!!");

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, 0);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(Global.NOTIFICATION_ID, mBuilder.build());
    }
    private void clearNotification(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Global.NOTIFICATION_ID);
    }
    private void vibratorAndSound(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        MediaPlayer mPlayer = MediaPlayer.create(MessageActivity.this, R.raw.sound_notification);
        mPlayer.start();
    }

    class TimeShow extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
           while(Global.isBeaconDetected){
               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            clearNotification();
            finish();
            super.onPostExecute(aVoid);
        }
    }
}
