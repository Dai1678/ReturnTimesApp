package org.dai1678.returntimesapp;


import android.graphics.Bitmap;

public class CustomProfileListItem {

    private Bitmap profileItemImage;
    private String profileHint;

    CustomProfileListItem(Bitmap profileItemImage, String profileHint){
        this.profileItemImage = profileItemImage;
        this.profileHint = profileHint;
    }

    public void setProfileItemImage(Bitmap profileItemImage){
        this.profileItemImage = profileItemImage;
    }

    public void setProfileHint(String profileHint){
        this.profileHint = profileHint;
    }

    public Bitmap getProfileItemImage(){
        return this.profileItemImage;
    }

    public String getProfileHint(){
        return this.profileHint;
    }

}
