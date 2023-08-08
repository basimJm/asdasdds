package com.schoofi.constants;

import android.content.Context;
import android.content.Intent;

public class AppConstants {

	public static final class SERVER_URLS
	{


		//
		//public static final String SERVER_URL = "http://139.59.248.89/3.0.3/";
		public static final String SERVER_URL = "https://www.schoofi.com/3.20/";
		//public static final String IMAGE_URL = "http://139.59.248.89/";
		public static final String IMAGE_URL = "https://www.schoofi.com/";
		public static final String LOGIN_URL = "json-login.php";
		public static final String NEW_LOGIN_URL = "json-new-login.php";
		public static final String LOGIN_DETAILS = "json-login-details.php";
		public static final String STUDENT_DAILY_ATTENDANCE_URL = "json-attend-daily.php";
		public static final String STUDENT_LEAVE_LIST_URL = "json-leave-list.php";
		public static final String STUDENT_WEEKLY_ATTENDANCE_URL ="json-attend-date.php";
		public static final String STUDENT_TIME_TABLE_URL = "json-shd.php";
		public static final String STUDENT_ANNOUNCEMENT_URL ="json-announce.php";
		public static final String STUDENT_NEW_LEAVE= "json-a-leave.php";
		public static final String STUDENT_FEEDBACK_LIST = "json-feed-list.php";
		public static final String STUDENT_FEEDBACK_REPLY_LIST = "json-feed-resp.php";
		public static final String STUDENT_FEEDBACK_POST_COMMENT = "json-feed-comment.php";
		public static final String STUDENT_FEEDBACK_TYPE = "json-a-feed-type.php";
		public static final String STUDENT_FEEDBACK_NEW = "json-feedback.php";
		public static final String STUDENT_EXAM_SCHEDULE= "json-a-cls-exname.php";
		public static final String STUDENT_EXAM_SCHEDULE_LIST = "json-exam.php";
		public static final String STUDENT_POLL_LIST = "json-poll-list.php";
		public static final String STUDENT_SUBMITTED_POLL_LIST = "json-submit-poll.php";
		public static final String STUDENT_SUBMIT_POLL = "json-poll-action.php";
		public static final String USER_PROFILE_URL = "json-profile.php";
		public static final String PARENT_STUDENT_LIST = "json-stulist.php";
		public static final String PARENT_SIGN_UP = "json-reg.php";
		public static final String PARENT_REGISTRATION_SCREEN_2 = "json-reg2.php";
		public static final String STUDENT_SCHOOL_LIST = "json-sclist-par.php";
		public static final String PARENT_REGISTRATION_SCREEN_3 = "json-reg3.php";
		public static final String PARENT_REGISTRATION_SCREEN_4 = "json-reg4.php";
		public static final String TEACHER_CLASS_LIST = "json-tech-clslist.php";
		public static final String TEACHER_ANNOUNCEMENT_URL = "json-tec-announce.php";
		public static final String TEACHER_SCHEDULE_URL = "json-shd-tech.php";
		public static final String TEACHER_LEAVE_LIST_URL = "json-leave-tech.php";
		public static final String TEACHER_LEAVE_UPDATE_URL = "json-leave-update.php";
		public static final String TEACHER_STUDENT_ATTENDANCE_URL = "json-tec-sec.php";
		public static final String TEACHER_STUDENT_LIST = "json-stu.php";
		public static final String TEACHER_STUDENT_SUBMITTED_ATTENDANCE = "json-tec-attend-ex.php";
		public static final String TEACHER_STUDENT_SUBMIT = "json-tec-a-attend.php";
		public static final String STUDENT_RESULT_EXAM_LIST = "json-result-examlist.php";
		public static final String STUDENT_RESULT_EXAM_LIST_COLLEGE = "json-result-examlist-college.php";
		public static final String STUDENT_RESULT_EXAM_NAME_LIST = "json-a-result-exname.php";
		public static final String TEACHER_STUDENT_RESULT_LIST = "json-result-tech.php";
		public static final String TEACHER_STUDENT_RESULT_DETAILS = "json-result-stulist.php";
		public static final String FORGOT_PASSWORD = "json-pass-reset.php";
		public static final String RESET_PASSWORD = "json-pass-change.php";
		public static final String CHAIRMAN_HOME_SCREEN_SCHOOL_LIST = "json-ch-sch.php";
		public static final String CHAIRMAN_STUDENT_LEAVE_LIST = "json-ch-leave-list.php";
		public static final String CHAIRMAN_STUDENT_POLL_GRAPH ="json-ch-poll-graph.php";
		public static final String CHAIRMAN_STUDENT_EXAM_LIST = "json-ch-examlist.php";
		public static final String CHAIRMAN_SCHOOL_AVERAGE_RESULT = "json-ch-avg-rslt.php";
		public static final String CHAIRMAN_SCHOOL_EXAM_WISE_RESULT = "json-ch-sch-result.php";
		public static final String CHAIRMAN_SCHOOL_CLASS_LIST ="json-ch-clslist.php";
		public static final String CHAIRMAN_CLASS_EXAM_LIST = "json-ch-cls-examlist.php";
		public static final String CHAIRMAN_CLASS_RESULT = "json-ch-cls-result.php";
		public static final String CHAIRMAN_STUDENT_ANNOUNCEMENT = "json-ch-announce.php";
		public static final String CHAIRMAN_STUDENT_ANNOUNCEMENT_DATE = "json-ch-announce-date.php";
		public static final String CHAIRMAN_STUDENT_DAILY_ATTENDANCE  ="json-ch-atten.php";
		public static final String CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST = "json-ch-cls-sec.php";
		public static final String CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST = "json-ch-cls.php";
		public static final String CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE = "json-ch-atten-cls.php";
		public static final String CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE = "json-ch-atten-cls-sec.php";
		public static final String CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE = "json-ch-atten-week.php";
		public static final String CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE_CLASS_WISE ="json-ch-atten-cls-week.php";
		public static final String CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE_SECTION_WISE ="json-ch-atten-cls-sec-week.php";
		public static final String CHAIRMAN_STUDENT_FEEDBACK_LIST = "json-ch-feedback-list.php";
		public static final String CHAIRMAN_STUDENT_ASSIGN_MEMBER = "json-a-assign-members.php";
		public static final String CHAIRMAN_STUDENT_ASSIGN_MEMBER_PROFILE = "json-feed-assigned-profile.php";
		public static final String CHAIRMAN_STUDENT_ASSIGN_DONE = "json-submit-feed-assign.php";
		public static final String STUDENT_TEACHER_FEEDBACK = "json-feed-tech.php";
		public static final String STUDENT_TEACHER_FEEDBACK_ASSIGNED = "json-tech-feed-assigned.php";
		public static final String STUDENT_ASSIGNMENT_URL ="json-assign.php";
		public static final String TEACHER_ASSIGNMENT_URL = "json-asn-tech.php";
		public static final String TEACHER_ASSIGNMENT_CLASS_LIST = "json-a-cls-subject.php";
		public static final String PLANNER_SUBJECT_LIST = "json-cls-subject.php";
		public static final String GCM_SENDER_ID = "738232626280";
		public static final String ABOUT_SCHOOL_IMAGES = "json-school_info.php";
		public static final String ABOUT_SCHOOL_LIST = "json-sch-news.php";
		public static final String FEEDBACK_ASSIGN_IMAGES = "json-a-tech-feed-action-reply.php";
		public static final String TEACHER_SUBMIT_ASSIGNMENT = "json-a-tech-submit-assign.php";
		public static final String LEAVE_DELETE = "json-delete-leave.php";
		public static final String AUDIT_USER_LIST = "json-audit-users-list.php";
		public static final String AUDIT_USER_CREATE_USER = "json-audit-add-users.php";
		public static final String AUDIT_TASK_LIST = "json-audit-tasks-list.php";
		public static final String AUDIT_ADD_TASK = "json-audit-add-task.php";
		public static final String AUDIT_SUBTASK_LIST = "json-audit-subtask-list.php";
		public static final String AUDIT_SCHOOL_LIST = "json-a-inst-schlist.php";
		public static final String AUDIT_PERSON_LIST = "json-audit-person.php";
		public static final String AUDIT_TASK_SPINNER_LIST = "json-a-ins-audit-tasks.php";
		public static final String AUDIT_TASK_SUBTASK_LIST = "json-a-ins-audit-subtask.php";
		public static final String STUDENT_ASSIGNMENT_SUBMIT = "json-a-submit-assign.php";
		public static final String TEACHER_FEEDBACK_REPLY_LIST = "json-feed-reply.php";
		//public static final String PROJECT_NUMBER = "1037499257230";
		public static final String MSG_KEY = "m";
		public static final String TEACHER_STUDENT_ASSIGNMENT_NOT_SUBMITTED_LIST = "json-asn-stu-not.php";
		public static final String TEACHER_STUDENT_ASSIGNMENT_SUBMITTED_LIST = "json-asn-stu-sub.php";
		public static final String CONTACT_US = "json-contact-us.php";
		public static final String STOCK_TYPE_LIST = "json-a-stock-type.php";
		public static final String STOCK_TYPE_SUB_LIST = "json-stock-sub-list.php";
		public static final String EVENT_LIST = "json-event-list.php";

