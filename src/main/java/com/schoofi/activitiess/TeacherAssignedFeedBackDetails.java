package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.fragments.StudentFeedBackReply;
import com.schoofi.fragments.StudentFeedbackList;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherAssignedFeedBackDetails extends AppCompatActivity {
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;

	ImageView back;


	private ScrollView studentScrollView;
	private TextView feedbackDetailTitle,nameDetail,nameDetail1,typeDetail,typeDetail1,date,date1,description,description1;
	private Button reply; 
	private int position;
	private JSONArray teacherFeedBackAssignedArray;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String feedBackId,status;
	String date2,date4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Assigned FeedBack Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_student_feed_back_detailss);
		
		position = getIntent().getExtras().getInt("position");

		feedbackDetailTitle = (TextView) findViewById(R.id.txt_feedBackDetails);
		nameDetail = (TextView) findViewById(R.id.text_nameDetail);
		nameDetail1 = (TextView) findViewById(R.id.text_nameDetail1);
		typeDetail = (TextView) findViewById(R.id.text_typeDetail);
		typeDetail1 = (TextView) findViewById(R.id.text_typeDetail1);

		date = (TextView) findViewById(R.id.text_date);
		date1 = (TextView) findViewById(R.id.text_date1);
		description = (TextView) findViewById(R.id.text_descriptionDetail);
		description1 = (TextView) findViewById(R.id.text_descriptionDetail1);
		reply = (Button) findViewById(R.id.btn_reply);

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		status = getIntent().getStringExtra("status");
		//Utils.showToast(getApplicationContext(), ""+status);
		
		
		


		

		
		
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(TeacherAssignedFeedBackDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TEACHER_FEEDBACK+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&device_id="+Preferences.getInstance().deviceId+"&teac_id="+Preferences.getInstance().teachId);
			teacherFeedBackAssignedArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherFeedBackAssignedArray!= null)
		{
			try {


				nameDetail.setText("Name:");
				nameDetail1.setText(teacherFeedBackAssignedArray.getJSONObject(position).getString("Name"));
				typeDetail.setText("Type:");
				typeDetail1.setText(teacherFeedBackAssignedArray.getJSONObject(position).getString("full_name"));
				date.setText("Date:");
				date2 = teacherFeedBackAssignedArray.getJSONObject(position).getString("created_date");
				Date date3 = formatter.parse(date2);
				
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				date2 = formatter1.format(date3);
				date1.setText(date2);
				//date1.setText(studentFeedbackArray.getJSONObject(position).getString("created_date"));
				description.setText("Description:");
				description1.setText(teacherFeedBackAssignedArray.getJSONObject(position).getString("description"));
				feedBackId = teacherFeedBackAssignedArray.getJSONObject(position).getString("feedback_id");
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

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}


		}
		
		
		
		
		
		Bundle bundle = new Bundle();
		bundle.putString("feedId", feedBackId);
		bundle.putString("value", "2");
		// set Fragmentclass Arguments
		StudentFeedbackList studentFeedbackList = new StudentFeedbackList();
		FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager

		FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction
		transaction.add(R.id.linearFeedback, studentFeedbackList);
		
		studentFeedbackList.setArguments(bundle);
		//transaction.addToBackStack(null);

		transaction.commit();
		//System.out.println(position);
		
		reply.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Bundle bundle = new Bundle();
				bundle.putString("feedId", feedBackId);
				TeacherAssignedReply teacherAssignedReply = new TeacherAssignedReply();
				FragmentManager fm = getSupportFragmentManager();
			     FragmentTransaction fragmentTransaction = fm.beginTransaction();
			     fragmentTransaction.replace(R.id.linearFeedback, teacherAssignedReply);
			     reply.setVisibility(View.INVISIBLE);
			     teacherAssignedReply.setArguments(bundle);
			     fragmentTransaction.commit();*/
				if(status.matches("2"))
				{
					
					Bundle bundle = new Bundle();
					bundle.putString("feedId", feedBackId);
					//TeacherAssignedReply teacherAssignedReply = new TeacherAssignedReply();
					StudentFeedBackReply studentFeedBackReply = new StudentFeedBackReply();
					FragmentManager fm = getSupportFragmentManager();
				     FragmentTransaction fragmentTransaction = fm.beginTransaction();
				     fragmentTransaction.replace(R.id.linearFeedback, studentFeedBackReply);
				     reply.setVisibility(View.INVISIBLE);
				     studentFeedBackReply.setArguments(bundle);
				     fragmentTransaction.commit();
					
				}
				
				else
				{
				Intent intent = new Intent(TeacherAssignedFeedBackDetails.this,FragmentsActivity.class);
				startActivity(intent);
				finish();
				}
				
			}
		});
		
	
		



	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		/*Intent intent = new Intent(StudentFeedBackDetailss.this,StudentFeedBack.class);
		startActivity(intent);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_feed_back_detailss, menu);
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
