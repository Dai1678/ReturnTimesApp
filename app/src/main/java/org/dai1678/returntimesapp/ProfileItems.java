package org.dai1678.returntimesapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProfileItems extends RealmObject {

    @PrimaryKey
    private int profileId;

    private String placeName;
    private int image;

    private String address;

    private String mail;

    //getter
    public int getProfileId(){ return profileId; }

    public String getPlaceName() { return placeName; }

    public int getImage() { return image; }

    public String getAddress() { return address; }

    public String getMail() { return mail; }

    //setter
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public void setImage(int image) { this.image = image; }

    public void setAddress(String address) { this.address = address; }

    public void setMail(String mail) { this.mail = mail; }
}
