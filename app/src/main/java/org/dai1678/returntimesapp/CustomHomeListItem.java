package org.dai1678.returntimesapp;

import android.graphics.Bitmap;

public class CustomHomeListItem {
    private Bitmap destinationImage;
    private String addressName;
    private String destination;
    private String addressMail;

    public CustomHomeListItem(Bitmap thumbnail, String addressName, String destination, String addressMail){
        this.destinationImage = thumbnail;
        this.addressName = addressName;
        this.destination = destination;
        this.addressMail = addressMail;
    }

    public void setThumbnail(Bitmap destinationImage){
        this.destinationImage = destinationImage;
    }

    public void setAddressName(String addressName){
        this.addressName = addressName;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setAddressMail(String addressMail){
        this.addressMail = addressMail;
    }

    public Bitmap getThumbnail(){
        return this.destinationImage;
    }

    public String getAddressName(){
        return this.addressName;
    }

    public String getDestination(){
        return this.destination;
    }

    public String getAddressMail(){
        return this.addressMail;
    }
}
