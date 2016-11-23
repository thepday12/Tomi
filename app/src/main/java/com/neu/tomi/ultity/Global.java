package com.neu.tomi.ultity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;

import com.neu.tomi.R;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.view.MailDetailActivity;
import com.neu.tomi.widget.TomiProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class Global {
    static SharedPreferences tomiPreferences;
    public static boolean isBeaconDetected = false;
    public static boolean isLayoutHomeActive = false;
    public static boolean isChangeTheme = false;
    public static boolean IS_CANCEL_NOTIFICATION = true;
    public static int idThemeItem = 0;
    public static String lastBeaconInfo = "";
    public static String currentAction = "";
    public static String lastText = "";


    public static final String TOMI_ACTION_SPEECH = "TOMI_ACTION_SPEECH";
    public static final String TOMI_ACTION_DANCE = "TOMI_ACTION_DANCE";
    public static final String TYPE_TOMI_ACTION_EAT = "TOMI_ACTION_EAT";
    public static final String TYPE_TOMI_ACTION_DAILY = "TOMI_ACTION_DAILY";
    public static final String TYPE_TOMI_ACTION_VOUCHER = "TOMI_ACTION_VOUCHER";
    public static final String TYPE_TOMI_BAG = "TOMI_BAG";
    public static final String TYPE_TOMI_THEME = "TOMI_THEME";
    public static final String TOMI_ACTION_UPDATE_BANANA = "TOMI_ACTION_UPDATE_BANANA";
    public static final String TOMI_ACTION_DRINK = "TOMI_ACTION_DRINK";
    public static final String TOMI_ACTION_PROMO = "TOMI_ACTION_PROMO";
    public static final String TOMI_ACTION_BAG = "TOMI_ACTION_BAG";
    public static final String TOMI_ACTION_NONE = "TOMI_ACTION_NONE";
    public static String CLOCK_WIDGET_UPDATE = "com.neu.tomi.CLOCK_WIDGET_UPDATE";
    public static final int TOMI_KEY_BANANA = -9001;
    public static final int TOMI_KEY_DANCE = -9002;
    public static final int TOMI_KEY_TAMBO = -9003;
    public static final int TOMI_KEY_BAG = -9004;
    public static final int TOMI_KEY_SPEECH = -9005;
    public static final int NOTIFICATION_ID = 109;
    public static final int NOTIFICATION_EXPIRE_ID = 110;
    public static final int NOTIFICATION_LAST_LOGIN_3D = 120;
    public static final int SHARE_REQUEST_CODE = 1912;

    public  static  int[] getListWidgetId(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        AppWidgetHost appWidgetHost = new AppWidgetHost(context, 1); // for removing phantoms
        boolean hasWidget = false;

        int[] appWidgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(context, TomiProvider.class));
       return appWidgetIDs;
    }

    public static SharedPreferences getTomiPreferences(Context context) {
        if (tomiPreferences == null)
            tomiPreferences = context.getSharedPreferences("MONKEY_WIDGET_PARAMS", Context.MODE_PRIVATE);
        return tomiPreferences;
    }

    public static PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(Global.CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public static final List<Integer> getDrinkFrames(Context context) {

        List<Integer> drinkList = new ArrayList<Integer>();
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_drink_01);
        drinkList.add(R.drawable.monkey_drink_02);
        drinkList.add(R.drawable.monkey_drink_03);
        drinkList.add(R.drawable.monkey_happy);
        drinkList.add(R.drawable.monkey_happy);
        return drinkList;
    }

    public static final List<Integer> getBananaEatFrames(Context context) {
        List<Integer> bananaEatList = new ArrayList<Integer>();
        bananaEatList.add(R.drawable.monkey_eat_01);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_01);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_01);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_eat_02);
        bananaEatList.add(R.drawable.monkey_eat_03);
        bananaEatList.add(R.drawable.monkey_happy);
        bananaEatList.add(R.drawable.monkey_happy);
        return bananaEatList;
    }

    public static List<Integer> getDanceFrames(Context context) {

        List<Integer> danceList = new ArrayList<Integer>();
        danceList.add(R.drawable.monkey_dance_01);
        danceList.add(R.drawable.monkey_dance_02);
        danceList.add(R.drawable.monkey_dance_03);
        danceList.add(R.drawable.monkey_dance_04);
        danceList.add(R.drawable.monkey_dance_05);
        danceList.add(R.drawable.monkey_dance_04);
        danceList.add(R.drawable.monkey_dance_03);
        danceList.add(R.drawable.monkey_dance_02);
        danceList.add(R.drawable.monkey_dance_01);
        danceList.add(R.drawable.monkey_dance_02);
        danceList.add(R.drawable.monkey_dance_03);
        danceList.add(R.drawable.monkey_dance_04);
        danceList.add(R.drawable.monkey_dance_05);
        danceList.add(R.drawable.monkey_dance_04);
        danceList.add(R.drawable.monkey_dance_03);
        danceList.add(R.drawable.monkey_dance_02);
        danceList.add(R.drawable.monkey_dance_01);
        danceList.add(R.drawable.monkey_dance_02);
        danceList.add(R.drawable.monkey_dance_03);
        danceList.add(R.drawable.monkey_dance_04);
        danceList.add(R.drawable.monkey_dance_05);
        danceList.add(R.drawable.monkey_happy_stand);
        danceList.add(R.drawable.monkey_happy);
        return danceList;
    }


    public static List<Integer> getOrangeFrames(Context context) {

        List<Integer> eatList = new ArrayList<Integer>();
        eatList.add(R.drawable.orange_eat_1);
        eatList.add(R.drawable.orange_eat_2);
        eatList.add(R.drawable.orange_eat_3);
        eatList.add(R.drawable.orange_eat_4);
        eatList.add(R.drawable.orange_eat_5);
        eatList.add(R.drawable.orange_eat_6);
        eatList.add(R.drawable.orange_eat_7);
        eatList.add(R.drawable.orange_eat_8);
        eatList.add(R.drawable.orange_eat_9);
        eatList.add(R.drawable.orange_eat_7);
        eatList.add(R.drawable.orange_eat_8);
        eatList.add(R.drawable.orange_eat_9);
        eatList.add(R.drawable.orange_eat_7);
        eatList.add(R.drawable.orange_eat_8);
        eatList.add(R.drawable.orange_eat_9);
        eatList.add(R.drawable.monkey_happy_stand);
        eatList.add(R.drawable.monkey_happy);
        return eatList;
    }

    public static List<Integer> getIceCreamFrames(Context context) {

        List<Integer> eatList = new ArrayList<Integer>();
        eatList.add(R.drawable.mokey_eat_ice_cream_1);
        eatList.add(R.drawable.mokey_eat_ice_cream_2);
        eatList.add(R.drawable.mokey_eat_ice_cream_3);
        eatList.add(R.drawable.mokey_eat_ice_cream_4);
        eatList.add(R.drawable.mokey_eat_ice_cream_2);
        eatList.add(R.drawable.mokey_eat_ice_cream_3);
        eatList.add(R.drawable.mokey_eat_ice_cream_4);
        eatList.add(R.drawable.mokey_eat_ice_cream_2);
        eatList.add(R.drawable.mokey_eat_ice_cream_3);
        eatList.add(R.drawable.mokey_eat_ice_cream_4);
//        eatList.add(R.drawable.monkey_happy_stand);
        eatList.add(R.drawable.monkey_happy);
        return eatList;
    }

    public static List<Integer> getFourLeafFrames(Context context) {

        List<Integer> eatList = new ArrayList<Integer>();
        eatList.add(R.drawable.four_leaf__1);
        eatList.add(R.drawable.four_leaf__2);
        eatList.add(R.drawable.four_leaf__3);
        eatList.add(R.drawable.four_leaf__2);
        eatList.add(R.drawable.four_leaf__3);
        eatList.add(R.drawable.four_leaf__2);
        eatList.add(R.drawable.four_leaf__3);
        eatList.add(R.drawable.four_leaf__2);
        eatList.add(R.drawable.four_leaf__3);
        eatList.add(R.drawable.four_leaf__2);
        eatList.add(R.drawable.four_leaf__3);
        eatList.add(R.drawable.four_leaf__2);
        eatList.add(R.drawable.four_leaf__3);
//        eatList.add(R.drawable.monkey_happy_stand);
        eatList.add(R.drawable.monkey_happy);
        return eatList;
    }

    public static List<Integer> getPeanutFrames(Context context) {

        List<Integer> eatList = new ArrayList<Integer>();
        eatList.add(R.drawable.eat_peanut_1);
        eatList.add(R.drawable.eat_peanut_2);
        eatList.add(R.drawable.eat_peanut_3);
        eatList.add(R.drawable.eat_peanut_4);
        eatList.add(R.drawable.eat_peanut_5);
        eatList.add(R.drawable.eat_peanut_6);
        eatList.add(R.drawable.eat_peanut_7);
        eatList.add(R.drawable.eat_peanut_2);
        eatList.add(R.drawable.eat_peanut_3);
        eatList.add(R.drawable.eat_peanut_4);
        eatList.add(R.drawable.eat_peanut_5);
        eatList.add(R.drawable.eat_peanut_2);
        eatList.add(R.drawable.eat_peanut_3);
        eatList.add(R.drawable.eat_peanut_4);
        eatList.add(R.drawable.eat_peanut_5);
        eatList.add(R.drawable.eat_peanut_6);
        eatList.add(R.drawable.eat_peanut_7);
        eatList.add(R.drawable.monkey_happy_stand);
        eatList.add(R.drawable.monkey_happy);
        return eatList;
    }

    public static List<Integer> getWatermelonFrames(Context context) {

        List<Integer> eatList = new ArrayList<Integer>();
        eatList.add(R.drawable.eat_watermelon_1);
        eatList.add(R.drawable.eat_watermelon_2);
        eatList.add(R.drawable.eat_watermelon_3);
        eatList.add(R.drawable.eat_watermelon_4);
        eatList.add(R.drawable.eat_watermelon_5);
        eatList.add(R.drawable.eat_watermelon_6);
        eatList.add(R.drawable.eat_watermelon_2);
        eatList.add(R.drawable.eat_watermelon_3);
        eatList.add(R.drawable.eat_watermelon_4);
        eatList.add(R.drawable.eat_watermelon_5);
        eatList.add(R.drawable.eat_watermelon_6);
        eatList.add(R.drawable.eat_watermelon_4);
        eatList.add(R.drawable.eat_watermelon_5);
        eatList.add(R.drawable.eat_watermelon_6);

        eatList.add(R.drawable.monkey_happy_stand);
        eatList.add(R.drawable.monkey_happy);
        return eatList;
    }

    public static final List<ItemObject> getItemResource() {
        List<ItemObject> itemObjects = new ArrayList<>();
        //(int id, String actionId, String name, String description, int max_action, int point, int pointType)
        itemObjects.add(new ItemObject(DataItems.bananasId, Global.TYPE_TOMI_ACTION_EAT, "Banana", "Up 5XP and 5PTs for tomi", 0, "Smile like a monkey with a new banana!;Burp!;Yummy yummy yummy banana in my tummy!", 5, 5));
        itemObjects.add(new ItemObject(DataItems.orangeId, Global.TYPE_TOMI_ACTION_EAT, "Orange", "Up 15XP and 20PTs for tomi", 0, "Great morning juice! ;High in Vitamin C!!", 20, 15));
        itemObjects.add(new ItemObject(DataItems.iceCreamId, Global.TYPE_TOMI_ACTION_EAT, "Ice cream", "Up 20XP and 15PTs for tomi", 0, "Great in this hot weather!;It's cold!!!", 15, 20));
        itemObjects.add(new ItemObject(DataItems.peanutId, Global.TYPE_TOMI_ACTION_EAT, "Peanut", "Up 10XP and 5PTs for tomi", 0, "I want more peanut;Great with butter!!", 10, 5));
        itemObjects.add(new ItemObject(DataItems.watermelonId, Global.TYPE_TOMI_ACTION_EAT, "Watermelon", "Up 15XP and 8PTs for tomi", 0, "Croap! Cro.ap! Croa.ppp!!;I love Watermelon!", 8, 15));
        itemObjects.add(new ItemObject(DataItems.bottleId, Global.TYPE_TOMI_ACTION_DAILY, "Bottle", "Up 1 XP for tomi (5/day)", 50, "Thanks for the drink...;Water is my best friend!;When you drink the water remember the spring.", 0, 1));
        itemObjects.add(new ItemObject(DataItems.tambourineId, Global.TYPE_TOMI_ACTION_DAILY, "Tambourine", "Up 2 XP for tomi(5/day)", 5, "T-t-t-tambourine yeah!;Dance like no one's watching....", 5, 8));
        itemObjects.add(new ItemObject(DataItems.fourLeafCloverId, Global.TYPE_TOMI_ACTION_DAILY, "Four Leaf Clover", "Up 2 XP for tomi(5/day)", 5, "Make a wish!;Huat Ah!;I gonna be lucky!", 5, 1));
        itemObjects.add(new ItemObject(DataItems.bag1Id, Global.TYPE_TOMI_BAG, "Bag1", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag2Id, Global.TYPE_TOMI_BAG, "Bag2", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag3Id, Global.TYPE_TOMI_BAG, "Bag3", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag4Id, Global.TYPE_TOMI_BAG, "Bag4", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag5Id, Global.TYPE_TOMI_BAG, "Bag5", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag6Id, Global.TYPE_TOMI_BAG, "Bag6", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag7Id, Global.TYPE_TOMI_BAG, "Bag7", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag8Id, Global.TYPE_TOMI_BAG, "Bag8", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag9Id, Global.TYPE_TOMI_BAG, "Bag9", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag10Id, Global.TYPE_TOMI_BAG, "Bag10", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag11Id, Global.TYPE_TOMI_BAG, "Bag11", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag12Id, Global.TYPE_TOMI_BAG, "Bag12", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.bag13Id, Global.TYPE_TOMI_BAG, "Bag13", "Bag", 0, "", 0, 0));
        itemObjects.add(new ItemObject(DataItems.themeId1, Global.TYPE_TOMI_THEME, "Default theme", "Theme", 0, "", 0, 0));
        return itemObjects;
    }

    public static String formatDateFacebook(String birthday) {
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = new Date();
        try {
            parsed = inputFormat.parse(birthday);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String outputText = outputFormat.format(parsed);
        return outputText;
    }
    public static final String[] text_beacon_trigger = {
            "Grab Tomi Treats Now!",
            "Grab Tomi Treats Now!!!"
    };
    public static final String[] speech_time_0 = {
            "I am Tomi!",
            "I love banana!"
    };
    public static final String[] speech_time_1 = {
            "Rise and Shine!",
            "Lets start the day with a mighty breakfast!",
            "Good Banana morning!"
    };
    public static final String[] speech_time_2 = {
            "Have a lovely day",
            "It’s a good day to have a good day!",
            "Think happy be happy",
            "Always be positive!"

    };
    public static final String[] speech_time_3 = {
            "I smell lunch",
            "Keep calm and eat lunch",
            "Lunch matters"
    };
    public static final String[] speech_time_4 = {
            "Anywhere with you is a great place for me",
            "Enjoy the little things",
            "Bliss Banana Bliss",
            "Nothing is impossible, the word itself says \"I'm possible\"!"
    };
    public static final String[] speech_time_5 = {
            "The best dish of dinner is good mood",
            "shall I be eating like a pauper?",
            "Sometimes you win, sometimes you learn"
    };
    public static final String[] speech_time_6 = {
            "Happiness is sleep",
            "Rest and be thankful",
            "I must get my beauty rest, see you when the sun is up",
            "Wake up with determination, go to bed with satisfaction"
    };

    public static String getRandomText(Context context, String[] t) {
        String xx = "";
        //if(tomiPreferences==null) xx = "--";
        int lastTextIndex = getTomiPreferences(context).getInt("LAST_SPEECH_INDEX", -1);

        String s = "";
        int n = (int) Math.round((t.length - 1) * Math.random());
        if (n == lastTextIndex) {
            n++;
            if (n >= t.length)
                n = 0;
        }
        getTomiPreferences(context).edit().putInt("LAST_SPEECH_INDEX", n).commit();
        return t[n] + xx;
    }

    public static boolean serviceNotRun(Context context) {

        ActivityManager actvityManager = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningServiceInfo service : actvityManager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().toLowerCase()
                    .contains("com.neu.tomi"))
                return false;
        }
        return true;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static int getColor() {
        Random random = new Random();
        int firstColor = Color.parseColor("#0097A7");
        int secondColor = Color.parseColor("#FFA000");
        int thirdColor = Color.parseColor("#FF5722");
        int forthColor = Color.parseColor("#673AB7");
        int fiveColor = Color.parseColor("#009866");
        switch (random.nextInt(5)) {
            case 1:
                return firstColor;
            case 2:
                return secondColor;
            case 3:
                return thirdColor;
            case 4:
                return forthColor;
            default:
                return fiveColor;
        }
    }

    public static String getTextSubDate(Date date) {
        String result = "";
        int betweenDay = getIntSubDate(date);
        if (betweenDay < 0) {
            result = "Expired";
        } else {
            int betweenYear = betweenDay / 365;
            if (betweenYear > 0) {
                result = betweenYear + " year";
                if (betweenYear > 1)
                    result += "s";
            } else {
                int betweenMonth = betweenDay / 30;
                if (betweenMonth > 0) {
                    result = betweenMonth + " month";
                    if (betweenMonth > 1)
                        result += "s";
                } else {
                    int betweenWeek = betweenDay / 7;
                    if (betweenWeek > 1) {
                        result = betweenWeek + " weeks";
                    } else {
                        result = betweenDay + " day";
                        if (betweenDay > 1)
                            result += "s";
                    }
                }
            }
        }
        return result;
    }
    public static double getDoubleSubDate(Date date) {
        long currentTime = System.currentTimeMillis();
        long endTime = date.getTime();
        double between = (endTime - currentTime) / 86400000.0;
        return between;
    }
    public static int getIntSubDate(Date date) {
        long currentTime = System.currentTimeMillis();
        long endTime = date.getTime();
        double between = (endTime - currentTime) / 86400000.0;
        int betweenDay = 0;
        if (between > 0) {
            betweenDay = (int) Math.ceil(between);
        } else{
            if(between<0) {
                betweenDay = (int) between;
                if(betweenDay==0){
                    return -1;
                }else {
                    return betweenDay;
                }
            }else{
                betweenDay =0;
            }
        }
        return betweenDay;
    }

    public static boolean checkDayLessThan(int dayValid, String dateExpire) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateExpire);
        } catch (ParseException e) {

        }
        int dayLeft = getIntSubDate(date);
        if (dayLeft > dayValid)
            return false;
        else
            return true;
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(c.getTime());
        return time;
    }

    public static String getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(c.getTime());
        return time;
    }

