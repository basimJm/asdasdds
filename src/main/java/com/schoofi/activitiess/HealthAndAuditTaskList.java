package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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

public class HealthAndAuditTaskList extends AppCompatActivity {
	
	private Button addTask;
	private ListView healthAndAuditTaskList;
	private JSONArray healthAndAuditTaskListArray;
	ImageView back;
	private com.schoofi.adapters.HealthAndAuditTaskList healthAndAuditTaskList2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actonBar = getSupportActionBar();
		actonBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Health And Audit TaskList");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_health_and_audit_task_list);
		
		addTask = (Button) findViewById(R.id.btn_addTask);
		healthAndAuditTaskList = (ListView) findViewById(R.id.listViewAddTask);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		initData();
		getClassList();

		
		healthAndAuditTaskList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String taskId="";
				
				try {
					taskId = healthAndAuditTaskListArray.getJSONObject(position).getString("task_id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(HealthAndAuditTaskList.this,HealthAndAuditSubtask.class);
				intent.putExtra("taskId", taskId);
				startActivity(intent);
				
			}
		});
	
		
		addTask.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(HealthAndAuditTaskList.this,HealthAndAuditAddTask.class);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_TASK_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				healthAndAuditTaskListArray= null;
			}
			else
			{
				healthAndAuditTaskListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(healthAndAuditTaskListArray!= null)
		{
			healthAndAuditTaskList2= new com.schoofi.adapters.HealthAndAuditTaskList(HealthAndAuditTaskList.this,healthAndAuditTaskListArray);
			healthAndAuditTaskList.setAdapter(healthAndAuditTaskList2);
			healthAndAuditTaskList2.notifyDataSetChanged();
		}
	}
	
	protected void getClassList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditTaskList.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_TASK_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
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
						Utils.showToast(HealthAndAuditTaskList.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(HealthAndAuditTaskList.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("task"))
							{
								healthAndAuditTaskListArray= new JSONObject(response).getJSONArray("task");
								if(null!=healthAndAuditTaskListArray && healthAndAuditTaskListArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = healthAndAuditTaskListArray.toString().getBytes();
									VolleySingleton.getInstance(HealthAndAuditTaskList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_TASK_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
									healthAndAuditTaskList.invalidateViews();
									healthAndAuditTaskList2= new com.schoofi.adapters.HealthAndAuditTaskList(HealthAndAuditTaskList.this,healthAndAuditTaskListArray);
									healthAndAuditTaskList.setAdapter(healthAndAuditTaskList2);
									healthAndAuditTaskList2.notifyDataSetChanged();
									
								}
							}
							else
								Utils.showToast(HealthAndAuditTaskList.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(HealthAndAuditTaskList.this, "Error fetching modules! Please try after sometime.");
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
			if(Utils.isNetworkAvailable(HealthAndAuditTaskList.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(HealthAndAuditTaskList.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_and_audit_task_list, menu);
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
