package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
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
import com.schoofi.adapters.TeacherAnnouncementAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeacherAnnouncement extends AppCompatActivity {
	
	private ImageView back,plus;
	private TextView screenTitle;
	private ListView teacherAnnouncementListView;
	TeacherAnnouncementAdapter teacherAnnouncementAdapter;
	String userId = Preferences.getInstance().userId;
	String token = Preferences.getInstance().token;
	String userEmailId = Preferences.getInstance().userEmailId;
	String schoolId = Preferences.getInstance().schoolId;
	private JSONArray teacherAnnouncementArray;
	private SwipyRefreshLayout swipyRefreshLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_teacher_announcement);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Teacher Announcement");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		screenTitle = (TextView) findViewById(R.id.txt_teacherAnnouncement);
		teacherAnnouncementListView = (ListView) findViewById(R.id.listViewTeacherAnnouncement);
		initData();
		getTeacherAnnouncementList();

		plus = (ImageView) findViewById(R.id.img_plus);

		if(Preferences.getInstance().userRoleId.matches("4") || Preferences.getInstance().userRoleId.matches("25"))
		{
			plus.setVisibility(View.INVISIBLE);
		}

		else
		{
			plus.setVisibility(View.VISIBLE);
		}

		plus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(TeacherAnnouncement.this,NewAnnouncement.class);
				startActivity(intent);
			}
		});

		swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);



		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				initData();
				getTeacherAnnouncementList();
			}
		});
		
		teacherAnnouncementListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent;
				
				try {
					/*if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("image")||teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("txt"))
					{
						intent = new Intent(TeacherAnnouncement.this,TeacherAnnouncementImageResult.class);
						intent.putExtra("position", position);
						startActivity(intent);	
					}

					else
					if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("text") || teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("Text") ||  teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("TEXT"))
					{
						intent = new Intent(TeacherAnnouncement.this,TeacherAnnouncementTextResult.class);
						intent.putExtra("position", position);
						startActivity(intent);
					}
					else if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("video"))
					{
						intent = new Intent(TeacherAnnouncement.this,TeacherVideoAnnouncementVideoResult.class);
						intent.putExtra("position", position);
						startActivity(intent);
					}
					
					else if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("audio"))
					{
						intent = new Intent(TeacherAnnouncement.this,TeacherAnnouncementAudioResult.class);
						intent.putExtra("position", position);
						startActivity(intent);
					}
					else if(teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("pdf")||teacherAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("ppt"))
					{
						*//*intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.SERVER_URLS.IMAGE_URL+teacherAnnouncementArray.getJSONObject(position).getString("image_path")));
						startActivity(intent);*/

						intent = new Intent(TeacherAnnouncement.this,AnnouncementPDFResult.class);
						intent.putExtra("url",teacherAnnouncementArray.getJSONObject(position).getString("image_path"));
						intent.putExtra("title",teacherAnnouncementArray.getJSONObject(position).getString("announcement_title"));
						intent.putExtra("description",teacherAnnouncementArray.getJSONObject(position).getString("announcement"));
						startActivity(intent);
					/*}
					
					else
					{
						Utils.showToast(TeacherAnnouncement.this,"No Records Found");
					}*/
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
		getMenuInflater().inflate(R.menu.teacher_announcement, menu);
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
	
	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				teacherAnnouncementArray= null;
			}
			else
			{
				teacherAnnouncementArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherAnnouncementArray!= null)
		{
			teacherAnnouncementAdapter= new TeacherAnnouncementAdapter(TeacherAnnouncement.this,teacherAnnouncementArray);
			teacherAnnouncementListView.setAdapter(teacherAnnouncementAdapter);
			teacherAnnouncementAdapter.notifyDataSetChanged();
		}
	}

	protected void getTeacherAnnouncementList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherAnnouncement.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				System.out.println(url);

				try 
				{
					responseObject = new JSONObject(response);
					
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(TeacherAnnouncement.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(TeacherAnnouncement.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Announcement"))
							{
								teacherAnnouncementArray= new JSONObject(response).getJSONArray("Announcement");
								if(null!=teacherAnnouncementArray && teacherAnnouncementArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherAnnouncementArray.toString().getBytes();
									VolleySingleton.getInstance(TeacherAnnouncement.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_ANNOUNCEMENT_URL+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
									teacherAnnouncementListView.invalidateViews();
									teacherAnnouncementAdapter = new TeacherAnnouncementAdapter(TeacherAnnouncement.this, teacherAnnouncementArray);
									teacherAnnouncementListView.setAdapter(teacherAnnouncementAdapter);
									teacherAnnouncementAdapter.notifyDataSetChanged();
									swipyRefreshLayout.setRefreshing(false);
								}
							}
							else
								Utils.showToast(TeacherAnnouncement.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(TeacherAnnouncement.this, "Error fetching modules! Please try after sometime.");
					setSupportProgressBarIndeterminateVisibility(false);
				}
				
				/*try
				{
					responseObject = new JSONObject(response);
					if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("image"))
					{   
						String ddd = responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type");
                        System.out.println(ddd);
						count = 1;
					}
					else if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("video"))
					{
						count =2;
					}
					else
						if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("audio"))
						{
							count =3;
						}
						else
							if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("text")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("pdf")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("ppt"))
							{
								count =4;
							}
					
					for(int i=0;i<=studentAnnouncementArray.length();i++)
					{
						if(studentAnnouncementArray.getJSONObject(0).getString("announcement_type").equals("image"))
						{
							count = 1;
						}
						else if(studentAnnouncementArray.getJSONObject(1).getString("announcement_type").equals("video"))
						{
							count = 2;
						}
						else if(studentAnnouncementArray.getJSONObject(2).getString("announcement_type").equals("audio"))
						{
							count =3;
						}
						else if(studentAnnouncementArray.getJSONObject(3).getString("announcement_type").equals("text")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("ppt")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("pdf"))
						{
							count =4;
						}
					}
				}
					catch (Exception e) {
						// TODO: handle exception
					}*/
				



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
				Preferences.getInstance().loadPreference(StudentAnnouncement.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("student_ID",Preferences.getInstance().studentId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id",Preferences.getInstance().studentClassId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(TeacherAnnouncement.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(TeacherAnnouncement.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
}
