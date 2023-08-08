package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.FeedbackVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentNewFeedback extends AppCompatActivity {
	
	private TextView feedbackName,feedbackName1,feedbackEmail,feedbackEmail1,feedbackType,feedbackTitle;
	private EditText feedback;
	private Button send;
	private ImageView back;
	private Spinner type;
	JSONObject jsonobject;
	JSONArray jsonarray;
	String message;
	ProgressDialog mProgressDialog;
	String userEmailId = Preferences.getInstance().userEmailId;
	String userId = Preferences.getInstance().userId;
	String token = Preferences.getInstance().token;
	String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	ArrayList<String> types;
	ArrayList<FeedbackVO> code;
    String codeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_new_feedback);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student NewFeedBack");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		feedbackTitle = (TextView) findViewById(R.id.txt_feedBack);
		feedbackName = (TextView) findViewById(R.id.text_name);
		feedbackName1 = (TextView) findViewById(R.id.text_name1);
		feedbackEmail = (TextView) findViewById(R.id.text_email);
		type = (Spinner) findViewById(R.id.spinn_type);
		feedbackEmail1 = (TextView) findViewById(R.id.text_email1);
		feedback = (EditText) findViewById(R.id.feed);
		send = (Button) findViewById(R.id.btn_send);
		back = (ImageView) findViewById(R.id.img_back);
		//type = (Spinner) findViewById(R.id.spinn_type);
		feedbackName1.setText(Preferences.getInstance().userName);
		feedbackEmail1.setText(Preferences.getInstance().userEmailId);
		message = feedback.getText().toString();

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
				// TODO Auto-generated method stub
		if(Utils.isNetworkAvailable(StudentNewFeedback.this))
		{
				new DownloadJSON().execute();
		}
		
		else
		{
			Utils.showToast(getApplicationContext(), "Kindly enable internet services");
		}
				
				send.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(feedback.getText().toString().matches("") || codeId.matches("0"))
						{
							Utils.showToast(StudentNewFeedback.this, "Please fill all fields");
						}
						
						else
						{
						postMessage();
						Intent intent = new Intent(StudentNewFeedback.this,StudentFeedBack.class);
						startActivity(intent);
						finish();
						}
					}
				});
				
		
		
	}
	
	// Download JSON file AsyncTask
		private class DownloadJSON extends AsyncTask<Void, Void, Void> {
	 
			@Override
			protected Void doInBackground(Void... params) {
					
				
				// Create an array to populate the spinner 
				code = new ArrayList<FeedbackVO>();
				types = new ArrayList<String>();
				// JSON file URL address
				jsonobject = JSONfunctions
						.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STUDENT_FEEDBACK_TYPE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId);
				try {
					// Locate the NodeList name
					jsonarray = jsonobject.getJSONArray("Typelist");
					for (int i = 0; i < jsonarray.length(); i++) {
						jsonobject = jsonarray.getJSONObject(i);
						FeedbackVO feedbackVO = new FeedbackVO();
						feedbackVO.setCode(jsonobject.optString("code"));
						code.add(feedbackVO);
						types.add(jsonobject.optString("full_name"));
						
					}
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void args) {
				// Locate the spinner in activity_main.xml
				
				type
				.setAdapter(new ArrayAdapter<String>(StudentNewFeedback.this,
						android.R.layout.simple_spinner_dropdown_item,
						types));
				
				type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						
						codeId = code.get(position).getCode().toString();
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
					
					
				});
				
		
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_new_feedback, menu);
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
	
	private void postMessage()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		
		
			String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_NEW;
		
		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
						Utils.showToast(StudentNewFeedback.this,"Error Submitting Comment");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(StudentNewFeedback.this, "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
								
								Utils.showToast(StudentNewFeedback.this,"Successfuly Submitted Comment");
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentNewFeedback.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(StudentNewFeedback.this, "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(StudentNewFeedback.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("email", Preferences.getInstance().userEmailId);
				params.put("user_id", Preferences.getInstance().userId);
				params.put("token", Preferences.getInstance().token);
				params.put("name", Preferences.getInstance().userName);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("crr_date", currentDate);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("feed_type", codeId);
				params.put("message", feedback.getText().toString());
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
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
}
