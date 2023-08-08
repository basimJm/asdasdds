package com.schoofi.utils;

import java.util.ArrayList;

/**
 * Created by Schoofi on 19-01-2017.
 */

public class DiaryVO {

    String date;
    private ArrayList<DiarySubVO> Items;

    public ArrayList<AssignmentMultiLevelVO> getItems1() {
        return Items1;
    }

    public void setItems1(ArrayList<AssignmentMultiLevelVO> items1) {
        Items1 = items1;
    }

    private ArrayList<AssignmentMultiLevelVO> Items1;

    public ArrayList<DiarySubVO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<DiarySubVO> items) {
        Items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
