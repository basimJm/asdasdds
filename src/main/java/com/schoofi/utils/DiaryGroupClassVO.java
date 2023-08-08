package com.schoofi.utils;

import java.util.ArrayList;

public class DiaryGroupClassVO {

    String className;
    String sectionName;
    String classId;
    String sectionId;

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }

    String isAdded;

    public ArrayList<DiaryGroupStudentVO> getItems() {
        return items;
    }

    public void setItems(ArrayList<DiaryGroupStudentVO> items) {
        this.items = items;
    }

    private ArrayList<DiaryGroupStudentVO> items;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
