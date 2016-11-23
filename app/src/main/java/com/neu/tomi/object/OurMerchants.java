package com.neu.tomi.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thep on 5/27/2016.
 */
public class OurMerchants {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String name;
    private String url;
    private String imageLink;
    private String description;

    public OurMerchants(JSONObject jsonObject){
        try {
            name =jsonObject.getString("name");
            url =jsonObject.getString("url");
            imageLink =jsonObject.getString("image_link");
            description =jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
