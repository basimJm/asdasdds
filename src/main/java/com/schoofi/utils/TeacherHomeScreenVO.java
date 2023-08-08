package com.schoofi.utils;

public class TeacherHomeScreenVO {

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

	public TeacherHomeScreenVO(String fieldName, String fieldId) {
		super();
		this.fieldName = fieldName;
		this.fieldId = fieldId;
	}

	

}
