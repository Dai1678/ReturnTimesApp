package org.dai1678.returntimesapp;

public class GridItem {

    private String itemName;
    private Integer itemImage;
    private boolean isSelected;

    public GridItem(String itemName, Integer itemImage){
        this.itemName = itemName;
        this.itemImage = itemImage;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public void setItemImage(Integer itemImage){
        this.itemImage = itemImage;
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    public String getItemName(){
        return this.itemName;
    }

    public Integer getItemImage(){
        return this.itemImage;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
