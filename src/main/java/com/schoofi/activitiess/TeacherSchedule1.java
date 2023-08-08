package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.StudentListViewTimeTableAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherSchedule1 extends AppCompatActivity {

    private CalendarView calendar;
    private TextView period,subject,newView;
    private ListView studentListViewTimeTable;
    private StudentListViewTimeTableAdapter studentListViewTimeTableAdapter;
    private JSONArray studentTimeTableArray;
    ImageView back;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String userEmailId = Preferences.getInstance().userEmailId;
    String userId = Preferences.getInstance().userId;
    String token = Preferences.getInstance().token;
    String studentId = Preferences.getInstance().studentId;
    String sectionId;
    String classId;
    String schoolId = Preferences.getInstance().schoolId;
    String date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_schedule);
        period = (TextView) findViewById(R.id.text_period);
        subject = (TextView) findViewById(R.id.text_subject);
        newView = (TextView) findViewById(R.id.newView);
        newView.setVisibility(View.INVISIBLE);
        studentListViewTimeTable = (ListView)findViewById(R.id.student_time_table_listView);
        studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
        back = (ImageView) findViewById(R.id.img_back);

        sectionId = getIntent().getStringExtra("sec_id");
        classId = getIntent().getStringExtra("class_id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initializeCalendar();

        if(Preferences.getInstance().schoolType.matches("College"))
        {
            try {
                Cache.Entry e;

                //Log.d("po",AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId);
                e = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
                if (e == null) {
                    studentTimeTableArray = null;
                } else {
                    studentTimeTableArray = new JSONArray(new String(e.data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (studentTimeTableArray != null) {
                studentListViewTimeTableAdapter = new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                studentListViewTimeTableAdapter.notifyDataSetChanged();
            }

            RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue();
            String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
            StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject responseObject;
                    try {
                        responseObject = new JSONObject(response);
                        //System.out.println(response);
                        //toa();
                        if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                            studentListViewTimeTable.setVisibility(View.INVISIBLE);
                            newView.setVisibility(View.VISIBLE);
                        } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                            Utils.showToast(TeacherSchedule1.this, "Session Expired:Please Login Again");
                        } else if (responseObject.has("Schedule")) {
                            newView.setVisibility(View.INVISIBLE);
                            studentListViewTimeTable.setVisibility(View.VISIBLE);
                            studentTimeTableArray = new JSONObject(response).getJSONArray("Schedule");
                            if (null != studentTimeTableArray && studentTimeTableArray.length() >= 0) {
                                Cache.Entry e = new Cache.Entry();
                                e.data = studentTimeTableArray.toString().getBytes();
                                VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId, e);
                                studentListViewTimeTable.invalidateViews();
                                studentListViewTimeTableAdapter = new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                                studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                                studentListViewTimeTableAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Utils.showToast(TeacherSchedule1.this, responseObject.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
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
            if (Utils.isNetworkAvailable(TeacherSchedule1.this))
                queue.add(requestObject);
            else {
                Utils.showToast(TeacherSchedule1.this, "Unable to fetch data, kindly enable internet settings!");
            }
        }

        else
        {
            try
            {
                Cache.Entry e;
                e = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&stu_id="+studentId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&date="+date+"&device_id="+Preferences.getInstance().deviceId);
                if(e == null)
                {
                    studentTimeTableArray= null;
                }
                else
                {
                    studentTimeTableArray = new JSONArray(new String(e.data));
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            if(studentTimeTableArray!= null)
            {
                studentListViewTimeTableAdapter= new StudentListViewTimeTableAdapter(TeacherSchedule1.this,studentTimeTableArray);
                studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                studentListViewTimeTableAdapter.notifyDataSetChanged();
            }

            RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue();
            String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&stu_id="+studentId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&date="+date+"&device_id="+Preferences.getInstance().deviceId;
            StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject responseObject;
                    try
                    {
                        responseObject = new JSONObject(response);
                        //System.out.println(response);
                        //toa();
                        if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        {
                            studentListViewTimeTable.setVisibility(View.INVISIBLE);
                            newView.setVisibility(View.VISIBLE);
                        }
                        else
                        if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                        {
                            Utils.showToast(TeacherSchedule1.this, "Session Expired:Please Login Again");
                        }
                        else
                        if(responseObject.has("Schedule"))
                        {
                            newView.setVisibility(View.INVISIBLE);
                            studentListViewTimeTable.setVisibility(View.VISIBLE);
                            studentTimeTableArray= new JSONObject(response).getJSONArray("Schedule");
                            if(null!=studentTimeTableArray && studentTimeTableArray.length()>=0)
                            {
                                Cache.Entry e = new Cache.Entry();
                                e.data = studentTimeTableArray.toString().getBytes();
                                VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&stu_id="+studentId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&date="+date+"&device_id="+Preferences.getInstance().deviceId,e);
                                studentListViewTimeTable.invalidateViews();
                                studentListViewTimeTableAdapter= new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                                studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                                studentListViewTimeTableAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Utils.showToast(TeacherSchedule1.this, responseObject.getString("errorMessage"));
                        }
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                        Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
                    }

                }}, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
                }
            })
            {
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
			}*/};

            requestObject.setRetryPolicy(new DefaultRetryPolicy(
                    25000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if(Utils.isNetworkAvailable(TeacherSchedule1.this))
                queue.add(requestObject);
            else
            {
                Utils.showToast(TeacherSchedule1.this, "Unable to fetch data, kindly enable internet settings!");
            }
        }




        studentListViewTimeTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(TeacherSchedule1.this,TeacherScheduleDetails1.class);
					/*if(date.equals("null"))
					{
						intent.putExtra("date", date1);
					}
					else
					{*/
                intent.putExtra("date", date1);
                //}

                intent.putExtra("position", position);
                intent.putExtra("sec_id",sectionId);
                intent.putExtra("cls_id",classId);
                startActivity(intent);


            }
        });


        //fragment();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_schedule, menu);
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

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        //calendar.setDate(date);
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
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
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
                        Cache.Entry e;
                        e = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date1 + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
                        if (e == null) {
                            studentTimeTableArray = null;
                        } else {
                            studentTimeTableArray = new JSONArray(new String(e.data));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (studentTimeTableArray != null) {
                        studentListViewTimeTableAdapter = new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                        studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                        studentListViewTimeTableAdapter.notifyDataSetChanged();
                    }

                    RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue();
                    String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date1 + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
                    StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject responseObject;
                            try {
                                responseObject = new JSONObject(response);
                                //System.out.println(response);
                                //toa();
                                if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                                    studentListViewTimeTable.setVisibility(View.INVISIBLE);
                                    newView.setVisibility(View.VISIBLE);
                                } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                                    Utils.showToast(TeacherSchedule1.this, "Session Expired:Please Login Again");
                                } else if (responseObject.has("Schedule")) {
                                    newView.setVisibility(View.INVISIBLE);
                                    studentListViewTimeTable.setVisibility(View.VISIBLE);

                                    studentTimeTableArray = new JSONObject(response).getJSONArray("Schedule");
                                    if (null != studentTimeTableArray && studentTimeTableArray.length() >= 0) {
                                        Cache.Entry e = new Cache.Entry();
                                        e.data = studentTimeTableArray.toString().getBytes();
                                        VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date1 + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId, e);
                                        studentListViewTimeTable.invalidateViews();
                                        studentListViewTimeTableAdapter = new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                                        studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                                        studentListViewTimeTableAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Utils.showToast(TeacherSchedule1.this, responseObject.getString("errorMessage"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
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
                    if (Utils.isNetworkAvailable(TeacherSchedule1.this))
                        queue.add(requestObject);
                    else {
                        Utils.showToast(TeacherSchedule1.this, "Unable to fetch data, kindly enable internet settings!");
                    }
                }

                else {
                    try {
                        Cache.Entry e;
                        e = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date1 + "&device_id=" + Preferences.getInstance().deviceId);
                        if (e == null) {
                            studentTimeTableArray = null;
                        } else {
                            studentTimeTableArray = new JSONArray(new String(e.data));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (studentTimeTableArray != null) {
                        studentListViewTimeTableAdapter = new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                        studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                        studentListViewTimeTableAdapter.notifyDataSetChanged();
                    }

                    RequestQueue queue = VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue();
                    String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date1 + "&device_id=" + Preferences.getInstance().deviceId;
                    StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject responseObject;
                            try {
                                responseObject = new JSONObject(response);
                                //System.out.println(response);
                                //toa();
                                if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                                    studentListViewTimeTable.setVisibility(View.INVISIBLE);
                                    newView.setVisibility(View.VISIBLE);
                                } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                                    Utils.showToast(TeacherSchedule1.this, "Session Expired:Please Login Again");
                                } else if (responseObject.has("Schedule")) {
                                    newView.setVisibility(View.INVISIBLE);
                                    studentListViewTimeTable.setVisibility(View.VISIBLE);

                                    studentTimeTableArray = new JSONObject(response).getJSONArray("Schedule");
                                    if (null != studentTimeTableArray && studentTimeTableArray.length() >= 0) {
                                        Cache.Entry e = new Cache.Entry();
                                        e.data = studentTimeTableArray.toString().getBytes();
                                        VolleySingleton.getInstance(TeacherSchedule1.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date1 + "&device_id=" + Preferences.getInstance().deviceId, e);
                                        studentListViewTimeTable.invalidateViews();
                                        studentListViewTimeTableAdapter = new StudentListViewTimeTableAdapter(TeacherSchedule1.this, studentTimeTableArray);
                                        studentListViewTimeTable.setAdapter(studentListViewTimeTableAdapter);
                                        studentListViewTimeTableAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Utils.showToast(TeacherSchedule1.this, responseObject.getString("errorMessage"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.showToast(TeacherSchedule1.this, "Error fetching modules! Please try after sometime.");
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
                    if (Utils.isNetworkAvailable(TeacherSchedule1.this))
                        queue.add(requestObject);
                    else {
                        Utils.showToast(TeacherSchedule1.this, "Unable to fetch data, kindly enable internet settings!");
                    }
                }


            }

        });
    }
}
