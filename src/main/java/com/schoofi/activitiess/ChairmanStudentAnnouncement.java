package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
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
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.ChairmanAnnouncementListAdapter;
import com.schoofi.adapters.HomeUserAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import androidx.appcompat.widget.Toolbar;
public class ChairmanStudentAnnouncement extends AppCompatActivity implements OnItemClickListener{

	private Button showAll,from1,to1;
	private TextView from,to;
	private ListView chairmanStudentAnnouncementListView;
	private JSONArray chairmanStudentAnnouncementArray;
	ChairmanAnnouncementListAdapter chairmanAnnouncementListAdapter;
	private DatePicker datePicker;
	private Calendar calendar;
	private int year, month, day,year1,month1,day1;
	String date="",date1,date2="",date3;
	String Month;
	DatePickerDialog.OnDateSetListener myDateListener,myDateListener1;
	String crrDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	Date date4,date5,crrDate1,crrDate2;
	int pick=0;
	private JSONArray schoolNotificationListArray;
	String url1;
	
	private DrawerLayout drawerlayout;
	private ActionBarDrawerToggle drawerListener;
	ActionBar actionBar;
	private ListView listView;
	private HomeUserAdapter homeUserAdapter;
	private FloatingActionButton fab;
	SwipyRefreshLayout swipyRefreshLayout;
	ArrayList<String> permissionArray1 = new ArrayList<String>();
	
	int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
		getSupportActionBar().setTitle("Announcements");
		setContentView(R.layout.activity_hairman_student_announcements);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman SchoolAnnouncement");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		listView = (ListView) findViewById(R.id.drawerList);
		homeUserAdapter = new HomeUserAdapter(this,permissionArray1);
		listView.setAdapter(homeUserAdapter);
		listView.setOnItemClickListener(this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		drawerListener = new ActionBarDrawerToggle(ChairmanStudentAnnouncement.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close)
		{
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub

			}
		};



