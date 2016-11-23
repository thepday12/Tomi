package com.neu.tomi.object;

import org.json.JSONObject;

/**
 * Created by Thep on 12/16/2015.
 */
public class ItemQTY {
    public ItemQTY(ItemObject itemObject, int qty) {
        this.itemObject = itemObject;
        this.qty = qty;
    }


    public ItemObject getItemObject() {
        return itemObject;
    }

    public void setItemObject(ItemObject itemObject) {
        this.itemObject = itemObject;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    private ItemObject itemObject;
    private int qty;
}
