package com.schoofi.activitiess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.HealthAndAuditSubmittedTaskListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class HealthAndAuditSubtask extends AppCompatActivity {

	private Button addTask;
	private ListView healthAndAuditTaskList;
	private JSONArray healthAndAuditSubTaskListArray;
	private HealthAndAuditSubmittedTaskListAdapter healthAndAuditSubmittedTaskListAdapter;
	String taskId;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Health And Audit Subtask");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_health_and_audit_task_list);

		addTask = (Button) findViewById(R.id.btn_addTask);
		addTask.setText("Add Subtask");
		healthAndAuditTaskList = (ListView) findViewById(R.id.listViewAddTask);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		taskId = getIntent().getStringExtra("taskId");
		initData();
		getClassList();

		addTask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(HealthAndAuditSubtask.this,HealthAndAuditAddSubtask.class);
				intent.putExtra("taskId",taskId);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		getClassList();

	}

	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_SUBTASK_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&task_id="+taskId);
			if(e == null)
			{
				healthAndAuditSubTaskListArray= null;
			}
			else
			{
				healthAndAuditSubTaskListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(healthAndAuditSubTaskListArray!= null)
		{
			healthAndAuditSubmittedTaskListAdapter= new HealthAndAuditSubmittedTaskListAdapter(HealthAndAuditSubtask.this,healthAndAuditSubTaskListArray);
			healthAndAuditTaskList.setAdapter(healthAndAuditSubmittedTaskListAdapter);
			healthAndAuditSubmittedTaskListAdapter.notifyDataSetChanged();
		}
	}

	protected void getClassList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditSubtask.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_SUBTASK_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&task_id="+taskId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
						Utils.showToast(HealthAndAuditSubtask.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(HealthAndAuditSubtask.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("sub_task"))
							{
								healthAndAuditSubTaskListArray= new JSONObject(response).getJSONArray("sub_task");
								if(null!=healthAndAuditSubTaskListArray && healthAndAuditSubTaskListArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = healthAndAuditSubTaskListArray.toString().getBytes();
									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_SUBTASK_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&task_id="+taskId,e);
									healthAndAuditTaskList.invalidateViews();
									healthAndAuditSubmittedTaskListAdapter= new HealthAndAuditSubmittedTaskListAdapter(HealthAndAuditSubtask.this,healthAndAuditSubTaskListArray);
									healthAndAuditTaskList.setAdapter(healthAndAuditSubmittedTaskListAdapter);
									healthAndAuditSubmittedTaskListAdapter.notifyDataSetChanged();

								}
							}
							else
								Utils.showToast(HealthAndAuditSubtask.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(HealthAndAuditSubtask.this, "Error fetching modules! Please try after sometime.");
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
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(HealthAndAuditSubtask.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(HealthAndAuditSubtask.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_and_audit_subtask, menu);
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
