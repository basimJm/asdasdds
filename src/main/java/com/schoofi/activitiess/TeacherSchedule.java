package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
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
import com.schoofi.adapters.TeacherListViewTimeTableAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherSchedule extends AppCompatActivity{
	
	String roleId = Preferences.getInstance().userRoleId;
	String token = Preferences.getInstance().token;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String schoolId = Preferences.getInstance().schoolId;
	String userName = Preferences.getInstance().userName;
	String date1;
	private CalendarView calendar;
	private TextView period,subject,newView;
	private ListView teacherListViewTimeTable;
	private TeacherListViewTimeTableAdapter teacherListViewTimeTableAdapter;
	private JSONArray teacherTimeTableArray;
	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	ImageView back,class1;
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_schedule);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Schedule");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		period = (TextView) findViewById(R.id.text_period);
		subject = (TextView) findViewById(R.id.text_subject);
		newView = (TextView) findViewById(R.id.newView);
		newView.setVisibility(View.INVISIBLE);
		teacherListViewTimeTable = (ListView)findViewById(R.id.student_time_table_listView);
		teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
		initializeCalendar();
		back = (ImageView) findViewById(R.id.img_back);
		class1 = (ImageView) findViewById(R.id.img_classschedule);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		class1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(TeacherSchedule.this,AdminClassListScreen.class);
				intent.putExtra("value3","1");
				startActivity(intent);
			}
		});

		Preferences.getInstance().loadPreference(getApplicationContext());
		if(Preferences.getInstance().schoolType.matches("College"))
		{
			try {
				Entry e;
				e = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId);
				if (e == null) {
					teacherTimeTableArray = null;
				} else {
					teacherTimeTableArray = new JSONArray(new String(e.data));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (teacherTimeTableArray != null) {
				teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
				teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
				teacherListViewTimeTableAdapter.notifyDataSetChanged();
			}

			RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue();
			Log.d("url",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId);
			String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId;
			StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					JSONObject responseObject;
					try {
						responseObject = new JSONObject(response);
						//System.out.println(response);

						//toa();
						if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
							teacherListViewTimeTable.setVisibility(View.INVISIBLE);
							newView.setVisibility(View.VISIBLE);
						} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
							Utils.showToast(TeacherSchedule.this, "Session Expired:Please Login Again");
						} else if (responseObject.has("Tech_Schedule")) {
							newView.setVisibility(View.INVISIBLE);
							teacherListViewTimeTable.setVisibility(View.VISIBLE);
							teacherTimeTableArray = new JSONObject(response).getJSONArray("Tech_Schedule");
							if (null != teacherTimeTableArray && teacherTimeTableArray.length() >= 0) {
								Entry e = new Entry();
								e.data = teacherTimeTableArray.toString().getBytes();
								VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId, e);
								teacherListViewTimeTable.invalidateViews();
								teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
								teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
								teacherListViewTimeTableAdapter.notifyDataSetChanged();
							}
						} else {
							Utils.showToast(TeacherSchedule.this, responseObject.getString("errorMessage"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
				}
			}) {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				Preferences.getInstance().loadPreference(context);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("token", Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("date", date);
				return params;
			}*/
			};

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if (Utils.isNetworkAvailable(TeacherSchedule.this))
				queue.add(requestObject);
			else {
				Utils.showToast(TeacherSchedule.this, "Unable to fetch data, kindly enable internet settings!");
			}
		}
		else {
			try {
				Entry e;
				e = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId);
				if (e == null) {
					teacherTimeTableArray = null;
				} else {
					teacherTimeTableArray = new JSONArray(new String(e.data));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (teacherTimeTableArray != null) {
				teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
				teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
				teacherListViewTimeTableAdapter.notifyDataSetChanged();
			}

			RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue();
			String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId;
			StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					JSONObject responseObject;
					try {
						responseObject = new JSONObject(response);
						//System.out.println(response);
						//toa();
						if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
							teacherListViewTimeTable.setVisibility(View.INVISIBLE);
							newView.setVisibility(View.VISIBLE);
						} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
							Utils.showToast(TeacherSchedule.this, "Session Expired:Please Login Again");
						} else if (responseObject.has("Tech_Schedule")) {
							newView.setVisibility(View.INVISIBLE);
							teacherListViewTimeTable.setVisibility(View.VISIBLE);
							teacherTimeTableArray = new JSONObject(response).getJSONArray("Tech_Schedule");
							if (null != teacherTimeTableArray && teacherTimeTableArray.length() >= 0) {
								Entry e = new Entry();
								e.data = teacherTimeTableArray.toString().getBytes();
								VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId, e);
								teacherListViewTimeTable.invalidateViews();
								teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
								teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
								teacherListViewTimeTableAdapter.notifyDataSetChanged();
							}
						} else {
							Utils.showToast(TeacherSchedule.this, responseObject.getString("errorMessage"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
				}
			}) {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				Preferences.getInstance().loadPreference(context);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("token", Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("date", date);
				return params;
			}*/
			};

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if (Utils.isNetworkAvailable(TeacherSchedule.this))
				queue.add(requestObject);
			else {
				Utils.showToast(TeacherSchedule.this, "Unable to fetch data, kindly enable internet settings!");
			}
		}



			teacherListViewTimeTable.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(TeacherSchedule.this,TeacherTimeTableDetails.class);
					/*if(date.equals("null"))
					{
						intent.putExtra("date", date1);
					}
					else
					{
					intent.putExtra("date", date1);
					}*/
					intent.putExtra("date", date1);
					intent.putExtra("position", position);
					startActivity(intent);


				}
			});
		
		
	}
	
	public void initializeCalendar() {
		calendar = (CalendarView) findViewById(R.id.calendar);

		// sets whether to show the week number.
		//calendar.setDate(date);
		//date=null;
		date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		calendar.setShowWeekNumber(false);

		// sets the first day of week according to Calendar.
		// here we set Monday as the first day of the Calendar
		calendar.setFirstDayOfWeek(2);


		//The background color for the selected week.
		calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.transparent));

		//sets the color for the dates of an unfocused month. 
		calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

		//sets the color for the separator line between weeks.
		calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

		//sets the color for the vertical bar shown at the beginning and at the end of the selected date.
		//calendar.setSelectedDateVerticalBar(R.color.darkgreen);
		calendar.setFocusedMonthDateColor(getResources().getColor(R.color.orange));

		//sets the listener to be notified upon selected date change.
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
			//show the selected date as a toast
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
				int y =year;
				int m =month;
				int d= day;

				date1 = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
				//Toast.makeText(getApplicationContext(), "day"+date1, Toast.LENGTH_SHORT).show();
				/*studentTimeTable.onDetach();
				Bundle bundle = new Bundle();
				bundle.putString("date",date);

				StudentTimeTable studentTimeTable = new StudentTimeTable();

				studentTimeTable.setArguments(bundle);*/
				if(Preferences.getInstance().schoolType.matches("College"))
				{
					try {
						Entry e;
						e = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date1 + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId);
						if (e == null) {
							teacherTimeTableArray = null;
						} else {
							teacherTimeTableArray = new JSONArray(new String(e.data));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (teacherTimeTableArray != null) {
						teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
						teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
						teacherListViewTimeTableAdapter.notifyDataSetChanged();
					}

					RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue();
					String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date1 + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId;
					StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							JSONObject responseObject;
							try {
								responseObject = new JSONObject(response);
								//System.out.println(response);
								//toa();
								if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
									teacherListViewTimeTable.setVisibility(View.INVISIBLE);
									newView.setVisibility(View.VISIBLE);
								} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
									Utils.showToast(TeacherSchedule.this, "Session Expired:Please Login Again");
								} else if (responseObject.has("Tech_Schedule")) {
									newView.setVisibility(View.INVISIBLE);
									teacherListViewTimeTable.setVisibility(View.VISIBLE);
									teacherTimeTableArray = new JSONObject(response).getJSONArray("Tech_Schedule");
									if (null != teacherTimeTableArray && teacherTimeTableArray.length() >= 0) {
										Entry e = new Entry();
										e.data = teacherTimeTableArray.toString().getBytes();
										VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COLLEGE_SCHEDULE + "?u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date1 + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId, e);
										teacherListViewTimeTable.invalidateViews();
										teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
										teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
										teacherListViewTimeTableAdapter.notifyDataSetChanged();
									}
								} else {
									Utils.showToast(TeacherSchedule.this, responseObject.getString("errorMessage"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
							}

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
						}
					}) {
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				Preferences.getInstance().loadPreference(context);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("token", Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("date", date);
				return params;
			}*/
					};

					requestObject.setRetryPolicy(new DefaultRetryPolicy(
							25000,
							DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
							DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					if (Utils.isNetworkAvailable(TeacherSchedule.this))
						queue.add(requestObject);
					else {
						Utils.showToast(TeacherSchedule.this, "Unable to fetch data, kindly enable internet settings!");
					}
				}

				else {
					try {
						Entry e;
						e = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date1 + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId);
						if (e == null) {
							teacherTimeTableArray = null;
						} else {
							teacherTimeTableArray = new JSONArray(new String(e.data));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (teacherTimeTableArray != null) {
						teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
						teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
						teacherListViewTimeTableAdapter.notifyDataSetChanged();
					}

					RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue();
					String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date1 + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId;
					StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							JSONObject responseObject;
							try {
								responseObject = new JSONObject(response);
								//System.out.println(response);
								//toa();
								if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
									teacherListViewTimeTable.setVisibility(View.INVISIBLE);
									newView.setVisibility(View.VISIBLE);
								} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
									Utils.showToast(TeacherSchedule.this, "Session Expired:Please Login Again");
								} else if (responseObject.has("Tech_Schedule")) {
									newView.setVisibility(View.INVISIBLE);
									teacherListViewTimeTable.setVisibility(View.VISIBLE);

									teacherTimeTableArray = new JSONObject(response).getJSONArray("Tech_Schedule");
									if (null != teacherTimeTableArray && teacherTimeTableArray.length() >= 0) {
										Entry e = new Entry();
										e.data = teacherTimeTableArray.toString().getBytes();
										VolleySingleton.getInstance(TeacherSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_SCHEDULE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&date=" + date1 + "&u_name=" + userName + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&teac_id=" + Preferences.getInstance().teachId + "&ins_id=" + Preferences.getInstance().institutionId + "&sec_id=" + Preferences.getInstance().studentSectionId, e);
										teacherListViewTimeTable.invalidateViews();
										teacherListViewTimeTableAdapter = new TeacherListViewTimeTableAdapter(TeacherSchedule.this, teacherTimeTableArray);
										teacherListViewTimeTable.setAdapter(teacherListViewTimeTableAdapter);
										teacherListViewTimeTableAdapter.notifyDataSetChanged();
									}
								} else {
									Utils.showToast(TeacherSchedule.this, responseObject.getString("errorMessage"));
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
							}

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Utils.showToast(TeacherSchedule.this, "Error fetching modules! Please try after sometime.");
						}
					}) {
					/*@Override
					protected Map<String,String> getParams(){
						Map<String,String> params = new HashMap<String, String>();
						Preferences.getInstance().loadPreference(context);
						params.put("sec_id",Preferences.getInstance().studentSectionId);
						params.put("stu_id", Preferences.getInstance().studentId);
						params.put("sch_id", Preferences.getInstance().schoolId);
						params.put("token", Preferences.getInstance().token);
						params.put("u_email_id",Preferences.getInstance().userEmailId);
						params.put("u_id", Preferences.getInstance().userId);
						params.put("cls_id", Preferences.getInstance().studentClassId);
						params.put("date", date);
						return params;
					}*/
					};

					requestObject.setRetryPolicy(new DefaultRetryPolicy(
							25000,
							DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
							DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					if (Utils.isNetworkAvailable(TeacherSchedule.this))
						queue.add(requestObject);
					else {
						Utils.showToast(TeacherSchedule.this, "Unable to fetch data, kindly enable internet settings!");
					}
				}


			}

		});
	}


}
