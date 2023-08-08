package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudentLeaveDetail extends AppCompatActivity {

	private JSONArray studentLeaveListArray;
	private int position;
	private TextView studentLeaveStatus,studentLeaveStartingDate,studentLevaeDescription,studentLeaveDetailPageTitle,studentLeaveStartingDate1,studentLevaeDescription1;
	private ImageView backButton,imageLeaveDetail;
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String fontPath = "fonts/asap.regular.ttf";
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1,date2;
	String image,leaveId;
	Button delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar =getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_leave_detail);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student LeaveDetail");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		Typeface face = Typeface.createFromAsset(getAssets(), fontPath);
		studentLeaveStatus = (TextView) findViewById(R.id.text_leave_detail_status);
		studentLeaveStartingDate = (TextView) findViewById(R.id.text_leave_detail_leave_starting_date);
		studentLevaeDescription= (TextView) findViewById(R.id.text_leave_detail_leave_description);
		studentLevaeDescription1 = (TextView) findViewById(R.id.text_leave_detail_leave_description1);
		studentLeaveStartingDate1 = (TextView) findViewById(R.id.text_leave_detail_leave_starting_date1);
		studentLeaveDetailPageTitle = (TextView) findViewById(R.id.txt_LeaveDetail);
		backButton = (ImageView) findViewById(R.id.img_back);
		delete = (Button) findViewById(R.id.btn_delete);
		imageLeaveDetail = (ImageView) findViewById(R.id.image_icon);
		studentLeaveDetailPageTitle.setTypeface(face);
		studentLeaveStartingDate.setTypeface(face);
		studentLeaveStartingDate1.setTypeface(face);
		studentLevaeDescription.setTypeface(face);
		studentLevaeDescription1.setTypeface(face);
		delete.setVisibility(View.INVISIBLE);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Preferences.getInstance().loadPreference(getApplicationContext());
		
		

		position = getIntent().getExtras().getInt("position");
		//System.out.println(position);
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(StudentLeaveDetail.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_LEAVE_LIST_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId);
			studentLeaveListArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentLeaveListArray!= null)
		{
			try {

                date1 = studentLeaveListArray.getJSONObject(position).getString("from_date");
                Date date3 = formatter.parse(date1);
            	
            	SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            	date2 = formatter1.format(date3);
            	studentLeaveStartingDate1.setText(date2);
				//studentLeaveStartingDate1.setText(studentLeaveListArray.getJSONObject(position).getString("from_date"));
				studentLevaeDescription1.setText(studentLeaveListArray.getJSONObject(position).getString("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).get("description"));
				//System.out.println(studentLeaveListArray.getJSONObject(position).getString("status"));
				if(studentLeaveListArray.getJSONObject(position).getString("status").equals("0"))
				{
					studentLeaveStatus.setText("Pending");
					studentLeaveStatus.setTextColor(getResources().getColor(R.color.orange));
					delete.setVisibility(View.VISIBLE);

				}
				else
					if(studentLeaveListArray.getJSONObject(position).getString("status").equals("1"))
					{
						studentLeaveStatus.setText("Approved");
						//studentLeaveStatus.setTextColor(Color.parseColor("F2F2F2"));
						delete.setVisibility(View.INVISIBLE);

					}
					else
						if(studentLeaveListArray.getJSONObject(position).getString("status").equals("2"))
						{
							studentLeaveStatus.setText("Rejected");
							//studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));
							delete.setVisibility(View.INVISIBLE);

						}
						else
						{   studentLeaveStatus.setText("Rejected");
						//studentLeaveStatus.setTextColor(Color.parseColor("EE4749"));
						delete.setVisibility(View.INVISIBLE);

						}
				
				image = studentLeaveListArray.getJSONObject(position).getString("attachment");
				
				if(image.matches(""))
				{
					imageLeaveDetail.setImageResource(R.drawable.cameracross);
				}
				
				else
				{
					imageLeaveDetail.setImageResource(R.drawable.camera);
				}
				
					
				

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		imageLeaveDetail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(image.matches(""))
				{
					Utils.showToast(getApplicationContext(), "No image");
				}
				
				else
				{
	            Intent intent = new Intent(StudentLeaveDetail.this,TeacherStudentImageDetails.class);
	            intent.putExtra("imageUrl", image);
	            startActivity(intent);
				}
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					if(studentLeaveListArray.getJSONObject(position).getString("status").equals("1") || studentLeaveListArray.getJSONObject(position).getString("status").equals("2") )
					{
						Utils.showToast(getApplicationContext(), "This leave cannont be deleted");
					}
					
					else
					{
						postDelete();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_leave_detail, menu);
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
	
	 protected void postDelete()
		{
			setProgressBarIndeterminateVisibility(true);
			RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
			
			
			
			
				final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.LEAVE_DELETE;
			
			StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					JSONObject responseObject;
					//System.out.println(response);
					//Utils.showToast(getApplicationContext(), ""+response);
					//System.out.println(url1);
					try 
					{
						responseObject = new JSONObject(response);
						
						if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						{
							
							//Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
							Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
						}
						else
							if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
							{
								Toast.makeText(getApplicationContext(), "error6", Toast.LENGTH_LONG).show();
								//Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
							}
						
							else
								if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
								{
									
									Toast.makeText(getApplicationContext(), "leave deleted", Toast.LENGTH_LONG).show();
									Intent intent = new Intent(StudentLeaveDetail.this,StudentLeaveRequest.class);
									startActivity(intent);
									finish();
								}
						
								else
								{
									System.out.println("kkk");
								}
					}
					catch(JSONException e)
					{
						e.printStackTrace();
						//Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
						Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
					}
					setProgressBarIndeterminateVisibility(false);

				}}, new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {

						Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
						setProgressBarIndeterminateVisibility(false);
					}
				})
			{
				@Override
				protected Map<String,String> getParams(){
					//.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
					Map<String,String> params = new HashMap<String, String>();
					//params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
					//params.put("Students", jsonObject1.toString());
					
					
					
					/*params.put("tech_id" , Preferences.getInstance().teachId);
					params.put("inst_id", Preferences.getInstance().institutionId);
					params.put("sch_id", Preferences.getInstance().schoolId);
					params.put("cls_id", Preferences.getInstance().studentClassId);
					params.put("sec_id", Preferences.getInstance().studentSectionId);
					params.put("title", title.getText().toString());
					params.put("last_Date", "2016-03-22");
					
					params.put("sub_id", teacherAssignmentUpload1);
					params.put("crr_Date", date);
					params.put("file", images.toString());
					params.put("module_type", moduleType1);
					params.put("u_id", Preferences.getInstance().userId);
					
					params.put("count", ""+count);*/
					
					//params.put("user_id", Preferences.getInstance().userId);
					//params.put("feedback_id", Preferences.getInstance().feedbackId);
					//params.put("token", Preferences.getInstance().token);
					//params.put("device_id", Preferences.getInstance().deviceId);
					params.put("stu_id", Preferences.getInstance().studentId);
					try {
						params.put("leave_id", studentLeaveListArray.getJSONObject(position).getString("leave_id"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//params.put("u_name", Preferences.getInstance().userName);
				    //params.put("reply", messageReply.getText().toString());
					//params.put("created_date", date);
					//params.put("teach_email",Preferences.getInstance().userEmailId);
					//params.put("count", String.valueOf(i));
					//params.put("sch_id", Preferences.getInstance().schoolId);
					//params.put("cls_id", Preferences.getInstance().studentClassId);
					//params.put("sec_id", Preferences.getInstance().studentSectionId);
					//params.put("tea_id", Preferences.getInstance().userId);
					//params.put("crr_date",date);
					//params.put("Students", "harsh");
					//params.put("u_email_id", Preferences.getInstance().userEmailId);
					return params;
				}};		

				requestObject.setRetryPolicy(new DefaultRetryPolicy(
						25000, 
						DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				
					queue.add(requestObject);
				
				
		
		
		}



}

