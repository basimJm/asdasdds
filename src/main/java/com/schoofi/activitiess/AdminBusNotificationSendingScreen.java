package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import com.schoofi.modals.Request;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.BusNotificationMessageVO;
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

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import smtchahal.materialspinner.MaterialSpinner;


public class AdminBusNotificationSendingScreen extends AppCompatActivity {


    String selectedDate1,selectedDate2;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    final Context context = this;

    String busRoute,shift,bus_no;
    HorizontalCalendar horizontalCalendar;

    private Button markStudents,upload,custom;
    private JSONArray teacherStudentSubmittedAttendancearray;
    private TextView description;
    String dat;
    int f=0;
    String array1,array2;
    public ArrayList<String> attendance = new ArrayList<String>();
    MaterialSpinner materialSpinner;
    private String message1,messageId1;
    ArrayList<BusNotificationMessageVO> messageId;
    ArrayList<String> message;
    JSONObject jsonObject1;
    JSONArray jsonArray1;
    private ArrayAdapter<String> adapter;
    private String m="material";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Admin Bus Notification Sending Screen");



        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_admin_bus_notification_sending_screen);

        busRoute = getIntent().getStringExtra("bus_route");
        //shift = getIntent().getStringExtra("shift");
        bus_no = getIntent().getStringExtra("bus_no");

        markStudents = (Button) findViewById(R.id.btn_mark_students);
        upload = (Button) findViewById(R.id.btn_done);
        materialSpinner = (MaterialSpinner) findViewById(R.id.spinner_board);
        custom = (Button) findViewById(R.id.btn_custom_message);
        materialSpinner.setBackgroundResource(R.drawable.grey_button);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);

        description = (EditText) findViewById(R.id.edit_message);
        description.setVisibility(View.GONE);

        new DownloadJSON().execute();

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(custom.getText().toString().matches("Write Custom Message"))
                {
                    materialSpinner.setVisibility(View.GONE);
                    description.setVisibility(View.VISIBLE);
                    custom.setText("Select From Existing");
                    m="description";
                }

                else
                {
                    m="material";
                    custom.setText("Write Custom Message");
                    materialSpinner.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                }
            }
        });



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
            }
        });

        markStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminBusNotificationSendingScreen.this,BusAdminStudentMarkList.class);
                intent.putExtra("bus_route",busRoute);
                intent.putExtra("bus_no",bus_no);
                intent.putExtra("date",date);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(m.matches("material"))
                {
                    if(message1.matches("") || message1.matches("null"))
                    {
                        Utils.showToast(getApplicationContext(),"Please select message");
                    }

                    else
                    {
                        try {

                            Cache.Entry e;
                            e = e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&bus_no=" + bus_no + "&route_number=" + busRoute + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&day=" + date);
                            if (e == null) {
                                teacherStudentSubmittedAttendancearray = null;
                                Utils.showToast(getApplicationContext(), "Please select students");
                            } else {
                                teacherStudentSubmittedAttendancearray = new JSONArray(new String(e.data));
                            }

                            if (teacherStudentSubmittedAttendancearray != null) {


                                if (date.matches(teacherStudentSubmittedAttendancearray.getJSONObject(0).getString("date"))) {
                                    for (int i = 0; i < teacherStudentSubmittedAttendancearray.length(); i++) {
                                        try {
                                            if (teacherStudentSubmittedAttendancearray.getJSONObject(i).getString("isAdded").matches("A")) {
                                                attendance.add(teacherStudentSubmittedAttendancearray.getJSONObject(i).getString("stu_id"));
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                    array1 = attendance.toString();
                                    array2 = array1.substring(1, array1.length() - 1);
                                    postAttendance();
                                } else {
                                    teacherStudentSubmittedAttendancearray = null;
                                    Utils.showToast(getApplicationContext(), "Date not matches!! Please select the date and students again!1");
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                else
                    if(m.matches("description"))
                    {
                        if(description.getText().toString().matches("") || description.getText().toString().matches("null"))
                        {
                            Utils.showToast(getApplicationContext(),"Please fill message");
                        }

                        else
                        {

                            try {

                                Cache.Entry e;
                                e = e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_ROUTE_STUDENT_LIST + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&bus_no=" + bus_no + "&route_number=" + busRoute + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&day=" + date);
                                if (e == null) {
                                    teacherStudentSubmittedAttendancearray = null;
                                    Utils.showToast(getApplicationContext(), "Please select students");
                                } else {
                                    teacherStudentSubmittedAttendancearray = new JSONArray(new String(e.data));
                                }

                                if (teacherStudentSubmittedAttendancearray != null) {


                                    if (date.matches(teacherStudentSubmittedAttendancearray.getJSONObject(0).getString("date"))) {
                                        for (int i = 0; i < teacherStudentSubmittedAttendancearray.length(); i++) {
                                            try {
                                                if (teacherStudentSubmittedAttendancearray.getJSONObject(i).getString("isAdded").matches("A")) {
                                                    attendance.add(teacherStudentSubmittedAttendancearray.getJSONObject(i).getString("stu_id"));
                                                }
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        array1 = attendance.toString();
                                        array2 = array1.substring(1, array1.length() - 1);
                                        postAttendance();
                                    } else {
                                        teacherStudentSubmittedAttendancearray = null;
                                        Utils.showToast(getApplicationContext(), "Date not matches!! Please select the date and students again!1");
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }



            }
        });


    }

    protected void postAttendance1()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_NOTIFICATION/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;


        StringRequest requestObject = new StringRequest(com.android.volley.Request.Method.POST,url1, new Response.Listener<String>() {
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
                        Utils.showToast(getApplicationContext(),"Error Sending Notification");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Notification Sent Successfully!!");
                        finish();

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("stu_ids",array2.toString());
                if(m.matches("material"))
                {
                    params.put("message",message1);
                }
                else {
                    params.put("message", description.getText().toString());
                }


                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("user_id",Preferences.getInstance().userId);

                /*Log.d("ins_id",Preferences.getInstance().institutionId);
                Log.d("sch_id",Preferences.getInstance().schoolId);
                Log.d("stu_ids",array2.toString());
                Log.d("message",description.getText().toString());


                Log.d("token",Preferences.getInstance().token);
                Log.d("device_id",Preferences.getInstance().deviceId);
                Log.d("user_id",Preferences.getInstance().userId);*/

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

    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_NOTIFICATION/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(com.android.volley.Request.Method.POST,url1, new Response.Listener<String>() {
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
                        Utils.showToast(getApplicationContext(),"Error Sending Notification");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Notification Sent Successfully!!");
                        finish();

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                //System.out.println(response);
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("stu_ids",array2.toString());
                if(m.matches("material"))
                {
                    params.put("message",message1);
                }
                else {
                    params.put("message", description.getText().toString());
                }


                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("user_id",Preferences.getInstance().userId);

                /*Log.d("ins_id",Preferences.getInstance().institutionId);
                Log.d("sch_id",Preferences.getInstance().schoolId);
                Log.d("stu_ids",array2.toString());
                Log.d("message",description.getText().toString());


                Log.d("token",Preferences.getInstance().token);
                Log.d("device_id",Preferences.getInstance().deviceId);
                Log.d("user_id",Preferences.getInstance().userId);*/

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

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            messageId = new ArrayList<BusNotificationMessageVO>();
            message = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            jsonObject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.BUS_NOTIFICATION_MESSAGE+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId+"&token="+Preferences.getInstance().token);
            try {
                // Locate the NodeList name
                jsonArray1 = jsonObject1.getJSONArray("responseObject");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    jsonObject1 = jsonArray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //EventUploadAudienceVO eventUploadAudienceVO1 = new EventUploadAudienceVO();
                    BusNotificationMessageVO busNotificationMessageVO1 = new BusNotificationMessageVO();

                    busNotificationMessageVO1.setMessage(jsonObject1.optString("message"));
                    messageId.add(busNotificationMessageVO1);

                    message.add(jsonObject1.optString("message"));

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


            adapter = new ArrayAdapter<>(AdminBusNotificationSendingScreen.this, android.R.layout.simple_spinner_item, message);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            materialSpinner.setAdapter(adapter);


            materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub




                    message1 = messageId.get(position).getMessage().toString();
                    Log.d("kkk",message1);
                   // message1 = messageId.get(position).getMessage().toString();
                    //materialSpinner2.setVisibility(View.VISIBLE);
                    //new DownloadJSON1().execute();





                    // System.out.println(boardId1);



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                    message1="";

                }


            });




        }
    }

    private void postLeave7() {
        class UploadImage extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            com.schoofi.modals.Request request = new com.schoofi.modals.Request();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdminBusNotificationSendingScreen.this, "Uploading", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                Log.d("ll",s);

                finish();
            }

            @Override
            protected String doInBackground(String... params) {
                String string = params[0];
                //String uploadImage = getStringImage(bitmap);
                //image = getStringImage(bitmap);


                // Utils.showToast(getApplicationContext(), image);
                HashMap<String, String> data = new HashMap<>();
                data.put("ins_id",Preferences.getInstance().institutionId);
                data.put("sch_id",Preferences.getInstance().schoolId);
                data.put("stu_ids",array2.toString());
                if(m.matches("material"))
                {
                    data.put("message",message1);
                }
                else {
                    data.put("message", description.getText().toString());
                }


                data.put("token",Preferences.getInstance().token);
                data.put("device_id",Preferences.getInstance().deviceId);
                data.put("user_id",Preferences.getInstance().userId);

                String result = request.sendPostRequest(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_BUS_NOTIFICATION, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute("harsh");
    }
}
