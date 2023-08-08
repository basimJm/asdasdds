package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.AuditPersonVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.SchoolIdVO;
import com.schoofi.utils.TaskVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HealthAndAuditAuditsForm extends AppCompatActivity {
	
	private Spinner schoolList,personList,taskList,subTaskList;
	private Button addAudit;
	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<String> schoolName;
	ArrayList<SchoolIdVO> schoolId;
	ArrayList<String> personName;
	ArrayList<String> personUserName;
	ArrayList<AuditPersonVO> personId;
	ArrayList<TaskVO> taskId;
	ArrayList<String> taskName;
	String schoolId1,personId1,taskId1;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Health And Audit AuditsForm");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_health_and_audit_audits_form);
		
		schoolList = (Spinner) findViewById(R.id.spinnerSchoolList);
		personList = (Spinner) findViewById(R.id.spinnerAuditPersonList);
		taskList = (Spinner) findViewById(R.id.spinnerAuditTaskList);
		subTaskList = (Spinner) findViewById(R.id.spinnerAuditSubTaskList);
		
		addAudit = (Button) findViewById(R.id.btn_addAudit);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		if(Utils.isNetworkAvailable(getApplicationContext()))
		{
			new DownloadJSON().execute();
			new DownloadJSON1().execute();
			new DownloadJSON2().execute();
		}
		
		else
		{
			Utils.showToast(getApplicationContext(), "Please enable internet services");
		}
	}
	
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		 
		@Override
		protected Void doInBackground(Void... params) {
				
			
			// Create an array to populate the spinner 
			schoolId = new ArrayList<SchoolIdVO>();
			schoolName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/
			
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.AUDIT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("school_list");
				//System.out.print(jsonarray.toString());
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					SchoolIdVO schoolIdVO = new SchoolIdVO();
					
					schoolIdVO.setSchoolid(jsonobject.optString("school_id"));
					schoolId.add(schoolIdVO);
					
					schoolName.add(jsonobject.optString("school_name"));
					
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml
			
			schoolList
			.setAdapter(new ArrayAdapter<String>(HealthAndAuditAuditsForm.this,
					android.R.layout.simple_spinner_dropdown_item,
					schoolName));
			
			schoolList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					
					schoolId1 = schoolId.get(position).getSchoolid().toString();
					//System.out.println(schoolId1);

					// TODO Auto-generated method stub
					
					
					
					

					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
			
			
			
	
		}
	}
	
	
	private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {
		 
		@Override
		protected Void doInBackground(Void... params) {
				
			
			// Create an array to populate the spinner 
			personId = new ArrayList<AuditPersonVO>();
			personName = new ArrayList<String>();
			personUserName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/
			
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.AUDIT_PERSON_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("users");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					AuditPersonVO auditPersonVO = new AuditPersonVO();
					
					auditPersonVO.setPersonId(jsonobject.optString("user_id"));
					personId.add(auditPersonVO);
					
					personName.add(jsonobject.optString("name"));
					personUserName.add(jsonobject.optString("user_name"));
					
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml
			
			personList
			.setAdapter(new ArrayAdapter<String>(HealthAndAuditAuditsForm.this,
					android.R.layout.simple_spinner_dropdown_item,
					personName));
			
			personList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					
					personId1 = personId.get(position).getPersonId().toString();
					//System.out.println(personId1);

					// TODO Auto-generated method stub
					
					
					
					

					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
			
			
			
	
		}
	}
	
	private class DownloadJSON2 extends AsyncTask<Void, Void, Void> {
		 
		@Override
		protected Void doInBackground(Void... params) {
				
			
			// Create an array to populate the spinner 
			taskId = new ArrayList<TaskVO>();
			taskName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/
			
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.AUDIT_TASK_SPINNER_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("task");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					TaskVO taskVO = new TaskVO();
					
					taskVO.setTaskId(jsonobject.optString("task_id"));
					taskId.add(taskVO);
					
					taskName.add(jsonobject.optString("task"));
					
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml
			
			taskList
			.setAdapter(new ArrayAdapter<String>(HealthAndAuditAuditsForm.this,
					android.R.layout.simple_spinner_dropdown_item,
					taskName));
			
			taskList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					
					taskId1 = taskId.get(position).getTaskId().toString();
					//System.out.println(taskId1);

					// TODO Auto-generated method stub
					
					
					
					

					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
			
			
			
	
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_and_audit_audits_form, menu);
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
