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
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.ChairmanHomeScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChairmanHomeScreen extends AppCompatActivity {
	
	private ListView chairmanHomeScreenListView;
	private TextView schoolList;
	private Button Logout,healthAndAudit;
	ChairmanHomeScreenAdapter chairmanHomeScreenAdapter;
	private JSONArray chairmanHomeScreenArray;
	ImageView back;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.activity_chairman_home_screen);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman HomeScreen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		chairmanHomeScreenListView =  (ListView) findViewById(R.id.chairman_home_screen_listView);
		schoolList = (TextView) findViewById(R.id.txt_school_list);
		Logout = (Button) findViewById(R.id.btn_Logout);
		healthAndAudit = (Button) findViewById(R.id.btn_healthAndAudit);

		healthAndAudit.setVisibility(View.GONE);

		initData();
		getSchoolFeedList();
		Logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				Intent intent = new Intent(ChairmanHomeScreen.this,LoginScreen.class);
				startActivity(intent);
				
			}
		});
		
		healthAndAudit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChairmanHomeScreen.this,HealthAndAuditHomeScreen.class);
				startActivity(intent);
				
			}
		});
		
		chairmanHomeScreenListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Preferences.getInstance().loadPreference(ChairmanHomeScreen.this);

				if(Preferences.getInstance().userRoleId.matches("7")) {
					try {
						Preferences.getInstance().schoolId = chairmanHomeScreenArray.getJSONObject(position).getString("school_id");
						Preferences.getInstance().totalStudents = chairmanHomeScreenArray.getJSONObject(position).getString("total_stu");
						//Utils.showToast(getApplicationContext(),Preferences.getInstance().totalStudents);
						Preferences.getInstance().schoolLattitude = chairmanHomeScreenArray.getJSONObject(position).getString("sch_latitude");
						Preferences.getInstance().schoolLongitude = chairmanHomeScreenArray.getJSONObject(position).getString("sch_longitude");
						Preferences.getInstance().permissions = chairmanHomeScreenArray.getJSONObject(position).getString("permission");
						Preferences.getInstance().schoolType = chairmanHomeScreenArray.getJSONObject(position).getString("school_type");
						Preferences.getInstance().session1 = chairmanHomeScreenArray.getJSONObject(position).getString("session");
						Preferences.getInstance().schoolName = chairmanHomeScreenArray.getJSONObject(position).getString("school_name");
                        Preferences.getInstance().institutionId = chairmanHomeScreenArray.getJSONObject(position).getString("institution_id");
						Preferences.getInstance().schoolScheduleLevel = chairmanHomeScreenArray.getJSONObject(0).getString("schedule_level");

						Preferences.getInstance().savePreference(ChairmanHomeScreen.this);

						Intent intent = new Intent(ChairmanHomeScreen.this, ChairmanDashboard.class);
						startActivity(intent);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				else
				{
					try {
						Preferences.getInstance().schoolId = chairmanHomeScreenArray.getJSONObject(position).getString("school_id");
						Preferences.getInstance().totalStudents = chairmanHomeScreenArray.getJSONObject(position).getString("total_stu");
						//Utils.showToast(getApplicationContext(),Preferences.getInstance().totalStudents);
						Preferences.getInstance().schoolLattitude = chairmanHomeScreenArray.getJSONObject(position).getString("sch_latitude");
						Preferences.getInstance().schoolLongitude = chairmanHomeScreenArray.getJSONObject(position).getString("sch_longitude");
						Preferences.getInstance().schoolType = chairmanHomeScreenArray.getJSONObject(position).getString("school_type");
						Preferences.getInstance().permissions = chairmanHomeScreenArray.getJSONObject(position).getString("permission");
						Preferences.getInstance().session1 = chairmanHomeScreenArray.getJSONObject(position).getString("session");
						Preferences.getInstance().schoolName = chairmanHomeScreenArray.getJSONObject(position).getString("school_name");
                        Preferences.getInstance().institutionId = chairmanHomeScreenArray.getJSONObject(position).getString("institution_id");
						Preferences.getInstance().schoolScheduleLevel = chairmanHomeScreenArray.getJSONObject(0).getString("schedule_level");
						Preferences.getInstance().userRoleId ="3";
						Preferences.getInstance().savePreference(ChairmanHomeScreen.this);
						Intent intent = new Intent(ChairmanHomeScreen.this, AdminHomeScreen.class);
						startActivity(intent);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				
			}
		});
		
		
		
		
		
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chairman_home_screen, menu);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_HOME_SCREEN_SCHOOL_LIST+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanHomeScreenArray= null;
			}
			else
			{
				chairmanHomeScreenArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanHomeScreenArray!= null)
		{
			chairmanHomeScreenAdapter= new ChairmanHomeScreenAdapter(ChairmanHomeScreen.this,chairmanHomeScreenArray);
			chairmanHomeScreenListView.setAdapter(chairmanHomeScreenAdapter);
			chairmanHomeScreenAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getSchoolFeedList() 
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_HOME_SCREEN_SCHOOL_LIST+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(getApplicationContext(),"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("School"))
							{
								chairmanHomeScreenArray= new JSONObject(response).getJSONArray("School");
								if(null!=chairmanHomeScreenArray && chairmanHomeScreenArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = chairmanHomeScreenArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanHomeScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_HOME_SCREEN_SCHOOL_LIST+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanHomeScreenListView.invalidateViews();
									chairmanHomeScreenAdapter = new ChairmanHomeScreenAdapter(ChairmanHomeScreen.this, chairmanHomeScreenArray);
									chairmanHomeScreenListView.setAdapter(chairmanHomeScreenAdapter);
									chairmanHomeScreenAdapter.notifyDataSetChanged();	
									
								}
							}
							else
								Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setProgressBarIndeterminateVisibility(false);
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
