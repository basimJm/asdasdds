package com.schoofi.activitiess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.TeacherSelfLeaveListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EmployeeLeaveTypeVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;
import smtchahal.materialspinner.MaterialSpinner;

public class TeacherLeaveApplication extends AppCompatActivity {

    private Button  leaveUpload, leaveSubmit;
    private TextView leaveStartingDate,leaveStartingTime,leaveEndingDate,leaveEndingTime;
    private EditText editLeaveSubject, editLeaveDetails;
    private TextView textDepartmentName;
    String UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_UPLOAD;
    private ImageView imageBack, imageLeave;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private PopupWindow calendarPopup, calendarPopup1,calendarPopup2;
    int width;
    String startDate, endDate,feeDate;
    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    String date1,date2,date3;
    int clickable = 0;
    private JSONArray studentLeaveListArray;

    int count1 = 0;
    String image;
    int count = 0;
    int count2 = 0;
    private Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1888;
    //String image = getStringImage(bitmap);
    private int PICK_IMAGE_REQUEST = 1;
    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String subject, details;
    private int STORAGE_PERMISSION_CODE = 23;
    int f;
    private static final String TAG = "Sample";

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private SwitchDateTimeDialogFragment dateTimeFragment,dateTimeFragment1;

    Calendar calendar ;

