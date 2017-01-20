package com.neu.tomi.ultity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.neu.tomi.BuildConfig;
import com.neu.tomi.R;
import com.neu.tomi.object.FoodObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.ItemQTY;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.view.dialog.UnlockDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Thep on 10/18/2015.
 */
public class DataItems {
//    public static final String LINK_API = "http://neublick.com/tomi";//"http://neublick.com/tomi/test";
        public static final String LINK_API = "http://neublick.com/tomi/test";
    public static final String PROMOTION_ID_KEY = "PROMOTION_ID";
    public static final String INFO_FILE_NAME = "info.txt";
    public static final String BEACON_ID_KEY = "BEACON_ID";
    public static final String PROMOTION_NAME_KEY = "PROMOTION_NAME";
    public static final String LAST_TIME_LOGIN = "LAST_TIME_LOGIN";
    public static final String PROMOTION_IMAGE_KEY = "PROMOTION_IMAGE";
    public static final String PROMOTION_EXPIRY_KEY = "PROMOTION_EXPIRY";
    public static final String PROMOTION_DESCRIPTION_KEY = "PROMOTION_DESCRIPTION";
    public static final String PROMOTION_NOTE_KEY = "PROMOTION_NOTE";
    public static final String PROMOTION_REDIRECT_KEY = "PROMOTION_REDIRECT";
    public static final String STATUS_REDIRECT_KEY = "STATUS_REDIRECT";
    public static final String GAME_LINK_KEY = "GAME_LINK";
    public static final String GAME_NAME_KEY = "GAME_NAME";
    public static final String PROMOTION_TYPE_CODE_KEY = "PROMOTION_TYPE_CODE";
    public static final String PROMOTION_SHOW_URL_KEY = "PROMOTION_SHOW_URL";
    public static final String THEME_ITEM_KEY = "THEME_ITEM";
    public static final String EAT_ITEM_KEY = "EAT_ITEM";
    public static final String DAILY_ITEM_KEY = "DAILY_ITEM";
    public static final String BAG_ITEM_KEY = "BAG_ITEM";
    public static final String EXTRA_BONUS_TYPE_KEY = "EXTRA_BONUS_TYPE";
    private static final String BEACON_DETECT_LIST_KEY = "BEACON_DETECT";
    private static final String BEACON_ACTIVE_LIST_KEY = "BEACON_ACTIVE";
    private static final String BAG_WIDGET = "BAG_WIDGET_";
    private final String NAME_FILE_CHECK_LOST_DATA = "system_check.sys";
    public static final String NAME_FILE_USER_ID = "user.sys";
    private final String IMAGE_SAVE = "IMAGE_SAVE";
    private final String LOST_DATA = "LOST_DATA";
    private final String POINT_XP_SAVE = "POINT_XP_SAVE";
    private final String POINT_SUB = "POINT_SUB";
    private final String XP_SAVE = "XP_SAVE";
    private final String FIRST_BONUS = "FIRST_BONUS";
    private final String FIRST_SETUP = "FIRST_SETUP";
    private final String FIRST_BONUS_ITEM = "FIRST_BONUS_ITEM";
    private final String FIRST_BONUS_SHOW = "FIRST_BONUS_SHOW";
    private final String LAST_TIME_CHECK = "LAST_TIME_CHECK";
    private final String LAST_TIME_SEND_ACTION = "LAST_TIME_SEND_ACTION";
    private final String LAST_TIME_ADD_FOOD = "LAST_TIME_ADD_FOOD";
    private final String ACTION_LIST_SAVE = "ACTION_LIST_SAVE";
    private final String DETECT_LIST_SAVE = "DETECT_LIST_SAVE";
    private final String VIEW_LIST_SAVE = "VIEW_LIST_SAVE";
    private final String USE_ITEM_LIST_SAVE = "USE_ITEM_LIST_SAVE";
    private final String CURRENT_VERSION = "CURRENT_VERSION";
    private final String STATUS_REGISTER_GCM = "STATUS_REGISTER_GCM";
    private final String KEY_SENDER_GCM = "KEY_SENDER_GCM";
    private final String IS_NEW_NAME = "IS_NEW_NAME";
    private final String LAST_TIME_EXPIRE = "LAST_TIME_EXPIRE";
    private final String NAME_OF_USER_KEY = "NAME_OF_USER";
    private final String LOGIN_GMAIL_STATE_KEY = "LOGIN_GMAIL_STATE";
    private final String LOGIN_FACE_STATE_KEY = "LOGIN_FACE_STATE";
    private final String HELP_FIRST_KEY = "HELP_FIRST";
    private final String PHONE_ID_KEY = "PHONE_ID";
    private final String UPDATE_VERSION_41_KEY = "UPDATE_VERSION_41";
    private final String UPDATE_VERSION_48_KEY = "UPDATE_VERSION_48";
    private final String UPDATE_INFO_KEY = "UPDATE_INFO_";
    private final String LOCATION_A_KEY = "LOCATION_A";
    private final String LOCATION_B_KEY = "LOCATION_B";
    private final String MESAGE_DEMO_KEY = "MESAGE_DEMO";
    private final String CONSECUTIVE_DAYS = "CONSECUTIVE_DAYS";
    private final String LAST_DAY_CONSECUTIVE = "LAST_DAY_CONSECUTIVE";
    private final String MONTH_OF_DAILY_LOGIN = "MONTH_OF_DAILY_LOGIN";
    private final String YEAR_OF_DAILY_LOGIN = "YEAR_OF_DAILY_LOGIN";
    private final String EVENT_ID_OF_DAILY_LOGIN = "EVENT_ID_OF_DAILY_LOGIN";
    private final String ITEMS_OF_DAILY_LOGIN = "ITEMS_OF_DAILY_LOGIN";
    private final String LAST_DAY_SHOW_DAILY_LOGIN = "LAST_DAY_SHOW_DAILY_LOGIN";
    private final String LAST_DAY_CHECK_VERSION = "LAST_DAY_CHECK_VERSION";
    private final String IS_SOCIAL_LOGIN = "IS_SOCIAL_LOGIN";
    private final String USER_ID_FG = "USER_ID_FG";
    private final String LAST_BAG = "LAST_BAG";

    private final String ACTION_DAILY_MAX = "ACTION_DAILY_";
    private final String ACTION_DAILY_DATE = "ACTION_DAILY_DATE";

    public static final String ID_ACTION_GRAB = "146";
    public static final String ID_ACTION_USE = "147";
    public static final String ID_ACTION_SHARE = "148";
    public static final String ID_ACTION_DELETE = "149";

    public static final int bananasId = 1;
    public static final int orangeId = 2;
    public static final int bottleId = 3;
    public static final int tambourineId = 4;
    public static final int bag1Id = 5;
    public static final int bag2Id = 6;
    public static final int bag3Id = 7;
    public static final int bag4Id = 8;
    public static final int bag5Id = 9;
    public static final int bag6Id = 10;
    public static final int bag7Id = 12;
    public static final int bag8Id = 16;
    public static final int bag9Id = 17;//chua update
    public static final int bag10Id = 18;
    public static final int bag11Id = 20;
    public static final int bag12Id = 22;
    public static final int bag13Id = 19;
    public static final int fourLeafCloverId = 21;
    public static final int themeId1 = 11;
    public static final int iceCreamId = 13;
    public static final int peanutId = 14;
    public static final int watermelonId = 15;
    public static final int ID_RESOURCE_MAX = 25;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SqliteHelper mSqliteHelper;

    public DataItems(Context context) {
        mContext = context;
        mSqliteHelper = SqliteHelper.getInstanceSQLiteHelper(mContext);
        String PACKAGE_NAME = context.getApplicationContext().getPackageName();
        mSharedPreferences = context.getSharedPreferences(PACKAGE_NAME + "ITEM_SAVES", Context.MODE_PRIVATE);
        if (mSharedPreferences.getBoolean("FIRST_TIME", true)) {
            addFirstTheme();
            addFirstBags();
            addFirstFood();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("FIRST_TIME", false);
            editor.apply();
        }
    }

    public void clearDataShareReferences() {
        boolean isAddWidget = isHelpFirst();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
        if(isAddWidget){
            setStateHelp(true);
        }

    }

    public boolean isLoadFirstRun() {
        return mSharedPreferences.getBoolean(FIRST_BONUS, false);
    }

