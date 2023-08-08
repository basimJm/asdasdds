package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.modals.Request;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;

public class StudentNewLeave extends AppCompatActivity implements View.OnClickListener {


	String UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_NEW_LEAVE;
	/*String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;*/
	String studentId;

	private Button leaveStarting, leaveEnding, leaveUpload, leaveSubmit;
	private TextView studentLeaveTitle, studentLeaveSubject, studentLeaveDetails, studentLeaveWordLimit, studentLeaveStartDetails, studentLeaveEndDetails;
	private EditText editLeaveSubject, editLeaveDetails;
	private ImageView imageBack, imageLeave;
	private PopupWindow calendarPopup, calendarPopup1;
	String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	String fromDate, todate;

	String leaveSubject, leaveDetails;
	int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	int count1 = 0;
	String image;
	int count = 0;
	int count2 = 0;
	private Bitmap bitmap;
	private static final int CAMERA_REQUEST = 1888;
	//String image = getStringImage(bitmap);
	private int PICK_IMAGE_REQUEST = 1;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String date1, date2;
	String subject, details;
	private int STORAGE_PERMISSION_CODE = 23;
	Switch hostelHoilday,notifyWardon;
	int f;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.student_leave_request);
		final Tracker[] t = {((SchoofiApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER)};

		// Set screen name.
		t[0].setScreenName("Student NewLeave");

		// Send a screen view.
		t[0].send(new HitBuilders.ScreenViewBuilder().build());
		studentLeaveTitle = (TextView) findViewById(R.id.txt_newLeave);
		imageBack = (ImageView) findViewById(R.id.img_back);
		studentLeaveSubject = (TextView) findViewById(R.id.text_student_leave_subject);
		editLeaveSubject = (EditText) findViewById(R.id.edit_leave_subject);
		editLeaveDetails = (EditText) findViewById(R.id.edit_student_leave_details);
		studentLeaveDetails = (TextView) findViewById(R.id.text_student_leave_details);
		studentLeaveWordLimit = (TextView) findViewById(R.id.text_student_leave_words_limit);
		studentLeaveStartDetails = (TextView) findViewById(R.id.text_student_leave_start_details);
		studentLeaveEndDetails = (TextView) findViewById(R.id.text_student_leave_end_details);
		leaveStarting = (Button) findViewById(R.id.btn_student_leave_starting_date);
		leaveEnding = (Button) findViewById(R.id.btn_student_leave_ending_date);
		leaveUpload = (Button) findViewById(R.id.btn_student_leave_upload_file);
		leaveSubmit = (Button) findViewById(R.id.btn_student_leave_submit);
		hostelHoilday = (Switch) findViewById(R.id.mySwitch1);
		notifyWardon = (Switch) findViewById(R.id.mySwitch2);

		Preferences.getInstance().loadPreference(getApplicationContext());

		if(Preferences.getInstance().isHostelStudent.matches("") || Preferences.getInstance().isHostelStudent.matches("null") || Preferences.getInstance().isHostelStudent.matches("N"))
		{
			hostelHoilday.setVisibility(View.GONE);
			notifyWardon.setVisibility(View.GONE);
		}
		else
		{
			hostelHoilday.setVisibility(View.VISIBLE);
			notifyWardon.setVisibility(View.VISIBLE);
		}



		imageLeave = (ImageView) findViewById(R.id.imageLeave);
		//System.out.println(Preferences.getInstance().studentId);
		leaveStarting.setOnClickListener(onEditTextClickListener);
		leaveEnding.setOnClickListener(onEditTextClickListener1);
		//leaveSubject = studentLeaveSubject.getText().toString();
		//leaveDetails = studentLeaveDetails.getText().toString();
		/*System.out.println(token);*/
		leaveUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
				if (checkAndRequestPermissions()) {
					selectImage();
					f = 1;
				}

			}
		});
		//leaveSubmit.setOnClickListener(this);
		//System.out.println(studentId);
		//System.out.println(token);
		imageBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		leaveSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (editLeaveSubject.getText().toString().matches("") || editLeaveDetails.getText().toString().matches("") || leaveStarting.getText().toString().matches("") || leaveEnding.getText().toString().matches("")) {
					Utils.showToast(StudentNewLeave.this, "plz fill the required fields");
				} else if (f == 1) {
					subject = editLeaveSubject.getText().toString();
					details = editLeaveDetails.getText().toString();
					postLeave();

				} else {
					postLeave1();
				}

				//postLeave4();

			}
		});


	}

	private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (calendarPopup == null) {
				calendarPopup = new PopupWindow(StudentNewLeave.this);
				CalendarPickerView calendarView = new CalendarPickerView(StudentNewLeave.this);
				calendarView.setListener(dateSelectionListener);
				calendarPopup.setContentView(calendarView);
				calendarPopup.setWindowLayoutMode(
						View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
						ViewGroup.LayoutParams.WRAP_CONTENT);
				calendarPopup.setHeight(1);
				calendarPopup.setWidth(view.getWidth());
				calendarPopup.setOutsideTouchable(true);
			}
			calendarPopup.showAtLocation(studentLeaveDetails, Gravity.CENTER, 0, 0);
		}
	};

	private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (calendarPopup1 == null) {
				calendarPopup1 = new PopupWindow(StudentNewLeave.this);
				CalendarPickerView calendarView = new CalendarPickerView(StudentNewLeave.this);
				calendarView.setListener(dateSelectionListener1);
				calendarPopup1.setContentView(calendarView);
				calendarPopup1.setWindowLayoutMode(
						View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
						ViewGroup.LayoutParams.WRAP_CONTENT);
				calendarPopup1.setHeight(1);
				calendarPopup1.setWidth(view.getWidth());
				calendarPopup1.setOutsideTouchable(true);
			}
			calendarPopup1.showAtLocation(studentLeaveDetails, Gravity.CENTER, 0, 0);
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

			leaveStarting.setText(formatter1.format(selectedDate.getTime()));
			//toEditTextDate.setText(formatter.format(selectedDate.getTime()));
			date1 = formatter.format(selectedDate.getTime());
			//toEditTextDate.setText(formatter.format(selectedDate.getTime()));

		}
	};

	private CalendarNumbersView.DateSelectionListener dateSelectionListener1 = new CalendarNumbersView.DateSelectionListener() {
		@Override
		public void onDateSelected(Calendar selectedDate) {
			if (calendarPopup1.isShowing()) {
				calendarPopup1.getContentView().postDelayed(new Runnable() {
					@Override
					public void run() {
						calendarPopup1.dismiss();
					}
				}, 500);//For clarity, we close the popup not immediately.
			}
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MMM/yyyy");

			leaveEnding.setText(formatter1.format(selectedDate.getTime()));
			//toEditTextDate.setText(formatter.format(selectedDate.getTime()));
			date2 = formatter.format(selectedDate.getTime());

		}
	};

	private void showFileChooser() {
		//Preferences.getInstance().savePreference(StudentNewLeave.this);
		Intent intent = new Intent();
		intent.setType("image*//*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

	}

	//gggg= http://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/;

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Preferences.getInstance().loadPreference(StudentNewLeave.this);
		studentId = Preferences.getInstance().studentId;
		System.out.println(studentId);

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri filePath = data.getData();
			//Utils.showToast(getApplicationContext(), filePath.toString());
			Log.d("harsh", "kk" + filePath.toString());
			Preferences.getInstance().loadPreference(StudentNewLeave.this);
			studentId = Preferences.getInstance().studentId;
			try {
				//Getting the Bitmap from Gallery
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
				//Setting the Bitmap to ImageView
				imageLeave.setImageBitmap(bitmap);

				image = getStringImage(bitmap);
				//image = getStringImage(bitmap);
				//System.out.println(image);
				//Utils.showToast(getApplicationContext(), image);
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
	}*/

	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			image = getStringImage(photo);
			Utils.showToast(getApplicationContext(),image);
			//imageView.setImageBitmap(photo);
		}
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {

				Preferences.getInstance().loadPreference(StudentNewLeave.this);
				studentId = Preferences.getInstance().studentId;

				try {
					Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

					image = getStringImage(thumbnail);
					imageLeave.setImageBitmap(thumbnail);
				}
				catch (OutOfMemoryError e)
				{
					Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
				}
				//Utils.showToast(getApplicationContext(),image);
				/*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
				File destination = new File(Environment.getExternalStorageDirectory(),
						System.currentTimeMillis() + ".jpg");
				FileOutputStream fo;
				try {
					destination.createNewFile();
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				//ivImage.setImageBitmap(thumbnail);
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				//String[] projection = {MediaStore.MediaColumns.DATA};
				Log.d("harsh", "kk" + selectedImageUri.toString());
				Preferences.getInstance().loadPreference(StudentNewLeave.this);
				studentId = Preferences.getInstance().studentId;
				try {
					//Getting the Bitmap from Gallery
					bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
					//Setting the Bitmap to ImageView
					imageLeave.setImageBitmap(bitmap);
					image = getStringImage(bitmap);


					//Utils.showToast(getApplicationContext(),image);
					//image = getStringImage(bitmap);
					//System.out.println(image);
					//Utils.showToast(getApplicationContext(), image);
				}
				catch (OutOfMemoryError e)
				{
					Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				/*CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
						null);
				Cursor cursor = cursorLoader.loadInBackground();
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				cursor.moveToFirst();
				String selectedImagePath = cursor.getString(column_index);
				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);*/
				//ivImage.setImageBitmap(bm);
			}
		}
	}


	private void postLeave()
    {
		//Utils.showToast(getApplicationContext(), ""+date2+date1);
    	final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
    	//Preferences.getInstance().loadPreference(StudentNewLeave.this);
    	//System.out.println(Preferences.getInstance().studentId);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //System.out.println(s);
                        //Showing toast message of the response
                        Toast.makeText(StudentNewLeave.this, s , Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        System.out.println(volleyError);

                        //Showing toast
                        Utils.showToast(getApplicationContext(), "error2");
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                //Converting Bitmap to String

              // Preferences.getInstance().loadPreference(StudentNewLeave.this);
               //studentId = Preferences.getInstance().studentId;


 //http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
                //Getting Image Name
               // String name = editTextName.getText().toString().trim();

                //Creating parameters

                Map<String,String> params = new Hashtable<String, String>();
				//image = getStringImage(bitmap);

                //Adding parameters
				params.put("file", image);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("email", Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("crr_Date", date);
				params.put("from_Date", date1);
				params.put("to_Date", date2);
				params.put("leave_Subject", subject);
				params.put("leave_Details", details);
				params.put("token", Preferences.getInstance().token);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("name",Preferences.getInstance().Name);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);
				if(Preferences.getInstance().isHostelStudent.matches("") || Preferences.getInstance().isHostelStudent.matches("null") || Preferences.getInstance().isHostelStudent.matches("N"))
				{
					params.put("is_hostel_leave","N");
					params.put("notify_wardon","N");
				}
				else
				{
					if(hostelHoilday.isChecked())
					{
						params.put("is_hostel_leave","Y");
					}
					else
					{
						params.put("is_hostel_leave","N");
					}

					if(notifyWardon.isChecked())
					{
						params.put("notify_wardon","Y");
					}
					else
					{
						params.put("notify_wardon","N");
					}
				}
                //params.put("token",Preferences.getInstance().token);
               // params.put("device_id", Preferences.getInstance().deviceId);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

	/*private void postLeave7() {
		class UploadImage extends AsyncTask<Bitmap, Void, String> {

			ProgressDialog loading;
			Request request = new Request();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(StudentNewLeave.this, "Uploading Image", "Please wait...", true, true);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

				finish();
			}

			@Override
			protected String doInBackground(Bitmap... params) {
				Bitmap bitmap = params[0];
				//String uploadImage = getStringImage(bitmap);
				//image = getStringImage(bitmap);


				// Utils.showToast(getApplicationContext(), image);
				HashMap<String, String> data = new HashMap<>();
				data.put("file", image);
				data.put("stu_id", Preferences.getInstance().studentId);
				data.put("email", Preferences.getInstance().userEmailId);
				data.put("u_id", Preferences.getInstance().userId);
				data.put("crr_Date", date);
				data.put("from_Date", date1);
				data.put("to_Date", date2);
				data.put("leave_Subject", subject);
				data.put("leave_Details", details);
				data.put("token", Preferences.getInstance().token);
				data.put("device_id", Preferences.getInstance().deviceId);

				String result = request.sendPostRequest(UPLOAD_URL, data);

				return result;
			}
		}

		UploadImage ui = new UploadImage();
		ui.execute(bitmap);
	}*/

	private void postLeave1() {
		//Utils.showToast(getApplicationContext(), ""+date2+date1);
		final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
		//Preferences.getInstance().loadPreference(StudentNewLeave.this);
		//System.out.println(Preferences.getInstance().studentId);
		StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, UPLOAD_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						//Disimissing the progress dialog
						loading.dismiss();
						//System.out.println(s);
						//Showing toast message of the response
						Toast.makeText(StudentNewLeave.this, s, Toast.LENGTH_SHORT).show();
						finish();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						//Dismissing the progress dialog
						loading.dismiss();
						System.out.println(volleyError);

						//Showing toast
						Utils.showToast(getApplicationContext(), "error2");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				//Converting Bitmap to String

				// Preferences.getInstance().loadPreference(StudentNewLeave.this);
				//studentId = Preferences.getInstance().studentId;


				//http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
				//Getting Image Name
				// String name = editTextName.getText().toString().trim();

				//Creating parameters
				Map<String, String> params = new Hashtable<String, String>();
				//image = getStringImage(bitmap);

				//Adding parameters
				//params.put("file", image);
				params.put("stu_id", Preferences.getInstance().studentId);
				params.put("email", Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("crr_Date", date);
				params.put("from_Date", date1);
				params.put("to_Date", date2);
				params.put("leave_Subject", editLeaveSubject.getText().toString());
				params.put("leave_Details", editLeaveDetails.getText().toString());
				params.put("token", Preferences.getInstance().token);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("name",Preferences.getInstance().Name);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);
				params.put("sec_id",Preferences.getInstance().studentSectionId);

				if(Preferences.getInstance().isHostelStudent.matches("") || Preferences.getInstance().isHostelStudent.matches("null") || Preferences.getInstance().isHostelStudent.matches("N"))
				{
					params.put("is_hostel_leave","N");
					params.put("notify_wardon","N");
				}
				else
				{
					if(hostelHoilday.isChecked())
					{
						params.put("is_hostel_leave","Y");
					}
					else
					{
						params.put("is_hostel_leave","N");
					}

					if(notifyWardon.isChecked())
					{
						params.put("notify_wardon","Y");
					}
					else
					{
						params.put("notify_wardon","N");
					}
				}

				//returning parameters
				return params;
			}
		};

		//Creating a Request Queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);

		//Adding request to the queue
		requestQueue.add(stringRequest);
	}

	private void postLeave4() {
		//Utils.showToast(getApplicationContext(), ""+date2+date1);
		final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
		//Preferences.getInstance().loadPreference(StudentNewLeave.this);
		//System.out.println(Preferences.getInstance().studentId);
		StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,"http://159.203.89.4/schoofi_test/2.2.1/fortest.php",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						//Disimissing the progress dialog
						loading.dismiss();
						//System.out.println(s);
						//Showing toast message of the response
						Toast.makeText(StudentNewLeave.this, s, Toast.LENGTH_SHORT).show();
						finish();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						//Dismissing the progress dialog
						loading.dismiss();
						System.out.println(volleyError);

						//Showing toast
						Utils.showToast(getApplicationContext(), "error submitting leave");
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				//Converting Bitmap to String

				// Preferences.getInstance().loadPreference(StudentNewLeave.this);
				//studentId = Preferences.getInstance().studentId;


				//http://www.androidbegin.com/tutorial/android-populating-spinner-json-tutorial/
				//Getting Image Name
				// String name = editTextName.getText().toString().trim();

				//Creating parameters
				Map<String, String> params = new Hashtable<String, String>();
				//image = getStringImage(bitmap);

				//Adding parameters
				//params.put("file", image);
				params.put("title", "a");
				/*params.put("email", Preferences.getInstance().userEmailId);
				params.put("u_id", Preferences.getInstance().userId);
				params.put("crr_Date", date);
				params.put("from_Date", date1);
				params.put("to_Date", date2);
				params.put("leave_Subject", editLeaveSubject.getText().toString());
				params.put("leave_Details", editLeaveDetails.getText().toString());
				params.put("token", Preferences.getInstance().token);
				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("name",Preferences.getInstance().Name);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("sch_id",Preferences.getInstance().schoolId);*/

				//returning parameters
				return params;
			}
		};

		//Creating a Request Queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);

		//Adding request to the queue
		requestQueue.add(stringRequest);
	}

	@Override
	public void onClick(View v) {


	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(StudentNewLeave.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private  boolean checkAndRequestPermissions() {
		int permissionSendMessage = ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.READ_EXTERNAL_STORAGE);
		int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
		List<String> listPermissionsNeeded = new ArrayList<>();
		if (locationPermission != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
		}
		if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
			return false;
		}
		return true;
	}

}



		/*if(v == leaveUpload){
			showFileChooser();
			count2 = 1;
			f=1;
		}*//*

		*//*if(v == leaveSubmit){
			postLeave();
		}
	}


}*/
