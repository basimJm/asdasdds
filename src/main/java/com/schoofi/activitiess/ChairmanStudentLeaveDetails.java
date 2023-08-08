package com.schoofi.activitiess;

import android.app.ProgressDialog;
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

public class ChairmanStudentLeaveDetails extends AppCompatActivity {
	
	private TextView screenTitle,file,status,studentName,studentName1,leaveSubject,leaveSubject1,studentClass,studentClass1,date,date1,from,from1,to,to1,leaveDetails,leaveDetails1;
	private int position;
	private ImageView statusImage,back;
	private JSONArray chairmanStudentLeaveListArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String imageUrl;
	String dates1,date2,date3,date4,date5,date6;
	private Button approve,reject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_chairman_student_leave_details);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentLeaveDetails");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		screenTitle = (TextView) findViewById(R.id.txt_chairmanLeaveDetails);
		status = (TextView) findViewById(R.id.text_teacherStudentLeaveStatus);
		studentName = (TextView) findViewById(R.id.text_StudentNameChairmanLeaveDetails);
		studentName1 = (TextView) findViewById(R.id.text_StudentNameChairmanLeaveDetails1);
		leaveSubject = (TextView) findViewById(R.id.text_LeaveSubjectChairmanLeaveDetails);
		leaveSubject1 = (TextView) findViewById(R.id.text_LeaveSubjectChairmanLeaveDetails1);
		leaveDetails = (TextView) findViewById(R.id.text_studentLeaveDetailsChairmanLeaveDetails);
		leaveDetails1 = (TextView) findViewById(R.id.text_studentLeaveDetailsChairmanLeaveDetails1);
		studentClass = (TextView) findViewById(R.id.text_studentClassChairmanLeaveDetails);
		studentClass1 = (TextView) findViewById(R.id.text_studentClassChairmanLeaveDetails1);
		date = (TextView) findViewById(R.id.text_studentDateChairmanLeaveDetails);
		date1 = (TextView) findViewById(R.id.text_studentDateChairmanLeaveDetails1);
		from = (TextView) findViewById(R.id.text_studentLeaveStartingDateChairmanLeaveDetails);
		from1 = (TextView) findViewById(R.id.text_studentLeaveStartingDateChairmanLeaveDetails1);
		to = (TextView) findViewById(R.id.text_studentLeaveEndingDateChairmanLeaveDetails);
		to1 = (TextView) findViewById(R.id.text_studentLeaveEndingDateChairmanLeaveDetails1);
		statusImage = (ImageView) findViewById(R.id.chairmanLeaveImage);
		file = (TextView) findViewById(R.id.text_file);
		back = (ImageView) findViewById(R.id.img_back);
		approve = (Button) findViewById(R.id.btn_StudentLeaveApprove);
		reject = (Button) findViewById(R.id.btn_StudentLeaveReject);

		approve.setVisibility(View.GONE);
		reject.setVisibility(View.GONE);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		position = getIntent().getExtras().getInt("position");
		
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_LEAVE_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			chairmanStudentLeaveListArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentLeaveListArray!= null)
		{
			try {

                dates1 = chairmanStudentLeaveListArray.getJSONObject(position).getString("leave_date");
                date2 = chairmanStudentLeaveListArray.getJSONObject(position).getString("to_date");
                date3 = chairmanStudentLeaveListArray.getJSONObject(position).getString("from_date");
                Date date31 = formatter.parse(dates1);
                Date date32 = formatter.parse(date2);
                Date date33 = formatter.parse(date3);
            	
            	SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            	date4 = formatter1.format(date31);
            	date5 = formatter1.format(date32);
            	date6 = formatter1.format(date33);
            	to1.setText(date5);
            	from1.setText(date6);
            	date1.setText(date4);
				studentName1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("stu_name"));
				leaveSubject1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("subject"));
				leaveDetails1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("description"));
				studentClass1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("class_name")+"-"+chairmanStudentLeaveListArray.getJSONObject(position).getString("section_name"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//to1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("to_date"));
				//from1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("from_date"));
				//date1.setText(chairmanStudentLeaveListArray.getJSONObject(position).getString("leave_date"));
				//imageUrl.setText("http://schoofi.com/leave_uploads/"+teacherStudentLeaveListArray.getJSONObject(position).getString("picture"));
				//leaveId = teacherStudentLeaveListArray.getJSONObject(position).getString("leave_id");
				//System.out.println(chairmanStudentLeaveListArray.getJSONObject(position).getString("status"));
				if(chairmanStudentLeaveListArray.getJSONObject(position).getString("status").equals("0"))
				{
					status.setText("Pending");
					status.setTextColor(getResources().getColor(R.color.orange));
					approve.setVisibility(View.VISIBLE);
					reject.setVisibility(View.VISIBLE);

				}
				else
					if(chairmanStudentLeaveListArray.getJSONObject(position).getString("status").equals("1"))
					{
						status.setText("Approved");
						status.setTextColor(getResources().getColor(R.color.green));
						approve.setVisibility(View.GONE);
						reject.setVisibility(View.GONE);
						
					}
					else
						if(chairmanStudentLeaveListArray.getJSONObject(position).getString("status").equals("2"))
						{
							status.setText("Rejected");
							status.setTextColor(getResources().getColor(R.color.red));
							approve.setVisibility(View.GONE);
							reject.setVisibility(View.GONE);
							
						}
						else
						{   status.setText("Rejected");
						status.setTextColor(getResources().getColor(R.color.red));
							approve.setVisibility(View.GONE);
							reject.setVisibility(View.GONE);

						}
				
				imageUrl = chairmanStudentLeaveListArray.getJSONObject(position).getString("attachment");
				
				if(imageUrl.matches(""))
				{
					statusImage.setImageResource(R.drawable.cameracross);
				}
				
				else
				{
					statusImage.setImageResource(R.drawable.camera);
				}





			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	statusImage.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(imageUrl.matches(""))
			{
				Utils.showToast(ChairmanStudentLeaveDetails.this,"Image Not Present");
			}
			
			else
			{
				Intent intent = new Intent(ChairmanStudentLeaveDetails.this,TeacherStudentImageDetails.class);
				intent.putExtra("imageUrl", imageUrl);
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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chairman_student_leave_details, menu);
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

						Utils.showToast(getApplicationContext(),"Error Submitting Comment");


					}
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
					}

					else
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
					{

						Utils.showToast(getApplicationContext(),"Successfuly Submitted ");
						finish();
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error) {

				Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(getApplicationContext());
				Map<String,String> params = new HashMap<String, String>();
				try {
					params.put("stu_id",chairmanStudentLeaveListArray.getJSONObject(position).getString("student_id"));
					params.put("leave_id", chairmanStudentLeaveListArray.getJSONObject(position).getString("leave_id"));
					params.put("status","1");
					params.put("u_email_id",Preferences.getInstance().userEmailId);
					params.put("u_id",Preferences.getInstance().userId);
					params.put("token", Preferences.getInstance().token);
					params.put("from",chairmanStudentLeaveListArray.getJSONObject(position).getString("from_date"));
					params.put("to",chairmanStudentLeaveListArray.getJSONObject(position).getString("to_date"));
					params.put("device_id", Preferences.getInstance().deviceId);
					params.put("ins_id",Preferences.getInstance().institutionId);
					params.put("sch_id",Preferences.getInstance().schoolId);
					params.put("name",chairmanStudentLeaveListArray.getJSONObject(position).getString("stu_name"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
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

						Utils.showToast(getApplicationContext(),"Error Submitting Comment");
						loading.dismiss();


					}
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						loading.dismiss();
					}

					else
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
					{
						loading.dismiss();
						Utils.showToast(getApplicationContext(),"Successfuly Submitted");

						finish();
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					loading.dismiss();
					Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
				}
				//setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error) {
				loading.dismiss();
				Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(getApplicationContext());
				Map<String,String> params = new HashMap<String, String>();
				try {
					params.put("stu_id",chairmanStudentLeaveListArray.getJSONObject(position).getString("student_id"));
					params.put("leave_id", chairmanStudentLeaveListArray.getJSONObject(position).getString("leave_id"));
					params.put("status","2");
					params.put("u_email_id",Preferences.getInstance().userEmailId);
					params.put("u_id",Preferences.getInstance().userId);
					params.put("token", Preferences.getInstance().token);
					params.put("from",chairmanStudentLeaveListArray.getJSONObject(position).getString("from_date"));
					params.put("to",chairmanStudentLeaveListArray.getJSONObject(position).getString("to_date"));
					params.put("device_id", Preferences.getInstance().deviceId);
					params.put("ins_id",Preferences.getInstance().institutionId);
					params.put("sch_id",Preferences.getInstance().schoolId);
					params.put("name",chairmanStudentLeaveListArray.getJSONObject(position).getString("stu_name"));

				} catch (JSONException e) {
					e.printStackTrace();
				}
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
}