    public void setLoadFirstRun() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_BONUS, true);
        editor.apply();
    }

    public boolean isFirstSetup() {
        return mSharedPreferences.getBoolean(FIRST_SETUP, false);
    }

    public void setFirstSetup() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_SETUP, true);
        editor.apply();
    }

    public void setFirstShow() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_BONUS_SHOW, true);
        editor.apply();
    }

    public boolean getFirstShow() {
        return mSharedPreferences.getBoolean(FIRST_BONUS_SHOW, false);
    }

    public void setSateLoadFirstRun(String jsonData) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_BONUS, true);
        editor.putString(FIRST_BONUS_ITEM, jsonData);
        editor.apply();
    }

    public String getDataFirstRun() {
        return mSharedPreferences.getString(FIRST_BONUS_ITEM, "");
    }

    public void setPoint(int pointAndXp, int xp, int subPoint) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(POINT_XP_SAVE, pointAndXp);
        editor.putInt(XP_SAVE, xp);
        editor.putInt(POINT_SUB, subPoint);
        if (!existFileCheck()) {
            saveFileCheck();
        }
        editor.apply();
    }

    public void addPoint(int pts, int xp) {
        pts = pts * (-1);
        if (!existFileCheck()) {
            saveFileCheck();
        }
        addOnlyXP(xp);
        subPoints(pts);
    }

    public boolean isLostData() {
        if (serverDetectLostData()) {
            return true;
        } else {
            return false;//((getTotalXP() + getIntBalancePoint()) == 0 && existFileCheck());
        }
    }

    public boolean serverDetectLostData() {
        return mSharedPreferences.getBoolean(LOST_DATA, false);
    }

    public void setServerDetectLostData(boolean exist) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LOST_DATA, exist);
        editor.apply();
    }

    public void deleteFileCheck() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/tomi_image", NAME_FILE_CHECK_LOST_DATA);
        if (file.exists())
            file.delete();
    }

    private boolean existFileCheck() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/tomi_image", NAME_FILE_CHECK_LOST_DATA);
        return file.exists();
    }

    private void saveFileCheck() {
        try {
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, NAME_FILE_CHECK_LOST_DATA);
            FileOutputStream out = new FileOutputStream(myDir);
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            outWriter.append("%jwi19ksw^&jsk");
            outWriter.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFileBug(String url, String json) {
        try {
            File myDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, "BUG.txt");
            FileOutputStream out = new FileOutputStream(myDir);
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            if (json == null) {
                json = "NULL";
            }
            outWriter.append(url + "\n" + json);
            outWriter.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addPointAndXP(int point) {
        int currentPoint = getPointAndXP();
        int currentLevel = getCurrentLevel();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(POINT_XP_SAVE, currentPoint + point);
        editor.apply();
        int newLevel = getCurrentLevel();
        if (currentLevel < newLevel) {
            showDialogUnlock(newLevel);
        }
    }

    private void addOnlyXP(int point) {
        int currentPoint = getOnlyXP();
        int currentLevel = getCurrentLevel();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(XP_SAVE, currentPoint + point);
        editor.apply();
        int newLevel = getCurrentLevel();
        if (currentLevel < newLevel) {
            showDialogUnlock(newLevel);
        }
    }

    //Ap dung cho point tieu phi va point + them ko tinh vao xp ( neu mang gia tri am)
    private void subPoints(int point) {
        int currentPoint = getSubPoint();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(POINT_SUB, currentPoint + point);
        editor.apply();
    }


    public int getSubPoint() {
        return mSharedPreferences.getInt(POINT_SUB, 0);
    }

    public int getPointAndXP() {
        return mSharedPreferences.getInt(POINT_XP_SAVE, 0);
    }

    public int getOnlyXP() {
        return mSharedPreferences.getInt(XP_SAVE, 0);
    }

    public int getTotalXP() {
        return getPointAndXP() + getOnlyXP();
    }

    //So point se hien thi
    public String getBalancePoint() {
        return NumberFormat.getNumberInstance(Locale.US).format(getPointAndXP() - getSubPoint());
    }

    public int getIntBalancePoint() {
        return getPointAndXP() - getSubPoint();
    }


    public List<FoodObject> getFoodItemList() {
        List<FoodObject> itemObjects = mSqliteHelper.getFoodItems();
        return itemObjects;
    }

    public List<ItemObject> getDailyItemList() {

        List<ItemObject> itemObjects = new ArrayList<>();
        if (getCurrentLevel() >= 2)
            itemObjects.add(getItem(Integer.valueOf(bottleId)));
        if (getCurrentLevel() >= 6)
            itemObjects.add(getItem(Integer.valueOf(tambourineId)));
        String stringItems = getStringListItem(DAILY_ITEM_KEY);
        if (!stringItems.isEmpty()) {
            String[] items = stringItems.split(";");
            for (String item : items) {
                itemObjects.add(getItem(Integer.valueOf(item)));
            }
        }

        return itemObjects;
    }


    public List<ItemObject> getBagItemList() {
        List<ItemObject> itemObjects = new ArrayList<>();
        String stringItems = getStringListItem(BAG_ITEM_KEY);//+";"+bag8Id+";"+bag9Id+";"+bag10Id+";"+bag11Id;
        if (SqliteHelper.DATABASE_VERSION >= 8)
            if (!stringItems.isEmpty()) {
                {
                    if (!stringItems.startsWith(bag1Id + ";" + bag2Id + ";" + bag13Id + ";" + bag7Id)) {
                        if (!stringItems.startsWith(bag1Id + ";" + bag2Id + ";" + bag13Id))
                            stringItems = stringItems.replace(bag1Id + ";" + bag2Id, bag1Id + ";" + bag2Id + ";" + bag13Id + ";" + bag7Id);
                        else
                            stringItems = stringItems.replace(bag1Id + ";" + bag2Id + ";" + bag13Id, bag1Id + ";" + bag2Id + ";" + bag13Id + ";" + bag7Id);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(BAG_ITEM_KEY, stringItems);
                        editor.apply();
                    }
                }
                String[] items = stringItems.split(";");
                for (String item : items) {
                    itemObjects.add(getItem(Integer.valueOf(item)));
                }
            }
        if (getCurrentLevel() >= 3)
            itemObjects.add(getItem(Integer.valueOf(bag3Id)));
        if (getCurrentLevel() >= 5)
            itemObjects.add(getItem(Integer.valueOf(bag4Id)));
        if (getCurrentLevel() >= 7)
            itemObjects.add(getItem(Integer.valueOf(bag5Id)));
        if (getCurrentLevel() >= 9)
            itemObjects.add(getItem(Integer.valueOf(bag6Id)));
        List<ItemObject> tmp = new ArrayList<>();
        for (ItemObject object : itemObjects) {
            boolean valid = true;
            for (ItemObject object1 : tmp) {
                if (object.getId() == object1.getId()) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                tmp.add(object);
            }
        }
        return tmp;
    }

    public String getListBeaconDetect() {
        return mSharedPreferences.getString(BEACON_DETECT_LIST_KEY, "");
    }

    public String getListBeaconActive() {
        return mSharedPreferences.getString(BEACON_ACTIVE_LIST_KEY, "");
    }

    public String getListBeacon() {
        String[] detects = getListBeaconDetect().split(";");
        String[] actives = getListBeaconActive().split(";");
        String beaconList = "";
//        Toast.makeText(mContext,"MISS: "+getListBeaconDetect()+"\nDETECT: "+getListBeaconActive(),Toast.LENGTH_LONG).show();
        for (int i = 0; i < detects.length; i++) {
            boolean validBeacon = true;
            for (int j = 0; j < actives.length; j++) {
                if (detects[i].equals(actives[j])) {
                    validBeacon = false;
                    break;
                }
            }
            if (validBeacon) {
                String tmp = detects[i];// URLEncoder.encode(detects[i], "UTF-8");
                if (beaconList.isEmpty()) {
                    beaconList += tmp;
                } else {
                    beaconList += "," + tmp;
                }
            }
        }
        resetBeaconList(beaconList);
        return beaconList;
    }


    public void addBeaconDetect(String beacon) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = getListBeaconDetect();

        String[] listNewBeacon = beacon.split(",");
        for (int i = 0; i < listNewBeacon.length; i++) {
            stringItems = stringItems.replace(listNewBeacon[i] + ",", "");
            stringItems = stringItems.replace("," + listNewBeacon[i], "");
            stringItems = stringItems.replace(listNewBeacon[i], "");
        }

        String[] listBeacon = stringItems.split(",");
        int lengthNewBeacon = listNewBeacon.length;
        if ((listBeacon.length + lengthNewBeacon) > 9) {
            stringItems = beacon;
            for (int i = 0; i < (10 - lengthNewBeacon); i++) {
                stringItems += "," + listBeacon[i];
            }
        } else {
            if (stringItems.isEmpty())
                stringItems = beacon;
            else {
                stringItems = beacon + "," + stringItems;
            }
        }
        editor.putString(BEACON_DETECT_LIST_KEY, stringItems);
        editor.apply();
    }

    public void resetBeaconList(String beaconDetect) {
        String listData = beaconDetect.replace(",", ";");
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(BEACON_DETECT_LIST_KEY, listData);
        editor.putString(BEACON_ACTIVE_LIST_KEY, "");
        editor.apply();
    }

    public void addBeaconActive(String beacon) {
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        String stringItems = getListBeaconActive();
//        beacon = beacon.replace(",",";");
//        if (stringItems.isEmpty())
//            stringItems = beacon;
//        else {
//            stringItems += ";" + beacon;
//        }
//        editor.putString(BEACON_ACTIVE_LIST_KEY, stringItems);
//        editor.apply();
    }

//    public void removeBeaconWithID(String beacon) {
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        String stringItems = getListBeaconDetect();
//        if (!stringItems.isEmpty()) {
//            stringItems = stringItems.replace("," + beacon, "");
//            stringItems = stringItems.replace(beacon + ",", "");
//            stringItems = stringItems.replace(beacon, "");
//            editor.putString(BEACON_DETECT_LIST_KEY, stringItems);
//            editor.apply();
//        }
//    }

    public void removeBeacon(int position) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = getListBeaconDetect();
        Log.e("BEACON", stringItems);
        int LENGTH_ITEM = stringItems.length();
        if (LENGTH_ITEM < 2) {
            stringItems = "";
        } else if (position == 0) {
            stringItems = stringItems.substring(2);
        } else {
            int endIndex1 = position * 2;
            if (endIndex1 >= (stringItems.length() - 1)) {
                stringItems = stringItems.substring(0, endIndex1);
            } else {
                String tmp = stringItems;
                stringItems = stringItems.substring(0, endIndex1);
                stringItems += tmp.substring(endIndex1 + 2);
            }
        }
        Log.e("STRING_LIST", stringItems);
        editor.putString(BEACON_DETECT_LIST_KEY, stringItems);
        editor.apply();
    }


    public void addItemToList(String ITEM_KEY, int item) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = getStringListItem(ITEM_KEY);
        if (ITEM_KEY.equals(EAT_ITEM_KEY)) {

//            if (item == bananasId) {
//                FoodObject foodObject = mSqliteHelper.getFoodItem(bananasId);
//                if (foodObject.getQty() > 9) {
////                    setLastTimeAddBananaBonus(-1);
//                    return;
//                }
//            }
            mSqliteHelper.updateFood(item, 1);
        }
        if (stringItems.isEmpty()) {
            stringItems = String.valueOf(item);
        } else {
            stringItems += ";" + item;
        }
        editor.putString(ITEM_KEY, stringItems);
        editor.apply();
    }

    public void addItemToList(String ITEM_KEY, int item, int qty) {
        for (int i = 0; i < qty; i++) {
            addItemToList(ITEM_KEY, item);
        }
    }

    public void updateFoodItem(int item, int qty) {
        mSqliteHelper.updateFoodFirst(item, qty);
    }

    public void addItemToListByID(int item, int qty) {
        String itemKey = getKeyItem(item);
        for (int i = 0; i < qty; i++) {
            addItemToList(itemKey, item);
        }
    }

    private String getKeyItem(int item) {
        switch (item) {
            case bag1Id:
                return BAG_ITEM_KEY;
            case bag2Id:
                return BAG_ITEM_KEY;
            case bag3Id:
                return BAG_ITEM_KEY;
            case bag4Id:
                return BAG_ITEM_KEY;
            case bag5Id:
                return BAG_ITEM_KEY;
            case bag6Id:
                return BAG_ITEM_KEY;
            case bag7Id:
                return BAG_ITEM_KEY;
            case bag8Id:
                return BAG_ITEM_KEY;
            case bag9Id:
                return BAG_ITEM_KEY;
            case bag10Id:
                return BAG_ITEM_KEY;
            case bag11Id:
                return BAG_ITEM_KEY;
            case bag12Id:
                return BAG_ITEM_KEY;
            case bag13Id:
                return BAG_ITEM_KEY;
            case bottleId:
                return DAILY_ITEM_KEY;
            case tambourineId:
                return DAILY_ITEM_KEY;
            default:
                return EAT_ITEM_KEY;
        }
    }


    public void removeItemToList(String ITEM_KEY, int position) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = getStringListItem(ITEM_KEY);
        int LENGTH_ITEM = stringItems.length();
        if (LENGTH_ITEM < 2) {
            stringItems = "";
        } else if (position == 0) {
            stringItems = stringItems.substring(2);
        } else {
            int endIndex1 = position * 2;
            if (endIndex1 >= (stringItems.length() - 1)) {
                stringItems = stringItems.substring(0, endIndex1);
            } else {
                String tmp = stringItems;
                stringItems = stringItems.substring(0, endIndex1);
                stringItems += tmp.substring(endIndex1 + 2);
            }
        }
        editor.putString(ITEM_KEY, stringItems);
        editor.apply();
    }

    private void addFirstTheme() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = String.valueOf(themeId1);
        editor.putString(THEME_ITEM_KEY, stringItems);
        editor.apply();
    }

    private void addFirstBags() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = bag1Id + ";" + bag2Id + ";" + bag13Id + ";" + bag7Id;
        editor.putString(BAG_ITEM_KEY, stringItems);
        editor.apply();
    }

    private void addFirstFood() {

        mSqliteHelper.updateFoodFirst(bananasId, 4);
//        mSqliteHelper.updateFoodFirst(orangeId, 4);
//        mSqliteHelper.updateFoodFirst(watermelonId, 2);
//        mSqliteHelper.updateFoodFirst(iceCreamId, 2);
//        mSqliteHelper.updateFoodFirst(peanutId, 2);
    }

    private void addFirstDailyItem() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = "";
        editor.putString(DAILY_ITEM_KEY, stringItems);
        editor.apply();
    }

    public void addDailyItem(int id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = getStringListItem(DAILY_ITEM_KEY);
        if (stringItems.isEmpty()) {
            stringItems = String.valueOf(id);
        } else {
            stringItems += ";" + id;
        }
        editor.putString(DAILY_ITEM_KEY, stringItems);
        editor.apply();
    }

    private String getStringListItem(String ITEM_KEY) {
        return mSharedPreferences.getString(ITEM_KEY, "");
    }

    public ItemObject getItem(int id) {
        List<ItemObject> itemObjects = mSqliteHelper.getAllItem();
        for (ItemObject itemObject : itemObjects) {
            if (itemObject.getId() == id) {
                return itemObject;
            }
        }
        return null;
//                return new ItemObject(mContext,id, Global.TOMI_ACTION_EAT, "Banana","Banana +2 XP for tomi",0,1,1,R.drawable.item_banana);
    }

    public void setBagWidget(int widgetId, int itemId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(BAG_WIDGET + widgetId, itemId);
        editor.apply();
    }

    //    public String encodeBitmapToString(Bitmap bitmap){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
