package com.neu.tomi.model;

import android.util.Log;

import java.util.Observable;


/**
 * Created by Thep on 11/2/2015.
 */
public class ShopModel extends Observable {




    public void changeInfoShop(){
        Log.e("CALLLL", "TRUE");
        setChanged();
        notifyObservers(true);
    }
    public void errorShop(){
        setChanged();
        notifyObservers(false);
    }
}
