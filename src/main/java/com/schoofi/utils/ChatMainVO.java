package com.schoofi.utils;

import java.util.ArrayList;

public class ChatMainVO {

    String Date;
    private ArrayList<ChatVO> items;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ArrayList<ChatVO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ChatVO> items) {
        this.items = items;
    }
}
