package com.neu.tomi.object;

/**
 * Created by Thep on 12/7/2015.
 */
public class FoodObject {
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

    public FoodObject(ItemObject itemObject, int qty) {
        this.itemObject = itemObject;
        this.qty = qty;
    }

    private ItemObject itemObject;
    private int qty;
}
