package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonArray;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.SchoolNotificationListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentSettings extends AppCompatActivity {

	private ImageView back;
	private Button profile,aboutUs,logout,help,aboutSchool,changePassword,markLocation,linkedAccount,qrCode;
	private JSONArray schoolNotificationListArray;
	String url1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_settings);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student Settings");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		profile = (Button) findViewById(R.id.btn_profile);
		aboutUs = (Button) findViewById(R.id.btn_aboutUs);
		logout = (Button) findViewById(R.id.btn_Logout);
		help = (Button) findViewById(R.id.btn_help);
		aboutSchool = (Button) findViewById(R.id.btn_aboutSchool);
		back = (ImageView) findViewById(R.id.img_back);
		changePassword = (Button) findViewById(R.id.btn_changePassword);
		markLocation = (Button) findViewById(R.id.btn_mark_location);
		if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
		{
			markLocation.setVisibility(View.VISIBLE);
		}
		else
		{
			markLocation.setVisibility(View.GONE);
		}
		linkedAccount = (Button) findViewById(R.id.btn_linked_accounts);
		qrCode = (Button) findViewById(R.id.btn_qrcode);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		qrCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StudentSettings.this,QrCodeGenerator.class);
				startActivity(intent);
			}
		});
		
		aboutSchool.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Intent intent = new Intent(StudentSettings.this,AboutSchool.class);
				//startActivity(intent);
				getStudentFeedList();
				
			}
		});

		markLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StudentSettings.this,EditProfileActivity.class);
				startActivity(intent);
			}
		});

		changePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(StudentSettings.this,ChangePassword.class);
				startActivity(intent);

			}
		});
		
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://schoofi.com/help.docx"));
				//startActivity(intent);
				
				Intent intent = new Intent(StudentSettings.this,ContactUs.class);
				startActivity(intent);
				
			}
		});
		
		aboutUs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*
				Intent intent = new Intent(StudentSettings.this,AboutUs.class);

				startActivity(intent);*/

				Intent intent = new Intent(StudentSettings.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}
		});
		
		
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(StudentSettings.this);
				Intent intent = new Intent(StudentSettings.this,LoginScreen.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Preferences.getInstance().loadPreference(StudentSettings.this);
				if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6"))
				{
					Intent intent = new Intent(StudentSettings.this,StudentProfile.class);
					startActivity(intent);
				}

				else
					if(Preferences.getInstance().userRoleId.matches("4") || Preferences.getInstance().userRoleId.matches("26") || Preferences.getInstance().userRoleId.matches("27") )
					{
						Intent intent = new Intent(StudentSettings.this,TeacherProfile.class);
						startActivity(intent);
					}

				else {
					Intent intent = new Intent(StudentSettings.this, ChairmanProfile.class);
					startActivity(intent);
				}
				
			}
		});

		linkedAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StudentSettings.this,AccountLinkingActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_settings, menu);
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

	protected void getStudentFeedList()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		 final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_WEBSITE_URL + "?ins_id=" + Preferences.getInstance().institutionId+"&sch_id=" + Preferences.getInstance().schoolId;
		StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					//toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Intent intent = new Intent(StudentSettings.this,AboutSchool.class);
						startActivity(intent);
					}
						//Utils.showToast(A,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("school"))
					{
						schoolNotificationListArray= new JSONObject(response).getJSONArray("school");
						if(null!=schoolNotificationListArray && schoolNotificationListArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = schoolNotificationListArray.toString().getBytes();
							VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
							url1 = responseObject.getJSONArray("school").getJSONObject(0).getString("school_website_url");
							if(url1.matches("") || url1.matches("null"))
							{
								Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
							}

							else {

								Intent intent = new Intent(StudentSettings.this, WebViewActivity.class);
								intent.putExtra("url", url1);
								intent.putExtra("position",0);
								startActivity(intent);
							}

						}
					}
					else
						Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(getApplicationContext()))
			queue.add(requestObject);
		else
		{
			Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
		}
	}
}
