package com.schoofi.utils;

import java.util.ArrayList;

/**
 * Created by Schoofi on 30-04-2019.
 */

public class RoyalityParentVO {

    private String feeDate,feeTotalAmount;
    private ArrayList<RoyalityChildVO> Items;

    public ArrayList<RoyalityChildVO> getItems() {
        return Items;
    }

    public String getFeeDate() {
        return feeDate;
    }

    public void setFeeDate(String feeDate) {
        this.feeDate = feeDate;
    }

    public String getFeeTotalAmount() {
        return feeTotalAmount;
    }

    public void setFeeTotalAmount(String feeTotalAmount) {
        this.feeTotalAmount = feeTotalAmount;
    }

    public void setItems(ArrayList<RoyalityChildVO> items) {
        Items = items;
    }


}
