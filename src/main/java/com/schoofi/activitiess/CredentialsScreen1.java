package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.SchoolIdVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;

public class CredentialsScreen1 extends AppCompatActivity {

	//{"sch_list":[{"school_id":"I001sc02","school_name":"Schoofi Vasant Kunj"}]}
	private TextView screenTitle, studentName, studentAdmissionNo, studentSchoolname, studentDob, studentSection, studentClass, studentDob1;
	private Button save;

	private EditText studentName1, studentAdmissionNo1, studentSection1, studentClass1;
	private Spinner schoolName;
	JSONObject jsonobject;
	JSONArray jsonarray;
	private JSONArray studentDetailsArray;
	ArrayList<String> types;
	ArrayList<SchoolIdVO> id;
	String schoolId;
	String email, mobile;
	private PopupWindow calendarPopup;
	int year_x, month_x, day_x;
	static final int dialog_id = 1;
	//String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	//String date1;
	ImageView back;
	private DatePicker datePicker;
	private Calendar calendar;
	private int year, month, day;
	String date, date1;
	String Month;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_credentials_screen1);

		Tracker t = ((SchoofiApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Credentials Screen1");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		screenTitle = (TextView) findViewById(R.id.txt_Credentials2);
		studentName = (TextView) findViewById(R.id.text_StudentName);
		studentAdmissionNo = (TextView) findViewById(R.id.text_AdmissionNo);
		studentSchoolname = (TextView) findViewById(R.id.text_school_Name);
		studentSection = (TextView) findViewById(R.id.text_studentSection);
		studentClass = (TextView) findViewById(R.id.text_studentClass);
		studentDob1 = (TextView) findViewById(R.id.text_studentDob1);
		studentName1 = (EditText) findViewById(R.id.edit_studentName);
		studentClass1 = (EditText) findViewById(R.id.edit_studentClass1);
		studentSection1 = (EditText) findViewById(R.id.text_studentSection1);
		studentAdmissionNo1 = (EditText) findViewById(R.id.text_studentAdmissionNo);
		schoolName = (Spinner) findViewById(R.id.spinn_type1);
		studentDob = (TextView) findViewById(R.id.text_studentDob);
		save = (Button) findViewById(R.id.btn_doneCredentilas);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		email = getIntent().getStringExtra("email");
		mobile = getIntent().getStringExtra("mobile");

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


		if (email.matches("")) {
			new DownloadJSON2().execute();
		} else if (mobile.matches("")) {
			new DownloadJSON1().execute();
		} else {
			new DownloadJSON().execute();
		}

		//studentDob1.setOnClickListener(onEditTextClickListener);

		studentDob1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(999);
				showDate(year, month, day);

			}
		});

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (studentAdmissionNo1.getText().toString().matches("") || studentClass1.getText().toString().matches("") || studentDob1.getText().toString().matches("") || studentName1.getText().toString().matches("") || studentSection1.getText().toString().matches("")) {
					Utils.showToast(CredentialsScreen1.this, "No fields can be left blank");
				} else {
					postMessage();
				}

			}
		});


	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {


			// Create an array to populate the spinner 
			id = new ArrayList<SchoolIdVO>();
			types = new ArrayList<String>();
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_SCHOOL_LIST + "?u_email_id=" + email + "&u_phone=" + mobile);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("sch_list");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					//FeedbackVO feedbackVO = new FeedbackVO();
					SchoolIdVO schoolIdVO = new SchoolIdVO();
					schoolIdVO.setSchoolid(jsonobject.optString("school_id"));
					id.add(schoolIdVO);
					types.add(jsonobject.optString("school_name"));

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

			schoolName
					.setAdapter(new ArrayAdapter<String>(CredentialsScreen1.this,
							android.R.layout.simple_spinner_dropdown_item,
							types));

			schoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
					// TODO Auto-generated method stub

					schoolId = id.get(position).getSchoolid().toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}


			});


		}
	}


	private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {


			// Create an array to populate the spinner 
			id = new ArrayList<SchoolIdVO>();
			types = new ArrayList<String>();
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_SCHOOL_LIST + "?u_email_id=" + email);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("sch_list");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					//FeedbackVO feedbackVO = new FeedbackVO();
					SchoolIdVO schoolIdVO = new SchoolIdVO();
					schoolIdVO.setSchoolid(jsonobject.optString("school_id"));
					id.add(schoolIdVO);
					types.add(jsonobject.optString("school_name"));

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

			schoolName
					.setAdapter(new ArrayAdapter<String>(CredentialsScreen1.this,
							android.R.layout.simple_spinner_dropdown_item,
							types));

			schoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
					// TODO Auto-generated method stub

					schoolId = id.get(position).getSchoolid().toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}


			});


		}
	}

	private class DownloadJSON2 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {


			// Create an array to populate the spinner 
			id = new ArrayList<SchoolIdVO>();
			types = new ArrayList<String>();
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_SCHOOL_LIST + "?u_phone=" + mobile);
			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("sch_list");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					//FeedbackVO feedbackVO = new FeedbackVO();
					SchoolIdVO schoolIdVO = new SchoolIdVO();
					schoolIdVO.setSchoolid(jsonobject.optString("school_id"));
					id.add(schoolIdVO);
					types.add(jsonobject.optString("school_name"));

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

			schoolName
					.setAdapter(new ArrayAdapter<String>(CredentialsScreen1.this,
							android.R.layout.simple_spinner_dropdown_item,
							types));

			schoolName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
					// TODO Auto-generated method stub

					schoolId = id.get(position).getSchoolid().toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}


			});


		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.credentials_screen1, menu);
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

	private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (calendarPopup == null) {
				calendarPopup = new PopupWindow(CredentialsScreen1.this);
				CalendarPickerView calendarView = new CalendarPickerView(CredentialsScreen1.this);
				calendarView.setListener(dateSelectionListener);
				calendarPopup.setContentView(calendarView);
				calendarPopup.setWindowLayoutMode(
						View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
						ViewGroup.LayoutParams.WRAP_CONTENT);
				calendarPopup.setHeight(1);
				calendarPopup.setWidth(view.getWidth());
				calendarPopup.setOutsideTouchable(true);
			}
			calendarPopup.showAsDropDown(view);
		}
	};

	private CalendarNumbersView.DateSelectionListener dateSelectionListener = new CalendarNumbersView.DateSelectionListener() {
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

			studentDob1.setText(formatter1.format(selectedDate.getTime()));
			//toEditTextDate.setText(formatter.format(selectedDate.getTime()));
			date1 = formatter.format(selectedDate.getTime());

		}
	};

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
		}
	};

	private void showDate(int year, int month, int day) {


		switch (month) {
			case 1:
				Month = "Jan";
				break;
			case 2:
				Month = "Feb";
				break;
			case 3:
				Month = "Mar";
				break;
			case 4:
				Month = "Apr";
				break;
			case 5:
				Month = "May";
				break;
			case 6:
				Month = "Jun";
				break;
			case 7:
				Month = "Jul";
				break;
			case 8:
				Month = "Aug";
				break;
			case 9:
				Month = "Sep";
				break;
			case 10:
				Month = "Oct";
				break;
			case 11:
				Month = "Nov";
				break;
			case 12:
				Month = "Dec";
				break;
			default:
				System.out.println("llll");

				break;
		}

		date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);

		date1 = String.valueOf(day) + "-" + Month + "-" + String.valueOf(year);
		studentDob1.setText(date1);
		studentDob1.setTextColor(Color.BLACK);
	}



	private void postMessage() {
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_REGISTRATION_SCREEN_3/*+"?u_email="+email+"&s_name="+studentName1.getText().toString()+"&adm_no="+studentAdmissionNo1.getText().toString()+"&sch_id="+schoolId+"&&sec="+studentSection1.getText().toString()+"&cls="+studentClass1.getText().toString()+"&stu_dob="+date1*/;
		//System.out.println(url1);

		StringRequest requestObject = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				try {
					responseObject = new JSONObject(response);
					//System.out.println(responseObject.toString());
					if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {

						Utils.showToast(CredentialsScreen1.this, "Error Submitting Comment");

					} else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
						Utils.showToast(CredentialsScreen1.this, "Session Expired:Please Login Again");
					} else if (responseObject.has("Stu_details")) {
						studentDetailsArray = new JSONObject(response).getJSONArray("Stu_details");
						if (null != studentDetailsArray && studentDetailsArray.length() >= 0) {
							Entry e = new Entry();
							e.data = studentDetailsArray.toString().getBytes();
							VolleySingleton.getInstance(CredentialsScreen1.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PARENT_REGISTRATION_SCREEN_3/*+"?u_email="+email+"&s_name="+studentName1.getText().toString()+"&adm_no="+studentAdmissionNo1.getText().toString()+"&sch_id="+schoolId+"&&sec="+studentSection1.getText().toString()+"&cls="+studentClass1.getText().toString()+"&stu_dob="+date1*/, e);
							Intent intent = new Intent(CredentialsScreen1.this, AddChild.class);
							intent.putExtra("studentName", studentDetailsArray.getJSONObject(0).getString("stu_name"));
							intent.putExtra("schoolName", studentDetailsArray.getJSONObject(0).getString("school_name"));
							intent.putExtra("studentClass", studentDetailsArray.getJSONObject(0).getString("class_name") + "-" + studentDetailsArray.getJSONObject(0).getString("section_name"));
							intent.putExtra("fathersName", studentDetailsArray.getJSONObject(0).getString("father_name"));
							intent.putExtra("mothersName", studentDetailsArray.getJSONObject(0).getString("mother_name"));
							intent.putExtra("imageUrl", studentDetailsArray.getJSONObject(0).getString("picture"));
							intent.putExtra("email", email);
							intent.putExtra("studentId", studentDetailsArray.getJSONObject(0).getString("stu_id"));
							startActivity(intent);
							Utils.showToast(CredentialsScreen1.this, "Successfuly Submitted Comment");
						}

						//Intent intent = new Intent(CredentialsScreen1.this,AddChild.class);
						//startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Utils.showToast(CredentialsScreen1.this, "Error submitting alert! Please try after sometime.");
				}
				setSupportProgressBarIndeterminateVisibility(false);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Utils.showToast(CredentialsScreen1.this, "Error submitting alert! Please try after sometime.");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("u_email", email);
				params.put("s_name", studentName1.getText().toString());
				params.put("adm_no", studentAdmissionNo1.getText().toString());
				params.put("stu_dob", date1);
				params.put("sch_id", schoolId);
				params.put("sec", studentSection1.getText().toString());
				params.put("cls", studentClass1.getText().toString());
				return params;
			}
		};

		requestObject.setRetryPolicy(new DefaultRetryPolicy(
				25000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		if (Utils.isNetworkAvailable(this))
			queue.add(requestObject);
		else {
			Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
			setSupportProgressBarIndeterminateVisibility(false);
		}

	}


}
