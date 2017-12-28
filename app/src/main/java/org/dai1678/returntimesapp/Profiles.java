package org.dai1678.returntimesapp;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Profiles extends RealmObject {

    private int listId;

    private RealmList<ProfileItems> items;

    //getter
    public int getListId() {
        return listId;
    }

    public RealmList<ProfileItems> getItems() {
        return items;
    }

    //setter
    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setItems(RealmList<ProfileItems> items) {
        this.items = items;
    }
}
