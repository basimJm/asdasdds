package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class StudentVideoViewAnnouncement extends AppCompatActivity {
	
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String classId = Preferences.getInstance().studentClassId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	private int position;
	private ImageView imageBack,button;
	int count =1;
	private VideoView videoShow;
	private TextView title,date,description,senderName,save;
	private JSONArray studentAnnouncementArray;
	private ProgressDialog pDialog;
	String videoUrl;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_video_view_announcement);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student VideoView Announcement");

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
			e = VolleySingleton.getInstance(StudentVideoViewAnnouncement.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&cls_id=" +classId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId);
			studentAnnouncementArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentAnnouncementArray!= null)
		{
			try {
                date1 = studentAnnouncementArray.getJSONObject(position).getString("date");
                Date date3 = formatter.parse(date1);
            	
            	SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            	date2 = formatter1.format(date3);
				//date.setText(date2);
				date.setVisibility(View.GONE);
				title.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement_title"));
				//date.setText(studentAnnouncementArray.getJSONObject(position).getString("date"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				//senderName.setText(studentAnnouncementArray.getJSONObject(position).getString("full_name"));
				senderName.setVisibility(View.GONE);
				description.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement"));
				videoUrl = studentAnnouncementArray.getJSONObject(position).getString("image_path");
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
				
				Intent intent = new Intent(StudentVideoViewAnnouncement.this,StudentVideoView.class);
				intent.putExtra("url", videoUrl);
				startActivity(intent);
				
			}
		});
		
			
			/*Uri uri = Uri.parse(videoUrl);
			Intent i = new Intent("android.intent.action.VIEW");
			i.setDataAndType(uri, "video/*");
			startActivity(i);
		});
		
		}*/
				/*button.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(StudentVideoViewAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", videoUrl);
			            startActivity(intent);
						
					}
				});*/
		
				
				
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_video_view_announcement, menu);
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