//        byte[] b = baos.toByteArray();
//        return Base64.encodeToString(b, Base64.DEFAULT);
//    }
//
//    public  Bitmap decodeStringToBitmap(String bitmapEncode, int resourceDefault){
//        Bitmap bitmap;
//        if(bitmapEncode.isEmpty()){
//            bitmap= BitmapFactory.decodeResource(mContext.getResources(), resourceDefault);
//            return bitmap;
//        }else {
//            try {
//                byte[] imageAsBytes = Base64.decode(bitmapEncode.getBytes(),Base64.DEFAULT);
//                bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//            }catch (Exception e){
//                bitmap= BitmapFactory.decodeResource(mContext.getResources(), resourceDefault);
//                return bitmap;
//            }
//        }
//        return bitmap;
//    }
    public int getBagWidget(int widgetId) {
        return mSharedPreferences.getInt(BAG_WIDGET + widgetId, getLastBagSelect());
    }

    public void updatePointUseItem(ItemObject itemObject) {
        addUseItemToList(itemObject.getId());
        int xp = itemObject.getPointType();
        int pts = itemObject.getPoint();
        addPoint(pts, xp);
    }

    private static String getName(String imageURL) {
        String name = imageURL;
        int lastIndexDot = imageURL.lastIndexOf(".");
        if (lastIndexDot > 0)
            name = imageURL.substring(0, lastIndexDot);
        name = name.substring(name.lastIndexOf('/') + 1);
        name = name.replace(";", "");
//        String name= URLUtil.guessFileName(imageURL, null, null);
        return name;
    }

    private void saveImage(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = mSharedPreferences.getString(IMAGE_SAVE, "");
        if (stringItems.isEmpty()) {
            stringItems = String.valueOf(name);
        } else {
            stringItems += ";" + name;
        }
        editor.putString(IMAGE_SAVE, stringItems);
        editor.apply();
    }

    public void addTheme(int id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String stringItems = getTheme();
        if (stringItems.isEmpty()) {
            stringItems = String.valueOf(id);
        } else {
            stringItems += ";" + id;
        }
        editor.putString(THEME_ITEM_KEY, stringItems);
        editor.apply();
    }

    public String getTheme() {
        return mSharedPreferences.getString(THEME_ITEM_KEY, "");
    }


    public static Bitmap viewToBitmap(View view) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


    public Uri getLocalShareBitmapUri(View imageView, String imageURL) {
        // Extract Bitmap from ImageView drawable
//        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = viewToBitmap(imageView);
        // Store image to default external storage directory
        Uri bmpUri = null;
        String nameImage = getName(imageURL);
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/share", nameImage + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bmpUri != null)
            saveImage(nameImage);
        return bmpUri;
    }

    public Uri getLocalBitmapUri(ImageView imageView, String imageURL) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        String nameImage = imageURL.substring(imageURL.lastIndexOf('/') + 1);//getName(imageURL);
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image", nameImage);
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bmpUri != null)
            saveImage(nameImage);
        return bmpUri;
    }

    public Uri getLocalBitmapUri(Bitmap bmp, String imageURL) {
        Uri bmpUri = null;
        String nameImage = getName(imageURL);
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image", nameImage);
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bmpUri != null)
            saveImage(nameImage);
        return bmpUri;
    }

    public static boolean isSave(String imageURL) {
        String nameImage = getName(imageURL);
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/tomi_image", nameImage + ".png");
        return file.exists();
    }

    public static Uri getDefaultUriScript(int id, String nameImage) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/resources/" + id, nameImage + ".png");

        if (file.exists()) {
            return Uri.fromFile(file);
        } else {
            return null;
        }
    }


    public static JSONObject Rename(Context context, String name) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        try {
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Application&act=EditName&phone_id=" + phoneId + "&name=" + name;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject CheckVersion(Context context) {
        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Application";
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject DailyLoginCheckedIsValid(Context context, int eventId) {
        String phoneId = getPhoneId(context);
        String brand = Build.MANUFACTURER.toUpperCase();
        String model = Build.MODEL.toUpperCase();
        if (model.startsWith(brand)) {
            model = model.replace(brand, "").trim();

        }
        try {
            brand = URLEncoder.encode(brand, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            model = URLEncoder.encode(model, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String os = "&os=Android&version=" + Build.VERSION.SDK_INT + "&brand=" + brand + "&model=" + model;

        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Application&act=DailyLogin&phone_id=" + phoneId + "&eventid=" + eventId + os;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
//        json="{\"day\":\"10\",\"state\":\"true\", \"qty\":1,\"id\":1,\"name\":\"Banana\",\"description\":\"Tomi love bananas\",\"action_id\":\"TOMI_ACTION_EAT\",\"image_url\":\"http://neublick.com/tomi/test/cms/gallery/1_icon.png\",\"point\":5,\"point_type\":5,\"max_action\":10,\"script_list\":\"\",\"script_text\":\"\",\"image_list\":[]}";
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject GetDataRestore(Context context) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Synchronize&phone_id=" + phoneId;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject GrabPromotionRequest(Context context, String promotionId, String beaconID) {
        String phoneId = getPhoneId(context);

        try {
            beaconID = URLEncoder.encode(beaconID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&phone_id=" + phoneId + "&act=Grab&promotion_id=" + promotionId + "&beacon_id=" + beaconID;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject UseCode(Context context, String code) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        try {
            code = URLEncoder.encode(code, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Codes&phone_id=" + phoneId + "&act=Use&code=" + code;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
//        json="{\"state\":\"true\", \"error_description\":\"OK\", \"point\":10, \"point_type\":10}";
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject UsePromotion(Context context, String promotionID, String code, String beaconID, int codeType) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&phone_id=" + phoneId + "&act=Use&promotion_id=" + promotionID + "&code=" + code + "&beacon_id=" + beaconID + "&code_type=" + codeType;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        try {
            Log.e("RESPONSE_SERVER", json);
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject BuyItem(Context context, int itemId) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Shop&act=Buy&phone_id=" + phoneId + "&item_id=" + itemId;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("URL__", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject getShopData(Context context) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        DataItems dataItems = new DataItems(context);
        String actionList = dataItems.getActionList();
        String useList = dataItems.getUseItemList();
        try {
            actionList = URLEncoder.encode(actionList, "UTF-8");
            useList = URLEncoder.encode(useList, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Shop&act=Show&phone_id=" + phoneId + "&action_list=" + actionList + "&use_list=" + useList;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject getEventUpdate(Context context) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        SqliteHelper sqliteHelper = SqliteHelper.getInstanceSQLiteHelper(context);
        List<MailObject> mailObjects = sqliteHelper.getAllMail();
        String queryString = "";
        for (MailObject mailObject : mailObjects) {
            queryString += mailObject.getId() + ",";
        }
        if (!queryString.isEmpty())
            queryString = queryString.substring(0, queryString.length() - 1);
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Notifies&phone_id=" + phoneId + "&act=Query&query_string=" + queryString;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    /**
     * @param context
     * @param socialId   id social
     * @param socialType 1 google, 2 facebook
     * @return
     */
    public static JSONObject LoginSocialSendSync(Context context, String socialId, int socialType, String avatar, String name, String birthday, int sex) {
        String phoneId = getPhoneId(context);
        JSONObject jsonObject = null;
        try {
            socialId = URLEncoder.encode(socialId, "UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Social&act=Log_in&phone_id=" + phoneId + "&social_id=" + socialId + "&social_type=" + socialType + "&avatar=" + avatar + "&display_name=" + name
                + "&birthday=" + birthday + "&sex=" + sex;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject getFirstRunService(String socialId, String avatar, String name, String birthday, int sex, Context context,String email) {
        String brand = Build.MANUFACTURER.toUpperCase();
        String model = Build.MODEL.toUpperCase();
        if (model.startsWith(brand)) {
            model = model.replace(brand, "").trim();

        }
        try {
            socialId = URLEncoder.encode(socialId, "UTF-8");
            name = URLEncoder.encode(name, "UTF-8");
            avatar = URLEncoder.encode(avatar, "UTF-8");
            birthday = URLEncoder.encode(birthday, "UTF-8");
            brand = URLEncoder.encode(brand, "UTF-8");
            model = URLEncoder.encode(model, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String os = "&os=Android&version=" + Build.VERSION.SDK_INT + "&brand=" + brand + "&model=" + model;
        String acc = "&avatar=" + avatar + "&display_name=" + name + "&birthday=" + birthday + "&sex=" + sex+"&email="+email;
        JSONObject jsonObject = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Users&act=Sign_up&phone_id=" + socialId + os + acc + "&old_id=" + getIMEIId(context);
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject getFirstRunService(String email, String password, String name, Context context) {
        String brand = Build.MANUFACTURER.toUpperCase();
        String model = Build.MODEL.toUpperCase();
        if (model.startsWith(brand)) {
            model = model.replace(brand, "").trim();

        }
//        try {
//            password = Global.md5(URLEncoder.encode(password, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//
//        }
        JSONObject jsonObject =null;
        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Users");
        hashMap.put("act", "Sign_up");
        hashMap.put("phone_id", email);
        hashMap.put("os", "Android");
        hashMap.put("brand", brand);
        hashMap.put("model", model);
        hashMap.put("avatar", "");
        hashMap.put("display_name", "");
        hashMap.put("birthday", "");
        hashMap.put("sex", "0");
        hashMap.put("email", email);
        hashMap.put("password", password);
        hashMap.put("name", name);
        hashMap.put("version", String.valueOf(Build.VERSION.SDK_INT));

        String json = performPostCall(url, hashMap);


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }


        return jsonObject;
    }
    public static JSONObject updateInfoGift(String name,String email, String phone, Context context) {
        String phoneId = getPhoneIdNoEncode(context);
        JSONObject jsonObject =null;
        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Users");
        hashMap.put("act", "Update_contact");
        hashMap.put("phone_id", phoneId);
        hashMap.put("name", name);
        hashMap.put("email", email);
        hashMap.put("mobile", phone);
        String json = performPostCall(url, hashMap);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    public static JSONObject signInWithEmailPassword(String email, String password, Context context) {
//        try {
//            password = Global.md5(URLEncoder.encode(password, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//
//        }
        JSONObject jsonObject =null;
        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        //=Users&act=Login&phone_id=thep@hotmail.com&email=thep@hotmail.com&password=123456
        hashMap.put("Object", "Users");
        hashMap.put("act", "Login");
        hashMap.put("phone_id", email);
        hashMap.put("email", email);
        hashMap.put("password", password);

        String json = performPostCall(url, hashMap);

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }


        return jsonObject;
    }

    public static JSONObject getPassword(String email, Context context) {

        JSONObject jsonObject =null;
        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        //Object=Users&act=Forget_pass&phone_id=thep@hotmail.com&email=thep@hotmail.com
        hashMap.put("Object", "Users");
        hashMap.put("act", "Forget_pass");
        hashMap.put("phone_id", email);
        hashMap.put("email", email);

        String json = performPostCall(url, hashMap);

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }


        return jsonObject;
    }

    public static JSONObject getPassword( Context context,String oldPassword, String newPassword) {

        JSONObject jsonObject =null;
        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        //Object=Users&act=Change_pass&phone_id=thep@hotmail.com&email=thep@hotmail.com&old_password=123456&new_password=654321
        hashMap.put("Object", "Users");
        hashMap.put("act", "Change_pass");
        hashMap.put("phone_id", getPhoneIdNoEncode(context));
        hashMap.put("email", getPhoneIdNoEncode(context));
        hashMap.put("old_password", oldPassword);
        hashMap.put("new_password", newPassword);
        String json = performPostCall(url, hashMap);

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }


        return jsonObject;
    }

    public JSONObject sendAction() {
        String phoneId = getPhoneId(mContext);
        JSONObject jsonObject = null;

        String actionList = mSharedPreferences.getString(ACTION_LIST_SAVE, "");
        String detectList = mSharedPreferences.getString(DETECT_LIST_SAVE, "");
        String useList = mSharedPreferences.getString(USE_ITEM_LIST_SAVE, "");
        String viewList = mSharedPreferences.getString(VIEW_LIST_SAVE, "");
        if (actionList.isEmpty() && useList.isEmpty() && viewList.isEmpty())
            return null;
        try {
            actionList = URLEncoder.encode(actionList, "UTF-8");
            useList = URLEncoder.encode(useList, "UTF-8");
            viewList = URLEncoder.encode(viewList, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=info&phone_id=" + phoneId + "&action_list=" + actionList + "&use_list=" + useList + "&view_list=" + viewList + "&detect_list=" + detectList;
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER_URL", url);
        Log.e("RESPONSE_SERVER", json);

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        if (jsonObject != null) {
            try {
                if (jsonObject.getBoolean("state")) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(DETECT_LIST_SAVE, "");
                    editor.putString(ACTION_LIST_SAVE, "");
                    editor.putString(USE_ITEM_LIST_SAVE, "");
                    editor.putString(VIEW_LIST_SAVE, "");
                    editor.apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    public JSONObject sendActionPost() {
        String phoneId = getPhoneIdNoEncode(mContext);
        JSONObject jsonObject = null;

        String actionList = mSharedPreferences.getString(ACTION_LIST_SAVE, "");
        String detectList = mSharedPreferences.getString(DETECT_LIST_SAVE, "");
        String useList = mSharedPreferences.getString(USE_ITEM_LIST_SAVE, "");
        String viewList = mSharedPreferences.getString(VIEW_LIST_SAVE, "");
        if (actionList.isEmpty() && useList.isEmpty() && viewList.isEmpty() && detectList.isEmpty())
            return null;
        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "info");
        hashMap.put("phone_id", phoneId);
        hashMap.put("action_list", actionList);
        hashMap.put("use_list", useList);
        hashMap.put("view_list", viewList);
        hashMap.put("detect_list", detectList);
        String json = performPostCall(url, hashMap);


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        if (jsonObject != null) {
            try {
                if (jsonObject.getBoolean("state")) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(DETECT_LIST_SAVE, "");
                    editor.putString(ACTION_LIST_SAVE, "");
                    editor.putString(USE_ITEM_LIST_SAVE, "");
                    editor.putString(VIEW_LIST_SAVE, "");
                    editor.apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }


    public static JSONArray getGameList(Context context) {
        String phoneId = getPhoneIdNoEncode(context);
        JSONArray jsonArray = null;

        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Games");
        hashMap.put("act", "Query");
        hashMap.put("phone_id", phoneId);

        String json = performPostCall(url, hashMap);


        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            return null;
        }

        return jsonArray;
    }
    public JSONObject registerGCM(String token) {
        String phoneId = getPhoneIdNoEncode(mContext);
        JSONObject jsonObject = null;

        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Users");
        hashMap.put("act", "RegNotify");
        hashMap.put("phone_id", phoneId);
        hashMap.put("registrationid", token);
        String json = performPostCall(url, hashMap);


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    public static JSONArray getOurMerchants(Context context) {
        String phoneId = getPhoneIdNoEncode(context);
        JSONArray jsonArray = null;

        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Application");
        hashMap.put("act", "Partner");
        hashMap.put("phone_id", phoneId);

        String json = performPostCall(url, hashMap);


        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            return null;
        }

        return jsonArray;
    }

    public static JSONObject updatePointAndXp(Context context) {
        String phoneId = getPhoneIdNoEncode(context);
        JSONObject jsonObject = null;
        DataItems dataItems = new DataItems(context);
        String actionList = dataItems.getActionList();
        String useList = dataItems.getUseItemList();

        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Users");
        hashMap.put("act", "Query");
        hashMap.put("phone_id", phoneId);
        hashMap.put("action_list", actionList);
        hashMap.put("use_list", useList);
        String json = performPostCall(url, hashMap);


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    public static JSONObject checkDailyLogin(Context context) {
        String phoneId = getPhoneIdNoEncode(context);
        JSONObject jsonObject = null;

        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Application");
        hashMap.put("act", "DailyLogin");
        hashMap.put("phone_id", phoneId);

        String json = performPostCall(url, hashMap);


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    public static JSONObject collectBonusDailyLogin(Context context) {
        String phoneId = getPhoneIdNoEncode(context);
        JSONObject jsonObject = null;

        String url = LINK_API + "/cms/pages/Tommi_API.php";
        HashMap hashMap = new HashMap();
        hashMap.put("Object", "Application");
        hashMap.put("act", "Collect");
        hashMap.put("phone_id", phoneId);
//        hashMap.put("phone_id", "866046028357677");

        String json = performPostCall(url, hashMap);


        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }

        return jsonObject;
    }

    public static JSONArray getDataOfBeaCon(String beaconId, int wid, Context context, SqliteHelper sqliteHelper) {
        String phoneId = getPhoneId(context);
        List<PromtionObject> promtionObjects = sqliteHelper.getAllPromotionWithBeacon(beaconId);
        String promotion = "";
        if (promtionObjects.size() > 0) {
            for (PromtionObject object : promtionObjects) {
                promotion += object.getPromotionId() + ",";
            }
            promotion = promotion.substring(0, promotion.length() - 1);
        }
        try {
            promotion = URLEncoder.encode(promotion, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }


        try {
//            beaconId="28788_201";//major_minor
            beaconId = URLEncoder.encode(beaconId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("URL", e.getMessage());
        }
        JSONArray jsonArray = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&act=Query&info=" + beaconId + "&phone_id=" + phoneId + "&wid=" + beaconId + "&query_string=" + promotion;
        Log.e("RESPONSE_SERVER_URL", url);
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonArray;
    }

    public static JSONObject getDailyLoginForMonth(Context context) {
        String phoneId = getPhoneId(context);
        JSONObject jsonArray = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Application&act=DailyInfo&phone_id=" + phoneId;
        Log.e("RESPONSE_SERVER_URL", url);
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonArray = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonArray;
    }

    public static JSONArray getDataOfMissBeaCon(String beaconId, int wid, Context context, SqliteHelper sqliteHelper) {
        String phoneId = getPhoneId(context);
        List<PromtionObject> promtionObjects = sqliteHelper.getAllPromotionWithBeacon(beaconId);
        String promotion = "";
        if (promtionObjects.size() > 0) {
            for (PromtionObject object : promtionObjects) {
                promotion += object.getPromotionId() + ",";
            }
            promotion = promotion.substring(0, promotion.length() - 1);
        }
        try {
            promotion = URLEncoder.encode(promotion, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }


        JSONObject jsonObject = null;
        try {
//            beaconId="28788_201";//major_minor
            beaconId = URLEncoder.encode(beaconId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("URL", e.getMessage());
        }
        JSONArray jsonArray = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&act=Query_new&info=" + beaconId + "&phone_id=" + phoneId + "&wid=" + beaconId + "&query_string=" + promotion;
        Log.e("RESPONSE_SERVER_URL", url);
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonArray;
    }

    public static JSONArray getPromotionsFirstTime(Context context) {
        String phoneId = getPhoneId(context);
        String beaconId = "1111_1111";
        try {
//            beaconId="28788_201";//major_minor
            beaconId = URLEncoder.encode(beaconId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("URL", e.getMessage());
        }
        JSONArray jsonArray = null;
        String url = LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&act=Query&info=" + beaconId + "&phone_id=" + phoneId + "&wid=" + beaconId;
        Log.e("RESPONSE_SERVER_URL", url);
        String json = HttpRequest.get(url).connectTimeout(30000).body();
        Log.e("RESPONSE_SERVER", json);
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            return null;
        }
        return jsonArray;
    }


    public static String getPhoneId(Context context) {
        DataItems dataItems = new DataItems(context);
        String phoneId = "";
        try {
            phoneId = URLEncoder.encode(dataItems.getUserId(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            phoneId = dataItems.getUserId();
        }
        return phoneId;
    }

    public static String getPhoneIdNoEncode(Context context) {
        DataItems dataItems = new DataItems(context);
        return dataItems.getUserId();
    }

    private static String randomId() {
        Random random = new Random();
        String phoneId = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + System.currentTimeMillis();
        return phoneId;
    }

    public String getPhoneIdOld() {
        return mSharedPreferences.getString(PHONE_ID_KEY, "");
    }

    public void setPhoneIdOld(String phoneId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PHONE_ID_KEY, phoneId);
        editor.apply();
    }

    public String getNameOfUser() {
        return mSharedPreferences.getString(NAME_OF_USER_KEY, "Anonymous");
    }

    public boolean isLoginGmail() {
        return mSharedPreferences.getBoolean(LOGIN_GMAIL_STATE_KEY, false);
    }

    public boolean isLoginFace() {
        return mSharedPreferences.getBoolean(LOGIN_FACE_STATE_KEY, false);
    }

    public void setNameOfUser(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(NAME_OF_USER_KEY, name);
        editor.apply();
    }

    public void setLoginGmailState() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LOGIN_GMAIL_STATE_KEY, true);
        editor.apply();
    }

    public void setLoginFaceState() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LOGIN_FACE_STATE_KEY, true);
        editor.apply();
    }


    private void showDialogUnlock(int level) {
        Intent intent = new Intent(mContext, UnlockDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        switch (level) {
            case 2:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 3:
                intent.putExtra("IMAGE_ITEM", getItem(bottleId).getIconResouce());
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_3);
                break;
            case 4:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 5:
                intent.putExtra("IMAGE_ITEM", getItem(bag4Id).getIconResouce());
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_5);
                break;
            case 6:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 7:
                subPoints(-50);
                intent.putExtra("TEXT_UNLOCK", "+50PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_7);
                break;
            case 8:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 9:
                intent.putExtra("IMAGE_ITEM", getItem(bag5Id).getIconResouce());
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_9);
                break;
            case 10:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 11:
                intent.putExtra("IMAGE_ITEM", getItem(tambourineId).getIconResouce());
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_11);
                break;
            case 12:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 13:
                intent.putExtra("IMAGE_ITEM", getItem(bag6Id).getIconResouce());
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_13);
                break;
            case 14:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 15:
                subPoints(-50);
                intent.putExtra("TEXT_UNLOCK", "+50PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_15);
                break;
            case 16:
                subPoints(-10);
                intent.putExtra("TEXT_UNLOCK", "+10PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_2);
                break;
            case 17:
                intent.putExtra("IMAGE_ITEM", getItem(bag3Id).getIconResouce());
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_13);
                break;
            case 18:
                subPoints(-20);
                intent.putExtra("TEXT_UNLOCK", "+20PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_18);
                break;
            case 19:
                subPoints(-100);
                intent.putExtra("TEXT_UNLOCK", "+100PTs");
                intent.putExtra("TEXT_DESCRIPTION", R.string.next_level_19);
                break;
        }
        mContext.startActivity(intent);
    }

    public int getXPforNextLevel() {
        switch (getCurrentLevel()) {
            case 0:
                return 50;
            case 1:
                return 100;
            case 2:
                return 150;
            case 3:
                return 250;
            case 4:
                return 400;
            case 5:
                return 650;
            case 6:
                return 1050;
            case 7:
                return 1700;
            case 8:
                return 2750;
            case 9:
                return 4450;
            case 10:
                return 7200;
            case 11:
                return 11650;
            case 12:
                return 18850;
            case 13:
                return 30500;
            case 14:
                return 49350;
            case 15:
                return 79850;
            case 16:
                return 129200;
            case 17:
                return 209050;
            case 18:
                return 338250;
            case 19:
                return 547300;
            default:
                return 99999999;

        }
    }

    public int getCurrentLevel() {
        int xp = getTotalXP();
        if (xp < 50) {
            return 0;
        } else if (xp < 100) {
            return 1;
        } else if (xp < 150) {
            return 2;
        } else if (xp < 250) {
            return 3;
        } else if (xp < 400) {
            return 4;
        } else if (xp < 650) {
            return 5;
        } else if (xp < 1050) {
            return 6;
        } else if (xp < 1700) {
            return 7;
        } else if (xp < 2750) {
            return 8;
        } else if (xp < 4450) {
            return 9;
        } else if (xp < 7200) {
            return 10;
        } else if (xp < 11650) {
            return 11;
        } else if (xp < 18850) {
            return 12;
        } else if (xp < 30500) {
            return 13;
        } else if (xp < 49350) {
            return 14;
        } else if (xp < 79850) {
            return 15;
        } else if (xp < 129200) {
            return 16;
        } else if (xp < 209050) {
            return 17;
        } else if (xp < 338250) {
            return 18;
        } else if (xp < 547300) {
            return 19;
        } else {
            return 20;
        }
    }

    public boolean checkEventValid() {
        if (!DataItems.getPhoneId(mContext).isEmpty()) {
            if (HttpRequest.isOnline(mContext)) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                if (hour <= 22 && hour >= 6) {

                    long currentTime = System.currentTimeMillis();
                    long lastTime = mSharedPreferences.getLong(LAST_TIME_CHECK, 0);
                    if (currentTime - lastTime > 10800000) {
                        setLastTime(currentTime);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkSendActionValid() {
        if (HttpRequest.isOnline(mContext)) {
            long currentTime = System.currentTimeMillis();
            long lastTime = mSharedPreferences.getLong(LAST_TIME_SEND_ACTION, 0);
//            Log.e("VIEW_LISTT",String.valueOf(currentTime - lastTime));
            if (currentTime - lastTime > 1800000) {
                setLastTimeSendAction(currentTime);
                return true;
            }
        }
        return false;
    }


    public void setLastTime(long lastTime) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_TIME_CHECK, lastTime);
        editor.apply();
    }

    public void setLastTimeSendAction(long lastTime) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_TIME_SEND_ACTION, lastTime);
        editor.apply();
    }

    public void addBananaBonus() {
        long currentTime = System.currentTimeMillis();
        long timeOnline = getLastTimeAddBananaBonus();
        long betweenTime = currentTime - timeOnline;
        int qty = (int) (betweenTime / 3600000);
//        Log.e("CHECK_TIME", timeOnline+"_"+(currentTime - (betweenTime % 3600000)));
        setLastTimeAddBananaBonus(currentTime - (betweenTime % 3600000));
        FoodObject foodObject = mSqliteHelper.getFoodItem(bananasId);
        int max = 10;
        if (foodObject.getQty() <= max) {
            if (foodObject != null)
                max -= foodObject.getQty();
            if (max > qty)
                mSqliteHelper.updateFood(bananasId, qty);
            else
                mSqliteHelper.updateFood(bananasId, max);
        }
    }

    public void setLastTimeAddBananaBonus(long lastTime) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_TIME_ADD_FOOD, lastTime);
        editor.apply();
    }

    public String getActionList() {
        return mSharedPreferences.getString(ACTION_LIST_SAVE, "");
    }

    public String getDetectList() {
        return mSharedPreferences.getString(DETECT_LIST_SAVE, "");
    }

    public String getViewActionList() {
        return mSharedPreferences.getString(VIEW_LIST_SAVE, "");
    }

    public String getUseItemList() {
        return mSharedPreferences.getString(USE_ITEM_LIST_SAVE, "");
    }

    public long getLastTimeAddBananaBonus() {
        return mSharedPreferences.getLong(LAST_TIME_ADD_FOOD, 0);
    }

    public String getLastTimeCheckExpire() {
        return mSharedPreferences.getString(LAST_TIME_EXPIRE, "");
    }

    public void addActionToList(String promotionID, String actionID, String beaconID) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String currentChange = getActionList();
        if (currentChange.isEmpty())
            editor.putString(ACTION_LIST_SAVE, promotionID + "_" + actionID + "_" + Global.getTime() + "_" + beaconID);
        else
            editor.putString(ACTION_LIST_SAVE, currentChange + ";" + promotionID + "_" + actionID + "_" + Global.getTime() + "_" + beaconID);
        editor.apply();
    }

    public void addDetectToList(String beaconIds) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String currentChange = getDetectList();
        String[] tmp = beaconIds.split(",");
        for (int i = 0; i < tmp.length; i++) {
            String beaconId = tmp[i];
            if (currentChange.isEmpty())
                editor.putString(DETECT_LIST_SAVE, beaconId + "_" + Global.getTime());
            else
                editor.putString(DETECT_LIST_SAVE, currentChange + ";" + beaconId + "_" + Global.getTime());
        }
        editor.apply();
    }

    public void addViewActionToList(String listPromotionView) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String currentChange = getViewActionList();
        if (currentChange.isEmpty())
            editor.putString(VIEW_LIST_SAVE, listPromotionView);
        else
            editor.putString(VIEW_LIST_SAVE, currentChange + ";" + listPromotionView);
        editor.apply();
    }

    public void addUseItemToList(int itemId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String currentChange = getUseItemList();
        if (currentChange.isEmpty())
            editor.putString(USE_ITEM_LIST_SAVE, itemId + "_" + Global.getTime());
        else
            editor.putString(USE_ITEM_LIST_SAVE, currentChange + ";" + itemId + "_" + Global.getTime());
        editor.apply();
    }

    public void setLastTimeExpire() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LAST_TIME_EXPIRE, Global.getDate());
        editor.apply();
    }

    public void clearActionList() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACTION_LIST_SAVE, "");
        editor.apply();
    }

    public void clearViewActionList() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(VIEW_LIST_SAVE, "");
        editor.apply();
    }

    public void clearUseItemList() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USE_ITEM_LIST_SAVE, "");
        editor.apply();
    }

    public boolean checkTimeExpireValid() {
        String lastTime = getLastTimeCheckExpire();
        if (lastTime.isEmpty()) {
            setLastTimeExpire();
            return false;
        }

        if (Global.getDate().equals(lastTime)) {
            return false;
        } else {
            setLastTimeExpire();
            return true;
        }
    }


    public void updateDailyDate(int id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACTION_DAILY_DATE + "_" + id, Global.getDate());
        editor.apply();
    }

    public void updateDailyAction(int id, int total) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ACTION_DAILY_MAX + "_" + id, total);
        editor.apply();
    }

    public void updateDailyAction(int id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int action = getDailyMax(id) + 1;
        editor.putInt(ACTION_DAILY_MAX + "_" + id, action);
        editor.apply();
    }

    public void resetDailyAction(int id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ACTION_DAILY_MAX + "_" + id, 0);
        editor.apply();
    }

    public String getDailyDate(int id) {
        return mSharedPreferences.getString(ACTION_DAILY_DATE + "_" + id, "");
    }

    public int getDailyMax(int id) {
        return mSharedPreferences.getInt(ACTION_DAILY_MAX + "_" + id, 0);
    }

    public int getActionDaily(int id) {
        String dailyDate = getDailyDate(id);
        String currentDate = Global.getDate();
        if (dailyDate.equals(currentDate)) {
            return getDailyMax(id);
        } else {
            updateDailyDate(id);
            resetDailyAction(id);
            return 0;
        }
    }

    public static String uriIconItem(ItemObject itemObject, Context context) {
        SqliteHelper sqliteHelper = SqliteHelper.getInstanceSQLiteHelper(context);
        List<ItemObject> itemObjects = sqliteHelper.getAllItem();
        Log.e("URI", itemObject.getId() + "__");

        for (ItemObject object : itemObjects) {
            if (itemObject.getId() == object.getId()) {
                return object.getIconUrl();
            }
        }
        return itemObject.getIconUrl();
    }

    public static String uriIconItemWithId(int id, Context context) {
        SqliteHelper sqliteHelper = SqliteHelper.getInstanceSQLiteHelper(context);
        List<ItemObject> itemObjects = sqliteHelper.getAllItem();
        Log.e("URI", id + "__");

        for (ItemObject object : itemObjects) {
            if (id == object.getId()) {
                return object.getIconUrl();
            }
        }
        return "";
    }

    public void addItemForGrab(ItemQTY itemQTY) {
        ItemObject itemObject = itemQTY.getItemObject();
        List<ItemObject> itemObjects = new ArrayList<ItemObject>();
        if (itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_DAILY))
            itemObjects = this.getDailyItemList();
        else if (itemObject.getActionId().equals(Global.TYPE_TOMI_BAG))
            itemObjects = this.getBagItemList();
        else if (itemObject.getActionId().equals(Global.TYPE_TOMI_THEME)) {
            try {
                String items[] = this.getTheme().split(";");
                for (String item : items) {
                    itemObjects.add(this.getItem(Integer.valueOf(item)));
                }
            } catch (Exception e) {
            }
        }
        boolean skip = false;
        for (ItemObject item : itemObjects) {
            if (itemObject.getId() == item.getId()) {
                skip = true;
            }
        }
        if (!skip) {
            String itemType = itemObject.getActionId();
            if (itemType.equals(Global.TYPE_TOMI_BAG)) {
                this.addItemToList(DataItems.BAG_ITEM_KEY, itemObject.getId());
            } else if (itemType.equals(Global.TYPE_TOMI_THEME)) {
                this.addItemToList(DataItems.THEME_ITEM_KEY, itemObject.getId());
            } else if (itemType.equals(Global.TYPE_TOMI_ACTION_EAT)) {
                this.addItemToList(DataItems.EAT_ITEM_KEY, itemObject.getId(), itemQTY.getQty());
            } else {
                this.addItemToList(DataItems.DAILY_ITEM_KEY, itemObject.getId());
            }
        }
    }

    public void addItemForRestore(ItemQTY itemQTY) {
        ItemObject itemObject = itemQTY.getItemObject();
        List<ItemObject> itemObjects = new ArrayList<ItemObject>();
        if (itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_DAILY))
            itemObjects = this.getDailyItemList();
        else if (itemObject.getActionId().equals(Global.TYPE_TOMI_BAG))
            itemObjects = this.getBagItemList();
        else if (itemObject.getActionId().equals(Global.TYPE_TOMI_THEME)) {
            try {
                String items[] = this.getTheme().split(";");
                for (String item : items) {
                    itemObjects.add(this.getItem(Integer.valueOf(item)));
                }
            } catch (Exception e) {
            }
        }
        boolean skip = false;
        for (ItemObject item : itemObjects) {
            if (itemObject.getId() == item.getId()) {
                skip = true;
            }
        }
        if (!skip) {
            String itemType = itemObject.getActionId();
            if (itemType.equals(Global.TYPE_TOMI_BAG)) {
                this.addItemToList(DataItems.BAG_ITEM_KEY, itemObject.getId());
            } else if (itemType.equals(Global.TYPE_TOMI_THEME)) {
                this.addItemToList(DataItems.THEME_ITEM_KEY, itemObject.getId());
            } else if (itemType.equals(Global.TYPE_TOMI_ACTION_EAT)) {
                this.updateFoodItem(itemObject.getId(), itemQTY.getQty());
            } else {
                this.addItemToList(DataItems.DAILY_ITEM_KEY, itemObject.getId());
            }
        }
    }

    public boolean isHelpFirst() {
        return mSharedPreferences.getBoolean(HELP_FIRST_KEY, false);
    }

    public void setStateHelp(boolean isActive) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(HELP_FIRST_KEY, isActive);
        editor.apply();
    }


    public boolean isDetectLocationA() {
        return mSharedPreferences.getBoolean(LOCATION_A_KEY, false);
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_ID_FG, userId);
        editor.apply();
    }


    public String getUserId() {
        return mSharedPreferences.getString(USER_ID_FG, "");
    }

    public void setDetectLocationA(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LOCATION_A_KEY, state);
        editor.apply();
    }

    public boolean isDetectLocationB() {
        return mSharedPreferences.getBoolean(LOCATION_B_KEY, false);
    }

    public void setIsNewName(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_NEW_NAME, state);
        editor.apply();
    }

    public boolean isNewName() {
        return mSharedPreferences.getBoolean(IS_NEW_NAME, false);
    }

    public void setDetectLocationB(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(LOCATION_B_KEY, state);
        editor.apply();
    }

    public String getMessageDemoKey() {
        return mSharedPreferences.getString(MESAGE_DEMO_KEY, "");
    }

    public void setMessageDemoKey(String id) {
        String idList = getMessageDemoKey();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (idList.isEmpty()) {
            editor.putString(MESAGE_DEMO_KEY, id);
        } else {
            editor.putString(MESAGE_DEMO_KEY, idList + ";" + id);
        }
        editor.apply();
    }


    public SqliteHelper getSQLiteHelper() {
        return mSqliteHelper;
    }

    public int getConsecutiveDays() {
        return mSharedPreferences.getInt(CONSECUTIVE_DAYS, 0);
    }

    public void setConsecutiveDays() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int days = 1;

        if (validConsecutive()) {
            if (getConsecutiveDays() >= 28) {
                days = getConsecutiveDays() - 28;
            } else
                days = getConsecutiveDays() + 1;
        }
        editor.putInt(CONSECUTIVE_DAYS, days);
        editor.putString(LAST_DAY_CONSECUTIVE, getCurrentDate());
        editor.apply();
    }

    public void setManualConsecutiveDays(int days) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CONSECUTIVE_DAYS, days);
        editor.putString(LAST_DAY_CONSECUTIVE, getCurrentDate());
        editor.apply();
    }

    public void resetConsecutiveDays() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CONSECUTIVE_DAYS, 0);
        editor.putString(LAST_DAY_CONSECUTIVE, "");
        editor.apply();

    }

    public String getLastDateConsecutive() {
        return mSharedPreferences.getString(LAST_DAY_CONSECUTIVE, "");
    }


    public String getItemsDailyLogin() {
        return mSharedPreferences.getString(ITEMS_OF_DAILY_LOGIN, "");
    }

    public void setItemsDailyLogin(String jsonArray) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ITEMS_OF_DAILY_LOGIN, jsonArray);
        editor.apply();
    }

    public int getMonthOfDailyLogin() {
        return mSharedPreferences.getInt(MONTH_OF_DAILY_LOGIN, 0);
    }

    public void setMonthOfDailyLogin(int month) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(MONTH_OF_DAILY_LOGIN, month);
        editor.apply();
    }

    public int getYearOfDailyLogin() {
        return mSharedPreferences.getInt(YEAR_OF_DAILY_LOGIN, 0);
    }

    public void setYearOfDailyLogin(int year) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(YEAR_OF_DAILY_LOGIN, year);
        editor.apply();
    }

    public int getEventIdOfDailyLogin() {
        return mSharedPreferences.getInt(EVENT_ID_OF_DAILY_LOGIN, 0);
    }

    public void setEventIdOfDailyLogin(int id) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(EVENT_ID_OF_DAILY_LOGIN, id);
        editor.apply();
    }

    public boolean isDailyChecked() {
        return getCurrentDate().equals(getLastDateConsecutive());
    }

    public boolean validConsecutive() {
        if (getConsecutiveDays() == 0) {
            return true;
        }
        try {
            String[] tmpCurrent = getCurrentDate().split("-");
            String[] tmpLast = getLastDateConsecutive().split("-");
            if (Integer.valueOf(tmpCurrent[2]) - Integer.valueOf(tmpLast[2]) == 1) {
                if (tmpLast[0].equals(tmpCurrent[0]) && tmpLast[1].equals(tmpCurrent[1])) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(c.getTime());
        return currentDate;
    }

    public void setLastDayShowDailyLogin() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LAST_DAY_SHOW_DAILY_LOGIN, Global.getDate());
        editor.apply();
    }

    public int getLastBagSelect() {
        return mSharedPreferences.getInt(LAST_BAG, bag1Id);
    }

    public void setLastBagSelect(int bagId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LAST_BAG, bagId);
        editor.apply();
    }

    public String getLastDayShowDailyLogin() {
        return mSharedPreferences.getString(LAST_DAY_SHOW_DAILY_LOGIN, "");
    }
    public String getLastDayCheckVersion() {
        return mSharedPreferences.getString(LAST_DAY_CHECK_VERSION, "");
    }
    public void setLastDayCheckVersion() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LAST_DAY_CHECK_VERSION, Global.getDate());
        editor.apply();
    }
    public boolean isSocialLogin() {
        return mSharedPreferences.getBoolean(IS_SOCIAL_LOGIN, false);
    }
    public void setSocialLogin(boolean isSocial) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_SOCIAL_LOGIN, isSocial);
        editor.apply();
    }
    public void setLastTimeLogin() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_TIME_LOGIN, System.currentTimeMillis());
        editor.apply();
    }

    public void setLastTimeLoginTimeOut() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(LAST_TIME_LOGIN, System.currentTimeMillis() - 172800000);//1day 3-2=1
        editor.apply();
    }

    public long getLastTimeLogin() {
        return mSharedPreferences.getLong(LAST_TIME_LOGIN, System.currentTimeMillis());
    }

    public void setCurrentVersion(int version) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(CURRENT_VERSION, version);
        editor.apply();
    }

    public long getCurrentVersion() {
        return mSharedPreferences.getInt(CURRENT_VERSION, BuildConfig.VERSION_CODE);
    }

    public void registerGCM(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(STATUS_REGISTER_GCM, state);
        editor.apply();
    }

    public boolean isRegisterGCM() {
        return mSharedPreferences.getBoolean(STATUS_REGISTER_GCM,false);
    }
    public void setKeyGCM(String keyGCM) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_SENDER_GCM, keyGCM);
        editor.apply();
    }

    public String getKeyGCM() {
        return mSharedPreferences.getString(KEY_SENDER_GCM,"");
    }

    public void setDemo() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String s = new Random().nextInt(1000) + "";
        Log.e("RESPONSE_SERVER", s);
        editor.putString("DEMO", s);
        editor.apply();

    }

