package com.schoofi.activitiess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.Manifest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.FilePath;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherAssignmentUploadVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import polypicker.ImagePickerActivity;
import polypicker.utils.ImageInternalFetcher;

public class NewAssignmentTeacher extends FragmentsActivity{

	Spinner type,teacherClass;
private static final String TAG = FragmentsActivity.class.getSimpleName();
	
	private static final int INTENT_REQUEST_GET_IMAGES = 13;
	private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

	private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    String selectedPath1 = "NONE";
    String selectedPath2 = "NONE";
    int count;
	private DatePicker datePicker;
	private Calendar calendar;
	private int year, month, day;
	String Month;
	String date2,date1;
    int i;
    
    private Bitmap bitmap;
    String imageLeave,imageLeave1;
	
	private Context mContext;
	
	private ViewGroup mSelectedImagesContainer;
	private ViewGroup mSelectedImagesNone;
	HashSet<Uri> mMedia = new HashSet<Uri>();
    private TextView submit;
    private String selectedpath;
    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
	String date5 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private EditText messageReply,description;
    ArrayList<String> images = new ArrayList<String>();
	JSONObject jsonobject;
	String moduleType,moduleType1;
	ImageView back;
	private static final int PICK_FILE_REQUEST = 1;
	private static final int READ_REQUEST_CODE = 42;
	private String selectedFilePath;
	private String SERVER_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.TEACHER_SUBMIT_ASSIGNMENT;
	ImageView ivAttachment;
	Button bUpload;
	TextView tvFileName;
	ProgressDialog dialog;
	private int STORAGE_PERMISSION_CODE = 23;
	
