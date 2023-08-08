package com.schoofi.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.schoofi.activitiess.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activitiess.ChairmanHomeScreen;
import com.schoofi.activitiess.CredentialsScreen;
import com.schoofi.activitiess.ParentHomeScreen;
import com.schoofi.activitiess.StudentAssignments;
import com.schoofi.activitiess.WebViewActivity;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpScreen extends AppCompatActivity {

	private TextView screenTitle,title,line1,line2,line3,line4;
	private EditText mobile,email;
	private CheckBox checkBox;
	private Button done;
	String mobile1,email1;
	int count =0;
	int count1 = 0;
	String mobile2 = "";
	String email2 = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_sign_up_screen);
		/*Tracker t = ((SchoofiApplication) this.getApplication()).getTracker(
				TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("SignUp Screen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());*/
		screenTitle = (TextView) findViewById(R.id.txt_credentials);
		title = (TextView) findViewById(R.id.txt_title);
		line1 = (TextView) findViewById(R.id.txt_line1);
		line2 = (TextView) findViewById(R.id.txt_line2);
		line3 = (TextView) findViewById(R.id.txt_line3);
		line4 = (TextView) findViewById(R.id.text_line4);
		mobile = (EditText) findViewById(R.id.edit_userMobile);
		email = (EditText) findViewById(R.id.edit_userEmail);
		done = (Button) findViewById(R.id.btn_done);
		checkBox = (CheckBox) findViewById(R.id.checkbox_credentilas);

		line2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(SignUpScreen.this,WebViewActivity.class);
				String url = "http://www.schoofi.com/terms.php";
				intent.putExtra("url",url);
				startActivity(intent);





			}
		});

		line4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(SignUpScreen.this,WebViewActivity.class);
				String url = "http://www.schoofi.com/privacy.php";
				intent.putExtra("url",url);
				startActivity(intent);





			}
		});
		//mobile1 = mobile.getText().toString();
		//email1 = email.getText().toString();

		/*if(mobile1 !=null && email1 != null)
		{
			email1 = email.getText().toString();
			System.out.println(email1);

			count1 =4;
		}
		else if(mobile1 != null && email1 == null)
		{
			count1 =3;
		}

		else if(email1 !=null && mobile1 == null)
		{
			count1 = 2;
		}

		else if(mobile1 == null && email1 == null) 
		{
			count1 =1;
		}

		else
		{
			count1=0;
		}
		 */

		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mobile1 = mobile.getText().toString();
				email1 = email.getText().toString();
				itemClicked(v);

				if (mobile1.matches("") || email1.matches("")) {
					Utils.showToast(getApplicationContext(), "Please fill all fields");
				}



				else
				{
					if (count == 0) {
						Utils.showToast(SignUpScreen.this, "Please accept terms and conditions");
					} else if (count == 1 && !mobile1.matches("") && !email1.matches("")) {
						signUp();
					} else if (count == 1 && !mobile1.matches("") && email1.matches("")) {
						Utils.showToast(SignUpScreen.this, "plz fill email");
					} else if (count == 1 && !email1.matches("") && mobile1.matches("")) {
						Utils.showToast(SignUpScreen.this, "plz fill mobile No");
					} else if (count == 1 && mobile1.matches("") && email1.matches("")) {
						Utils.showToast(SignUpScreen.this, "Plz fill atleast on field");
					} else {
						Utils.showToast(SignUpScreen.this, "hhhhhh");
					}

				}
			}
		});




	}

	public void itemClicked(View v) {
		//code to check if this checkbox is checked!
		if(checkBox.isChecked()){

			//Intent intent = new Intent(SignUpScreen.this,ParentHomeScreen.class);
			//startActivity(intent);
			count =1;

		}

		else
		{
			//Utils.showToast(SignUpScreen.this, "Please accept terms and conditions");
			count =0;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up_screen, menu);
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

	private void signUp1()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(SignUpScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		//email1 =email.getText().toString();
		//mobile1 = mobile.getText().toString();
		//System.out.println("username: " + email1);
		//System.out.println("userPass: "+ mobile1);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_SIGN_UP+"?email="+email1;
		System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.GET,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				try 
				{
					toa();
					responseObject = new JSONObject(response);
					System.out.println(responseObject.toString());
					if(responseObject.has("Status") && responseObject.getString("Status").equals("0"))
						Toast.makeText(SignUpScreen.this, "User Does not exist", Toast.LENGTH_SHORT).show();
					else
						if(responseObject.has("Status") && responseObject.getString("Status").equals("1"))
						{
							Utils.showToast(SignUpScreen.this, "Successful");
							Intent intent = new Intent(SignUpScreen.this,CredentialsScreen.class);
							intent.putExtra("email", email1);
							intent.putExtra("mobile", mobile2);
							startActivity(intent);
						}

						else
							if(responseObject.has("Status") && responseObject.getString("Status").equals("2"))
							{
								Utils.showToast(SignUpScreen.this, "Record already exists!! Please Login");
							}

							else
								Utils.showToast(SignUpScreen.this, "Try after Sometimes!!!");



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(SignUpScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(SignUpScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
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

	private void signUp()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(SignUpScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		//email1 =email.getText().toString();
		//mobile1 = mobile.getText().toString();
		//System.out.println("username: " + email1);
		//System.out.println("userPass: "+ mobile1);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_SIGN_UP/*+"?email="+email1+"&mob="+mobile1*/;
		System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.POST,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				try 
				{
					toa();
					responseObject = new JSONObject(response);
					System.out.println(responseObject.toString());
					if(responseObject.has("Status") && responseObject.getString("Status").equals("0"))
						Toast.makeText(SignUpScreen.this, "User Does not exist", Toast.LENGTH_SHORT).show();
					else
						if(responseObject.has("Status") && responseObject.getString("Status").equals("1"))
						{
							Utils.showToast(SignUpScreen.this, "Successful");
							Intent intent = new Intent(SignUpScreen.this,CredentialsScreen.class);
							intent.putExtra("email", email1);
							intent.putExtra("mobile", mobile1);
							startActivity(intent);
						}

						else
							if(responseObject.has("Status") && responseObject.getString("Status").equals("2"))
							{
								Utils.showToast(SignUpScreen.this, "Record already exists!! Please Login");
							}

							else
								Utils.showToast(SignUpScreen.this, "Try after Sometimes!!!");



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(SignUpScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(SignUpScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
					setSupportProgressBarIndeterminateVisibility(false);
					dialog.dismiss();
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("email",email1);
				params.put("mob",mobile1);
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

	private void signUp2()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(SignUpScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		//email1 =email.getText().toString();
		//mobile1 = mobile.getText().toString();
		//System.out.println("username: " + email1);
		//System.out.println("userPass: "+ mobile1);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.PARENT_SIGN_UP+"?mob="+mobile1;
		System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.GET,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				try 
				{
					toa();
					responseObject = new JSONObject(response);
					System.out.println(responseObject.toString());
					if(responseObject.has("Status") && responseObject.getString("Status").equals("0"))
						Toast.makeText(SignUpScreen.this, "User Does not exist", Toast.LENGTH_SHORT).show();
					else
						if(responseObject.has("Status") && responseObject.getString("Status").equals("1"))
						{
							Utils.showToast(SignUpScreen.this, "Successful");
							Intent intent = new Intent(SignUpScreen.this,CredentialsScreen.class);
							intent.putExtra("mobile", mobile1);
							intent.putExtra("email", email2);
							startActivity(intent);
						}

						else
							if(responseObject.has("Status") && responseObject.getString("Status").equals("2"))
							{
								Utils.showToast(SignUpScreen.this, "Record already exists!! Please Login");
							}

							else
								Utils.showToast(SignUpScreen.this, "Try after Sometimes!!!");



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(SignUpScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(SignUpScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
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

	public void toa(){
		System.out.println("sdkbksdbksd");
	}


}
