package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.schoofi.utils.ExpandableHeightListView;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Cache;
import com.bumptech.glide.Glide;
import com.schoofi.adapters.ChairmanTeacherProfileSubjectsAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EmployeeDirectoryProfile extends AppCompatActivity {

    private ImageView back,profileImage;
    private int position;
    String date1,date2,date3,date4;
    Date date5,date6;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private JSONArray teacherDirectoryArray;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ExpandableHeightListView teacherSubjectListview;
    JSONArray jsonArray,jsonArray1,jsonArray2;
    JSONObject jsonObject,jsonObject1,jsonObject2;
    ArrayList<String> classTeacher;
    ArrayList<String> classCoordinator;
    private ChairmanTeacherProfileSubjectsAdapter chairmanTeacherProfileSubjectsAdapter;
    String value,dept_id;
    Button addresses;

    private TextView employeeDepartment,employeeCode,newAddmissionAnalysis,teacherName,teacherDesignation,teacherEmailAddress,teacherExperience,teacherQualification,teacherGender,teacherDOB,teacherDOJ,teacherAddress,teacherContactNO,teacherClassTeacher,teacherClassCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.employee_directory_profile);

        position = getIntent().getExtras().getInt("position");

        value = getIntent().getStringExtra("value");
        if(value.matches("2"))
        {
            dept_id = getIntent().getStringExtra("dept_id");
        }

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileImage = (ImageView) findViewById(R.id.img_teacher_profile);
        teacherName = (TextView) findViewById(R.id.text_teacher_name1);
        employeeDepartment = (TextView) findViewById(R.id.text_department_name1);
        teacherEmailAddress = (TextView) findViewById(R.id.text_teacher_email1);
        teacherExperience = (TextView) findViewById(R.id.text_teacher_experience1);
        teacherQualification = (TextView) findViewById(R.id.text_teacher_qualification1);
        teacherGender = (TextView) findViewById(R.id.text_teacher_gender1);
        teacherDOB = (TextView) findViewById(R.id.text_teacher_dob1);
        teacherDOJ = (TextView) findViewById(R.id.text_teacher_doj1);
        teacherAddress = (TextView) findViewById(R.id.text_teacher_address1);
        teacherContactNO = (TextView) findViewById(R.id.text_teacher_contact1);
        teacherDesignation = (TextView) findViewById(R.id.text_teacher_designation1);
        employeeCode = (TextView) findViewById(R.id.text_employee_code1);
        addresses = (Button) findViewById(R.id.text_button);

        addresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(EmployeeDirectoryProfile.this,EmployeeAddressesScreen.class);
                    intent.putExtra("emp_id",teacherDirectoryArray.getJSONObject(position).getString("emp_id"));
                    Log.d("pp",teacherDirectoryArray.getJSONObject(position).getString("emp_id"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//        teacherClassTeacher = (TextView) findViewById(R.id.text_teacher_class_teaher1);
//        teacherClassCoordinator = (TextView) findViewById(R.id.text_teacher_coordinater_teaher1);
//        teacherSubjectListview = (ExpandableHeightListView) findViewById(R.id.listView_student_daily_attendance);
//
//        teacherSubjectListview.setExpanded(true);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(EmployeeDirectoryProfile.this,TeacherStudentImageDetails.class);
                    intent.putExtra("imageUrl",teacherDirectoryArray.getJSONObject(position).getString("emp_image"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

if(value.matches("2"))
{
    initData1();
}
else {
    initData();
}
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_EMPLOYEE_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"1");
            if(e == null)
            {
                teacherDirectoryArray= null;
            }
            else
            {
                teacherDirectoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherDirectoryArray!= null)
        {

            try {
                mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(teacherDirectoryArray.getJSONObject(position).getString("emp_first_name").charAt(0)), R.color.blue);
                Glide.with(EmployeeDirectoryProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherDirectoryArray.getJSONObject(position).getString("emp_image")).placeholder(textDrawable).error(textDrawable).into(profileImage);
                date1 = teacherDirectoryArray.getJSONObject(position).getString("employee_dob");
                date3 = teacherDirectoryArray.getJSONObject(position).getString("employee_doj");

                try {
                    date5 = formatter.parse(date1);
                    date6 = formatter.parse(date3);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2= formatter1.format(date5);
                date4 =formatter1.format(date6);

                teacherDOB.setText(date2);
                teacherDOJ.setText(date4);
                String emp_first_name,emp_middle_name,emp_last_name;
                emp_first_name = teacherDirectoryArray.getJSONObject(position).getString("emp_first_name");
                emp_middle_name = teacherDirectoryArray.getJSONObject(position).getString("emp_middle_name");
                emp_last_name = teacherDirectoryArray.getJSONObject(position).getString("emp_last_name");

                if(emp_first_name.matches("") || emp_first_name.matches("null"))
                {
                    emp_first_name = "";
                }
                else
                {
                    emp_first_name = teacherDirectoryArray.getJSONObject(position).getString("emp_first_name");
                }

                if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                {
                    emp_middle_name = "";
                }
                else
                {
                    emp_middle_name = " "+teacherDirectoryArray.getJSONObject(position).getString("emp_middle_name");
                }
                if(emp_last_name.matches("") || emp_last_name.matches("null"))
                {
                    emp_last_name ="";
                }
                else
                {
                    emp_last_name = " "+teacherDirectoryArray.getJSONObject(position).getString("emp_last_name");
                }

                 teacherName.setText(emp_first_name+emp_middle_name+emp_last_name);

                if(teacherDirectoryArray.getJSONObject(position).getString("employee_personal_email").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_personal_email").matches("null"))
                {

                }
                else
                {
                    teacherEmailAddress.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_personal_email"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("employee_experience").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_experience").matches("null"))
                {
                    teacherExperience.setText("");
                }

                else
                {
                    teacherExperience.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_experience"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("employee_gender").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_gender").matches("null"))
                {
                    teacherGender.setText("");
                }

                else
                {
                    teacherGender.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_gender"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("dept_name").matches("") || teacherDirectoryArray.getJSONObject(position).getString("dept_name").matches("null"))
                {
                    employeeDepartment.setText("");
                }

                else
                {
                    employeeDepartment.setText(teacherDirectoryArray.getJSONObject(position).getString("dept_name"));
                }



                if(teacherDirectoryArray.getJSONObject(position).getString("employee_mobile_no").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_mobile_no").matches("null"))
                {
                    teacherContactNO.setText("");
                }

                else
                {
                    teacherContactNO.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_mobile_no"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("highest_qualification").matches("") || teacherDirectoryArray.getJSONObject(position).getString("highest_qualification").matches("null"))
                {
                    teacherQualification.setText("");
                }

                else
                {
                    teacherQualification.setText(teacherDirectoryArray.getJSONObject(position).getString("highest_qualification"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("designation").matches("") || teacherDirectoryArray.getJSONObject(position).getString("designation").matches("null"))
                {
                    teacherDesignation.setText("");
                }

                else
                {
                    teacherDesignation.setText(teacherDirectoryArray.getJSONObject(position).getString("designation"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("emp_code").matches("") || teacherDirectoryArray.getJSONObject(position).getString("emp_code").matches("null"))
                {
                    employeeCode.setText("");
                }

                else
                {
                    employeeCode.setText(teacherDirectoryArray.getJSONObject(position).getString("emp_code"));
                }

                teacherAddress.setText(teacherDirectoryArray.getJSONObject(position).getString("address_line1")+" "+teacherDirectoryArray.getJSONObject(position).getString("address_line2")+" "+teacherDirectoryArray.getJSONObject(position).getString("locality")+" "+teacherDirectoryArray.getJSONObject(position).getString("city")+" "+teacherDirectoryArray.getJSONObject(position).getString("zipcode")+" "+teacherDirectoryArray.getJSONObject(position).getString("state"));





                // System.out.print(jsonArray.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADMIN_EMPLOYEE_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+"&dept_id="+dept_id);
            if(e == null)
            {
                teacherDirectoryArray= null;
            }
            else
            {
                teacherDirectoryArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherDirectoryArray!= null)
        {

            try {
                mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(teacherDirectoryArray.getJSONObject(position).getString("emp_first_name").charAt(0)), R.color.blue);
                Glide.with(EmployeeDirectoryProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherDirectoryArray.getJSONObject(position).getString("emp_image")).placeholder(textDrawable).error(textDrawable).into(profileImage);
                date1 = teacherDirectoryArray.getJSONObject(position).getString("employee_dob");
                date3 = teacherDirectoryArray.getJSONObject(position).getString("employee_doj");

                try {
                    date5 = formatter.parse(date1);
                    date6 = formatter.parse(date3);

                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2= formatter1.format(date5);
                date4 =formatter1.format(date6);

                teacherDOB.setText(date2);
                teacherDOJ.setText(date4);
                String emp_first_name,emp_middle_name,emp_last_name;
                emp_first_name = teacherDirectoryArray.getJSONObject(position).getString("emp_first_name");
                emp_middle_name = teacherDirectoryArray.getJSONObject(position).getString("emp_middle_name");
                emp_last_name = teacherDirectoryArray.getJSONObject(position).getString("emp_last_name");

                if(emp_first_name.matches("") || emp_first_name.matches("null"))
                {
                    emp_first_name = "";
                }
                else
                {
                    emp_first_name = teacherDirectoryArray.getJSONObject(position).getString("emp_first_name");
                }

                if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                {
                    emp_middle_name = "";
                }
                else
                {
                    emp_middle_name = " "+teacherDirectoryArray.getJSONObject(position).getString("emp_middle_name");
                }
                if(emp_last_name.matches("") || emp_last_name.matches("null"))
                {
                    emp_last_name ="";
                }
                else
                {
                    emp_last_name = " "+teacherDirectoryArray.getJSONObject(position).getString("emp_last_name");
                }

                teacherName.setText(emp_first_name+emp_middle_name+emp_last_name);

                if(teacherDirectoryArray.getJSONObject(position).getString("employee_personal_email").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_personal_email").matches("null"))
                {

                }
                else
                {
                    teacherEmailAddress.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_personal_email"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("employee_experience").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_experience").matches("null"))
                {
                    teacherExperience.setText("");
                }

                else
                {
                    teacherExperience.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_experience"));
                }


                if(teacherDirectoryArray.getJSONObject(position).getString("dept_name").matches("") || teacherDirectoryArray.getJSONObject(position).getString("dept_name").matches("null"))
                {
                    employeeDepartment.setText("");
                }

                else
                {
                    employeeDepartment.setText(teacherDirectoryArray.getJSONObject(position).getString("dept_name"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("employee_gender").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_gender").matches("null"))
                {
                    teacherGender.setText("");
                }

                else
                {
                    teacherGender.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_gender"));
                }



                if(teacherDirectoryArray.getJSONObject(position).getString("employee_mobile_no").matches("") || teacherDirectoryArray.getJSONObject(position).getString("employee_mobile_no").matches("null"))
                {
                    teacherContactNO.setText("");
                }

                else
                {
                    teacherContactNO.setText(teacherDirectoryArray.getJSONObject(position).getString("employee_mobile_no"));
                }
                if(teacherDirectoryArray.getJSONObject(position).getString("highest_qualification").matches("") || teacherDirectoryArray.getJSONObject(position).getString("highest_qualification").matches("null"))
                {
                    teacherQualification.setText("");
                }

                else
                {
                    teacherQualification.setText(teacherDirectoryArray.getJSONObject(position).getString("highest_qualification"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("designation").matches("") || teacherDirectoryArray.getJSONObject(position).getString("designation").matches("null"))
                {
                    teacherDesignation.setText("");
                }

                else
                {
                    teacherDesignation.setText(teacherDirectoryArray.getJSONObject(position).getString("designation"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("emp_code").matches("") || teacherDirectoryArray.getJSONObject(position).getString("emp_code").matches("null"))
                {
                    employeeCode.setText("");
                }

                else
                {
                    employeeCode.setText(teacherDirectoryArray.getJSONObject(position).getString("emp_code"));
                }


                    teacherAddress.setText(teacherDirectoryArray.getJSONObject(position).getString("address_line1")+" "+teacherDirectoryArray.getJSONObject(position).getString("address_line2")+" "+teacherDirectoryArray.getJSONObject(position).getString("locality")+" "+teacherDirectoryArray.getJSONObject(position).getString("city")+" "+teacherDirectoryArray.getJSONObject(position).getString("zipcode")+" "+teacherDirectoryArray.getJSONObject(position).getString("state"));





                // System.out.print(jsonArray.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
