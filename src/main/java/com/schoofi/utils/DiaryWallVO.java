package com.schoofi.utils;

import java.util.ArrayList;

public class DiaryWallVO {

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<DiaryWallChildVO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<DiaryWallChildVO> items) {
        Items = items;
    }

    private ArrayList<DiaryWallChildVO> Items;
}
