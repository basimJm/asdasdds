package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.schoofi.adapters.ChairmanStudentAssignList;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.ChairmanAssignFeedBackListVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class  ChairmanStudentFeedBackAssign extends AppCompatActivity {
	
	private TextView assignDate;
	private Spinner assign;
	private EditText assignFeddback;
	private ListView assignListView;
	private Button postAssign;
	private DatePicker datePicker;
	private Calendar calendar;
	private int year, month, day;
	String date,date1;
	String Month;
	JSONObject jsonobject;
	JSONArray jsonarray;
	private JSONArray chairmanStudentAssignArray;
	ImageView back;
	
	ArrayList<String> names;
	ArrayList<ChairmanAssignFeedBackListVO> assignFeedBackListVOs;
	String personId,assignRoleId;
	ChairmanStudentAssignList chairmanStudentAssignList;
	String crr_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_chairman_student_feed_back_assign);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentFeedBackAssign");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		assignDate = (TextView) findViewById(R.id.ch_dateTextView);
		assign = (Spinner) findViewById(R.id.spinner_assign);
		assignFeddback = (EditText) findViewById(R.id.ch_feedback_msg);
		assignListView = (ListView) findViewById(R.id.listView_member_assign);
		postAssign = (Button) findViewById(R.id.btn_ch_assign_done);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		new DownloadJSON().execute();

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		assignDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(999);
				showDate(year, month, day);
				
			}
		});
		
		postAssign.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(assignFeddback.getText().toString().matches("") || personId.matches("select_user_id"))
				{
					Utils.showToast(ChairmanStudentFeedBackAssign.this, "Plz fill msg or select mentor");
				}
				else
				{
					postMessage();
				}
				
			}
		});
		
		
		
	}
	
	// Download JSON file AsyncTask
			private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		 
				@Override
				protected Void doInBackground(Void... params) {
						
					
					// Create an array to populate the spinner 
					assignFeedBackListVOs = new ArrayList<ChairmanAssignFeedBackListVO>();
					names = new ArrayList<String>();
					// JSON file URL address
					jsonobject = JSONfunctions
							.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ASSIGN_MEMBER+"?role_id="+Preferences.getInstance().userRoleId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
					try {
						// Locate the NodeList name
						jsonarray = jsonobject.getJSONArray("profile");
						System.out.print(jsonarray.toString());
						for (int i = 0; i < jsonarray.length(); i++) {
							jsonobject = jsonarray.getJSONObject(i);
							ChairmanAssignFeedBackListVO chairmanAssignFeedBackListVO= new ChairmanAssignFeedBackListVO();
							chairmanAssignFeedBackListVO.setRoleId(jsonobject.optString("role_id"));
							chairmanAssignFeedBackListVO.setUserEmailId(jsonobject.optString("user_email_id"));
							chairmanAssignFeedBackListVO.setUserId(jsonobject.optString("user_id"));
							chairmanAssignFeedBackListVO.setUserName("user_name");
							assignFeedBackListVOs.add(chairmanAssignFeedBackListVO);
							names.add(jsonobject.optString("Name"));
							
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

					ArrayAdapter<String> adapter1 = new ArrayAdapter<String> (ChairmanStudentFeedBackAssign.this, R.layout.spinner_layout, names);
					adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					assign.setAdapter(adapter1);


					/*assign
					.setAdapter(new ArrayAdapter<String>(ChairmanStudentFeedBackAssign.this,
							android.R.layout.simple_spinner_dropdown_item,
							names));*/
					
					assign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							// TODO Auto-generated method stub
							
							personId = assignFeedBackListVOs.get(position).getUserId().toString();
							assignRoleId = assignFeedBackListVOs.get(position).getRoleId().toString();
							if(personId.matches("select_user_id"))
							{
								Log.d("hhhh", "kkk");
							}
							
							else
							{
								((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
								initData();
								getStudentAnnouncementList();
							}
							
							
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
		getMenuInflater().inflate(R.menu.chairman_student_feed_back_assign,
				menu);
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
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2+1, arg3);
		}
	};

	private void showDate(int year, int month, int day) {

		

		switch(month)
		{
		case 1: Month = "Jan";
		break;
		case 2: Month = "Feb";
		break;
		case 3: Month = "Mar";
		break;
		case 4: Month = "Apr";
		break;
		case 5: Month = "May";
		break;
		case 6: Month = "Jun";
		break;
		case 7: Month = "Jul";
		break;
		case 8: Month = "Aug";
		break;
		case 9: Month = "Sep";
		break;
		case 10: Month = "Oct";
		break;
		case 11: Month = "Nov";
		break;
		case 12: Month = "Dec";
		break;
		default : System.out.println("llll");

		break;
		}
		
		date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

		date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
		assignDate.setText(date1);
		assignDate.setTextColor(Color.BLACK);
}
	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ASSIGN_MEMBER_PROFILE+"?role_id="+assignRoleId+"&user_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&person_id="+personId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanStudentAssignArray= null;
			}
			else
			{
				chairmanStudentAssignArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentAssignArray!= null)
		{
			chairmanStudentAssignList= new ChairmanStudentAssignList(ChairmanStudentFeedBackAssign.this,chairmanStudentAssignArray);
	        assignListView.setAdapter(chairmanStudentAssignList);
			chairmanStudentAssignList.notifyDataSetChanged();
		}
	}

	protected void getStudentAnnouncementList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentFeedBackAssign.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ASSIGN_MEMBER_PROFILE+"?role_id="+assignRoleId+"&user_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&person_id="+personId+"&device_id="+Preferences.getInstance().deviceId;
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
						Utils.showToast(ChairmanStudentFeedBackAssign.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ChairmanStudentFeedBackAssign.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("profile"))
							{
								chairmanStudentAssignArray= new JSONObject(response).getJSONArray("profile");
								if(null!=chairmanStudentAssignArray && chairmanStudentAssignArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = chairmanStudentAssignArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanStudentFeedBackAssign.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ASSIGN_MEMBER_PROFILE+"?role_id="+assignRoleId+"&user_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&person_id="+personId+"&device_id="+Preferences.getInstance().deviceId,e);
									assignListView.invalidateViews();
									chairmanStudentAssignList = new ChairmanStudentAssignList(ChairmanStudentFeedBackAssign.this, chairmanStudentAssignArray);
									assignListView.setAdapter(chairmanStudentAssignList);
									chairmanStudentAssignList.notifyDataSetChanged();	
								}
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
				
				/*try
				{
					responseObject = new JSONObject(response);
					if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("image"))
					{   
						String ddd = responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type");
                        System.out.println(ddd);
						count = 1;
					}
					else if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("video"))
					{
						count =2;
					}
					else
						if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("audio"))
						{
							count =3;
						}
						else
							if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("text")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("pdf")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("ppt"))
							{
								count =4;
							}
					
					for(int i=0;i<=studentAnnouncementArray.length();i++)
					{
						if(studentAnnouncementArray.getJSONObject(0).getString("announcement_type").equals("image"))
						{
							count = 1;
						}
						else if(studentAnnouncementArray.getJSONObject(1).getString("announcement_type").equals("video"))
						{
							count = 2;
						}
						else if(studentAnnouncementArray.getJSONObject(2).getString("announcement_type").equals("audio"))
						{
							count =3;
						}
						else if(studentAnnouncementArray.getJSONObject(3).getString("announcement_type").equals("text")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("ppt")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("pdf"))
						{
							count =4;
						}
					}
				}
					catch (Exception e) {
						// TODO: handle exception
					}*/
				



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
				Preferences.getInstance().loadPreference(StudentAnnouncement.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("student_ID",Preferences.getInstance().studentId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id",Preferences.getInstance().studentClassId);
				params.put("sch_id", Preferences.getInstance().schoolId);
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
		System.out.println(personId+Preferences.getInstance().feedbackId+crr_date+date+assignFeddback.getText().toString()+Preferences.getInstance().userId+Preferences.getInstance().schoolId);
		
	}
	
	private void postMessage()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		
		
			String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ASSIGN_DONE;
		
		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.print("kk"+response.toString());
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
						Utils.showToast(getApplicationContext(),"Error Submitting Comment");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
								
								Utils.showToast(getApplicationContext(),"Successfuly Submitted Comment");
								Intent intent = new Intent(ChairmanStudentFeedBackAssign.this,ChairmanStudentFeedBack.class);
								startActivity(intent);
								finish();
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(ChairmanStudentFeedBackAssign.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("feedback_id",Preferences.getInstance().feedbackId);
				params.put("assign_date", crr_date);
				params.put("token", Preferences.getInstance().token);
				params.put("assign_user_id", personId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("target_date", date);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("action", assignFeddback.getText().toString());
				params.put("status", "active");
				params.put("user_name", Preferences.getInstance().userName);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("ins_id",Preferences.getInstance().institutionId);

				/*Log.d("feedback_id",Preferences.getInstance().feedbackId);
				Log.d("assign_date", crr_date);
				Log.d("token", Preferences.getInstance().token);
				Log.d("assign_user_id", personId);
				Log.d("u_id", Preferences.getInstance().userId);
				Log.d("target_date", date);
				Log.d("sch_id", Preferences.getInstance().schoolId);
				Log.d("action", assignFeddback.getText().toString());
				Log.d("status", "active");
				Log.d("user_name", Preferences.getInstance().userName);
				Log.d("device_id", Preferences.getInstance().deviceId);
				Log.d("ins_id",Preferences.getInstance().institutionId);*/
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
				setSupportProgressBarIndeterminateVisibility(false);
			}
	}
}
