package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.HealthAndAuditListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HealthAndAuditAuditsList extends AppCompatActivity {
	
	private Button audit;
	private ListView auditListView;
	ImageView back;
	private JSONArray healthAndAuditListArray;
	HealthAndAuditListAdapter healthAndAuditListAdapter;
	String taskId,uniqueId,userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Health And Audit AuditsList");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_health_and_audit_task_list);
		
		audit = (Button) findViewById(R.id.btn_addTask);
		auditListView = (ListView) findViewById(R.id.listViewAddTask);
		audit.setText("Review");
		back = (ImageView) findViewById(R.id.img_back);

		audit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(HealthAndAuditAuditsList.this,HealthAndAuditReviewForm.class);
				startActivity(intent);
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		initData();
		getClassList();

		auditListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



				try {
					taskId = healthAndAuditListArray.getJSONObject(i).getString("task_id");
					uniqueId = healthAndAuditListArray.getJSONObject(i).getString("uniqueId");
					userId = healthAndAuditListArray.getJSONObject(i).getString("user_id");
					//Utils.showToast(getApplicationContext(),userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(HealthAndAuditAuditsList.this,HealthAndAuditUserSubtaskList.class);
				intent.putExtra("position",i);
				intent.putExtra("value","3");
				intent.putExtra("task_id",taskId);
				intent.putExtra("unique_id",uniqueId);
				intent.putExtra("userId",userId);
				startActivity(intent);
			}
		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		getClassList();
	}

	private void initData()
	{


		try
		{
			Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_ASSIGNED_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				healthAndAuditListArray= null;
			}
			else
			{
				healthAndAuditListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(healthAndAuditListArray!= null)
		{
			healthAndAuditListAdapter= new HealthAndAuditListAdapter(HealthAndAuditAuditsList.this,healthAndAuditListArray);
			auditListView.setAdapter(healthAndAuditListAdapter);
			healthAndAuditListAdapter.notifyDataSetChanged();
		}
	}

	protected void getClassList()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(HealthAndAuditAuditsList.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_ASSIGNED_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				//System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);

					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(HealthAndAuditAuditsList.this,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(HealthAndAuditAuditsList.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("audit"))
					{
						healthAndAuditListArray= new JSONObject(response).getJSONArray("audit");
						if(null!=healthAndAuditListArray && healthAndAuditListArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = healthAndAuditListArray.toString().getBytes();
							VolleySingleton.getInstance(HealthAndAuditAuditsList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.HEALTH_AND_AUDIT_ASSIGNED_LIST+"?u_id="+ Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
							auditListView.invalidateViews();
							healthAndAuditListAdapter= new HealthAndAuditListAdapter(HealthAndAuditAuditsList.this,healthAndAuditListArray);
							auditListView.setAdapter(healthAndAuditListAdapter);
							healthAndAuditListAdapter.notifyDataSetChanged();

						}
					}
					else
						Utils.showToast(HealthAndAuditAuditsList.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(HealthAndAuditAuditsList.this, "Error fetching modules! Please try after sometime.");
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
		if(Utils.isNetworkAvailable(HealthAndAuditAuditsList.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(HealthAndAuditAuditsList.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_and_audit_audits_list, menu);
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
