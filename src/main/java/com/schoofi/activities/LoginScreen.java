package com.schoofi.activities;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.schoofi.activitiess.AdminHomeScreen;
import com.schoofi.activitiess.AdmissionEnquiry;
import com.schoofi.activitiess.AdmissionEnquiryMain;
import com.schoofi.activitiess.AuditUserHomeScreen;
import com.schoofi.activitiess.BusAdminHomeScreen;
import com.schoofi.activitiess.BusCoordinatorAdminScreen;
import com.schoofi.activitiess.ChairmanDashboard;
import com.schoofi.activitiess.EmployeeAttendanceScreen;
import com.schoofi.activitiess.FeeAdminScreen;
import com.schoofi.activitiess.HRHomeScreen;
import com.schoofi.activitiess.HealthAndAuditHomeScreen;
import com.schoofi.activitiess.MedicalCoordinatorHomeScreen;
import com.schoofi.activitiess.NonTeachingHomeScreen;
import com.schoofi.activitiess.ParentInnerHomeScreen;
import com.schoofi.activitiess.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.SignUpScreen;
import com.schoofi.activitiess.AddChild;
import com.schoofi.activitiess.ChairmanHomeScreen;
import com.schoofi.activitiess.ChairmanSchoolAttendance;
import com.schoofi.activitiess.ForgotPassword;
import com.schoofi.activitiess.ParentHomeScreen;

