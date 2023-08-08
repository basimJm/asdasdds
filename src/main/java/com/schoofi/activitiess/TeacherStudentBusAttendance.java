package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.TeacherStudentBusAttendaneAdapter;
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

public class TeacherStudentBusAttendance extends AppCompatActivity {

    private JSONArray teacherStudentAttendanceArray1,teacherStudentSubmittedAttendancearray;
    private ListView teacherStudentAttendanceListViewDetails;
    TeacherStudentBusAttendaneAdapter teacherStudentBusAttendaneAdapter;

    String classId = Preferences.getInstance().studentClassId;
    String sectionId = Preferences.getInstance().studentSectionId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    private PopupWindow calendarPopup;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private TextView dateView;
    String date1,date2;
    String year1,month1,day1;
    private Button submit;
    Date date3,date4;
    public boolean c,s;
    JSONObject jsonObject1,jo;
    int i;
    String array1,array2,array3,array4,array5,array6,array7,array8;
    public ArrayList<String> attendance = new ArrayList<String>();
    public ArrayList<String> attendanceName = new ArrayList<String>();
    public ArrayList<String> attendanceClass = new ArrayList<String>();
    public ArrayList<String> attendanceSection = new ArrayList<String>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
    String selectedDate1,selectedDate2;
    Date selectedDate3;
    final Context context = this;
    TextView noOfStudents,selectedDate;
    String busRoute,shift,bus_no;
    HorizontalCalendar horizontalCalendar;
    private Button save;
    String dat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d3d3d3")));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setContentView(R.layout.activity_teacher_student_bus_attendance);

        Preferences.getInstance().loadPreference(TeacherStudentBusAttendance.this);

        noOfStudents = (TextView) findViewById(R.id.text_no_of_students);
        selectedDate = (TextView) findViewById(R.id.attendance_date);

        busRoute = getIntent().getStringExtra("bus_route");
        //shift = getIntent().getStringExtra("shift");
        bus_no = getIntent().getStringExtra("bus_no");




        submit = (Button) findViewById(R.id.btn_submitAttendanceList);
        save = (Button) findViewById(R.id.btn_submitAttendanceList1);
        save.setText("Save");
        submit.setText("Submit");

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