//    public  String  getDemo() {
//        return mSharedPreferences.getString("DEMO", "a");
//    }
//
//
//    private String encodeString(String s) {
//        byte[] data = new byte[0];
//
//        try {
//            data = s.getBytes("UTF-8");
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } finally {
//            String base64Encoded = Base64.encodeToString(data, Base64.DEFAULT);
//
//            return base64Encoded;
//
//        }
//    }

    private static String performPostCall(String requestURL,
                                          HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            Log.e("RESPONSE_SERVER_Ex",e.getMessage().toString());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        Log.e("RESPONSE_SERVER", response);
        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.e("RESPONSE_SERVER_DATA",result.toString());
        return result.toString();
    }

    public static String getIMEIId(Context context) {
        DataItems dataItems = new DataItems(context);
        try {
            String oldId = dataItems.getPhoneIdOld();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneId = telephonyManager.getDeviceId();

            if (oldId.isEmpty()) {
                try {
                    return Global.md5(URLEncoder.encode(phoneId, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    return Global.md5(phoneId);
                }
            } else {
                if (oldId.equals(phoneId)) {
                    oldId = Global.md5(oldId);
                    dataItems.setPhoneIdOld(oldId);
                }
                return oldId;
            }
        } catch (Exception e) {
            return "";
        }
    }

    //update 2016-change name promotion
    public void setUpdateVersion41(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(UPDATE_VERSION_41_KEY, state);
        editor.apply();
    }

    public boolean isUpdateVersion41() {
        return mSharedPreferences.getBoolean(UPDATE_VERSION_41_KEY, false);
    }
    //update 2016-change name promotion
    public void setUpdateVersion48(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(UPDATE_VERSION_48_KEY, state);
        editor.apply();
    }

    public boolean isUpdateVersion48() {
        return mSharedPreferences.getBoolean(UPDATE_VERSION_48_KEY, false);
    }

    public void setUpdateInfo(boolean state) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(UPDATE_INFO_KEY, state);
        editor.apply();
    }

    public boolean isUpdateInfo() {
        return mSharedPreferences.getBoolean(UPDATE_INFO_KEY, true);
    }


}
