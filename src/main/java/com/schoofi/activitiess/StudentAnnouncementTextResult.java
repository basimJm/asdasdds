package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentAnnouncementTextResult extends AppCompatActivity {

    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String classId = Preferences.getInstance().studentClassId;
    String sectionId = Preferences.getInstance().studentSectionId;
    String userId = Preferences.getInstance().userId;
    String userEmailId = Preferences.getInstance().userEmailId;
    private int position;
    private ImageView imageBack,imageShow;
    private TextView title,date,description,senderName,save;
    private JSONArray studentAnnouncementArray;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String date1,date2;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student Announcement Text Result");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_announcement_text_result);

        position = getIntent().getExtras().getInt("position");
        imageBack = (ImageView) findViewById(R.id.img_back);
        //imageShow = (ImageView) findViewById(R.id.imageView1);
        title = (TextView) findViewById(R.id.text_studentImageViewScreenTitle);
        // date = (TextView) findViewById(R.id.text_studentImageViewScreenDate);
        description = (TextView) findViewById(R.id.text_studentImageViewScreenDescription);
        //senderName = (TextView) findViewById(R.id.text_studentImageViewScreenSenderName);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(StudentAnnouncementTextResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ANNOUNCEMENT_URL + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&cls_id=" + classId + "&sec_id=" + sectionId + "&sch_id=" + schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&stu_id=" + Preferences.getInstance().studentId + "&ins_id=" + Preferences.getInstance().institutionId);
            studentAnnouncementArray = new JSONArray(new String(e.data));
            //System.out.println(studentLeaveListArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (studentAnnouncementArray != null) {
            try {

                // date1 = studentAnnouncementArray.getJSONObject(position).getString("date");
                title.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement_title"));
                //date.setText(studentAnnouncementArray.getJSONObject(position).getString("date"));
                //System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
                //System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
                // senderName.setVisibility(View.GONE);
                //senderName.setText(studentAnnouncementArray.getJSONObject(position).getString("full_name"));
                description.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement"));
                /*Date date3 = formatter.parse(date1);

                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                date2 = formatter1.format(date3);
                //date.setText(date2);
                date.setVisibility(View.GONE);
				*//*Picasso.with(StudentAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.SERVER_URL+studentAnnouncementArray.getJSONObject(position).getString("image_path")).placeholder(R.drawable.ic_launcher).
				error(R.drawable.ic_launcher).into(imageShow);*//*
                Glide.with(StudentAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.IMAGE_URL+studentAnnouncementArray.getJSONObject(position).getString("image_path")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(imageShow);
                imageShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            image = studentAnnouncementArray.getJSONObject(position).getString("image_path");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(StudentAnnouncementImageResult.this,TeacherStudentImageDetails.class);
                        intent.putExtra("imageUrl",image);
                        startActivity(intent);
                    }
                });*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
