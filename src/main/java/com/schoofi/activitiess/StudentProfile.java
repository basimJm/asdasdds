package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Cache.Entry;
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

/**
 * Created by Schoofi on 27-05-2016.
 */
public class StudentProfile extends AppCompatActivity {

    private TextView screenTitle,username,userClass,userSchool,userFathersName,userMotherName;
    private TextView studentName,studentRollNumber,studentDob,studentAdmissionNo,studentDate,studentSex,studentTrade,studentClass,studentSecion,studentStream,studentEmail,studentSchoolName,studentSchoolAddress,studentFatherName,studentFatherDob,studentfatherPhone,studentFatherEmail,studentMotherName,studentMotherDob,studentMotherPhone,studentMotherEmail,studentReligion,studentEws,studentDiasbility,studentBusNumber,studentBirthmark,studentHeight,studentWeight,studentBloodGroup,studentalergies;
    private ImageView userProfileImage,back;
    private ImageView studentImage,studentFatherImage,studentMotherImage;
    private JSONArray userProfileArray;
    String schoolId = Preferences.getInstance().schoolId;
    String userEmailId = Preferences.getInstance().userEmailId;
    String userId = Preferences.getInstance().userId;
    String token = Preferences.getInstance().token;
    String roleId = Preferences.getInstance().userRoleId;
    String date1,date2,date3,date4,date5,date6;
    String studentDob2,studentFatherDob2,studentMotherDob2,studentAdmissionDate2,studentAdmissionDate3,studentDob3,studentFatherDob3,studentMotherDob3;
    Date studentDob1,studentFatherDob1,studentMotherDob1,studentAdmissionDate1;
    Date date7,date8,date9;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("User Profile");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.student_profile);

        //Utils.showToast(getApplicationContext(),"kkkk");

        screenTitle = (TextView) findViewById(R.id.txt_profile);
        studentName = (TextView) findViewById(R.id.text_student_name);
        studentDob = (TextView) findViewById(R.id.text_student_dob);
        studentAdmissionNo = (TextView) findViewById(R.id.text_student_admissionNo);
        studentDate = (TextView) findViewById(R.id.text_student_date);
        studentRollNumber = (TextView) findViewById(R.id.text_student_roll);
        studentSex = (TextView) findViewById(R.id.text_student_sex);
        studentTrade = (TextView) findViewById(R.id.text_student_trade);
        studentClass = (TextView) findViewById(R.id.text_student_class);
        studentSecion = (TextView) findViewById(R.id.text_student_section);
        studentStream = (TextView) findViewById(R.id.text_student_stream);
        studentEmail = (TextView) findViewById(R.id.text_student_email);
        studentSchoolName = (TextView) findViewById(R.id.text_school_name);
        studentSchoolAddress = (TextView) findViewById(R.id.text_school_address);
        studentFatherName = (TextView) findViewById(R.id.text_student_father_name);
        studentFatherDob = (TextView) findViewById(R.id.text_student_father_dob);
        studentFatherEmail = (TextView) findViewById(R.id.text_student_father_email);
        studentfatherPhone = (TextView) findViewById(R.id.text_student_father_phoneNo);
        studentMotherName = (TextView) findViewById(R.id.text_student_mother_name);
        studentMotherDob = (TextView) findViewById(R.id.text_student_mother_dob);
        studentMotherEmail = (TextView) findViewById(R.id.text_student_mother_email);
        studentMotherPhone = (TextView) findViewById(R.id.text_student_mother_phoneNo);
        studentReligion = (TextView) findViewById(R.id.text_student_religion);
        studentEws = (TextView) findViewById(R.id.text_student_ews);
        studentDiasbility = (TextView) findViewById(R.id.text_student_disability);
        //studentBusNumber = (TextView) findViewById(R.id.text_student_bus_number);
        studentBirthmark = (TextView) findViewById(R.id.text_student_birth_mark);
        studentHeight = (TextView) findViewById(R.id.text_student_height);
        studentWeight = (TextView) findViewById(R.id.text_student_weight);
        studentBloodGroup = (TextView) findViewById(R.id.text_student_blood_group);
        studentalergies = (TextView) findViewById(R.id.text_student_alergies);
        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        studentImage = (ImageView) findViewById(R.id.student_profile);
        studentFatherImage = (ImageView) findViewById(R.id.student_father_profile);
        studentMotherImage = (ImageView) findViewById(R.id.student_mother_profile);

        Preferences.getInstance().loadPreference(getApplicationContext());

        if(Preferences.getInstance().userRoleId.matches("3")) {

            studentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentProfile.this, ProfileImageUpdateScreen.class);
                    intent.putExtra("type", "student");
                    startActivity(intent);
                }
            });

            studentMotherImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentProfile.this, ProfileImageUpdateScreen.class);
                    intent.putExtra("type", "mother");
                    startActivity(intent);
                }
            });

            studentFatherImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentProfile.this, ProfileImageUpdateScreen.class);
                    intent.putExtra("type", "father");
                    startActivity(intent);
                }
            });

        }

        else
        {

        }

        if(Preferences.getInstance().schoolType.matches("School") || Preferences.getInstance().schoolType.matches("school") || Preferences.getInstance().schoolType.matches("SCHOOL"))
        {
            studentStream.setVisibility(View.GONE);
            studentTrade.setVisibility(View.GONE);
        }
        else
        {
            studentStream.setVisibility(View.VISIBLE);
            studentTrade.setVisibility(View.VISIBLE);
        }

        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(StudentProfile.this).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id=" +schoolId+"&role_id="+roleId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId;
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                //System.out.println(response);
                System.out.println(url);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(StudentProfile.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentProfile.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("profile"))
                    {
                        userProfileArray= new JSONObject(response).getJSONArray("profile");
                        if(null!=userProfileArray && userProfileArray.length()>=0)
                        {
                            Entry e = new Entry();
                            e.data = userProfileArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentProfile.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.USER_PROFILE_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id=" +schoolId+"&role_id="+roleId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId,e);
									/*username.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name"));
									userSchool.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_name"));
									userClass.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name")+" "+responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name"));
									userFathersName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name"));
									userMotherName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name"));*/
                            //Picasso.with(UserProfile.this).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(userProfileImage);
                            //Glide.with(UserProfile.this).load(AppConstants.SERVER_URLS.SERVER_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).crossFade().placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(userProfileImage);

                            mDrawableBuilder = TextDrawable.builder().round();
                            String Na = "NA";
                            TextDrawable textDrawable,textDrawable1,textDrawable2;
                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").matches("null") )
                            {
                                textDrawable = mDrawableBuilder.build(String.valueOf(Na.charAt(0)), R.color.blue);
                            }
                            else {
                                textDrawable = mDrawableBuilder.build(String.valueOf(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").charAt(0)), R.color.blue);
                            }
                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name").matches("null"))
                            {
                                textDrawable1 = mDrawableBuilder.build(String.valueOf(Na.charAt(0)), R.color.blue);
                            }
                            else
                            {
                                textDrawable1 = mDrawableBuilder.build(String.valueOf(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name").charAt(0)), R.color.blue);
                            }
                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name").matches("null"))
                            {
                                textDrawable2 = mDrawableBuilder.build(String.valueOf(Na.charAt(0)), R.color.blue);
                            }
                            else
                            {
                                textDrawable2 = mDrawableBuilder.build(String.valueOf(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name").charAt(0)), R.color.blue);
                            }


								Glide.with(StudentProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("picture")).placeholder(textDrawable).error(textDrawable).into(studentImage);
								Glide.with(StudentProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("father_pic")).placeholder(textDrawable1).error(textDrawable1).into(studentFatherImage);
								Glide.with(StudentProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_pic")).placeholder(textDrawable2).error(textDrawable2).into(studentMotherImage);

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name").matches("null"))
                            {
                                studentName.setText("-");
                            }

                            else
                            {
                                studentName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_roll_no").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("class_roll_no").matches("null"))
                            {
                                studentRollNumber.setText("-");
                            }

                            else
                            {
                                studentRollNumber.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_roll_no"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth").matches("null") || responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth").matches("0000-00-00"))
                            {
                                studentDob.setText("DOB "+"-");
                            }

                            else
                            {
                                studentDob2 = responseObject.getJSONArray("profile").getJSONObject(0).getString("date_of_birth");
                                try {
                                    studentDob1 = formatter.parse(studentDob2);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                                studentDob3 = formatter1.format(studentDob1);
                                studentDob.setText("Dob: "+studentDob3);

                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("admn_No").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("admn_No").matches("null"))
                            {
                                studentAdmissionNo.setText("Admn No: -");
                            }

                            else
                            {
                                studentAdmissionNo.setText("Admn No: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("admn_No"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("admission_date").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("admission_date").matches("null") || responseObject.getJSONArray("profile").getJSONObject(0).getString("admission_date").matches("0000-00-00"))
                            {
                                studentDate.setText("Admn Date: -");
                            }

                            else
                            {
                                studentAdmissionDate2 = responseObject.getJSONArray("profile").getJSONObject(0).getString("admission_date");
                                try {
                                    studentAdmissionDate1 = formatter.parse(studentAdmissionDate2);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                                studentAdmissionDate3 = formatter1.format(studentAdmissionDate1);
                                studentDate.setText("Admn Date: "+studentAdmissionDate3);

                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("gender").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("gender").matches("null"))
                            {
                                studentSex.setText("Gender: -");
                            }

                            else
                            {
                                studentSex.setText("Gender: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("gender"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("trade").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("trade").matches("null"))
                            {
                                studentTrade.setText("Trade: -");
                            }

                            else
                            {
                                studentTrade.setText("Trade: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("trade"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name").matches("null"))
                            {
                                studentClass.setText("Class: -");
                            }

                            else
                            {
                                studentClass.setText("Class: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("class_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name").matches("null"))
                            {
                                studentSecion.setText("Section: -");
                            }

                            else
                            {
                                studentSecion.setText("Section: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("section_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("stream").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("stream").matches("null"))
                            {
                                studentStream.setText("Stream: -");
                            }

                            else
                            {
                                studentStream.setText("Stream: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("stream"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_email").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_email").matches("null"))
                            {
                                studentEmail.setText("Email: -");
                            }

                            else
                            {
                                studentEmail.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("stu_email"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("school_name").matches("null"))
                            {
                                studentSchoolName.setText("-");
                            }

                            else
                            {
                                studentSchoolName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_address").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("school_address").matches("null"))
                            {
                                studentSchoolAddress.setText("-");
                            }

                            else
                            {
                                studentSchoolAddress.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("school_address"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name").matches("null"))
                            {
                                studentFatherName.setText("-");
                            }

                            else
                            {
                                studentFatherName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_dob").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("father_dob").matches("null") || responseObject.getJSONArray("profile").getJSONObject(0).getString("father_dob").matches("0000-00-00"))
                            {
                                studentFatherDob.setText("Dob: -");
                            }

                            else
                            {
                                studentFatherDob2 = responseObject.getJSONArray("profile").getJSONObject(0).getString("father_dob");
                                try {
                                    studentFatherDob1 = formatter.parse(studentFatherDob2);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                                studentFatherDob3 = formatter1.format(studentFatherDob1);
                                studentFatherDob.setText("Dob: "+studentFatherDob3);

                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_mob").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("father_mob").matches("null"))
                            {
                                studentfatherPhone.setText("Mobile: -");
                            }

                            else
                            {
                                studentfatherPhone.setText("Mobile: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("father_mob"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("father_email").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("father_email").matches("null"))
                            {
                                studentFatherEmail.setText("Email: -");
                            }

                            else
                            {
                                studentFatherEmail.setText("Email: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("father_email"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name").matches("null"))
                            {
                                studentMotherName.setText("-");
                            }

                            else
                            {
                                studentMotherName.setText(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_name"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_dob").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_dob").matches("null") || responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_dob").matches("0000-00-00"))
                            {
                                studentMotherDob.setText("Dob: -");
                            }

                            else
                            {
                                studentMotherDob2 = responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_dob");
                                try {
                                    studentMotherDob1 = formatter.parse(studentMotherDob2);
                                } catch (ParseException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }

                                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                                studentMotherDob3 = formatter1.format(studentMotherDob1);
                                studentMotherDob.setText("Dob: "+studentMotherDob3);

                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_mob").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_mob").matches("null"))
                            {
                                studentMotherPhone.setText("Mobile: -");
                            }

                            else
                            {
                                studentMotherPhone.setText("Mobile: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_mob"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_email").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_email").matches("null"))
                            {
                                studentMotherEmail.setText("Email: -");
                            }

                            else
                            {
                                studentMotherEmail.setText("Email: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("mother_email"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("religion").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("religion").matches("null"))
                            {
                                studentReligion.setText("Religion: -");
                            }

                            else
                            {
                                studentReligion.setText("Religion: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("religion"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl").matches("null"))
                            {
                                studentEws.setText("EWS: -");
                            }

                            else
                            {
                                studentEws.setText("EWS: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl").matches("null"))
                            {
                                studentEws.setText("EWS: -");
                            }

                            else
                            {
                                studentEws.setText("EWS: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("bpl"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("disability").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("disability").matches("null"))
                            {
                                studentDiasbility.setText("Disability: -");
                            }

                            else
                            {
                                studentDiasbility.setText("Disability: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("disability"));
                            }

                            /*if(responseObject.getJSONArray("profile").getJSONObject(0).getString("bus_route_no").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("bus_route_no").matches("null"))
                            {
                                studentBusNumber.setText("Bus Route No: -");
                            }

                            else
                            {
                                studentBusNumber.setText("Bus Route No: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("bus_route_no"));
                            }*/

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark").matches("null"))
                            {
                                studentBirthmark.setText("Birth Mark: -");
                            }

                            else
                            {
                                studentBirthmark.setText("Birth Mark: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("birth_mark"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("height").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("height").matches("null"))
                            {
                                studentHeight.setText("Height: -");
                            }

                            else
                            {
                                studentHeight.setText("Height: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("height"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("weight").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("weight").matches("null"))
                            {
                                studentWeight.setText("Weight: -");
                            }

                            else
                            {
                                studentWeight.setText("Weight: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("weight"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group").matches("null"))
                            {
                                studentBloodGroup.setText("Blood Group: -");
                            }

                            else
                            {
                                studentBloodGroup.setText("Blood Group: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("blood_group"));
                            }

                            if(responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies").matches("") || responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies").matches("null"))
                            {
                                studentalergies.setText("Alergies: -");
                            }

                            else
                            {
                                studentalergies.setText("Alergies: "+responseObject.getJSONArray("profile").getJSONObject(0).getString("allergies"));
                            }
                            /*

									*//*date1 = responseObject.getJSONArray("profile").getJSONObject(0).getString("user_dob");
									try {
										date7 = formatter.parse(date1);
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

									SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
									date2 = formatter1.format(date7);
									userFathersName.setText(date2);*//*











                           */






                        }


                    }
                    else
                        Utils.showToast(StudentProfile.this, "Error Fetching Response");
                    setSupportProgressBarIndeterminateVisibility(false);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentProfile.this, "Error fetching modules! Please try after sometime.");
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
        if(Utils.isNetworkAvailable(StudentProfile.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentProfile.this, "Unable to fetch data, kindly enable internet settings!");
        }

        //getUserProfile();

    }
}
