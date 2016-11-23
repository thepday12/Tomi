package com.neu.tomi.object;

/**
 * Created by Thep on 1/10/2016.
 */
public class ActionBonusItem {
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ActionBonusItem(String imageUri, int qty) {
        this.imageUri = imageUri;
        this.qty = qty;
    }

    private String imageUri;
    private int qty;
}
