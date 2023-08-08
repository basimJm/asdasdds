package com.schoofi.utils;

/**
 * Created by Schoofi on 02-11-2016.
 */

public class QuizQuestionScreenVO {

    String solutionName;
    String solutionType;
    String solutionId;

    public QuizQuestionScreenVO(String solutionName, String solutionType, String solutionId) {
        this.solutionName = solutionName;
        this.solutionType = solutionType;
        this.solutionId = solutionId;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }



    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getSolutionType() {
        return solutionType;
    }

    public void setSolutionType(String solutionType) {
        this.solutionType = solutionType;
    }
}
