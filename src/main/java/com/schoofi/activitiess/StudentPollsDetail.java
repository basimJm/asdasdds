package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
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
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentPollsDetail extends AppCompatActivity {

	private TextView screenTitle,question;
	private Button answer1,answer2,answer3,answer4,submit;
	private ImageView imageAnswer1,imageAnswer2,imageAnswer3,imageAnswer4,back;
	private int position;
	private JSONArray studentPollArray;
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userType = Preferences.getInstance().userType;
	int count,count1;
	boolean status;
	String quesId;
	String value;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_polls_detail);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student PollDetail");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		screenTitle = (TextView) findViewById(R.id.txt_PollsDetails);
		question = (TextView) findViewById(R.id.text_question);
		imageAnswer1 = (ImageView) findViewById(R.id.image_poll1);
		imageAnswer2 = (ImageView) findViewById(R.id.image_poll2);
		imageAnswer3 = (ImageView) findViewById(R.id.image_poll3);
		imageAnswer4 = (ImageView) findViewById(R.id.image_poll4);
		answer1 = (Button) findViewById(R.id.btn_pollOption1);
		answer2 = (Button) findViewById(R.id.btn_pollOption2);
		answer3 = (Button) findViewById(R.id.btn_pollOption3);
		answer4 = (Button) findViewById(R.id.btn_pollOption4);
		submit = (Button) findViewById(R.id.btn_submitPoll);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		position = getIntent().getExtras().getInt("position");

		try
		{
			Entry e;
			e = VolleySingleton.getInstance(StudentPollsDetail.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId);
			studentPollArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentPollArray!= null)
		{
			try {



				question.setText(studentPollArray.getJSONObject(position).getString("ques"));

				answer1.setText(studentPollArray.getJSONObject(position).getString("choice_a"));

				answer2.setText(studentPollArray.getJSONObject(position).getString("choice_b"));

				answer3.setText(studentPollArray.getJSONObject(position).getString("choice_c"));

				answer4.setText(studentPollArray.getJSONObject(position).getString("choice_d"));
				
				quesId = studentPollArray.getJSONObject(position).getString("q_id");
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				/*System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				if(studentLeaveListArray.getJSONObject(position).getString("status").equals("0"))
				{
					studentLeaveStatus.setText("Pending");
					studentLeaveStatus.setTextColor(getResources().getColor(R.color.orange));

				}
				else
					if(studentLeaveListArray.getJSONObject(position).getString("status").equals("1"))
					{
						studentLeaveStatus.setText("Approved");
						//studentLeaveStatus.setTextColor(Color.parseColor("F2F2F2"));

					}
					else
						if(studentLeaveListArray.getJSONObject(position).getString("status").equals("2"))
						{
							studentLeaveStatus.setText("Rejected");
							//studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));

						}
						else
						{   studentLeaveStatus.setText("Rejected");
						//studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));

						}


				Picasso.with(StudentLeaveDetail.this).load("http://schoofi.com/leave_uploads/"+studentLeaveListArray.getJSONObject(position).getString("attachment")).placeholder(R.drawable.ic_launcher).
				error(R.drawable.ic_action_back).into(imageLeaveDetail);	
				 */

				imageAnswer1.setVisibility(View.INVISIBLE);
				imageAnswer2.setVisibility(View.INVISIBLE);
				imageAnswer3.setVisibility(View.INVISIBLE);
				imageAnswer4.setVisibility(View.INVISIBLE);



			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

		answer1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageAnswer1.setVisibility(View.VISIBLE);
				imageAnswer2.setVisibility(View.INVISIBLE);
				imageAnswer3.setVisibility(View.INVISIBLE);
				imageAnswer4.setVisibility(View.INVISIBLE);
				//Utils.showToast(getApplicationContext(), answer1.getText().toString());
				count = 0;
				status = true;
				value = answer1.getText().toString();
				count1=0;

			}
		});

		answer2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				imageAnswer1.setVisibility(View.INVISIBLE);
				imageAnswer2.setVisibility(View.VISIBLE);
				imageAnswer3.setVisibility(View.INVISIBLE);
				imageAnswer4.setVisibility(View.INVISIBLE);
				//Utils.showToast(getApplicationContext(), answer2.getText().toString());
				count = 1;
				status = true;
				value = answer2.getText().toString();
				count1=1;

			}
		});

		answer3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				imageAnswer1.setVisibility(View.INVISIBLE);
				imageAnswer2.setVisibility(View.INVISIBLE);
				imageAnswer3.setVisibility(View.VISIBLE);
				imageAnswer4.setVisibility(View.INVISIBLE);
				//Utils.showToast(getApplicationContext(), answer3.getText().toString());
				count = 2;
				status = true;
				count1=2;
				value = answer3.getText().toString();

			}
		});
		
		answer4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				imageAnswer1.setVisibility(View.INVISIBLE);
				imageAnswer2.setVisibility(View.INVISIBLE);
				imageAnswer3.setVisibility(View.INVISIBLE);
				imageAnswer4.setVisibility(View.VISIBLE);
				//Utils.showToast(getApplicationContext(), answer4.getText().toString());
				count = 3;
				status = true;
				value = answer4.getText().toString();
				count1 = 3;

			}
		});
		
		
		
		/*if(count == 0)
		{
			value = answer1.getText().toString();
			//System.out.println(value);
		}
		else
			if(count == 1)
			{
				value = answer2.getText().toString();
				//System.out.println(value);
			}
		
			else
				if(count == 2)
				{
					value = answer3.getText().toString();
				}
		
				else
					if(count == 3)
					{
						value = answer4.getText().toString();
					}
					else
						
					{
						value = "4";
					}*/
				
		
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				System.out.println(count);
				if(status == false)
				{
				
					Utils.showToast(StudentPollsDetail.this, "select the option");
					
					
				}
				
				
				else
				{
				
					postPoll();
					//finish();
				}
					
				
				
			}
		});
					
		
		


	}
	
	private void postPoll()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		
		
			String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_SUBMIT_POLL;
		
		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						
						Utils.showToast(StudentPollsDetail.this,"Error Submitting Comment");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(StudentPollsDetail.this, "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
								
								Utils.showToast(StudentPollsDetail.this,"Successfuly Submitted Comment");
								finish();
								
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentPollsDetail.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(StudentPollsDetail.this, "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(StudentPollsDetail.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("token", Preferences.getInstance().token);
				params.put("ques_id", quesId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("u_email_id", Preferences.getInstance().userEmailId);
				params.put("user_action", String.valueOf(count));
				params.put("value", value);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_polls_detail, menu);
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
