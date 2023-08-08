package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.schoofi.utils.ExpandableHeightListView;
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


public class ChairmanTeacherProfile extends AppCompatActivity {

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

    private TextView teacherName,teacherEmailAddress,teacherExperience,teacherQualification,teacherGender,teacherDOB,teacherDOJ,teacherAddress,teacherContactNO,teacherClassTeacher,teacherClassCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_chairman_teacher_profile);

        position = getIntent().getExtras().getInt("position");

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileImage = (ImageView) findViewById(R.id.img_teacher_profile);
        teacherName = (TextView) findViewById(R.id.text_teacher_name1);
        teacherEmailAddress = (TextView) findViewById(R.id.text_teacher_email1);
        teacherExperience = (TextView) findViewById(R.id.text_teacher_experience1);
        teacherQualification = (TextView) findViewById(R.id.text_teacher_qualification1);
        teacherGender = (TextView) findViewById(R.id.text_teacher_gender1);
        teacherDOB = (TextView) findViewById(R.id.text_teacher_dob1);
        teacherDOJ = (TextView) findViewById(R.id.text_teacher_doj1);
        teacherAddress = (TextView) findViewById(R.id.text_teacher_address1);
        teacherContactNO = (TextView) findViewById(R.id.text_teacher_contact1);
        teacherClassTeacher = (TextView) findViewById(R.id.text_teacher_class_teaher1);
        teacherClassCoordinator = (TextView) findViewById(R.id.text_teacher_coordinater_teaher1);
        teacherSubjectListview = (ExpandableHeightListView) findViewById(R.id.listView_student_daily_attendance);
        teacherSubjectListview.setExpanded(true);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(ChairmanTeacherProfile.this,TeacherStudentImageDetails.class);
                    intent.putExtra("imageUrl",teacherDirectoryArray.getJSONObject(position).getString("image"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        initData();
    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
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
                TextDrawable textDrawable = mDrawableBuilder.build(String.valueOf(teacherDirectoryArray.getJSONObject(position).getString("teac_name").charAt(0)), R.color.blue);
                Glide.with(ChairmanTeacherProfile.this).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherDirectoryArray.getJSONObject(position).getString("image")).placeholder(textDrawable).error(textDrawable).into(profileImage);
                date1 = teacherDirectoryArray.getJSONObject(position).getString("date_birth");
                date3 = teacherDirectoryArray.getJSONObject(position).getString("joining_date");

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
                if(teacherDirectoryArray.getJSONObject(position).getString("teac_name").matches("null") || teacherDirectoryArray.getJSONObject(position).getString("teac_name").matches("") )
                {
                    teacherName.setText("");
                }
                else
                {
                    teacherName.setText(teacherDirectoryArray.getJSONObject(position).getString("teac_name"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("email").matches("") || teacherDirectoryArray.getJSONObject(position).getString("email").matches("null"))
                {

                }
                else
                {
                   teacherEmailAddress.setText(teacherDirectoryArray.getJSONObject(position).getString("email"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("experience").matches("") || teacherDirectoryArray.getJSONObject(position).getString("experience").matches("null"))
                {
                    teacherExperience.setText("");
                }

                else
                {
                    teacherExperience.setText(teacherDirectoryArray.getJSONObject(position).getString("experience"));
                }
                if(teacherDirectoryArray.getJSONObject(position).getString("qualification").matches("") || teacherDirectoryArray.getJSONObject(position).getString("qualification").matches("null"))
                {
                    teacherQualification.setText("");
                }

                else
                {
                    teacherQualification.setText(teacherDirectoryArray.getJSONObject(position).getString("qualification"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("gender").matches("") || teacherDirectoryArray.getJSONObject(position).getString("gender").matches("null"))
                {
                    teacherGender.setText("");
                }

                else
                {
                    teacherGender.setText(teacherDirectoryArray.getJSONObject(position).getString("gender"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("address1").matches("") || teacherDirectoryArray.getJSONObject(position).getString("address1").matches("null"))
                {
                    teacherAddress.setText("");
                }

                else
                {
                    teacherAddress.setText(teacherDirectoryArray.getJSONObject(position).getString("address1"));
                }

                if(teacherDirectoryArray.getJSONObject(position).getString("contact").matches("") || teacherDirectoryArray.getJSONObject(position).getString("contact").matches("null"))
                {
                    teacherContactNO.setText("");
                }

                else
                {
                    teacherContactNO.setText(teacherDirectoryArray.getJSONObject(position).getString("contact"));
                }

                jsonArray = teacherDirectoryArray.getJSONObject(position).getJSONArray("class_teacher");
                jsonArray1 = teacherDirectoryArray.getJSONObject(position).getJSONArray("coordinator");
                jsonArray2 = teacherDirectoryArray.getJSONObject(position).getJSONArray("teacher_subjects");

                System.out.println(jsonArray.toString());
                System.out.println(jsonArray1.toString());
                System.out.println(jsonArray2.toString());

                classTeacher = new ArrayList<String>();
                classCoordinator =new ArrayList<String>();


                if(jsonArray.length()<=0)
                {

                }

                else {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        classTeacher.add(i, jsonArray.getJSONObject(i).getString("class_name") + "-" + jsonArray.getJSONObject(i).getString("section_name"));
                    }

                    String array1 = classTeacher.toString();

                    teacherClassTeacher.setText(array1.substring(1, array1.length()-1));
                }

                if(jsonArray1.length()<=0) {

                }
                else
                {
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        classCoordinator.add(i, jsonArray1.getJSONObject(i).getString("class_name") + "-" + jsonArray1.getJSONObject(i).getString("section_name"));
                    }

                    String array2 = classCoordinator.toString();

                    teacherClassCoordinator.setText(array2.substring(1, array2.length()-1));
                }




                teacherSubjectListview.invalidateViews();
                chairmanTeacherProfileSubjectsAdapter = new ChairmanTeacherProfileSubjectsAdapter(ChairmanTeacherProfile.this, jsonArray2);
                teacherSubjectListview.setAdapter(chairmanTeacherProfileSubjectsAdapter);
                chairmanTeacherProfileSubjectsAdapter.notifyDataSetChanged();
               // System.out.print(jsonArray.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
