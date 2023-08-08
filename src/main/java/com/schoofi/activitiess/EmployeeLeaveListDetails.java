package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class EmployeeLeaveListDetails extends AppCompatActivity {

    private JSONArray studentLeaveListArray;
    private Button approve,reject;
    private int position;
    private TextView studentLeaveStatus,leaveRejectionReason,leaveRejectedBy1,leaveRejectionreason1,leaverejectedBy,leaveType,studentLeaveStartingDate,studentLevaeDescription,studentLeaveDetailPageTitle,studentLeaveStartingDate1,studentLevaeDescription1,leaveEndingDate;
    private ImageView backButton,imageLeaveDetail,departmentLevel;
    String studentId = Preferences.getInstance().studentId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    String fontPath = "fonts/asap.regular.ttf";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String date1,date2,date6,date7;;
    String image,leaveId;
    Button delete;
    private EditText editReason;
    private String leave_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_employee_leave_list_details);

        Typeface face = Typeface.createFromAsset(getAssets(), fontPath);
        studentLeaveStatus = (TextView) findViewById(R.id.text_leave_detail_status);
        studentLeaveStartingDate = (TextView) findViewById(R.id.text_leave_detail_leave_starting_date);
        studentLevaeDescription= (TextView) findViewById(R.id.text_leave_detail_leave_description);
        studentLevaeDescription1 = (TextView) findViewById(R.id.text_leave_detail_leave_description1);
        studentLeaveStartingDate1 = (TextView) findViewById(R.id.text_leave_detail_leave_starting_date1);
        leaveEndingDate = (TextView) findViewById(R.id.text_leave_detail_leave_ending_date1);
        studentLeaveDetailPageTitle = (TextView) findViewById(R.id.txt_LeaveDetail);
        backButton = (ImageView) findViewById(R.id.img_back);
        leaverejectedBy = (TextView) findViewById(R.id.text_leave_detail_rejected_by1);
        leaveRejectionReason = (TextView) findViewById(R.id.text_leave_detail_reason_of_rejection1);
        leaverejectedBy.setVisibility(View.GONE);
        leaveRejectionReason.setVisibility(View.GONE);
        leaveType = (TextView) findViewById(R.id.text_leave_detail_leave_type1);
        leaveRejectedBy1 = (TextView) findViewById(R.id.text_leave_detail_rejected_by);
        leaveRejectionreason1 = (TextView) findViewById(R.id.text_leave_detail_rejected_reason);

        departmentLevel = (ImageView) findViewById(R.id.img_level);
       // delete = (Button) findViewById(R.id.btn_delete);
        imageLeaveDetail = (ImageView) findViewById(R.id.image_icon);
        studentLeaveDetailPageTitle.setTypeface(face);
        studentLeaveStartingDate.setTypeface(face);
        studentLeaveStartingDate1.setTypeface(face);
        studentLevaeDescription.setTypeface(face);
        studentLevaeDescription1.setTypeface(face);
        leave_id = getIntent().getStringExtra("leave_id");
       // delete.setVisibility(View.INVISIBLE);
        approve = (Button) findViewById(R.id.btn_StudentLeaveApprove);
        reject = (Button) findViewById(R.id.btn_StudentLeaveReject);
        editReason = (EditText) findViewById(R.id.edit_reason);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        departmentLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(EmployeeLeaveListDetails.this,EmployeeLeaveLevelUpdates.class);
              intent.putExtra("leave_id",leave_id);
              startActivity(intent);
            }
        });

        Preferences.getInstance().loadPreference(getApplicationContext());



        position = getIntent().getExtras().getInt("position");
        //System.out.println(position);
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_APPROVAL_LEAVE_LIST+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&emp_id="+Preferences.getInstance().employeeId+"&user_id="+Preferences.getInstance().userId);
            studentLeaveListArray= new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentLeaveListArray!= null)
        {
            try {

                date1 = studentLeaveListArray.getJSONObject(position).getString("from_date");
                date6 = studentLeaveListArray.getJSONObject(position).getString("to_date");
                Date date3 = formatter.parse(date1);
                Date date4 = formatter.parse(date6);

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);
                date7 = formatter1.format(date4);
                studentLeaveStartingDate1.setText(date2);
                leaveEndingDate.setText(date7);
                //studentLeaveStartingDate1.setText(studentLeaveListArray.getJSONObject(position).getString("from_date"));
                studentLevaeDescription1.setText(studentLeaveListArray.getJSONObject(position).getString("leave_reason"));
                leaveType.setText(studentLeaveListArray.getJSONObject(position).getString("leave_type_name"));
                if(studentLeaveListArray.getJSONObject(position).getString("updated_by_emp_name").matches("null") || studentLeaveListArray.getJSONObject(position).getString("updated_by_emp_name").matches("") )
                {
                    leaverejectedBy.setText(studentLeaveListArray.getJSONObject(position).getString("approved_by_emp_name"));
                }
                else
                {
                    leaverejectedBy.setText(studentLeaveListArray.getJSONObject(position).getString("updated_by_emp_name"));
                }

                leaveRejectionReason.setText(studentLeaveListArray.getJSONObject(position).getString("rejection_reason"));
                //System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
                //System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
                if(studentLeaveListArray.getJSONObject(position).getString("final_approval").matches("1")) {
                    if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("0") || studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("null")) {
                        studentLeaveStatus.setText("Pending");
                        studentLeaveStatus.setTextColor(getResources().getColor(R.color.orange));
                        //delete.setVisibility(View.VISIBLE);
                        leaveRejectionReason.setVisibility(View.GONE);
                        leaverejectedBy.setVisibility(View.GONE);
                        leaveRejectionreason1.setVisibility(View.GONE);
                        leaveRejectedBy1.setVisibility(View.GONE);
                        approve.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                        editReason.setVisibility(View.VISIBLE);


                    } else if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("1")) {
                        studentLeaveStatus.setText("Approved");
                        //studentLeaveStatus.setTextColor(Color.parseColor("F2F2F2"));
                        studentLeaveStatus.setTextColor(getResources().getColor(R.color.green));
                        // delete.setVisibility(View.INVISIBLE);
                        leaveRejectionReason.setVisibility(View.VISIBLE);
                        leaverejectedBy.setVisibility(View.VISIBLE);
                        leaveRejectionreason1.setVisibility(View.VISIBLE);
                        leaveRejectedBy1.setVisibility(View.VISIBLE);
                        approve.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        editReason.setVisibility(View.GONE);

                    } else if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("2")) {
                        studentLeaveStatus.setText("Rejected");
                        //studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));
                        studentLeaveStatus.setTextColor(getResources().getColor(R.color.red));
                        leaveRejectionReason.setVisibility(View.VISIBLE);
                        leaverejectedBy.setVisibility(View.VISIBLE);
                        leaveRejectionreason1.setVisibility(View.VISIBLE);
                        leaveRejectedBy1.setVisibility(View.VISIBLE);
                        approve.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        editReason.setVisibility(View.GONE);


                    } else {
                        studentLeaveStatus.setText("Rejected");
                        //studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));
                        // delete.setVisibility(View.INVISIBLE);

                    }
                }
                else
                {
                    if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("0") || studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("null")) {
                        studentLeaveStatus.setText("Pending");
                        studentLeaveStatus.setTextColor(getResources().getColor(R.color.orange));
                        //delete.setVisibility(View.VISIBLE);
                        leaveRejectionReason.setVisibility(View.GONE);
                        leaverejectedBy.setVisibility(View.GONE);
                        leaveRejectionreason1.setVisibility(View.GONE);
                        leaveRejectedBy1.setVisibility(View.GONE);
                        approve.setVisibility(View.VISIBLE);
                        reject.setVisibility(View.VISIBLE);
                        editReason.setVisibility(View.VISIBLE);
                    }


