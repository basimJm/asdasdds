package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
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

public class TeacherAnnouncementImageResult extends AppCompatActivity {
	
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	private int position;
	private ImageView imageBack,imageShow;
	private TextView title,date,description,senderName,save;
	private JSONArray teacherAnnouncementArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;
	String image;

	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		
		setContentView(R.layout.activity_student_announcement_image_result);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Announcement ImageResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		position = getIntent().getExtras().getInt("position");
		imageBack = (ImageView) findViewById(R.id.img_back);
		imageShow = (ImageView) findViewById(R.id.imageView1);
		title = (TextView) findViewById(R.id.text_studentImageViewScreenTitle);
		date = (TextView) findViewById(R.id.text_studentImageViewScreenDate);
		description = (TextView) findViewById(R.id.text_studentImageViewScreenDescription);
		senderName = (TextView) findViewById(R.id.text_studentImageViewScreenSenderName);

		imageShow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeacherAnnouncementImageResult.this,TeacherStudentImageDetails.class);
				intent.putExtra("imageUrl", image);
				startActivity(intent);
			}
		});

		imageBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(TeacherAnnouncementImageResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
		    teacherAnnouncementArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherAnnouncementArray!= null)
		{
			try {

                date1 = teacherAnnouncementArray.getJSONObject(position).getString("date");
                Date date3 = formatter.parse(date1);
            	
            	SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            	date2 = formatter1.format(date3);
				//date.setText(date2);
				date.setVisibility(View.GONE);
				title.setText(teacherAnnouncementArray.getJSONObject(position).getString("announcement_title"));
				//date.setText(teacherAnnouncementArray.getJSONObject(position).getString("date"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				//senderName.setText(teacherAnnouncementArray.getJSONObject(position).getString("full_name"));
				senderName.setVisibility(View.GONE);
				image = teacherAnnouncementArray.getJSONObject(position).getString("image_path");
				description.setText(teacherAnnouncementArray.getJSONObject(position).getString("announcement"));
				/*Picasso.with(TeacherAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.SERVER_URL+teacherAnnouncementArray.getJSONObject(position).getString("image_path")).placeholder(R.drawable.ic_launcher).
				error(R.drawable.ic_launcher).into(imageShow);*/

				Glide.with(TeacherAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherAnnouncementArray.getJSONObject(position).getString("image_path")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(imageShow);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}

}
