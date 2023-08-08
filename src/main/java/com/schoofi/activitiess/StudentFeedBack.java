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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.StudentFeedbackListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentFeedBack extends AppCompatActivity {
	
	private Button giveFeedback;
	private ImageView back;
	private TextView title;
	private ListView studentFeedbackList;
	private JSONArray studentFeedbackArray;
	private StudentFeedbackListAdapter studentFeedbackListAdapter;
	private SwipyRefreshLayout swipyRefreshLayout;
	
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_feed_back);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student FeedBack");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		giveFeedback = (Button) findViewById(R.id.btn_student_new_feedBack);
		back = (ImageView) findViewById(R.id.img_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title = (TextView) findViewById(R.id.txt_feedbackList);
		studentFeedbackList = (ListView) findViewById(R.id.student_feedBack_list);
		initData();
		getStudentFeedList();
		giveFeedback.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StudentFeedBack.this,StudentNewFeedback.class);
				startActivity(intent);
				finish();
				
			}
		});

		swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				initData();
				getStudentFeedList();
			}
		});
		
		studentFeedbackList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Preferences.getInstance().loadPreference(StudentFeedBack.this);
				try {
					Preferences.getInstance().feedbackId = studentFeedbackArray.getJSONObject(position).getString("feedback_id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Preferences.getInstance().savePreference(StudentFeedBack.this);
				
				Intent intent = new Intent(StudentFeedBack.this,StudentFeedBackDetailss.class);
				intent.putExtra("position",position);
				startActivity(intent);
				
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		getStudentFeedList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_feed_back, menu);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&stu_id=" +studentId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				studentFeedbackArray= null;
			}
			else
			{
				studentFeedbackArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentFeedbackArray!= null)
		{
			studentFeedbackListAdapter= new StudentFeedbackListAdapter(StudentFeedBack.this,studentFeedbackArray);
			studentFeedbackList.setAdapter(studentFeedbackListAdapter);
			studentFeedbackListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentFeedList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentFeedBack.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&stu_id=" +studentId+"&device_id="+Preferences.getInstance().deviceId;
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
						Utils.showToast(StudentFeedBack.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(StudentFeedBack.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Feedlist"))
							{
								studentFeedbackArray= new JSONObject(response).getJSONArray("Feedlist");
								if(null!=studentFeedbackArray && studentFeedbackArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentFeedbackArray.toString().getBytes();
									VolleySingleton.getInstance(StudentFeedBack.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&stu_id=" +studentId+"&device_id="+Preferences.getInstance().deviceId,e);
									studentFeedbackList.invalidateViews();
									studentFeedbackListAdapter = new StudentFeedbackListAdapter(StudentFeedBack.this, studentFeedbackArray);
									studentFeedbackList.setAdapter(studentFeedbackListAdapter);
									studentFeedbackListAdapter.notifyDataSetChanged();
									swipyRefreshLayout.setRefreshing(false);
								}
							}
							else
								Utils.showToast(StudentFeedBack.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentFeedBack.this, "Error fetching modules! Please try after sometime.");
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
			if(Utils.isNetworkAvailable(StudentFeedBack.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(StudentFeedBack.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
}
