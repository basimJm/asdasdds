package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StudentExamScheduleDetail extends AppCompatActivity {
	
	private TextView examSubject,examSubject1,examDate,examDate1,examTime,examTime1,examLocation,examLocation1,exam,exam1,examDetails,examDetails1;
	private Button addToReminder;
	private int position;
	private JSONArray examArray;
	String userEmailId = Preferences.getInstance().userEmailId;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String userId = Preferences.getInstance().userId;
	String token = Preferences.getInstance().token;
	String sectionId = Preferences.getInstance().studentSectionId;
	String schoolId = Preferences.getInstance().schoolId;
	String classId = Preferences.getInstance().studentClassId;
	String examId1; 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd-hh-mm");
	String endDate,endTime;
	String subject,location,description,termId;
	long timeInMilliseconds;
	String year,day,hour,minute,month;
	int year1,day1,hour1,minute1,month1;
	String date1,date2;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_exam_schedule_detail);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student ExamSchedule Detail");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		addToReminder = (Button) findViewById(R.id.btn_add_to_reminder);
		examSubject = (TextView) findViewById(R.id.text_subject_title);
		examSubject1 = (TextView) findViewById(R.id.text_subject_title1);
		examDate = (TextView) findViewById(R.id.text_subject_date);
		examDate1 = (TextView) findViewById(R.id.text_subject_date1);
		examTime = (TextView) findViewById(R.id.text_subject_time); 
		examTime1  = (TextView) findViewById(R.id.text_time1);
		examLocation = (TextView) findViewById(R.id.text_location);
		examLocation1 = (TextView) findViewById(R.id.text_location1);
		exam = (TextView) findViewById(R.id.text_exam);
		exam1 = (TextView) findViewById(R.id.text_exam1);
		examDetails = (TextView) findViewById(R.id.text_details);
		examDetails1 = (TextView) findViewById(R.id.text_details1);
		position = getIntent().getExtras().getInt("position");
		examId1 =getIntent().getExtras().getString("examID");
		back = (ImageView) findViewById(R.id.img_back);
		termId = getIntent().getStringExtra("termId");


		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(StudentExamScheduleDetail.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&term="+termId+"&ins_id="+Preferences.getInstance().institutionId);
			examArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(examArray!= null)
		{
			try {


				examSubject.setText("Subject");
				examSubject1.setText(examArray.getJSONObject(position).getString("subject"));
				subject = examArray.getJSONObject(position).getString("subject");
				description = examArray.getJSONObject(position).getString("detail");
				location = examArray.getJSONObject(position).getString("location");
				examDate.setText("Date");
				date1 = examArray.getJSONObject(position).getString("exam_date");
				Date date3 = formatter.parse(date1);
				
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				date2 = formatter1.format(date3);
				examDate1.setText(date2);
				examTime.setText("Time");
				examTime1.setText(examArray.getJSONObject(position).getString("start_time")+"-"+examArray.getJSONObject(position).getString("end_time"));
				examLocation1.setText(examArray.getJSONObject(position).getString("location"));
				examLocation.setText("Location");
				exam.setText("Exam");
				exam1.setText(examArray.getJSONObject(position).getString("exam_name"));
				examDetails.setText("Details");
				examDetails1.setText(examArray.getJSONObject(position).getString("detail"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				endDate = examDate1.getText().toString();
				year = endDate.substring(0,4);
				month = endDate.substring(5,7);
				day = endDate.substring(8,10);
				endTime = examTime1.getText().toString();
				hour = endTime.substring(0,2);
				minute = endTime.substring(4,6);
				//System.out.println(year);
				//System.out.println(month);
				//System.out.println(day);
				month1 = Integer.parseInt(month);
				year1 = Integer.parseInt(year);
				day1 = Integer.parseInt(day);
				hour1 = Integer.parseInt(hour);
				minute1 = Integer.parseInt(minute);
				//System.out.println(year);
				//System.out.println(month);
				//System.out.println(day1);
				
				
				

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		addToReminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent calIntent = new Intent(Intent.ACTION_INSERT); 
				calIntent.setType("vnd.android.cursor.item/event");    
				calIntent.putExtra(Events.TITLE, "My House Party"); 
				calIntent.putExtra(Events.EVENT_LOCATION, "My Beach House"); 
				calIntent.putExtra(Events.DESCRIPTION, "A Pig Roast on the Beach");*/
				/*Calendar c = Calendar.getInstance();
				System.out.println("Current time => " + c.getTime());

				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				String formattedDate = df.format(c.getTime());
				 
				GregorianCalendar calDate = new GregorianCalendar(2012, 7, 15,10,30);
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true); 
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 
				     calDate.getTimeInMillis()); 
				calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, 
				     calDate.getTimeInMillis()); 
				 
				startActivity(calIntent);*/
				
				Calendar cal1 = Calendar.getInstance();
				Calendar cal = Calendar.getInstance(); 
				cal.set(Calendar.MONTH,month1-1);
		        cal.set(Calendar.YEAR, year1);
		        cal.set(Calendar.DAY_OF_MONTH, day1-1);
		        cal.set(Calendar.HOUR_OF_DAY, hour1-1);
		        cal.set(Calendar.MINUTE, minute1);
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime",cal.getTimeInMillis());
				intent.putExtra("allDay", false);
				intent.putExtra("rrule", "FREQ=DAILY");
				intent.putExtra("endTime",cal.getTimeInMillis());
				intent.putExtra("title", subject);
				intent.putExtra("location", location);
				intent.putExtra("description", description);
				startActivity(intent);
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_exam_schedule_detail, menu);
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