        teacherStudentAttendanceListViewDetails = (ListView) findViewById(R.id.listViewTeacherStudentAttendanceDetails);

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date0, int position) {
                dat = new SimpleDateFormat("yyyy-MM-dd").format(date0);
                selectedDate1 = (dat);
                selectedDate2 = new SimpleDateFormat("dd-MMM-yyyy").format(date0);
                date = dat;

                try{






                    selectedDate.setText(selectedDate2);
                }catch (Exception ex ){
                    System.out.println("kgf"+ex);
                }

                RequestQueue queue = VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue();
                final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE;
                StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseObject;
                       // System.out.println(response);
                        //System.out.println(url);
                        try {
                            responseObject = new JSONObject(response);
                            //System.out.println(res);
                            toa();
                            if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {

                                Utils.showToast(TeacherStudentBusAttendance.this, "No Records Found");
                            } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {

                                Utils.showToast(TeacherStudentBusAttendance.this, "Session Expired:Please Login Again");
                            } else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("1")) {

									/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherStudentAttendanceDetails.this);

									// Setting Dialog Title
									alertDialog.setTitle("Attendance already submitted");

									// Setting Dialog Message
									alertDialog.setMessage("Want to see it");

									// Setting Icon to Dialog
									//alertDialog.setIcon(R.drawable.calander_icon);

									// Setting Positive "Yes" Button
									alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int which) {*/

                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                initData4();
                                getTeacherStudentList2();
                                s = true;
                                submit.setEnabled(false);
                                submit.setBackgroundResource(R.drawable.button_red_50_percent);
                                save.setBackgroundResource(R.drawable.button_green_50_percent);
                                save.setClickable(false);
										/*}
									});

									// Setting Negative "NO" Button
									alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											// Write your code here to invoke NO event
											//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
											dialog.cancel();
										}
									});

									// Showing Alert Message
									alertDialog.show();*/
                            } else if (responseObject.has("Students"))

                            {
                                s = false;
                                submit.setEnabled(true);
                                submit.setBackgroundResource(R.drawable.buttons);
                                attendance = new ArrayList<String>();

                                teacherStudentAttendanceArray1 = new JSONObject(response).getJSONArray("Students");
                                int visi=0;

                                for (i = 0; i < teacherStudentAttendanceArray1.length(); i++) {
                                    teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent", teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_id") + "-P");
                                    teacherStudentAttendanceArray1.getJSONObject(i).put("crr_date", date);
                                    teacherStudentAttendanceArray1.getJSONObject(i).put("u_email_id", Preferences.getInstance().userEmailId);
                                    teacherStudentAttendanceArray1.getJSONObject(i).put("position","0");
                                    teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent1","NA");
                                    teacherStudentAttendanceArray1.getJSONObject(i).put("isRecorded","NA");
                                    //teacherStudentAttendanceArray1.getJSONObject(i).put("count", i);
                                }

                                for(int o=0;o<teacherStudentAttendanceArray1.length();o++)
                                {
                                    if(teacherStudentAttendanceArray1.getJSONObject(o).getString("attendance").matches("0"))
                                    {
                                        visi++;
                                        Log.d("kkiu",teacherStudentAttendanceArray1.getJSONObject(o).getString("attendance"));
                                    }
                                }

                                if(visi == 0)
                                {
                                    save.setClickable(false);
                                    submit.setClickable(false);
                                }

                                else
                                {
                                    save.setClickable(true);
                                    submit.setClickable(true);
                                }

                                noOfStudents.setText("Total Students: "+ String.valueOf(i));



                                //System.out.println(i);

                                for (int j = 0; j < teacherStudentAttendanceArray1.length(); j++) {
                                    teacherStudentAttendanceArray1.getJSONObject(j).put("count", i);
                                    //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
                                }
                                if (null != teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length() >= 0) {
                                    Cache.Entry e = new Cache.Entry();
                                    e.data = teacherStudentAttendanceArray1.toString().getBytes();
                                    VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE + "?token=" + token + "&bus_number=" + bus_no + "&route=" + busRoute + "&sch_id=" + schoolId + "&date="/*+"2003-11-21"*/ + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId, e);
                                    teacherStudentAttendanceListViewDetails.invalidateViews();
                                    teacherStudentBusAttendaneAdapter = new TeacherStudentBusAttendaneAdapter(TeacherStudentBusAttendance.this, teacherStudentAttendanceArray1, 1);
                                    teacherStudentAttendanceListViewDetails.setAdapter(teacherStudentBusAttendaneAdapter);
                                    teacherStudentBusAttendaneAdapter.notifyDataSetChanged();
											/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
										Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
										Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                                }
                            } else
                                Utils.showToast(TeacherStudentBusAttendance.this, "Error Fetching Response");
                            setSupportProgressBarIndeterminateVisibility(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.showToast(TeacherStudentBusAttendance.this, "Error fetching modules! Please try after sometime.");
                            setSupportProgressBarIndeterminateVisibility(false);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                        setSupportProgressBarIndeterminateVisibility(false);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sch_id", Preferences.getInstance().schoolId);
                        // params.put("id_class_section", Preferences.getInstance().studentSectionId);
                        params.put("token", Preferences.getInstance().token);
                        params.put("route", busRoute);
                        params.put("bus_number", bus_no);
                        //params.put("u_id", Preferences.getInstance().userId);
                        params.put("date", date);
                        params.put("device_id", Preferences.getInstance().deviceId);
                        params.put("ins_id",Preferences.getInstance().institutionId);
                        return params;
                    }
                };

                requestObject.setRetryPolicy(new DefaultRetryPolicy(
                        25000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                if (Utils.isNetworkAvailable(TeacherStudentBusAttendance.this))
                    queue.add(requestObject);
                else {
                    Utils.showToast(TeacherStudentBusAttendance.this, "Unable to fetch data, kindly enable internet settings!");
                }
            }
        });


        //initData();
        getTeacherStudentList();
        getSupportActionBar().setIcon(android.R.color.transparent);

        teacherStudentAttendanceListViewDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                teacherStudentBusAttendaneAdapter.setSelectedIndex(position);

                try {
                    if(teacherStudentAttendanceArray1.getJSONObject(position).getString("status").matches("1"))
                    {
                        teacherStudentAttendanceListViewDetails.setEnabled(false);

                    }

                    else
                    {
                        teacherStudentAttendanceListViewDetails.setEnabled(true);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.teacher_submit_attendance_dialog_new_present, null);



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);




                final TextView present = (TextView) promptsView.findViewById(R.id.text_total_present);

                final TextView total =   (TextView) promptsView.findViewById(R.id.text_total);

                int present1=0,total1=0;

                for(int k =0;k<i;k++)
                {
                    try {
                        if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P1") || teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P2"))
                        {
                            present1 = present1+1;
                            attendance.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_id"));
                            attendanceName.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));
                            attendanceClass.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("class_id"));
                            attendanceSection.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("section_id"));
                        }

                        else
                        {

                        }






                        total1=k;


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                present.setText("Total Present: "+ String.valueOf(present1));

                total.setText("Total Students: "+ String.valueOf(total1+1));



                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // get user input and set it to result


                                        // edit text


                                        array1 = attendance.toString();
                                        array2 = array1.substring(1, array1.length()-1);
                                        array3 = attendanceName.toString();
                                        array4 = array3.substring(1,array3.length()-1);
                                        array5 = attendanceClass.toString();
                                        array6 = array5.substring(1,array5.length()-1);
                                        array7 = attendanceSection.toString();
                                        array8 = array7.substring(1,array7.length()-1);


                                        postAttendance();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        attendance.clear();
                                        attendanceName.clear();
                                        attendanceClass.clear();
                                        attendanceSection.clear();
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub





                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.teacher_attendance_submit_dialog_box, null);



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);




                final TextView present = (TextView) promptsView.findViewById(R.id.text_total_present);
                final TextView absent =  (TextView) promptsView.findViewById(R.id.text_total_absent);
                final TextView leave =   (TextView) promptsView.findViewById(R.id.text_total_leave);
                final TextView total =   (TextView) promptsView.findViewById(R.id.text_total);

                int present1=0,absent1=0,leave1=0,total1=0;

                for(int k =0;k<i;k++)
                {
                    try {
                        if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("attendance").matches("P") || teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P1") || teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P2"))
                        {
                            present1 = present1+1;
                            Log.d("kk13",teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));

                        }

                        else
                        {

                        }

                        if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("NA"))
                        {
                            absent1 = absent1+1;
                            Log.d("kk14",teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));
                        }

                        else
                        {

                        }

                        if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("L"))
                        {
                            leave1 = leave1+1;
                        }

                        else
                        {

                        }

                        if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("attendance").matches("P") || teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("attendance").matches("A") ||  teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("attendance").matches("L"))
                        {
                            attendance.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_id")+"-R");
                            attendanceName.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));
                            attendanceClass.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("class_id"));
                            attendanceSection.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("section_id"));
                        }

                        else
                            if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P1") || teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P2"))
                        {
                            attendance.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_id")+"-P");
                            attendanceName.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));
                            attendanceClass.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("class_id"));
                            attendanceSection.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("section_id"));

                        }

                            else
                            if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("L"))
                            {
                                attendance.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_id")+"-L");
                                attendanceName.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));
                                attendanceClass.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("class_id"));
                                attendanceSection.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("section_id"));

                            }

                            else
                            if(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("NA"))
                            {
                                attendance.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_id")+"-A");
                                attendanceName.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));
                                attendanceClass.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("class_id"));
                                attendanceSection.add(teacherStudentBusAttendaneAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("section_id"));

                            }



                        total1=k;


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                present.setText("Total Present: "+ String.valueOf(present1));
                absent.setText("Total Absent: "+ String.valueOf(absent1));
                leave.setText("Total Leave: "+ String.valueOf(leave1));
                total.setText("Total Students: "+ String.valueOf(total1+1));



                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // get user input and set it to result


                                        // edit text


                                        array1 = attendance.toString();
                                        array2 = array1.substring(1, array1.length()-1);
                                        array3 = attendanceName.toString();
                                        array4 = array3.substring(1,array3.length()-1);
                                        array5 = attendanceClass.toString();
                                        array6 = array5.substring(1,array5.length()-1);
                                        array7 = attendanceSection.toString();
                                        array8 = array7.substring(1,array7.length()-1);


                                        postAttendance1();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        attendance.clear();
                                        attendanceName.clear();
                                        attendanceClass.clear();
                                        attendanceSection.clear();
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                //System.out.println(array2);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getTeacherStudentList();
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

                teacherStudentBusAttendaneAdapter.getFilter().filter(newText);



                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered

                teacherStudentBusAttendaneAdapter.getFilter().filter(query);



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
			/*try {
				date3 = sdf.parse(date);
				date4 = sdf.parse(date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (date4.after(date3)) {
				Utils.showToast(getApplicationContext(), "Future attendance cannot be recorded");
			}
			else {*/
            RequestQueue queue = VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue();
            final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE;
            StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject responseObject;
                   // System.out.println(response);
                    //System.out.println(url);
                    try {
                        responseObject = new JSONObject(response);
                        //System.out.println(res);
                        toa();
                        if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {

                            Utils.showToast(TeacherStudentBusAttendance.this, "No Records Found");
                        } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {

                            Utils.showToast(TeacherStudentBusAttendance.this, "Session Expired:Please Login Again");
                        } else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("1")) {

									/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherStudentAttendanceDetails.this);

									// Setting Dialog Title
									alertDialog.setTitle("Attendance already submitted");

									// Setting Dialog Message
									alertDialog.setMessage("Want to see it");

									// Setting Icon to Dialog
									//alertDialog.setIcon(R.drawable.calander_icon);

									// Setting Positive "Yes" Button
									alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int which) {*/

                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            initData4();
                            getTeacherStudentList2();
                            s = true;
                            submit.setEnabled(false);
                            submit.setBackgroundResource(R.drawable.button_red_50_percent);
                            save.setBackgroundResource(R.drawable.button_green_50_percent);
                            save.setClickable(false);
										/*}
									});

									// Setting Negative "NO" Button
									alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											// Write your code here to invoke NO event
											//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
											dialog.cancel();
										}
									});

									// Showing Alert Message
									alertDialog.show();*/
                        } else if (responseObject.has("Students"))

                        {
                            s = false;
                            submit.setEnabled(true);
                            submit.setBackgroundResource(R.drawable.buttons);
                            attendance = new ArrayList<String>();

                            teacherStudentAttendanceArray1 = new JSONObject(response).getJSONArray("Students");

                            int visi=0;

                            for (i = 0; i < teacherStudentAttendanceArray1.length(); i++) {
                                teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent", teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_id") + "-P");
                                teacherStudentAttendanceArray1.getJSONObject(i).put("crr_date", date);
                                teacherStudentAttendanceArray1.getJSONObject(i).put("u_email_id", Preferences.getInstance().userEmailId);
                                teacherStudentAttendanceArray1.getJSONObject(i).put("position","0");
                                teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent1","NA");
                                teacherStudentAttendanceArray1.getJSONObject(i).put("isRecorded","NA");
                                //teacherStudentAttendanceArray1.getJSONObject(i).put("count", i);
                            }

                            for(int o=0;o<teacherStudentAttendanceArray1.length();o++)
                            {
                                if(teacherStudentAttendanceArray1.getJSONObject(o).getString("attendance").matches("0"))
                                {
                                    visi++;
                                    Log.d("kkiu",teacherStudentAttendanceArray1.getJSONObject(o).getString("attendance"));
                                }
                            }

                            if(visi == 0)
                            {
                                submit.setEnabled(false);
                                submit.setBackgroundResource(R.drawable.button_red_50_percent);
                                save.setBackgroundResource(R.drawable.button_green_50_percent);
                                save.setClickable(false);

                            }

                            else
                            {
                                save.setClickable(true);
                                submit.setClickable(true);
                            }

                            noOfStudents.setText("Total Students: "+ String.valueOf(i));

                            //System.out.println(i);

                            for (int j = 0; j < teacherStudentAttendanceArray1.length(); j++) {
                                teacherStudentAttendanceArray1.getJSONObject(j).put("count", i);
                                //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
                            }
                            if (null != teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length() >= 0) {
                                Cache.Entry e = new Cache.Entry();
                                e.data = teacherStudentAttendanceArray1.toString().getBytes();
                                VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE + "?token=" + token + "&bus_number=" + bus_no + "&route=" + busRoute + "&sch_id=" + schoolId + "&date="/*+"2003-11-21"*/ + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId, e);
                                teacherStudentAttendanceListViewDetails.invalidateViews();
                                teacherStudentBusAttendaneAdapter = new TeacherStudentBusAttendaneAdapter(TeacherStudentBusAttendance.this, teacherStudentAttendanceArray1, 1);
                                teacherStudentAttendanceListViewDetails.setAdapter(teacherStudentBusAttendaneAdapter);
                                teacherStudentBusAttendaneAdapter.notifyDataSetChanged();
											/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
										Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
										Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                            }
                        } else
                            Utils.showToast(TeacherStudentBusAttendance.this, "Error Fetching Response");
                        setSupportProgressBarIndeterminateVisibility(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.showToast(TeacherStudentBusAttendance.this, "Error fetching modules! Please try after sometime.");
                        setSupportProgressBarIndeterminateVisibility(false);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setSupportProgressBarIndeterminateVisibility(false);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sch_id", Preferences.getInstance().schoolId);
                   // params.put("id_class_section", Preferences.getInstance().studentSectionId);
                    params.put("token", Preferences.getInstance().token);
                    params.put("route", busRoute);
                    params.put("bus_number", bus_no);
                    //params.put("u_id", Preferences.getInstance().userId);
                    params.put("date", date);
                    params.put("device_id", Preferences.getInstance().deviceId);
                    params.put("ins_id",Preferences.getInstance().institutionId);
                    return params;
                }
            };

            requestObject.setRetryPolicy(new DefaultRetryPolicy(
                    25000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (Utils.isNetworkAvailable(TeacherStudentBusAttendance.this))
                queue.add(requestObject);
            else {
                Utils.showToast(TeacherStudentBusAttendance.this, "Unable to fetch data, kindly enable internet settings!");
            }
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

        selectedDate.setText(selectedDate2);


        //System.out.println(date);

		/*try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="+"2003-11-21"+date1);
			if(e == null)
			{
				teacherStudentAttendanceArray1= null;
			}
			else
			{
				teacherStudentAttendanceArray1= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentAttendanceArray1!= null)
		{
			teacherAttendanceDetailsListViewAdapter= new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this,teacherStudentAttendanceArray1);
			teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
			teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();
		}*/








    }

    protected void getTeacherStudentList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(TeacherStudentBusAttendance.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(TeacherStudentBusAttendance.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

								/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherStudentAttendanceDetails.this);

								// Setting Dialog Title
								alertDialog.setTitle("Attendance already submitted");

								// Setting Dialog Message
								alertDialog.setMessage("Want to see it");

								// Setting Icon to Dialog
								//alertDialog.setIcon(R.drawable.calander_icon);

								// Setting Positive "Yes" Button
								alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {*/

                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
										/*Intent intent = new Intent(TeacherStudentAttendanceDetails.this,TeacherStudentSubmittedAtendanceList.class);
										intent.putExtra("date", date);
										startActivity(intent);
										finish();*/

                        initData4();
                        getTeacherStudentList2();
                        s=true;
                        submit.setEnabled(false);
                        submit.setBackgroundResource(R.drawable.button_red_50_percent);
                        save.setBackgroundResource(R.drawable.button_green_50_percent);
                        save.setClickable(false);
								/*	}
								});

								// Setting Negative "NO" Button
								alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to invoke NO event
										//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
										dialog.cancel();
									}
								});

								// Showing Alert Message
								alertDialog.show();*/
                    }
                    else
                    if(responseObject.has("Students"))

                    {
                        s=false;
                        submit.setEnabled(true);
                        attendance = new ArrayList<String>();
                        submit.setBackgroundResource(R.drawable.buttons);

                        teacherStudentAttendanceArray1= new JSONObject(response).getJSONArray("Students");
                        //teacherStudentAttendanceArray1.getJSONObject(0).put("isPresent", "P");
                        //teacherStudentAttendanceArray1.getJSONArray(0).getJSONObject(0).put("isPresent", "P");
                        int visi=0;

                        for (i = 0; i < teacherStudentAttendanceArray1.length(); i++) {
                            teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent", teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_id") + "-P");
                            teacherStudentAttendanceArray1.getJSONObject(i).put("crr_date", date);
                            teacherStudentAttendanceArray1.getJSONObject(i).put("u_email_id", Preferences.getInstance().userEmailId);
                            teacherStudentAttendanceArray1.getJSONObject(i).put("position","0");
                            teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent1","NA");
                            teacherStudentAttendanceArray1.getJSONObject(i).put("isRecorded","NA");
                            //teacherStudentAttendanceArray1.getJSONObject(i).put("count", i);
                        }

                        for(int o=0;o<teacherStudentAttendanceArray1.length();o++)
                        {
                            if(teacherStudentAttendanceArray1.getJSONObject(o).getString("attendance").matches("0"))
                            {
                                visi++;
                                Log.d("kkiu",teacherStudentAttendanceArray1.getJSONObject(o).getString("attendance"));
                            }
                        }

                        if(visi == 0)
                        {
                            submit.setEnabled(false);
                            submit.setBackgroundResource(R.drawable.button_red_50_percent);
                            save.setBackgroundResource(R.drawable.button_green_50_percent);
                            save.setClickable(false);
                        }

                        else
                        {
                            save.setClickable(true);
                            submit.setClickable(true);
                        }

                        //System.out.println(i);
                        noOfStudents.setText("Total Students: "+ String.valueOf(i));
                        selectedDate1 = date;

                        try {
                            selectedDate3 = sdf.parse(selectedDate1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        selectedDate2 = sdf1.format(selectedDate3);

                        selectedDate.setText(selectedDate2);

                        for(int j= 0;j<teacherStudentAttendanceArray1.length();j++)
                        {
                            teacherStudentAttendanceArray1.getJSONObject(j).put("count", i);
                            //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
                        }
                        if(null!=teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray1.toString().getBytes();
                            VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE + "?token=" + token + "&bus_number=" + bus_no + "&route=" + busRoute + "&sch_id=" + schoolId + "&date="/*+"2003-11-21"*/ + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            teacherStudentAttendanceListViewDetails.invalidateViews();
                            teacherStudentBusAttendaneAdapter = new TeacherStudentBusAttendaneAdapter(TeacherStudentBusAttendance.this, teacherStudentAttendanceArray1,1);
                            teacherStudentAttendanceListViewDetails.setAdapter(teacherStudentBusAttendaneAdapter);
                            teacherStudentBusAttendaneAdapter.notifyDataSetChanged();
										/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
                        }
                    }
                    else
                        Utils.showToast(TeacherStudentBusAttendance.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(TeacherStudentBusAttendance.this, "Error fetching modules! Please try after sometime.");
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
               // params.put("id_class_section",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("route",busRoute);
                params.put("bus_number",bus_no);
                //params.put("u_id",Preferences.getInstance().userId);
                params.put("date",date);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(TeacherStudentBusAttendance.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(TeacherStudentBusAttendance.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_ATTENDANCE_SUBMIT/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(TeacherStudentBusAttendance.this,"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(TeacherStudentBusAttendance.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(TeacherStudentBusAttendance.this,"Successfuly Submitted Attendance");
                        Intent intent = new Intent(TeacherStudentBusAttendance.this,TeacherStudentBusAttendance.class);
                        intent.putExtra("bus_route",busRoute);
                        intent.putExtra("bus_no",bus_no);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);

                        //getTeacherStudentList2();
                       // submit.setEnabled(false);
                        //submit.setBackgroundResource(R.drawable.button_red_50_percent);
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(TeacherStudentBusAttendance.this, "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(TeacherStudentBusAttendance.this, "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(TeacherStudentBusAttendance.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());
                params.put("students", array2);
                params.put("teach_email",Preferences.getInstance().userEmailId);
                Log.d("kko",array2.toString());
                //params.put("count", String.valueOf(i));
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("cls_id", array6);
                params.put("sec_id", array8);
                params.put("stu_name",array4);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("user_id",Preferences.getInstance().userId);
                //params.put("tea_id", Preferences.getInstance().userId);
                params.put("attendance_date",date);
                //params.put("shift",shift);
                params.put("bus_route_no",busRoute);
                params.put("bus_no",bus_no);
                //params.put("Students", "harsh");
                //params.put("u_email_id", Preferences.getInstance().userEmailId);
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
            loading.dismiss();
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }


    protected void postAttendance1()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.FINAL_SUBMISSION_ATTENDANCE/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.getJSONArray("responseObject").getJSONObject(0).getString("Msg").matches("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(TeacherStudentBusAttendance.this,"Error Submitting Attendance");

                    }
                    else
                    if(responseObject.getJSONArray("responseObject").getJSONObject(0).getString("Msg").matches("2"))
                    {
                        loading.dismiss();
                        Utils.showToast(TeacherStudentBusAttendance.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.getJSONArray("responseObject").getJSONObject(0).getString("Msg").matches("1"))
                    {
                        loading.dismiss();
                        submit.setBackgroundResource(R.drawable.button_red_50_percent);
                        save.setBackgroundResource(R.drawable.button_green_50_percent);
                        submit.setClickable(false);
                        save.setClickable(false);
                        Utils.showToast(TeacherStudentBusAttendance.this,"Successfuly Submitted Attendance");
                        Intent intent = new Intent(TeacherStudentBusAttendance.this,TeacherStudentBusAttendance.class);
                        intent.putExtra("bus_route",busRoute);
                        intent.putExtra("bus_no",bus_no);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);

                        //getTeacherStudentList2();
                        // submit.setEnabled(false);
                        //submit.setBackgroundResource(R.drawable.button_red_50_percent);
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(TeacherStudentBusAttendance.this, "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(TeacherStudentBusAttendance.this, "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(TeacherStudentBusAttendance.this);
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());
                params.put("students", array2);
                params.put("teach_email",Preferences.getInstance().userEmailId);
                Log.d("kko",array2.toString());
                //params.put("count", String.valueOf(i));
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("token",Preferences.getInstance().token);
                params.put("cls_id", array6);
                params.put("sec_id", array8);
                params.put("stu_name",array4);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("user_id",Preferences.getInstance().userId);
                //params.put("tea_id", Preferences.getInstance().userId);
                params.put("attendance_date",date);
                //params.put("shift",shift);
                params.put("bus_route_no",busRoute);
                params.put("bus_no",bus_no);
                //params.put("Students", "harsh");
                //params.put("u_email_id", Preferences.getInstance().userEmailId);
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
            loading.dismiss();
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            //setSupportProgressBarIndeterminateVisibility(false);
        }



    }

    private void initData4()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_SUNMITTED_ATTENDANCE+"?token="+token+"&bus_number="+bus_no+"&route="+busRoute+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                teacherStudentAttendanceArray1= null;
            }
            else
            {
                teacherStudentAttendanceArray1= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherStudentAttendanceArray1!= null)
        {
            teacherStudentBusAttendaneAdapter= new TeacherStudentBusAttendaneAdapter(TeacherStudentBusAttendance.this,teacherStudentAttendanceArray1,2);
            teacherStudentAttendanceListViewDetails.setAdapter(teacherStudentBusAttendaneAdapter);
            teacherStudentBusAttendaneAdapter.notifyDataSetChanged();
        }
    }

    protected void getTeacherStudentList2()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_SUNMITTED_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    //System.out.println(res);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(TeacherStudentBusAttendance.this,"No Records Found");
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(TeacherStudentBusAttendance.this, "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(TeacherStudentBusAttendance.this, "");
                    }
                    else
                    if(responseObject.has("Students"))

                    {
                        submit.setBackgroundResource(R.drawable.button_red_50_percent);
                        save.setBackgroundResource(R.drawable.button_green_50_percent);
                        submit.setClickable(false);
                        save.setClickable(false);
                        attendance = new ArrayList<String>();
                        teacherStudentAttendanceArray1= new JSONObject(response).getJSONArray("Students");
                        if(null!=teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherStudentAttendanceArray1.toString().getBytes();
                            VolleySingleton.getInstance(TeacherStudentBusAttendance.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_BUS_SUNMITTED_ATTENDANCE+"?token="+token+"&bus_number="+bus_no+"&route="+busRoute+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            teacherStudentAttendanceListViewDetails.invalidateViews();
                            teacherStudentBusAttendaneAdapter = new TeacherStudentBusAttendaneAdapter(TeacherStudentBusAttendance.this, teacherStudentAttendanceArray1,2);
                            teacherStudentAttendanceListViewDetails.setAdapter(teacherStudentBusAttendaneAdapter);
                            teacherStudentBusAttendaneAdapter.notifyDataSetChanged();

                            int presents=0,absents=0,leaves=0;
                            for(int g=0;g<teacherStudentAttendanceArray1.length();g++)
                            {
                                if(teacherStudentAttendanceArray1.getJSONObject(g).getString("attendance").toString().matches("P"))
                                {
                                    presents= presents+1;
                                }

                                else
                                if(teacherStudentAttendanceArray1.getJSONObject(g).getString("attendance").toString().matches("A"))
                                {
                                    absents = absents+1;
                                }

                                else
                                if(teacherStudentAttendanceArray1.getJSONObject(g).getString("attendance").toString().matches("L"))
                                {
                                    leaves = leaves+1;
                                }

                                selectedDate1 = date;

                                try {
                                    selectedDate3 = sdf.parse(selectedDate1);
                                } catch (ParseException ew) {
                                    ew.printStackTrace();
                                }

                                selectedDate2 = sdf1.format(selectedDate3);

                                selectedDate.setText(selectedDate2);

                                noOfStudents.setText("Total:"+ String.valueOf(g+1)+" Present:"+ String.valueOf(presents)+" Absent:"+ String.valueOf(absents)+" Leaves:"+ String.valueOf(leaves));


                            }

                            //noOfStudents.setText();
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
				params.put("token",Preferences.getInstance().token);
                params.put("bus_number",bus_no);
                params.put("route",busRoute);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("crr_date",date);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
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
}
