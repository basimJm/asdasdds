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

public class ChairmanStudentAnnouncementVideoResult extends AppCompatActivity {
	
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String institutionId = Preferences.getInstance().institutionId;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String from,to;
	private int position;
	private ImageView imageBack,button;
	int count =1;
	private VideoView videoShow;
	private TextView title,date,description,senderName,save;
	
	private JSONArray teacherAnnouncementArray;
	private ProgressDialog pDialog;
	String videoUrl;
	private JSONArray chairmanStudentAnnouncementArray;
	String date1,date2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_video_view_announcement);
		
		/*Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Video AnnouncementResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());*/
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentAnnouncement VideoResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		position = getIntent().getExtras().getInt("position");
		imageBack = (ImageView) findViewById(R.id.img_back);
		imageBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		videoShow = (VideoView) findViewById(R.id.videoViewStudentAnnouncement);
		title = (TextView) findViewById(R.id.text_studentVideoViewScreenTitle);
		date = (TextView) findViewById(R.id.text_studentVideoViewScreenDate);
		description = (TextView) findViewById(R.id.text_studentVideoViewScreenDescription);
		senderName = (TextView) findViewById(R.id.text_studentVideoViewScreenSenderName);
		button = (ImageView) findViewById(R.id.playButton);
		
		from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
		
        if(from.matches("") || to.matches(""))
        {
        	initData();
        }

        else
        {
        	initData1();
        }

		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(ChairmanStudentAnnouncementVideoResult.this,StudentVideoView.class);
				intent.putExtra("url", videoUrl);
				startActivity(intent);
				
			}
		});
	}
	
	protected void initData()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(ChairmanStudentAnnouncementVideoResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&inst_id="+institutionId+"&device_id="+Preferences.getInstance().deviceId);
			chairmanStudentAnnouncementArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentAnnouncementArray!= null)
		{
			try {
				
				date1 = chairmanStudentAnnouncementArray.getJSONObject(position).getString("date");

				Date date3 = formatter.parse(date1);
				
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				date2 = formatter1.format(date3);
				//date.setText(date2);
				date.setVisibility(View.GONE);
				title.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_title"));
				//date.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("date"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				//senderName.setText(teacherAnnouncementArray.getJSONObject(position).getString("full_name"));
				senderName.setVisibility(View.GONE);
				description.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement"));
				videoUrl = chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void initData1()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(ChairmanStudentAnnouncementVideoResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT_DATE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&inst_id="+institutionId+"&frm_date="+from+"&to_date="+to+"&device_id="+Preferences.getInstance().deviceId);
			chairmanStudentAnnouncementArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentAnnouncementArray!= null)
		{
			try {

				date1 = chairmanStudentAnnouncementArray.getJSONObject(position).getString("date");

				Date date3 = formatter.parse(date1);
				
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				date2 = formatter1.format(date3);
				date.setText(date2);
				title.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_title"));
				//date.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("date"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				senderName.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("full_name"));
				description.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement"));
				videoUrl = chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}


}