    int year,month,day,hour,minute,second;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private MaterialSpinner leaveTypeMaterialSpinner;
    ArrayList<String> leaveTypeName;
    ArrayList<EmployeeLeaveTypeVO> employeeLeaveTypeVOs;
    JSONObject jsonobject;
    String leaveTypeId="0";
    JSONArray jsonarray;
    SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Switch halfDay;
    final Context context = this;
    String leaveTypeName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_teacher_leave_application);

        leaveTypeMaterialSpinner = (MaterialSpinner) findViewById(R.id.spinner_leave_type);
        leaveTypeMaterialSpinner.setBackgroundResource(R.drawable.grey_button);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;

        imageBack = (ImageView) findViewById(R.id.img_back);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);*/

        halfDay = (Switch) findViewById(R.id.mySwitch);

        textDepartmentName = (TextView) findViewById(R.id.edit_department_name);
        Preferences.getInstance().loadPreference(getApplicationContext());
        textDepartmentName.setText(Preferences.getInstance().departmentName);

        leaveStartingDate = (TextView) findViewById(R.id.txt_student_leave_starting_date);
        leaveStartingTime = (TextView) findViewById(R.id.txt_student_leave_starting_time);
        leaveEndingDate = (TextView) findViewById(R.id.txt_student_leave_ending_date);
        leaveEndingTime = (TextView) findViewById(R.id.txt_student_leave_ending_time);
        leaveUpload = (Button) findViewById(R.id.btn_student_leave_upload_file);
        leaveSubmit = (Button) findViewById(R.id.btn_student_leave_submit);

        imageLeave = (ImageView) findViewById(R.id.imageLeave);
        editLeaveSubject = (EditText) findViewById(R.id.edit_leave_subject);
        editLeaveDetails = (EditText) findViewById(R.id.edit_student_leave_details);

        leaveStartingDate.setOnClickListener(onEditTextClickListener);
        leaveEndingDate.setOnClickListener(onEditTextClickListener1);
        leaveStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(halfDay.isChecked() ||  leaveTypeName1.matches("Restricted Holiday"))
                {
                    Utils.showToast(getApplicationContext(),"Disabled because leave type is Half day!");
                }

                else {
                    showPicker(v);
                }
            }
        });

        leaveEndingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(halfDay.isChecked() || leaveTypeName1.matches("Restricted Holiday") )
                {
                    Utils.showToast(getApplicationContext(),"Disabled because leave type is Half day!");
                }

                else {
                    showPicker1(v);
                }
            }
        });



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



        new DownloadJSON().execute();

        leaveSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editLeaveSubject.getText().toString().matches("")  || leaveTypeId.matches("0")  )
                {
                    Utils.showToast(getApplicationContext(),"Please fill all fields");
                }

                else
                {
                    if(f==1)
                    {
                        if(leaveTypeName1.matches("Over Time") || leaveTypeName1.matches("Outdoor Duty"))
                        {
                            if(leaveStartingTime.getText().toString().matches("") || leaveEndingTime.getText().toString().matches(""))
                            {
                                Utils.showToast(getApplicationContext(),"Select Time");
                            }
                            else
                            {
                                postLeave();
                            }
                        }
                        else
                        {
                            postLeave();
                        }

                    }

                    else
                    {
                        if(leaveTypeName1.matches("Over Time") || leaveTypeName1.matches("Outdoor Duty"))
                        {
                            if(leaveStartingTime.getText().toString().matches("") || leaveEndingTime.getText().toString().matches(""))
                            {
                                Utils.showToast(getApplicationContext(),"Select Time");
                            }
                            else
                            {
                                postLeave1();
                            }
                        }
                        else
                        {
                            postLeave1();
                        }
                    }
                }
            }
        });
    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                Preferences.getInstance().loadPreference(TeacherLeaveApplication.this);
                //studentId = Preferences.getInstance().studentId;

                try {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                    image = getStringImage(thumbnail);
                    imageLeave.setImageBitmap(thumbnail);
                }
                catch (OutOfMemoryError e)
                {
                    Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
                }

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                //String[] projection = {MediaStore.MediaColumns.DATA};
                Log.d("harsh", "kk" + selectedImageUri.toString());

                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    //Setting the Bitmap to ImageView
                    imageLeave.setImageBitmap(bitmap);
                    image = getStringImage(bitmap);



                }
                catch (OutOfMemoryError e)
                {
                    Utils.showToast(getApplicationContext(),"Image size too high Please select different image");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherLeaveApplication.this);
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

   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current textView
        savedInstanceState.putCharSequence(STATE_TEXTVIEW, leaveStarting.getText());
        savedInstanceState.putCharSequence(STATE_TEXTVIEW, leaveEnding.getText());
        super.onSaveInstanceState(savedInstanceState);
    }*/

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
                        System.out.println(s);
                        try {
                        JSONObject responseObject;

                            responseObject = new JSONObject(s);
                            if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
                                Toast.makeText(getApplicationContext(), "Uploading Failed", Toast.LENGTH_SHORT).show();

                                    else
                                        if(responseObject.has("Msg") && responseObject.getString("Msg").equals("1"))
                            {
                                Toast.makeText(getApplicationContext(), "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                {
                                    Toast.makeText(getApplicationContext(), responseObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                                }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Showing toast message of the response
                        //Toast.makeText(TeacherLeaveApplication.this, s , Toast.LENGTH_SHORT).show();




                       // finish();
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
                params.put("image", image);
               // params.put("stu_id", Preferences.getInstance().studentId);
                params.put("email", Preferences.getInstance().userEmailId);
                params.put("u_id", Preferences.getInstance().userId);
                params.put("crr_Date", date);
                params.put("from_date", date1);
                if(!halfDay.isChecked())
                {
                    if(leaveTypeName1.matches("Restricted Holiday"))
                    {
                        params.put("to_date", date1);
                    }
                    else {
                        params.put("to_date", date2);
                    }
                    params.put("from_time",leaveStartingTime.getText().toString());
                    params.put("to_time",leaveEndingTime.getText().toString());
                    params.put("half_day","0");

                }
                else
                {
                    params.put("half_day","1");
                    params.put("to_date", date1);
                    Log.d("kkk","jjjj");
                }


                params.put("leave_type",leaveTypeId);
                params.put("value","1");
                params.put("leave_subject", editLeaveSubject.getText().toString());
                if(editLeaveDetails.getText().toString().matches(""))
                {
                    params.put("leave_reason","Not Mentioned");
                }
                else
                {
                    params.put("leave_reason", editLeaveDetails.getText().toString());
                }

               // params.put("value","1");
                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("name",Preferences.getInstance().Name);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("dept_id",Preferences.getInstance().departmentId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("emp_id" ,Preferences.getInstance().employeeId);
               // params.put("sec_id",Preferences.getInstance().studentSectionId);
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
                        System.out.println(s);
                        try {
                            JSONObject responseObject;

                            responseObject = new JSONObject(s);
                            if(responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
                                Toast.makeText(getApplicationContext(), "Uploading Failed", Toast.LENGTH_SHORT).show();

                            else
                            if(responseObject.has("Msg") && responseObject.getString("Msg").equals("1"))
                            {
                                Toast.makeText(getApplicationContext(), "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), responseObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                //params.put("stu_id", Preferences.getInstance().studentId);
                params.put("email", Preferences.getInstance().userEmailId);
                params.put("u_id", Preferences.getInstance().userId);
                params.put("crr_Date", date);
                params.put("from_date", date1);

                if(!halfDay.isChecked())
                {

                    if(leaveTypeName1.matches("Restricted Holiday"))
                    {
                        params.put("to_date", date1);
                    }
                    else {
                        params.put("to_date", date2);
                    }
                    params.put("from_time",leaveStartingTime.getText().toString());
                    params.put("to_time",leaveEndingTime.getText().toString());
                    params.put("half_day","0");

                }
                else
                {
                    params.put("half_day","1");
                    params.put("to_date", date1);
                    Log.d("kkk","jjjj");
                }


                params.put("leave_type",leaveTypeId);
                params.put("value","1");
                params.put("leave_subject", editLeaveSubject.getText().toString());
                if(editLeaveDetails.getText().toString().matches(""))
                {
                    params.put("leave_reason","Not Mentioned");
                }
                else
                {
                    params.put("leave_reason", editLeaveDetails.getText().toString());
                }
                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("dept_id",Preferences.getInstance().departmentId);
                params.put("name",Preferences.getInstance().Name);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("emp_id" ,Preferences.getInstance().employeeId);
               // params.put("sec_id",Preferences.getInstance().studentSectionId);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            employeeLeaveTypeVOs= new ArrayList<EmployeeLeaveTypeVO>();
            leaveTypeName = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_TYPE+"?token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId);
            try {
                // Locate the NodeList name
                System.out.print(jsonobject.toString());
                jsonarray = jsonobject.getJSONArray("leave_type");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //FeedbackVO feedbackVO = new FeedbackVO();
                    EmployeeLeaveTypeVO employeeLeaveTypeVO = new EmployeeLeaveTypeVO();
                    employeeLeaveTypeVO.setEmployeeTypeId(jsonobject.optString("id"));
                    employeeLeaveTypeVO.setEmployeeTypeName(jsonobject.optString("full_name"));
                    employeeLeaveTypeVOs.add(employeeLeaveTypeVO);
                    leaveTypeName.add(jsonobject.optString("full_name"));

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

            leaveTypeMaterialSpinner
                    .setAdapter(new ArrayAdapter<String>(TeacherLeaveApplication.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            leaveTypeName));

            leaveTypeMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub



                    leaveTypeId = employeeLeaveTypeVOs.get(position).getEmployeeTypeId().toString();
                    leaveTypeName1 = employeeLeaveTypeVOs.get(position).getEmployeeTypeName().toString();

                    if(leaveTypeName1.contentEquals("Casual Leave") || leaveTypeName1.contentEquals("Leave Without Pay") || leaveTypeName1.contentEquals("Compensatory Holiday"))
                    {
                        halfDay.setClickable(true);
                    }
                    else
                    {
                        halfDay.setClickable(false);
                        halfDay.setChecked(false);

                    }

                    getStudentLeaveList();




                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });


        }




    }

    public void showPicker(View v){
        Calendar now = Calendar.getInstance();
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(com.schoofi.activitiess.TimePicker view, int hourOfDay, int minute, int seconds) {

                leaveStartingTime.setText(String.format("%02d", hourOfDay)+
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));

            }

            /*@Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                *//*time.setText(getString(R.string.time) + String.format("%02d", hourOfDay)+
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));*//*
            }*/
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        mTimePicker.show();
    }

    public void showPicker1(View v){
        Calendar now = Calendar.getInstance();
        MyTimePickerDialog mTimePicker1 = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(com.schoofi.activitiess.TimePicker view, int hourOfDay, int minute, int seconds) {

                leaveEndingTime.setText(String.format("%02d", hourOfDay)+
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));

            }

            /*@Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                *//*time.setText(getString(R.string.time) + String.format("%02d", hourOfDay)+
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));*//*
            }*/
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), true);
        mTimePicker1.show();
    }

    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(TeacherLeaveApplication.this);
                CalendarPickerView calendarView = new CalendarPickerView(TeacherLeaveApplication.this);
                calendarView.setListener(dateSelectionListener);
                calendarPopup.setContentView(calendarView);
                calendarPopup.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup.setHeight(1);
                calendarPopup.setWidth(width);
                calendarPopup.setOutsideTouchable(true);
            }
            calendarPopup.showAtLocation(editLeaveSubject, Gravity.CENTER, 0, 0);
        }
    };

    private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (halfDay.isChecked() || leaveTypeName1.matches("Restricted Holiday")) {
                Utils.showToast(getApplicationContext(), "Disabled because leave type is Half Day");
            } else {
                if (calendarPopup1 == null) {
                    calendarPopup1 = new PopupWindow(TeacherLeaveApplication.this);
                    CalendarPickerView calendarView = new CalendarPickerView(TeacherLeaveApplication.this);
                    calendarView.setListener(dateSelectionListener1);
                    calendarPopup1.setContentView(calendarView);
                    calendarPopup1.setWindowLayoutMode(
                            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    calendarPopup1.setHeight(1);
                    calendarPopup1.setWidth(width);
                    calendarPopup1.setOutsideTouchable(true);
                }
                calendarPopup1.showAtLocation(editLeaveSubject, Gravity.CENTER, 0, 0);
            }
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

            leaveStartingDate.setText(formatter1.format(selectedDate.getTime()));
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

            leaveEndingDate.setText(formatter1.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            date2 = formatter.format(selectedDate.getTime());

        }
    };

    protected void getStudentLeaveList()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_COUNT/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                        Utils.showToast(getApplicationContext(),"");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("responseObject"))
                    {
                        studentLeaveListArray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=studentLeaveListArray && studentLeaveListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentLeaveListArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_LEAVE_COUNT+"?device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&emp_id="+Preferences.getInstance().employeeId+"&leave_type="+leaveTypeId,e);

                            LayoutInflater li = LayoutInflater.from(context);
                            View promptsView = li.inflate(R.layout.leave_count_popup, null);



                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);




                            final TextView present = (TextView) promptsView.findViewById(R.id.text_total_present);
                            final TextView absent =  (TextView) promptsView.findViewById(R.id.text_total_absent);
                            //final TextView leave =   (TextView) promptsView.findViewById(R.id.text_total_leave);
                            final TextView total =   (TextView) promptsView.findViewById(R.id.text_total);

                           present.setText("Yearly Count Left: "+studentLeaveListArray.getJSONObject(0).getString("allowed_count_yearly_left"));
                            absent.setText("Monthly Count Left: "+studentLeaveListArray.getJSONObject(0).getString("allowed_count_monthly_left"));







                            alertDialogBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int id) {
                                                    // get user input and set it to result


                                                    // edit text



                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                    int id) {
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();

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
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                //params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                //params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("emp_id",Preferences.getInstance().employeeId);
                params.put("leave_type",leaveTypeId);
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
    private void toa()
    {
        System.out.println("aaa");
    }
}
