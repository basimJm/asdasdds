package com.schoofi.activitiess;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

public class TeacherScheduleDetails1 extends AppCompatActivity {

    private JSONArray studentTimeTableArray;
    private int position;
    private TextView teacherName,teacherName1,lectureStartingTime,lectureStartingTime1,lectureEndingTime,lectureEndingTime1;
    private ImageView backButton;
    String date;
    String userEmailId = Preferences.getInstance().userEmailId;
    String userId = Preferences.getInstance().userId;
    String token = Preferences.getInstance().token;
    String studentId = Preferences.getInstance().studentId;
    String sectionId;
    String classId;
    String schoolId = Preferences.getInstance().schoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_student_time_table_details);
        position = getIntent().getExtras().getInt("position");
        date = getIntent().getExtras().getString("date");
        sectionId = getIntent().getStringExtra("sec_id");
        classId = getIntent().getStringExtra("cls_id");
        backButton =(ImageView) findViewById(R.id.img_back);
        teacherName = (TextView) findViewById(R.id.text_teacherName);
        teacherName1 = (TextView) findViewById(R.id.text_teacherName1);
        lectureStartingTime = (TextView) findViewById(R.id.text_lectureStartingTime);
        lectureStartingTime1 = (TextView) findViewById(R.id.text_lectureStartingTime1);
        lectureEndingTime = (TextView) findViewById(R.id.text_lectureEndingTime);
        lectureEndingTime1 = (TextView) findViewById(R.id.text_lectureEndingTime1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(Preferences.getInstance().schoolType.matches("College"))
        {
            try
            {
                Cache.Entry e;
                e = VolleySingleton.getInstance(TeacherScheduleDetails1.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_SCHEDULE + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
                studentTimeTableArray= new JSONArray(new String(e.data));
                //System.out.println(studentLeaveListArray);
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            if(studentTimeTableArray!= null)
            {
                try {


                    teacherName1.setText(studentTimeTableArray.getJSONObject(position).getString("teacher"));
                    lectureStartingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("period_start_time"));
                    //System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
                    //System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
                    lectureEndingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("period_end_time"));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        else {


            try {
                Cache.Entry e;
                e = VolleySingleton.getInstance(TeacherScheduleDetails1.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TIME_TABLE_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&stu_id=" + studentId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&cls_id=" + classId + "&date=" + date + "&device_id=" + Preferences.getInstance().deviceId);
                studentTimeTableArray = new JSONArray(new String(e.data));
                //System.out.println(studentLeaveListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (studentTimeTableArray != null) {
                try {


                    teacherName1.setText(studentTimeTableArray.getJSONObject(position).getString("teacher"));
                    lectureStartingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("period_start_time"));
                    //System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
                    //System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
                    lectureEndingTime1.setText(studentTimeTableArray.getJSONObject(position).getString("period_end_time"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_time_table_details, menu);
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
}