//                    } else if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("1")) {
//                        studentLeaveStatus.setText("Approved");
//                        //studentLeaveStatus.setTextColor(Color.parseColor("F2F2F2"));
//                        // delete.setVisibility(View.INVISIBLE);
//                        leaveRejectionReason.setVisibility(View.VISIBLE);
//                        studentLeaveStatus.setTextColor(getResources().getColor(R.color.green));
//                        leaverejectedBy.setVisibility(View.VISIBLE);
//                        leaveRejectionreason1.setVisibility(View.VISIBLE);
//                        leaveRejectedBy1.setVisibility(View.VISIBLE);
//                        approve.setVisibility(View.GONE);
//                        reject.setVisibility(View.GONE);
//                        editReason.setVisibility(View.GONE);
//
//                    } else if (studentLeaveListArray.getJSONObject(position).getString("approved_or_rejected").equals("2")) {
//                        studentLeaveStatus.setText("Rejected");
//                        //studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));
//                        leaveRejectionReason.setVisibility(View.VISIBLE);
//                        leaverejectedBy.setVisibility(View.VISIBLE);
//                        leaveRejectionreason1.setVisibility(View.VISIBLE);
//                        studentLeaveStatus.setTextColor(getResources().getColor(R.color.red));
//                        leaveRejectedBy1.setVisibility(View.VISIBLE);
//                        approve.setVisibility(View.GONE);
//                        reject.setVisibility(View.GONE);
//                        editReason.setVisibility(View.GONE);
//
//
//                    } else {
//                        studentLeaveStatus.setText("Rejected");
//                        //studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));
//                        // delete.setVisibility(View.INVISIBLE);
//
//                    }
                }

                image = studentLeaveListArray.getJSONObject(position).getString("image");

                if(image.matches(""))
                {
                    imageLeaveDetail.setImageResource(R.drawable.cameracross);
                }

                else
                {
                    imageLeaveDetail.setImageResource(R.drawable.camera);
                }




            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        imageLeaveDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(image.matches(""))
                {
                    Utils.showToast(getApplicationContext(), "No image");
                }

                else
                {
                    Intent intent = new Intent(EmployeeLeaveListDetails.this,TeacherStudentImageDetails.class);
                    intent.putExtra("imageUrl", image);
                    startActivity(intent);
                }
            }
        });


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postMessage();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage1();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_leave_detail, menu);
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



    private void postMessage()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_STATUS_UPDATE/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("oppp",response);

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"Error Submitting Comment");
                        loading.dismiss();


                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        loading.dismiss();
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Successfuly Submitted ");
                        loading.dismiss();
                        finish();
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                    loading.dismiss();
                }


            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                loading.dismiss();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();

                try {
                    params.put("emp_id",studentLeaveListArray.getJSONObject(position).getString("emp_id"));
                    params.put("id", studentLeaveListArray.getJSONObject(position).getString("id"));
                    params.put("dept_id",studentLeaveListArray.getJSONObject(position).getString("dept_id"));
//                    Log.d("dept_id",studentLeaveListArray.getJSONObject(position).getString("dept_id"));
//                    Log.d("oppp",studentLeaveListArray.getJSONObject(position).getString("id"));
//                    Log.d("oppp1",studentLeaveListArray.getJSONObject(position).getString("emp_id"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("status","1");
                params.put("u_email_id",userEmailId);
                params.put("user_id",userId);
                params.put("token", token);
                params.put("device_id", Preferences.getInstance().deviceId);

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("leave_rejection_reason",editReason.getText().toString());

                params.put("approved_by_emp_id",Preferences.getInstance().userId);



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
            loading.dismiss();
        }
    }

    private void postMessage1()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Loading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_STATUS_UPDATE/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("oppp",response);

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"Error Submitting Comment");
                        loading.dismiss();


                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        loading.dismiss();
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Successfuly Submitted");

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

                try {
                    params.put("emp_id",studentLeaveListArray.getJSONObject(position).getString("emp_id"));
                    params.put("id", studentLeaveListArray.getJSONObject(position).getString("id"));
                    params.put("dept_id",studentLeaveListArray.getJSONObject(position).getString("dept_id"));
//                    Log.d("dept_id",studentLeaveListArray.getJSONObject(position).getString("dept_id"));
//                    Log.d("emp_id",studentLeaveListArray.getJSONObject(position).getString("emp_id"));
//                    Log.d("id", studentLeaveListArray.getJSONObject(position).getString("id"));
//                    Log.d("status","2");
//                    Log.d("u_email_id",userEmailId);
//                    Log.d("u_id",userId);
//                    Log.d("token", token);
//
//                    Log.d("device_id", Preferences.getInstance().deviceId);
//                    Log.d("ins_id",Preferences.getInstance().institutionId);
//                    Log.d("sch_id",Preferences.getInstance().schoolId);
//                    Log.d("leave_rejection_reason",editReason.getText().toString());
//                    Log.d("dept_id",Preferences.getInstance().departmentId);
//                    Log.d("approved_by_emp_id",Preferences.getInstance().employeeId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("status","2");
                params.put("u_email_id",userEmailId);
                params.put("user_id",userId);
                params.put("token", token);

                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("leave_rejection_reason",editReason.getText().toString());

                params.put("approved_by_emp_id",Preferences.getInstance().userId);


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
