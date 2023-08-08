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
import com.schoofi.adapters.ParentHomeScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParentHomeScreen extends AppCompatActivity {
	
	private Button addStudent,logout;
	private TextView title;
	ImageView back;
	private ListView parentHomeScreenListView;
	
	String email = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String userId = Preferences.getInstance().userId;
	
    private JSONArray parentHomeScreenArray;
    private ParentHomeScreenAdapter parentHomeScreenAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_parent_home_screen);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Parent HomeScreen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		addStudent = (Button) findViewById(R.id.btn_parent_add_child);
		logout = (Button) findViewById(R.id.btn_parentLogout);
		title = (TextView) findViewById(R.id.txt_parent);
		parentHomeScreenListView = (ListView) findViewById(R.id.parent_home_screen_listView);

		addStudent.setVisibility(View.GONE);

		
		initData();
		getStudentFeedList();
		
		parentHomeScreenListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				Preferences.getInstance().loadPreference(ParentHomeScreen.this);
				try {
					Preferences.getInstance().studentId = parentHomeScreenArray.getJSONObject(position).getString("stu_id");
					Preferences.getInstance().schoolId = parentHomeScreenArray.getJSONObject(position).getString("school_id");
					Preferences.getInstance().studentClassId = parentHomeScreenArray.getJSONObject(position).getString("class_id");
					Preferences.getInstance().studentName = parentHomeScreenArray.getJSONObject(position).getString("stu_name");
					Preferences.getInstance().studentSectionId = parentHomeScreenArray.getJSONObject(position).getString("section_id");
					Preferences.getInstance().schoolLattitude = parentHomeScreenArray.getJSONObject(position).getString("sch_latitude");
					Preferences.getInstance().schoolLongitude = parentHomeScreenArray.getJSONObject(position).getString("sch_longitude");
					Preferences.getInstance().addmissionNumber = parentHomeScreenArray.getJSONObject(position).getString("admn_No");
					Preferences.getInstance().eventAddCharges = parentHomeScreenArray.getJSONObject(position).getString("event_add_charges");
					Preferences.getInstance().feesAddCharges = parentHomeScreenArray.getJSONObject(position).getString("fee_add_charges");
					Preferences.getInstance().permissions = parentHomeScreenArray.getJSONObject(position).getString("permission");
					Preferences.getInstance().board = parentHomeScreenArray.getJSONObject(position).getString("board");
					Preferences.getInstance().session1 = parentHomeScreenArray.getJSONObject(position).getString("session");
					Preferences.getInstance().schoolName = parentHomeScreenArray.getJSONObject(position).getString("school_name");
					Preferences.getInstance().studentGender = parentHomeScreenArray.getJSONObject(position).getString("gender");
					Preferences.getInstance().schoolType = parentHomeScreenArray.getJSONObject(position).getString("school_type");
					Preferences.getInstance().studentSemester = parentHomeScreenArray.getJSONObject(position).getString("semester");
					Preferences.getInstance().studentCourse = parentHomeScreenArray.getJSONObject(position).getString("course_id");
					Preferences.getInstance().savePreference(ParentHomeScreen.this);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Intent intent = new Intent(ParentHomeScreen.this,ParentInnerHomeScreen.class);
				startActivity(intent);
				
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Preferences.getInstance().isLoggedIn = false;
				Intent intent = new Intent(ParentHomeScreen.this,LoginScreen.class);
				startActivity(intent);
				finish();
				
				
			}
		});
		
		addStudent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(ParentHomeScreen.this,CredentialsScreen1.class);
				intent.putExtra("email", email);
				intent.putExtra("mobile", "");
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parent_home_screen, menu);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_LIST+"?email="+email+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				parentHomeScreenArray= null;
			}
			else
			{
				parentHomeScreenArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(parentHomeScreenArray!= null)
		{
			parentHomeScreenAdapter= new ParentHomeScreenAdapter(ParentHomeScreen.this,parentHomeScreenArray);
			parentHomeScreenListView.setAdapter(parentHomeScreenAdapter);
			parentHomeScreenAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getStudentFeedList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ParentHomeScreen.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_LIST+"?email="+email+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId;
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
						Utils.showToast(ParentHomeScreen.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ParentHomeScreen.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Student_details"))
							{
								parentHomeScreenArray= new JSONObject(response).getJSONArray("Student_details");
								if(null!=parentHomeScreenArray && parentHomeScreenArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = parentHomeScreenArray.toString().getBytes();
									VolleySingleton.getInstance(ParentHomeScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_STUDENT_LIST+"?email="+email+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId,e);
									parentHomeScreenListView.invalidateViews();
									parentHomeScreenAdapter = new ParentHomeScreenAdapter(ParentHomeScreen.this, parentHomeScreenArray);
									parentHomeScreenListView.setAdapter(parentHomeScreenAdapter);
									parentHomeScreenAdapter.notifyDataSetChanged();	
									
								}
							}
							else
								Utils.showToast(ParentHomeScreen.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(ParentHomeScreen.this, "Error fetching modules! Please try after sometime.");
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
			if(Utils.isNetworkAvailable(ParentHomeScreen.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(ParentHomeScreen.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initData();
		getStudentFeedList();
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
