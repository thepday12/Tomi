package com.neu.tomi.ultity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.neu.tomi.object.ActionObject;
import com.neu.tomi.object.BonusItemObject;
import com.neu.tomi.object.FoodObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.object.PromtionObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "PromotionDatabase";
    private static final String TABLE_PROMOTION_NAME = "tblPromotion";
    private static final String TABLE_USE_NAME = "tblUse";
    private static final String TABLE_ITEM_NAME = "tblItem";
    private static final String TABLE_MESSAGE_NAME = "tblMessage";
    private static final String TABLE_SHARE_NAME = "tblShare";
    private static final String TABLE_FOOD_NAME = "tblFood";
    private Context mContext;
    private static SqliteHelper sqliteHelper = null;

    public static SqliteHelper getInstanceSQLiteHelper(Context context) {
        if (sqliteHelper == null)
            sqliteHelper = new SqliteHelper(context);
        return sqliteHelper;
    }

    private SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        //Tạo mới hoặc mở kết nối đến DB name
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Nên gọi các phương thức tạo bảng
        String stringCreateTablePromotion = "CREATE TABLE " + TABLE_PROMOTION_NAME + " ( " +
                "id TEXT PRIMARY KEY, " +
                "image_uri TEXT,promo_link TEXT,beacon_id TEXT, description TEXT, note TEXT DEFAULT '', point_share INTEGER, xp_share INTEGER,  point_use INTEGER,xp_use INTEGER, share_action INTEGER, end_time TEXT, total INTEGER DEFAULT 1,show_url INTEGER DEFAULT 1,code_type INTEGER DEFAULT 1, name TEXT DEFAULT '',priority_level INTEGER DEFAULT 0,expired_status INTEGER DEFAULT 0)";
        String stringCreateTableUse = "CREATE TABLE " + TABLE_USE_NAME + " ( " +
                "id TEXT, " +
                "item_id INTEGER, image_uri TEXT, qty INTEGER,  PRIMARY KEY(id,item_id))";
        String stringCreateTableMessage = "CREATE TABLE " + TABLE_MESSAGE_NAME + " ( " +
                "id TEXT, " +
                "title TEXT, content TEXT, link TEXT, state INTEGER, date TEXT,link_type INTEGER, link_caption TEXT DEFAULT 'Tell us',  PRIMARY KEY(id))";
        String stringCreateTableFood = "CREATE TABLE " + TABLE_FOOD_NAME + " ( " +
                "id INTEGER  PRIMARY KEY, " +
                "qty INTEGER)";
        String stringCreateTableShare = "CREATE TABLE " + TABLE_SHARE_NAME + " ( " +
                "id TEXT, " +
                "item_id INTEGER, image_uri TEXT, qty INTEGER,  PRIMARY KEY(id,item_id))";
        String stringCreateTableItem = "CREATE TABLE " + TABLE_ITEM_NAME + " ( " +
                "id INTEGER, " +
                "name TEXT, description TEXT, action_id TEXT, max_action TEXT, image_uri TEXT, script TEXT, text_script TEXT, point INTEGER, point_type INTEGER, PRIMARY KEY(id))";
        db.execSQL(stringCreateTablePromotion);
        db.execSQL(stringCreateTableUse);
        db.execSQL(stringCreateTableShare);
        db.execSQL(stringCreateTableMessage);
        db.execSQL(stringCreateTableItem);
        db.execSQL(stringCreateTableFood);
        insertFirstResourceItem(db);

    }

    public void clearData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PROMOTION_NAME, null, null);
            db.delete(TABLE_SHARE_NAME, null, null);
            db.delete(TABLE_USE_NAME, null, null);
            db.delete(TABLE_MESSAGE_NAME, null, null);
            db.delete(TABLE_FOOD_NAME, null, null);

        } catch (Exception e) {

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //version 3 update item resource
        //version 6 update struct promotion table
        //version 7 update struct promotion table more
        try {
            insertFirstResourceItem(db);
            if (oldVersion < 6) {
                String _1alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                        "total INTEGER DEFAULT 1";
                db.execSQL(_1alterTablePromotion);
            }
            if (oldVersion < 7) {
                String _2alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                        "show_url INTEGER DEFAULT 1";
                String _3alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                        "code_type INTEGER DEFAULT 1";
                String _4alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                        "name TEXT DEFAULT ''";
                db.execSQL(_2alterTablePromotion);
                db.execSQL(_3alterTablePromotion);
                db.execSQL(_4alterTablePromotion);
            }
            if (oldVersion < 9) {
                String _5alterTablePromotion = "ALTER TABLE " + TABLE_MESSAGE_NAME + " ADD COLUMN " +
                        "link_type INTEGER DEFAULT 9";
                db.execSQL(_5alterTablePromotion);
            }
            if (oldVersion == 9) {
                String _alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                        "note TEXT DEFAULT ''";
                db.execSQL(_alterTablePromotion);
            }
            updateVersion11(oldVersion, db);
            updateVersion12(oldVersion, db);
        } catch (Exception e) {

        }
    }

    private void updateVersion11(int oldVersion, SQLiteDatabase db) {
        if (oldVersion < 11) {
            String alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                    "priority_level INTEGER DEFAULT 0";
            String alterTableMessage = "ALTER TABLE " + TABLE_MESSAGE_NAME + " ADD COLUMN " +
                    "link_caption TEXT DEFAULT 'Tell us'";
            db.execSQL(alterTablePromotion);
            db.execSQL(alterTableMessage);
        }
    }

    private void updateVersion12(int oldVersion, SQLiteDatabase db) {
        if (oldVersion < 12) {
            String alterTablePromotion = "ALTER TABLE " + TABLE_PROMOTION_NAME + " ADD COLUMN " +
                    "expired_status INTEGER DEFAULT 0";
            db.execSQL(alterTablePromotion);
        }
    }


    public boolean promotionExist(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = null;
        boolean total = false;
        try {
            cursor = db.rawQuery("select * from " + TABLE_PROMOTION_NAME + " WHERE id=" + id, null);
            total = cursor.getCount() > 0;

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return total;
        }
    }

    public PromtionObject getPromotionWithID(String idPromotion) {
        List<BonusItemObject> useBonusItemObjects = getAllUseBonusItem();
        List<BonusItemObject> shareBonusItemObjects = getAllShareBonusItem();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        PromtionObject promtionObject = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + TABLE_PROMOTION_NAME + " WHERE id=" + idPromotion, null);
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String beacon_id = cursor.getString(cursor
                            .getColumnIndex("beacon_id"));
                    String promo_link = cursor.getString(cursor
                            .getColumnIndex("promo_link"));
                    String image_uri = cursor.getString(cursor
                            .getColumnIndex("image_uri"));
                    String description = cursor.getString(cursor
                            .getColumnIndex("description"));
                    String note = cursor.getString(cursor
                            .getColumnIndex("note"));
                    String endTime = cursor.getString(cursor
                            .getColumnIndex("end_time"));
                    String name = cursor.getString(cursor
                            .getColumnIndex("name"));
                    int total = cursor.getInt(cursor
                            .getColumnIndex("total"));
                    int show_url = cursor.getInt(cursor
                            .getColumnIndex("show_url"));
                    int code_type = cursor.getInt(cursor
                            .getColumnIndex("code_type"));
                    int point_share = cursor.getInt(cursor
                            .getColumnIndex("point_share"));
                    int xp_share = cursor.getInt(cursor
                            .getColumnIndex("xp_share"));
                    int point_use = cursor.getInt(cursor
                            .getColumnIndex("point_use"));
                    int xp_use = cursor.getInt(cursor
                            .getColumnIndex("xp_use"));
                    int share_action = cursor.getInt(cursor
                            .getColumnIndex("share_action"));
                    int priority_level = cursor.getInt(cursor
                            .getColumnIndex("priority_level"));
                    int expired_status = cursor.getInt(cursor
                            .getColumnIndex("expired_status"));
                    ActionObject useAction = new ActionObject(point_use, xp_use, getBonusItemById(useBonusItemObjects, id), 0);
                    ActionObject shareAction = new ActionObject(point_share, xp_share, getBonusItemById(shareBonusItemObjects, id), share_action);
                    promtionObject = new PromtionObject(id, image_uri, description, note, useAction, shareAction, endTime, beacon_id, promo_link, total, show_url, code_type, name, priority_level, expired_status);
                    break;
                }
            }

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return promtionObject;
        }
    }


    public FoodObject getFoodItem(int itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_FOOD_NAME + " WHERE id =" + itemId, null);
        FoodObject foodObject = null;
        try {

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    int id = cursor.getInt(cursor
                            .getColumnIndex("id"));
                    int qty = cursor.getInt(cursor
                            .getColumnIndex("qty"));
                    ItemObject itemObject = getItemObjectWithId(db, id);
                    foodObject = new FoodObject(itemObject, qty);
                    break;
                }
            }


        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return foodObject;
        }

    }

    public List<FoodObject> getFoodItems() {
        List<FoodObject> itemObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_FOOD_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    int id = cursor.getInt(cursor
                            .getColumnIndex("id"));
                    int qty = cursor.getInt(cursor
                            .getColumnIndex("qty"));
                    ItemObject itemObject = getItemObjectWithId(db, id);
                    itemObjects.add(new FoodObject(itemObject, qty));
                    cursor.moveToNext();
                }
            }


        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return itemObjects;
        }

    }

    public List<ItemObject> getAllItem() {
        List<ItemObject> itemObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ITEM_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor

                    int id = cursor.getInt(cursor
                            .getColumnIndex("id"));
                    String name = cursor.getString(cursor
                            .getColumnIndex("name"));
                    String description = cursor.getString(cursor
                            .getColumnIndex("description"));
                    String actionId = cursor.getString(cursor
                            .getColumnIndex("action_id"));
                    String script = cursor.getString(cursor
                            .getColumnIndex("script"));
                    String textScript = cursor.getString(cursor
                            .getColumnIndex("text_script"));
                    int maxAction = cursor.getInt(cursor
                            .getColumnIndex("max_action"));
                    int point = cursor.getInt(cursor
                            .getColumnIndex("point"));
                    int pointType = cursor.getInt(cursor
                            .getColumnIndex("point_type"));
                    String imageUri = "";
                    if (id == 2 || id > 11) {
                        imageUri = cursor.getString(cursor
                                .getColumnIndex("image_uri"));
                    }
                    itemObjects.add(new ItemObject(id, actionId, name, description, maxAction, script, textScript, point, pointType, imageUri));
                    cursor.moveToNext();
                }
            }


        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return itemObjects;
        }

    }

    public boolean isItemSave(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ITEM_NAME + " WHERE id=" + id, null);
        int count = 0;
        try {
            count = cursor.getCount();

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return count > 0;
        }

    }

    public List<MailObject> getAllMail() {
        List<MailObject> mailObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_MESSAGE_NAME + " ORDER BY date  DESC", null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String title = cursor.getString(cursor
                            .getColumnIndex("title"));
                    String content = cursor.getString(cursor
                            .getColumnIndex("content"));
                    String link = cursor.getString(cursor
                            .getColumnIndex("link"));
                    int state = cursor.getInt(cursor
                            .getColumnIndex("state"));
                    int linkType = cursor.getInt(cursor
                            .getColumnIndex("link_type"));
                    String linkCaption = cursor.getString(cursor
                            .getColumnIndex("link_caption"));
                    if (state > 0) {
                        mailObjects.add(new MailObject(id, title, content, link, true, linkType, linkCaption));
                    } else {
                        mailObjects.add(new MailObject(id, title, content, link, false, linkType, linkCaption));
                    }
                    cursor.moveToNext();
                }
            }

        } finally {
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            return mailObjects;
        }
    }

    public List<MailObject> getAllMailNoCheck() {
        List<MailObject> mailObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_MESSAGE_NAME + " WHERE state=0", null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String title = cursor.getString(cursor
                            .getColumnIndex("title"));
                    String content = cursor.getString(cursor
                            .getColumnIndex("content"));
                    String link = cursor.getString(cursor
                            .getColumnIndex("link"));
                    int state = cursor.getInt(cursor
                            .getColumnIndex("state"));
                    int linkType = cursor.getInt(cursor
                            .getColumnIndex("link_type"));
                    String linkCaption = cursor.getString(cursor
                            .getColumnIndex("link_caption"));
                    if (state > 0) {
                        mailObjects.add(new MailObject(id, title, content, link, true, linkType, linkCaption));
                    } else {
                        mailObjects.add(new MailObject(id, title, content, link, false, linkType, linkCaption));
                    }
                    cursor.moveToNext();
                }
            }

        } finally {
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            return mailObjects;
        }
    }

    private List<BonusItemObject> getAllUseBonusItem() {
        List<BonusItemObject> bonusItemObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_USE_NAME, null);
        try {
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    int item_id = cursor.getInt(cursor
                            .getColumnIndex("item_id"));
                    int qty = cursor.getInt(cursor
                            .getColumnIndex("qty"));
                    String image_uri = cursor.getString(cursor
                            .getColumnIndex("image_uri"));
                    bonusItemObjects.add(new BonusItemObject(id, item_id, qty, image_uri));
                    cursor.moveToNext();
                }
            }

        } finally {
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            return bonusItemObjects;
        }
    }

    private List<BonusItemObject> getAllShareBonusItem() {
        List<BonusItemObject> bonusItemObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_SHARE_NAME, null);
        try {
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    int item_id = cursor.getInt(cursor
                            .getColumnIndex("item_id"));
                    int qty = cursor.getInt(cursor
                            .getColumnIndex("qty"));
                    String image_uri = cursor.getString(cursor
                            .getColumnIndex("image_uri"));
                    bonusItemObjects.add(new BonusItemObject(id, item_id, qty, image_uri));
                    cursor.moveToNext();
                }
            }

        } finally {
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
            return bonusItemObjects;
        }
    }

    private List<BonusItemObject> getBonusItemById(List<BonusItemObject> bonusItemObjects, String adId) {
        List<BonusItemObject> items = new ArrayList<>();
        for (BonusItemObject item : bonusItemObjects) {
            if (item.getAdId().equals(adId)) {
                items.add(item);
            }
        }
        return items;
    }

    public List<PromtionObject> getAllPromotion() {
        List<PromtionObject> userObjects = new ArrayList<PromtionObject>();
        List<PromtionObject> promtionObjects = new ArrayList<PromtionObject>();
        List<BonusItemObject> useBonusItemObjects = getAllUseBonusItem();
        List<BonusItemObject> shareBonusItemObjects = getAllShareBonusItem();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PROMOTION_NAME, null);
        try {
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String beacon_id = cursor.getString(cursor
                            .getColumnIndex("beacon_id"));
                    String promo_link = cursor.getString(cursor
                            .getColumnIndex("promo_link"));
                    String image_uri = cursor.getString(cursor
                            .getColumnIndex("image_uri"));
                    String description = cursor.getString(cursor
                            .getColumnIndex("description"));
                    String note = cursor.getString(cursor
                            .getColumnIndex("note"));
                    String endTime = cursor.getString(cursor
                            .getColumnIndex("end_time"));
                    String name = cursor.getString(cursor
                            .getColumnIndex("name"));
                    int total = cursor.getInt(cursor
                            .getColumnIndex("total"));
                    int show_url = cursor.getInt(cursor
                            .getColumnIndex("show_url"));
                    int code_type = cursor.getInt(cursor
                            .getColumnIndex("code_type"));
                    int point_share = cursor.getInt(cursor
                            .getColumnIndex("point_share"));
                    int xp_share = cursor.getInt(cursor
                            .getColumnIndex("xp_share"));
                    int point_use = cursor.getInt(cursor
                            .getColumnIndex("point_use"));
                    int xp_use = cursor.getInt(cursor
                            .getColumnIndex("xp_use"));
                    int share_action = cursor.getInt(cursor
                            .getColumnIndex("share_action"));
                    int priority_level = cursor.getInt(cursor
                            .getColumnIndex("priority_level"));
                    int expired_status = cursor.getInt(cursor
                            .getColumnIndex("expired_status"));
                    ActionObject useAction = new ActionObject(point_use, xp_use, getBonusItemById(useBonusItemObjects, id), 0);
                    ActionObject shareAction = new ActionObject(point_share, xp_share, getBonusItemById(shareBonusItemObjects, id), share_action);
                    userObjects.add(new PromtionObject(id, image_uri, description, note, useAction, shareAction, endTime, beacon_id, promo_link, total, show_url, code_type, name, priority_level,expired_status));
                    cursor.moveToNext();
                }
            }
            //soft
            int lastIndex = userObjects.size() - 1;
            for (int i = lastIndex; i >= 0; i--) {
                promtionObjects.add(userObjects.get(i));
            }
