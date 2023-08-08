package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
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
import com.schoofi.adapters.TeacherAttendanceDetailsListViewAdapter;
import com.schoofi.adapters.TeacherStudentSubmittedAttendanceListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

@SuppressWarnings("deprecation")
public class TeacherStudentAttendanceDetails extends AppCompatActivity {

	//http://semycolon.blogspot.in/2014/11/first-android-app-step-7-populate.html

	private JSONArray teacherStudentAttendanceArray1,teacherStudentSubmittedAttendancearray;
	private ListView teacherStudentAttendanceListViewDetails;
	TeacherAttendanceDetailsListViewAdapter teacherAttendanceDetailsListViewAdapter;
	TeacherStudentSubmittedAttendanceListViewAdapter teacherStudentSubmittedAttendanceListViewAdapter;
	String classId = Preferences.getInstance().studentClassId;
	String sectionId = Preferences.getInstance().studentSectionId;
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	private PopupWindow calendarPopup;
	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	private DatePicker datePicker;
	private Calendar calendar;
	private int year, month, day;
	private TextView dateView;
	String date1,date2;
	String year1,month1,day1;
	private Button submit;
	Date date3,date4;
	public boolean c,s;
	JSONObject jsonObject1,jo;
	int i;
	String array1,array2,array3,array4;
	public ArrayList<String> attendance = new ArrayList<String>();
	public ArrayList<String> attendanceName = new ArrayList<String>();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
	String selectedDate1,selectedDate2;
	Date selectedDate3;
	String dat;
	final Context context = this;
	TextView noOfStudents,selectedDate;
	HorizontalCalendar horizontalCalendar;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d3d3d3")));
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_teacher_student_attendance_details);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("TeacherStudent Attendance Details");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		Preferences.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);

		noOfStudents = (TextView) findViewById(R.id.text_no_of_students);
		selectedDate = (TextView) findViewById(R.id.attendance_date);




		submit = (Button) findViewById(R.id.btn_submitAttendanceList);
		submit.setText("Submit");
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, 0);

		/** start before 1 month from now */
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.MONTH, -12);



		horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
				.startDate(startDate.getTime())
				.endDate(endDate.getTime())
				.datesNumberOnScreen(7)
				.dayFormat("EEE")
				.dayNumberFormat("dd")
				.textColor(Color.LTGRAY, Color.WHITE)
				.selectedDateBackground(Color.TRANSPARENT)
				.build();

		horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
			@Override
			public void onDateSelected(Date date0, int position) {

				dat = new SimpleDateFormat("yyyy-MM-dd").format(date0);
				selectedDate1 = (dat);
				selectedDate2 = new SimpleDateFormat("dd-MMM-yyyy").format(date0);
				date = dat;

				try{






					selectedDate.setText(selectedDate2);
				}catch (Exception ex ){
					System.out.println("kgf"+ex);
				}

				//Toast.makeText(TeacherStudentAttendanceDetails.this, DateFormat.getDateInstance().format(date) + " is selected!"+sdf.format(date), Toast.LENGTH_SHORT).show();



				RequestQueue queue = VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue();
				final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST;
				StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						JSONObject responseObject;
						//System.out.println(response);
						//System.out.println(url);
						try {
							responseObject = new JSONObject(response);

							//System.out.println(res);
							toa();
							if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {

								Utils.showToast(TeacherStudentAttendanceDetails.this, "No Records Found");
								teacherStudentAttendanceListViewDetails.setVisibility(View.INVISIBLE);
							} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {

								Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
							}
							else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {
								teacherStudentAttendanceListViewDetails.setVisibility(View.INVISIBLE);
								Utils.showToast(TeacherStudentAttendanceDetails.this, "Today is Holiday! You can't take the attendance");
							}
							else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("1")) {

									/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherStudentAttendanceDetails.this);

									// Setting Dialog Title
									alertDialog.setTitle("Attendance already submitted");

									// Setting Dialog Message
									alertDialog.setMessage("Want to see it");

									// Setting Icon to Dialog
									//alertDialog.setIcon(R.drawable.calander_icon);

									// Setting Positive "Yes" Button
									alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int which) {*/

								// Write your code here to invoke YES event
								//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
								initData4();
								getTeacherStudentList2();
								s = true;
								submit.setEnabled(false);
								submit.setBackgroundResource(R.drawable.button_red_50_percent);
										/*}
									});

									// Setting Negative "NO" Button
									alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											// Write your code here to invoke NO event
											//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
											dialog.cancel();
										}
									});

									// Showing Alert Message
									alertDialog.show();*/
							} else if (responseObject.has("Students"))

							{
								s = false;
								submit.setEnabled(true);
								submit.setBackgroundResource(R.drawable.buttons);
								attendance = new ArrayList<String>();

								teacherStudentAttendanceArray1 = new JSONObject(response).getJSONArray("Students");
								String date8 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

								for (i = 0; i < teacherStudentAttendanceArray1.length(); i++) {
									teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent", teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_id") + "-P");
									teacherStudentAttendanceArray1.getJSONObject(i).put("crr_date", date8);
									teacherStudentAttendanceArray1.getJSONObject(i).put("u_email_id", Preferences.getInstance().userEmailId);
									teacherStudentAttendanceArray1.getJSONObject(i).put("position","0");
									teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent1","P");
									//teacherStudentAttendanceArray1.getJSONObject(i).put("count", i);
								}

								noOfStudents.setText("Total Students: "+String.valueOf(i));
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);

								//System.out.println(i);

								for (int j = 0; j < teacherStudentAttendanceArray1.length(); j++) {
									teacherStudentAttendanceArray1.getJSONObject(j).put("count", i);
									//teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
								}
								if (null != teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length() >= 0) {
									Entry e = new Entry();
									e.data = teacherStudentAttendanceArray1.toString().getBytes();
									VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&id_class=" + classId + "&id_class_section=" + sectionId + "&sch_id=" + schoolId + "&crr_date="/*+"2003-11-21"*/ + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId, e);
									teacherStudentAttendanceListViewDetails.invalidateViews();
									teacherAttendanceDetailsListViewAdapter = new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this, teacherStudentAttendanceArray1, 1);
									teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
									teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();
											/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
										Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
										Preferences.getInstance().savePreference(StudentFeedBack.this);*/
								}
							} else
								Utils.showToast(TeacherStudentAttendanceDetails.this, "Error Fetching Response");
							setSupportProgressBarIndeterminateVisibility(false);

						} catch (JSONException e) {
							e.printStackTrace();
							Utils.showToast(TeacherStudentAttendanceDetails.this, "Error fetching modules! Please try after sometime.");
							setSupportProgressBarIndeterminateVisibility(false);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
						setSupportProgressBarIndeterminateVisibility(false);
					}
				}) {
					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("sch_id", Preferences.getInstance().schoolId);
						params.put("id_class_section", Preferences.getInstance().studentSectionId);
						params.put("token", Preferences.getInstance().token);
						params.put("u_email_id", Preferences.getInstance().userEmailId);
						params.put("id_class", Preferences.getInstance().studentClassId);
						params.put("u_id", Preferences.getInstance().userId);
						String date8 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						params.put("crr_date", date);
						params.put("device_id", Preferences.getInstance().deviceId);
						params.put("ins_id",Preferences.getInstance().institutionId);
						return params;
					}
				};

				requestObject.setRetryPolicy(new DefaultRetryPolicy(
						25000,
						DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				if (Utils.isNetworkAvailable(TeacherStudentAttendanceDetails.this))
					queue.add(requestObject);
				else {
					Utils.showToast(TeacherStudentAttendanceDetails.this, "Unable to fetch data, kindly enable internet settings!");
				}
			}

		});


		teacherStudentAttendanceListViewDetails = (ListView) findViewById(R.id.listViewTeacherStudentAttendanceDetails);
		

		//initData();
		getTeacherStudentList();
		getSupportActionBar().setIcon(android.R.color.transparent);
		
		teacherStudentAttendanceListViewDetails.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				try {
					if(teacherStudentAttendanceArray1.getJSONObject(position).getString("status").matches("1"))
					{
						teacherStudentAttendanceListViewDetails.setEnabled(false);
					}
					
					else
					{
						teacherStudentAttendanceListViewDetails.setEnabled(true);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		/*System.out.println(date);
		System.out.println(classId);
		System.out.println(sectionId);
		System.out.println(userEmailId);
		System.out.println(userId);
		System.out.println(schoolId);
		System.out.println(token);
		 */
		
		/*jo = new JSONObject();
		try {
			jo.put("action", "par_action");
			jo.put("par_1", "1");
			jo.put("par_2", "2");
			jo.put("par_3", "3");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		/*submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				URL url = null;
				try {
					url = new URL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMIT);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost;
				try {
					httpPost = new HttpPost(url.toURI());
					// Prepare JSON to send by setting the entity
					httpPost.setEntity(new StringEntity(jo.toString(), "UTF-8"));

					// Set up the header types needed to properly transfer JSON
					httpPost.setHeader("Content-Type", "application/json");
					httpPost.setHeader("Accept-Encoding", "application/json");
					httpPost.setHeader("Accept-Language", "en-US");

					// Execute POST
					HttpResponse response = httpClient.execute(httpPost);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			}
		});*/
		
		
      


		

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*array1 = "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}";
				array1.replaceAll("\\\\s+","");
				array1.replaceAll("\\s+","");
				//array1.replaceAll("\\\\\\s+","");

				//System.out.println(array1);
				try {
					jsonObject1 = new JSONObject(array1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				postAttendance();
				System.out.println(array1);*/
				


				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.teacher_attendance_submit_dialog_box, null);



				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);




				final TextView present = (TextView) promptsView.findViewById(R.id.text_total_present);
				final TextView absent =  (TextView) promptsView.findViewById(R.id.text_total_absent);
				final TextView leave =   (TextView) promptsView.findViewById(R.id.text_total_leave);
				final TextView total =   (TextView) promptsView.findViewById(R.id.text_total);

				int present1=0,absent1=0,leave1=0,total1=0;

				for(int k =0;k<i;k++)
				{
					try {
						if(teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("P"))
						{
							present1 = present1+1;
						}

						else
						{

						}

						if(teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("A"))
						{
							absent1 = absent1+1;
						}

						else
						{

						}

						if(teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent1").matches("L"))
						{
							leave1 = leave1+1;
						}

						else
						{

						}
						attendance.add(teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("isPresent"));
						attendanceName.add(teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.getJSONObject(k).getString("stu_name"));

						total1=k;


					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				present.setText("Total Present: "+String.valueOf(present1));
				absent.setText("Total Absent: "+String.valueOf(absent1));
				leave.setText("Total Leave: "+String.valueOf(leave1));
				total.setText("Total Students: "+String.valueOf(total1+1));

				array1 = attendance.toString();
				array2 = array1.substring(1, array1.length()-1);
				array3 = attendanceName.toString();
				array4 = array3.substring(1,array3.length()-1);

				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										// get user input and set it to result


										// edit text

										postAttendance();

									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				//System.out.println(array2);


			}
		});



	}



	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//super.onResume();
		getTeacherStudentList();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_student_attendance_details,
				menu);
		SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
		searchView.setQueryHint("search");
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextChange(String newText)
			{
				
					teacherAttendanceDetailsListViewAdapter.getFilter().filter(newText);	
				
				
				
				return true;
			}
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				// this is your adapter that will be filtered
				
				teacherAttendanceDetailsListViewAdapter.getFilter().filter(query);
				
				
				
				return true;
			}
		};
		searchView.setOnQueryTextListener(textChangeListener);
		return true;
	}




	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.calender) {

			//dateView = (TextView) findViewById(R.id.textView3);

			//showDate(year, month+1, day);

			calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);

			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			showDialog(999);
			//Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
			//.show();

			showDate(year, month, day);








		}


		switch(item.getItemId())
		{
			case android.R.id.home:
				//NavUtils.navigateUpFromSameTask(this);
				this.finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	public void setDate(View view) {
		showDialog(999);
		//Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
		//.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2 + 1, arg3);
			/*try {
				date3 = sdf.parse(date);
				date4 = sdf.parse(date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (date4.after(date3)) {
				Utils.showToast(getApplicationContext(), "Future attendance cannot be recorded");
			}
			else {*/
				RequestQueue queue = VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue();
				final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST;
				StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {

						JSONObject responseObject;
						//System.out.println(response);
						//System.out.println(url);
						try {
							responseObject = new JSONObject(response);
							//System.out.println(res);
							toa();
							if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {

								Utils.showToast(TeacherStudentAttendanceDetails.this, "No Records Found");
								teacherStudentAttendanceListViewDetails.setVisibility(View.INVISIBLE);
							} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {

								Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
							}

							else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {
								teacherStudentAttendanceListViewDetails.setVisibility(View.INVISIBLE);

								Utils.showToast(TeacherStudentAttendanceDetails.this, "Today is Holiday! You can't take the attendance");
							}

							else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("1")) {

									/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherStudentAttendanceDetails.this);

									// Setting Dialog Title
									alertDialog.setTitle("Attendance already submitted");

									// Setting Dialog Message
									alertDialog.setMessage("Want to see it");

									// Setting Icon to Dialog
									//alertDialog.setIcon(R.drawable.calander_icon);

									// Setting Positive "Yes" Button
									alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int which) {*/

								// Write your code here to invoke YES event
								//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
								initData4();
								getTeacherStudentList2();
								s = true;
								submit.setEnabled(false);
								submit.setBackgroundResource(R.drawable.button_red_50_percent);
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
										/*}
									});

									// Setting Negative "NO" Button
									alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											// Write your code here to invoke NO event
											//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
											dialog.cancel();
										}
									});

									// Showing Alert Message
									alertDialog.show();*/
							} else if (responseObject.has("Students"))

							{
								s = false;
								submit.setEnabled(true);
								submit.setBackgroundResource(R.drawable.buttons);
								attendance = new ArrayList<String>();

								teacherStudentAttendanceArray1 = new JSONObject(response).getJSONArray("Students");

								for (i = 0; i < teacherStudentAttendanceArray1.length(); i++) {
									teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent", teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_id") + "-P");
									teacherStudentAttendanceArray1.getJSONObject(i).put("crr_date", date);
									teacherStudentAttendanceArray1.getJSONObject(i).put("u_email_id", Preferences.getInstance().userEmailId);
									teacherStudentAttendanceArray1.getJSONObject(i).put("position","0");
									teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent1","P");
									//teacherStudentAttendanceArray1.getJSONObject(i).put("count", i);
								}

								noOfStudents.setText("Total Students: "+String.valueOf(i));
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);

								//System.out.println(i);

								for (int j = 0; j < teacherStudentAttendanceArray1.length(); j++) {
									teacherStudentAttendanceArray1.getJSONObject(j).put("count", i);
									//teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
								}
								if (null != teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length() >= 0) {
									Entry e = new Entry();
									e.data = teacherStudentAttendanceArray1.toString().getBytes();
									VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST + "?u_email_id=" + userEmailId + "&u_id=" + userId + "&token=" + token + "&id_class=" + classId + "&id_class_section=" + sectionId + "&sch_id=" + schoolId + "&crr_date="/*+"2003-11-21"*/ + date + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId, e);
									teacherStudentAttendanceListViewDetails.invalidateViews();
									teacherAttendanceDetailsListViewAdapter = new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this, teacherStudentAttendanceArray1, 1);
									teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
									teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();
											/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
										Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
										Preferences.getInstance().savePreference(StudentFeedBack.this);*/
								}
							} else
								Utils.showToast(TeacherStudentAttendanceDetails.this, "Error Fetching Response");
							setSupportProgressBarIndeterminateVisibility(false);

						} catch (JSONException e) {
							e.printStackTrace();
							Utils.showToast(TeacherStudentAttendanceDetails.this, "Error fetching modules! Please try after sometime.");
							setSupportProgressBarIndeterminateVisibility(false);
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
						setSupportProgressBarIndeterminateVisibility(false);
					}
				}) {
					@Override
					protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("sch_id", Preferences.getInstance().schoolId);
						params.put("id_class_section", Preferences.getInstance().studentSectionId);
						params.put("token", Preferences.getInstance().token);
						params.put("u_email_id", Preferences.getInstance().userEmailId);
						params.put("id_class", Preferences.getInstance().studentClassId);
						params.put("u_id", Preferences.getInstance().userId);
						params.put("crr_date", date);
						params.put("device_id", Preferences.getInstance().deviceId);
						params.put("ins_id",Preferences.getInstance().institutionId);
						return params;
					}
				};

				requestObject.setRetryPolicy(new DefaultRetryPolicy(
						25000,
						DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				if (Utils.isNetworkAvailable(TeacherStudentAttendanceDetails.this))
					queue.add(requestObject);
				else {
					Utils.showToast(TeacherStudentAttendanceDetails.this, "Unable to fetch data, kindly enable internet settings!");
				}
			}
		//}
	};

	private void showDate(int year, int month, int day) {
		//date1 = day+month+year;
		//System.out.println(date1);
		//System.out.println(year);
		//System.out.println(month);
		//System.out.println(day);

		year1 = Integer.toString(year);
		month1 = Integer.toString(month);
		day1 = Integer.toString(day);

		date = year1+"-"+month1+"-"+day1;

		selectedDate1 = date;

		try {
			selectedDate3 = sdf.parse(selectedDate1);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		selectedDate2 = sdf1.format(selectedDate3);

		selectedDate.setText(selectedDate2);


		//System.out.println(date);

		/*try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="+"2003-11-21"+date1);
			if(e == null)
			{
				teacherStudentAttendanceArray1= null;
			}
			else
			{
				teacherStudentAttendanceArray1= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentAttendanceArray1!= null)
		{
			teacherAttendanceDetailsListViewAdapter= new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this,teacherStudentAttendanceArray1);
			teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
			teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();
		}*/








	}


	private void initData() 
	{

           s= false;
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				teacherStudentAttendanceArray1= null;
			}
			else
			{
				teacherStudentAttendanceArray1= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentAttendanceArray1!= null)
		{
			teacherAttendanceDetailsListViewAdapter= new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this,teacherStudentAttendanceArray1,1);
			teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
			teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();
		}
	}



	/*final View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (calendarPopup == null) {
				calendarPopup = new PopupWindow(TeacherStudentAttendanceDetails.this);
				CalendarPickerView calendarView = new CalendarPickerView(TeacherStudentAttendanceDetails.this);
				calendarView.setListener(dateSelectionListener);
				calendarPopup.setContentView(calendarView);
				calendarPopup.setWindowLayoutMode(
						View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
						ViewGroup.LayoutParams.WRAP_CONTENT);
				calendarPopup.setHeight(1);
				calendarPopup.setWidth(view.getWidth());
				calendarPopup.setOutsideTouchable(true);
			}
			calendarPopup.showAtLocation(, Gravity.CENTER, 0, 0);
		}
	};

	final CalendarNumbersView.DateSelectionListener dateSelectionListener = new CalendarNumbersView.DateSelectionListener() {
		@Override
		public void onDateSelected(Calendar selectedDate) {
			if (calendarPopup.isShowing()) {
				calendarPopup.getContentView().postDelayed(new Runnable() {
					@Override
					public void run() {
						calendarPopup.dismiss();
					}
				}, 500);//For clarity, we close the popup not immediately.
			}
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MMM/yyyy");

			//leaveStarting.setText(formatter1.format(selectedDate.getTime()));
			//toEditTextDate.setText(formatter.format(selectedDate.getTime()));
			//date1 = formatter.format(selectedDate.getTime());
			//toEditTextDate.setText(formatter.format(selectedDate.getTime()));

		}
	};*/


	protected void getTeacherStudentList() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					//System.out.println(res);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(TeacherStudentAttendanceDetails.this,"No Records Found");
						teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{

							Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
						}

						else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {
							teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);

							Utils.showToast(TeacherStudentAttendanceDetails.this, "Today is Holiday! You can't take the attendance");
						}

						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{

								/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherStudentAttendanceDetails.this);

								// Setting Dialog Title
								alertDialog.setTitle("Attendance already submitted");

								// Setting Dialog Message
								alertDialog.setMessage("Want to see it");

								// Setting Icon to Dialog
								//alertDialog.setIcon(R.drawable.calander_icon);

								// Setting Positive "Yes" Button
								alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {*/

										// Write your code here to invoke YES event
										//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
										/*Intent intent = new Intent(TeacherStudentAttendanceDetails.this,TeacherStudentSubmittedAtendanceList.class);
										intent.putExtra("date", date);
										startActivity(intent);
										finish();*/
								
								initData4();
								getTeacherStudentList2();
								s=true;
								submit.setEnabled(false);
								submit.setBackgroundResource(R.drawable.button_red_50_percent);
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
								/*	}
								});

								// Setting Negative "NO" Button
								alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to invoke NO event
										//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
										dialog.cancel();
									}
								});

								// Showing Alert Message
								alertDialog.show();*/
							}
							else
								if(responseObject.has("Students"))

								{
									s=false;
									submit.setEnabled(true);
									attendance = new ArrayList<String>();
									submit.setBackgroundResource(R.drawable.buttons);
                                    
									teacherStudentAttendanceArray1= new JSONObject(response).getJSONArray("Students");
									//teacherStudentAttendanceArray1.getJSONObject(0).put("isPresent", "P");
									//teacherStudentAttendanceArray1.getJSONArray(0).getJSONObject(0).put("isPresent", "P");
									for(i = 0;i<teacherStudentAttendanceArray1.length();i++)
									{
										teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent", teacherStudentAttendanceArray1.getJSONObject(i).getString("stu_id")+"-P");
										teacherStudentAttendanceArray1.getJSONObject(i).put("crr_date", date);
										teacherStudentAttendanceArray1.getJSONObject(i).put("position","0");
										teacherStudentAttendanceArray1.getJSONObject(i).put("u_email_id", Preferences.getInstance().userEmailId);
										teacherStudentAttendanceArray1.getJSONObject(i).put("isPresent1","P");
										//teacherStudentAttendanceArray1.getJSONObject(i).put("count", i);
									}
									
									//System.out.println(i);
									noOfStudents.setText("Total Students: "+String.valueOf(i));
									teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
									selectedDate1 = date;

									try {
										selectedDate3 = sdf.parse(selectedDate1);
									} catch (ParseException e) {
										e.printStackTrace();
									}

									selectedDate2 = sdf1.format(selectedDate3);

									selectedDate.setText(selectedDate2);
									
									for(int j= 0;j<teacherStudentAttendanceArray1.length();j++)
									{
										teacherStudentAttendanceArray1.getJSONObject(j).put("count", i);
										//teacherStudentAttendanceArray1.getJSONObject(j).put("", value)
									}
									if(null!=teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length()>=0)
									{
										Entry e = new Entry();
										e.data = teacherStudentAttendanceArray1.toString().getBytes();
										VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
										teacherStudentAttendanceListViewDetails.invalidateViews();
										teacherAttendanceDetailsListViewAdapter = new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this, teacherStudentAttendanceArray1,1);
										teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
										teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();	
										/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
									}
								}
								else
									Utils.showToast(TeacherStudentAttendanceDetails.this, "Error Fetching Response");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(TeacherStudentAttendanceDetails.this, "Error fetching modules! Please try after sometime.");
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
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("id_class_section",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("id_class",Preferences.getInstance().studentClassId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("crr_date",date);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("ins_id",Preferences.getInstance().institutionId);
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(TeacherStudentAttendanceDetails.this))
				queue.add(requestObject);
			else
			{
				Utils.showToast(TeacherStudentAttendanceDetails.this, "Unable to fetch data, kindly enable internet settings!");
			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
	
	protected void postAttendance()
	{
		//setSupportProgressBarIndeterminateVisibility(true);
		final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
		
		
		
		
			final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMIT/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;
		
		StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				Log.d("ki",response);
				//System.out.println(response);

				//Log.d("ki",response);
				//Utils.showToast(getApplicationContext(), ""+response);
				//System.out.println(url1);
				try 
				{
					responseObject = new JSONObject(response);
					
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						loading.dismiss();
						Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
						
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							loading.dismiss();
							Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{
								loading.dismiss();
								Utils.showToast(TeacherStudentAttendanceDetails.this,"Successfuly Submitted Attendance");
								submit.setEnabled(false);
								submit.setBackgroundResource(R.drawable.button_red_50_percent);
								getTeacherStudentList2();

							}
				}
				catch(JSONException e)
				{
					e.printStackTrace();
					loading.dismiss();
					Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
				}
				//setSupportProgressBarIndeterminateVisibility(false);

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
					Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
					//setSupportProgressBarIndeterminateVisibility(false);
				}
			})
		{
			@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
				//params.put("Students", jsonObject1.toString());
				params.put("students", array2);
				params.put("teach_email",Preferences.getInstance().userEmailId);
				//params.put("count", String.valueOf(i));
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("sec_id", Preferences.getInstance().studentSectionId);
				params.put("stu_name",array4);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("user_id",Preferences.getInstance().userId);
				//params.put("tea_id", Preferences.getInstance().userId);
				params.put("crr_date",date);

				Log.d("students", array2);
				Log.d("teach_email",Preferences.getInstance().userEmailId);
				//params.put("count", String.valueOf(i));
				Log.d("sch_id", Preferences.getInstance().schoolId);
				Log.d("cls_id", Preferences.getInstance().studentClassId);
				Log.d("sec_id", Preferences.getInstance().studentSectionId);
				Log.d("stu_name",array4);
				Log.d("ins_id",Preferences.getInstance().institutionId);
				Log.d("user_id",Preferences.getInstance().userId);
				//params.put("tea_id", Preferences.getInstance().userId);
				Log.d("crr_date",date);

				Log.d("jjj",date);
				//params.put("Students", "harsh");
				//params.put("u_email_id", Preferences.getInstance().userEmailId);
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
				loading.dismiss();
				Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
				//setSupportProgressBarIndeterminateVisibility(false);
			}
			
	
	
	}
	
	private void initData4() 
	{


		try
		{
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMITTED_ATTENDANCE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
			if(e == null)
			{
				teacherStudentAttendanceArray1= null;
			}
			else
			{
				teacherStudentAttendanceArray1= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(teacherStudentAttendanceArray1!= null)
		{
			teacherAttendanceDetailsListViewAdapter= new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this,teacherStudentAttendanceArray1,2);
			teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
			teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();
		}
	}
	
	protected void getTeacherStudentList2() 
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMITTED_ATTENDANCE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);
				try 
				{
					responseObject = new JSONObject(response);
					//System.out.println(res);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{

						Utils.showToast(TeacherStudentAttendanceDetails.this,"No Records Found");
						teacherStudentAttendanceListViewDetails.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{

							Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
						}
					
						else
							if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
							{

								Utils.showToast(TeacherStudentAttendanceDetails.this, "");
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
							}

							else if (responseObject.has("Msg") && responseObject.getString("Msg").equals("2")) {
								teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);

								Utils.showToast(TeacherStudentAttendanceDetails.this, "Today is Holiday! You can't take the attendance");
							}
						else
							if(responseObject.has("Students"))

							{
                                attendance = new ArrayList<String>();
								teacherStudentAttendanceArray1= new JSONObject(response).getJSONArray("Students");
								if(null!=teacherStudentAttendanceArray1 && teacherStudentAttendanceArray1.length()>=0)
								{
									Entry e = new Entry();
									e.data = teacherStudentAttendanceArray1.toString().getBytes();
									VolleySingleton.getInstance(TeacherStudentAttendanceDetails.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_SUBMITTED_ATTENDANCE+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&id_class="+classId+"&id_class_section="+sectionId+"&sch_id="+schoolId+"&crr_date="/*+"2003-11-21"*/+date+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId,e);
									teacherStudentAttendanceListViewDetails.invalidateViews();
									teacherAttendanceDetailsListViewAdapter = new TeacherAttendanceDetailsListViewAdapter(TeacherStudentAttendanceDetails.this, teacherStudentAttendanceArray1,2);
									teacherStudentAttendanceListViewDetails.setAdapter(teacherAttendanceDetailsListViewAdapter);
									teacherAttendanceDetailsListViewAdapter.notifyDataSetChanged();

									int presents=0,absents=0,leaves=0;
									teacherStudentAttendanceListViewDetails.setVisibility(View.VISIBLE);
									for(int g=0;g<teacherStudentAttendanceArray1.length();g++)
									{
										if(teacherStudentAttendanceArray1.getJSONObject(g).getString("attendance").toString().matches("P"))
										{
											presents= presents+1;
										}

										else
											if(teacherStudentAttendanceArray1.getJSONObject(g).getString("attendance").toString().matches("A"))
											{
												absents = absents+1;
											}

											else
											if(teacherStudentAttendanceArray1.getJSONObject(g).getString("attendance").toString().matches("L"))
											{
												leaves = leaves+1;
											}

										selectedDate1 = date;

										try {
											selectedDate3 = sdf.parse(selectedDate1);
										} catch (ParseException ew) {
											ew.printStackTrace();
										}

										selectedDate2 = sdf1.format(selectedDate3);

										selectedDate.setText(selectedDate2);

										noOfStudents.setText("Total:"+String.valueOf(g+1)+" Present:"+String.valueOf(presents)+" Absent:"+String.valueOf(absents)+" Leaves:"+String.valueOf(leaves));


									}

									//noOfStudents.setText();
									/*Preferences.getInstance().loadPreference(StudentFeedBack.this);
									Preferences.getInstance().feedbackId = responseObject.getJSONArray("Feedlist").getJSONObject(0).getString("feedback_id");
									Preferences.getInstance().savePreference(StudentFeedBack.this);*/
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
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("id_class_section",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				params.put("id_class",Preferences.getInstance().studentClassId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("crr_date",date);
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
