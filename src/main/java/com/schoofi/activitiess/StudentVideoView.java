package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VideoPlayView;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentVideoView extends AppCompatActivity {


	private VideoPlayView videoView;
	private ProgressDialog pDialog;
	String url;
	int mPos=0;
	int v=0;
	int remainingTime=0;
	int seconds=0;
	int seconds1=0;
	String tutorial_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_video_view);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student VideoView");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		videoView = (VideoPlayView) findViewById(R.id.myvideoview);

	url = getIntent().getExtras().getString("url");
	tutorial_id = getIntent().getStringExtra("tutorial_id");

		if(savedInstanceState!=null)
		{
			mPos=savedInstanceState.getInt("pos");
			//videoView.resume();
		}

		else {

			startVideo();
		}






	}

	@Override
	protected void onSaveInstanceState (Bundle outState){
		outState.putInt("pos", videoView.getCurrentPosition()); // save it here
	}

	protected void startVideo()
	{
		pDialog = new ProgressDialog(StudentVideoView.this);

		if(Utils.isNetworkAvailable(StudentVideoView.this))
		{

			// Set progressbar title
			// Set progressbar message
			pDialog.setMessage("Buffering...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			// Show progressbar
			pDialog.show();

			try {
				// Start the MediaController
				MediaController mediacontroller = new MediaController(StudentVideoView.this);
				mediacontroller.setAnchorView(videoView);
				// Get the URL from String VideoURL
				Uri video = Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+url);
				videoView.setMediaController(mediacontroller);
				videoView.setVideoURI(video);




			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				pDialog.dismiss();
				e.printStackTrace();
			}



			videoView.requestFocus();
			videoView.setOnPreparedListener(new OnPreparedListener() {
				// Close the progress bar and play the video


				@Override
				public void onPrepared(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					pDialog.dismiss();
					videoView.start();
					videoView.getDuration();
					Log.d("poo", String.valueOf(videoView.getDuration()));

				}
			});


		}

		else
		{
			Utils.showToast(StudentVideoView.this, "error");
		}

		videoView.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				pDialog.dismiss();
				return false;
			}
		});

		videoView.setPlayPauseListener(new VideoPlayView.PlayPauseListener() {
			@Override
			public void onPlay() {

			}

			@Override
			public void onPause() {
                   Log.d("po","ppoooo");
                   Log.d("oi", String.valueOf(videoView.getCurrentPosition()));
                   remainingTime = videoView.getDuration()-videoView.getCurrentPosition();
				   seconds = (int)((videoView.getCurrentPosition() / 1000) % 60);
				   seconds1 = (int)((videoView.getDuration() / 1000) % 60);
				Log.d("oi4", String.valueOf(seconds));
				postMessage1();
			}
		});


	}


	@Override
	protected void onResume() {
		videoView.resume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		videoView.suspend();

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		videoView.stopPlayback();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_video_view, menu);
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
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void postMessage1()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_UPDATE_VIDEO_STATUS/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(StudentVideoView.this,"Error Submitting Comment");


					}
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(StudentVideoView.this, "Session Expired:Please Login Again");
					}

					else
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
					{

						Utils.showToast(StudentVideoView.this,"Successfuly Submitted");
						finish();
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentVideoView.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error) {

				Utils.showToast(StudentVideoView.this, "Error submitting alert! Please try after sometime.");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(StudentVideoView.this);
				Map<String,String> params = new HashMap<String, String>();

				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("stu_id",Preferences.getInstance().studentId);
				params.put("tutorial_id",tutorial_id);
				params.put("total_duration",String.valueOf(seconds1));
				params.put("watched_duration",String.valueOf(seconds));




				return params;
			}};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("po","ppoooo");
		Log.d("oi", String.valueOf(videoView.getCurrentPosition()));
		remainingTime = videoView.getDuration()-videoView.getCurrentPosition();
		seconds = (int)((videoView.getCurrentPosition() / 1000) % 60);
		seconds1 = (int)((videoView.getDuration() / 1000) % 60);
		Log.d("oi4", String.valueOf(seconds));
		postMessage1();
	}
}
