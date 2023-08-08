package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.adapters.EmployeeAttendanceListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;

public class BiometricAttendanceStudentScreen extends AppCompatActivity implements View.OnClickListener{

    private EditText leaveStarting, leaveEnding;
    private TextView assignmentTextView,circularTextView;
    private ImageView imageBack;
    private PopupWindow calendarPopup, calendarPopup1;
    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    String fromDate, todate;
    int count1 = 0;
    int count = 0;
    int count2 = 0;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String date1, date2;
    private ListView employeeAttendanceListView;
    private EmployeeAttendanceListViewAdapter employeeAttendanceListViewAdapter;
    private JSONArray employeeAttendanceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_biometric_attendance_student_screen);

        imageBack = (ImageView) findViewById(R.id.img_back);
        leaveStarting = (EditText) findViewById(R.id.btn_student_leave_starting_date);
        leaveEnding = (EditText) findViewById(R.id.btn_student_leave_ending_date);

        circularTextView = (TextView) findViewById(R.id.text_cirular);

        leaveStarting.setOnClickListener(onEditTextClickListener);
        leaveEnding.setOnClickListener(onEditTextClickListener1);
        employeeAttendanceListView = (ListView) findViewById(R.id.listView_student_daily_attendance);

        circularTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                getStudentCustomAttendance();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(BiometricAttendanceStudentScreen.this);
                CalendarPickerView calendarView = new CalendarPickerView(BiometricAttendanceStudentScreen.this);
                calendarView.setListener(dateSelectionListener);
                calendarPopup.setContentView(calendarView);
                calendarPopup.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup.setHeight(1);
                calendarPopup.setWidth(view.getWidth());
                calendarPopup.setOutsideTouchable(true);
            }
            calendarPopup.showAtLocation(circularTextView, Gravity.CENTER, 0, 0);
        }
    };

    private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup1 == null) {
                calendarPopup1 = new PopupWindow(BiometricAttendanceStudentScreen.this);
                CalendarPickerView calendarView = new CalendarPickerView(BiometricAttendanceStudentScreen.this);
                calendarView.setListener(dateSelectionListener1);
                calendarPopup1.setContentView(calendarView);
                calendarPopup1.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup1.setHeight(1);
                calendarPopup1.setWidth(view.getWidth());
                calendarPopup1.setOutsideTouchable(true);
            }
            calendarPopup1.showAtLocation(circularTextView, Gravity.CENTER, 0, 0);
        }
    };

    private CalendarNumbersView.DateSelectionListener dateSelectionListener = new CalendarNumbersView.DateSelectionListener() {
        @Override
        public void onDateSelected(Calendar selectedDate) {
            if (calendarPopup.isShowing()) {
                calendarPopup.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calendarPopup.dismiss();
                    }
                }, 500);//For clarity, we close the popup not immediately.
            }
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MMM/yyyy");

            leaveStarting.setText(formatter1.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            date1 = formatter.format(selectedDate.getTime());
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));

        }
    };

    private CalendarNumbersView.DateSelectionListener dateSelectionListener1 = new CalendarNumbersView.DateSelectionListener() {
        @Override
        public void onDateSelected(Calendar selectedDate) {
            if (calendarPopup1.isShowing()) {
                calendarPopup1.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calendarPopup1.dismiss();
                    }
                }, 500);//For clarity, we close the popup not immediately.
            }
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MMM/yyyy");

            leaveEnding.setText(formatter1.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            date2 = formatter.format(selectedDate.getTime());

        }
    };

    @Override
    public void onClick(View v) {


    }

    private void initData()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_BIOEMTRIC_ATTENDANCE+"?stu_id="+ Preferences.getInstance().studentId+"&token="+Preferences.getInstance().token+"&from_dt="+date1+"&to_dt="+date2+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId);
            if(e == null)
            {
                employeeAttendanceArray = null;
            }
            else
            {
                employeeAttendanceArray = new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(employeeAttendanceArray!= null)
        {
            employeeAttendanceListViewAdapter= new EmployeeAttendanceListViewAdapter(BiometricAttendanceStudentScreen.this,employeeAttendanceArray);
            employeeAttendanceListView.setAdapter(employeeAttendanceListViewAdapter);
            employeeAttendanceListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentCustomAttendance()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_BIOEMTRIC_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("opp",response);

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        employeeAttendanceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        // Utils.showToast(context, "Session Expired:Please Login Again");

                        employeeAttendanceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("student_attendance"))
                    {

                        employeeAttendanceListView.setVisibility(View.VISIBLE);
                        employeeAttendanceArray= new JSONObject(response).getJSONArray("student_attendance");
                        if(null!=employeeAttendanceArray && employeeAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = employeeAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_BIOEMTRIC_ATTENDANCE+"?stu_id="+ Preferences.getInstance().studentId+"&token="+Preferences.getInstance().token+"&from_dt="+date1+"&to_dt="+date2+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId,e);
                            employeeAttendanceListView.invalidateViews();
                            employeeAttendanceListViewAdapter = new EmployeeAttendanceListViewAdapter(BiometricAttendanceStudentScreen.this, employeeAttendanceArray);
                            employeeAttendanceListView.setAdapter(employeeAttendanceListViewAdapter);
                            employeeAttendanceListViewAdapter.notifyDataSetChanged();

                        }
                    }
                    else
                        //Utils.showToast(context, "Error Fetching Response");
                        System.out.println("op");
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    // Utils.showToast(context, "Error fetching modules! Please try after sometime.");
                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                Log.d("dd",date1+date2);
                params.put("from_dt",date1);
                params.put("to_dt",date2);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("cls_id",Preferences.getInstance().studentClassId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
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
