package com.schoofi.utils;

import java.util.ArrayList;

/**
 * Created by Schoofi on 04-09-2016.
 */
public class ParentStudentFessUnpaidMultilevelParentVO {

    private String feesStartDate,feesEndDate,totalAmount,payBy;
    private ArrayList<ParentStudentFeesUnpaidMultilevelChildVO> Items;

    public ArrayList<ParentStudentFeesUnpaidMultilevelChildVO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ParentStudentFeesUnpaidMultilevelChildVO> items) {
        Items = items;
    }

    public String getFeesStartDate() {
        return feesStartDate;
    }

    public void setFeesStartDate(String feesStartDate) {
        this.feesStartDate = feesStartDate;
    }

    public String getFeesEndDate() {
        return feesEndDate;
    }

    public void setFeesEndDate(String feesEndDate) {
        this.feesEndDate = feesEndDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayBy() {
        return payBy;
    }

    public void setPayBy(String payBy) {
        this.payBy = payBy;
    }


}