	JSONArray jsonarray;
	ArrayList<String> UploadClassNames;
	ArrayList<TeacherAssignmentUploadVO> teacherAssignmentUploadVOs;
	private String teacherAssignmentUpload1="null";
	String type1;
	TextView className;
	EditText title,optionalSubject;
	TextView lastDate;
	int count1=0;
	Button uploadDocument;
	private int choice=0,choice1=0,googleDrive=0;
	Uri uri;

	
	public static final CharSequence[] DAYS_OPTIONS  = {"Select Type","HomeWork","Assignment","Circular"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ActionBar actionBar = getSupportActionBar();
		//actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("New Assignment Teacher");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_new_assignment_teacher);
		mContext = NewAssignmentTeacher.this;

		TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(getResources().getDimension(R.dimen.abc_text_size_body_1_material));
		//paint.setStrikeThruText(true);
		paint.setColor(Color.RED);
		//paint.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf"));

		TextPaint title1 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		title1.setTextSize(60);
		title1.setUnderlineText(true);
		title1.setColor(Color.RED);
		//title.setTypeface(Typeface.createFromAsset(getAssets(), "RobotoSlab-Regular.ttf"));

		ShowcaseView showcaseView = new ShowcaseView.Builder(this)
				.withNewStyleShowcase()
				.setTarget(new ViewTarget(R.id.btn_submit_assignment, this))
				.setContentTextPaint(paint)
				.setContentTitle("For Submission Press Here")
				.setContentText("")
				.setContentTitlePaint(title1)
				.build();

		showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_CENTER);
		showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_CENTER);
		showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
		type = (Spinner) findViewById(R.id.spinner_type);
		teacherClass = (Spinner) findViewById(R.id.spinner_subject);
		submit = (TextView) findViewById(R.id.btn_submit_assignment);
		className = (TextView) findViewById(R.id.txt_class1);
		title = (EditText) findViewById(R.id.edit_title_assignment);
		className.setText(Preferences.getInstance().sectionName);
		back = (ImageView) findViewById(R.id.img_back);
		lastDate = (TextView) findViewById(R.id.edit_title_last_date);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		description = (EditText) findViewById(R.id.edit_description);
		uploadDocument = (Button) findViewById(R.id.get_doc);
		optionalSubject = (EditText) findViewById(R.id.edit_optional);
		//uploadDocument.setVisibility(View.GONE);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if(Utils.isNetworkAvailable(getApplicationContext()))
		{
		new DownloadJSON().execute();
		
		
		}
		
		else
		{
			Utils.showToast(getApplicationContext(), "Kindly enable internet services");
		}
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, R.layout.spinner_layout, DAYS_OPTIONS);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
	    type.setAdapter(adapter); // Apply the adapter to the spinner
	    
	    type.setOnItemSelectedListener(new OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	           // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
	        	moduleType = ""+parent.getItemAtPosition(position);
				//Utils.showToast(getApplicationContext(),moduleType);
				if(moduleType.matches("Select Type"))
				{
					Log.d("jjj","kkkk");
				}

				else
				if(moduleType.matches("Circular"))
				{
					lastDate.setVisibility(View.GONE);
					teacherClass.setVisibility(View.GONE);
					optionalSubject.setVisibility(View.GONE);
					((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
					choice1=1;
				}

				else
				{
					((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
					lastDate.setVisibility(View.VISIBLE);
					teacherClass.setVisibility(View.VISIBLE);
					optionalSubject.setVisibility(View.VISIBLE);
				}
	        	
	        	
	        }

	        public void onNothingSelected(AdapterView<?> parent) {
	            //showToast("Spinner2: unselected");
	        }
	    });
	    
	    final View getImages = findViewById(R.id.get_images);
		//View getNImages = findViewById(R.id.get_n_images);
		mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

		uploadDocument.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				choice=1;

				getImages.setVisibility(View.GONE);

				//if(Build.VERSION.SDK_INT >22) {

					if (checkAndRequestPermissions()) {
						// carry on the normal flow, as the case of  permissions  granted.

						//getImages();
						selectImage();
					}
				//}

				/*else
				{
					//getImages();
					showFileChooser();
				}*/

			}
		});
		

		getImages.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Preferences.getInstance().savePreference(getApplicationContext());
				/*if(isReadStorageAllowed()){
					//If permission is already having then showing the toast
					requestStoragePermission();
					//Toast.makeText(NewAssignmentTeacher.this,"You already have the permission",Toast.LENGTH_LONG).show();
					//Existing the method with return
					return;
				}

				//If the app has not the permission then asking for the permission
				requestStoragePermission();*/

				choice = 0;
				uploadDocument.setVisibility(View.GONE);

				//if(Build.VERSION.SDK_INT >22) {

					if (checkAndRequestPermissions()) {
						// carry on the normal flow, as the case of  permissions  granted.

						getImages();
					}
				//}

				/*else
				{
					getImages();
				}*/

			}
		});
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (moduleType.matches("Select Type")) {
					Utils.showToast(getApplicationContext(), "Plz select type");
				}

				else
					if (moduleType.matches("Circular")) {

						if(title.getText().toString().matches("") || title.getText().toString().matches("null") || description.getText().toString().matches("") || description.getText().toString().matches("null"))
						{
							Utils.showToast(getApplicationContext(), "Plz fill all fields");
						}

						else {


							if (choice == 0) {

								postAttendance();

							} else {
								if (googleDrive == 0) {



									if (selectedFilePath!= null) {

										dialog = ProgressDialog.show(NewAssignmentTeacher.this, "", "Uploading File...", true);
										Log.d("uytr","kjhg");

										new Thread(new Runnable() {
											@Override
											public void run() {
												//creating new thread to handle Http Operations
												uploadFile(selectedFilePath);

											}
										}).start();
									} else {
										Toast.makeText(NewAssignmentTeacher.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
									}
								} else {


									try {

										dialog = ProgressDialog.show(NewAssignmentTeacher.this, "", "Uploading File...", true);
										final ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
										final FileDescriptor fd = mInputPFD.getFileDescriptor();

										final Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
										final int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
										int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
										returnCursor.moveToFirst();
										Log.d("hhhh", returnCursor.getString(nameIndex));


										new Thread(new Runnable() {
											@Override
											public void run() {
												//creating new thread to handle Http Operations
												int serverResponseCode = 0;

												HttpURLConnection connection;
												DataOutputStream dataOutputStream;
												String lineEnd = "\r\n";
												String twoHyphens = "--";
												String boundary = "*****";


												int bytesRead, bytesAvailable, bufferSize;
												byte[] buffer;
												int maxBufferSize = 1 * 1024 * 1024;

												try {


													FileInputStream fileInputStream = new FileInputStream(mInputPFD.getFileDescriptor());
													URL url = new URL(SERVER_URL);
													connection = (HttpURLConnection) url.openConnection();
													connection.setDoInput(true);//Allow Inputs
													connection.setDoOutput(true);//Allow Outputs
													connection.setUseCaches(false);//Don't use a cached Copy
													connection.setRequestMethod("POST");
													connection.setRequestProperty("Connection", "Keep-Alive");
													connection.setRequestProperty("ENCTYPE", "multipart/form-data");
													connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
													connection.setRequestProperty("file1",String.valueOf(uri));
													//connection.setRequestProperty("title","llll");

													//creating new dataoutputstream
													dataOutputStream = new DataOutputStream(connection.getOutputStream());
													//addFormField(dataOutputStream,"abc","a");

													//writing bytes to data outputstream
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"tech_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().teachId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"inst_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"cls_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sec_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(title.getText().toString());
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"crr_Date\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(date5);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().token);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"u_id\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(Preferences.getInstance().userId);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"desc\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(description.getText().toString());
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes("2");
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													if(moduleType.matches("HomeWork"))
													{
														moduleType1 = "home_work";
														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_Date\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(date2);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(teacherAssignmentUpload1);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
													}

													else
													if(moduleType.matches("Assignment"))
													{
														moduleType1 = "Assignment";
														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_Date\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(date2);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(teacherAssignmentUpload1);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
													}

													else
													if(moduleType.matches("Circular"))
													{
														moduleType1 = "Circular";
													}

													dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"module_type\""+ lineEnd);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(moduleType1);
													dataOutputStream.writeBytes(lineEnd);
													dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

													if(optionalSubject.getText().toString().matches("") || optionalSubject.getText().toString().matches("null"))
													{
														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes("null");
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
													}

													else
													{
														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(optionalSubject.getText().toString());
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
													}


													//dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
				/*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(teacherAssignmentUpload1);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);*/
													if(count==0)
													{
														Log.d("kkkk","kkkk");

													}

													else {
														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\""
																+ returnCursor.getString(nameIndex) + "\"" + lineEnd);

														dataOutputStream.writeBytes(lineEnd);

														//returns no. of bytes present in fileInputStream
														bytesAvailable = fileInputStream.available();
														//selecting the buffer size as minimum of available bytes or 1 MB
														bufferSize = Math.min(bytesAvailable, maxBufferSize);
														//setting the buffer as byte array of size of bufferSize
														buffer = new byte[bufferSize];

														//reads bytes from FileInputStream(from 0th index of buffer to buffersize)
														bytesRead = fileInputStream.read(buffer, 0, bufferSize);

														//loop repeats till bytesRead = -1, i.e., no bytes are left to read
														while (bytesRead > 0) {
															//write the bytes read from inputstream
															dataOutputStream.write(buffer, 0, bufferSize);
															bytesAvailable = fileInputStream.available();
															bufferSize = Math.min(bytesAvailable, maxBufferSize);
															bytesRead = fileInputStream.read(buffer, 0, bufferSize);
														}

														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
													}

													serverResponseCode = connection.getResponseCode();
													String serverResponseMessage = connection.getResponseMessage();

													Log.i("jjjj", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
													InputStream is = connection.getInputStream();

													// retrieve the response from server
													int ch;

													StringBuffer b = new StringBuffer();
													while ((ch = is.read()) != -1) {
														b.append((char) ch);
													}
													String s = b.toString();
													Log.i("Response", s);

													//response code of 200 indicates the server status OK
													if (serverResponseCode == 200) {
														finish();
													}

													fileInputStream.close();
													dataOutputStream.flush();
													dataOutputStream.close();


												} catch (FileNotFoundException e) {
													e.printStackTrace();
												} catch (MalformedURLException e) {
													e.printStackTrace();
												} catch (IOException e) {
													e.printStackTrace();
												}

												dialog.dismiss();
											}
										}).start();


									} catch (FileNotFoundException e) {
										e.printStackTrace();
									}
								}
							}
						}


					}


					else
						if(moduleType.matches("HomeWork") || moduleType.matches("Assignment"))
						{
							if(teacherAssignmentUpload1.isEmpty() || teacherAssignmentUpload1.matches("") || teacherAssignmentUpload1.matches("null") || teacherAssignmentUpload1.matches("Select Subject") || teacherAssignmentUpload1.matches("0")||title.getText().toString().matches("") || title.getText().toString().matches("null") || description.getText().toString().matches("") || description.getText().toString().matches("null") )
							{
								Utils.showToast(getApplicationContext(), "Plz fill all fields");
							}



							else {


								if (choice == 0) {

									postAttendance();

								} else {
									if (googleDrive == 0) {

										if (selectedFilePath != null) {
											dialog = ProgressDialog.show(NewAssignmentTeacher.this, "", "Uploading File...", true);

											new Thread(new Runnable() {
												@Override
												public void run() {
													//creating new thread to handle Http Operations
													uploadFile(selectedFilePath);

												}
											}).start();
										} else {
											Toast.makeText(NewAssignmentTeacher.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
										}
									} else {


										try {

											dialog = ProgressDialog.show(NewAssignmentTeacher.this, "", "Uploading File...", true);
											final ParcelFileDescriptor mInputPFD = getContentResolver().openFileDescriptor(uri, "r");
											final FileDescriptor fd = mInputPFD.getFileDescriptor();

											final Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
											final int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
											int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
											returnCursor.moveToFirst();
											Log.d("hhhh", returnCursor.getString(nameIndex));


											new Thread(new Runnable() {
												@Override
												public void run() {
													//creating new thread to handle Http Operations
													int serverResponseCode = 0;

													HttpURLConnection connection;
													DataOutputStream dataOutputStream;
													String lineEnd = "\r\n";
													String twoHyphens = "--";
													String boundary = "*****";


													int bytesRead, bytesAvailable, bufferSize;
													byte[] buffer;
													int maxBufferSize = 1 * 1024 * 1024;

													try {


														FileInputStream fileInputStream = new FileInputStream(mInputPFD.getFileDescriptor());
														URL url = new URL(SERVER_URL);
														connection = (HttpURLConnection) url.openConnection();
														connection.setDoInput(true);//Allow Inputs
														connection.setDoOutput(true);//Allow Outputs
														connection.setUseCaches(false);//Don't use a cached Copy
														connection.setRequestMethod("POST");
														connection.setRequestProperty("Connection", "Keep-Alive");
														connection.setRequestProperty("ENCTYPE", "multipart/form-data");
														connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
														connection.setRequestProperty("file1",String.valueOf(uri));
														//connection.setRequestProperty("title","llll");

														//creating new dataoutputstream
														dataOutputStream = new DataOutputStream(connection.getOutputStream());
														//addFormField(dataOutputStream,"abc","a");

														//writing bytes to data outputstream
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"tech_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().teachId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"inst_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"cls_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sec_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(title.getText().toString());
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"crr_Date\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(date5);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().token);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"u_id\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(Preferences.getInstance().userId);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"desc\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(description.getText().toString());
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes("2");
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														if(moduleType.matches("HomeWork"))
														{
															moduleType1 = "home_work";
															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_Date\""+ lineEnd);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(date2);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(teacherAssignmentUpload1);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
														}

														else
														if(moduleType.matches("Assignment"))
														{
															moduleType1 = "Assignment";
															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_Date\""+ lineEnd);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(date2);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(teacherAssignmentUpload1);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
														}

														else
														if(moduleType.matches("Circular"))
														{
															moduleType1 = "Circular";
														}

														dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"module_type\""+ lineEnd);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(moduleType1);
														dataOutputStream.writeBytes(lineEnd);
														dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

														if(optionalSubject.getText().toString().matches("") || optionalSubject.getText().toString().matches("null"))
														{
															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes("null");
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
														}

														else
														{
															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(optionalSubject.getText().toString());
															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
														}


														//dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
				/*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(teacherAssignmentUpload1);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);*/
														if(count==0)
														{
															Log.d("kkkk","kkkk");

														}

														else {
															dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\""
																	+ returnCursor.getString(nameIndex) + "\"" + lineEnd);

															dataOutputStream.writeBytes(lineEnd);

															//returns no. of bytes present in fileInputStream
															bytesAvailable = fileInputStream.available();
															//selecting the buffer size as minimum of available bytes or 1 MB
															bufferSize = Math.min(bytesAvailable, maxBufferSize);
															//setting the buffer as byte array of size of bufferSize
															buffer = new byte[bufferSize];

															//reads bytes from FileInputStream(from 0th index of buffer to buffersize)
															bytesRead = fileInputStream.read(buffer, 0, bufferSize);

															//loop repeats till bytesRead = -1, i.e., no bytes are left to read
															while (bytesRead > 0) {
																//write the bytes read from inputstream
																dataOutputStream.write(buffer, 0, bufferSize);
																bytesAvailable = fileInputStream.available();
																bufferSize = Math.min(bytesAvailable, maxBufferSize);
																bytesRead = fileInputStream.read(buffer, 0, bufferSize);
															}

															dataOutputStream.writeBytes(lineEnd);
															dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
														}

														serverResponseCode = connection.getResponseCode();
														String serverResponseMessage = connection.getResponseMessage();

														Log.i("jjjj", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
														InputStream is = connection.getInputStream();

														// retrieve the response from server
														int ch;

														StringBuffer b = new StringBuffer();
														while ((ch = is.read()) != -1) {
															b.append((char) ch);
														}
														String s = b.toString();
														Log.i("Response", s);

														//response code of 200 indicates the server status OK
														if (serverResponseCode == 200) {
															finish();
														}

														fileInputStream.close();
														dataOutputStream.flush();
														dataOutputStream.close();


													} catch (FileNotFoundException e) {
														e.printStackTrace();
													} catch (MalformedURLException e) {
														e.printStackTrace();
													} catch (IOException e) {
														e.printStackTrace();
													}

													dialog.dismiss();
												}
											}).start();


										} catch (FileNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}

					//Utils.showToast(getApplicationContext(), "kk"+teacherAssignmentUpload1);


				
			}
		});

		lastDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog(999);
				showDate(year,month,day);
			}
		});
	    
	    
	}
	
	public String getStringImage(FileInputStream fis){
		Bitmap bm = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);    
		byte[] b = baos.toByteArray(); 
		String encImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encImage;
	}

	private void showFileChooser() {
		Intent intent = new Intent();
		//sets the select file to all types of files
		intent.setType("*/*");
		//allows to select data and return it
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		//starts new activity to select file and return data
		startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
	}

	private void showFileChooser1() {

		googleDrive =1;
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

		// Filter to only show results that can be "opened", such as a
		// file (as opposed to a list of contacts or timezones)
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		// Filter to show only images, using the image MIME data type.
		// If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
		// To search for all documents available via installed storage providers,
		// it would be "*/*".
		intent.setType("*/*");

		startActivityForResult(intent, READ_REQUEST_CODE);
	}


	

	private void getImages() {
		Intent intent = new Intent(mContext, ImagePickerActivity.class);
		startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
	}
	
	private void getNImages() {
		Intent intent = new Intent(mContext, ImagePickerActivity.class);
		
		// limit image pick count to only 5 images.
		intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 5);
		startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
	}

	@Override
	protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
		super.onActivityResult(requestCode, resuleCode, intent);

		switch (requestCode)
		{
			case 1: if (resuleCode == Activity.RESULT_OK) {
				if (requestCode == PICK_FILE_REQUEST) {
					if (intent == null) {
						//no data present
						return;
					}


					Uri selectedFileUri = intent.getData();
					selectedFilePath = FilePath.getPath(this, selectedFileUri);
					Log.i(TAG, "Selected File Path:" + selectedFilePath);

					count=1;

					if (selectedFilePath != null && !selectedFilePath.equals("")) {

					} else {
						Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
					}
				}
			}

				break;

			case 42:
				if (requestCode == READ_REQUEST_CODE && resuleCode == Activity.RESULT_OK) {
					// The document selected by the user won't be returned in the intent.
					// Instead, a URI to that document will be contained in the return intent
					// provided to this method as a parameter.
					// Pull that URI using resultData.getData().

					if (intent != null) {
						uri = intent.getData();
						count = 1;
					}







						//showImage(uri);
					}


				break;



			case 13:


		if (resuleCode == Activity.RESULT_OK) {
			if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES && resuleCode == RESULT_OK && intent != null && intent.getData() != null) {
				
				//Preferences.getInstance().loadPreference(getApplicationContext());
				
				Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
				
				
				
            	
            	
                
                if(parcelableUris ==null) {
                	return;
                }

                // Java doesn't allow array casting, this is a little hack
                
                
                Uri[] uris = new Uri[parcelableUris.length];
                //Uri uris1 = intent.getData();
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                //Toast.makeText(getApplicationContext(), uris[0].getPath(), Toast.LENGTH_SHORT).show();
                
                /*for(int i=0;i<=uris.length;i++)
                {
                	
                }
                selectedpath = uris[0].getPath();
                
                
                
                
                File file = new File(selectedpath);
                
                FileBody bin1 = new FileBody(file);
                Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
               
                
                MultipartEntity reqEntity = new MultipartEntity();*/
                
                
                
                
                
                
                if(uris != null) {
					for (Uri uri : uris) {
						Log.i(TAG, " uri: " + uri);
						mMedia.add(uri);
					}
                	
                	
                /* for(int i=0;i<mMedia.size();i++)
                 {
                	 
                	 
                	 
                 }*/
					// Toast.makeText(getApplicationContext(), filePath.toString(), Toast.LENGTH_LONG).show();
					// Toast.makeText(getApplicationContext(), selectedPaths.toString(), Toast.LENGTH_LONG).show();


					showMedia();


					for (int i = 1; i <= mMedia.size(); i++) {
						count = i;

					}

					//Utils.showToast(getApplicationContext(), ""+count);

					if (count == 1) {

						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);
					} else if (count == 2) {
						//urt.add(uris[0].getPath().toString());
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt1);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);
					} else if (count == 2) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);
					} else if (count == 3) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);


						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);
					} else if (count == 4) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);
					} else if (count == 5) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);

						String urt4 = uris[4].getPath().toString();
						Log.d("tag", "kio" + urt4);
						File imgFile4 = new File(urt4);
						FileInputStream fis4 = null;
						try {
							fis4 = new FileInputStream(imgFile4);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave4;
						imageLeave4 = getStringImage(fis4);
						images.add(imageLeave4);
					} else if (count == 6) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);

						String urt4 = uris[4].getPath().toString();
						Log.d("tag", "kio" + urt4);
						File imgFile4 = new File(urt4);
						FileInputStream fis4 = null;
						try {
							fis4 = new FileInputStream(imgFile4);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave4;
						imageLeave4 = getStringImage(fis4);
						images.add(imageLeave4);

						String urt5 = uris[5].getPath().toString();
						Log.d("tag", "kio" + urt5);
						File imgFile5 = new File(urt5);
						FileInputStream fis5 = null;
						try {
							fis5 = new FileInputStream(imgFile5);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave5;
						imageLeave5 = getStringImage(fis5);
						images.add(imageLeave5);
					} else if (count == 7) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);

						String urt4 = uris[4].getPath().toString();
						Log.d("tag", "kio" + urt4);
						File imgFile4 = new File(urt4);
						FileInputStream fis4 = null;
						try {
							fis4 = new FileInputStream(imgFile4);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave4;
						imageLeave4 = getStringImage(fis4);
						images.add(imageLeave4);

						String urt5 = uris[5].getPath().toString();
						Log.d("tag", "kio" + urt5);
						File imgFile5 = new File(urt5);
						FileInputStream fis5 = null;
						try {
							fis5 = new FileInputStream(imgFile5);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave5;
						imageLeave5 = getStringImage(fis5);
						images.add(imageLeave5);

						String urt6 = uris[6].getPath().toString();
						Log.d("tag", "kio" + urt6);
						File imgFile6 = new File(urt6);
						FileInputStream fis6 = null;
						try {
							fis6 = new FileInputStream(imgFile6);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave6;
						imageLeave6 = getStringImage(fis6);
						images.add(imageLeave6);
					} else if (count == 8) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);

						String urt4 = uris[4].getPath().toString();
						Log.d("tag", "kio" + urt4);
						File imgFile4 = new File(urt4);
						FileInputStream fis4 = null;
						try {
							fis4 = new FileInputStream(imgFile4);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave4;
						imageLeave4 = getStringImage(fis4);
						images.add(imageLeave4);

						String urt5 = uris[5].getPath().toString();
						Log.d("tag", "kio" + urt5);
						File imgFile5 = new File(urt5);
						FileInputStream fis5 = null;
						try {
							fis5 = new FileInputStream(imgFile5);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave5;
						imageLeave5 = getStringImage(fis5);
						images.add(imageLeave5);

						String urt6 = uris[6].getPath().toString();
						Log.d("tag", "kio" + urt6);
						File imgFile6 = new File(urt6);
						FileInputStream fis6 = null;
						try {
							fis6 = new FileInputStream(imgFile6);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave6;
						imageLeave6 = getStringImage(fis6);
						images.add(imageLeave6);

						String urt7 = uris[7].getPath().toString();
						Log.d("tag", "kio" + urt7);
						File imgFile7 = new File(urt7);
						FileInputStream fis7 = null;
						try {
							fis7 = new FileInputStream(imgFile7);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave7;
						imageLeave7 = getStringImage(fis7);
						images.add(imageLeave7);
					} else if (count == 9) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);

						String urt4 = uris[4].getPath().toString();
						Log.d("tag", "kio" + urt4);
						File imgFile4 = new File(urt4);
						FileInputStream fis4 = null;
						try {
							fis4 = new FileInputStream(imgFile4);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave4;
						imageLeave4 = getStringImage(fis4);
						images.add(imageLeave4);

						String urt5 = uris[5].getPath().toString();
						Log.d("tag", "kio" + urt5);
						File imgFile5 = new File(urt5);
						FileInputStream fis5 = null;
						try {
							fis5 = new FileInputStream(imgFile5);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave5;
						imageLeave5 = getStringImage(fis5);
						images.add(imageLeave5);

						String urt6 = uris[6].getPath().toString();
						Log.d("tag", "kio" + urt6);
						File imgFile6 = new File(urt6);
						FileInputStream fis6 = null;
						try {
							fis6 = new FileInputStream(imgFile6);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave6;
						imageLeave6 = getStringImage(fis6);
						images.add(imageLeave6);

						String urt7 = uris[7].getPath().toString();
						Log.d("tag", "kio" + urt7);
						File imgFile7 = new File(urt7);
						FileInputStream fis7 = null;
						try {
							fis7 = new FileInputStream(imgFile7);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave7;
						imageLeave7 = getStringImage(fis7);
						images.add(imageLeave7);

						String urt8 = uris[8].getPath().toString();
						Log.d("tag", "kio" + urt8);
						File imgFile8 = new File(urt8);
						FileInputStream fis8 = null;
						try {
							fis8 = new FileInputStream(imgFile8);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave8;
						imageLeave8 = getStringImage(fis8);
						images.add(imageLeave8);
					} else if (count == 10) {
						String urt = uris[0].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile = new File(urt);
						FileInputStream fis = null;
						try {
							fis = new FileInputStream(imgFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						imageLeave = getStringImage(fis);
						images.add(imageLeave);

						String urt1 = uris[1].getPath().toString();
						Log.d("tag", "kio" + urt);
						File imgFile1 = new File(urt1);
						FileInputStream fis1 = null;
						try {
							fis1 = new FileInputStream(imgFile1);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave1;
						imageLeave1 = getStringImage(fis1);
						images.add(imageLeave1);

						String urt2 = uris[2].getPath().toString();
						Log.d("tag", "kio" + urt2);
						File imgFile2 = new File(urt2);
						FileInputStream fis2 = null;
						try {
							fis2 = new FileInputStream(imgFile2);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave2;
						imageLeave2 = getStringImage(fis2);
						images.add(imageLeave2);

						String urt3 = uris[3].getPath().toString();
						Log.d("tag", "kio" + urt3);
						File imgFile3 = new File(urt3);
						FileInputStream fis3 = null;
						try {
							fis3 = new FileInputStream(imgFile3);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave3;
						imageLeave3 = getStringImage(fis3);
						images.add(imageLeave3);

						String urt4 = uris[4].getPath().toString();
						Log.d("tag", "kio" + urt4);
						File imgFile4 = new File(urt4);
						FileInputStream fis4 = null;
						try {
							fis4 = new FileInputStream(imgFile4);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave4;
						imageLeave4 = getStringImage(fis4);
						images.add(imageLeave4);

						String urt5 = uris[5].getPath().toString();
						Log.d("tag", "kio" + urt5);
						File imgFile5 = new File(urt5);
						FileInputStream fis5 = null;
						try {
							fis5 = new FileInputStream(imgFile5);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave5;
						imageLeave5 = getStringImage(fis5);
						images.add(imageLeave5);

						String urt6 = uris[6].getPath().toString();
						Log.d("tag", "kio" + urt6);
						File imgFile6 = new File(urt6);
						FileInputStream fis6 = null;
						try {
							fis6 = new FileInputStream(imgFile6);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave6;
						imageLeave6 = getStringImage(fis6);
						images.add(imageLeave6);

						String urt7 = uris[7].getPath().toString();
						Log.d("tag", "kio" + urt7);
						File imgFile7 = new File(urt7);
						FileInputStream fis7 = null;
						try {
							fis7 = new FileInputStream(imgFile7);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave7;
						imageLeave7 = getStringImage(fis7);
						images.add(imageLeave7);

						String urt8 = uris[8].getPath().toString();
						Log.d("tag", "kio" + urt8);
						File imgFile8 = new File(urt8);
						FileInputStream fis8 = null;
						try {
							fis8 = new FileInputStream(imgFile8);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave8;
						imageLeave8 = getStringImage(fis8);
						images.add(imageLeave8);

						String urt9 = uris[9].getPath().toString();
						Log.d("tag", "kio" + urt9);
						File imgFile9 = new File(urt9);
						FileInputStream fis9 = null;
						try {
							fis9 = new FileInputStream(imgFile9);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
						//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
						String imageLeave9;
						imageLeave9 = getStringImage(fis9);
						images.add(imageLeave9);
					} else if (count > 10) {
						Utils.showToast(getApplicationContext(), "Maximum images can be uploded is 10");
					}
                		/*String urt = uris[0].getPath().toString();
                		Log.d("tag", "kio"+urt);
                		File imgFile = new  File(urt);
                		FileInputStream fis = null;
                		try {
                		    fis = new FileInputStream(imgFile);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave = getStringImage(fis);
                		//images.add(imageLeave);
                		
                		String urt1 = uris[1].getPath().toString();
                		Log.d("tag", "kio1"+urt1);
                		File imgFile1 = new  File(urt1);
                		FileInputStream fis1 = null;
                		try {
                		    fis1= new FileInputStream(imgFile1);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave1 = getStringImage(fis1);
                		Log.d("harsh", "ll"+imageLeave1);
                		
                		//images.add(imageLeave);
                		
                	}
                	
                	//images.add("jkkkjkjj");
                	//images.add(imageLeave);
                	//images.add(imageLeave);
               // images.add(imageLeave);
                //images.add(imageLeave);
                images.add(imageLeave);
                images.add(imageLeave1);
                //images.add("jjkgfg");
                	
                	count = i;*/


					//Log.d("array", "kk"+images.toString());
					//Toast.makeText(getApplicationContext(), images.toString(), Toast.LENGTH_LONG).show();
                	
                	/*try {*/
					//Getting the Bitmap from Gallery
                		/*String urt = uris[0].getPath().toString();
                		Log.d("tag", ""+urt);
                		File imgFile = new  File(urt);
                		FileInputStream fis = null;
                		try {
                		    fis = new FileInputStream(imgFile);
                		    } catch (FileNotFoundException e) {
                		    e.printStackTrace();
                		}
                		//BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                		//Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                		imageLeave = getStringImage(fis);*/

					//bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
					//Toast.makeText(getApplicationContext(), imageLeave.toString(), Toast.LENGTH_LONG).show();
					Log.d("harsh", "kkkco" + images.toString());
        			/*} catch (IOException e) {
        				e.printStackTrace();
        			}*/

				}
                }

			}
break;

		}




		
	}
	
	private void showMedia() {
		// Remove all views before
		// adding the new ones.
		mSelectedImagesContainer.removeAllViews();
		
		Iterator<Uri> iterator = mMedia.iterator();
		ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
		while(iterator.hasNext()) {
			Uri uri = iterator.next();
			//Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
			
			// showImage(uri);
			Log.i(TAG, " uri: " + uri);
			if(mMedia.size() >= 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
            }
			
			View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
			
			// View removeBtn = imageHolder.findViewById(R.id.remove_media);
			// initRemoveBtn(removeBtn, imageHolder, uri);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
            
            if(!uri.toString().contains("content://")) {
    			// probably a relative uri
            	uri = Uri.fromFile(new File(uri.toString()));
            	
    		}
            
            imageFetcher.loadImage(uri, thumbnail);
            
            mSelectedImagesContainer.addView(imageHolder);
            
            // set the dimension to correctly 
            // show the image thumbnail.
            int wdpx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            int htpx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
            //Toolbar toolBar;
		}
	}
	
	
	
	
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	void showToast(CharSequence msg) {
	    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_assignment_teacher, menu);
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
	
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {


			// Create an array to populate the spinner 
			teacherAssignmentUploadVOs= new ArrayList<TeacherAssignmentUploadVO>();
			UploadClassNames = new ArrayList<String>();
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.TEACHER_ASSIGNMENT_CLASS_LIST+"?sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
			try {
				// Locate the NodeList name

				jsonarray = jsonobject.getJSONArray("subject_List");
				//System.out.print(jsonarray.toString());
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					//FeedbackVO feedbackVO = new FeedbackVO();
					TeacherAssignmentUploadVO teacherAssignmentUploadVO = new TeacherAssignmentUploadVO();
				    teacherAssignmentUploadVO.setClassId(jsonobject.optString("subject_id"));
					teacherAssignmentUploadVOs.add(teacherAssignmentUploadVO);
					UploadClassNames.add(jsonobject.optString("subject_name"));

				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				//Utils.showToast(getApplicationContext(),"No subjects defined for this teacher!!");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewAssignmentTeacher.this,R.layout.spinner_layout,UploadClassNames);

			/*teacherClass
			.setAdapter(new ArrayAdapter<String>(NewAssignmentTeacher.this,
					android.R.layout.simple_spinner_dropdown_item,
					UploadClassNames));*/
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			teacherClass.setAdapter(adapter);

			teacherClass.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					

					teacherAssignmentUpload1 = teacherAssignmentUploadVOs.get(position).getClassId().toString();
					//Utils.showToast(getApplicationContext(),teacherAssignmentUpload1);
					if(teacherAssignmentUpload1.toString().matches("0"))
					{
						Log.d("kkk","jjj");
					}

					else
					{
						((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}


			});


		}
	
	


	}
	
	 protected void postAttendance()
		{
			//setProgressBarIndeterminateVisibility(true);
			RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


			final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
			
				final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.TEACHER_SUBMIT_ASSIGNMENT;
			
			StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					JSONObject responseObject;
					//System.out.println(response);
					//Utils.showToast(getApplicationContext(), ""+response);
					//System.out.println(url1);
					try 
					{
						responseObject = new JSONObject(response);
						
						if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
						{
							
							//Utils.showToast(TeacherStudentAttendanceDetails.this,"Error Submitting Comment");
							Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
							loading.dismiss();
						}
						else
							if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
							{
								Toast.makeText(getApplicationContext(), "error6", Toast.LENGTH_LONG).show();
								loading.dismiss();
								//Utils.showToast(TeacherStudentAttendanceDetails.this, "Session Expired:Please Login Again");
							}
						
							else
								if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
								{
									
									Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_LONG).show();
									loading.dismiss();
									finish();
								}
						
								else
								{
									System.out.println("kkk");
								}
					}
					catch(JSONException e)
					{
						e.printStackTrace();
						//Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
						Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG).show();
						loading.dismiss();
					}
					//setProgressBarIndeterminateVisibility(false);

				}}, new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error) {

						Toast.makeText(getApplicationContext(), "catch1", Toast.LENGTH_LONG).show();
						loading.dismiss();
						//setProgressBarIndeterminateVisibility(false);
					}
				})
			{
				@Override
				protected Map<String,String> getParams(){
					//.getInstance().loadPreference(TeacherStudentAttendanceDetails.this);
					Map<String,String> params = new HashMap<String, String>();
					//params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
					//params.put("Students", jsonObject1.toString());
					if(moduleType.matches("HomeWork"))
					{
						moduleType1 = "home_work";
						params.put("last_Date", date2);
						params.put("sub_id", teacherAssignmentUpload1);
					}
					
					else
						if(moduleType.matches("Assignment"))
						{
							moduleType1 = "Assignment";
							params.put("last_Date", date2);
							params.put("sub_id", teacherAssignmentUpload1);
						}

					else
						if(moduleType.matches("Circular"))
						{
							moduleType1 = "Circular";
						}
					
					
					params.put("tech_id" , Preferences.getInstance().teachId);
					params.put("inst_id", Preferences.getInstance().institutionId);
					params.put("sch_id", Preferences.getInstance().schoolId);
					params.put("cls_id", Preferences.getInstance().studentClassId);
					params.put("sec_id", Preferences.getInstance().studentSectionId);
					params.put("title", title.getText().toString());

					

					params.put("crr_Date", date5);
					//System.out.print("lkkkj"+images.toString());
					if(count==0)
					{
						Log.d("kkk","kkk");
					}

					else {
						params.put("file", images.toString());
					}
					params.put("module_type", moduleType1);
					params.put("u_id", Preferences.getInstance().userId);
					
					params.put("count", ""+count);
					//params.put("user_id", Preferences.getInstance().userId);
					//params.put("feedback_id", Preferences.getInstance().feedbackId);
					params.put("token", Preferences.getInstance().token);
					params.put("device_id", Preferences.getInstance().deviceId);
					params.put("desc",description.getText().toString());
					params.put("value","1");
					if(optionalSubject.getText().toString().matches("") || optionalSubject.getText().toString().matches("null"))
					{
						params.put("opt_subject","null");
					}

					else
					{
						params.put("opt_subject",optionalSubject.getText().toString());
					}


					if(moduleType.matches("HomeWork"))
					{
						moduleType1 = "home_work";
						Log.d("last_Date", date2);
						Log.d("sub_id", teacherAssignmentUpload1);
					}

					else
					if(moduleType.matches("Assignment"))
					{
						moduleType1 = "Assignment";
						Log.d("last_Date", date2);
						Log.d("sub_id", teacherAssignmentUpload1);
					}

					else
					if(moduleType.matches("Circular"))
					{
						moduleType1 = "Circular";
					}


					Log.d("tech_id" , Preferences.getInstance().teachId);
					Log.d("inst_id", Preferences.getInstance().institutionId);
					Log.d("sch_id", Preferences.getInstance().schoolId);
					Log.d("cls_id", Preferences.getInstance().studentClassId);
					Log.d("sec_id", Preferences.getInstance().studentSectionId);
					Log.d("title", title.getText().toString());



					Log.d("crr_Date", date5);
					//System.out.print("lkkkj"+images.toString());
					if(count==0)
					{
						Log.d("kkk","kkk");
					}

					else {
						Log.d("file", images.toString());
					}
					Log.d("module_type", moduleType1);
					Log.d("u_id", Preferences.getInstance().userId);

					Log.d("count", ""+count);
					//params.put("user_id", Preferences.getInstance().userId);
					//params.put("feedback_id", Preferences.getInstance().feedbackId);
					Log.d("token", Preferences.getInstance().token);
					Log.d("device_id", Preferences.getInstance().deviceId);
					Log.d("desc",description.getText().toString());
					Log.d("value","1");
					if(optionalSubject.getText().toString().matches("") || optionalSubject.getText().toString().matches("null"))
					{
						Log.d("opt_subject","null");
					}

					else
					{
						Log.d("opt_subject",optionalSubject.getText().toString());
					}
					return params;
				}};		

				requestObject.setRetryPolicy(new DefaultRetryPolicy(
						25000, 
						DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				
					queue.add(requestObject);
				
				
		
		
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
			showDate(arg1, arg2+1, arg3);
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

		date2 = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

		date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
		lastDate.setText(date1);
		lastDate.setTextColor(Color.BLACK);
	}

	private boolean isReadStorageAllowed() {
		//Getting the permission status
		int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

		//If permission is granted returning true
		if (result == PackageManager.PERMISSION_GRANTED)
			return true;

		//If permission is not granted returning false
		return false;
	}

	//Requesting permission
	private void requestStoragePermission(){

		if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
			//If the user has denied the permission previously your code will come to this block
			//Here you can explain why you need this permission
			//Explain here why you need this permission
		}

		//And finally ask for the permission
		ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
	}

	//This method will be called when the user will tap on allow or deny
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		//Checking the request code of our request
		if(requestCode == STORAGE_PERMISSION_CODE){

			//If permission is granted
			if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

				//Displaying a toast
				//Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
				//getImages();
				/*if(count1 ==0) {
					requestStoragePermission1();
				}

				else
				{
					getImages();
				}*/
			}else{
				//Displaying another toast if permission is not granted
				Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
			}
		}
	}

	//Requesting permission
	private void requestStoragePermission1(){

		if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
			//If the user has denied the permission previously your code will come to this block
			//Here you can explain why you need this permission
			//Explain here why you need this permission
		}

		//And finally ask for the permission
		ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},STORAGE_PERMISSION_CODE);
		count1=1;
	}

	private  boolean checkAndRequestPermissions() {
		int permissionSendMessage = ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE);
		int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		int locationPermission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		List<String> listPermissionsNeeded = new ArrayList<>();
		if (locationPermission != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.CAMERA);
		}
		if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		}
		if (locationPermission1 != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),STORAGE_PERMISSION_CODE);
			return false;
		}
		return true;
	}

	//android upload file to server
	public int uploadFile(final String selectedFilePath){

		int serverResponseCode = 0;

		HttpURLConnection connection;
		DataOutputStream dataOutputStream;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";


		int bytesRead,bytesAvailable,bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File selectedFile = new File(selectedFilePath);


		String[] parts = selectedFilePath.split("/");
		final String fileName = parts[parts.length-1];

		if (!selectedFile.isFile()){
			dialog.dismiss();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
				}
			});
			return 0;
		}else{
			try{
				FileInputStream fileInputStream = new FileInputStream(selectedFile);
				URL url = new URL(SERVER_URL);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);//Allow Inputs
				connection.setDoOutput(true);//Allow Outputs
				connection.setUseCaches(false);//Don't use a cached Copy
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("ENCTYPE", "multipart/form-data");
				connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				connection.setRequestProperty("file1",selectedFilePath);
				//connection.setRequestProperty("title","llll");

				//creating new dataoutputstream
				dataOutputStream = new DataOutputStream(connection.getOutputStream());
				//addFormField(dataOutputStream,"abc","a");

				//writing bytes to data outputstream
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"tech_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().teachId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"inst_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().institutionId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sch_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().schoolId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"cls_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().studentClassId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sec_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().studentSectionId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(title.getText().toString());
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"crr_Date\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(date5);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().token);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"device_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().deviceId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"u_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(Preferences.getInstance().userId);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"desc\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(description.getText().toString());
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"value\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes("2");
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

				if(moduleType.matches("HomeWork"))
				{
					moduleType1 = "home_work";
					dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_Date\""+ lineEnd);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(date2);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);



					dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(teacherAssignmentUpload1);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

					if(optionalSubject.getText().toString().matches("") || optionalSubject.getText().toString().matches("null"))
					{
						dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes("null");
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
					}

					else
					{
						dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes(optionalSubject.getText().toString());
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
					}
				}

				else
				if(moduleType.matches("Assignment"))
				{
					moduleType1 = "Assignment";
					dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_Date\""+ lineEnd);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(date2);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

					dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(teacherAssignmentUpload1);
					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

					if(optionalSubject.getText().toString().matches("") || optionalSubject.getText().toString().matches("null"))
					{
						dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes("null");
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
					}

					else
					{
						dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"opt_subject\""+ lineEnd);
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes(optionalSubject.getText().toString());
						dataOutputStream.writeBytes(lineEnd);
						dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
					}
				}

				else
				if(moduleType.matches("Circular"))
				{
					moduleType1 = "Circular";
				}

				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"module_type\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(moduleType1);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);




				//dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
				/*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sub_id\""+ lineEnd);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(teacherAssignmentUpload1);
				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);*/
				if(count==0)
				{
					Log.d("kkkk","kkkk");

				}

				else {
					dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\""
							+ selectedFilePath + "\"" + lineEnd);

					dataOutputStream.writeBytes(lineEnd);

					//returns no. of bytes present in fileInputStream
					bytesAvailable = fileInputStream.available();
					//selecting the buffer size as minimum of available bytes or 1 MB
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					//setting the buffer as byte array of size of bufferSize
					buffer = new byte[bufferSize];

					//reads bytes from FileInputStream(from 0th index of buffer to buffersize)
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

					//loop repeats till bytesRead = -1, i.e., no bytes are left to read
					while (bytesRead > 0) {
						//write the bytes read from inputstream
						dataOutputStream.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}

					dataOutputStream.writeBytes(lineEnd);
					dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				}
                /*dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"abc\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes("a");//your parameter value
                dataOutputStream.writeBytes(lineEnd); //to add multiple parameters write Content-Disposition: form-data; name=\"your parameter name\"" + crlf again and keep repeating till here :)
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens);*/



                /*dataOutputStream.writeBytes(String.valueOf((twoHyphens+boundary+lineEnd).getBytes()));
                dataOutputStream.write( "Content-Type: text/plain".getBytes());
                dataOutputStream.write( ("Content-Disposition: form-data; name="+ "abc"+lineEnd).getBytes());;
                dataOutputStream.write( (lineEnd + "a" + lineEnd).getBytes());*/


				serverResponseCode = connection.getResponseCode();
				String serverResponseMessage = connection.getResponseMessage();

				Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
				InputStream is = connection.getInputStream();

				// retrieve the response from server
				int ch;

				StringBuffer b =new StringBuffer();
				while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
				String s=b.toString();
				Log.i("Response",s);

				//response code of 200 indicates the server status OK
				if(serverResponseCode == 200){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							finish();
							//tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
						}
					});
				}

				//closing the input and output streams
				fileInputStream.close();
				dataOutputStream.flush();
				dataOutputStream.close();



			} catch (FileNotFoundException e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(NewAssignmentTeacher.this,"File Not Found",Toast.LENGTH_SHORT).show();
					}
				});
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Toast.makeText(NewAssignmentTeacher.this, "URL error!", Toast.LENGTH_SHORT).show();

			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(NewAssignmentTeacher.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
			}
			dialog.dismiss();
			return serverResponseCode;
		}

	}

	private void selectImage() {
		final CharSequence[] items = { "Phone Storage", "Cloud Drives", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(NewAssignmentTeacher.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Phone Storage")) {
					showFileChooser1();
				} else if (items[item].equals("Cloud Drives")) {
					if(Build.VERSION.SDK_INT>=19) {
						showFileChooser1();
					}

					else
					{
						Utils.showToast(getApplicationContext(),"Your device is not compatible for this feature");
					}

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	//This method will be called when the user will tap on allow or deny

}
