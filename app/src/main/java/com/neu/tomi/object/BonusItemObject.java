package com.neu.tomi.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thep on 10/22/2015.
 */
public class BonusItemObject {
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemBonus() {
        return itemBonus;
    }

    public void setItemBonus(int itemBonus) {
        this.itemBonus = itemBonus;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    private String adId;
    private int itemId;
    private int itemBonus;
    private String imageURL;

    public BonusItemObject(JSONObject jsonObject){
        try {
            setItemId(jsonObject.getInt("item_type"));
            setItemBonus(jsonObject.getInt("item_qty"));
            setImageURL(jsonObject.getString("image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public BonusItemObject(String adId, int itemId, int itemBonus, String imageURL){
        setAdId(adId);
        setItemId(itemId);
        setItemBonus(itemBonus);
        setImageURL(imageURL);
    }


}
