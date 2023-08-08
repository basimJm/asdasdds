package com.schoofi.utils;

import java.util.ArrayList;

/**
 * Created by Schoofi on 23-03-2017.
 */

public class SchoolPlannerListParsingMainVO {

    String date;

    public ArrayList<SchoolPlannerListParsingVO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<SchoolPlannerListParsingVO> items) {
        Items = items;
    }

    private ArrayList<SchoolPlannerListParsingVO> Items;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
