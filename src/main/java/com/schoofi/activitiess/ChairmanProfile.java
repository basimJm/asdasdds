package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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

public class ChairmanProfile extends AppCompatActivity {

    ImageView profileImage,back;
    JSONArray userProfileArray;
    String schoolId = Preferences.getInstance().schoolId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String userId = Preferences.getInstance().userId;
    String token = Preferences.getInstance().token;
    String roleId = Preferences.getInstance().userRoleId;
    String date1,date2,date3,date4;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    TextView name,dob,phone,schoolName,schoolAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Profile");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_profile);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    Intent intent = new Intent(ChairmanProfile.this,ChairmanDashboard.class);
                    startActivity(intent);
                    finish();

            }
        });

        profileImage = (ImageView) findViewById(R.id.imageView_teacherImage);
        name = (TextView) findViewById(R.id.text_teacher_name);
        dob = (TextView) findViewById(R.id.text_teacher_dob);
        phone = (TextView) findViewById(R.id.text_teacher_phone);
        schoolName = (TextView) findViewById(R.id.text_school_name);
        schoolAddress = (TextView) findViewById(R.id.text_school_address);


        RequestQueue queue = VolleySingleton.getInstance(ChairmanProfile.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id=" +schoolId+"&role_id="+roleId+"&device_id="+ Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(ChairmanProfile.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(ChairmanProfile.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("profile")) {
                        userProfileArray = new JSONObject(response).getJSONArray("profile");
                        if (null != userProfileArray && userProfileArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = userProfileArray.toString().getBytes();
                            VolleySingleton.getInstance(ChairmanProfile.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId + "&stu_id=" + Preferences.getInstance().studentId + "&ins_id=" + Preferences.getInstance().institutionId, e);
                            Glide.with(ChairmanProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("user_picture")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(profileImage);
                            date1 = userProfileArray.getJSONObject(0).getString("user_dob");


                            try {
                                date5 = formatter.parse(date1);


                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                            date2= formatter1.format(date5);




                            dob.setText(date2+","+userProfileArray.getJSONObject(0).getString("user_email_id"));


                            if(userProfileArray.getJSONObject(0).getString("name").matches("") || userProfileArray.getJSONObject(0).getString("name").matches("null"))
                            {
                                name.setText("NA");
                            }

                            else
                            {
                                name.setText(userProfileArray.getJSONObject(0).getString("name"));
                            }
                            if(userProfileArray.getJSONObject(0).getString("user_phone").matches("") || userProfileArray.getJSONObject(0).getString("user_phone").matches("null"))
                            {
                                phone.setText("NA");
                            }

                            else
                            {
                                phone.setText(userProfileArray.getJSONObject(0).getString("user_phone"));
                            }

                            if(Preferences.getInstance().userRoleId.matches("7")) {

                                if (userProfileArray.getJSONObject(0).getString("institution_name").matches("") || userProfileArray.getJSONObject(0).getString("institution_name").matches("null")) {
                                    schoolName.setText("NA");
                                } else {
                                    schoolName.setText(userProfileArray.getJSONObject(0).getString("institution_name"));
                                }

                                if (userProfileArray.getJSONObject(0).getString("institution_add").matches("") || userProfileArray.getJSONObject(0).getString("institution_add").matches("")) {
                                    schoolAddress.setText("NA");
                                } else {
                                    schoolAddress.setText(userProfileArray.getJSONObject(0).getString("institution_add"));
                                }
                            }

                            else
                            {
                                if (userProfileArray.getJSONObject(0).getString("school_name").matches("") || userProfileArray.getJSONObject(0).getString("school_name").matches("null")) {
                                    schoolName.setText("NA");
                                } else {
                                    schoolName.setText(userProfileArray.getJSONObject(0).getString("school_name"));
                                }

                                if (userProfileArray.getJSONObject(0).getString("school_address").matches("") || userProfileArray.getJSONObject(0).getString("school_address").matches("")) {
                                    schoolAddress.setText("NA");
                                } else {
                                    schoolAddress.setText(userProfileArray.getJSONObject(0).getString("school_address"));
                                }
                            }


                        } else
                            Utils.showToast(ChairmanProfile.this, "Error Fetching Response");
                        setSupportProgressBarIndeterminateVisibility(false);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(ChairmanProfile.this, "Error fetching modules! Please try after sometime.");
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
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(ChairmanProfile.this))

            queue.add(requestObject);
        else
        {
            Utils.showToast(ChairmanProfile.this, "Unable to fetch data, kindly enable internet settings!");
        }


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChairmanProfile.this, TeacherStudentProfileImageUpload.class);
                intent.putExtra("email",Preferences.getInstance().userEmailId);
                intent.putExtra("value","2");
                startActivity(intent);
            }
        });


    }
}
