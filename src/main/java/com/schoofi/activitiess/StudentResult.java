package com.schoofi.activitiess;

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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.schoofi.adapters.ChairmanClassExamListAdapter;
import com.schoofi.adapters.DialogListAdapter;
import com.schoofi.adapters.StudentResultListAdapter;
import com.schoofi.adapters.StudentResultSpinnerAdapter;
import com.schoofi.adapters.TermAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.StudentResultExamVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentResult extends AppCompatActivity {
	
	//private Spinner examNameSpinner;
	private TextView screenTitle,newView,subjectName,subjectMarks,totalMarks,total,percentageText,percentageText1,subjectWeigtedMarks;
	private ListView studentResultListView;
	JSONObject jsonobject;
	JSONArray jsonarray,termArray,studentResultSpinnerArray;
	ImageView back;
	ArrayList<String> resultExamNames;
	ArrayList<StudentResultExamVO> resultExamId;
	String resultExamId1;
	private JSONArray studentResultArray;
	StudentResultListAdapter studentResultListAdapter;
	float total1=0,totalObtained=0;
	float percentage;
	TextView term,grade;
	ListView listView1,listView2;
	LinearLayout linearLayout;
	PopupWindow popupWindow1,popupWindow2;
	TermAdapter termAdapter;
	StudentResultSpinnerAdapter studentResultSpinnerAdapter;
	//DialogListAdapter dialogListAdapter;
	int width;
	String termId="";
	Button next;
	TextView report;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_student_result);
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Student Result");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		Preferences.getInstance().loadPreference(StudentResult.this);
		
		screenTitle = (TextView) findViewById(R.id.txt_result);
		//examNameSpinner = (Spinner) findViewById(R.id.spinnerExamResultList);
		newView = (TextView) findViewById(R.id.newView);
		subjectName = (TextView) findViewById(R.id.text_studentSubject);
		
		subjectMarks = (TextView) findViewById(R.id.text_studentMarks);
		totalMarks = (TextView) findViewById(R.id.text_totalMark);
		total = (TextView) findViewById(R.id.text_totalMarks1);
		percentageText = (TextView) findViewById(R.id.text_percentage);
		percentageText1 = (TextView) findViewById(R.id.text_percentage1);
		back = (ImageView) findViewById(R.id.img_back);
		subjectWeigtedMarks = (TextView) findViewById(R.id.text_studentMarks1);
		linearLayout = (LinearLayout) findViewById(R.id.linearMarks);

		report = (TextView) findViewById(R.id.report);

		report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StudentResult.this,ResultPdfActivity.class);
				startActivity(intent);
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		term = (TextView) findViewById(R.id.textNewExam);
		grade = (TextView) findViewById(R.id.textReset);
		next = (Button) findViewById(R.id.btn_next);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;

		TextView marque = (TextView) this.findViewById(R.id.marque_scrolling_text);
		marque.setSelected(true);

		listView1 = new ListView(getApplicationContext());
		listView2 = new ListView(getApplicationContext());


		popupWindow1 = TermListWindow();

		next.setVisibility(View.GONE);

		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(termId.matches("") || termId.matches("null"))
				{
					Utils.showToast(getApplicationContext(),"Please select the term and exam");
				}

				else
				{
					Intent intent = new Intent(StudentResult.this,StudentResultPart2COScholasticAreas1.class);
					intent.putExtra("termId",termId);
					startActivity(intent);
				}
			}
		});


		
		
		studentResultListView = (ListView) findViewById(R.id.student_result_listView);

		studentResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(termId.matches("OT"))
				{

					try {
						Intent intent = new Intent(StudentResult.this,StudentResultDetails.class);
						intent.putExtra("examId",resultExamId1);
						intent.putExtra("subId",studentResultArray.getJSONObject(i).getString("sub_id"));
						startActivity(intent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
		newView.setVisibility(View.VISIBLE);

		term.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow1.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			}
		});

		grade.setOnClickListener(new View.OnClickListener() {
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
					grade.setText(studentResultSpinnerArray.getJSONObject(i).getString("exam_name"));
					resultExamId1 = studentResultSpinnerArray.getJSONObject(i).getString("exam_id");
					grade.setTextColor(Color.BLACK);

					total1 = 0;
					totalObtained = 0;

					if(resultExamId1.matches("0"))
					{
						Log.d("hhhh", "kkk");
					}

					else
					{
						studentResultListView.setVisibility(View.VISIBLE);
						getStudentResultList();
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
					term.setText(termArray.getJSONObject(position).getString("full_name"));
					termId = termArray.getJSONObject(position).getString("code");

					if(termId.matches("OT"))
					{
						term.setTextColor(Color.BLACK);
						grade.setText("Select Exam");
						grade.setTextColor(Color.GRAY);
						total1 = 0;
						totalObtained = 0;
						resultExamId1="";
						studentResultListView.setVisibility(View.VISIBLE);
						getStudentResultList();
					}

					else
					{
						term.setTextColor(Color.BLACK);
						grade.setText("Select Exam");
						grade.setTextColor(Color.GRAY);
						studentResultListView.setVisibility(View.INVISIBLE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
		
		/*if(Utils.isNetworkAvailable(getApplicationContext()))
		{
		new DownloadJSON().execute();
		
		
		}
		
		else
		{
			Utils.showToast(getApplicationContext(), "Kindly enable internet services");
		}*/
		
		
		
		
		
		
		
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		
		
		
	}


	public PopupWindow TermListWindow()
	{
		PopupWindow popupWindow = new PopupWindow(this);



		popupWindow.setFocusable(true);
		popupWindow.setWidth(width);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		initData3();
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

	private void initData3()
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
			termAdapter= new TermAdapter(StudentResult.this,termArray);
			listView1.setAdapter(termAdapter);
			termAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanExamClassList()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentResult.this).getRequestQueue();
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
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(StudentResult.this,"No Records Found");
						//mChart.setVisibility(View.INVISIBLE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(StudentResult.this, "Session Expired:Please Login Again");
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
							VolleySingleton.getInstance(StudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
							listView1.invalidateViews();
							termAdapter= new TermAdapter(StudentResult.this,termArray);
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

				Preferences.getInstance().loadPreference(StudentResult.this);
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
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1);
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
			studentResultSpinnerAdapter= new StudentResultSpinnerAdapter(StudentResult.this,studentResultSpinnerArray);
			listView2.setAdapter(studentResultSpinnerAdapter);
			studentResultSpinnerAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanExamClassList1()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentResult.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
						Utils.showToast(StudentResult.this,"No Records Found");
						//mChart.setVisibility(View.INVISIBLE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(StudentResult.this, "Session Expired:Please Login Again");
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
							VolleySingleton.getInstance(StudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
							listView2.invalidateViews();
							studentResultSpinnerAdapter= new StudentResultSpinnerAdapter(StudentResult.this,studentResultSpinnerArray);
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

				Preferences.getInstance().loadPreference(StudentResult.this);
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
	
	/*private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {


			// Create an array to populate the spinner 
			resultExamId= new ArrayList<StudentResultExamVO>();
			resultExamNames = new ArrayList<String>();
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STUDENT_RESULT_EXAM_NAME_LIST+"?sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("e_Name");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					//FeedbackVO feedbackVO = new FeedbackVO();
					StudentResultExamVO studentResultExamVO = new StudentResultExamVO();
					studentResultExamVO.setResultExamId(jsonobject.optString("exam_id"));
					resultExamId.add(studentResultExamVO);
					resultExamNames.add(jsonobject.optString("exam_name"));

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

			examNameSpinner
			.setAdapter(new ArrayAdapter<String>(StudentResult.this,
					android.R.layout.simple_spinner_dropdown_item,
					resultExamNames));

			examNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					total1 = 0;
					totalObtained = 0;

					resultExamId1 = resultExamId.get(position).getResultExamId().toString();
					//System.out.println("hh"+resultExamId1);
					
					if(resultExamId1.matches("0"))
					{
					    Log.d("hhhh", "kkk");
					}
					
					else
					{
					getStudentResultList();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}


			});


		}
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_result, menu);
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
	
	private void initData() {


		try {
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_EXAM_LIST + "?sec_id=" + Preferences.getInstance().studentSectionId + "&sch_id=" + Preferences.getInstance().schoolId + "&cls_id=" + Preferences.getInstance().studentClassId + "&stu_id=" + Preferences.getInstance().studentId + "&ex_id=" + resultExamId1 + "&u_email_id=" + Preferences.getInstance().userEmailId + "&u_id=" + Preferences.getInstance().userId + "&token=" + Preferences.getInstance().token + "&device_id=" + Preferences.getInstance().deviceId + "&term=" + termId + "&session=" + Preferences.getInstance().session1 + "&board=" + Preferences.getInstance().board + "ins_id=" + Preferences.getInstance().institutionId);
			if (e == null) {
				studentResultArray = null;
			} else {
				studentResultArray = new JSONArray(new String(e.data));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (studentResultArray != null) {
			try {
				if (studentResultArray.getJSONObject(0).getString("display_option").matches("G")) {
					subjectMarks.setVisibility(View.INVISIBLE);
				} else if (studentResultArray.getJSONObject(0).getString("display_option").matches("M")) {
					if (studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("actual")) {
						subjectWeigtedMarks.setVisibility(View.INVISIBLE);
						subjectMarks.setVisibility(View.VISIBLE);
					} else if (studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("weighted")) {
						subjectMarks.setVisibility(View.INVISIBLE);
						subjectWeigtedMarks.setVisibility(View.VISIBLE);
					} else if (studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("both")) {
						subjectMarks.setVisibility(View.VISIBLE);
						subjectWeigtedMarks.setVisibility(View.VISIBLE);
					} else {
						if (studentResultArray.getJSONObject(0).getString("display_option").matches("GM")) {
							subjectMarks.setVisibility(View.VISIBLE);
							subjectWeigtedMarks.setVisibility(View.VISIBLE);
						} else
						if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("actual"))
						{
							subjectMarks.setVisibility(View.VISIBLE);
							subjectWeigtedMarks.setVisibility(View.VISIBLE);
						}

						else
						if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("weighted"))
						{
							subjectMarks.setVisibility(View.INVISIBLE);
							subjectWeigtedMarks.setVisibility(View.VISIBLE);
						}
					}

					studentResultListAdapter = new StudentResultListAdapter(StudentResult.this, studentResultArray);
					studentResultListView.setAdapter(studentResultListAdapter);
					studentResultListAdapter.notifyDataSetChanged();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void getStudentResultList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(StudentResult.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_EXAM_LIST+"?sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&stu_id="+Preferences.getInstance().studentId+"&ex_id="+resultExamId1+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&term="+termId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board+"&ins_id="+Preferences.getInstance().institutionId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				total1=0;
				totalObtained=0;
				try 
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Status")&&responseObject.getString("Status").equals("0"))
					{	
					newView.setVisibility(View.VISIBLE);
					studentResultListView.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							newView.setVisibility(View.INVISIBLE);
							Utils.showToast(StudentResult.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Result"))
							{
								newView.setVisibility(View.INVISIBLE);
								studentResultListView.setVisibility(View.VISIBLE);
								studentResultArray= new JSONObject(response).getJSONArray("Result");
								
								
								if(null!=studentResultArray && studentResultArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = studentResultArray.toString().getBytes();
									VolleySingleton.getInstance(StudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_RESULT_EXAM_LIST+"?sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&stu_id="+Preferences.getInstance().studentId+"&ex_id="+resultExamId1+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&term="+termId+"&session="+Preferences.getInstance().session1+"&board="+Preferences.getInstance().board+"&ins_id="+Preferences.getInstance().institutionId,e);
									studentResultListView.invalidateViews();
									studentResultListAdapter = new StudentResultListAdapter(StudentResult.this, studentResultArray);
									studentResultListView.setAdapter(studentResultListAdapter);
									studentResultListAdapter.notifyDataSetChanged();
									
									


									/*if(Preferences.getInstance().studentClassId.matches("cl11") || Preferences.getInstance().studentClassId.matches("cl12") || Preferences.getInstance().studentClassId.matches("cl13") || Preferences.getInstance().studentClassId.matches("cl14") || Preferences.getInstance().studentClassId.matches("cl14") || Preferences.getInstance().studentClassId.matches("cl18") || Preferences.getInstance().studentClassId.matches("cl19"))
									{
										total.setText(studentResultArray.getJSONObject(0).getString("total_marks")+"/"+studentResultArray.getJSONObject(0).getString("total_obtained"));
										percentageText1.setText(studentResultArray.getJSONObject(0).getString("final_percentage")+"% "+" ("+studentResultArray.getJSONObject(0).getString("final_grade")+")");
									}

									else
									{
										total.setVisibility(View.GONE);
										linearLayout.setVisibility(View.GONE);
										percentageText.setText("Final Grade: ");
										percentageText1.setText(studentResultArray.getJSONObject(0).getString("final_grade"));
									}*/

									if(studentResultArray.getJSONObject(0).getString("display_option").matches("G"))
									{
										subjectMarks.setVisibility(View.INVISIBLE);
									}

									else
										if(studentResultArray.getJSONObject(0).getString("display_option").matches("M")) {
											if (studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("actual")) {
												subjectWeigtedMarks.setVisibility(View.INVISIBLE);
												subjectMarks.setVisibility(View.VISIBLE);

											} else if (studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("weighted")) {
												subjectMarks.setVisibility(View.INVISIBLE);
												subjectWeigtedMarks.setVisibility(View.VISIBLE);
											} else if (studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("both")) {
												subjectMarks.setVisibility(View.VISIBLE);
												subjectWeigtedMarks.setVisibility(View.VISIBLE);
											}
										}

													else
													{
														if(studentResultArray.getJSONObject(0).getString("display_option").matches("GM"))
														{
															if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("both")) {
																subjectMarks.setVisibility(View.VISIBLE);
																subjectWeigtedMarks.setVisibility(View.VISIBLE);
															}

															else
																if(studentResultArray.getJSONObject(0).getString("rep_disp_opt").matches("actual"))
																{
																	subjectMarks.setVisibility(View.VISIBLE);
																	subjectWeigtedMarks.setVisibility(View.VISIBLE);
																}

																else

																	subjectMarks.setVisibility(View.INVISIBLE);
																	subjectWeigtedMarks.setVisibility(View.VISIBLE);

														}

														else
														{

														}


										}
									
									
									
									}

								}
							
							else
								Utils.showToast(StudentResult.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(StudentResult.this, "Error fetching modules! Please try after sometime.");
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
				
				Preferences.getInstance().loadPreference(StudentResult.this);
				
				Map<String,String> params = new HashMap<String, String>();
				params.put("stu_id",Preferences.getInstance().studentId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("ex_id",examId1);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				//params.put("crr_date",currentDate);
				return params;
			}*/};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(StudentResult.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(StudentResult.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		getStudentResultList();
	}
	
}
