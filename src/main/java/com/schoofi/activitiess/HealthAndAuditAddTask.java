package com.schoofi.activitiess;

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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class HealthAndAuditAddTask extends AppCompatActivity {
	
	private EditText addTask;
	private Button addTaskButton,cancelTaskButton;
	ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Health And Audit AddTask");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_health_and_audit_add_task);
		
		addTask = (EditText) findViewById(R.id.edit_addTask);
		addTaskButton = (Button) findViewById(R.id.btn_submitTask);
		cancelTaskButton = (Button) findViewById(R.id.btn_cancelTask);
		
		addTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(addTask.getText().toString().matches(""))
				{
					Utils.showToast(getApplicationContext(), "Please fill the fields");
				}
				
				else
				{
					postEmergency();
				}
				
			}
		});

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		cancelTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_and_audit_add_task, menu);
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
	
	protected void postEmergency() 
	{
		final ProgressDialog loading = ProgressDialog.show(this,"Creating New Task","Please wait...",false,false);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.AUDIT_ADD_TASK;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						loading.dismiss();
						Utils.showToast(HealthAndAuditAddTask.this, "Please try again later");
						//finish();
					}
					else
						if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
					{
							loading.dismiss();
						Utils.showToast(HealthAndAuditAddTask.this, "Task  Created ");
						finish();
					}
					
						else
							if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
							{   
								loading.dismiss();
								Utils.showToast(HealthAndAuditAddTask.this, "Error creating task ");
								
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(HealthAndAuditAddTask.this, "Error submitting alert! Please try after sometime.");
				}
				loading.dismiss();

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(HealthAndAuditAddTask.this, "Error creating task! Please try after sometime.");
					loading.dismiss();
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(HealthAndAuditAddTask.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("u_id",Preferences.getInstance().userId);
				params.put("token", Preferences.getInstance().token);
				params.put("task", addTask.getText().toString());
				params.put("device_id", Preferences.getInstance().deviceId);
				//params.put("user_name", healthAndAuditAddUserFullName.getText().toString());
				//params.put("name", healthAndAuditAddUserUserName.getText().toString());
				params.put("ins_id", Preferences.getInstance().institutionId);
				//params.put("password" , healthAndAuditAddUserPassword.getText().toString());
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
				loading.dismiss();
			}
	}
}