import com.schoofi.activitiess.RoleLoginScreen;
import com.schoofi.activitiess.SchoolPlannerMakingFirstScreen;
import com.schoofi.activitiess.SecurityAdminScreen;
import com.schoofi.activitiess.StudentSettings;
import com.schoofi.activitiess.WebViewActivity;
import com.schoofi.constants.AppConstants;
import com.schoofi.modals.HttpConnection;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import android.os.CountDownTimer;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity {

	EditText userEmail,userPassword;
	Button login,signUp,admissionEnquiry;
	TextView forgotPassword,termsAndConditions,privacyPolicy;
	ImageView schoofiIcon;
	private static String urlString;
	static final String Key_USER_NAME = "user_Name";
	static final String Key_USER_PASSWORD = "user_Pass";
	String userName,userPass;
	String fontPath = "fonts/asap.regular.ttf";
	LinearLayout linearUserEmail,linearUserPassword;
	String u = "";
	String permission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float yInches= metrics.heightPixels/metrics.ydpi;
		float xInches= metrics.widthPixels/metrics.xdpi;
		double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
		if (diagonalInches>=6.0){
			// 6.5inch device or bigger
			u = "1";
			setContentView(R.layout.activity_tablet_login);
		}else{
			// smaller device
			u = "2";
			setContentView(R.layout.activity_login_screen);

		}
		
		

		
		
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Login Screen");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());


		userEmail = (EditText) findViewById(R.id.edt_userEmail);
		userPassword = (EditText) findViewById(R.id.edt_userPassword);
		forgotPassword = (TextView) findViewById(R.id.txt_forgotYourPassword);
		login = (Button) findViewById(R.id.btn_login);
		signUp = (Button) findViewById(R.id.btn_signUp);
		linearUserEmail = (LinearLayout) findViewById(R.id.linear_userEmail);
		linearUserPassword = (LinearLayout) findViewById(R.id.linear_userPassword);
		admissionEnquiry = (Button) findViewById(R.id.btn_admission_enquiry);
		termsAndConditions = (TextView) findViewById(R.id.text_termsAndConditions);
		privacyPolicy = (TextView) findViewById(R.id.text_privacy_policy);
		Typeface face = Typeface.createFromAsset(getAssets(), fontPath);
		Preferences.getInstance().loadPreference(LoginScreen.this);


		/*if(Preferences.getInstance().userName.matches("") || Preferences.getInstance().userName.matches("null"))
		{
			Log.d("llll","lll");
		}
		else {
			userEmail.setText(Preferences.getInstance().userName);
		}*/
		/*Utils.showToast(getApplicationContext(),Preferences.getInstance().password);
		if(Preferences.getInstance().password.matches(""))
		{
			Log.d("llll","lll");
		}
		else {
			userPassword.setText(Preferences.getInstance().password);
		}*/
		userEmail.setTypeface(face);
		login.setTypeface(face);
		signUp.setTypeface(face);
		login.setText("LOGIN");


		privacyPolicy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginScreen.this,WebViewActivity.class);
				String url = /*"http://www.schoofi.com/privacy.php";*/  "https://drive.google.com/file/d/0B7e14kKbW68iUEh6Z1R1d3Nvc3M/view?usp=sharing";
				intent.putExtra("url",url);
				startActivity(intent);

			}
		});

		termsAndConditions.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginScreen.this,WebViewActivity.class);
				String url = "https://drive.google.com/file/d/0B7e14kKbW68iNGtGRS1fSk9aT2s/view?usp=sharing";
				intent.putExtra("url",url);
				startActivity(intent);

			}
		});
		
		
		
		linearUserEmail.setBackgroundColor(Color.argb(85, 194, 194,194));
		linearUserPassword.setBackgroundColor(Color.argb(85, 194, 194, 194));
		forgotPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(LoginScreen.this,ForgotPassword.class);
				startActivity(intent);
				
			}
		});


		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
				Preferences.getInstance().userName = userEmail.getText().toString();
				Preferences.getInstance().userPassword = userPassword.getText().toString();
				Preferences.getInstance().savePreference(LoginScreen.this);


			}
		});

		signUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginScreen.this,SchoolPlannerMakingFirstScreen.class);
				startActivity(intent);

			}
		});

                admissionEnquiry.setVisibility(View.GONE);
		admissionEnquiry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginScreen.this, AdmissionEnquiryMain.class);
				startActivity(intent);
			}
		});

		signUp.setVisibility(View.GONE);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_screen, menu);
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

	private void login()
	{   
		final ProgressDialog dialog;
		dialog = new ProgressDialog(LoginScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false); 
		dialog.setMessage("Loading...");
		dialog.show();
		userName =userEmail.getText().toString();
		userPass = userPassword.getText().toString();

		Preferences.getInstance().loadPreference(getApplicationContext());
		Preferences.getInstance().userName1 = userEmail.getText().toString();
		Preferences.getInstance().userPassword = userPassword.getText().toString();
		Preferences.getInstance().savePreference(getApplicationContext());
		//System.out.println("username: " + userName);
		//System.out.println("userPass: "+ userPass);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.NEW_LOGIN_URL/*+"?user_Name="+userName+"&user_Pass="+userPass*/;
		//System.out.println(urlString);
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
					Log.d("po",responseObject.toString());
					if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
						Toast.makeText(LoginScreen.this, "Login Failed!", Toast.LENGTH_SHORT).show();
					else if(responseObject.has("User_details")) {
						Preferences.getInstance().loadPreference(getApplicationContext());
						Preferences.getInstance().fullRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
						Preferences.getInstance().savePreference(getApplicationContext());
						ArrayList aList = new ArrayList(Arrays.asList(Preferences.getInstance().fullRoleId.split(",")));
						for (int i = 0; i < aList.size(); i++) {
							Log.d(String.valueOf(i), String.valueOf(aList.get(i)));
						}
						if (aList.size() > 1) {
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							//Preferences.getInstance().userType = "1";
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().fullRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(getApplicationContext());
							Intent intent = new Intent(LoginScreen.this, RoleLoginScreen.class);
							intent.putExtra("user_name",userEmail.getText().toString());
							intent.putExtra("user_pass",userPassword.getText().toString());
							startActivity(intent);

						} else {
							Preferences.getInstance().loadPreference(getApplicationContext());
							Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().fullRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().savePreference(getApplicationContext());
							login1();

						}
					}
					

				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(LoginScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{


				@Override
				public void onErrorResponse(VolleyError error) {
					// TODO Auto-generated method stub
					Toast.makeText(LoginScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
					setSupportProgressBarIndeterminateVisibility(false);
					dialog.dismiss();
				}
			})
		{

			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(getApplicationContext());
				Map<String,String> params = new HashMap<String, String>();
				params.put("user_Name",userName);
				Log.d("user_Name",userName);
				params.put("user_Pass",userPass);
				Log.d("user_Pass",userPass);
				params.put("device_id", Preferences.getInstance().deviceId);
				Log.d("device_id", Preferences.getInstance().deviceId);
				params.put("indicator","A");
				Log.d("indicator","A");
				params.put("fcm_enable" ,"F");
				Log.d("fcm_enable" ,"F");

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
				dialog.dismiss();
			}
	}

	public void toa(){
		System.out.println("sdkbksdbksd");
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}


	private void login1()
	{
		Preferences.getInstance().loadPreference(getApplicationContext());
		final ProgressDialog dialog;
		dialog = new ProgressDialog(LoginScreen.this, AlertDialog.THEME_HOLO_DARK);
		dialog.setCancelable(false);
		dialog.setMessage("Loading...");
		dialog.show();

		//System.out.println("username: " + userName);
		//System.out.println("userPass: "+ userPass);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		String urlString = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.LOGIN_DETAILS/*+"?user_Name="+userName+"&user_Pass="+userPass*/;
		//System.out.println(urlString);
		StringRequest requestObject = new StringRequest(Request.Method.POST,urlString, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				dialog.dismiss();
				Log.d("op",response);
				try
				{
					toa();
					responseObject = new JSONObject(response);
					System.out.println(responseObject.toString());
					if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
						Toast.makeText(LoginScreen.this, "Login Failed!", Toast.LENGTH_SHORT).show();
					else if(responseObject.has("User_details")) {
						// Utils.showToast(getApplicationContext(),"90");
						//



						Intent intent;
						Preferences.getInstance().loadPreference(getApplicationContext());
						if (Preferences.getInstance().userRoleId.matches("5")) {

							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().studentSectionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("section_id");
							Preferences.getInstance().studentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("stu_id");
							Preferences.getInstance().studentClassId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("class_id");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							// Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							//Preferences.getInstance().busRouteNo = responseObject.getJSONArray("User_details").getJSONObject(0).getString("route_no");
							//Preferences.getInstance().busNumber =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("bus_number");
							//Preferences.getInstance().lattitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("latitude");
							//Preferences.getInstance().longitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("longitude");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("mobile");
							Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
							Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
							Preferences.getInstance().addmissionNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("admn_No");
							Preferences.getInstance().feesAddCharges = responseObject.getJSONArray("User_details").getJSONObject(0).getString("fee_add_charges");
							Preferences.getInstance().eventAddCharges = responseObject.getJSONArray("User_details").getJSONObject(0).getString("event_add_charges");
							Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
							Preferences.getInstance().board = responseObject.getJSONArray("User_details").getJSONObject(0).getString("board");
							Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
							Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
							Preferences.getInstance().studentGender = responseObject.getJSONArray("User_details").getJSONObject(0).getString("gender");
							Preferences.getInstance().studentSemester = responseObject.getJSONArray("User_details").getJSONObject(0).getString("semester");
							Preferences.getInstance().studentCourse = responseObject.getJSONArray("User_details").getJSONObject(0).getString("course_id");
							Preferences.getInstance().isHostelStudent = responseObject.getJSONArray("User_details").getJSONObject(0).getString("is_hostel");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, StudentHomeScreen.class);

							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("7")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							/*Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");*/
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, ChairmanHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("6")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, ParentHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("8")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("mobile");
							Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
							Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
							Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
							Preferences.getInstance().board = responseObject.getJSONArray("User_details").getJSONObject(0).getString("board");
							Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
							Preferences.getInstance().totalStudents = responseObject.getJSONArray("User_details").getJSONObject(0).getString("total_stu");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, ChairmanDashboard.class);
							startActivity(intent);
							finish();

						} else if (Preferences.getInstance().userRoleId.matches("4")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							//Preferences.getInstance().teachId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							//Preferences.getInstance().busRouteNo = responseObject.getJSONArray("User_details").getJSONObject(0).getString("route_no");
							//Preferences.getInstance().busNumber =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("bus_number");
							//Preferences.getInstance().lattitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("latitude");
							//Preferences.getInstance().longitude =  responseObject.getJSONArray("User_details").getJSONObject(0).getString("longitude");
							Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("mobile");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
							Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
							Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							//Preferences.getInstance().teacherId1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
							Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
							Preferences.getInstance().board = responseObject.getJSONArray("User_details").getJSONObject(0).getString("board");
							Preferences.getInstance().chairmanAssignmentTeacherId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");

							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, TeacherHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("0")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							//Preferences.getInstance().teachId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("teac_id");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");

							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, AuditUserHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("3")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							//Preferences.getInstance().teachId = "Harsh jkkl";
							Preferences.getInstance().session1 = responseObject.getJSONArray("User_details").getJSONObject(0).getString("session");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
							Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
							Preferences.getInstance().healthAndSafety = responseObject.getJSONArray("User_details").getJSONObject(0).getString("health_safety");
							//Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							//Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							//Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							//Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, AdminHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("9")) {

							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							// Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");

							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, AdmissionEnquiry.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("10")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							// Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");

							Preferences.getInstance().schoolName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_name");
							Preferences.getInstance().schoolLattitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_latitude");
							Preferences.getInstance().schoolLongitude = responseObject.getJSONArray("User_details").getJSONObject(0).getString("sch_longitude");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, BusAdminHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("11")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							//  Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");

							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, FeeAdminScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("2")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							// Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							//Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, ChairmanHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("15")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							//Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, MedicalCoordinatorHomeScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("16")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							//  Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							//Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");

							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, BusCoordinatorAdminScreen.class);
							startActivity(intent);
							finish();
						} else if (Preferences.getInstance().userRoleId.matches("17")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, SecurityAdminScreen.class);
							startActivity(intent);
							finish();
						}

						else if (Preferences.getInstance().userRoleId.matches("26")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, NonTeachingHomeScreen.class);
							startActivity(intent);
							finish();
						}

						else if (Preferences.getInstance().userRoleId.matches("27")) {
							Preferences.getInstance().loadPreference(LoginScreen.this);
							Preferences.getInstance().token = responseObject.getJSONArray("User_details").getJSONObject(0).getString("token");
							Preferences.getInstance().Name = responseObject.getJSONArray("User_details").getJSONObject(0).getString("Name");
							Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
							Preferences.getInstance().userId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_id");
							//Preferences.getInstance().phoneNumber = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_phone");
							Preferences.getInstance().userEmailId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_email_id");
							//Preferences.getInstance().userRoleId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("role_id");
							Preferences.getInstance().institutionId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("institution_id");
							Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().schoolId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("school_id");
							//Preferences.getInstance().permissions = responseObject.getJSONArray("User_details").getJSONObject(0).getString("permission");
							Preferences.getInstance().userType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("type");
							Preferences.getInstance().employeeId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("emp_id");
							Preferences.getInstance().departmentId = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_id");
							Preferences.getInstance().departmentName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("dept_name");
							Preferences.getInstance().employeeType = responseObject.getJSONArray("User_details").getJSONObject(0).getString("employee_type");
							Preferences.getInstance().schoolScheduleLevel = responseObject.getJSONArray("User_details").getJSONObject(0).getString("schedule_level");
							Preferences.getInstance().isLoggedIn = true;
							Preferences.getInstance().savePreference(LoginScreen.this);
							intent = new Intent(LoginScreen.this, HRHomeScreen.class);
							startActivity(intent);
							finish();
						}
					}



				}

				catch(JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(LoginScreen.this, "Error fetching modules! Please try after sometime", Toast.LENGTH_SHORT).show();
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener()
		{


			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(LoginScreen.this, "Try after sometime", Toast.LENGTH_SHORT).show();
				setSupportProgressBarIndeterminateVisibility(false);
				dialog.dismiss();
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(getApplicationContext());
				Map<String,String> params = new HashMap<String, String>();


				params.put("device_id", Preferences.getInstance().deviceId);
				//params.put("token",Preferences.getInstance().token);
				params.put("role_id",Preferences.getInstance().userRoleId);
				params.put("user_Name",userEmail.getText().toString());
				params.put("user_Pass",userPassword.getText().toString());

				Log.d("params",Preferences.getInstance().userRoleId+Preferences.getInstance().deviceId+Preferences.getInstance().token+userEmail.getText().toString()+userPassword.getText().toString());

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
			dialog.dismiss();
		}
	}


}




