package org.dai1678.returntimesapp;

import java.util.Arrays;
import java.util.List;

public class DestinationItem {

    private final List<String> itemNameList = Arrays.asList("家","レストラン","病院","銀行","郵便局","駅");

    private final List<Integer> itemDrawableList = Arrays.asList(R.drawable.ic_home, R.drawable.ic_restaurant, R.drawable.ic_hospital, R.drawable.ic_monetization
                                                                , R.drawable.ic_local_post_office, R.drawable.ic_train);

    public List<String> getItemNameList() {
        return itemNameList;
    }

    public List<Integer> getItemDrawableList() {
        return itemDrawableList;
    }
}
