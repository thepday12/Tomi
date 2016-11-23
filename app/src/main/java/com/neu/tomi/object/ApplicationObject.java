package com.neu.tomi.object;

import android.graphics.drawable.Drawable;

/**
 * Created by Thep on 10/23/2015.
 */
public class ApplicationObject {
    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    private String packetName;
    private String name;
    private Drawable icon;
    public ApplicationObject(String packetName, String name, Drawable icon){
        setPacketName(packetName);
        setName(name);
        setIcon(icon);
    }
}
