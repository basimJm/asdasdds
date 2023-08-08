package com.schoofi.activitiess;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EventCoordinator1VO;
import com.schoofi.utils.EventCoordinatorVO2;
import com.schoofi.utils.EventCoordinatorVO3;
import com.schoofi.utils.EventCoordinatorVO4;
import com.schoofi.utils.EventCoordinatorVO5;
import com.schoofi.utils.EventUploadAudienceVO;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.MasterEventVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;
import smtchahal.materialspinner.MaterialSpinner;
//import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public class EventAdminUpload extends FragmentsActivity{


    EditText eventName,eventFees,eventChoice,eventTotalSeats,eventPlace,eventDescription,eventStartDate,eventEndDate,eventFeeDate,eventStartTime,eventEndTime;
    Spinner audience;
    Button done,upload;
    ArrayList<String> groupName;
    ArrayList<EventUploadAudienceVO> groupId;
    JSONObject jsonobject;
    JSONArray jsonarray;
    String groupId1;
    EventUploadAudienceVO eventUploadAudienceVO = new EventUploadAudienceVO();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    int count1 = 0;
    String image;
    int count = 0;
    int count2 = 0;
    int count4=0;
    private Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1888;
    //String image = getStringImage(bitmap);
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView imageBack, imageLeave;
    private PopupWindow calendarPopup, calendarPopup1,calendarPopup2;
    String startDate, endDate,feeDate;
    String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    String date1,date2,date3;
    String UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_UPLOAD_URL;
    int hour1,minute1,hour2,minute2;
    int width;
    int f;
    private MaterialSpinner eventIsHosptalised;
    private MaterialSpinner masterEventSpinner,eventCoordinator1,eventCoordinator2,eventCoordinator3,eventCoordinator4,eventCoordinator5;
    JSONObject masterEventSpiinerJsonObject,eventCoordinatorJsonObject1,eventCoordinatorJsonObject2,eventCoordinatorJsonObject3,eventCoordinatorJsonObject4,eventCoordinatorJsonObject5;
    JSONArray masterEventSpinnerJsonArray,eventCoordinatorJsonArray1,eventCoordinatorJsonArray2,eventCoordinatorJsonArray3,eventCoordinatorJsonArray4,eventCoordinatorJsonArray5;
    ArrayList<String> masterEventName;
    ArrayList<MasterEventVO> masterEventVOS;
    ArrayList<String> eventCoordinatorName1;
    ArrayList<EventCoordinator1VO> eventCoordinator1VOS;
    ArrayList<String> eventCoordinatorName2;
    ArrayList<EventCoordinatorVO2> eventCoordinator2VOS;
    ArrayList<String> eventCoordinatorName3;
    ArrayList<EventCoordinatorVO3> eventCoordinator3VOS;
    ArrayList<String> eventCoordinatorName4;
    ArrayList<EventCoordinatorVO4> eventCoordinator4VOS;
    ArrayList<String> eventCoordinatorName5;
    ArrayList<EventCoordinatorVO5> eventCoordinator5VOS;
    private EditText doctorName,organiserName;
    public static final CharSequence[] DAYS_OPTIONS  = {"Yes","No"};
    String gender1="",masterEventSpinnnerId1="",masterEventCoordinatorId1="",masterEventCoordinatorId2="",masterEventCoordinatorId3="",masterEventCoordinatorId4="",masterEventCoordinatorId5="";

    String eventStartDate1,eventName1,eventFees1,eventEndDate1,eventFeesDate1,eventChoice1,eventTotalSeats1,eventPlace1,eventDescription1,eventStartTime1,eventEndTime1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Event Admin Upload");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_event_admin_upload);

        eventStartDate = (EditText) findViewById(R.id.text_eventStartDate);
        imageBack = (ImageView) findViewById(R.id.img_back);
        eventEndDate = (EditText) findViewById(R.id.text_eventEndDate);
        eventFeeDate = (EditText) findViewById(R.id.text_evenFeetDate);
        eventName = (EditText) findViewById(R.id.edit_eventTitle);
        eventFees = (EditText) findViewById(R.id.text_eventFees);
        eventChoice = (EditText) findViewById(R.id.text_eventChoice);
        eventTotalSeats = (EditText) findViewById(R.id.text_eventTotalSeats);
        eventPlace = (EditText) findViewById(R.id.edit_Place);
        eventDescription = (EditText) findViewById(R.id.edit_eventDetails);
        audience = (Spinner) findViewById(R.id.spin_groupName);
        eventIsHosptalised = (MaterialSpinner) findViewById(R.id.spinner_class);
        eventIsHosptalised.setBackgroundResource(R.drawable.grey_button);
        organiserName = (EditText) findViewById(R.id.text_eventOrganiser);
        doctorName = (EditText) findViewById(R.id.text_eventDoctorName);
        organiserName.setVisibility(View.GONE);
        doctorName.setVisibility(View.GONE);
        done = (Button) findViewById(R.id.btn_done);
        upload = (Button) findViewById(R.id.btn_student_leave_upload_file);
        imageLeave = (ImageView) findViewById(R.id.imageLeave);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;

        masterEventSpinner = (MaterialSpinner) findViewById(R.id.spinner_master_event);
        eventCoordinator1 = (MaterialSpinner) findViewById(R.id.spinner_coordinator1);
        eventCoordinator2 = (MaterialSpinner) findViewById(R.id.spinner_coordinator2);
        eventCoordinator3 = (MaterialSpinner) findViewById(R.id.spinner_coordinator3);
        eventCoordinator4 = (MaterialSpinner) findViewById(R.id.spinner_coordinator4);
        eventCoordinator5 = (MaterialSpinner) findViewById(R.id.spinner_coordinator5);

        masterEventSpinner.setBackgroundResource(R.drawable.grey_button);
        eventCoordinator1.setBackgroundResource(R.drawable.grey_button);
        eventCoordinator2.setBackgroundResource(R.drawable.grey_button);
        eventCoordinator3.setBackgroundResource(R.drawable.grey_button);
        eventCoordinator4.setBackgroundResource(R.drawable.grey_button);
        eventCoordinator5.setBackgroundResource(R.drawable.grey_button);

        if(Utils.isNetworkAvailable(EventAdminUpload.this))
        {
            new DownloadJSON().execute();
            new DownloadJSON1().execute();
            new DownloadJSON2().execute();
            new DownloadJSON3().execute();
            new DownloadJSON4().execute();
            new DownloadJSON5().execute();
            new DownloadJSON6().execute();
        }

        eventStartTime = (EditText) findViewById(R.id.text_eventStartTime);
        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectTimeToDisplay(v);
                showPicker(v);

            }
        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (EventAdminUpload.this, R.layout.support_simple_spinner_dropdown_item, DAYS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Specify the layout to use when the list of choices appears
        eventIsHosptalised.setAdapter(adapter); // Apply the adapter to the spinner







        eventIsHosptalised.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // showToast("Spinner2: position=" + position + ", id= " + id + ", value=" + parent.getItemAtPosition(position));
                gender1 = "" + parent.getItemAtPosition(position);

                if(gender1.matches("Yes"))
                {
                    doctorName.setVisibility(View.VISIBLE);
                    organiserName.setVisibility(View.VISIBLE);
                }

                else
                {
                    doctorName.setVisibility(View.GONE);
                    organiserName.setVisibility(View.GONE);
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //showToast("Spinner2: unselected");
            }
        });






        eventEndTime = (EditText) findViewById(R.id.text_eventEndTime);
        eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectTimeToDisplay1(v);
                showPicker1(v);

            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        eventStartDate.setOnClickListener(onEditTextClickListener);
        eventEndDate.setOnClickListener(onEditTextClickListener1);
        eventFeeDate.setOnClickListener(onEditTextClickListener2);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectImage();
                f=1;

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventChoice1 = eventChoice.getText().toString();
                eventDescription1 = eventDescription.getText().toString();
                eventEndDate1 = eventEndDate.getText().toString();
                eventFeesDate1 = eventFeeDate.getText().toString();
                eventStartDate1 = eventStartDate.getText().toString();
                eventPlace1 = eventPlace.getText().toString();
                eventTotalSeats1 = eventTotalSeats.getText().toString();
                eventFees1 = eventFees.getText().toString();
                eventName1 = eventName.getText().toString();
                eventStartTime1 = eventStartTime.getText().toString();
                eventEndTime1 = eventEndTime.getText().toString();

                //Utils.showToast(getApplicationContext(),date1);

                if(f==1)
                {
                    if(eventName.getText().toString().matches(""))
                    {
                        Utils.showToast(getApplicationContext(),"Please fill the title");
                    }
                    else {
                        postLeave();
                    }
                }

                else {
                    if(eventName.getText().toString().matches(""))
                    {
                        Utils.showToast(getApplicationContext(),"Please fill the title");
                    }
                    else {
                        postLeave1();
                    }
                }
            }
        });





    }

    public void showPicker(View v){
        Calendar now = Calendar.getInstance();
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(com.schoofi.activitiess.TimePicker view, int hourOfDay, int minute, int seconds) {

                eventStartTime.setText(String.format("%02d", hourOfDay)+
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

                eventEndTime.setText(String.format("%02d", hourOfDay)+
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





    private void postLeave() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            com.schoofi.modals.Request request = new com.schoofi.modals.Request();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventAdminUpload.this, "Uploading", "Please wait...", true, true);
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
                data.put("ins_id", Preferences.getInstance().institutionId);
                data.put("school_id", Preferences.getInstance().schoolId);
                data.put("program_name", eventName1);
                data.put("program_start_date", date1);
                data.put("program_end_date", date2);
                data.put("program_start_time", eventStartTime1);
                data.put("program_end_time", eventEndTime1);
                if(eventFees1.matches(""))
                {
                    data.put("program_fees","");
                }
                else {
                    data.put("program_fees", eventFees1);
                    data.put("fees_submission_date", date3);
                }

                if(masterEventSpinnnerId1.matches("") || masterEventSpinnnerId1.matches("null"))
                {
                    data.put("parent_event","");
                }
                else
                {
                    data.put("parent_event",masterEventSpinnnerId1);
                }

                if(masterEventCoordinatorId1.matches("") || masterEventCoordinatorId1.matches("null"))
                {
                    data.put("coordinator1","");
                }
                else
                {
                    data.put("coordinator1",masterEventCoordinatorId1);
                }

                if(masterEventCoordinatorId2.matches("") || masterEventCoordinatorId2.matches("null"))
                {
                    data.put("coordinator2","");
                }
                else
                {
                    data.put("coordinator2",masterEventCoordinatorId2);
                }

                if(masterEventCoordinatorId3.matches("") || masterEventCoordinatorId3.matches("null"))
                {
                    data.put("coordinator3","");
                }
                else
                {
                    data.put("coordinator3",masterEventCoordinatorId3);
                }

                if(masterEventCoordinatorId4.matches("") || masterEventCoordinatorId4.matches("null"))
                {
                    data.put("coordinator4","");
                }
                else
                {
                    data.put("coordinator4",masterEventCoordinatorId4);
                }

                if(masterEventCoordinatorId5.matches("") || masterEventCoordinatorId5.matches("null"))
                {
                    data.put("coordinator5","");
                }
                else
                {
                    data.put("coordinator5",masterEventCoordinatorId5);
                }


                data.put("group_id",groupId1);
                data.put("choice",eventChoice1);
                data.put("image",image);
                data.put("event_detail",eventDescription1);
                data.put("user_id",Preferences.getInstance().userId);
                data.put("place",eventPlace1);
                data.put("total_seats",eventTotalSeats1);
                data.put("token", Preferences.getInstance().token);
                data.put("device_id", Preferences.getInstance().deviceId);

                String result = request.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
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
                        //System.out.println(s);
                        //Showing toast message of the response
                        Toast.makeText(EventAdminUpload.this, s, Toast.LENGTH_SHORT).show();
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
                        Utils.showToast(getApplicationContext(), "error uploading event");
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
                params.put("ins_id", Preferences.getInstance().institutionId);
                params.put("school_id", Preferences.getInstance().schoolId);
                params.put("program_name", eventName.getText().toString());
                params.put("program_start_date", date1);
                params.put("program_end_date", date2);
                params.put("program_start_time", eventStartTime1);
                params.put("program_end_time", eventEndTime1);
                if(eventFees.getText().toString().matches(""))
                {
                    params.put("program_fees","");
                }
                else {

                    params.put("program_fees", eventFees.getText().toString());

                }

                if(eventFeeDate.getText().toString().matches(""))
                {
                    params.put("fees_submission_date","");
                }

                else
                {
                    params.put("fees_submission_date", date3);
                }

                if(masterEventSpinnnerId1.matches("") || masterEventSpinnnerId1.matches("null"))
                {
                    params.put("parent_event","");
                }
                else
                {
                    params.put("parent_event",masterEventSpinnnerId1);
                }

                if(masterEventCoordinatorId1.matches("") || masterEventCoordinatorId1.matches("null"))
                {
                    params.put("coordinator1","");
                }
                else
                {
                    params.put("coordinator1",masterEventCoordinatorId1);
                }

                if(masterEventCoordinatorId2.matches("") || masterEventCoordinatorId2.matches("null"))
                {
                    params.put("coordinator2","");
                }
                else
                {
                    params.put("coordinator2",masterEventCoordinatorId2);
                }

                if(masterEventCoordinatorId3.matches("") || masterEventCoordinatorId3.matches("null"))
                {
                    params.put("coordinator3","");
                }
                else
                {
                    params.put("coordinator3",masterEventCoordinatorId3);
                }

                if(masterEventCoordinatorId4.matches("") || masterEventCoordinatorId4.matches("null"))
                {
                    params.put("coordinator4","");
                }
                else
                {
                    params.put("coordinator4",masterEventCoordinatorId4);
                }

                if(masterEventCoordinatorId5.matches("") || masterEventCoordinatorId5.matches("null"))
                {
                    params.put("coordinator5","");
                }
                else
                {
                    params.put("coordinator5",masterEventCoordinatorId5);
                }

                params.put("group_id",groupId1);
                params.put("choice",eventChoice.getText().toString());
                params.put("user_id",Preferences.getInstance().userId);
                //data.put("image",image);
                params.put("event_detail",eventDescription.getText().toString());
                params.put("place",eventPlace.getText().toString());
                params.put("total_seats",eventTotalSeats.getText().toString());
                params.put("token", Preferences.getInstance().token);
                params.put("device_id", Preferences.getInstance().deviceId);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(EventAdminUpload.this);
                CalendarPickerView calendarView = new CalendarPickerView(EventAdminUpload.this);
                calendarView.setListener(dateSelectionListener);
                calendarPopup.setContentView(calendarView);
                calendarPopup.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup.setHeight(1);
                calendarPopup.setWidth(width);
                calendarPopup.setOutsideTouchable(true);
            }
            calendarPopup.showAtLocation(eventPlace, Gravity.CENTER, 0, 0);
        }
    };

    private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup1 == null) {
                calendarPopup1 = new PopupWindow(EventAdminUpload.this);
                CalendarPickerView calendarView = new CalendarPickerView(EventAdminUpload.this);
                calendarView.setListener(dateSelectionListener1);
                calendarPopup1.setContentView(calendarView);
                calendarPopup1.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup1.setHeight(1);
                calendarPopup1.setWidth(width);
                calendarPopup1.setOutsideTouchable(true);
            }
            calendarPopup1.showAtLocation(eventPlace, Gravity.CENTER, 0, 0);
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

            eventStartDate.setText(formatter1.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            date1 = formatter.format(selectedDate.getTime());
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));

        }
    };

    private View.OnClickListener onEditTextClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup2 == null) {
                calendarPopup2 = new PopupWindow(EventAdminUpload.this);
                CalendarPickerView calendarView = new CalendarPickerView(EventAdminUpload.this);
                calendarView.setListener(dateSelectionListener2);
                calendarPopup2.setContentView(calendarView);
                calendarPopup2.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup2.setHeight(1);
                calendarPopup2.setWidth(width);
                calendarPopup2.setOutsideTouchable(true);
            }
            calendarPopup2.showAtLocation(eventPlace, Gravity.CENTER, 0, 0);
        }
    };

    private CalendarNumbersView.DateSelectionListener dateSelectionListener2 = new CalendarNumbersView.DateSelectionListener() {
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

            eventFeeDate.setText(formatter1.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            date3 = formatter.format(selectedDate.getTime());
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

            eventEndDate.setText(formatter1.format(selectedDate.getTime()));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                image = getStringImage(thumbnail);
                imageLeave.setImageBitmap(thumbnail);
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
                String[] projection = {MediaStore.MediaColumns.DATA};
                Log.d("harsh", "kk" + selectedImageUri.toString());
                Preferences.getInstance().loadPreference(EventAdminUpload.this);
                //studentId = Preferences.getInstance().studentId;
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
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //gggg= http://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/;

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EventAdminUpload.this);
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

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            groupId = new ArrayList<EventUploadAudienceVO>();
            groupName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_UPLOAD_AUDIENCE_URL+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("groups");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    EventUploadAudienceVO eventUploadAudienceVO1 = new EventUploadAudienceVO();

                    eventUploadAudienceVO1.setGroupId(jsonobject.optString("group_id"));
                    groupId.add(eventUploadAudienceVO1);

                    groupName.add(jsonobject.optString("group_name"));

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

            audience
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,groupName
                            ));

            audience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    groupId1 = groupId.get(position).getGroupId().toString();
                    //System.out.println(groupId1);



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
            masterEventVOS = new ArrayList<MasterEventVO>();
            masterEventName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/
			Log.d("poii",AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.MASTER_EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);

            // JSON file URL address
            masterEventSpiinerJsonObject = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.MASTER_EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                masterEventSpinnerJsonArray = masterEventSpiinerJsonObject.getJSONArray("responseObject");
                for (int i = 0; i < masterEventSpinnerJsonArray.length(); i++) {
                    masterEventSpiinerJsonObject = masterEventSpinnerJsonArray.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    MasterEventVO masterEventVO1 = new MasterEventVO();

                    masterEventVO1.setMasterEventId(masterEventSpiinerJsonObject.optString("event_id"));
                    masterEventVOS.add(masterEventVO1);

                    masterEventName.add(masterEventSpiinerJsonObject.optString("program_name"));

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

            masterEventSpinner
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,masterEventName
                    ));

            masterEventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    masterEventSpinnnerId1 = masterEventVOS.get(position).getMasterEventId().toString();
                    //System.out.println(groupId1);



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
            eventCoordinator1VOS = new ArrayList<EventCoordinator1VO>();
            eventCoordinatorName1 = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            eventCoordinatorJsonObject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_COORDINATOR_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                eventCoordinatorJsonArray1 = eventCoordinatorJsonObject1.getJSONArray("responseObject");
                for (int i = 0; i < eventCoordinatorJsonArray1.length(); i++) {
                    eventCoordinatorJsonObject1 = eventCoordinatorJsonArray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    EventCoordinator1VO eventCoordinator1VO = new EventCoordinator1VO();

                    eventCoordinator1VO.setEventCoordinatrId1(eventCoordinatorJsonObject1.optString("emp_id"));
                    eventCoordinator1VOS.add(eventCoordinator1VO);

                    String emp_first_name,emp_middle_name,emp_last_name;
                    emp_first_name = eventCoordinatorJsonObject1.optString("emp_first_name");
                    emp_middle_name = eventCoordinatorJsonObject1.optString("emp_middle_name");
                    emp_last_name = eventCoordinatorJsonObject1.optString("emp_last_name");

                    if(emp_first_name.matches("") || emp_first_name.matches("null"))
                    {
                        emp_first_name = "";
                    }
                    else
                    {
                        emp_first_name = eventCoordinatorJsonObject1.optString("emp_first_name");
                    }

                    if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                    {
                        emp_middle_name = "";
                    }
                    else
                    {
                        emp_middle_name = " "+eventCoordinatorJsonObject1.optString("emp_middle_name");
                    }
                    if(emp_last_name.matches("") || emp_last_name.matches("null"))
                    {
                        emp_last_name ="";
                    }
                    else
                    {
                        emp_last_name = " "+eventCoordinatorJsonObject1.optString("emp_last_name");
                    }



                    eventCoordinatorName1.add(emp_first_name+emp_middle_name+emp_last_name);

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

            eventCoordinator1
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,eventCoordinatorName1
                    ));

            eventCoordinator1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    masterEventCoordinatorId1 = eventCoordinator1VOS.get(position).getEventCoordinatrId1().toString();
                    //System.out.println(groupId1);



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    private class DownloadJSON3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            eventCoordinator2VOS = new ArrayList<EventCoordinatorVO2>();
            eventCoordinatorName2 = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            eventCoordinatorJsonObject2 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_COORDINATOR_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                eventCoordinatorJsonArray2 = eventCoordinatorJsonObject2.getJSONArray("responseObject");
                for (int i = 0; i < eventCoordinatorJsonArray2.length(); i++) {
                    eventCoordinatorJsonObject2 = eventCoordinatorJsonArray2.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    EventCoordinatorVO2 eventCoordinatorVO2 = new EventCoordinatorVO2();

                    eventCoordinatorVO2.setEventCoordinatrId2(eventCoordinatorJsonObject2.optString("emp_id"));
                    eventCoordinator2VOS.add(eventCoordinatorVO2);

                    String emp_first_name,emp_middle_name,emp_last_name;
                    emp_first_name = eventCoordinatorJsonObject2.optString("emp_first_name");
                    emp_middle_name = eventCoordinatorJsonObject2.optString("emp_middle_name");
                    emp_last_name = eventCoordinatorJsonObject2.optString("emp_last_name");

                    if(emp_first_name.matches("") || emp_first_name.matches("null"))
                    {
                        emp_first_name = "";
                    }
                    else
                    {
                        emp_first_name = eventCoordinatorJsonObject2.optString("emp_first_name");
                    }

                    if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                    {
                        emp_middle_name = "";
                    }
                    else
                    {
                        emp_middle_name = " "+eventCoordinatorJsonObject2.optString("emp_middle_name");
                    }
                    if(emp_last_name.matches("") || emp_last_name.matches("null"))
                    {
                        emp_last_name ="";
                    }
                    else
                    {
                        emp_last_name = " "+eventCoordinatorJsonObject2.optString("emp_last_name");
                    }



                    eventCoordinatorName2.add(emp_first_name+emp_middle_name+emp_last_name);

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

            eventCoordinator2
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,eventCoordinatorName2
                    ));

            eventCoordinator2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    masterEventCoordinatorId2 = eventCoordinator2VOS.get(position).getEventCoordinatrId2().toString();
                    //System.out.println(groupId1);



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    private class DownloadJSON4 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            eventCoordinator3VOS = new ArrayList<EventCoordinatorVO3>();
            eventCoordinatorName3 = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            eventCoordinatorJsonObject3 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_COORDINATOR_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                eventCoordinatorJsonArray3 = eventCoordinatorJsonObject3.getJSONArray("responseObject");
                for (int i = 0; i < eventCoordinatorJsonArray3.length(); i++) {
                    eventCoordinatorJsonObject3 = eventCoordinatorJsonArray3.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    EventCoordinatorVO3 eventCoordinatorVO3 = new EventCoordinatorVO3();

                    eventCoordinatorVO3.setEventCoordinatrId3(eventCoordinatorJsonObject3.optString("emp_id"));
                    eventCoordinator3VOS.add(eventCoordinatorVO3);

                    String emp_first_name,emp_middle_name,emp_last_name;
                    emp_first_name = eventCoordinatorJsonObject3.optString("emp_first_name");
                    emp_middle_name = eventCoordinatorJsonObject3.optString("emp_middle_name");
                    emp_last_name = eventCoordinatorJsonObject3.optString("emp_last_name");

                    if(emp_first_name.matches("") || emp_first_name.matches("null"))
                    {
                        emp_first_name = "";
                    }
                    else
                    {
                        emp_first_name = eventCoordinatorJsonObject3.optString("emp_first_name");
                    }

                    if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                    {
                        emp_middle_name = "";
                    }
                    else
                    {
                        emp_middle_name = " "+eventCoordinatorJsonObject3.optString("emp_middle_name");
                    }
                    if(emp_last_name.matches("") || emp_last_name.matches("null"))
                    {
                        emp_last_name ="";
                    }
                    else
                    {
                        emp_last_name = " "+eventCoordinatorJsonObject3.optString("emp_last_name");
                    }



                    eventCoordinatorName3.add(emp_first_name+emp_middle_name+emp_last_name);

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

            eventCoordinator3
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,eventCoordinatorName3
                    ));

            eventCoordinator3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    masterEventCoordinatorId3 = eventCoordinator3VOS.get(position).getEventCoordinatrId3().toString();
                    //System.out.println(groupId1);



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    private class DownloadJSON5 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            eventCoordinator4VOS = new ArrayList<EventCoordinatorVO4>();
            eventCoordinatorName4 = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            eventCoordinatorJsonObject4 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_COORDINATOR_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                eventCoordinatorJsonArray4 = eventCoordinatorJsonObject4.getJSONArray("responseObject");
                for (int i = 0; i < eventCoordinatorJsonArray4.length(); i++) {
                    eventCoordinatorJsonObject4 = eventCoordinatorJsonArray4.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    EventCoordinatorVO4 eventCoordinatorVO4 = new EventCoordinatorVO4();

                    eventCoordinatorVO4.setEventCoordinatrId4(eventCoordinatorJsonObject4.optString("emp_id"));
                    eventCoordinator4VOS.add(eventCoordinatorVO4);

                    String emp_first_name,emp_middle_name,emp_last_name;
                    emp_first_name = eventCoordinatorJsonObject4.optString("emp_first_name");
                    emp_middle_name = eventCoordinatorJsonObject4.optString("emp_middle_name");
                    emp_last_name = eventCoordinatorJsonObject4.optString("emp_last_name");

                    if(emp_first_name.matches("") || emp_first_name.matches("null"))
                    {
                        emp_first_name = "";
                    }
                    else
                    {
                        emp_first_name = eventCoordinatorJsonObject4.optString("emp_first_name");
                    }

                    if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                    {
                        emp_middle_name = "";
                    }
                    else
                    {
                        emp_middle_name = " "+eventCoordinatorJsonObject4.optString("emp_middle_name");
                    }
                    if(emp_last_name.matches("") || emp_last_name.matches("null"))
                    {
                        emp_last_name ="";
                    }
                    else
                    {
                        emp_last_name = " "+eventCoordinatorJsonObject4.optString("emp_last_name");
                    }



                    eventCoordinatorName4.add(emp_first_name+emp_middle_name+emp_last_name);

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

            eventCoordinator4
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,eventCoordinatorName4
                    ));

            eventCoordinator4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    masterEventCoordinatorId4 = eventCoordinator4VOS.get(position).getEventCoordinatrId4().toString();
                    //System.out.println(groupId1);



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    private class DownloadJSON6 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            eventCoordinator5VOS = new ArrayList<EventCoordinatorVO5>();
            eventCoordinatorName5 = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            eventCoordinatorJsonObject5 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.EVENT_COORDINATOR_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&ins_id="+Preferences.getInstance().institutionId+"&token="+Preferences.getInstance().token+"&device_id="+ Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                eventCoordinatorJsonArray5 = eventCoordinatorJsonObject5.getJSONArray("responseObject");
                for (int i = 0; i < eventCoordinatorJsonArray5.length(); i++) {
                    eventCoordinatorJsonObject5 = eventCoordinatorJsonArray5.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    EventCoordinatorVO5 eventCoordinatorVO5 = new EventCoordinatorVO5();

                    eventCoordinatorVO5.setEventCoordinatrId5(eventCoordinatorJsonObject5.optString("emp_id"));
                    eventCoordinator5VOS.add(eventCoordinatorVO5);

                    String emp_first_name,emp_middle_name,emp_last_name;
                    emp_first_name = eventCoordinatorJsonObject5.optString("emp_first_name");
                    emp_middle_name = eventCoordinatorJsonObject5.optString("emp_middle_name");
                    emp_last_name = eventCoordinatorJsonObject5.optString("emp_last_name");

                    if(emp_first_name.matches("") || emp_first_name.matches("null"))
                    {
                        emp_first_name = "";
                    }
                    else
                    {
                        emp_first_name = eventCoordinatorJsonObject5.optString("emp_first_name");
                    }

                    if(emp_middle_name.matches("") || emp_middle_name.matches("null"))
                    {
                        emp_middle_name = "";
                    }
                    else
                    {
                        emp_middle_name = " "+eventCoordinatorJsonObject5.optString("emp_middle_name");
                    }
                    if(emp_last_name.matches("") || emp_last_name.matches("null"))
                    {
                        emp_last_name ="";
                    }
                    else
                    {
                        emp_last_name = " "+eventCoordinatorJsonObject5.optString("emp_last_name");
                    }



                    eventCoordinatorName5.add(emp_first_name+emp_middle_name+emp_last_name);

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

            eventCoordinator5
                    .setAdapter(new ArrayAdapter<String>(EventAdminUpload.this,
                            android.R.layout.simple_spinner_dropdown_item,eventCoordinatorName5
                    ));

            eventCoordinator5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    masterEventCoordinatorId5 = eventCoordinator5VOS.get(position).getEventCoordinatrId5().toString();
                    //System.out.println(groupId1);



                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }
}
