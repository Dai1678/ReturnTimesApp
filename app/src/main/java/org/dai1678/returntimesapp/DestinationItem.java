package org.dai1678.returntimesapp;

import java.util.Arrays;
import java.util.List;

public class DestinationItem {

    private final List<String> itemNameList = Arrays.asList("家","レストラン","病院","銀行","郵便局","駅");
    private final List<Integer> itemImageList = Arrays.asList(R.mipmap.ic_house,R.mipmap.ic_restaurant,R.mipmap.ic_hospital,R.mipmap.ic_bank
                                                                ,R.mipmap.ic_postoffice,R.mipmap.ic_station);

    public List<String> getItemNameList() {
        return itemNameList;
    }

    public List<Integer> getItemImageList() {
        return itemImageList;
    }
}
