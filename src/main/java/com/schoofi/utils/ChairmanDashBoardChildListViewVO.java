package com.schoofi.utils;

/**
 * Created by Schoofi on 02-01-2017.
 */

public class ChairmanDashBoardChildListViewVO {

    String examName;
    String totalExamStudents;
    String examGrade1;
    String examGrade2;
    String examGrade3;
    String examGrade4;

    public String getExamGrade5() {
        return examGrade5;
    }

    public void setExamGrade5(String examGrade5) {
        this.examGrade5 = examGrade5;
    }

    public String getExamGrade3() {
        return examGrade3;
    }

    public void setExamGrade3(String examGrade3) {
        this.examGrade3 = examGrade3;
    }

    public String getExamGrade4() {
        return examGrade4;
    }

    public void setExamGrade4(String examGrade4) {
        this.examGrade4 = examGrade4;
    }

    public String getExamGrade2() {
        return examGrade2;
    }

    public void setExamGrade2(String examGrade2) {
        this.examGrade2 = examGrade2;
    }

    String examGrade5;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getTotalExamStudents() {
        return totalExamStudents;
    }

    public void setTotalExamStudents(String totalExamStudents) {
        this.totalExamStudents = totalExamStudents;
    }

    public String getExamGrade1() {
        return examGrade1;
    }

    public void setExamGrade1(String examGrade1) {
        this.examGrade1 = examGrade1;
    }

    public ChairmanDashBoardChildListViewVO(String examName, String totalExamStudents, String examGrade1, String examGrade2, String examGrade3, String examGrade4, String examGrade5) {
        this.examName = examName;
        this.totalExamStudents = totalExamStudents;
        this.examGrade1 = examGrade1;
        this.examGrade2 = examGrade2;
        this.examGrade3 = examGrade3;
        this.examGrade4 = examGrade4;
        this.examGrade5 = examGrade5;
    }


}