		public static final String MAPS_ANDROID_KEY = "AIzaSyB_1WTXmxOqh_U2_6u_SKFEDo0Mnd6uED4";

		public static final String HEALTH_AND_AUDIT_ADD_SUBTASK = "json-a-add-subtask.php";
		public static final String HEALTH_AND_AUDIT_USER_TASK_LIST = "json-audit-user-view.php";
		public static final String HEALTH_AND_AUDIT_USER_SUBTASK_LIST = "json-audit-user-view-subtask.php";
		public static final String HEALTH_AND_AUDIT_USER_SUBMIT_RESPONSE = "json-a-audit-response.php";
		public static final String HEALTH_AND_AUDIT_ASSIGNED_LIST = "json-assigned-audits.php";
		public static final String HEALTH_AND_AUDIT_INSTITUTION_LIST = "json-inst-schlist.php";
		public static final String HEALTH_AND_AUDIT_PERSON_LIST = "json-audit-person.php";
		public static final String HEALTH_AND_AUDIT_TASK_LIST = "json-ins-audit-tasks.php";
		public static final String HEALTH_AND_AUDIT_SUBTASK_LIST = "json-ins-audit-subtask.php";
		public static final String HEALTH_AND_AUDIT_POST_AUDIT_REVIEW = "json-a-ch-audit-form.php";
		public static final String EVENT_UPLOAD_AUDIENCE_URL = "json-a-group-list.php";
		public static final String EVENT_UPLOAD_URL = "json-a-add-event.php";
		public static final String STUDENT_EVENT_CONFIRMATION = "json-update-seat.php";
		public static final String STUDENT_UNPAID_FESS = "json-unpaidfee-first.php";
		public static final String STUDENT_FEES_HISTORY = "json-feehistory.php";
		public static final String ANNOUNCEMENT_TYPE = "json-a-announce-type.php";
		public static final String ENQUIRY_USER_REGISTRATION = "json-enquiry-user-login.php";
		public static final String ENQUIRY_TYPE = "json-a-sch-type.php";
		public static final String ADMISSION_ENQUIRY_FORM_SUBMIT = "json-enq-submit.php";
		public static final String ADMISSION_ENQUIRY_STATUS_LIST ="json-enquiry-list.php";
		public static final String ADMIN_BUS_LIST = "json-bus-list.php";
		public static final String TEACHER_EVENT_LIST = "json-tech-events.php";
		public static final String PAYMENT_SUCCESS = "json-payment.php";
		public static final String UPDATE_ADDMISSION_ENQUIRY = "json-update-enquiry.php";
		public static final String ADD_EVENT_GALLERY = "json-add-event-gallery.php";
		public static final String ADMIN_ADMISSION_ENQUIRY_LIST = "json-admin-enquiry-list.php";
		public static final String CHANGE_PASSWORD = "json-changepassword.php";
		public static final String TEACHER_STUDENT_SUBJECT_LIST = "json-class-students.php";
		public static final String PARENT_FEES_PAYMENT_SUCCESS = "json-a-fee-payment.php";
		public static final String NOTIFICATION_LIST = "json-notifications-list.php";
		public static final String CHAIRMAN_FRAGMENT_STUDENT_FEES = "json-fee-dashboard.php";
		public static final String CHAIRMAN_FRAGMENT_STUDENT_FEES_CLASS = "json-fee-dashboard-cls.php";
		public static final String CHAIRMAN_FRAGMENT_STUDENT_FEES_SECTION = "json-fee-dashboard-seclist.php";
		public static final String CHAIRMAN_FRAGMENT_STUDENT_FEES_STUDENT_LIST = "json-fee-dashboard-stulist.php";
		public static final String CHAIRMAN_FRAGMENT_STUDENT_FEES_AGEING_ANALYSIS = "json-fee-dashboard-daywise.php";
		public static final String TEACHER_STUDENT_PROFILE_IMAGE_UPDATE_URL = "json-ios-tech-image-update.php";
		public static final String STUDENT_TERM_TYPE = "json-feed-type.php";
		public static final String TERM_EXAM_LIST = "json-term-examlist.php";
		public static final String TERM_EXAM_LIST_COLLEGE = "json-term-examlist-college.php";
		public static final String TERM_EXAM_LIST1 = "json-cls-exname-term.php";
		public static final String STUDENT_RESULT_DETAILS = "json-result-detail.php";
		public static final String STUDENT_CO_SCHOLASTIC_RESULTS = "json-co-sch-result.php";
		public static final String ADMIN_CLASS_LIST = "json-admin-class-list.php";
		public static final String ADMIN_SECTION_LIST = "json-admin-section-list.php";
		public static final String ADMIN_STUDENT_LIST = "json-admin-student-list.php";
		public static final String CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS = "json-attend-analysis.php";
		public static final String ADMIN_BUS_LIST_MAP = "json-bus-url-list.php";
		public static final String USER_BUS_LIST = "json-student-bus.php";
		public static final String APP_VERSION = "json-version.php";
		public static final String ADD_ANNOUNCEMENT = "json-add-announce.php";
		public static final String PARENT_STUDENT_UNPAID_FEES_DETAILS = "json-unpaidfee.php";
		public static final String CHAIRMAN_TERM_EXAM_LIST = "json-sch-term-result-list.php";
		public static final String CHAIRMAN_FINAL_RESULT = "json-ch-final-result.php";
		public static final String BADGE_COUNT = "json-badge.php";
		public static final String TEACHER_STUDENT_BUS_ATTENDANCE = "json-tech-stu-bus-atten.php";
		public static final String TEACHER_STUDENT_BUS_ATTENDANCE_SUBMIT = "json-a-tech-bus-atten.php";
		public static final String TEACHER_STUDENT_BUS_SUNMITTED_ATTENDANCE = "json-tec-attend-ex-stu-bus.php";
		public static final String NOTIFICATION_ATTENDANCE_TAG = "1";
		public static final String NOTIFICATION_LEAVE_TAG = "2";
		public static final String NOTIFICATION_ASSIGNMENT_TAG = "3";
		public static final String NOTIFICATION_ANNOUNCEMENT_TAG = "4";
		public static final String NOTIFICATION_FEEDBACK_TAG = "5";
		public static final String NOTIFICATION_EXAM_SCHEDULE_TAG = "6";
		public static final String NOTIFICATION_RESULT_TAG = "7";
		public static final String NOTIFICATION_EVENT_TAG = "8";
		public static final String NOTIFICATION_POLL_TAG = "9";
		public static final String NOTIFICATION_BUS_TRACKING_TAG = "10";
		public static final String NOTIFICATION_FEES_TAG = "11";
		public static final String NOTIFICATION_BIRTHDAY_TAG = "12";
		public static final String NOTIFICATION_TEACHER_ANNIVERSERY_TAG = "13";
		public static final String HEALTH_AND_SAFETY_USER_TAG = "17";
		public static final String HEALTH_AND_SAFETY_ASSIGNER_TAG = "18";
		public static final String NOTIFICATION_APP_UPDATE_TAG = "14";
		public static final String NOTIFICATION_OTHERS_TAG = "15";
		public static final String BUS_ATTENDANCE_TAG = "16";
		public static final String CHAIRMAN_DAILY_FEE_COLLECTION_NOTIFICATION_TAG = "50";
		public static final String SCHOOL_WEBSITE_URL = "json_school_website_url.php";
		public static final String CHAIRMAN_DASHBOARD_URL = "json-dashboard-analysis.php";
		public static final String SCHOOL_DIARY_URL = "json-diary.php";
		public static final String SCHOOL_SPINNER_DIARY_URL = "json-diary-dropdowns.php";
		public static final String SCHOOL_DIARY_UPLOAD_URL = "json-diary-upload.php";
		public static final String SCHOOL_DIARY_UPLOAD_URL_1 = "json-a-diary-upload.php";
		public static final String SCHOOL_DIARY_REPLY_LIST = "json-diary-reply.php";
		public static final String SCHOLL_DIARY_REPLY_SUBMIT = "json-diary-reply-ins.php";
		public static final String MARK_LOCATION_URL="json-update-lat-long.php";
		public static final String SCHOOL_DIARY_DELETE_URL = "json-delete-diary.php";
		public static final String MARK_LOCATION_FIRST_URL = "json-latlong-indicate.php";
		public static final String PARENT_STUDENT_BUS_ATTENDANCE = "json-show-bus-atten.php";
		public static final String BUS_ROUTES = "json-bus-route-list.php";
		public static final String CLASS_SECTION_LIST = "json-class-section-list.php";
		public static final String ADD_PLANNER = "json-add-planer.php";
		public static final String PLANNER_LIST = "json-planer-event-list.php";
		public static final String DELETE_EVENT = "json-delete-planner.php";
		public static final String EDIT_PLANNER_EVENT = "json-edit-planner.php";
		public static final String ADMIN_BUS_ROUTE_STUDENT_LIST = "json-busroute_stulist.php";
		public static final String ADMIN_BUS_NOTIFICATION = "json-bus-send-notif.php";
		public static final String STUDENT_BUS_ROUTE_LIST = "json-route-list.php";
		public static final String LINKING_SOCIAL_ACCOUNTS = "json-ins-social-info.php";
		public static final String CHAIRMAN_FEES_ANALYSIS = "json-ch-fee-analysis.php";
		public static final String CHAIRMAN_ASSIGNMENT_ANALYSIS = "json-sec-asn-list.php";
		public static final String CHAIRMAN_ASSIGNMENT_ANALYSIS_DETAILS = "json-sec-asn-list-detail.php";
		public static final String ADMIN_ASSIGNMENT_ANALYSIS = "json-admin-assign-analysis.php";
		public static final String CHAIRMAN_SCHOOL_LIST_TESTING = "json_ch_sch_gg.php";
		public static final String TEACHER_BADGE_COUNT = "json-badge-update-count.php";
		public static final String BUS_STOP_LIST = "json-bus-stop-list.php";
		public static final String BUS_NOTIFICATION_MESSAGE = "bus_messages.php";
		public static final String ADMIN_HEALTH_COMMON_LISTVIEW = "json-health.php";
		public static final String ADMIN_HEALTH_MEDICAL_HISTORY_DETAILS = "json-medical-history.php";
		public static final String ADMIN_HEALTH_COMMON_INSURANCE_DETAILS = "json-insurance.php";
		public static final String ADMIN_HEALTH_COMMON_VACCINATION_DETAILS = "json-vaccines.php";
		public static final String HEALTH_PROFILE = "json-health-profile.php";
		public static final String VACCINE_DROPDOWNS = "json-vaccines-dropdown.php";
		public static final String ADD_VACCINE = "json-add-vaccine.php";
		public static final String ADD_VACCINE_CALENDER = "json-ins-vaccine-cld.php";
		public static final String ADD_INSURANCE_DETAILS = "json-add-insurance.php";
		public static final String ADD_MEDICAL_HISTORY = "json-add-medical.php";
		public static final String CHECK_UP_DETAILS = "json-checkup-details.php";
		public static final String MEDICAL_EVENT_LIST = "json-medical-eventlist.php";
		public static final String ADD_CHECK_UP_DETAILS = "json-add-checkup-details.php";
		public static final String STUDENT_DETAILS = "json-stu-details.php";
		public static final String ADMIN_CLASS_SECTION_LIST_VISITOR = "json-class-section-list.php";
		public static final String VISITOR_TYPE = "visitor_type.php";
		public static final String VISITOR_ENTRY_URL = "json-visitor-entry-log.php";
		public static final String BUS_BOARDING_NOTIFICATION_URL = "json-safe-bus-pickup.php";
		public static final String BUS_ROUTE_INFO = "json-stu-bus-route-info.php";
		public static final String PARENT_DETAILS = "json-parent-details.php";
		public static final String QR_CODE_STRING = "json-match-qr-code.php";
		public static final String BUS_PICKED_URL ="json-safe-bus-drop-notif.php";
		public static final String PARENT_QR_CODE_MATCH = "json-qr-stu_details.php";
		public static final String ALL_BUS_MAP_LIST = "json-last-contact-buslist.php";
		public static final String VISITOR_ANALYSIS_FIRST_SERVICE = "json-safe-dashboard.php";
		public static final String UPDATE_PROFILE = "json-update-profile-by-parent.php";
		public static final String VISITOR_ANALYSIS_PRIMARY_SCREEN = "json-visit-type-count.php";
		public static final String FINAL_SUBMISSION_ATTENDANCE = "json-final-bus-atten.php";
		public static final String VISITOR_ANALYSIS_MAIN_SERVICE = "json-visitor-logs-details.php";
		public static final String TEACHER_COORDINATOR_ATTENDANCE_ANALYSIS = "json-coordinater-dashboard.php";
		public static final String TEACHER_COORDINATOR_ATTENDANCE_ANALYSIS_CLASS_WISE = "json-coordinater-dashboard-classwise.php";
		public static final String TEACHER_COORDINATOR_ATTENDANCE_ANALYSIS_SECTION_WISE = "json-coordinater-dashboard-secwise.php";
		public static final String TEACHER_COORDINATOR_BUS_ATTENDANCE_ANALYSIS_ROUTE_WISE = "json-coordinater-dashboard-bus.php";
		public static final String TEACHER_COORDINATOR_PENDING_LEAVES_COUNT = "json-coordinater-dashboard-pending-leavs.php";
		public static final String TEACHER_COORDINATOR_PENDING_FEES = "json-coordinater-dashboard-fee.php";
		public static final String TEACHER_COORDINATER_PENDING_FEES_STUDENT_WISE = "json-coordinater-dashboard-fee-stuwise.php";
		public static final String TEACHER_COORDINATOR_ASSIGNMENT_ANALYSIS = "json-coordinater-dashboard-assignments.php";
		public static final String TEACHER_LIST = "json-teac-list.php";
		public static final String VISITOR_EXIT_FORM = "json-update-exittime.php";
		public static final String STUDENT_PARENT_IMAGE_UPLOAD = "json-update-user-images.php";
		public static final String ZONE_SPINNER = "json-zone-list.php";
		public static final String TURNSTILE_LOG = "json-turnstile-log.php";
		public static final String CHAIRMAN_DASHBOARD_ANALYSIS_PIE_CHART_SCREEN = "json-discount-analysis.php";
		public static final String CHAIRMAN_DASHBOARD_ANALYSIS_CLASS_WISE_SCREEN = "json-discount-analysis-cls.php";
		public static final String CHAIRMAN_DASHBOARD_ANALYSIS_SECTION_WISE_SCREEN = "json-discount-analysis-sec.php";
		public static final String CHAIRMAN_DASHBOARD_ANALYSIS_STUDENT_WISE_SCREEN = "json-discount-analysis-stu.php";
		public static final String CATEGORY_TYPE = "json-dropdown.php";
		public static final String STUDENT_ANALYSIS = "json-analysis.php";
		public static final String EMPLOYEE_LEAVE_UPLOAD = "json-emp-leave.php";
		public static final String EMPLOYEE_LEAVE_TYPE = "json-emp-leave-type.php";
		public static final String EMPLOYEE_LEAVE_LIST = "json-emp-leave-list.php";
		public static final String EMPLOYEE_DELETE_LEAVE = "json-delete-emp-leave.php";
		public static final String EMPLOYEE_LEAVE_APPROVAL_LEAVE_LIST = "json-emp-leave-list-pending-approval.php";
		public static final String EMPLOYEE_LEAVE_STATUS_UPDATE = "json-emp-leave-update.php";
		public static final String EMPLOYEE_LEAVE_COUNT = "json-emp-check-leave-count.php";
		public static final String EMPLOYEE_ATTENDANCE = "json-emp-attendance-custom.php";
		public static final String CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH = "json-ch-employee_attendance.php";
		public static final String EMPLOYEE_DEPARTMENT_TYPE = "json-department-type.php";
		public static final String CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH_DETAILS = "json-emp-attendance-graph-details.php";
		public static final String CHAIRMAN_EMPLOYEE_ATTENDANCE_WITHOUT_GRAPH_DETAILS = "json-ch-emp-atten-first.php";
		public static final String CHAIRMAN_EMPLOYEE_ALL_DEPARTMENT_ATTENDANCE_COUNT = "json-ch-dept-wise-attendance.php";
		public static final String CHAIRMAN_EMPLOYEE_ALL_EMPLOYEE_DEPARTMENT_ATTENDANCE_COUNT = "json-ch-emp-dept-atten.php";
		public static final String STUDENT_ASSIGNEMENT_SUBJECT_LIST = "json-stu-subject-list.php";
		public static final String STUDENT_ASSIGNMENT_NEW_LIST = "json-assign-new.php";
		public static final String ADMIN_EMPLOYEE_LIST = "json-emp-list.php";
		public static final String ADMIN_TWO_STEP_ATTENDANCE_PRIMARY = "json-recorded-attendance-cls-list.php";
		public static final String NEW_ADDMISSION_ANALYSIS = "json-admission-analysis.php";
		public static final String TWO_STEP_NOTIFICATIONS = "json-cls-wise-atten-notif.php";
		public static final String TEACHER_COLLEGE_CLASS_LIST = "json-college-class-list-tec.php";
		public static final String TEACHER_COLLEGE_SUBJECT_LIST = "json-college-subject-list-tec.php";
		public static final String TEACHER_COLLEGE_SUBJECT_LIST_NEW = "json-college-subject-list-tec-new.php";
		public static final String TEACHER_COLLEGE_STUDENT_ATTENDANCE_LIST ="json-college-attendance-stu-list.php";
		public static final String TEACHER_COLLEGE_STUDENT_ATTENDANCE_LIST_NEW ="json-college-attendance-stu-list-new.php";
		public static final String TEACHER_COLLEGE_STUDENT_RECORDED_ATTENDANCE_LIST = "json-college-attendance-recorded-stu-list.php";
		public static final String TEACHER_COLLEGE_STUDENT_RECORDED_ATTENDANCE_LIST_NEW = "json-college-attendance-recorded-stu-list-new.php";
		public static final String TEACHER_COLLEGE_STUDENT_ATTENDANCE_SUBMIT = "json-a-tec-college-attend.php";
		public static final String STUDENT_COLLEGE_DAILY_ATTENDANCE = "json-college-stu-attend-daily.php";
		public static final String STUDENT_COLLEGE_CUSTOM_ATTENDANCE = "json-college-student-attendance-custom.php";
		public static final String STUDENT_ATTENDANCE_SUBJECT = "json-college-stu-atten.php";
		public static final String NEW_POLL_LIST = "json-new-poll-list.php";
		public static final String NEW_POLL_QUESTION_LIST = "json-poll-question-list.php";
		public static final String NEW_POLL_SUBMIT_LIST = "json-ins-poll.php";
		public static final String NEW_POLL_SUBMITTED_LIST = "json-submit-new-poll-list.php";
		public static final String NEW_POLL_SUBMITTED_QUESTION_LIST = "json-new-poll-question-submit-list.php";
		public static final String HOSTEL_LIST = "json-hostel-list.php";
		public static final String HOSTEL_ROOM_BEDDING_TYPE = "json-hostel-room-bedding-type.php";
		public static final String HOSTEL_ROOM_ACCOMADATION_TYPE = "json-hostel-accomadation-type.php";
		public static final String HOSTEL_ROOM_LIST = "json-hostel-room-list.php";
		public static final String STUDENT_ELECTIVE_SUBJECT_LIST = "json-elective-sub-list.php";
		public static final String STUDENT_ELECTIVE_SUBJECT_SUBMIT = "json-add-clg-stu-sub.php";
		public static final String STUDENT_HOSTEL_RESERVATION = "json-hostel-reserve.php";
		public static final String STUDENT_HOSTEL_RESERVATION_LIST = "json-hostel-reservation-list.php";
		public static final String MASTER_EVENT_LIST = "json-event-dropdown-list.php";
		public static final String EVENT_COORDINATOR_LIST = "json-event-select-coordinator-list.php";
		public static final String SUB_EVENT_LIST = "json-sub-events-list.php";
		public static final String APPROVAL_MATRIX_LIST = "json-emp-leave-matrix.php";
		public static final String TEACHER_COLLEGE_SCHEDULE = "json-college-teacher-schedule.php";
		public static final String STUDENT_COLLEGE_SCHEDULE = "json-college-student-schedule.php";
		public static final String COLLEGE_STUDENT_PERIOD_LIST = "json-college-period-list.php";
		public static final String TEACHING_EMPLOYEE_LIST = "json-teaching-employee-list.php";
		public static final String TEACHING_PERIOD_LIST = "json-ch-college-period-list.php";
		public static final String EMPLOYEE_ADDRESSES = "json-emp-addresses.php";
		public static final String STUDY_PLANNER_LIST = "json-study-planner-list.php";
		public static final String STUDENT_ALL_SUBJECT_LIST = "json-student-all-subject-list.php";
		public static final String STUDENT_BIOEMTRIC_ATTENDANCE = "json-student_atten_bio.php";
		public static final String LIBRARY_BOOK_FINDER = "json-library-book-finder.php";
		public static final String LIBRARY_BOOK_HISTORY = "json-library-book-transaction.php";
		public static final String MOCK_TEST_SUBJECT_LIST="json-sl-mock-test.php";
		public static final String ALLOTED_TEST_LIST = "json_assessment_stu_test_list.php";
		public static final String ALLOTED_TEST_QUESTION_LIST = "json-test-ques-list.php";
		public static final String ALLOTED_TEST_SUBMITTED_SERVICE = "json-submit-test.php";
		public static final String ASSESMENT_CLASS_LIST = "json_ass_class_list.php";
		public static final String ASSESMENT_SUBJECT_LIST = "json_ass_subject_list.php";
		public static final String ASSESMENT_KNOWLEDGE_RESOURCE_SUBMIT_LIST = "json_add_ass_knowledge_resource.php";
		public static final String ASSESMENT_KNOWLEDGE_RESOURSE_LIST= "json_knowlege_resource.php";
		public static final String ASSESMENT_KNOWLEDGE_RESOURSE_SUBJECT_LIST = "json_ass_student_subject_list.php";
		public static final String ASSESMENT_KNOWLEDGE_RESOURSE_TOPIC_LIST = "json_ass_knowledge_resource_topic_list.php";
		public static final String ASSESMENT_KNOWLEDGE_RESOURSE_SUB_TOPIC_LIST = "json_ass_knowledge_resource_sub_topic_list.php";
		public static final String STUDENT_DIARY_TEACHER_LIST = "get_stu_teacher_name.php";
		public static final String STUDENT_DIARY_CHAT_SERVICE= "json_teacher_parent_chat.php";
		public static final String STUDENT_DIARY_SUBMIT_SERVICE = "ins_teacher_parent_chat.php";
		public static final String TEACHER_STUDENT_DIARY_GROUP_STUDENT_LIST = "json-group-and-students.php";
		public static final String TEACHER_STUDENT_DIARY_CHAT_SERVICE = "json_teacher_parent_chat.php";
		public static final String TEACHER_STUDENT_MESSAGE_VISIBLE = "json_visibleall_msg.php";
		public static final String DIARY_TEACHER_LIST = "json-teachers-list.php";
		public static final String TECAHER_CLASS_LSIT = "json-class-and-stu-list.php";
		public static final String TEACHER_GROUP_CREATION_ACTIVITY = "json-add-chat-group.php";
		public static final String TEACHER_NEW_ASSIGNEMENT = "json-asn-cirular-tech.php";
		public static final String DIARY_WALL = "json-diary-wall.php";
		public static final String STUDENT_TUTORIAL_LIST = "json-tutorial-list.php";
		public static final String STUDENT_UPDATE_VIDEO_STATUS = "update-tutorial-status.php";
		public static final String STUDENT_PROGRESS_UPDATE_STATUS = "json-tutorials.php";
		public static final String STUDENT_RESULT_LIST_STATUS = "json-view-student-result.php";
		public static final String EMPLOYEE_CHECK_IN_CHECK_OUT = "json_employee_bio_out_time.php";
		public static final String EMPLOYEE_CHECK_IN_CHECK_OUT_1 = "json_employee_bio_in_time.php";




		
		

		
		

	}
	
