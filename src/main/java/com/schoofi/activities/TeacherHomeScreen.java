package com.schoofi.activities;

import java.util.ArrayList;

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
import com.schoofi.activitiess.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activitiess.ParentHomeScreen;
import com.schoofi.activitiess.TeacherCoOrdinatorHomeScreen;
import com.schoofi.activitiess.TeacherCordinatorAttendanceAnalysisScreen;
import com.schoofi.activitiess.TeacherInnerHomeScreen;
import com.schoofi.adapters.ParentHomeScreenAdapter;
import com.schoofi.adapters.TeacherHomeScreenAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherHomeScreenVO;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TeacherHomeScreen extends AppCompatActivity {
	
	private TextView schoolClass,logouut;
	ImageView backButton;
	public ArrayList<TeacherHomeScreenVO> teacherHomeScreenList;
	private ListView teacherHomeScreenListView;
	TeacherHomeScreenAdapter teacherHomeScreenAdapter;
	private JSONArray teacherHomeScreenArray;
	
	String email = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	private Button viewAnalysis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.activity_teacher_home_screen);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher HomeScreen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		schoolClass = (TextView) findViewById(R.id.txt_class);
		backButton = (ImageView) findViewById(R.id.img_back);
		teacherHomeScreenListView = (ListView) findViewById(R.id.teacher_homescreen_listview);
		logouut = (TextView) findViewById(R.id.txt_teacherLogout);
		viewAnalysis = (Button) findViewById(R.id.btn_analysis);
		logouut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(TeacherHomeScreen.this,LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(TeacherHomeScreen.this);
				startActivity(intent);
				
			}
		});

		viewAnalysis.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(TeacherHomeScreen.this, TeacherCoOrdinatorHomeScreen.class);
				startActivity(intent);
			}
		});

		initData();
		getClassList();
		
		teacherHomeScreenListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				Preferences.getInstance().loadPreference(TeacherHomeScreen.this);
				try {
					Preferences.getInstance().studentClassId = teacherHomeScreenArray.getJSONObject(position).getString("class_id");
					Preferences.getInstance().studentSectionId = teacherHomeScreenArray.getJSONObject(position).getString("section_id");
					Preferences.getInstance().sectionName = teacherHomeScreenArray.getJSONObject(position).getString("class_name")+"-"+teacherHomeScreenArray.getJSONObject(position).getString("section_name");
					Preferences.getInstance().chairmanAssignmentSectionName = teacherHomeScreenArray.getJSONObject(position).getString("class_name")+"-"+teacherHomeScreenArray.getJSONObject(position).getString("section_name");
					Preferences.getInstance().savePreference(TeacherHomeScreen.this);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				//Preferences.getInstance().loadPreference(TeacherHomeScreen.this);
				//System.out.print(Preferences.getInstance().teachId);
				
				Intent intent = new Intent(TeacherHomeScreen.this,TeacherInnerHomeScreen.class);
				startActivity(intent);
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_home_screen, menu);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_CLASS_LIST+"?u_email="+email+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				teacherHomeScreenArray= null;
			}
			else
			{
				teacherHomeScreenArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherHomeScreenArray!= null)
		{
			try {
				if(teacherHomeScreenArray.getJSONObject(0).getString("is_coordinator").matches("YES"))
                {
                    viewAnalysis.setVisibility(View.VISIBLE);
                }
                else {
                    viewAnalysis.setVisibility(View.GONE);
                }
			} catch (JSONException e) {
				e.printStackTrace();
			}
			teacherHomeScreenAdapter= new TeacherHomeScreenAdapter(TeacherHomeScreen.this,teacherHomeScreenArray);
			teacherHomeScreenListView.setAdapter(teacherHomeScreenAdapter);
			teacherHomeScreenAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getClassList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherHomeScreen.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_CLASS_LIST+"?u_email="+email+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId+"&ins_id="+Preferences.getInstance().institutionId;
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
						Utils.showToast(TeacherHomeScreen.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(TeacherHomeScreen.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("class_List"))
							{
								teacherHomeScreenArray= new JSONObject(response).getJSONArray("class_List");
								if(null!=teacherHomeScreenArray && teacherHomeScreenArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherHomeScreenArray.toString().getBytes();
									VolleySingleton.getInstance(TeacherHomeScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_CLASS_LIST+"?u_email="+email+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId+"&ins_id="+Preferences.getInstance().institutionId,e);
									if(teacherHomeScreenArray.getJSONObject(0).getString("is_coordinator").matches("YES"))
									{
										viewAnalysis.setVisibility(View.VISIBLE);
									}
									else {
										viewAnalysis.setVisibility(View.GONE);
									}
									teacherHomeScreenListView.invalidateViews();
									teacherHomeScreenAdapter = new TeacherHomeScreenAdapter(TeacherHomeScreen.this, teacherHomeScreenArray);
									teacherHomeScreenListView.setAdapter(teacherHomeScreenAdapter);
									teacherHomeScreenAdapter.notifyDataSetChanged();	
									
								}
							}
							else
								Utils.showToast(TeacherHomeScreen.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(TeacherHomeScreen.this, "Error fetching modules! Please try after sometime.");
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
			if(Utils.isNetworkAvailable(TeacherHomeScreen.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(TeacherHomeScreen.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
}
