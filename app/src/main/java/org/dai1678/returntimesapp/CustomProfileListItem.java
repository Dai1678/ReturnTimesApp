package org.dai1678.returntimesapp;


import android.graphics.Bitmap;

public class CustomProfileListItem {

    private Bitmap mProfileItemImage = null;
    private String mProfileHint = null;

    public CustomProfileListItem(Bitmap profileItemImage,String profileHint){
        mProfileItemImage = profileItemImage;
        mProfileHint = profileHint;
    }

    public void setProfileItemImage(Bitmap profileItemImage){
        mProfileItemImage = profileItemImage;
    }

    public void setProfileHint(String profileHint){
        mProfileHint = profileHint;
    }

    public Bitmap getProfileItemImage(){
        return mProfileItemImage;
    }

    public String getProfileHint(){
        return mProfileHint;
    }

}
