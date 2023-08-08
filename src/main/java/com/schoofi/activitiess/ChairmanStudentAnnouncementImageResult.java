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

import com.android.volley.Cache.Entry;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChairmanStudentAnnouncementImageResult extends AppCompatActivity {
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String institutionId = Preferences.getInstance().institutionId;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;
	ImageView back;


	String from,to;
	private int position;
	private ImageView imageBack,imageShow;
	private TextView title,date,description,senderName,save;
	private JSONArray chairmanStudentAnnouncementArray;
	String image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_announcement_image_result);
		/*Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student Announcement ImageResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());*/
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentAnnouncement ImageResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		position = getIntent().getExtras().getInt("position");
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imageShow = (ImageView) findViewById(R.id.imageView1);
		title = (TextView) findViewById(R.id.text_studentImageViewScreenTitle);
		date = (TextView) findViewById(R.id.text_studentImageViewScreenDate);
		description = (TextView) findViewById(R.id.text_studentImageViewScreenDescription);
		senderName = (TextView) findViewById(R.id.text_studentImageViewScreenSenderName);
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

		imageShow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChairmanStudentAnnouncementImageResult.this,TeacherStudentImageDetails.class);
				intent.putExtra("imageUrl", image);
				startActivity(intent);
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_announcement_image_result,
				menu);
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

	protected void initData()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(ChairmanStudentAnnouncementImageResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&inst_id="+institutionId+"&device_id="+Preferences.getInstance().deviceId);
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
				/*Picasso.with(ChairmanStudentAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.SERVER_URL+chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path")).placeholder(R.drawable.ic_launcher).
				error(R.drawable.ic_launcher).into(imageShow);*/
				image =chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path");

				Glide.with(ChairmanStudentAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.IMAGE_URL+chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(imageShow);
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
			e = VolleySingleton.getInstance(ChairmanStudentAnnouncementImageResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT_DATE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&inst_id="+institutionId+"&frm_date="+from+"&to_date="+to+"&device_id="+Preferences.getInstance().deviceId);
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

				image = chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path");
				senderName.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("full_name"));
				description.setText(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement"));
				Picasso.with(ChairmanStudentAnnouncementImageResult.this).load(AppConstants.SERVER_URLS.SERVER_URL+chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path")).placeholder(R.drawable.imagenotavailble).
				error(R.drawable.imagenotavailble).into(imageShow);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

	}
}
