package com.neu.tomi.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.neu.tomi.R;
import com.neu.tomi.ultity.DataItems;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thep on 10/18/2015.
 */
public class ItemObject {

    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMaxAction() {
        return maxAction;
    }

    public void setMaxAction(int max_action) {
        this.maxAction = max_action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPointType() {
        return pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    public boolean isResource() {
        if (id < DataItems.ID_RESOURCE_MAX)
            return true;
        else
            return false;
    }

    public int getIconResouce() {
        int resourceId;
        switch (id) {
            case DataItems.bottleId:
                resourceId = R.drawable.item_bottle;
                break;
            case DataItems.orangeId:
                resourceId = R.drawable.item_orange;
                break;
            case DataItems.tambourineId:
                resourceId = R.drawable.item_tambourine;
                break;
            case DataItems.bag1Id:
                resourceId = R.drawable.item_bag01;
                break;
            case DataItems.bag2Id:
                resourceId = R.drawable.item_bag02;
                break;
            case DataItems.bag3Id:
                resourceId = R.drawable.item_bag03;
                break;
            case DataItems.bag4Id:
                resourceId = R.drawable.item_bag04;
                break;
            case DataItems.bag5Id:
                resourceId = R.drawable.item_bag05;
                break;
            case DataItems.bag6Id:
                resourceId = R.drawable.item_bag06;
                break;
            case DataItems.bag7Id:
                resourceId = R.drawable.item_bag07;
                break;
            case DataItems.bag8Id:
                resourceId = R.drawable.item_bag08;
                break;
            case DataItems.bag9Id:
                resourceId = R.drawable.item_bag09;
                break;
            case DataItems.bag10Id:
                resourceId = R.drawable.item_bag10;
                break;
            case DataItems.bag11Id:
                resourceId = R.drawable.item_bag11;
                break;
            case DataItems.bag12Id:
                resourceId = R.drawable.item_bag12;
                break;
            case DataItems.bag13Id:
                resourceId = R.drawable.item_bag_sasa;
                break;
            case DataItems.fourLeafCloverId:
                resourceId = R.drawable.four_leaf_icon;
                break;
            case DataItems.themeId1:
                resourceId = R.drawable.ic_theme;
                break;
            case DataItems.iceCreamId:
                resourceId = R.drawable.item_ice_cream;
                break;
            case DataItems.peanutId:
                resourceId = R.drawable.item_peanut;
                break;
            case DataItems.watermelonId:
                resourceId = R.drawable.item_watermelon;
                break;
            default:
                resourceId = R.drawable.item_banana;
        }

        return resourceId;
    }


    private int id;
    private String actionId;
    private String name;
    private String description;
    private int maxAction;
    private String script;
    private int price;

    public String getTextScript() {
        return textScript;
    }

    public void setTextScript(String textScript) {
        this.textScript = textScript;
    }

    private String textScript;
    private int point;//pts
    private int pointType;//xp


    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    private List<String> imageList;
    private String iconUrl;

    //get data from Server
    public ItemObject(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.actionId = jsonObject.getString("action_id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.iconUrl = jsonObject.getString("image_url");
            this.point = jsonObject.getInt("point");//pts
            this.pointType = jsonObject.getInt("point_type");//xp
            this.maxAction = jsonObject.getInt("max_action");
            this.price = jsonObject.getInt("price");
            this.limit = jsonObject.getInt("limited");
        } catch (JSONException e) {
        }
    }

    public ItemObject(JSONObject jsonObject, boolean isRestore) {
        try {
            this.id = jsonObject.getInt("id");
            this.actionId = jsonObject.getString("action_id");
            this.name = jsonObject.getString("name");
            this.description = jsonObject.getString("description");
            this.iconUrl = jsonObject.getString("image_url");
            this.point = jsonObject.getInt("point");
            this.pointType = jsonObject.getInt("point_type");
            this.maxAction = jsonObject.getInt("max_action");
            if (isRestore) {
                if (!isResource()) {
                    this.setScript(jsonObject.getString("script_list"));
                    this.setTextScript(jsonObject.getString("script_text"));
                    JSONArray array = jsonObject.getJSONArray("image_list");
                    List<String> imageList = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        imageList.add(array.getString(j));
                    }
                    this.setImageList(imageList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //get data from SQLite
    public ItemObject(int id, String actionId, String name, String description, int maxAction, String script, String textScript, int point, int pointType, String iconUrl) {
        this.id = id;
        this.actionId = actionId;
        this.name = name;
        this.description = description;
        this.maxAction = maxAction;
        this.script = script;
        this.textScript = textScript;
        this.point = point;
        this.pointType = pointType;
        this.iconUrl = iconUrl;
    }

    //Insert Resource
    public ItemObject(int id, String actionId, String name, String description, int maxAction, String textScript, int point, int pointType) {
        this.id = id;
        this.actionId = actionId;
        this.name = name;
        this.description = description;
        this.maxAction = maxAction;
        this.script = "";
        this.point = point;
        this.pointType = pointType;
        this.textScript = textScript;
    }

    public List<Bitmap> getBitmapScript(Context context) {
        List<Bitmap> bitmapList = new ArrayList<>();
        String[] items = getScript().split(";");
        for (String item : items) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image/resources", item + ".png");
            Uri imageUri = Uri.fromFile(file);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                bitmapList.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmapList;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public static void setImage(Context context, int itemId, ImageView view) {
        ItemObject itemObject = new DataItems(context).getItem(itemId);
        if (itemObject.isResource()) {
            view.setImageResource(itemObject.getIconResouce());
        } else {
            Picasso.with(context).load(itemObject.getIconUrl()).into(view);
        }
    }
}
