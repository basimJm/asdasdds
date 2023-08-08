package com.schoofi.utils;

/**
 * Created by Schoofi on 31-01-2017.
 */

public class DiaryFilterScreenLeftVO {

    String listItemName,listitemId;

    public String getListItemName() {
        return listItemName;
    }

    public void setListItemName(String listItemName) {
        this.listItemName = listItemName;
    }

    public String getListitemId() {
        return listitemId;
    }

    public void setListitemId(String listitemId) {
        this.listitemId = listitemId;
    }

    public DiaryFilterScreenLeftVO(String listItemName, String listitemId) {

        this.listItemName = listItemName;
        this.listitemId = listitemId;
    }
}
