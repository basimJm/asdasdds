package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherProfile extends AppCompatActivity {

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
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    TextView name,dob,phone,schoolName,schoolAddress,joiningDate,rank,qualification,experience,religion,disability,busRouteNumber,birthMark,height,weight,bloodGroup,alergies,employeeCode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_profile);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        profileImage = (ImageView) findViewById(R.id.imageView_teacherImage);
        name = (TextView) findViewById(R.id.text_teacher_name);
        dob = (TextView) findViewById(R.id.text_teacher_dob);
        phone = (TextView) findViewById(R.id.text_teacher_phone);
        schoolName = (TextView) findViewById(R.id.text_school_name);
        schoolAddress = (TextView) findViewById(R.id.text_school_address);
        joiningDate = (TextView) findViewById(R.id.text_student_joining_date);
        rank = (TextView) findViewById(R.id.text_student_rank);
        qualification = (TextView) findViewById(R.id.text_student_qualification);
        experience = (TextView) findViewById(R.id.text_student_experience);
        religion = (TextView) findViewById(R.id.text_student_religion);
        disability = (TextView) findViewById(R.id.text_student_disability);
        disability.setVisibility(View.GONE);
        busRouteNumber = (TextView) findViewById(R.id.text_student_bus_number);
        birthMark = (TextView) findViewById(R.id.text_student_birth_mark);
        birthMark.setVisibility(View.GONE);
        height = (TextView) findViewById(R.id.text_student_height);
        weight = (TextView) findViewById(R.id.text_student_weight);
        bloodGroup = (TextView) findViewById(R.id.text_student_blood_group);
        alergies = (TextView) findViewById(R.id.text_student_alergies);
        employeeCode = (TextView) findViewById(R.id.text_student_code);



        RequestQueue queue = VolleySingleton.getInstance(TeacherProfile.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id=" +schoolId+"&role_id="+roleId+"&device_id="+ Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                //System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(TeacherProfile.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(TeacherProfile.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("profile")) {
                        userProfileArray = new JSONObject(response).getJSONArray("profile");
                        if (null != userProfileArray && userProfileArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = userProfileArray.toString().getBytes();
                            VolleySingleton.getInstance(TeacherProfile.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&sch_id=" + schoolId + "&role_id=" + roleId + "&device_id=" + Preferences.getInstance().deviceId  + "&ins_id=" + Preferences.getInstance().institutionId, e);
                            mDrawableBuilder = TextDrawable.builder().round();
                            TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(responseObject.getJSONArray("profile").getJSONObject(0).getString("teac_name").charAt(0)), R.color.blue);
                            Glide.with(TeacherProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("user_picture")).placeholder(textDrawable).error(textDrawable).into(profileImage);
                            date1 = userProfileArray.getJSONObject(0).getString("date_birth");
                            date3 = userProfileArray.getJSONObject(0).getString("joining_date");

                            try {
                                date5 = formatter.parse(date1);
                                date6 = formatter.parse(date3);

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                            date2= formatter1.format(date5);
                            date4 =formatter1.format(date6);



                            dob.setText(date2+","+userProfileArray.getJSONObject(0).getString("user_email_id"));
                            joiningDate.setText("Joining Date: "+date4);

                            if(userProfileArray.getJSONObject(0).getString("teac_name").matches("") || userProfileArray.getJSONObject(0).getString("teac_name").matches("null"))
                            {
                                name.setText("Name : -");
                            }

                            else
                            {
                                name.setText(userProfileArray.getJSONObject(0).getString("teac_name"));
                            }
                            if(userProfileArray.getJSONObject(0).getString("contact").matches("") || userProfileArray.getJSONObject(0).getString("contact").matches("null"))
                            {
                                phone.setText("Mobile : -");
                            }

                            else
                            {
                                phone.setText(userProfileArray.getJSONObject(0).getString("contact"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("school_name").matches("") || userProfileArray.getJSONObject(0).getString("school_name").matches("null"))
                            {
                                schoolName.setText("School Name : -");
                            }

                            else
                            {
                                schoolName.setText(userProfileArray.getJSONObject(0).getString("school_name"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("school_address").matches("") || userProfileArray.getJSONObject(0).getString("school_address").matches(""))
                            {
                                schoolAddress.setText("School Address : -");
                            }

                            else
                            {
                                schoolAddress.setText(userProfileArray.getJSONObject(0).getString("school_address"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("rank").matches("") || userProfileArray.getJSONObject(0).getString("rank").matches("null"))
                            {
                                rank.setText("Rank: "+"-");
                            }

                            else
                            {
                                rank.setText("Rank: "+userProfileArray.getJSONObject(0).getString("rank"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("qualification").matches("") || userProfileArray.getJSONObject(0).getString("qualification").matches("null"))
                            {
                                qualification.setText("Qualification: "+"-");
                            }

                            else
                            {
                                qualification.setText("Qualification: "+userProfileArray.getJSONObject(0).getString("qualification"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("experience").matches("") || userProfileArray.getJSONObject(0).getString("experience").matches("null"))
                            {
                                experience.setText("Experience: "+"-");
                            }

                            else
                            {
                                experience.setText("Experience: "+userProfileArray.getJSONObject(0).getString("experience"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("religion").matches("") || userProfileArray.getJSONObject(0).getString("religion").matches("null"))
                            {
                                religion.setText("Religion: "+"-");
                            }

                            else
                            {
                                religion.setText("Religion: "+userProfileArray.getJSONObject(0).getString("religion"));
                            }



                            if(userProfileArray.getJSONObject(0).getString("bus_route_no").matches("") || userProfileArray.getJSONObject(0).getString("bus_route_no").matches("null"))
                            {
                                busRouteNumber.setText("Bus Route: "+"-");
                            }

                            else
                            {
                                busRouteNumber.setText("Bus Route: "+userProfileArray.getJSONObject(0).getString("bus_route_no"));
                            }



                            if(userProfileArray.getJSONObject(0).getString("height").matches("") || userProfileArray.getJSONObject(0).getString("height").matches("null"))
                            {
                                height.setText("Height: "+"-");
                            }
                            else
                            {
                                height.setText("Height: "+userProfileArray.getJSONObject(0).getString("height"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("weight").matches("") || userProfileArray.getJSONObject(0).getString("weight").matches("null"))
                            {
                                weight.setText("Weight: "+"-");
                            }

                            else
                            {
                                weight.setText("Weight: "+userProfileArray.getJSONObject(0).getString("weight"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("blood_group").matches("") || userProfileArray.getJSONObject(0).getString("blood_group").matches("null"))
                            {
                                bloodGroup.setText("Blood Group: "+"-");
                            }

                            else
                            {
                                bloodGroup.setText("Blood Group: "+userProfileArray.getJSONObject(0).getString("blood_group"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("allergies").matches("") || userProfileArray.getJSONObject(0).getString("allergies").matches("null"))
                            {
                               alergies.setText("Alergies: "+"-");
                            }

                            else
                            {
                                alergies.setText("Alergies: "+userProfileArray.getJSONObject(0).getString("allergies"));
                            }

                            if(userProfileArray.getJSONObject(0).getString("emp_code").matches("") || userProfileArray.getJSONObject(0).getString("emp_code").matches("null"))
                            {
                                employeeCode.setText("Employee Code: "+"-");
                            }

                            else
                            {
                                employeeCode.setText("Employee Code: "+userProfileArray.getJSONObject(0).getString("emp_code"));
                            }
                        } else
                            Utils.showToast(TeacherProfile.this, "Error Fetching Response");
                        setSupportProgressBarIndeterminateVisibility(false);
                    }

                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                        Utils.showToast(TeacherProfile.this, "Error fetching modules! Please try after sometime.");
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
            if(Utils.isNetworkAvailable(TeacherProfile.this))
                    queue.add(requestObject);
            else
            {
                Utils.showToast(TeacherProfile.this, "Unable to fetch data, kindly enable internet settings!");
            }


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherProfile.this, TeacherStudentProfileImageUpload.class);
                intent.putExtra("email",Preferences.getInstance().userEmailId);
                intent.putExtra("value","2");
                startActivity(intent);
            }
        });


    }

}
