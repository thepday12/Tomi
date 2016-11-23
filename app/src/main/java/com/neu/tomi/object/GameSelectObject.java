package com.neu.tomi.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thep on 5/18/2016.
 */
public class GameSelectObject {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String name;
    private String icon;
    private String link;

    public GameSelectObject(JSONObject jsonObject){
        try {
            name =jsonObject.getString("Name");
            icon =jsonObject.getString("Icon");
            link =jsonObject.getString("Link");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
