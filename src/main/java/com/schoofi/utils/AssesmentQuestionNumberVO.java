package com.schoofi.utils;

/**
 * Created by Schoofi on 17-12-2019.
 */

public class AssesmentQuestionNumberVO {

    String fieldName,fieldId;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public AssesmentQuestionNumberVO(String fieldName, String fieldId) {
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }
}