//            soft by priority_level
            Collections.sort(promtionObjects, new Comparator<PromtionObject>() {
                @Override
                public int compare(PromtionObject lhs, PromtionObject rhs) {
                    int sub = rhs.getPriorityLevel() - lhs.getPriorityLevel();
                    return sub;
                }
            });

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return promtionObjects;
        }
    }

    public List<PromtionObject> getAllPromotionWithBeacon(String beaconid) {
        List<PromtionObject> userObjects = new ArrayList<PromtionObject>();
        List<PromtionObject> PromtionObjects = new ArrayList<PromtionObject>();
        List<BonusItemObject> useBonusItemObjects = getAllUseBonusItem();
        List<BonusItemObject> shareBonusItemObjects = getAllShareBonusItem();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        String[] tmp = beaconid.split(",");
        String where = "(";
        for (int i = 0; i < tmp.length; i++) {
            where += "'" + tmp[i] + "',";
        }
        where = where.substring(0, where.length() - 1) + ")";
        Cursor cursor = db.rawQuery("select * from " + TABLE_PROMOTION_NAME + " WHERE beacon_id NOT IN " + where, null);
        try {
            if (cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                    String id = cursor.getString(cursor
                            .getColumnIndex("id"));
                    String beacon_id = cursor.getString(cursor
                            .getColumnIndex("beacon_id"));
                    String promo_link = cursor.getString(cursor
                            .getColumnIndex("promo_link"));
                    String image_uri = cursor.getString(cursor
                            .getColumnIndex("image_uri"));
                    String description = cursor.getString(cursor
                            .getColumnIndex("description"));
                    String note = cursor.getString(cursor
                            .getColumnIndex("note"));
                    String endTime = cursor.getString(cursor
                            .getColumnIndex("end_time"));
                    String name = cursor.getString(cursor
                            .getColumnIndex("name"));
                    int total = cursor.getInt(cursor
                            .getColumnIndex("total"));
                    int show_url = cursor.getInt(cursor
                            .getColumnIndex("show_url"));
                    int code_type = cursor.getInt(cursor
                            .getColumnIndex("code_type"));
                    int point_share = cursor.getInt(cursor
                            .getColumnIndex("point_share"));
                    int xp_share = cursor.getInt(cursor
                            .getColumnIndex("xp_share"));
                    int point_use = cursor.getInt(cursor
                            .getColumnIndex("point_use"));
                    int xp_use = cursor.getInt(cursor
                            .getColumnIndex("xp_use"));
                    int share_action = cursor.getInt(cursor
                            .getColumnIndex("share_action"));
                    int priority_level = cursor.getInt(cursor
                            .getColumnIndex("priority_level"));
                    int expired_status = cursor.getInt(cursor
                            .getColumnIndex("expired_status"));
                    ActionObject useAction = new ActionObject(point_use, xp_use, getBonusItemById(useBonusItemObjects, id), 0);
                    ActionObject shareAction = new ActionObject(point_share, xp_share, getBonusItemById(shareBonusItemObjects, id), share_action);
                    userObjects.add(new PromtionObject(id, image_uri, description, note, useAction, shareAction, endTime, beacon_id, promo_link, total, show_url, code_type, name, priority_level,expired_status));
                    cursor.moveToNext();
                }
            }
            //soft
            int lastIndex = userObjects.size() - 1;
            for (int i = lastIndex; i >= 0; i--) {
                PromtionObjects.add(userObjects.get(i));
            }

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            cursor.close();
            return PromtionObjects;
        }
    }

    //thêm bản ghi
    public boolean insertPromotion(PromtionObject PromtionObject) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues promotionContentValues = new ContentValues();
            promotionContentValues.put("id", PromtionObject.getPromotionId());
            promotionContentValues.put("image_uri", PromtionObject.getImageURL());
            promotionContentValues.put("description", PromtionObject.getDescription());
            promotionContentValues.put("note", PromtionObject.getNote());
            promotionContentValues.put("point_share", PromtionObject.getShareBonus().getPoint());
            promotionContentValues.put("xp_share", PromtionObject.getShareBonus().getXp());
            promotionContentValues.put("point_use", PromtionObject.getUseBonus().getPoint());
            promotionContentValues.put("xp_use", PromtionObject.getUseBonus().getXp());
            promotionContentValues.put("end_time", PromtionObject.getEndTime());
            promotionContentValues.put("name", PromtionObject.getName());
            promotionContentValues.put("beacon_id", PromtionObject.getBeaconID());
            promotionContentValues.put("promo_link", PromtionObject.getPromotionLink());
            promotionContentValues.put("code_type", PromtionObject.getCodeType());
            promotionContentValues.put("show_url", PromtionObject.getShowUrl());
            promotionContentValues.put("priority_level", PromtionObject.getPriorityLevel());
            promotionContentValues.put("expired_status", PromtionObject.getExpiredStatus());
            int total = 1;
            try {
                total = PromtionObject.getTotal();
            } catch (Exception e) {

            }
            promotionContentValues.put("total", total);
            promotionContentValues.put("share_action", 0);
            db.insert(TABLE_PROMOTION_NAME, null, promotionContentValues);


            List<BonusItemObject> useItem = PromtionObject.getUseBonus().getBonusItemObjects();
            for (BonusItemObject item : useItem) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", PromtionObject.getPromotionId());
                contentValues.put("item_id", item.getItemId());
                contentValues.put("qty", item.getItemBonus());
                contentValues.put("image_uri", item.getImageURL());
                db.insert(TABLE_USE_NAME, null, contentValues);

            }

            List<BonusItemObject> shareItem = PromtionObject.getShareBonus().getBonusItemObjects();
            for (BonusItemObject item : shareItem) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", PromtionObject.getPromotionId());
                contentValues.put("item_id", item.getItemId());
                contentValues.put("qty", item.getItemBonus());
                contentValues.put("image_uri", item.getImageURL());
                db.insert(TABLE_SHARE_NAME, null, contentValues);

            }

        } catch (Exception ex) {
            result = false;
            return false;

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }

    public boolean insertMessage(String id, String title, String content, String link, int linkType, String linkCaption) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues messageContentValues = new ContentValues();
            messageContentValues.put("id", id);
            messageContentValues.put("title", title);
            messageContentValues.put("content", content);
            messageContentValues.put("link", link);
            messageContentValues.put("link_type", linkType);
            messageContentValues.put("date", Global.getTime());
            messageContentValues.put("state", 0);
            messageContentValues.put("link_caption", linkCaption);
            db.insert(TABLE_MESSAGE_NAME, null, messageContentValues);


        } catch (Exception ex) {
            result = false;
            return false;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }


    private void insertFirstResourceItem(SQLiteDatabase db) {
        List<ItemObject> itemObjects = Global.getItemResource();
        for (ItemObject itemObject : itemObjects) {
            insertItemResource(db, itemObject);
        }
    }

    private boolean insertItemResource(SQLiteDatabase db, ItemObject itemObject) {
        ContentValues messageContentValues = new ContentValues();
        messageContentValues.put("id", itemObject.getId());
        messageContentValues.put("name", itemObject.getName());
        messageContentValues.put("description", itemObject.getDescription());
        messageContentValues.put("action_id", itemObject.getActionId());
        messageContentValues.put("max_action", itemObject.getMaxAction());
        messageContentValues.put("point", itemObject.getPoint());
        messageContentValues.put("point_type", itemObject.getPointType());
        messageContentValues.put("text_script", itemObject.getTextScript());
        try {
            db.insert(TABLE_ITEM_NAME, null, messageContentValues);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean insertItemFromShop(ItemObject itemObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues messageContentValues = new ContentValues();
            messageContentValues.put("id", itemObject.getId());
            messageContentValues.put("name", itemObject.getName());
            messageContentValues.put("description", itemObject.getDescription());
            messageContentValues.put("action_id", itemObject.getActionId());
            messageContentValues.put("max_action", itemObject.getMaxAction());
            messageContentValues.put("script", itemObject.getScript());
            messageContentValues.put("text_script", itemObject.getTextScript());
            messageContentValues.put("point", itemObject.getPoint());
            messageContentValues.put("point_type", itemObject.getPointType());
            db.insert(TABLE_ITEM_NAME, null, messageContentValues);


        } catch (Exception ex) {
            result = false;
            return false;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }

    public boolean insertItemFromShop(int id, String actionId, String name, String description, int max_action, String script, String textScript, int point, int pointType, boolean isResource, String imageUri) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues messageContentValues = new ContentValues();
            messageContentValues.put("id", id);
            messageContentValues.put("name", name);
            messageContentValues.put("description", description);
            messageContentValues.put("action_id", actionId);
            messageContentValues.put("max_action", max_action);
            messageContentValues.put("image_uri", imageUri);
            messageContentValues.put("script", script);
            messageContentValues.put("text_script", textScript);
            messageContentValues.put("point", point);
            messageContentValues.put("point_type", pointType);
            db.insert(TABLE_ITEM_NAME, null, messageContentValues);

        } catch (Exception ex) {
            result = false;
            return false;

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }

    public boolean updateStateMessage(String id) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues messageContentValues = new ContentValues();
            messageContentValues.put("state", 1);
            db.update(TABLE_MESSAGE_NAME, messageContentValues, "id=" + id, null);

        } catch (Exception ex) {
            result = false;
            return false;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }

    public boolean updatePromotion(String id, int total) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues messageContentValues = new ContentValues();
            messageContentValues.put("total", total);
            db.update(TABLE_PROMOTION_NAME, messageContentValues, "id=" + id, null);

        } catch (Exception ex) {
            result = false;
            return false;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }

    public boolean updateIconItem(int id, String uri) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        try {
            ContentValues messageContentValues = new ContentValues();
            messageContentValues.put("image_uri", uri);
            db.update(TABLE_ITEM_NAME, messageContentValues, "id=" + id, null);

        } catch (Exception ex) {
            result = false;
            return false;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }
    }

    public boolean updateFood(int id, int qtyAdd) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        boolean result = true;
        SQLiteDatabase db2 = this.getReadableDatabase();
        db2.beginTransaction();
        try {

            Cursor cursor = db2.rawQuery("select * from " + TABLE_FOOD_NAME + " WHERE id =" + id, null);

            try {
                FoodObject foodObject = null;
                if (cursor.moveToFirst()) {
                    while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                        int itemId = cursor.getInt(cursor
                                .getColumnIndex("id"));
                        int qty = cursor.getInt(cursor
                                .getColumnIndex("qty"));
                        ItemObject itemObject = getItemObjectWithId(db2, itemId);
                        foodObject = new FoodObject(itemObject, qty);
                        break;
                    }
                }
                if (foodObject == null) {

                    if (qtyAdd > 0) {
                        ContentValues foodValues = new ContentValues();
                        foodValues.put("id", id);
                        foodValues.put("qty", qtyAdd);
                        db.insert(TABLE_FOOD_NAME, null, foodValues);
                    }
                } else {

                    ContentValues foodValues = new ContentValues();
                    int qty = foodObject.getQty();
                    qty += qtyAdd;
                    foodValues.put("qty", qty);
                    db.update(TABLE_FOOD_NAME, foodValues, "id=" + id, null);
                }


            } catch (Exception e) {
                result = false;
                return false;
            }

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db2.setTransactionSuccessful();
            db2.endTransaction();
            return result;
        }
    }

    public boolean updateFoodFirst(int id, int qtyAdd) {//ghi file trc khi call
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        SQLiteDatabase db2 = this.getReadableDatabase();
        db2.beginTransaction();
        boolean result = true;
        try {

            Cursor cursor = db2.rawQuery("select * from " + TABLE_FOOD_NAME + " WHERE id =" + id, null);

            try {
                FoodObject foodObject = null;
                if (cursor.moveToFirst()) {
                    while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor
                        int itemId = cursor.getInt(cursor
                                .getColumnIndex("id"));
                        int qty = cursor.getInt(cursor
                                .getColumnIndex("qty"));
                        ItemObject itemObject = getItemObjectWithId(db2, itemId);
                        foodObject = new FoodObject(itemObject, qty);
                        break;
                    }
                }
                if (foodObject == null) {
                    if (qtyAdd > 0) {
                        ContentValues foodValues = new ContentValues();
                        foodValues.put("id", id);
                        foodValues.put("qty", qtyAdd);
                        db.insert(TABLE_FOOD_NAME, null, foodValues);
                    }
                } else {
                    ContentValues foodValues = new ContentValues();
                    foodValues.put("qty", qtyAdd);
                    db.update(TABLE_FOOD_NAME, foodValues, "id=" + id, null);
                }


            } catch (Exception e) {
                result = false;
                return false;
            }

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db2.setTransactionSuccessful();
            db2.endTransaction();
            return result;
        }
    }

    //Xóa bản ghi
    public void deletePromotion(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PROMOTION_NAME, "id = " + id, null);
            db.delete(TABLE_SHARE_NAME, "id = " + id, null);
            db.delete(TABLE_USE_NAME, "id = " + id, null);
            deleteImagePromotion(id);

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    private void deleteImagePromotion(String promotionId) {
        String fileName = promotionId + ".png";
        File myDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/tomi_image");
        new File(myDir, fileName).delete();

    }


    private ItemObject getItemObjectWithId(SQLiteDatabase db, int idSearch) {
        ItemObject itemObject = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_ITEM_NAME + " WHERE id=" + idSearch, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {//Cách lấy dữ liệu từ curssor

                    int id = cursor.getInt(cursor
                            .getColumnIndex("id"));
                    String name = cursor.getString(cursor
                            .getColumnIndex("name"));
                    String description = cursor.getString(cursor
                            .getColumnIndex("description"));
                    String actionId = cursor.getString(cursor
                            .getColumnIndex("action_id"));
                    String script = cursor.getString(cursor
                            .getColumnIndex("script"));
                    String textScript = cursor.getString(cursor
                            .getColumnIndex("text_script"));
                    int maxAction = cursor.getInt(cursor
                            .getColumnIndex("max_action"));
                    int point = cursor.getInt(cursor
                            .getColumnIndex("point"));
                    int pointType = cursor.getInt(cursor
                            .getColumnIndex("point_type"));
                    String imageUri = "";
                    if (id == 2 || id > 11) {
                        imageUri = cursor.getString(cursor
                                .getColumnIndex("image_uri"));
                    }
                    itemObject = new ItemObject(id, actionId, name, description, maxAction, script, textScript, point, pointType, imageUri);
                    break;
                }
            }
        } finally {
            cursor.close();
            return itemObject;
        }

    }
}