public static String saveImagePromotion(String imageUrl){
//    final String imageUrl = promtionObject.getImageURL();
    try {
        String fileName =imageUrl.substring(imageUrl.lastIndexOf('/') + 1); //promtionObject.getPromotionId() + ".png";
        URL url = new URL(imageUrl);
        URLConnection conn = url.openConnection();
        Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/tomi_image");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        myDir = new File(myDir, fileName);
        if (!myDir.exists()) {

            FileOutputStream out = new FileOutputStream(myDir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }
        return Uri.fromFile(myDir).toString();
    } catch (Exception e) {
        return "";
    }
}
    public static boolean savePromotion(PromtionObject promtionObject, SqliteHelper sqliteHelper) {
        // Store image to default external storage directory
        PromtionObject object = sqliteHelper.getPromotionWithID(promtionObject.getPromotionId());
        if (object != null) {
            sqliteHelper.updatePromotion(promtionObject.getPromotionId(), object.getTotal() + 1);
            return true;
        } else {
            String promotionURI = saveImagePromotion(promtionObject.getImageURL());
            if (promotionURI.isEmpty())
                return false;
            else {
                promtionObject.setImageURL(promotionURI);
                sqliteHelper.insertPromotion(promtionObject);
                return true;
            }
        }

    }

    public static boolean buyPromotion(PromtionObject promtionObject, SqliteHelper sqliteHelper) {
        // Store image to default external storage directory
            PromtionObject object = sqliteHelper.getPromotionWithID(promtionObject.getPromotionId());
            if (object != null) {
                sqliteHelper.updatePromotion(promtionObject.getPromotionId(), object.getTotal() + 1);
                return true;
            } else {
                String promotionURI = saveImagePromotion(promtionObject.getImageURL());
                if (promotionURI.isEmpty())
                    return false;
                else {
                    promtionObject.setImageURL(promotionURI);
                    sqliteHelper.insertPromotion(promtionObject);
                    return true;
                }
            }
    }

    public static boolean saveImageItem(final String imageUrl, final int itemId) {
        // Store image to default external storage directory
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/resources/" + itemId);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, fileName);
            if (!myDir.exists()) {

                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static String saveImageItemBonusUse(final String imageUrl, final int itemId) {
        // Store image to default external storage directory
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/resources/" + itemId);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, fileName);
            if (!myDir.exists()) {

                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            return Uri.fromFile(myDir).toString();
        } catch (Exception e) {
            return null;
        }

    }

    public static String saveImageItemBonusShare(final String imageUrl, final int itemId) {
        // Store image to default external storage directory
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/resources/" + itemId);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, fileName);
            if (!myDir.exists()) {

                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            return Uri.fromFile(myDir).toString();
        } catch (Exception e) {
            return null;
        }

    }

    public static boolean saveIconItem(SqliteHelper sqliteHelper, final String imageUrl, final int itemId) {
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/resources/" + itemId);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, fileName);
            if (!myDir.exists()) {
                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            bmpUri = Uri.fromFile(myDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmpUri != null) {
            sqliteHelper.updateIconItem(itemId, bmpUri.toString());
            return true;
        }
        return false;

    }

    public static void clearNotificationDetectBeacon(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Global.NOTIFICATION_ID);
    }

    public static String formatNumber(int number) {
        String s = NumberFormat.getNumberInstance(Locale.US).format(number);
        return s;
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isBluetooth(){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {

            if (mBluetoothAdapter.isEnabled()) {
                return true;
            }
        }
        return false;
    }


    public static void showNotification(Context context, String title, String content, String id) {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.monkey_icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSound(soundUri)
                        .setAutoCancel(true);

        Intent intent = new Intent(context, MailDetailActivity.class);
        intent.putExtra("MAIL_ID", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        try {
            mNotificationManager.notify(Integer.valueOf(id), mBuilder.build());
        } catch (Exception e) {
            mNotificationManager.notify(Global.NOTIFICATION_ID, mBuilder.build());
        }
        vibrator(context);
    }

    public static void vibrator(Context context) {
        try {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        } catch (Exception ex) {
        }
    }
    public static String convertDateTimeToString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        String time = sdf.format(c.getTime());
        return time;
    }

    public static String getDateFromDateString(String date){
        return date.split(" ")[0];
    }
    public static boolean dateAndDate(String date1,String date2){
        if(getDateFromDateString(date1).equals(getDateFromDateString(date2))){
            return true;
        }else {
            return false;
        }
    }
    /***

     * @return
     */
    public static Date add1Day() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 24);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = sdf.format(c.getTime());
//        return time;
        return c.getTime();
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    } public static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }
}
