package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

public class TeacherTimeTableDetails extends AppCompatActivity{
	
	String roleId = Preferences.getInstance().userRoleId;
	String token = Preferences.getInstance().token;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String schoolId = Preferences.getInstance().schoolId;
	String userName = Preferences.getInstance().userName;
	private JSONArray teacherTimeTableArray;
	private int position;
	private TextView classname,className1,lectureStartingTime,lectureStartingTime1,lectureEndingTime,lectureEndingTime1;

	private TextView teacherName,teacherName1,lectureStartingTime2,lectureStartingTime3,lectureStartingTime4,lectureStartingTime5,lectureEndingTime2,lectureEndingTime3,lectureEndingTime4,lectureEndingTime5;
	private LinearLayout linear,linear1,linear2,linear3,linear4;
	private TextView teacherName2,teacherName3,teacherName4,teacherName5,group1,group2,group3,group4,subject1,subject2,subject3,subject4,group5,group6,group7,group8,subject5,subject6,subject7,subject8;
	private ImageView backButton;
	private ImageView back;
	String date;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_time_table_details);

		Tracker t = ((SchoofiApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher TimeTable Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		back = (ImageView) findViewById(R.id.img_back);
		classname = (TextView) findViewById(R.id.text_teacherName);
		className1 = (TextView) findViewById(R.id.text_teacherName1);
		lectureStartingTime = (TextView) findViewById(R.id.text_lectureStartingTime);
		lectureStartingTime1 = (TextView) findViewById(R.id.text_lectureStartingTime1);
		lectureEndingTime = (TextView) findViewById(R.id.text_lectureEndingTime);
		lectureEndingTime1 = (TextView) findViewById(R.id.text_lectureEndingTime1);
		classname.setText("Class");
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		position = getIntent().getExtras().getInt("position");
		date = getIntent().getExtras().getString("date");

		teacherName2 = (TextView) findViewById(R.id.text_teacherName10);
		teacherName3 = (TextView) findViewById(R.id.text_teacherName4);
		teacherName4 = (TextView) findViewById(R.id.text_teacherName6);
		teacherName5 = (TextView) findViewById(R.id.text_teacherName8);

		lectureStartingTime2 = (TextView) findViewById(R.id.text_lectureStartingTime3);
		lectureEndingTime2 = (TextView) findViewById(R.id.text_lectureEndingTime3);

		lectureStartingTime3 = (TextView) findViewById(R.id.text_lectureStartingTime5);
		lectureEndingTime3 = (TextView) findViewById(R.id.text_lectureEndingTime5);

		lectureStartingTime4 = (TextView) findViewById(R.id.text_lectureStartingTime7);
		lectureEndingTime4 = (TextView) findViewById(R.id.text_lectureEndingTime7);

		lectureStartingTime5 = (TextView) findViewById(R.id.text_lectureStartingTime9);
		lectureEndingTime5 = (TextView) findViewById(R.id.text_lectureEndingTime9);

		group1 = (TextView) findViewById(R.id.text_group1);
		group2 = (TextView) findViewById(R.id.text_group2);
		group3 = (TextView) findViewById(R.id.text_group3);
		group4 = (TextView) findViewById(R.id.text_group4);
		group5 = (TextView) findViewById(R.id.text_group5);
		group6 = (TextView) findViewById(R.id.text_group6);
		group7 = (TextView) findViewById(R.id.text_group7);
		group8 = (TextView) findViewById(R.id.text_group8);


		subject1 = (TextView) findViewById(R.id.text_subject1);
		subject2 = (TextView) findViewById(R.id.text_subject2);
		subject3 = (TextView) findViewById(R.id.text_subject3);
		subject4 = (TextView) findViewById(R.id.text_subject4);
		subject5 = (TextView) findViewById(R.id.text_subject5);
		subject6 = (TextView) findViewById(R.id.text_subject6);
		subject7 = (TextView) findViewById(R.id.text_subject7);
		subject8 = (TextView) findViewById(R.id.text_subject8);

		linear = (LinearLayout) findViewById(R.id.linear);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);

		linear.setVisibility(View.GONE);
		linear1.setVisibility(View.GONE);
		linear2.setVisibility(View.GONE);
		linear3.setVisibility(View.GONE);
		linear4.setVisibility(View.GONE);



		Preferences.getInstance().loadPreference(getApplicationContext());

		if (Preferences.getInstance().schoolType.matches("College")) {



			try {
				Entry e;
				e = VolleySingleton.getInstance(TeacherTimeTableDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId);
				if (e == null) {
					Log.d("po",String.valueOf(position)+date);
					teacherTimeTableArray = null;
				} else {
					teacherTimeTableArray = new JSONArray(new String(e.data));
				}
				//System.out.println(studentLeaveListArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (teacherTimeTableArray != null) {
				try {


					className1.setText(teacherTimeTableArray.getJSONObject(position).getString("class_name") + "-" + teacherTimeTableArray.getJSONObject(position).getString("section_name"));
					lectureStartingTime1.setText(teacherTimeTableArray.getJSONObject(position).getString("period_start_time"));
					//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
					//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
					lectureEndingTime1.setText(teacherTimeTableArray.getJSONObject(position).getString("period_end_time"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		} else {


			try {
				Entry e;
				e = VolleySingleton.getInstance(TeacherTimeTableDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId);
				teacherTimeTableArray = new JSONArray(new String(e.data));
				//System.out.println(studentLeaveListArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (teacherTimeTableArray != null) {
				try {


					if(teacherTimeTableArray.getJSONObject(position).getString("subject_name").matches("") || teacherTimeTableArray.getJSONObject(position).getString("subject_name").matches("null"))
					{
						linear.setVisibility(View.GONE);
						if(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name1").matches("") || teacherTimeTableArray.getJSONObject(position).getString("lab_group_name1").matches("null"))
						{
							linear1.setVisibility(View.GONE);
						}
						else
						{
							linear1.setVisibility(View.VISIBLE);
							teacherName2.setText(teacherTimeTableArray.getJSONObject(position).getString("class_name") + "-" + teacherTimeTableArray.getJSONObject(position).getString("section_name"));
							lectureStartingTime2.setText(teacherTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime2.setText(teacherTimeTableArray.getJSONObject(position).getString("end_time"));
							group2.setText(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name1"));
							subject2.setText(teacherTimeTableArray.getJSONObject(position).getString("subject_name_g1"));
						}

						if(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name2").matches("") || teacherTimeTableArray.getJSONObject(position).getString("lab_group_name2").matches("null"))
						{
							linear2.setVisibility(View.GONE);
						}
						else
						{
							linear2.setVisibility(View.VISIBLE);
							teacherName3.setText(teacherTimeTableArray.getJSONObject(position).getString("class_name") + "-" + teacherTimeTableArray.getJSONObject(position).getString("section_name"));
							lectureStartingTime3.setText(teacherTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime3.setText(teacherTimeTableArray.getJSONObject(position).getString("end_time"));
							group4.setText(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name2"));
							subject4.setText(teacherTimeTableArray.getJSONObject(position).getString("subject_name_g2"));
						}

						if(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name3").matches("") || teacherTimeTableArray.getJSONObject(position).getString("lab_group_name3").matches("null"))
						{
							linear3.setVisibility(View.GONE);
						}
						else
						{
							linear3.setVisibility(View.VISIBLE);
							teacherName4.setText(teacherTimeTableArray.getJSONObject(position).getString("class_name") + "-" + teacherTimeTableArray.getJSONObject(position).getString("section_name"));
							lectureStartingTime4.setText(teacherTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime4.setText(teacherTimeTableArray.getJSONObject(position).getString("end_time"));
							group6.setText(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name3"));
							subject6.setText(teacherTimeTableArray.getJSONObject(position).getString("subject_name_g3"));
						}


						if(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name4").matches("") || teacherTimeTableArray.getJSONObject(position).getString("lab_group_name4").matches("null"))
						{
							linear4.setVisibility(View.GONE);
						}
						else
						{
							linear4.setVisibility(View.VISIBLE);
							teacherName5.setText(teacherTimeTableArray.getJSONObject(position).getString("class_name") + "-" + teacherTimeTableArray.getJSONObject(position).getString("section_name"));
							lectureStartingTime5.setText(teacherTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime5.setText(teacherTimeTableArray.getJSONObject(position).getString("end_time"));
							group8.setText(teacherTimeTableArray.getJSONObject(position).getString("lab_group_name4"));
							subject8.setText(teacherTimeTableArray.getJSONObject(position).getString("subject_name_g4"));
						}


					}

					else {

						linear.setVisibility(View.VISIBLE);
						linear1.setVisibility(View.GONE);
						linear2.setVisibility(View.GONE);
						linear3.setVisibility(View.GONE);
						linear4.setVisibility(View.GONE);




						className1.setText(teacherTimeTableArray.getJSONObject(position).getString("class_name") + "-" + teacherTimeTableArray.getJSONObject(position).getString("section_name"));
						lectureStartingTime1.setText(teacherTimeTableArray.getJSONObject(position).getString("start_time"));
						//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
						//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
						lectureEndingTime1.setText(teacherTimeTableArray.getJSONObject(position).getString("end_time"));
					}




				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

}
