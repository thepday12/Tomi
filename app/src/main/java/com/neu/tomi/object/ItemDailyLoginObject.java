package com.neu.tomi.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thep on 3/29/2016.
 */
public class ItemDailyLoginObject {
    private int id;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private int quantity;
    private int day;
    private String imageUrl;

    public  ItemDailyLoginObject(JSONObject jsonObject){
        try {
            id= jsonObject.getInt("ItemID");
            quantity= jsonObject.getInt("Quantity");
            day= jsonObject.getInt("Day");
            imageUrl= jsonObject.getString("Link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
