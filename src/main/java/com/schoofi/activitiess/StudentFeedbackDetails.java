package com.schoofi.activitiess;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.adapters.FeedbackDetailListAdapter;
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

public class StudentFeedbackDetails extends AppCompatActivity {
	
	//http://www.androidhive.info/2013/12/android-populating-spinner-data-from-mysql-database/
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	
	
	private TextView feedbackDetailTitle,nameDetail,nameDetail1,typeDetail,typeDetail1,date,date1,description,description1;
	private ListView feedBackDetailList;
	private ImageView imageBack;
	private Button reply,submit,cancel;
	private SwipyRefreshLayout swipyRefreshLayout;
	private EditText message;
	private int position;
	String msg;
	private JSONArray studentFeedbackArray;
	private FeedbackDetailListAdapter feedbackDetailListAdapter;
	private JSONArray studentFeedbackReply;
	private LinearLayout linearLayout,linearLayoutReply;
	String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	String url;
	String url1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_feedback_details);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student FeedBackDetails");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		feedbackDetailTitle = (TextView) findViewById(R.id.txt_feedBackDetails);
		nameDetail = (TextView) findViewById(R.id.text_nameDetail);
		nameDetail1 = (TextView) findViewById(R.id.text_nameDetail1);
		typeDetail = (TextView) findViewById(R.id.text_typeDetail);
		typeDetail1 = (TextView) findViewById(R.id.text_typeDetail1);
		linearLayout = (LinearLayout) findViewById(R.id.list);
		linearLayoutReply = (LinearLayout) findViewById(R.id.reply);
		linearLayoutReply.setVisibility(View.INVISIBLE);
		date = (TextView) findViewById(R.id.text_date);
		date1 = (TextView) findViewById(R.id.text_date1);
		description = (TextView) findViewById(R.id.text_descriptionDetail);
		description1 = (TextView) findViewById(R.id.text_descriptionDetail1);
		imageBack = (ImageView) findViewById(R.id.img_back);
		imageBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		feedBackDetailList = (ListView) findViewById(R.id.feedbackDetailList);
		swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				initData();
				getFeedbackReplyList();
			}
		});
		feedBackDetailList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		reply = (Button) findViewById(R.id.btn_reply);
		
		position = getIntent().getExtras().getInt("position");
		//System.out.println(position);
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(StudentFeedbackDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&stu_id=" +studentId);
			studentFeedbackArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentFeedbackArray!= null)
		{
			try {


				nameDetail.setText("Name:");
				nameDetail1.setText(studentFeedbackArray.getJSONObject(position).getString("name"));
				typeDetail.setText("Type:");
				typeDetail1.setText(studentFeedbackArray.getJSONObject(position).getString("full_name"));
				date.setText("Date:");
				date1.setText(studentFeedbackArray.getJSONObject(position).getString("created_date"));
				description.setText("Description:");
				description1.setText(studentFeedbackArray.getJSONObject(position).getString("description"));
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
		
		initData();
		getFeedbackReplyList();
		
		reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linearLayout.setVisibility(View.INVISIBLE);
				linearLayoutReply.setVisibility(View.VISIBLE);
				reply.setVisibility(View.INVISIBLE);
				
				message = (EditText) findViewById(R.id.edit_msg);
				submit = (Button) findViewById(R.id.submit);
				cancel = (Button) findViewById(R.id.cancel);
				 //msg = message.getText().toString();
				submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(message.getText().toString().matches(""))
						{
							Utils.showToast(StudentFeedbackDetails.this, "Mesaage cannot be left blank");
							
						}
						
						else
						{
							postMessage();
						linearLayoutReply.setVisibility(View.INVISIBLE);
						linearLayout.setVisibility(View.VISIBLE);
						reply.setVisibility(View.VISIBLE);
						}
						
					}
				});
				
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						linearLayoutReply.setVisibility(View.INVISIBLE);
						linearLayout.setVisibility(View.VISIBLE);
						reply.setVisibility(View.VISIBLE);
						
					}
				});
				
				
				
			}
		});
		
		
		
				
	}
	
	private void initData()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+studentFeedbackArray.getJSONObject(position).getString("feedback_id")+"&crr_date="+currentDate);
			if(e == null)
			{
				studentFeedbackReply= null;
			}
			else
			{
				studentFeedbackReply= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentFeedbackReply!= null)
		{
			feedbackDetailListAdapter= new FeedbackDetailListAdapter(StudentFeedbackDetails.this,studentFeedbackReply);
			feedBackDetailList.setAdapter(feedbackDetailListAdapter);
			feedbackDetailListAdapter.notifyDataSetChanged();
		}
	}
	
	private void getFeedbackReplyList()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentFeedbackDetails.this).getRequestQueue();
		
		try {
			url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+studentFeedbackArray.getJSONObject(position).getString("feedback_id")+"&crr_date="+currentDate;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						linearLayout.setVisibility(View.INVISIBLE);
						Utils.showToast(StudentFeedbackDetails.this,"No Records Found");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(StudentFeedbackDetails.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Feedresp"))
							{
								studentFeedbackReply= new JSONObject(response).getJSONArray("Feedresp");
								if(null!=studentFeedbackReply && studentFeedbackReply.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentFeedbackReply.toString().getBytes();
									VolleySingleton.getInstance(StudentFeedbackDetails.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_REPLY_LIST+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+studentFeedbackArray.getJSONObject(position).getString("feedback_id")+"&crr_date="+currentDate,e);
									feedBackDetailList.invalidateViews();
									feedbackDetailListAdapter = new FeedbackDetailListAdapter(StudentFeedbackDetails.this, studentFeedbackReply);
									feedBackDetailList.setAdapter(feedbackDetailListAdapter);
									feedbackDetailListAdapter.notifyDataSetChanged();	
									
								}
							}
							else
								Utils.showToast(StudentFeedbackDetails.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentFeedbackDetails.this, "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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
			if(Utils.isNetworkAvailable(StudentFeedbackDetails.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(StudentFeedbackDetails.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
	
	private void postMessage()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		
		try {
			url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEEDBACK_POST_COMMENT+"?u_email_id="+userEmailId+"&user_id="+userId+"&token="+token+"&feedback_id="+studentFeedbackArray.getJSONObject(position).getString("feedback_id")+"&crr_date="+currentDate+"&reply="+msg;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try 
				{
					responseObject = new JSONObject(response);
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						linearLayoutReply.setVisibility(View.INVISIBLE);
						linearLayout.setVisibility(View.VISIBLE);
						reply.setVisibility(View.VISIBLE);
						Utils.showToast(StudentFeedbackDetails.this,"Error Submitting Comment");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(StudentFeedbackDetails.this, "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
								linearLayoutReply.setVisibility(View.INVISIBLE);
								linearLayout.setVisibility(View.VISIBLE);
								reply.setVisibility(View.VISIBLE);
								Utils.showToast(StudentFeedbackDetails.this,"Successfuly Submitted Comment");
							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentFeedbackDetails.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {

					Utils.showToast(StudentFeedbackDetails.this, "Error submitting alert! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(CreateEmergency.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("societyId",Preferences.getInstance().societyId);
				params.put("submitterId", Preferences.getInstance().submitterId);
				params.put("title", title.getText().toString());
				params.put("message", alert.getText().toString());
				params.put("postedBy", Preferences.getInstance().userName);
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
		getMenuInflater().inflate(R.menu.student_feedback_details, menu);
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