		fab = (FloatingActionButton) findViewById(R.id.fab);
		//fab.setVisibility(View.INVISIBLE);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NewAnnouncement.class);
				startActivity(intent);
			}
		});

		drawerlayout.setDrawerListener(drawerListener);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		//drawerlayout.openDrawer(listView);
		

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		//Utils.showToast(getApplicationContext(), ""+crrDate);

		year1 = calendar.get(Calendar.YEAR);
		month1 = calendar.get(Calendar.MONTH);
		day1 = calendar.get(Calendar.DAY_OF_MONTH);


		showAll = (Button) findViewById(R.id.btn_ch_showAll);
		from = (TextView) findViewById(R.id.text_ch_from_announce);
		to = (TextView) findViewById(R.id.text_ch_to_announce);
		from1 = (Button) findViewById(R.id.btn_ch_from_announce);
		to1 = (Button) findViewById(R.id.btn_ch_to_announce);
		
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		chairmanStudentAnnouncementListView = (ListView) findViewById(R.id.listView_ch_announcement);
		initData();
		getStudentAnnouncementList();

		swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh(SwipyRefreshLayoutDirection direction) {
				swipyRefreshLayout.setRefreshing(true);
				if(pick == 0) {
					initData();
					getStudentAnnouncementList();
				}

				else
				{
					initData();
					getStudentAnnouncementList1();
				}
			}
		});

		from1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(99);
				showDate(year, month, day);

			}
		});

		showAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				initData();
				getStudentAnnouncementList();

				to1.setText("");
				from1.setText("");

			}
		});

		to1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(999);
				showDate1(year, month, day);

			}
		});

		chairmanStudentAnnouncementListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub

				Intent intent;

				try {
					/*if(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("image") ||chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("txt"))
					{
						
						intent = new Intent(ChairmanStudentAnnouncement.this,ChairmanStudentAnnouncementImageResult.class);
						intent.putExtra("position", position);
						intent.putExtra("from", date);
						intent.putExtra("to", date2);
						startActivity(intent);
						
					}
					else if(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("text") ||chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("Text") || chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("TEXT"))
					{

						intent = new Intent(ChairmanStudentAnnouncement.this,ChairmanAnnouncementTextResult.class);
						intent.putExtra("position", position);
						intent.putExtra("from", date);
						intent.putExtra("to", date2);
						startActivity(intent);

					}
					else if(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("video"))
					{
						intent = new Intent(ChairmanStudentAnnouncement.this,ChairmanStudentAnnouncementVideoResult.class);
						intent.putExtra("position", position);
						intent.putExtra("from", date);
						intent.putExtra("to", date2);
						startActivity(intent);
					}

					else if(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("audio"))
					{
						intent = new Intent(ChairmanStudentAnnouncement.this,ChairmanStudentAnnouncementAudioResult.class);
						intent.putExtra("position", position);
						intent.putExtra("from", date);
						intent.putExtra("to", date2);
						startActivity(intent);
					}
					else if(chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("pdf")||chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_type").equals("ppt"))
					{*/
						intent = new Intent(ChairmanStudentAnnouncement.this,AnnouncementPDFResult.class);
						intent.putExtra("url",chairmanStudentAnnouncementArray.getJSONObject(position).getString("image_path"));
						intent.putExtra("title",chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement_title"));
						intent.putExtra("description",chairmanStudentAnnouncementArray.getJSONObject(position).getString("announcement"));
						startActivity(intent);
					/*}

					else
					{
						Utils.showToast(ChairmanStudentAnnouncement.this,"No Records Found");
					}*/
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	

	private void initData() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanStudentAnnouncementArray= null;
			}
			else
			{
				chairmanStudentAnnouncementArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentAnnouncementArray!= null)
		{
			chairmanAnnouncementListAdapter= new ChairmanAnnouncementListAdapter(ChairmanStudentAnnouncement.this,chairmanStudentAnnouncementArray);
			chairmanStudentAnnouncementListView.setAdapter(chairmanAnnouncementListAdapter);
			chairmanAnnouncementListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentAnnouncementList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentAnnouncement.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId;
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
						chairmanStudentAnnouncementListView.setVisibility(View.INVISIBLE);
						Utils.showToast(getApplicationContext(),"No Records Found");
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							chairmanStudentAnnouncementListView.setVisibility(View.INVISIBLE);
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Announcement_sch"))
							{
								chairmanStudentAnnouncementArray= new JSONObject(response).getJSONArray("Announcement_sch");
								if(null!=chairmanStudentAnnouncementArray && chairmanStudentAnnouncementArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = chairmanStudentAnnouncementArray.toString().getBytes();
									chairmanStudentAnnouncementListView.setVisibility(View.VISIBLE);
									VolleySingleton.getInstance(ChairmanStudentAnnouncement.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanStudentAnnouncementListView.invalidateViews();
									chairmanAnnouncementListAdapter = new ChairmanAnnouncementListAdapter(ChairmanStudentAnnouncement.this, chairmanStudentAnnouncementArray);
									chairmanStudentAnnouncementListView.setAdapter(chairmanAnnouncementListAdapter);
									chairmanAnnouncementListAdapter.notifyDataSetChanged();	
									count = 0;
									swipyRefreshLayout.setRefreshing(false);
								}
							}
							else
								Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 99) {
			return new DatePickerDialog(this, myDateListener11, year, month, day);
		}

		else

		{
			return new DatePickerDialog(this, myDateListener12, year1, month1, day1);
		}

	}

	DatePickerDialog.OnDateSetListener myDateListener11 = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2+1, arg3);
		}
	};

	DatePickerDialog.OnDateSetListener myDateListener12 = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate1(arg1, arg2+1, arg3);
		}
	};












	private void showDate(int year, int month, int day) {



		switch(month)
		{
		case 1: Month = "Jan";
		break;
		case 2: Month = "Feb";
		break;
		case 3: Month = "Mar";
		break;
		case 4: Month = "Apr";
		break;
		case 5: Month = "May";
		break;
		case 6: Month = "Jun";
		break;
		case 7: Month = "Jul";
		break;
		case 8: Month = "Aug";
		break;
		case 9: Month = "Sep";
		break;
		case 10: Month = "Oct";
		break;
		case 11: Month = "Nov";
		break;
		case 12: Month = "Dec";
		break;
		default : System.out.println("llll");

		break;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

		date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
		from1.setText(date1);

		try {
			date5 = formatter.parse(date);
			crrDate1 = formatter.parse(crrDate);

			if(crrDate1.compareTo(date5)<0)
			{
				Utils.showToast(getApplicationContext(), "Not Valid");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void showDate1(int year1, int month1, int day1) {



		switch(month1)
		{
		case 1: Month = "Jan";
		break;
		case 2: Month = "Feb";
		break;
		case 3: Month = "Mar";
		break;
		case 4: Month = "Apr";
		break;
		case 5: Month = "May";
		break;
		case 6: Month = "Jun";
		break;
		case 7: Month = "Jul";
		break;
		case 8: Month = "Aug";
		break;
		case 9: Month = "Sep";
		break;
		case 10: Month = "Oct";
		break;
		case 11: Month = "Nov";
		break;
		case 12: Month = "Dec";
		break;
		default : System.out.println("llll");

		break;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		date2 = String.valueOf(year1)+"-"+String.valueOf(month1)+"-"+String.valueOf(day1);

		date3 = String.valueOf(day1)+"-"+Month+"-"+String.valueOf(year1);

		try {
			date4 = formatter.parse(date2);
			crrDate2 = formatter.parse(crrDate);
			if(crrDate2.compareTo(date4)<0)
			{
				Utils.showToast(getBaseContext(), "Not Valid");
			}

			else
				if(date5.compareTo(date4)>0)
				{
					Utils.showToast(getApplicationContext(), "Not Valid");
				}

				else
				{
					to1.setText(date3);
					//System.out.println("kkk");
					pick =1;
					initData1();
					getStudentAnnouncementList1();
				}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	private void initData1() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT_DATE+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+date+"&to_date="+date2+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanStudentAnnouncementArray= null;
			}
			else
			{
				chairmanStudentAnnouncementArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentAnnouncementArray!= null)
		{
			chairmanAnnouncementListAdapter= new ChairmanAnnouncementListAdapter(ChairmanStudentAnnouncement.this,chairmanStudentAnnouncementArray);
			chairmanStudentAnnouncementListView.setAdapter(chairmanAnnouncementListAdapter);
			chairmanAnnouncementListAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentAnnouncementList1() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentAnnouncement.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT_DATE+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+date+"&to_date="+date2+"&device_id="+Preferences.getInstance().deviceId;
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
						Utils.showToast(getApplicationContext(),"No Records Found");
						chairmanStudentAnnouncementListView.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							chairmanStudentAnnouncementListView.setVisibility(View.INVISIBLE);
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Announcement_sch"))
							{

								chairmanStudentAnnouncementArray= new JSONObject(response).getJSONArray("Announcement_sch");
								if(null!=chairmanStudentAnnouncementArray && chairmanStudentAnnouncementArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = chairmanStudentAnnouncementArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanStudentAnnouncement.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ANNOUNCEMENT_DATE+"?u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+date+"&to_date="+date2+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanStudentAnnouncementListView.invalidateViews();
									chairmanStudentAnnouncementListView.setVisibility(View.VISIBLE);
									chairmanAnnouncementListAdapter = new ChairmanAnnouncementListAdapter(ChairmanStudentAnnouncement.this, chairmanStudentAnnouncementArray);
									chairmanStudentAnnouncementListView.setAdapter(chairmanAnnouncementListAdapter);
									chairmanAnnouncementListAdapter.notifyDataSetChanged();	
									count = 1;
									swipyRefreshLayout.setRefreshing(false);
								}
							}
							else
								Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
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
		if(Utils.isNetworkAvailable(getApplicationContext()))
			queue.add(requestObject);
		else
		{
			Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this,ChairmanDashboard.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(ChairmanStudentAnnouncement.this,ChairmanDashboard.class);
		startActivity(intent);
		this.finish();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chairman_school_attendance, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if(drawerListener.onOptionsItemSelected(item))
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub


		if(Preferences.getInstance().userRoleId.matches("7"))
		{


		switch(position)
		{
		case 0 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 1:
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
			
			

		case 2:	if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
			startActivity(intent);
			finish();

			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
		{

			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
			startActivity(intent);
			finish();
			break;
		}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
			Preferences.getInstance().isLoggedIn = false;
			Preferences.getInstance().savePreference(getApplicationContext());
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
		{
			Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

			startActivity(intent);
			finish();
			break;
		}

			break;

		case 3 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 4 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 5 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

			case 6 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;


		case 7 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;


		case 8 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 9 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 10 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 11 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
			
		case 12 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

			case 13:


				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 14:

				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 15:

				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 16:

				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}
				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}


				break;

			case 17:

				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

			case 18:

				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}



				break;

			case 19:

				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}



				break;


		default:

			break;
		
		}
		}
		
		else
		{
			switch(position)
			{
			case 0:	if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

				break;

			case 1:	if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				/*Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();*/
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

				break;

			case 2 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 3 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 4 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

				case 5 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;


			case 6 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 7 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 8 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 9 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 11 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;
				
			case 12 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

				case 13:

					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 14:

					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 15:

					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 16:

					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;


				case 17:

					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 18:

					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentResult.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS")) {
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentAnnouncement.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

			default:

				break;
			}
		}

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
						Intent intent = new Intent(ChairmanStudentAnnouncement.this,AboutSchool.class);
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

								Intent intent = new Intent(ChairmanStudentAnnouncement.this, WebViewActivity.class);
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
