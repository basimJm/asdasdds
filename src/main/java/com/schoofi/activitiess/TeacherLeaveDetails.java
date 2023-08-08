package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TeacherLeaveDetails extends AppCompatActivity {

	private TextView screenTitle,file,status,studentName,studentName1,leaveSubject,leaveSubject1,studentClass,studentClass1,date,date1,from,from1,to,to1,leaveDetails,leaveDetails1,imageUrl;
	private ImageView back,imageurl;
	private Button approve,reject;
	private int position;
	private JSONArray teacherStudentLeaveListArray;
	String schoolId = Preferences.getInstance().schoolId;
	String classId = Preferences.getInstance().studentClassId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String leaveId;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date2,date3,date4,date5,date6,date7;
    String image,name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_teacher_leave_details);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Leave Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		screenTitle = (TextView) findViewById(R.id.txt_teacherLeaveDetails);
		status = (TextView) findViewById(R.id.text_teacherStudentLeaveStatus);
		studentName = (TextView) findViewById(R.id.text_StudentNameTeacherLeaveDetails);
		studentName1 = (TextView) findViewById(R.id.text_StudentNameTeacherLeaveDetails1);
		leaveSubject = (TextView) findViewById(R.id.text_LeaveSubjectTeacherLeaveDetails);
		leaveSubject1 = (TextView) findViewById(R.id.text_LeaveSubjectTeacherLeaveDetails1);
		leaveDetails = (TextView) findViewById(R.id.text_studentLeaveDetailsTeacherLeaveDetails);
		leaveDetails1 = (TextView) findViewById(R.id.text_studentLeaveDetailsTeacherLeaveDetails1);
		studentClass = (TextView) findViewById(R.id.text_studentClassTeacherLeaveDetails);
		studentClass1 = (TextView) findViewById(R.id.text_studentClassTeacherLeaveDetails1);
		date = (TextView) findViewById(R.id.text_studentDateTeacherLeaveDetails);
		date1 = (TextView) findViewById(R.id.text_studentDateTeacherLeaveDetails1);
		from = (TextView) findViewById(R.id.text_studentLeaveStartingDateTeacherLeaveDetails);
		from1 = (TextView) findViewById(R.id.text_studentLeaveStartingDateTeacherLeaveDetails1);
		to = (TextView) findViewById(R.id.text_studentLeaveEndingDateTeacherLeaveDetails);
		to1 = (TextView) findViewById(R.id.text_studentLeaveEndingDateTeacherLeaveDetails1);
		//imageUrl = (TextView) findViewById(R.id.text_imageUrl);
		file = (TextView) findViewById(R.id.text_file);
		
		approve = (Button) findViewById(R.id.btn_StudentLeaveApprove);
		reject = (Button) findViewById(R.id.btn_StudentLeaveReject);
		imageurl = (ImageView) findViewById(R.id.imageUrl);
		name = getIntent().getStringExtra("name");

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		position = getIntent().getExtras().getInt("position");

		System.out.println(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_LIST_URL+"?sch_id="+schoolId+"&token="+token+"&u_email_id="+userEmailId+"&u_id="+userId+"&cls_id="+classId+"&sec_id="+sectionId);

		try
		{
			Entry e;
			e = VolleySingleton.getInstance(TeacherLeaveDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_LIST_URL+"?sch_id="+schoolId+"&token="+token+"&u_email_id="+userEmailId+"&u_id="+userId+"&cls_id="+classId+"&sec_id="+sectionId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			teacherStudentLeaveListArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentLeaveListArray!= null)
		{
			try {
                date2 = teacherStudentLeaveListArray.getJSONObject(position).getString("to_date");
				Date date31= formatter.parse(date2);
				
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				date3 = formatter1.format(date31);
				
				date4 = teacherStudentLeaveListArray.getJSONObject(position).getString("from_date");
				Date date32= formatter.parse(date4);
				
				
				date5 = formatter1.format(date32);
				
				date6 = teacherStudentLeaveListArray.getJSONObject(position).getString("leave_date");
				Date date33= formatter.parse(date6);
				
				
				date7 = formatter1.format(date33);

				studentName1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("stu_name"));
				leaveSubject1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("subject"));
				leaveDetails1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("description"));
				studentClass1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("class_name")+"-"+teacherStudentLeaveListArray.getJSONObject(position).getString("section_name"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//to1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("to_date"));
				//from1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("from_date"));
				//date1.setText(teacherStudentLeaveListArray.getJSONObject(position).getString("leave_date"));
				to1.setText(date3);
				from1.setText(date5);
				date1.setText(date7);
				image = teacherStudentLeaveListArray.getJSONObject(position).getString("attachment");
				//imageUrl.setText("http://schoofi.com/leave_uploads/"+teacherStudentLeaveListArray.getJSONObject(position).getString("picture"));
				leaveId = teacherStudentLeaveListArray.getJSONObject(position).getString("leave_id");
				//System.out.println(teacherStudentLeaveListArray.getJSONObject(position).getString("status"));
				if(teacherStudentLeaveListArray.getJSONObject(position).getString("status").equals("0"))
				{
					status.setText("Pending");
					status.setTextColor(getResources().getColor(R.color.orange));

				}
				else
					if(teacherStudentLeaveListArray.getJSONObject(position).getString("status").equals("1"))
					{
						status.setText("Approved");
						status.setTextColor(getResources().getColor(R.color.green));
						approve.setVisibility(View.INVISIBLE);
						reject.setVisibility(View.INVISIBLE);

					}
					else
						if(teacherStudentLeaveListArray.getJSONObject(position).getString("status").equals("2"))
						{
							status.setText("Rejected");
							status.setTextColor(getResources().getColor(R.color.red));
							approve.setVisibility(View.INVISIBLE);
							reject.setVisibility(View.INVISIBLE);

						}
						else
						{   status.setText("Rejected");
						status.setTextColor(getResources().getColor(R.color.red));

						}
				
				if(image.matches(""))
				{
					imageurl.setImageResource(R.drawable.cameracross);
				}
				
				else
				{
					imageurl.setImageResource(R.drawable.camera);
				}





			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

		imageurl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(image.matches(""))
				{
					Utils.showToast(getApplicationContext(), "image not present");
				}
				else
				{
				Intent intent = new Intent(TeacherLeaveDetails.this,TeacherStudentImageDetails.class);
				intent.putExtra("imageUrl", image);
				startActivity(intent);
				}

			}
		});

		approve.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				postMessage();

			}
		});

		reject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				postMessage1();

			}
		});



	}

	private void postMessage()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_UPDATE_URL/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(TeacherLeaveDetails.this,"Error Submitting Comment");
						

					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(TeacherLeaveDetails.this, "Session Expired:Please Login Again");
						}

						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{

								Utils.showToast(TeacherLeaveDetails.this,"Successfuly Submitted ");
								finish();
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(TeacherLeaveDetails.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(TeacherLeaveDetails.this, "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(TeacherLeaveDetails.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("stu_id",Preferences.getInstance().studentId);
				params.put("leave_id", leaveId);
				params.put("status","1");
				params.put("u_email_id",userEmailId);
				params.put("u_id",userId);
				params.put("token", token);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("from",date4);
				params.put("to",date2);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("name",name);
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
				setSupportProgressBarIndeterminateVisibility(false);
			}
	}

	private void postMessage1()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LEAVE_UPDATE_URL/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(TeacherLeaveDetails.this,"Error Submitting Comment");
						loading.dismiss();
						

					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(TeacherLeaveDetails.this, "Session Expired:Please Login Again");
							loading.dismiss();
						}

						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
                                loading.dismiss();
								Utils.showToast(TeacherLeaveDetails.this,"Successfuly Submitted");

								finish();
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					loading.dismiss();
					Utils.showToast(TeacherLeaveDetails.this, "Error submitting alert! Please try after sometime.");
				}
				//setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
					Utils.showToast(TeacherLeaveDetails.this, "Error submitting alert! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(TeacherLeaveDetails.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("stu_id",Preferences.getInstance().studentId);
				params.put("leave_id", leaveId);
				params.put("status","2");
				params.put("u_email_id",userEmailId);
				params.put("u_id",userId);
				params.put("token", token);
				params.put("from",date4);
				params.put("to",date2);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("name",name);
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(this))
				queue.add(requestObject);
			else
			{
				loading.dismiss();
				Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_leave_details, menu);
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
