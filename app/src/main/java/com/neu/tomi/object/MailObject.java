package com.neu.tomi.object;

import android.util.Log;

import com.neu.tomi.ultity.Global;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thep on 11/13/2015.
 */
public class MailObject {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkCaption() {
        return linkCaption;
    }

    public void setLinkCaption(String linkCaption) {
        this.linkCaption = linkCaption;
    }

    private String id;
    private String title;
    private String content;
    private String link;
    private String date;
    private String linkCaption;

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    private int linkType;
    private boolean state;

    public MailObject(String id, String title, String content, String link, boolean state,int linkType,String linkCaption) {
        setTitle(title);
        setContent(content);
        setLink(link);
        setState(state);
        setId(id);
        setDate(Global.getDate());
        setLinkType(linkType);
        setLinkCaption(linkCaption);
    }


    public String getDate() {
        String []items =date.split(" ");
        return items[0];
    }

    public void setDate(String date) {
        this.date = date;
    }
}

