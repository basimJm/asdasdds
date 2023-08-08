package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
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
import com.schoofi.adapters.TeacherStudentSubmittedAttendanceListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherStudentSubmittedAtendanceList extends AppCompatActivity {
	
	private TextView screenTitle;
	private ListView teacherStudentSubmittedAttendanceListView;
	private JSONArray teacherStudentSubmittedAttendancearray;
	TeacherStudentSubmittedAttendanceListViewAdapter teacherStudentSubmittedAttendanceListViewAdapter;
	String classId = Preferences.getInstance().studentClassId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String date;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_submitted_polls);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Student SubmittedAtendance List");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		screenTitle = (TextView) findViewById(R.id.txt_submittedPolls);
		teacherStudentSubmittedAttendanceListView = (ListView) findViewById(R.id.listview_student_submitted_polls);
		date = getIntent().getExtras().getString("date");
		screenTitle.setText("Attendance");
		//System.out.println(date);
		getTeacherStudentList();
		
		
		
		
	}
	
	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMITTED_ATTENDANCE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				teacherStudentSubmittedAttendancearray= null;
			}
			else
			{
				teacherStudentSubmittedAttendancearray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentSubmittedAttendancearray!= null)
		{
			teacherStudentSubmittedAttendanceListViewAdapter= new TeacherStudentSubmittedAttendanceListViewAdapter(TeacherStudentSubmittedAtendanceList.this,teacherStudentSubmittedAttendancearray);
			teacherStudentSubmittedAttendanceListView.setAdapter(teacherStudentSubmittedAttendanceListViewAdapter);
			teacherStudentSubmittedAttendanceListViewAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getTeacherStudentList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherStudentSubmittedAtendanceList.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMITTED_ATTENDANCE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					//System.out.println(res);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(TeacherStudentSubmittedAtendanceList.this,"No Records Found");
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{

							Utils.showToast(TeacherStudentSubmittedAtendanceList.this, "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{

								Utils.showToast(TeacherStudentSubmittedAtendanceList.this, "");
							}
						else
							if(responseObject.has("Students"))

							{

								teacherStudentSubmittedAttendancearray= new JSONObject(response).getJSONArray("Students");
								if(null!=teacherStudentSubmittedAttendancearray && teacherStudentSubmittedAttendancearray.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherStudentSubmittedAttendancearray.toString().getBytes();
									VolleySingleton.getInstance(TeacherStudentSubmittedAtendanceList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMITTED_ATTENDANCE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
									teacherStudentSubmittedAttendanceListView.invalidateViews();
									teacherStudentSubmittedAttendanceListViewAdapter = new TeacherStudentSubmittedAttendanceListViewAdapter(TeacherStudentSubmittedAtendanceList.this, teacherStudentSubmittedAttendancearray);
									teacherStudentSubmittedAttendanceListView.setAdapter(teacherStudentSubmittedAttendanceListViewAdapter);
									teacherStudentSubmittedAttendanceListViewAdapter.notifyDataSetChanged();	
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
								}
							}
							else
								Utils.showToast(TeacherStudentSubmittedAtendanceList.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(TeacherStudentSubmittedAtendanceList.this, "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("id_class_section",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("id_class",Preferences.getInstance().studentClassId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("crr_date",date);
				return params;
			}*/};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(TeacherStudentSubmittedAtendanceList.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(TeacherStudentSubmittedAtendanceList.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}

}
