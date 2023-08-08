package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.adapters.AdminNotifyScreenAdapter;
import com.schoofi.adapters.BusStudentMarkScreenAdapter;
import com.schoofi.adapters.TeacherStudentAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceNotifyScreen extends AppCompatActivity {

    private JSONArray adminNotifyScreenArray;
    TeacherStudentAttendanceAdapter teacherStudentAttendanceAdapter;
    private TextView screenTitle,teacherClass,teacherSection,newView1;
    private ImageView back,notify;
    private ListView teacherStudentAttendanceListView;
    String userEmailId,userId,token,teachId;
    private SwipyRefreshLayout swipyRefreshLayout;
    AdminNotifyScreenAdapter adminNotifyScreenAdapter;
    final Context context = this;
    public ArrayList<String> attendance = new ArrayList<String>();

    private Button done;
    String array1,array2;

    private String indicator;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_attendance_notify_screen);

        teacherStudentAttendanceListView = (ListView) findViewById(R.id.teacher_homescreen_listview);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        done = (Button) findViewById(R.id.btn_done);



        if(checkBox.isChecked())
        {
            indicator = "true";
        }

        else
        {
            indicator = "false";
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkBox.isChecked()) {
                    indicator = "true";
                    for (int j = 0; j < adminNotifyScreenArray.length(); j++) {
                        try {

                            adminNotifyScreenArray.getJSONObject(j).put("isAdded", "A");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = adminNotifyScreenArray.toString().getBytes();
                    VolleySingleton.getInstance(AttendanceNotifyScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_TWO_STEP_ATTENDANCE_PRIMARY+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId, e);
                    teacherStudentAttendanceListView.invalidateViews();
                    adminNotifyScreenAdapter = new AdminNotifyScreenAdapter(AttendanceNotifyScreen.this, adminNotifyScreenArray);
                    teacherStudentAttendanceListView.setAdapter(adminNotifyScreenAdapter);
                    adminNotifyScreenAdapter.notifyDataSetChanged();
                }


                else {
                    indicator = "false";
                    for (int j = 0; j < adminNotifyScreenArray.length(); j++) {
                        try {

                            adminNotifyScreenArray.getJSONObject(j).put("isAdded", "N");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Cache.Entry e = new Cache.Entry();
                    e.data = adminNotifyScreenArray.toString().getBytes();
                    VolleySingleton.getInstance(AttendanceNotifyScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_TWO_STEP_ATTENDANCE_PRIMARY+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId, e);
                    teacherStudentAttendanceListView.invalidateViews();
                    adminNotifyScreenAdapter = new AdminNotifyScreenAdapter(AttendanceNotifyScreen.this, adminNotifyScreenArray);
                    teacherStudentAttendanceListView.setAdapter(adminNotifyScreenAdapter);
                    adminNotifyScreenAdapter.notifyDataSetChanged();
                }


            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub





                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.notify_layout, null);



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);




                final TextView present = (TextView) promptsView.findViewById(R.id.text_total_present);

                final TextView total =   (TextView) promptsView.findViewById(R.id.text_total);

                int present1=0,absent1=0,leave1=0,total1=0;

                for(int k =0;k<adminNotifyScreenArray.length();k++)
                {
                    try {
                        if(adminNotifyScreenArray.getJSONObject(k).getString("isAdded").matches("A"))
                        {
                            present1 = present1+1;
                            attendance.add(adminNotifyScreenArray.getJSONObject(k).getString("section_id"));


                        }

                        else
                        {

                        }


                        total1 = k;














                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                present.setText("Total Marked: "+ String.valueOf(present1));

                total.setText("Total Classes: "+ String.valueOf(total1+1));



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

                                        System.out.println(array2);



                                        postAttendance();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        attendance.clear();

                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();




            }
        });



        //initData();
        getChairmanStudentLeaveList();
    }



    protected void getChairmanStudentLeaveList()
    {
        Preferences.getInstance().loadPreference(getApplicationContext());
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(AttendanceNotifyScreen.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_TWO_STEP_ATTENDANCE_PRIMARY/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("class_list"))
                    {
                        adminNotifyScreenArray= new JSONObject(response).getJSONArray("class_list");
                        for (int j = 0; j < adminNotifyScreenArray.length(); j++) {

                            adminNotifyScreenArray.getJSONObject(j).put("isAdded","N");
                            //teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
                        }

                        System.out.print(adminNotifyScreenArray.toString());
                        if(null!=adminNotifyScreenArray && adminNotifyScreenArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = adminNotifyScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(AttendanceNotifyScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_TWO_STEP_ATTENDANCE_PRIMARY+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&role_id="+Preferences.getInstance().userRoleId,e);
                            teacherStudentAttendanceListView.invalidateViews();
                            adminNotifyScreenAdapter = new AdminNotifyScreenAdapter(AttendanceNotifyScreen.this,adminNotifyScreenArray);
                            teacherStudentAttendanceListView.setAdapter(adminNotifyScreenAdapter);
                            adminNotifyScreenAdapter.notifyDataSetChanged();

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
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                Preferences.getInstance().loadPreference(getApplicationContext());
                params.put("token",Preferences.getInstance().token);
                //System.out.print("lll");
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                //System.out.print("lll");
                params.put("u_id",Preferences.getInstance().userId);
                //System.out.print("lll");
                params.put("device_id", Preferences.getInstance().deviceId);
                //System.out.print("lll");
                params.put("ins_id",Preferences.getInstance().institutionId);
                //System.out.print("lll");
                params.put("sch_id",Preferences.getInstance().schoolId);
                //System.out.print("lll");
                params.put("role_id",Preferences.getInstance().userRoleId);
                //System.out.print("lll");
                if(Preferences.getInstance().userRoleId.matches("3"))
                {
                    Log.d("kkkk","kkk");
                }

                else {
                    System.out.print("lllllmmmm");
                    params.put("teac_id",Preferences.getInstance().teachId);

                }
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

    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TWO_STEP_NOTIFICATIONS/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

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
                params.put("class_section_id",array2.toString());



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
}
