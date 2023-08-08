package com.schoofi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	
	private String appName="Schoofi";
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	public static final String USER_EMAIL_ID="u_email_id";
	public static final String STUDENT_SECTION_ID="sec_id";
	public static final String TOKEN="token";
	public static final String USER_ID="u_id";
	public static final String STUDENT_ID="stu_id";
	public static final String SCHOOL_ID="sch_id";
	public static final String STUDENT_CLASS_ID="cls_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_NAME1 = "demo1";

	public static final String FEEDBACK_ID = "feedback_id";
	public static final String USER_TYPE = "type";
	public static final String USER_ROLE_ID = "";
	public static final String TEACHER_ID = "teac_id";
	public static final String IS_LOGGED_IN="IS_LOGGED_IN";
	public static final String ScreenName = "screenName";
	public static final String INSTITUTION_ID = "ins_id";
	public static final String TOTAL_STUDENTS = "total_stu";
	public static final String STUDENT_NAME = "stu_name";
	public static final String DEVICE_ID = "device_id";
	public static final String SECTION_NAME = "section_name";
	public static final String NAME = "Name";
	public static final String CREATOR_ID = "creator_id";
	public static final String TOTAL_STUDENT_CLASS = "total_stu_cls";
	public static final String PHONE_NUMBER = "user_phone";
	public static final String BUS_ROUTE_NO = "route_no";
	public static final String BUS_LONGITUDE = "longitude";
	public static final String BUS_LATTITUDE = "latitude";
	public static final String BUS_NUMBER = "bus_number";
	public static final String PAID_AMOUNT = "paid_Amount";
	public static final String PRODUCT_INFO = "product_Info";
	public static final String USER_PASSWORD = "demo";
	public static final String SCHOOL_LATTITUDE = "sch_latitude";
	public static final String SCHOOL_LONGITUDE = "sch_longitude";
	public static final String ADDMISSION_NUMBER = "admn_No";
	public static final String EVENT_ADD_CHARGES = "event_add_charges";
	public static final String FEE_ADD_CHARGES = "fee_add_charges";
	public static final String PERMISSIONS = "permission";
	public static final String SESSION = "session";
	public static final String BOARD = "board";
	public static final String TEACHER_ID1 = "teac_id";
	public static final String SCHOOL_NAME = "school_name";
	public static final String CHAIRMAN_ASSIGNMENT_CLASS_ID = "class_id";
	public static final String CHAIRMAN_ASSIGNMENT_CLASS_NAME = "class_name";
	public static final String CHAIRMAN_ASSIGNMENT_SECTION_ID = "section_id";
	public static final String CHAIRMAN_ASSIGNMENT_SECTION_NAME = "section_name";
	public static final String CHAIRMAN_ASSIGNMENT_TEACHER_NAME = "teac_name";
	public static final String CHAIRMAN_ASSIGNMENT_TECAHER_ID = "teac_id";
	public static final String CHAIRMAN_ASSIGNMENT_SUBJECT_ID = "subject_id";
	public static final String CHAIRMAN_ASSIGNMENT_SUBJECT_NAME = "subject_name";
	public static final String TEACHER_BADGE_COUNT ="pending_leaves_count";
	public static final String HEALTH_AND_SAFETY = "health_safety";
	public static final String QR_CODE_STRING = "qr_code";
	public static final String PARENT_QR_CODE_STRING = "parent_qr_code";
	public static final String VISITOR_QR_CODE_STRING = "visitor_qr_code";
	public static final String VISITOR_TYPE = "visitor_type";
	public static final String EMPLOYEE_ID = "emp_id";
	public static final String EMPLOYEE_TYPE = "employment_type";
	public static final String DEPARTMENT_ID = "dept_id";
	public static final String DEPARTMENT_NAME = "dept_name";
	public static final String SCHOOL_TYPE = "school_type";
	public static final String STUDENT_SUBJECT_ID = "subject_id";
	public static final String STUDENT_GENDER = "gender";
    public static final String STUDENT_SEMESTER = "semester";
    public static final String STUDENT_COURSE_ID = "course_id";
    public static final String FULL_ROLE_ID = "role_id";
    public static final String IS_HOSTEL_STUDENT = "is_hostel";
    public static final String SCHOOL_SCHEDULE_LEVEL = "schedule_level";
    public static final String STUDY_PLANNER = "yearlyPlanner";
    public static final String ADDS_PERMISSION = "adds";


	
	public String userEmailId;
	public String studentSectionId;
	public String token;
	public String userId;
	public String studentId;
	public String schoolId;
	public String studentClassId;
	public String userName;
	public String feedbackId;
	public String userType;
	public String userRoleId;
	public String teachId;
	public boolean isLoggedIn;
	public String screenName;
	public String institutionId;
	public String totalStudents;
	public String studentName;
	public String deviceId;
	public String sectionName;
	public String Name;
	public String creatorId;
	public String totalStudentClass;
	public String phoneNumber;
	public String busRouteNo;
	public String lattitude;
	public String longitude;
	public String busNumber;
	public String paidAmount;
	public String productInfo;
	public String userPassword;
	public String schoolLattitude;
	public String schoolLongitude;
	public String addmissionNumber;
	public String eventAddCharges;
	public String feesAddCharges;
	public String permissions;
	public String board;
	public String session1;
	public String teacherId1;
	public String schoolName;
	public String chairmanAssignmentClassName;
	public String chairmanAssignmentClassId;
	public String chairmanAssignmentSectionId;
	public String chairmanAssignmentSectionName;
	public String chairmanAssignmentTeacherName;
	public String chairmanAssignmentTeacherId;
	public String chairmanAssignmentSubjectName;
	public String chairmanAssgnmentSubjectId;
	public String teacherBadgeCount;
	public String healthAndSafety;
	public String qrCode;
	public String parentQrCode;
	public String visitorQrCode;
	public String visitorType;
	public String employeeId;
	public String employeeType;
	public String departmentId;
	public String departmentName;
	public String schoolType;
	public String studentSubjectId;
	public String studentSemester;
	public String studentGender;
	public String studentCourse;
	public String fullRoleId;
	public String userName1;
	public String isHostelStudent;
	public String schoolScheduleLevel;
	public  String studyPlanner;
	public String  addsPermission;

	
	
	private static Preferences instance;
	private Preferences(){
		
		
		
	}
	public static synchronized Preferences getInstance(){
		if(instance==null)
			instance=new Preferences();
		
		return instance;
	}
	
	 public void loadPreference(Context context)
	 {
			preferences=context.getSharedPreferences(appName, Activity.MODE_PRIVATE);
			userEmailId=preferences.getString(USER_EMAIL_ID, null);
			userId=preferences.getString(USER_ID, null);
			studentClassId=preferences.getString(STUDENT_CLASS_ID, null);
			studentId=preferences.getString(STUDENT_ID, null);
			studentSectionId=preferences.getString(STUDENT_SECTION_ID, null);
			token=preferences.getString(TOKEN, null);
			schoolId=preferences.getString(SCHOOL_ID, null);
			userName = preferences.getString(USER_NAME, null);
			feedbackId = preferences.getString(FEEDBACK_ID,null);
			userType = preferences.getString(USER_TYPE, null);
			userRoleId = preferences.getString(USER_ROLE_ID, null);
			teachId = preferences.getString(TEACHER_ID,null);
			isLoggedIn=preferences.getBoolean(IS_LOGGED_IN, false);
			screenName = preferences.getString(ScreenName, null);
			institutionId = preferences.getString(INSTITUTION_ID, null);
			totalStudents = preferences.getString(TOTAL_STUDENTS, null);
			studentName = preferences.getString(STUDENT_NAME, null);
			deviceId = preferences.getString(DEVICE_ID, null);
			sectionName = preferences.getString(SECTION_NAME, null);
			Name = preferences.getString(NAME, null);
			creatorId = preferences.getString(CREATOR_ID, null);
			totalStudentClass = preferences.getString(TOTAL_STUDENT_CLASS, null);
		    phoneNumber = preferences.getString(PHONE_NUMBER,null);
		    busRouteNo = preferences.getString(BUS_ROUTE_NO,null);
		    longitude = preferences.getString(BUS_LONGITUDE,null);
		    lattitude = preferences.getString(BUS_LATTITUDE,null);
		    busNumber = preferences.getString(BUS_NUMBER,null);
		    paidAmount = preferences.getString(PAID_AMOUNT,null);
		    productInfo = preferences.getString(PRODUCT_INFO,null);
		    userPassword = preferences.getString(USER_PASSWORD,null);
		    schoolLattitude = preferences.getString(SCHOOL_LATTITUDE,null);
		    schoolLongitude = preferences.getString(SCHOOL_LONGITUDE,null);
		    addmissionNumber = preferences.getString(ADDMISSION_NUMBER,null);
		    eventAddCharges = preferences.getString(EVENT_ADD_CHARGES,null);
		    feesAddCharges = preferences.getString(FEE_ADD_CHARGES,null);
		    permissions = preferences.getString(PERMISSIONS,null);
		    board = preferences.getString(BOARD,null);
		    session1 = preferences.getString(SESSION,null);
		    teacherId1 = preferences.getString(TEACHER_ID1,null);
		    schoolName = preferences.getString(SCHOOL_NAME,null);
		    chairmanAssignmentClassName = preferences.getString(CHAIRMAN_ASSIGNMENT_CLASS_NAME,null);
		    chairmanAssignmentClassId = preferences.getString(CHAIRMAN_ASSIGNMENT_CLASS_ID,null);
		    chairmanAssignmentSectionName = preferences.getString(CHAIRMAN_ASSIGNMENT_SECTION_NAME,null);
		    chairmanAssignmentSectionId = preferences.getString(CHAIRMAN_ASSIGNMENT_SECTION_ID,null);
		    chairmanAssignmentTeacherName = preferences.getString(CHAIRMAN_ASSIGNMENT_TEACHER_NAME,null);
		    chairmanAssignmentTeacherId = preferences.getString(CHAIRMAN_ASSIGNMENT_TECAHER_ID,null);
		    chairmanAssignmentSubjectName = preferences.getString(CHAIRMAN_ASSIGNMENT_SUBJECT_NAME,null);
		    chairmanAssgnmentSubjectId = preferences.getString(CHAIRMAN_ASSIGNMENT_SUBJECT_ID,null);
		    teacherBadgeCount = preferences.getString(TEACHER_BADGE_COUNT,null);
		    healthAndSafety = preferences.getString(HEALTH_AND_SAFETY,null);
		    qrCode = preferences.getString(QR_CODE_STRING,null);
		    parentQrCode = preferences.getString(PARENT_QR_CODE_STRING,null);
		    visitorQrCode = preferences.getString(VISITOR_QR_CODE_STRING,null);
		    visitorType = preferences.getString(VISITOR_TYPE,null);
		    employeeId = preferences.getString(EMPLOYEE_ID,null);
		    employeeType = preferences.getString(EMPLOYEE_TYPE,null);
		    departmentId = preferences.getString(DEPARTMENT_ID,null);
		    departmentName = preferences.getString(DEPARTMENT_NAME,null);
		    schoolType = preferences.getString(SCHOOL_TYPE,null);
		    studentSubjectId = preferences.getString(STUDENT_SUBJECT_ID,null);
		    studentSemester = preferences.getString(STUDENT_SEMESTER,null);
		    studentGender = preferences.getString(STUDENT_GENDER,null);
		    studentCourse = preferences.getString(STUDENT_COURSE_ID,null);
		    fullRoleId = preferences.getString(FULL_ROLE_ID,null);
		    userName1 = preferences.getString(USER_NAME1,null);
		    isHostelStudent = preferences.getString(IS_HOSTEL_STUDENT,null);
		    schoolScheduleLevel = preferences.getString(SCHOOL_SCHEDULE_LEVEL,null);
		    studyPlanner = preferences.getString(STUDY_PLANNER,null);
		    addsPermission = preferences.getString(ADDS_PERMISSION,null);
					
			
	 }
	 
	 public void savePreference(Context context)
	 {
			preferences=context.getSharedPreferences(appName, Activity.MODE_PRIVATE);
			editor=preferences.edit();
			editor.putString(USER_EMAIL_ID,userEmailId);
			editor.putString(USER_ID,userId);
			editor.putString(STUDENT_CLASS_ID,studentClassId);
			editor.putString(STUDENT_ID,studentId);
			editor.putString(STUDENT_SECTION_ID,studentSectionId);
			editor.putString(TOKEN,token);
			editor.putString(SCHOOL_ID,schoolId);
			editor.putString(USER_NAME, userName);
			editor.putString(FEEDBACK_ID, feedbackId);
			editor.putString(USER_TYPE, userType);
			editor.putString(USER_ROLE_ID, userRoleId);
			editor.putString(TEACHER_ID, teachId);
			editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
			editor.putString(ScreenName, screenName);
			editor.putString(INSTITUTION_ID, institutionId);
			editor.putString(TOTAL_STUDENTS, totalStudents);
			editor.putString(STUDENT_NAME, studentName);
			editor.putString(DEVICE_ID, deviceId);
			editor.putString(SECTION_NAME, sectionName);
			editor.putString(NAME, Name);
			editor.putString(CREATOR_ID, creatorId);
			editor.putString(TOTAL_STUDENT_CLASS, totalStudentClass);
		    editor.putString(PHONE_NUMBER,phoneNumber);
		    editor.putString(BUS_ROUTE_NO,busRouteNo);
		    editor.putString(BUS_LATTITUDE,lattitude);
		    editor.putString(BUS_LONGITUDE,longitude);
		    editor.putString(BUS_NUMBER,busNumber);
		    editor.putString(PRODUCT_INFO,productInfo);
		    editor.putString(PAID_AMOUNT,paidAmount);
		    editor.putString(USER_PASSWORD,userPassword);
		    editor.putString(SCHOOL_LONGITUDE,schoolLongitude);
		    editor.putString(SCHOOL_LATTITUDE,schoolLattitude);
		    editor.putString(ADDMISSION_NUMBER,addmissionNumber);
		    editor.putString(EVENT_ADD_CHARGES,eventAddCharges);
		    editor.putString(FEE_ADD_CHARGES,feesAddCharges);
		    editor.putString(PERMISSIONS,permissions);
		    editor.putString(SESSION,session1);
		    editor.putString(BOARD,board);
		    editor.putString(TEACHER_ID1,teacherId1);
		    editor.putString(SCHOOL_NAME,schoolName);
		    editor.putString(CHAIRMAN_ASSIGNMENT_CLASS_NAME,chairmanAssignmentClassName);
		    editor.putString(CHAIRMAN_ASSIGNMENT_CLASS_ID,chairmanAssignmentClassId);
		    editor.putString(CHAIRMAN_ASSIGNMENT_SECTION_NAME,chairmanAssignmentSectionName);
		    editor.putString(CHAIRMAN_ASSIGNMENT_SECTION_ID,chairmanAssignmentSectionId);
		    editor.putString(CHAIRMAN_ASSIGNMENT_TEACHER_NAME,chairmanAssignmentTeacherName);
		    editor.putString(CHAIRMAN_ASSIGNMENT_TECAHER_ID,chairmanAssignmentTeacherId);
		    editor.putString(CHAIRMAN_ASSIGNMENT_SUBJECT_NAME,chairmanAssignmentSubjectName);
		    editor.putString(CHAIRMAN_ASSIGNMENT_SUBJECT_ID,chairmanAssgnmentSubjectId);
		    editor.putString(TEACHER_BADGE_COUNT,teacherBadgeCount);
		    editor.putString(HEALTH_AND_SAFETY,healthAndSafety);
		    editor.putString(QR_CODE_STRING,qrCode);
		    editor.putString(PARENT_QR_CODE_STRING,parentQrCode);
		    editor.putString(VISITOR_QR_CODE_STRING,visitorQrCode);
		    editor.putString(VISITOR_TYPE,visitorType);
		    editor.putString(EMPLOYEE_ID,employeeId);
		    editor.putString(EMPLOYEE_TYPE,employeeType);
		    editor.putString(DEPARTMENT_ID,departmentId);
		    editor.putString(DEPARTMENT_NAME,departmentName);
		    editor.putString(SCHOOL_TYPE,schoolType);
		    editor.putString(STUDENT_SUBJECT_ID,studentSubjectId);
		    editor.putString(STUDENT_GENDER,studentGender);
		    editor.putString(STUDENT_SEMESTER,studentSemester);
		    editor.putString(STUDENT_COURSE_ID,studentCourse);
		    editor.putString(FULL_ROLE_ID,fullRoleId);
		    editor.putString(USER_NAME1,userName1);
		    editor.putString(IS_HOSTEL_STUDENT,isHostelStudent);
		    editor.putString(SCHOOL_SCHEDULE_LEVEL,schoolScheduleLevel);
		    editor.putString(STUDY_PLANNER,studyPlanner);
		    editor.putString(ADDS_PERMISSION,addsPermission);
			editor.commit();
	 }
			
	
	
	

}
