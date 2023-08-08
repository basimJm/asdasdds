package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.TeacherStudentResultListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeacherStudentResultDetails extends AppCompatActivity {
	
	private TextView screenTitle,noOfstudents;
	private ListView teacherStudentResultListView;
	String index,examId,termId;
	TeacherStudentResultListAdapter teacherStudentResultListAdapter;
	private JSONArray teacherStudentResultDetailsArray;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_teacher_student_result_details);
		screenTitle = (TextView) findViewById(R.id.txt_teacherStudentResultDetails);
		index = getIntent().getStringExtra("index");
		examId = getIntent().getStringExtra("examId");
		termId = getIntent().getStringExtra("termId");
		//System.out.println(index);
		noOfstudents = (TextView) findViewById(R.id.textTotalStudentsDetails);
		teacherStudentResultListView = (ListView) findViewById(R.id.listViewTeacherStudentResult);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initData();
		getStudentResultList();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_student_result_details, menu);
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
	
	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_RESULT_DETAILS+"?token="+Preferences.getInstance().token+"&index_id="+index+"&u_email_id="+Preferences.getInstance().userEmailId+"&sec_id="+Preferences.getInstance().studentSectionId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&exam_id="+examId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board);
			if(e == null)
			{
				teacherStudentResultDetailsArray= null;
			}
			else
			{
				teacherStudentResultDetailsArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentResultDetailsArray!= null)
		{
			teacherStudentResultListAdapter= new TeacherStudentResultListAdapter(TeacherStudentResultDetails.this,teacherStudentResultDetailsArray);
			teacherStudentResultListView.setAdapter(teacherStudentResultListAdapter);
			teacherStudentResultListAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getStudentResultList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_RESULT_DETAILS+"?token="+Preferences.getInstance().token+"&index_id="+index+"&u_email_id="+Preferences.getInstance().userEmailId+"&sec_id="+Preferences.getInstance().studentSectionId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&exam_id="+examId+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
					}
						
					
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Student_list"))
							{
								
								teacherStudentResultDetailsArray= new JSONObject(response).getJSONArray("Student_list");
								
								
								if(null!=teacherStudentResultDetailsArray && teacherStudentResultDetailsArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherStudentResultDetailsArray.toString().getBytes();
									int i;
									for(i = 0;i<teacherStudentResultDetailsArray.length();i++)
									{
										
									}
									noOfstudents.setText("No.of Students:"+String.valueOf(i));
									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_RESULT_DETAILS+"?token="+Preferences.getInstance().token+"&index_id="+index+"&u_email_id="+Preferences.getInstance().userEmailId+"&sec_id="+Preferences.getInstance().studentSectionId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&exam_id="+examId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board,e);
									teacherStudentResultListView.invalidateViews();
									teacherStudentResultListAdapter = new TeacherStudentResultListAdapter(TeacherStudentResultDetails.this, teacherStudentResultDetailsArray);
									teacherStudentResultListView.setAdapter(teacherStudentResultListAdapter);
									teacherStudentResultListAdapter.notifyDataSetChanged();
								}	
									
									
									
									
									
									
					
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
								}
							
							else
								Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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
			@Override
			protected Map<String,String> getParams(){
				
				Preferences.getInstance().loadPreference(TeacherStudentResultDetails.this);
				
				Map<String,String> params = new HashMap<String, String>();
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("index_id", index);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("exam_id",examId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("board",Preferences.getInstance().board);
				params.put("session",Preferences.getInstance().session1);
				params.put("term",termId);
				//params.put("crr_date",currentDate);
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(getApplicationContext()))
				queue.add(requestObject);
			else
			{
				Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
}
