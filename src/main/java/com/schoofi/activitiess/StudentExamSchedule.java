package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import com.schoofi.adapters.StudentListViewExamScheduleAdapter;
import com.schoofi.adapters.StudentResultSpinnerAdapter;
import com.schoofi.adapters.TermAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.StudentExamVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentExamSchedule extends AppCompatActivity {
	
	String userEmailId = Preferences.getInstance().userEmailId;
	String userId = Preferences.getInstance().userId;
	String token = Preferences.getInstance().token;
	String sectionId = Preferences.getInstance().studentSectionId;
	String schoolId = Preferences.getInstance().schoolId;
	String classId = Preferences.getInstance().studentClassId;
	
	JSONObject jsonobject;
	JSONArray jsonarray;
	JSONArray termArray,studentResultSpinnerArray;
	ProgressDialog mProgressDialog;
	private JSONArray studentExamArray;
	StudentListViewExamScheduleAdapter studentListViewExamScheduleAdapter;
	private ListView studentExamScheduleListView;
	private LinearLayout linearLayout;
	
	ArrayList<String> examName;

	ArrayList<StudentExamVO> examId;
	String examId1;
	private TextView title,noRecords,selectTerm,selectExam;
	private ImageView imageBack;
	private Spinner examNames;
	StudentExamVO studentExamVO = new StudentExamVO();
	String termId="";
	private SwipyRefreshLayout swipyRefreshLayout;
	ListView listView1,listView2;
	PopupWindow popupWindow1,popupWindow2;
	TermAdapter termAdapter;
	StudentResultSpinnerAdapter studentResultSpinnerAdapter;
	//DialogListAdapter dialogListAdapter;
	int width;
	int pos;
	JSONArray jsArray;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_exam_schedule);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student ExamSchedule");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		title = (TextView) findViewById(R.id.txt_examSchedule);
		imageBack = (ImageView) findViewById(R.id.img_back);
		//examNames = (Spinner) findViewById(R.id.spinExamNames);
		selectTerm = (TextView) findViewById(R.id.textNewExam);
		selectExam = (TextView) findViewById(R.id.textReset);
		studentExamScheduleListView = (ListView) findViewById(R.id.student_exam_schedule_listView);
		linearLayout = (LinearLayout) findViewById(R.id.linear_exam_title);
		noRecords = (TextView) findViewById(R.id.newView1);
		imageBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;



		listView1 = new ListView(getApplicationContext());
		listView2 = new ListView(getApplicationContext());


		popupWindow1 = TermListWindow();


		//linearLayout.setVisibility(View.INVISIBLE);
		
		//{"e_Name":[{"exam_id":"1","exam_name":"SA1"},{"exam_id":"2","exam_name":"SA2"}]}
		//new DownloadJSON().execute();
		 

				//linearLayout.setVisibility(View.INVISIBLE);
				
				studentExamScheduleListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(StudentExamSchedule.this,StudentExamScheduleDetail.class);
						intent.putExtra("position",position);
						intent.putExtra("examID", examId1);
						intent.putExtra("termId",termId);
						startActivity(intent);
						
					}
				});

		selectTerm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow1.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			}
		});


		selectExam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(termId.matches("") || termId.matches("null"))
				{
					Utils.showToast(getApplicationContext(),"Please select the term");
				}

				else
				if(termId.matches("OT"))
				{
					Utils.showToast(getApplicationContext(),"No exams for final term");

				}

				else {

					popupWindow2 = ExamListWindow();
					popupWindow2.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				}
			}
		});

		listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				view.startAnimation(fadeInAnimation);

				popupWindow2.dismiss();

				try {
					selectExam.setText(studentResultSpinnerArray.getJSONObject(i).getString("exam_name"));
					examId1 = studentResultSpinnerArray.getJSONObject(i).getString("exam_id");
					selectExam.setTextColor(Color.BLACK);



					if(examId1.matches("0"))
					{
						Log.d("hhhh", "kkk");
					}

					else
					{
						studentExamScheduleListView.setVisibility(View.VISIBLE);
						initData();
						getStudentExamList();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		});

		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

				Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				view.startAnimation(fadeInAnimation);

				popupWindow1.dismiss();

				try {
					selectTerm.setText(termArray.getJSONObject(position).getString("full_name"));
					termId = termArray.getJSONObject(position).getString("code");

					if(termId.matches("OT"))
					{
						selectTerm.setTextColor(Color.BLACK);
						selectExam.setText("Select Exam");
						selectExam.setTextColor(Color.GRAY);
						examId1="";
						studentExamScheduleListView.setVisibility(View.VISIBLE);
						initData();
						getStudentExamList();
					}

					else
					{
						selectTerm.setTextColor(Color.BLACK);
						selectExam.setText("Select Exam");
						selectExam.setTextColor(Color.GRAY);
						studentExamScheduleListView.setVisibility(View.INVISIBLE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
				
				
		
		
		
	}

	public PopupWindow TermListWindow()
	{
		PopupWindow popupWindow = new PopupWindow(this);



		popupWindow.setFocusable(true);
		popupWindow.setWidth(width);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		//initData3();
		getChairmanExamClassList();


		popupWindow.setContentView(listView1);

		return popupWindow;
	}

	public PopupWindow ExamListWindow()
	{
		PopupWindow popupWindow2 = new PopupWindow(this);



		popupWindow2.setFocusable(true);
		popupWindow2.setWidth(width);
		popupWindow2.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		initData4();
		getChairmanExamClassList1();


		popupWindow2.setContentView(listView2);

		return popupWindow2;
	}

	/*private void initData3()
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1);
			if(e == null)
			{
				termArray= null;
			}
			else
			{
				termArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(termArray!= null)
		{
			termAdapter= new TermAdapter(StudentExamSchedule.this,termArray);
			listView1.setAdapter(termAdapter);
			termAdapter.notifyDataSetChanged();
		}
	}*/

	protected void getChairmanExamClassList()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				//System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					//toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(StudentExamSchedule.this,"No Records Found");
						//mChart.setVisibility(View.INVISIBLE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(StudentExamSchedule.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("Typelist"))
					{
						//mChart.setVisibility(View.VISIBLE);
						termArray= new JSONObject(response).getJSONArray("Typelist");
						if(null!=termArray && termArray.length()>=0)
						{

							com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
							e.data = termArray.toString().getBytes();
							VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
							ArrayList<String> list = new ArrayList<String>();

							for(int i=0;i<termArray.length();i++)
							{
								if(termArray.getJSONObject(i).getString("code").matches("OT"))
								{
									pos=i;
								}
							}

							for(int j=0;j<termArray.length();j++)
							{
								list.add(termArray.get(j).toString());
							}
							//list.remove(pos);
							String list1= list.toString();
							String list2 = "{"+'"'+"Typelist"+'"'+":"+list1+"}";
							JSONObject jsonObject = new JSONObject(list2);
							jsArray = jsonObject.getJSONArray("Typelist");
							System.out.println(jsArray.toString());
							listView1.invalidateViews();
							termAdapter= new TermAdapter(StudentExamSchedule.this,jsArray);
							listView1.setAdapter(termAdapter);
							termAdapter.notifyDataSetChanged();
						}
					}
					else
						Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				setProgressBarIndeterminateVisibility(false);
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(StudentExamSchedule.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("value","2");

				//params.put("stu_id",Preferences.getInstance().studentId);


				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("board",Preferences.getInstance().board);
				params.put("session",Preferences.getInstance().session1);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				return params;
			}};

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

	private void initData4()
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST1+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1);
			if(e == null)
			{
				studentResultSpinnerArray= null;
			}
			else
			{
				studentResultSpinnerArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentResultSpinnerArray!= null)
		{
			studentResultSpinnerAdapter= new StudentResultSpinnerAdapter(StudentExamSchedule.this,studentResultSpinnerArray);
			listView2.setAdapter(studentResultSpinnerAdapter);
			studentResultSpinnerAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanExamClassList1()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST1+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					//toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(StudentExamSchedule.this,"No Records Found");
						//mChart.setVisibility(View.INVISIBLE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(StudentExamSchedule.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("e_Name"))
					{
						//mChart.setVisibility(View.VISIBLE);
						studentResultSpinnerArray= new JSONObject(response).getJSONArray("e_Name");
						if(null!=studentResultSpinnerArray && studentResultSpinnerArray.length()>=0)
						{
							com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
							e.data = studentResultSpinnerArray.toString().getBytes();
							VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST1+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
							listView2.invalidateViews();
							studentResultSpinnerAdapter= new StudentResultSpinnerAdapter(StudentExamSchedule.this,studentResultSpinnerArray);
							listView2.setAdapter(studentResultSpinnerAdapter);
							studentResultSpinnerAdapter.notifyDataSetChanged();
						}
					}
					else
						Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
				setProgressBarIndeterminateVisibility(false);
			}
		})
		{
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(StudentExamSchedule.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("term",termId);

				//params.put("stu_id",Preferences.getInstance().studentId);


				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("sec_id", Preferences.getInstance().studentSectionId);
				params.put("session",Preferences.getInstance().session1);
				params.put("board",Preferences.getInstance().board);
				//params.put("crr_date",currentDate);
				return params;
			}};

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
	
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {
		 
		@Override
		protected Void doInBackground(Void... params) {
				
			
			// Create an array to populate the spinner 
			examId = new ArrayList<StudentExamVO>();
			examName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/
			
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("e_Name");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					StudentExamVO studentExamVO1 = new StudentExamVO();
					
					studentExamVO1.setExamId(jsonobject.optString("exam_id"));
					examId.add(studentExamVO1);
					
					examName.add(jsonobject.optString("exam_name"));
					
				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml
			
			examNames
			.setAdapter(new ArrayAdapter<String>(StudentExamSchedule.this,
					android.R.layout.simple_spinner_dropdown_item,
					examName));
			
			examNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					
					examId1 = examId.get(position).getExamId().toString();
					//System.out.println(examId1);
					try
					{
						Entry e;
						e = VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId);
						if(e == null)
						{
							studentExamArray= null;
						}
						else
						{
							studentExamArray = new JSONArray(new String(e.data));
						}
					}
					catch(JSONException e)
					{
						e.printStackTrace();
					}

					if(studentExamArray!= null)
					{
						studentListViewExamScheduleAdapter= new StudentListViewExamScheduleAdapter(StudentExamSchedule.this,studentExamArray);
						studentExamScheduleListView.setAdapter(studentListViewExamScheduleAdapter);
						studentListViewExamScheduleAdapter.notifyDataSetChanged();
					}
					
					RequestQueue queue = VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue();
					String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId;
					//System.out.println(url);
					StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {

							JSONObject responseObject;
							try 
							{
								responseObject = new JSONObject(response);
								//System.out.println(response);
								//toa();
								if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
								{
									linearLayout.setVisibility(View.VISIBLE);
									studentExamScheduleListView.setVisibility(View.INVISIBLE);
								}
								else
									if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
									{
										Utils.showToast(StudentExamSchedule.this, "Session Expired:Please Login Again");
									}
									else
										if(responseObject.has("Exam"))
										{
											noRecords.setVisibility(View.INVISIBLE);
											studentExamScheduleListView.setVisibility(View.VISIBLE);
											studentExamArray= new JSONObject(response).getJSONArray("Exam");
											if(null!=studentExamArray && studentExamArray.length()>=0)
											{
												Entry e = new Entry();
												e.data = studentExamArray.toString().getBytes();
												VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId,e);
												studentExamScheduleListView.invalidateViews();
												studentListViewExamScheduleAdapter= new StudentListViewExamScheduleAdapter(StudentExamSchedule.this, studentExamArray);
												studentExamScheduleListView.setAdapter(studentListViewExamScheduleAdapter);
												studentListViewExamScheduleAdapter.notifyDataSetChanged();	
											}
										}
										else
										{
											Utils.showToast(StudentExamSchedule.this, responseObject.getString("errorMessage"));
										}
							}
							catch(JSONException e)
							{
								e.printStackTrace();
								Utils.showToast(StudentExamSchedule.this, "Error fetching modules! Please try after sometime.");
							}

						}}, new Response.ErrorListener() 
						{
							@Override
							public void onErrorResponse(VolleyError error) 
							{
								Utils.showToast(StudentExamSchedule.this, "Error fetching modules! Please try after sometime.");
							}
						})
					{
						/*@Override
						protected Map<String,String> getParams(){
							Map<String,String> params = new HashMap<String, String>();
							Preferences.getInstance().loadPreference(context);
							params.put("sec_id",Preferences.getInstance().studentSectionId);
							params.put("stu_id", Preferences.getInstance().studentId);
							params.put("sch_id", Preferences.getInstance().schoolId);
							params.put("token", Preferences.getInstance().token);
							params.put("u_email_id",Preferences.getInstance().userEmailId);
							params.put("u_id", Preferences.getInstance().userId);
							params.put("cls_id", Preferences.getInstance().studentClassId);
							params.put("date", date);
							return params;
						}*/};		

						requestObject.setRetryPolicy(new DefaultRetryPolicy(
								25000, 
								DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
								DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
						if(Utils.isNetworkAvailable(StudentExamSchedule.this))
							queue.add(requestObject);
						else
						{
							Utils.showToast(StudentExamSchedule.this, "Unable to fetch data, kindly enable internet settings!");
						}

					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
			
			
			
	
		}
	}

	protected void initData()
	{
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&term="+termId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				studentExamArray= null;
			}
			else
			{
				studentExamArray = new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentExamArray!= null)
		{
			studentListViewExamScheduleAdapter= new StudentListViewExamScheduleAdapter(StudentExamSchedule.this,studentExamArray);
			studentExamScheduleListView.setAdapter(studentListViewExamScheduleAdapter);
			studentListViewExamScheduleAdapter.notifyDataSetChanged();
		}
	}

	protected void getStudentExamList()
	{
		RequestQueue queue = VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue();
		String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&term="+termId+"&ins_id="+Preferences.getInstance().institutionId;
		//System.out.println(url);
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try
				{
					responseObject = new JSONObject(response);
					System.out.println(response);
					//toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						linearLayout.setVisibility(View.VISIBLE);
						studentExamScheduleListView.setVisibility(View.INVISIBLE);
					}
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(StudentExamSchedule.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("Exam"))
					{
						noRecords.setVisibility(View.INVISIBLE);
						studentExamScheduleListView.setVisibility(View.VISIBLE);
						studentExamArray= new JSONObject(response).getJSONArray("Exam");
						if(null!=studentExamArray && studentExamArray.length()>=0)
						{
							Entry e = new Entry();
							e.data = studentExamArray.toString().getBytes();
							VolleySingleton.getInstance(StudentExamSchedule.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EXAM_SCHEDULE_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&sec_id="+sectionId+"&sch_id="+schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1+"&term="+termId+"&ins_id="+Preferences.getInstance().institutionId,e);
							studentExamScheduleListView.invalidateViews();
							studentListViewExamScheduleAdapter= new StudentListViewExamScheduleAdapter(StudentExamSchedule.this, studentExamArray);
							studentExamScheduleListView.setAdapter(studentListViewExamScheduleAdapter);
							studentListViewExamScheduleAdapter.notifyDataSetChanged();
						}
					}
					else
					{
						Utils.showToast(StudentExamSchedule.this, responseObject.getString("errorMessage"));
					}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentExamSchedule.this, "Error fetching modules! Please try after sometime.");
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(StudentExamSchedule.this, "Error fetching modules! Please try after sometime.");
			}
		})
		{
						/*@Override
						protected Map<String,String> getParams(){
							Map<String,String> params = new HashMap<String, String>();
							Preferences.getInstance().loadPreference(context);
							params.put("sec_id",Preferences.getInstance().studentSectionId);
							params.put("stu_id", Preferences.getInstance().studentId);
							params.put("sch_id", Preferences.getInstance().schoolId);
							params.put("token", Preferences.getInstance().token);
							params.put("u_email_id",Preferences.getInstance().userEmailId);
							params.put("u_id", Preferences.getInstance().userId);
							params.put("cls_id", Preferences.getInstance().studentClassId);
							params.put("date", date);
							return params;
						}*/};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if(Utils.isNetworkAvailable(StudentExamSchedule.this))
			queue.add(requestObject);
		else
		{
			Utils.showToast(StudentExamSchedule.this, "Unable to fetch data, kindly enable internet settings!");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_exam_schedule, menu);
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
