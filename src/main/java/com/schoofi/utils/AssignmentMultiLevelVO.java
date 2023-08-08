package com.schoofi.utils;

import java.util.ArrayList;

/**
 * Created by Schoofi on 28-02-2017.
 */

public class AssignmentMultiLevelVO {

    String title;
    String teacherName;
    String teacherfile;
    String description;
    String subjectName;
    String studentFile;
    String assignId;
    String optionalSubject;
    String createddate;
    String teacherId;
    String studentFiles;
    String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStudentFiles() {
        return studentFiles;
    }

    public void setStudentFiles(String studentFiles) {
        this.studentFiles = studentFiles;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubmitdate() {
        return submitdate;
    }

    public void setSubmitdate(String submitdate) {
        this.submitdate = submitdate;
    }

    String submitdate;

    public ArrayList<AssignmentMultiLevelVO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<AssignmentMultiLevelVO> items) {
        Items = items;
    }

    private ArrayList<AssignmentMultiLevelVO> Items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherfile() {
        return teacherfile;
    }

    public void setTeacherfile(String teacherfile) {
        this.teacherfile = teacherfile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStudentFile() {
        return studentFile;
    }

    public void setStudentFile(String studentFile) {
        this.studentFile = studentFile;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getOptionalSubject() {
        return optionalSubject;
    }

    public void setOptionalSubject(String optionalSubject) {
        this.optionalSubject = optionalSubject;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }
}
