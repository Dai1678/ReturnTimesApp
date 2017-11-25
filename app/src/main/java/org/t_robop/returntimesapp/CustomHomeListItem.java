package org.t_robop.returntimesapp;

import android.graphics.Bitmap;

public class CustomHomeListItem {
    private Bitmap mThumbnail = null;
    private String mAddressName = null;
    private String mDestination = null;
    private String mAddressMail = null;

    public CustomHomeListItem(Bitmap thumbnail, String addressName, String destination, String addressMail){
        mThumbnail = thumbnail;
        mAddressName = addressName;
        mDestination = destination;
        mAddressMail = addressMail;
    }

    public void setThumbnail(Bitmap thumbnail){
        mThumbnail = thumbnail;
    }

    public void setAddressName(String name){
        mAddressName = name;
    }

    public void setDestination(String destination){
        mDestination = destination;
    }

    public void setAddressMail(String address){
        mAddressMail = address;
    }

    public Bitmap getThumbnail(){
        return mThumbnail;
    }

    public String getAddressName(){
        return mAddressName;
    }

    public String getDestination(){
        return mDestination;
    }

    public String getAddressMail(){
        return mAddressMail;
    }
}
