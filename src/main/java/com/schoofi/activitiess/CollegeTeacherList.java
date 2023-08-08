package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paytm.pgsdk.Log;
import com.schoofi.adapters.CollegeTeacherListAdapter;
import com.schoofi.adapters.TeacherAttendanceDetailsListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class CollegeTeacherList extends AppCompatActivity {

    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    CollegeTeacherListAdapter collegeTeacherListAdapter;
    private JSONArray teacherStudentAttendanceArray1,teacherStudentSubmittedAttendancearray;
    private ListView teacherStudentAttendanceListViewDetails;
    String classId = Preferences.getInstance().studentClassId;
    String sectionId = Preferences.getInstance().studentSectionId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String date1,date2;
    String year1,month1,day1;
    Date date3,date4;
    public boolean c,s;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
    String selectedDate1,selectedDate2;
    Date selectedDate3;
    String dat;
    final Context context = this;
    HorizontalCalendar horizontalCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d3d3d3")));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_college_teacher_list);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);



        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date0, int position) {

                dat = new SimpleDateFormat("yyyy-MM-dd").format(date0);
                selectedDate1 = (dat);
                selectedDate2 = new SimpleDateFormat("dd-MMM-yyyy").format(date0);
                date = dat;

                try {


                } catch (Exception ex) {
                    System.out.println("kgf" + ex);
                }

                setSupportProgressBarIndeterminateVisibility(true);
                RequestQueue queue = VolleySingleton.getInstance(CollegeTeacherList.this).getRequestQueue();
                final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_EMPLOYEE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&date="+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
                StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseObject;
                        //System.out.println(response);
                        //System.out.println(url);
                        try
                        {
                            responseObject = new JSONObject(response);
                            //System.out.println(res);

                            if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                            {

                                Utils.showToast(CollegeTeacherList.this,"No Records Found");
                                teacherStudentAttendanceListViewDetails.setVisibility(View.INVISIBLE);
                            }
                            else
                            if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                            {

                                Utils.showToast(CollegeTeacherList.this, "Session Expired:Please Login Again");
                            }

                            else
                            if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                            {

                                Utils.showToast(CollegeTeacherList.this, "");
                                teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
                            }

                            else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {
                                teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);

                                Utils.showToast(CollegeTeacherList.this, "Today is Holiday! You can't take the attendance");
                            }
                            else
                            if(responseObject.has("emp_list"))

                            {
                                // attendance = new ArrayList<String>();
                                teacherStudentAttendanceArray1= new JSONObject(response).getJSONArray("emp_list");
                                if(null!=teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length()>=0)
                                {
                                    Cache.Entry e = new Cache.Entry();
                                    e.data = teacherStudentAttendanceArray1.toString().getBytes();
                                    VolleySingleton.getInstance(CollegeTeacherList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_EMPLOYEE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&date="+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                                    teacherStudentAttendanceListViewDetails.invalidateViews();
                                    collegeTeacherListAdapter = new CollegeTeacherListAdapter(CollegeTeacherList.this, teacherStudentAttendanceArray1,2);
                                    teacherStudentAttendanceListViewDetails.setAdapter(collegeTeacherListAdapter);
                                    collegeTeacherListAdapter.notifyDataSetChanged();





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
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("id_class_section",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("id_class",Preferences.getInstance().studentClassId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("crr_date",date);
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

        });

        teacherStudentAttendanceListViewDetails = (ListView) findViewById(R.id.listViewTeacherStudentAttendanceDetails);


        //initData();

        getSupportActionBar().setIcon(android.R.color.transparent);

        getTeacherClassList();

        teacherStudentAttendanceListViewDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_student_attendance_details,
                menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("search");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {

                collegeTeacherListAdapter.getFilter().filter(newText);



                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered

                collegeTeacherListAdapter.getFilter().filter(query);



                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.calender) {

            //dateView = (TextView) findViewById(R.id.textView3);

            //showDate(year, month+1, day);

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);

            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            showDialog(999);
            //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
            //.show();

            showDate(year, month, day);








        }


        switch(item.getItemId())
        {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
        //.show();
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
            showDate(arg1, arg2 + 1, arg3);

        }
        //}
    };

    private void showDate(int year, int month, int day) {
        //date1 = day+month+year;
        //System.out.println(date1);
        //System.out.println(year);
        //System.out.println(month);
        //System.out.println(day);

        year1 = Integer.toString(year);
        month1 = Integer.toString(month);
        day1 = Integer.toString(day);

        date = year1+"-"+month1+"-"+day1;

        selectedDate1 = date;

        try {
            selectedDate3 = sdf.parse(selectedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        selectedDate2 = sdf1.format(selectedDate3);

        //selectedDate.setText(selectedDate2);











    }

    protected void getTeacherClassList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_EMPLOYEE_LIST;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                Log.d("response",response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("emp_list"))

                    {

                        teacherStudentAttendanceArray1= new JSONObject(response).getJSONArray("emp_list");
                        if(null!=teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray1.toString().getBytes();
                            VolleySingleton.getInstance(CollegeTeacherList.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHING_EMPLOYEE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&date="+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            teacherStudentAttendanceListViewDetails.invalidateViews();
                            collegeTeacherListAdapter = new CollegeTeacherListAdapter(CollegeTeacherList.this, teacherStudentAttendanceArray1,2);
                            teacherStudentAttendanceListViewDetails.setAdapter(collegeTeacherListAdapter);
                            collegeTeacherListAdapter.notifyDataSetChanged();
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
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
                params.put("sch_id",Preferences.getInstance().schoolId);

                params.put("token",Preferences.getInstance().token);
                params.put("ins_id",Preferences.getInstance().institutionId);
                //params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("date",date);
                params.put("device_id", Preferences.getInstance().deviceId);
                //params.put("crr_date",currentDate);
                return params;
            }};

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
