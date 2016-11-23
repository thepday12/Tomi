package com.neu.tomi.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thep on 10/18/2015.
 */
public class BeaconObject {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Id;
   private String Name;
    public BeaconObject(JSONObject jsonObject){
        try {
            setId(jsonObject.getString("id"));
            setName(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public BeaconObject (String id, String name){
        setId(id);
        setName(name);
    }
}
