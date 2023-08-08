package com.schoofi.activitiess;

import android.app.ProgressDialog;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.ChairmanAttendanceListListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChairmanAttendenceDetails extends AppCompatActivity {

    private TextView date,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12;
    ImageView back;
    private JSONArray chairmanStudentDailyAttendanceArray;
    String from1,to1,crrDate,temp,fromDate,toDate;
    int position,value;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String date1,date2;
    Date date3;
    String classId,sectionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Attendence Details Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_attendence_details);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        date = (TextView) findViewById(R.id.text_date);
        text1 = (TextView) findViewById(R.id.text_1);
        text2 = (TextView) findViewById(R.id.text_2);
        text3 = (TextView) findViewById(R.id.text_3);
        text4 = (TextView) findViewById(R.id.text_4);
        text5 = (TextView) findViewById(R.id.text_5);
        text6 = (TextView) findViewById(R.id.text_6);
        text7 = (TextView) findViewById(R.id.text_7);
        text8 = (TextView) findViewById(R.id.text_8);
        text9 = (TextView) findViewById(R.id.text_9);
        text10 = (TextView) findViewById(R.id.text_10);
        text11 = (TextView) findViewById(R.id.text_11);
        text12= (TextView) findViewById(R.id.text_12);

        position = getIntent().getExtras().getInt("position");
        value = getIntent().getExtras().getInt("value");
        from1 = getIntent().getStringExtra("from_date");
        to1 = getIntent().getStringExtra("to_date");
        temp = getIntent().getStringExtra("temp");

        text1.setText("Total Recorded Attendance");
        text3.setText("Total Present");
        text5.setText("Boys Present");
        text7.setText("Girls Present");
        text9.setText("Total Absent");
        text11.setText("Total Leave");

        if(temp.matches("1"))
        {
            if(value==1)
            {
                initData();
            }

            else
                if(value==2)
                {
                    classId = getIntent().getStringExtra("cls_id");
                    initData1();
                }

            else
                {
                    classId = getIntent().getStringExtra("cls_id");
                    sectionId = getIntent().getStringExtra("sec_id");
                    initData2();
                }
        }

        else
            if(temp.matches("2"))
            {
                if(value==1)
                {
                    initData3();
                }

                else
                    if(value==2)
                    {
                        classId = getIntent().getStringExtra("cls_id");
                        //sectionId = getIntent().getStringExtra("sec_id");
                        initData4();
                    }

                else
                    {
                        classId = getIntent().getStringExtra("cls_id");
                        sectionId = getIntent().getStringExtra("sec_id");
                        initData5();
                    }
            }

        else
                if(temp.matches("3"))
                {
                    if(value==1)
                    {
                        initData6();
                    }

                    else
                    if(value==2)
                    {
                        classId = getIntent().getStringExtra("cls_id");
                        //sectionId = getIntent().getStringExtra("sec_id");
                        initData7();
                    }

                    else
                    {
                        classId = getIntent().getStringExtra("cls_id");
                        sectionId = getIntent().getStringExtra("sec_id");
                        initData8();
                    }
                }

        else
                    if(temp.matches("4"))
                    {
                        classId = getIntent().getStringExtra("cls_id");
                        sectionId = getIntent().getStringExtra("sec_id");
                        getStudentAnnouncementList2();
                    }





    }

    private void initData() {

        crrDate = from1;
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + crrDate + "&to_date=" + crrDate + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData1() {

        crrDate = from1;
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + crrDate + "&to_date=" + crrDate + "&device_id=" + Preferences.getInstance().deviceId+"&cls_id="+classId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData2() {

        crrDate = from1;
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + crrDate + "&to_date=" + crrDate + "&device_id=" + Preferences.getInstance().deviceId+"&cls_id="+classId+"&sec_id="+sectionId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData3() {

        fromDate = from1;
        toDate = to1;
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + fromDate + "&to_date=" + toDate + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData4() {

        fromDate = from1;
        toDate = to1;
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + fromDate + "&to_date=" + toDate + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData5() {

        fromDate = from1;
        toDate = to1;
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + fromDate + "&to_date=" + toDate + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData6() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + from1 + "&to_date=" + to1 + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData7() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_CLASS_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + from1 + "&to_date=" + to1 + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData8() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(ChairmanAttendenceDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + from1 + "&to_date=" + to1 + "&device_id=" + Preferences.getInstance().deviceId);
            chairmanStudentDailyAttendanceArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanStudentDailyAttendanceArray != null) {
            try {
                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("date");
                try {
                    date3 = formatter.parse(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);

                date.setText(date2+"-"+"("+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p")+"/"+chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded")+")");
                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_recorded"));
                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_p"));
                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("male_p"));
                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("female_p"));
                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_a"));
                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(position).getString("total_l"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    protected void getStudentAnnouncementList2()
    {


        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final ProgressDialog loading = ProgressDialog.show(ChairmanAttendenceDetails.this, "Loading...", "Please wait...", false, false);
        final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);

                try {
                    responseObject = new JSONObject(response);

                    //toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        //attendanceListView.setVisibility(View.INVISIBLE);
                        loading.dismiss();
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        // attendanceListView.setVisibility(View.INVISIBLE);
                        loading.dismiss();
                    } else if (responseObject.has("Attendance")) {
                        chairmanStudentDailyAttendanceArray = new JSONObject(response).getJSONArray("Attendance");
                        if (null != chairmanStudentDailyAttendanceArray && chairmanStudentDailyAttendanceArray.length() >= 0) {
                            com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
                            e.data = chairmanStudentDailyAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_DAILY_ATTENDANCE_SECTION_WISE + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&inst_id=" + Preferences.getInstance().institutionId + "&frm_date=" + from1 + "&to_date=" + to1 + "&device_id=" + Preferences.getInstance().deviceId + "&cls_id=" + classId + "&sec_id=" + sectionId, e);

                                date1 = chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("date");
                                try {
                                    date3 = formatter.parse(date1);
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                                date2 = formatter1.format(date3);


                                date.setText(date2 + "-" + "(" + chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("total_p") + "/" + chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("total_recorded") + ")");
                                text2.setText(chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("total_recorded"));
                                text4.setText(chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("total_p"));
                                text6.setText(chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("male_p"));
                                text8.setText(chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("female_p"));
                                text10.setText(chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("total_a"));
                                text12.setText(chairmanStudentDailyAttendanceArray.getJSONObject(0).getString("total_l"));


                        }
                    } else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    loading.dismiss();


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    //Utils.showToast(getActivity(), "");
                    loading.dismiss();

                }






            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                loading.dismiss();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                //params.put("student_ID",Preferences.getInstance().studentId);
                params.put("sec_id",sectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("cls_id",classId);
                params.put("sch_id", Preferences.getInstance().schoolId);
                params.put("frm_date",from1);
                params.put("to_date",to1);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("inst_id",Preferences.getInstance().institutionId);
                //params.put("exam_id","1");
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
            loading.dismiss();
        }
    }
}
