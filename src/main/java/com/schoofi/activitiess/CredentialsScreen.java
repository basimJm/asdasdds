package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class CredentialsScreen extends AppCompatActivity {

	private TextView screenTitle;
	private EditText userName,userPass,userRePass;
	private Button saveCredentials;
	
	String username1,userPass1,userRePass1;
    String email,mobile;
	//String email = "harsh";
	//String mobile = "harsh";
	String btn_reg = "1";
	int count;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_credentials_screen);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Credentials Screen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		screenTitle = (TextView) findViewById(R.id.txt_credentials1);
		userName = (EditText) findViewById(R.id.edit_userName);
		userPass  = (EditText) findViewById(R.id.edit_userPassword);
		userRePass = (EditText) findViewById(R.id.edit_userRePassword);
		saveCredentials = (Button) findViewById(R.id.btn_saveCredentials);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		

		email = getIntent().getStringExtra("email");
		mobile = getIntent().getStringExtra("mobile");



		saveCredentials.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				

				/*if(username1.matches("") || userPass1.matches("") || userRePass1.matches(""))
				{
					count = 1;
					
				}

				else
					if(!userPass1.equals(userRePass1))
					{
						count =2;
					}

					else
						if(email.matches(""))
						{
							count = 3;
						}
						else
							if(mobile.matches(""))
							{
								count = 4;
							}
							else{
								count = 0;
							}*/
				username1 = userName.getText().toString();
				userPass1 = userPass.getText().toString();
				userRePass1 = userRePass.getText().toString();
			

				if(username1.matches("") || userPass1.matches("") || userRePass1.matches(""))
				{
					Utils.showToast(CredentialsScreen.this, "Fields cannot be left blank");
					//System.out.println(username1);
				}

				else 
					if(!userPass1.equals(userRePass1))
					{
						Utils.showToast(CredentialsScreen.this, "Passwords don't match");
					}

					
							else
							{
								SaveCredentials();
							}

			}
		});





	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.credentials_screen, menu);
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

	private void SaveCredentials()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(CredentialsScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		//email1 =email.getText().toString();
		//mobile1 = mobile.getText().toString();
		//System.out.println("username: " + email1);
		//System.out.println("userPass: "+ mobile1);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_REGISTRATION_SCREEN_2+"?email="+email+"&pass="+userPass1+"&u_name="+username1+"&mob="+mobile+"&btn_reg="+btn_reg;
		//System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.GET,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				try 
				{
					toa();
					responseObject = new JSONObject(response);
					//System.out.println(responseObject.toString());
					if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
						Toast.makeText(CredentialsScreen.this, "Error", Toast.LENGTH_SHORT).show();
					else
						if(responseObject.has("Msg") && responseObject.getString("Msg").equals("1"))
						{
							Utils.showToast(CredentialsScreen.this, "Successful");
							Intent intent = new Intent(CredentialsScreen.this,CredentialsScreen1.class);
							intent.putExtra("email", email);
							intent.putExtra("mobile", mobile);
							startActivity(intent);
						}

						else
							Utils.showToast(CredentialsScreen.this, "Try after Sometimes!!!");



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(CredentialsScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(CredentialsScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
					setSupportProgressBarIndeterminateVisibility(false);
					dialog.dismiss();
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("user_Name",userName);
				params.put("user_Pass",userPass);
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

	private void SaveCredentials1()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(CredentialsScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		//email1 =email.getText().toString();
		//mobile1 = mobile.getText().toString();
		//System.out.println("username: " + email1);
		//System.out.println("userPass: "+ mobile1);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_REGISTRATION_SCREEN_2+"?email="+email+"&pass="+userPass1+"&u_name="+username1+"&btn_reg="+btn_reg+"&mob="+mobile;
		//System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.GET,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				try 
				{
					toa();
					responseObject = new JSONObject(response);
					//System.out.println(responseObject.toString());
					if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
						Toast.makeText(CredentialsScreen.this, "Error", Toast.LENGTH_SHORT).show();
					else
						if(responseObject.has("Msg") && responseObject.getString("Msg").equals("1"))
						{
							Utils.showToast(CredentialsScreen.this, "Successful");
							Intent intent = new Intent(CredentialsScreen.this,CredentialsScreen1.class);
							intent.putExtra("email", email);
							intent.putExtra("mobile", mobile);
							startActivity(intent);
						}

						else
							Utils.showToast(CredentialsScreen.this, "Try after Sometimes!!!");



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(CredentialsScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(CredentialsScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
					setSupportProgressBarIndeterminateVisibility(false);
					dialog.dismiss();
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("user_Name",userName);
				params.put("user_Pass",userPass);
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

	private void SaveCredentials2()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(CredentialsScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		//email1 =email.getText().toString();
		//mobile1 = mobile.getText().toString();
		//System.out.println("username: " + email1);
		//System.out.println("userPass: "+ mobile1);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_REGISTRATION_SCREEN_2+"?pass="+userPass1+"&u_name="+username1+"&btn_reg="+btn_reg;
		//System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.GET,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				try 
				{
					toa();
					responseObject = new JSONObject(response);
					//System.out.println(responseObject.toString());
					if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
						Toast.makeText(CredentialsScreen.this, "Error", Toast.LENGTH_SHORT).show();
					else
						if(responseObject.has("Msg") && responseObject.getString("Msg").equals("1"))
						{
							Utils.showToast(CredentialsScreen.this, "Successful");
							Intent intent = new Intent(CredentialsScreen.this,CredentialsScreen1.class);
							intent.putExtra("email", email);
							intent.putExtra("mobile", mobile);
							startActivity(intent);
						}

						else
							Utils.showToast(CredentialsScreen.this, "Try after Sometimes!!!");



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(CredentialsScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(CredentialsScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
					setSupportProgressBarIndeterminateVisibility(false);
					dialog.dismiss();
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("user_Name",userName);
				params.put("user_Pass",userPass);
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

	public void toa()
	{
		System.out.println("hjhhjhjhj");
	}
}
