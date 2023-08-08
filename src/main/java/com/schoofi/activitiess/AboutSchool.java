package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.schoofi.adapters.SchoolNotificationListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CircleTransform;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import library.Animations.DescriptionAnimation;
import library.SliderAdapter;
import library.SliderLayout;
import library.SliderTypes.BaseSliderView;
import library.SliderTypes.DefaultSliderView;
import library.Tricks.ViewPagerEx;

public class AboutSchool extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{

	private SliderLayout mDemoSlider;
	ArrayList<String> myList;
	ArrayList<String> aList= new ArrayList<String>();
	//private int position;
	SliderAdapter sliderAdapter;
	private JSONArray imageArray,schoolNotificationListArray;
	SchoolNotificationListAdapter schoolNotificationListAdapter;
	private ListView notificationListView;
	private String url;
	String image;
	private ImageView schoollogo,imgCall,back;
	private TextView schoolName;
	int width;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("About School");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_about_school);

		schoollogo = (ImageView) findViewById(R.id.img_school_logo);
		schoolName = (TextView) findViewById(R.id.text_school_full_name);
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		notificationListView = (ListView) findViewById(R.id.listView_notifications);
		imgCall = (ImageView) findViewById(R.id.img_call);
		back = (ImageView) findViewById(R.id.img_back);

		initData();
		getFeedbackReplyList();
		initData1();
		getStudentFeedList();

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		imgCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(AboutSchool.this,AboutSchoolContactUss.class);
				startActivity(intent);
				
			}
		});
        
	









	}

	protected void slider()
	{


		mDemoSlider = (SliderLayout)findViewById(R.id.slider);

		sliderAdapter = new SliderAdapter(this);


		aList = new ArrayList<String>(Arrays.asList(image.split(",")));
		/*for(int i=0;i<aList.size();i++)
		{
			System.out.println(""+aList.get(i));
		}*/



		for(String name : aList)
		{
			DefaultSliderView defaultSliderView = new DefaultSliderView(getApplicationContext());

			defaultSliderView.image(AppConstants.SERVER_URLS.IMAGE_URL+ name).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

			mDemoSlider.addSlider(defaultSliderView);
		}

		//mDemoSlider.getChildAt(position);
		//sliderAdapter.setPrimaryItem(mDemoSlider, position,sliderAdapter);
		//sliderAdapter.getSliderView(position);

		mDemoSlider.setCurrentPosition(1);


		// mDemoSlider.

		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
		mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
		mDemoSlider.setCustomAnimation(new DescriptionAnimation());

		mDemoSlider.setDuration(4000);
		//mDemoSlider.stopAutoCycle();

	}


	protected void bottomScrollView()
	{

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
		for (int i = 0; i < aList.size(); i++) {
			ImageView imageView = new ImageView(this);
			/*imageView.getLayoutParams().width = 30;*/
			imageView.setId(i);

			LayoutParams params = new LayoutParams(width/4,150);
			imageView.setLayoutParams(params);

			imageView.setPadding(2, 2, 2, 2);





			Picasso.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+aList.get(i)).placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(imageView);

			imageView.setScaleType(ScaleType.FIT_XY);
			linearLayout.addView(imageView);



			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Intent intent = new Intent(Produc,ProductDetail.class);
					//startActivity(intent);

				}


			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_school, menu);
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

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub


	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	private void initData()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_IMAGES+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId);
			if(e == null)
			{
				imageArray= null;
			}
			else
			{
				imageArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}


	}

	private void getFeedbackReplyList()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();


		url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_IMAGES+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId;

		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Log.d("kkk", "kkkk");

					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("school"))
							{
								imageArray= new JSONObject(response).getJSONArray("school");
								if(null!=imageArray && imageArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = imageArray.toString().getBytes();

									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_IMAGES+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
									image = responseObject.getJSONArray("school").getJSONObject(0).getString("photo_gallery");
									//Utils.showToast(getApplicationContext(), ""+image);

									Picasso.with(getApplicationContext()).load(AppConstants.SERVER_URLS.IMAGE_URL+responseObject.getJSONArray("school").getJSONObject(0).getString("school_logo")).placeholder(R.drawable.schoollogo).error(R.drawable.schoollogo).transform(new CircleTransform()).into(schoollogo);
									schoolName.setText(responseObject.getJSONArray("school").getJSONObject(0).getString("school_name"));
									slider();
									bottomScrollView();


								}
							}
							else
								Utils.showToast(getApplicationContext(), "Error Fetching Response");
					//setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

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
	private void toa()
	{
		System.out.println("aaa");
	}

	private void initData1() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId);
			if(e == null)
			{
				schoolNotificationListArray= null;
			}
			else
			{
				schoolNotificationListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(schoolNotificationListArray!= null)
		{
			schoolNotificationListAdapter= new SchoolNotificationListAdapter(AboutSchool.this,schoolNotificationListArray);
			notificationListView.setAdapter(schoolNotificationListAdapter);
			schoolNotificationListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentFeedList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(AboutSchool.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						Utils.showToast(AboutSchool.this,"No Records Found");
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(AboutSchool.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("school"))
							{
								schoolNotificationListArray= new JSONObject(response).getJSONArray("school");
								if(null!=schoolNotificationListArray && schoolNotificationListArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = schoolNotificationListArray.toString().getBytes();
									VolleySingleton.getInstance(AboutSchool.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
									notificationListView.invalidateViews();
									schoolNotificationListAdapter = new SchoolNotificationListAdapter(AboutSchool.this, schoolNotificationListArray);
									notificationListView.setAdapter(schoolNotificationListAdapter);
									schoolNotificationListAdapter.notifyDataSetChanged();	

								}
							}
							else
								Utils.showToast(AboutSchool.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(AboutSchool.this, "Error fetching modules! Please try after sometime.");
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
			if(Utils.isNetworkAvailable(AboutSchool.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(AboutSchool.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getFeedbackReplyList();
		initData1();

		getStudentFeedList();
		super.onResume();
	}*/



}
