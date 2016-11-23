package com.neu.tomi.object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thep on 10/22/2015.
 */
public class ActionObject {
    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    public List<BonusItemObject> getBonusItemObjects() {
        return bonusItemObjects;
    }

    public void setBonusItemObjects(List<BonusItemObject> bonusItemObjects) {
        this.bonusItemObjects = bonusItemObjects;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    int action;
    int point;
    int xp;
    List<BonusItemObject> bonusItemObjects;
    public ActionObject(JSONObject jsonObject){
        try {
            setPoint(jsonObject.getInt("point"));
            setXp(jsonObject.getInt("xp"));
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            bonusItemObjects = new ArrayList<BonusItemObject>();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object =jsonArray.getJSONObject(i);
                bonusItemObjects.add(new BonusItemObject(object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public ActionObject(int point, int xp, List<BonusItemObject> bonusItemObjects, int action){
        setPoint(point);
        setXp(xp);
        setBonusItemObjects(bonusItemObjects);
        setAction(action);//0-non 1- active
    }
}
