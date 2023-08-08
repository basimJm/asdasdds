package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {
	
	private EditText emailAddress,message;
	private Button submit;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Contact Us");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_contact_us);
		
		emailAddress = (EditText) findViewById(R.id.editEmailAddress);
		message = (EditText) findViewById(R.id.editMessage);
		submit = (Button) findViewById(R.id.btn_submit);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(emailAddress.getText().toString().matches("") || emailAddress.getText().toString().matches("null") || message.getText().toString().matches("") || message.getText().toString().matches("null"))
				{
					Utils.showToast(getApplicationContext(), "Please fill the fileds");
				}
				
				else
				{
					postMessage1();
				}
				
			}
		});
	}
	
	private void postMessage1()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CONTACT_US/*+"?email="+userEmailId+"&user_id="+userId+"&token="+token+"&name="+Preferences.getInstance().userName+"&crr_date="+currentDate+"&stu_id="+Preferences.getInstance().studentId+"&feed_type="+codeId+"&message="+feedback.getText().toString()*/;

		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(ContactUs.this,"Error Submitting Comment");
						

					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ContactUs.this, "Session Expired:Please Login Again");
						}

						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{

								Utils.showToast(ContactUs.this,"Successfuly Submitted");
								finish();
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(ContactUs.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(ContactUs.this, "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(ContactUs.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("user_id",Preferences.getInstance().userId);
				params.put("email", emailAddress.getText().toString());
				params.put("token",Preferences.getInstance().token);
				params.put("message",message.getText().toString());
				params.put("device_id", Preferences.getInstance().deviceId);
				
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
		getMenuInflater().inflate(R.menu.contact_us, menu);
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
