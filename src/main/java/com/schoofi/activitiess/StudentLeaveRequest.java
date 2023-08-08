package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.StudentLeaveListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentLeaveRequest extends AppCompatActivity {

	private TextView leaveList;
	private ImageView backButton;
	private Button newLeave;
	private JSONArray studentLeaveListArray;
	private ListView studentLeaveList;
	private StudentLeaveListAdapter studentLeaveListAdapter;

	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String fontPath = "fonts/asap.regular.ttf";
	private SwipyRefreshLayout swipyRefreshLayout;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_leave);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student LeaveRequest");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		Typeface face = Typeface.createFromAsset(getAssets(), fontPath);
		leaveList = (TextView) findViewById(R.id.txt_LeaveList);
		backButton = (ImageView) findViewById(R.id.img_back);
		studentLeaveList = (ListView) findViewById(R.id.student_leave_list);
		newLeave = (Button) findViewById(R.id.btn_student_new_leave_application);
		newLeave.setTypeface(face);

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		initData();
		getStudentLeaveList();

		swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

		Preferences.getInstance().loadPreference(getApplicationContext());

		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				initData();
				getStudentLeaveList();
			}
		});
		studentLeaveList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StudentLeaveRequest.this,StudentLeaveDetail.class);
				intent.putExtra("position", position);
				startActivity(intent);
			

			}

		});
		newLeave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StudentLeaveRequest.this,StudentNewLeave.class);
				startActivity(intent);
				

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		getStudentLeaveList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_leave, menu);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_LEAVE_LIST_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId);
			if(e == null)
			{
				studentLeaveListArray= null;
			}
			else
			{
				studentLeaveListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentLeaveListArray!= null)
		{
			studentLeaveListAdapter= new StudentLeaveListAdapter(StudentLeaveRequest.this,studentLeaveListArray);
			studentLeaveList.setAdapter(studentLeaveListAdapter);
			studentLeaveListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentLeaveList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentLeaveRequest.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_LEAVE_LIST_URL/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
						Utils.showToast(StudentLeaveRequest.this,"No Leaves Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(StudentLeaveRequest.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Leavelist"))
							{
								studentLeaveListArray= new JSONObject(response).getJSONArray("Leavelist");
								if(null!=studentLeaveListArray && studentLeaveListArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentLeaveListArray.toString().getBytes();
									VolleySingleton.getInstance(StudentLeaveRequest.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_LEAVE_LIST_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId,e);
									studentLeaveList.invalidateViews();
									studentLeaveListAdapter = new StudentLeaveListAdapter(StudentLeaveRequest.this, studentLeaveListArray);
									studentLeaveList.setAdapter(studentLeaveListAdapter);
									studentLeaveListAdapter.notifyDataSetChanged();
									swipyRefreshLayout.setRefreshing(false);
								}
							}
							else
								Utils.showToast(StudentLeaveRequest.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentLeaveRequest.this, "Error fetching modules! Please try after sometime.");
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
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("crr_date",currentDate);
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(StudentLeaveRequest.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(StudentLeaveRequest.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
}
