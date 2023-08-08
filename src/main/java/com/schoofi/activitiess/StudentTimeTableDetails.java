package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class StudentTimeTableDetails extends AppCompatActivity {
	
	private JSONArray studentTimeTableArray;
	private int position;
	private TextView teacherName,teacherName1,lectureStartingTime,lectureStartingTime1,lectureEndingTime,lectureEndingTime1,lectureStartingTime2,lectureStartingTime3,lectureStartingTime4,lectureStartingTime5,lectureEndingTime2,lectureEndingTime3,lectureEndingTime4,lectureEndingTime5;
	private LinearLayout linear,linear1,linear2,linear3,linear4;
	private TextView teacherName2,teacherName3,teacherName4,teacherName5,group1,group2,group3,group4,subject1,subject2,subject3,subject4,group5,group6,group7,group8,subject5,subject6,subject7,subject8;
	private ImageView backButton;
	String date;
	String userEmailId = Preferences.getInstance().userEmailId;
	String userId = Preferences.getInstance().userId;
	String token = Preferences.getInstance().token;
	String studentId = Preferences.getInstance().studentId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String classId = Preferences.getInstance().studentClassId;
	String schoolId = Preferences.getInstance().schoolId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_time_table_details);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student TimeTable Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		position = getIntent().getExtras().getInt("position");
		date = getIntent().getExtras().getString("date");
		backButton =(ImageView) findViewById(R.id.img_back);
		teacherName = (TextView) findViewById(R.id.text_teacherName);
		teacherName1 = (TextView) findViewById(R.id.text_teacherName1);
		lectureStartingTime = (TextView) findViewById(R.id.text_lectureStartingTime);
		lectureStartingTime1 = (TextView) findViewById(R.id.text_lectureStartingTime1);
		lectureEndingTime = (TextView) findViewById(R.id.text_lectureEndingTime);
		lectureEndingTime1 = (TextView) findViewById(R.id.text_lectureEndingTime1);

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


		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if(Preferences.getInstance().schoolType.matches("College"))
		{
			try
			{
				Entry e;
				e = VolleySingleton.getInstance(StudentTimeTableDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
				studentTimeTableArray= new JSONArray(new String(e.data));
				//System.out.println(studentLeaveListArray);
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}

			if(studentTimeTableArray!= null)
			{
				try {


					teacherName1.setText(studentTimeTableArray.getJSONObject(position).getString("teacher"));
					lectureStartingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("period_start_time"));
					//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
					//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
					lectureEndingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("period_end_time"));

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		else {

			try {
				Entry e;
				e = VolleySingleton.getInstance(StudentTimeTableDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId);
				studentTimeTableArray = new JSONArray(new String(e.data));
				//System.out.println(studentLeaveListArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (studentTimeTableArray != null) {
				try {

					if(studentTimeTableArray.getJSONObject(position).getString("subject_name").matches("") || studentTimeTableArray.getJSONObject(position).getString("subject_name").matches("null"))
					{
						linear.setVisibility(View.GONE);
						if(studentTimeTableArray.getJSONObject(position).getString("lab_group_name1").matches("") || studentTimeTableArray.getJSONObject(position).getString("lab_group_name1").matches("null"))
						{
							linear1.setVisibility(View.GONE);
						}
						else
						{
							linear1.setVisibility(View.VISIBLE);
							teacherName2.setText(studentTimeTableArray.getJSONObject(position).getString("teacher_g1"));
							lectureStartingTime2.setText(studentTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime2.setText(studentTimeTableArray.getJSONObject(position).getString("end_time"));
							group2.setText(studentTimeTableArray.getJSONObject(position).getString("lab_group_name1"));
							subject2.setText(studentTimeTableArray.getJSONObject(position).getString("subject_name_g1"));
						}

						if(studentTimeTableArray.getJSONObject(position).getString("lab_group_name2").matches("") || studentTimeTableArray.getJSONObject(position).getString("lab_group_name2").matches("null"))
						{
							linear2.setVisibility(View.GONE);
						}
						else
						{
							linear2.setVisibility(View.VISIBLE);
							teacherName3.setText(studentTimeTableArray.getJSONObject(position).getString("teacher_g2"));
							lectureStartingTime3.setText(studentTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime3.setText(studentTimeTableArray.getJSONObject(position).getString("end_time"));
							group4.setText(studentTimeTableArray.getJSONObject(position).getString("lab_group_name2"));
							subject4.setText(studentTimeTableArray.getJSONObject(position).getString("subject_name_g2"));
						}

						if(studentTimeTableArray.getJSONObject(position).getString("lab_group_name3").matches("") || studentTimeTableArray.getJSONObject(position).getString("lab_group_name3").matches("null"))
						{
							linear3.setVisibility(View.GONE);
						}
						else
						{
							linear3.setVisibility(View.VISIBLE);
							teacherName4.setText(studentTimeTableArray.getJSONObject(position).getString("teacher_g3"));
							lectureStartingTime4.setText(studentTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime4.setText(studentTimeTableArray.getJSONObject(position).getString("end_time"));
							group6.setText(studentTimeTableArray.getJSONObject(position).getString("lab_group_name3"));
							subject6.setText(studentTimeTableArray.getJSONObject(position).getString("subject_name_g3"));
						}


						if(studentTimeTableArray.getJSONObject(position).getString("lab_group_name4").matches("") || studentTimeTableArray.getJSONObject(position).getString("lab_group_name4").matches("null"))
						{
							linear4.setVisibility(View.GONE);
						}
						else
						{
							linear4.setVisibility(View.VISIBLE);
							teacherName5.setText(studentTimeTableArray.getJSONObject(position).getString("teacher_g4"));
							lectureStartingTime5.setText(studentTimeTableArray.getJSONObject(position).getString("start_time"));

							lectureEndingTime5.setText(studentTimeTableArray.getJSONObject(position).getString("end_time"));
							group8.setText(studentTimeTableArray.getJSONObject(position).getString("lab_group_name4"));
							subject8.setText(studentTimeTableArray.getJSONObject(position).getString("subject_name_g4"));
						}


					}

					else {

						linear.setVisibility(View.VISIBLE);
						linear1.setVisibility(View.GONE);
						linear2.setVisibility(View.GONE);
						linear3.setVisibility(View.GONE);
						linear4.setVisibility(View.GONE);




						teacherName1.setText(studentTimeTableArray.getJSONObject(position).getString("teacher_name"));
						lectureStartingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("start_time"));
						//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
						//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
						lectureEndingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("end_time"));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_time_table_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
