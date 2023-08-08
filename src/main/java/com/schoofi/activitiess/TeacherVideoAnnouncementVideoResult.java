package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Cache.Entry;
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

public class TeacherVideoAnnouncementVideoResult extends AppCompatActivity{
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	private int position;
	private ImageView imageBack,button;
	int count =1;
	private VideoView videoShow;
	private TextView title,date,description,senderName,save;
	private JSONArray teacherAnnouncementArray;
	private ProgressDialog pDialog;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;
	String videoUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_video_view_announcement);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Video AnnouncementResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		position = getIntent().getExtras().getInt("position");
		imageBack = (ImageView) findViewById(R.id.img_back);
		videoShow = (VideoView) findViewById(R.id.videoViewStudentAnnouncement);
		title = (TextView) findViewById(R.id.text_studentVideoViewScreenTitle);
		date = (TextView) findViewById(R.id.text_studentVideoViewScreenDate);
		description = (TextView) findViewById(R.id.text_studentVideoViewScreenDescription);
		senderName = (TextView) findViewById(R.id.text_studentVideoViewScreenSenderName);
		button = (ImageView) findViewById(R.id.playButton);
		imageBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(TeacherVideoAnnouncementVideoResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
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
				description.setText(teacherAnnouncementArray.getJSONObject(position).getString("announcement"));
				videoUrl = teacherAnnouncementArray.getJSONObject(position).getString("image_path");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(TeacherVideoAnnouncementVideoResult.this,StudentVideoView.class);
				intent.putExtra("url", videoUrl);
				startActivity(intent);
				
			}
		});
	}

}
