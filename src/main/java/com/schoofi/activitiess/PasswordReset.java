package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordReset extends AppCompatActivity {
	
	private TextView screenTitle,title;
	private EditText newPassword,confirmPassword;
	ImageView back;
	private Button done;
	String email,mobile,name,dob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Password Reset");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_password_reset);
		//email = getIntent().getStringExtra("userEmail");
		//mobile = getIntent().getStringExtra("userMob");
		name = getIntent().getStringExtra("userName");
		//dob = getIntent().getStringExtra("dob");
		
		screenTitle = (TextView) findViewById(R.id.txt_reset);
		title = (TextView) findViewById(R.id.text_title);
		newPassword = (EditText) findViewById(R.id.edit_userPass);
		confirmPassword = (EditText) findViewById(R.id.edit_userRePass);
		done = (Button) findViewById(R.id.btn_resetDone);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		done.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(newPassword.getText().toString().matches("") || confirmPassword.getText().toString().matches(""))
				{
					Utils.showToast(PasswordReset.this, "Please fill the fields");
				}
				
				else
					if(!newPassword.getText().toString().matches(confirmPassword.getText().toString()))
					{
						Utils.showToast(PasswordReset.this, "Password does not match");
					}
					else
					{
						postMessage();
					}
			}
		});
		
	}
	
	private void postMessage()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.RESET_PASSWORD+"?u_name="+name+"&new_pass="+newPassword.getText().toString();

		StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Status")&&responseObject.getString("Status").equals("1"))
					{

						Utils.showToast(getApplicationContext(),"Password Changed Succesfully");
						Intent intent = new Intent(PasswordReset.this,LoginScreen.class);
						
						startActivity(intent);

					}


					else

					{

						Utils.showToast(getApplicationContext(),"Error Occured");
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){

				Map<String,String> params = new HashMap<String, String>();

				params.put("mob",userMobile.getText().toString());
				params.put("email",userMobile.getText().toString());
				params.put("u_name", userName.getText().toString());
				params.put("dob", "2015-11-10");
				return params;
			}*/};		

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
		getMenuInflater().inflate(R.menu.password_reset, menu);
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
