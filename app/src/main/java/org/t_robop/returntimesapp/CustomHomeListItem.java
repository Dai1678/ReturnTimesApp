package org.t_robop.returntimesapp;

import android.graphics.Bitmap;

public class CustomHomeListItem {
    private Bitmap mThumbnail = null;
    private String mDestination = null;
    private String mAddress = null;

    public CustomHomeListItem(Bitmap thumbnail, String destination, String address){
        mThumbnail = thumbnail;
        mDestination = destination;
        mAddress = address;
    }

    public void setThumbnail(Bitmap thumbnail){
        mThumbnail = thumbnail;
    }

    public void setDestination(String destination){
        mDestination = destination;
    }

    public void setAddress(String address){
        mAddress = address;
    }

    public Bitmap getThumbnail(){
        return mThumbnail;
    }

    public String getDestination(){
        return mDestination;
    }

    public String getAddress(){
        return mAddress;
    }
}
