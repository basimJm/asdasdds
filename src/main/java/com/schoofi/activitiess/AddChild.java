package com.schoofi.activitiess;



import org.json.JSONArray;
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
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddChild extends AppCompatActivity {
	
	private TextView screenTitle,studentName,studentName1,studentClass,studentClass1,studentSchoolName,studentSchoolName1,studentFathersName,studentFathersName1,studentMothersName,studentMothersName1;
    private ImageView childImage,back;
    private Button AddChild;
    private String imageUrl,studentName2,schoolName2,class2,fathersName2,mothersName2,studentId,userEmail;
    private JSONArray studentDetailsArray;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_add_child);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Add Child");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		screenTitle = (TextView) findViewById(R.id.txt_addStudent);
		studentName = (TextView) findViewById(R.id.text_studentNameAddChild);
		studentName1 = (TextView) findViewById(R.id.text_studentName1AddChild);
		studentClass = (TextView) findViewById(R.id.text_studentClassAddChild);
		studentClass1 = (TextView) findViewById(R.id.text_studentClass1AddChild);
		studentSchoolName = (TextView) findViewById(R.id.text_studentSchoolName1AddChild);
		studentSchoolName1 = (TextView) findViewById(R.id.text_studentSchoolName1AddChild);
		studentFathersName = (TextView) findViewById(R.id.text_studentFatherNameAddChild);
		studentFathersName1 = (TextView) findViewById(R.id.text_studentFatherName1AddChild);
		studentMothersName = (TextView) findViewById(R.id.text_studentMotherNameAddChild);
		studentMothersName1 = (TextView) findViewById(R.id.text_studentMotherName1AddChild);
		AddChild = (Button) findViewById(R.id.btn_addchild);
		childImage = (ImageView) findViewById(R.id.childImageView);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		imageUrl = getIntent().getStringExtra("imageUrl");
		studentName2 = getIntent().getStringExtra("studentName");
		class2 = getIntent().getStringExtra("studentClass");
		fathersName2 = getIntent().getStringExtra("fathersName");
		schoolName2 = getIntent().getStringExtra("schoolName");
		mothersName2 = getIntent().getStringExtra("mothersName");
		studentId = getIntent().getStringExtra("studentId");
		userEmail = getIntent().getStringExtra("email");
		
		System.out.println(studentName2);
		
		studentName1.setText(studentName2);
		studentSchoolName1.setText(schoolName2);
		studentClass1.setText(class2);
		studentFathersName1.setText(fathersName2);
		studentMothersName1.setText(mothersName2);
		
		Picasso.with(AddChild.this).load(AppConstants.SERVER_URLS.SERVER_URL+imageUrl).placeholder(R.drawable.ic_action_person).error(R.drawable.ic_action_person).into(childImage);
		
		AddChild.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				postMessage();
				
			}
		});
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_child, menu);
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


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_REGISTRATION_SCREEN_4+"?email="+userEmail+"&stu_id="+studentId+"&device_id="+Preferences.getInstance().deviceId;
		System.out.println(url1);

		StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					System.out.println(responseObject.toString());
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(AddChild.this,"Error Submitting Comment");

					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(AddChild.this, "Session Expired:Please Login Again");
						}

						else
							if(responseObject.has("Student_details"))
							{
								
								Intent intent;
								if(responseObject.getJSONArray("Student_details").getJSONObject(0).getString("role_id").equals("6")){
									Preferences.getInstance().loadPreference(AddChild.this);
									Preferences.getInstance().userEmailId = userEmail;
									Preferences.getInstance().userId = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("user_id");
									Preferences.getInstance().schoolId = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("school_id");
									Preferences.getInstance().token = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("token");
									//Preferences.getInstance().studentSectionId = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("section_id");
									Preferences.getInstance().studentId = studentId;
									//Preferences.getInstance().studentClassId = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("class_id");
									//Preferences.getInstance().userName = responseObject.getJSONArray("User_details").getJSONObject(0).getString("user_name");
									Preferences.getInstance().userType = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("type"); 
									Preferences.getInstance().userRoleId = responseObject.getJSONArray("Student_details").getJSONObject(0).getString("role_id");
									Preferences.getInstance().savePreference(AddChild.this);
									intent = new Intent(AddChild.this,ParentHomeScreen.class);
									startActivity(intent);
								}
							
								else if(responseObject.getJSONArray("Student_details").getJSONObject(0).getString("role_id").equals("7")){
									intent = new Intent(AddChild.this,ChairmanHomeScreen.class);
									startActivity(intent);
								}
							}
				}
				
				
				
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AddChild.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(AddChild.this, "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("email",userEmail);
				params.put("stu_id", studentId);
				params.put("adm_no", studentAdmissionNo1.getText().toString());
				params.put("stu_dob",date1 );
				params.put("sch_id", schoolId);
				params.put("sec", studentSection1.getText().toString());
				params.put("cls", studentClass1.getText().toString());
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
}
