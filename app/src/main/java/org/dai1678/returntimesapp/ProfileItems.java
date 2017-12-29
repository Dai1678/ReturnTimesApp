package org.dai1678.returntimesapp;

import io.realm.RealmObject;

public class ProfileItems extends RealmObject {

    private int profileId;

    private String destinationName;
    private int imagePosition;

    private String placeName;
    private double latitude;
    private double longitude;

    private String contact;
    private String mail;

    //getter
    public int getProfileId(){ return profileId; }

    public String getDestinationName() { return destinationName; }

    public int getImagePosition() { return imagePosition; }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getContact() {
        return contact;
    }

    public String getMail() { return mail; }

    //setter
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }

    public void setImagePosition(int imagePosition) { this.imagePosition = imagePosition; }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setMail(String mail) { this.mail = mail; }
}