	public static final String TAG = "AndroidHive GCM";
    public static final String DISPLAY_MESSAGE_ACTION ="com.androidhive.pushnotifications.DISPLAY_MESSAGE";
    public  static final String EXTRA_MESSAGE = "message";

public static void displayMessage(Context context, String message) {
	Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
	intent.putExtra(EXTRA_MESSAGE, message);
	context.sendBroadcast(intent);
}

	public static final class MESSAGES
	{
		public static final String NETWORK_ERROR_MESSAGE = "Unable to fetch data, kindly enable internet settings!";
		public static final String NO_RECORD_FOUND_MESSAGE = "No Records Found!!";
		public static final String SESSION_EXPIRED_MESSAGE ="Session Expired:Please Login Again";
		public static final String SOMETHING_WENT_WRONG = "Error fetching modules! Please try after sometime";
		public static final String REGISTERED_SUCCESSFULLY = "Submitted!!";
		public static final String USER_ALREADY_EXISTS = "User Already exists";
		public static final String EMPTY_FIELDS = "Please fill all the fields";
		public static final String UNABLE_TO_FETCH_DATA= "Unable to fetch data!!";

	}


	public static class TimeAgo {
		private static final int SECOND_MILLIS = 1000;
		private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
		private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
		private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

		public static String getTimeAgo(long time) {
			if (time < 1000000000000L) {
				time *= 1000;
			}

			long now = System.currentTimeMillis();
			if (time > now || time <= 0) {
				return null;
			}


			final long diff = now - time;
			if (diff < MINUTE_MILLIS) {
				return "just now";
			} else if (diff < 2 * MINUTE_MILLIS) {
				return "a minute ago";
			} else if (diff < 50 * MINUTE_MILLIS) {
				return diff / MINUTE_MILLIS + " minutes ago";
			} else if (diff < 90 * MINUTE_MILLIS) {
				return "an hour ago";
			} else if (diff < 24 * HOUR_MILLIS) {
				return diff / HOUR_MILLIS + " hours ago";
			} else if (diff < 48 * HOUR_MILLIS) {
				return "yesterday";
			} else {
				return diff / DAY_MILLIS + " days ago";
			}
		}
	}



}


