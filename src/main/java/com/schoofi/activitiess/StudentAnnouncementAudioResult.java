package com.schoofi.activitiess;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentAnnouncementAudioResult extends AppCompatActivity {

	private SeekBar seekBar;
	private Button play,stop;
	private TextView title,description,senderName,date,screenTitle,save;
	private ImageView backButton;
	private final MediaPlayer mediaPlayer=new MediaPlayer(); 
	private int position;
	String url;
	String token = Preferences.getInstance().token;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String schoolId = Preferences.getInstance().schoolId;
	String classId = Preferences.getInstance().studentClassId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	Handler seekHandler = new Handler();
    String date1,date2;
	private JSONArray studentAnnouncementArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_announcement_audio_result);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student Announcement AudioResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		play = (Button) findViewById(R.id.audioStart);
		stop = (Button) findViewById(R.id.audioStop);
		title = (TextView) findViewById(R.id.text_studentAudioViewScreenTitle);
		date = (TextView) findViewById(R.id.text_studentAudioViewScreenDate);
		description = (TextView) findViewById(R.id.text_studentAudioViewScreenDescription);
		senderName = (TextView) findViewById(R.id.text_studentAudioViewScreenSenderName);
		backButton = (ImageView) findViewById(R.id.img_back);
		save = (TextView) findViewById(R.id.txt_save);

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		position = getIntent().getExtras().getInt("position");

		try
		{
			Entry e;
			e = VolleySingleton.getInstance(StudentAnnouncementAudioResult.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&cls_id=" +classId+"&sec_id="+sectionId+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&ins_id="+Preferences.getInstance().institutionId);
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
				title.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement_title"));
				//date.setText(date2);
				date.setVisibility(View.GONE);
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				//senderName.setText(studentAnnouncementArray.getJSONObject(position).getString("full_name"));
				senderName.setVisibility(View.GONE);
				description.setText(studentAnnouncementArray.getJSONObject(position).getString("announcement"));
				url = studentAnnouncementArray.getJSONObject(position).getString("image_path");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
				try {  
					mediaPlayer.setDataSource(AppConstants.SERVER_URLS.IMAGE_URL+url);

				} catch (IllegalArgumentException e) {  
					e.printStackTrace();  
				} catch (SecurityException e) {  
					e.printStackTrace();  
				} catch (IllegalStateException e) {  
					e.printStackTrace();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
				mediaPlayer.prepareAsync();  

				// You can show progress dialog here untill it prepared to play  
				mediaPlayer.setOnPreparedListener(new OnPreparedListener() {  
					@Override  
					public void onPrepared(MediaPlayer mediaPlayer) {  
						// Now dismis progress dialog, Media palyer will start playing 
						seekBar.setMax(mediaPlayer.getDuration());

						mediaPlayer.start();
						seekUpdation();
						seekBar.setEnabled(false);
						play.setEnabled(false);

					}  
				});  
				mediaPlayer.setOnErrorListener(new OnErrorListener() {  
					@Override  
					public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {  
						// dissmiss progress bar here. It will come here when  
						// MediaPlayer  
						// is not able to play file. You can show error message to user  
						return false;  
					}  
				}); 
				
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						mediaPlayer.stop();
						mediaPlayer.reset();
						seekBar.setProgress(0);
						seekBar.setEnabled(false);
						
					}
				});



			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mediaPlayer.stop();
				seekBar.setEnabled(false);

			}
		});







	}
	Runnable run = new Runnable() 
	{
		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void seekUpdation() 
	{
		seekBar.setProgress(mediaPlayer.getCurrentPosition());
		seekHandler.postDelayed(run, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_announcement_audio_result,
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mediaPlayer.stop();
		finish();
	}
}
