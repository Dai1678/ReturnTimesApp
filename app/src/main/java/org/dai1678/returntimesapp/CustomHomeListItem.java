package org.dai1678.returntimesapp;

import android.graphics.Bitmap;

public class CustomHomeListItem {

    private Bitmap destinationImage;
    private String listId;
    private String destination;
    private String place;
    private String destinationLatitude;
    private String destinationLongitude;
    private String addressName;
    private String addressMail;

    public CustomHomeListItem(Bitmap thumbnail, String destination, String place, String addressName, String addressMail){
        this.destinationImage = thumbnail;
        //this.listId = listId;
        this.destination = destination;
        this.place = place;
        this.addressName = addressName;
        this.addressMail = addressMail;
    }

    public void setDestinationImage(Bitmap destinationImage){
        this.destinationImage = destinationImage;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public void setAddressName(String addressName){
        this.addressName = addressName;
    }

    public void setAddressMail(String addressMail){
        this.addressMail = addressMail;
    }

    public Bitmap getDestinationImage(){
        return this.destinationImage;
    }

    public String getListId() {
        return listId;
    }

    public String getDestination(){
        return this.destination;
    }

    public String getPlace() {
        return place;
    }

    public String getLatitude() {
        return destinationLatitude;
    }

    public String getLongitude() {
        return destinationLongitude;
    }

    public String getAddressName(){
        return this.addressName;
    }

    public String getAddressMail(){
        return this.addressMail;
    }
}
