package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
//import com.schoofi.activities.PaymentActivity;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TeacherEventListDetails extends AppCompatActivity {

    private JSONArray teacherEventListArray;
    private TextView eventDate,eventMonth,eventTitle,eventStartingTime,eventEndingTime,eventVenue,eventFees,eventDetails,eventFeesSubmisionDate,eventChoice,eventAudience,confirmed,seatsLeft;
    private Button payNow;
    private TextView eventParentName,eventCoordinator1,eventCoordinator2,eventCoordinator3,eventCoordinator4,eventCoordinator5;
    private TextView eventStartingTime1;
    private ImageView eventGalleryIcon,eventMainImage,imageUpload,back,subEventsImageView;
    private int position;
    String crrDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String crrDate1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String combinedDate;
    String date,year,month,date1,date4,date5,date6,date7,date8;
    Date date2,date3,date9;
    String confirmationId,fees,eventId;
    String Rs;
    ArrayList aList= new ArrayList();
    String image,value;
    String value4,parent_event,program_name;
    View view;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Teacher EventList Details");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_teacher_event_list_details);
        eventDate = (TextView) findViewById(R.id.text_eventdate);
        eventMonth = (TextView) findViewById(R.id.text_eventmonth);
        eventTitle = (TextView) findViewById(R.id.text_title);
        eventStartingTime = (TextView) findViewById(R.id.text_eventStartingTime);
        eventEndingTime = (TextView) findViewById(R.id.text_eventEndingTime);
        eventStartingTime1 = (TextView) findViewById(R.id.text_eventStartingTime1);
        subEventsImageView = (ImageView) findViewById(R.id.subEvents);
        eventFees = (TextView) findViewById(R.id.text_fees);
        eventVenue = (TextView) findViewById(R.id.text_venue);
        eventGalleryIcon = (ImageView) findViewById(R.id.gallery);
        eventMainImage = (ImageView) findViewById(R.id.imageViewEventDetails);
        position = getIntent().getExtras().getInt("position");
        eventDetails = (TextView) findViewById(R.id.text_eventDetails);
        eventFeesSubmisionDate = (TextView) findViewById(R.id.text_feesSubmissionDate);
        payNow = (Button) findViewById(R.id.btn_payNow);
        seatsLeft = (TextView) findViewById(R.id.text_seatsLeft1);
        confirmed = (TextView) findViewById(R.id.text_confirmed1);
        value = getIntent().getStringExtra("value");
        imageUpload = (ImageView) findViewById(R.id.addgallery);

        value4 = getIntent().getStringExtra("value4");
        eventParentName = (TextView) findViewById(R.id.text_parent_event_name);
        eventCoordinator1 = (TextView) findViewById(R.id.text_coordinator1);
        eventCoordinator2 = (TextView) findViewById(R.id.text_coordinator2);
        eventCoordinator3 = (TextView) findViewById(R.id.text_coordinator3);
        eventCoordinator4 = (TextView) findViewById(R.id.text_coordinator4);
        eventCoordinator5 = (TextView) findViewById(R.id.text_coordinator5);

        parent_event = getIntent().getStringExtra("parent_event");
        program_name = getIntent().getStringExtra("program_name");
        view = findViewById(R.id.view);
        
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        // eventAudience = (TextView) findViewById(R.id.text_audience);
        // eventChoice = (TextView) findViewById(R.id.text_choice);
        back = (ImageView) findViewById(R.id.img_back);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Rs = getApplicationContext().getString(R.string.Rs);


        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddEventGallery.class);
                intent.putExtra("eventId",eventId);
                startActivity(intent);
            }
        });
        if(value4.matches("5"))
        {
            Log.d("op","ll");
            initData1();

        }
        else
        {
            initData();
        }

        //eventGalleryIcon.setVisibility(View.INVISIBLE);

        eventGalleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherEventListDetails.this,EventSharingScreen.class);
                intent.putExtra("position",position);
                intent.putExtra("startDate",date4);
                intent.putExtra("endDate",date6);
                intent.putExtra("value",value);
                intent.putExtra("param","2");
                startActivity(intent);
            }
        });



        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(payNow.getText().toString().matches("Add Album"))
                {

                    Intent intent = new Intent(getApplicationContext(),AddEventGallery.class);
                    intent.putExtra("eventId",eventId);
                    startActivity(intent);
                }

                else
                {
                    aList = new ArrayList<String>(Arrays.asList(image.split(",")));
                    if(image.matches("null") || image.matches(""))
                    {
                        Utils.showToast(getApplicationContext(),"No image present");
                    }

                    else
                    {
                        Intent intent = new Intent(getApplicationContext(),StudentFeedBackImages.class);
                        intent.putExtra("array", aList);
                        intent.putExtra("position",position);
                        intent.putExtra("param","2");
                        intent.putExtra("value",value);
                        startActivity(intent);
                    }

                }

            }
        });



       /* eventGalleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),AddEventGallery.class);
                intent.putExtra("eventId",eventId);
                startActivity(intent);

            }
        });*/






    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&value="+value);
            if(e == null)
            {
                teacherEventListArray= null;
            }
            else
            {
                teacherEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherEventListArray!= null)
            try {
                // Picasso.with(StudentEventListDetails.this).load(AppConstants.SERVER_URLS.SERVER_URL + studentEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(eventMainImage);
                Glide.with(TeacherEventListDetails.this).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherEventListArray.getJSONObject(position).getString("event_image")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(eventMainImage);
                date = teacherEventListArray.getJSONObject(position).getString("program_start_date");
                date1 = date.substring(8, 10);
                month = date.substring(5, 7);
                year = date.substring(0, 4);
                //System.out.println(date1 + month);

                eventDate.setText(date1);

                if(teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("null") || teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("") || teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("0"))
                {
                    subEventsImageView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    subEventsImageView.setVisibility(View.VISIBLE);
                }

                subEventsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eventId = "";
                        try {
                            eventId = teacherEventListArray.getJSONObject((Integer) v.getTag()).getString("event_id");

                            Intent intent = new Intent(TeacherEventListDetails.this, StudentSubEvents.class);
                            intent.putExtra("event_id", eventId);
                            intent.putExtra("value4", "5");

                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                });

                switch (Integer.parseInt(month)) {
                    case 1:

                        eventMonth.setText("Jan");
                        break;

                    case 2:
                        eventMonth.setText("Feb");
                        break;

                    case 3:
                        eventMonth.setText("Mar");
                        break;

                    case 4:
                        eventMonth.setText("Apr");
                        break;

                    case 5:
                        eventMonth.setText("May");
                        break;

                    case 6:
                        eventMonth.setText("Jun");
                        break;

                    case 7:
                        eventMonth.setText("Jul");
                        break;

                    case 8:
                        eventMonth.setText("Aug");
                        break;

                    case 9:
                        eventMonth.setText("Sep");
                        break;

                    case 10:
                        eventMonth.setText("Oct");
                        break;

                    case 11:
                        eventMonth.setText("Nov");
                        break;

                    case 12:
                        eventMonth.setText("Dec");
                        break;

                    default:
                        eventMonth.setText("Month");
                        break;

                }
                eventId = teacherEventListArray.getJSONObject(position).getString("event_id");
                fees = teacherEventListArray.getJSONObject(position).getString("program_fees");
                image = teacherEventListArray.getJSONObject(position).getString("images");
                //confirmationId = teacherEventListArray.getJSONObject(position).getString("confirm_id");

                 if(fees.matches("") || fees.matches("null"))
                 {
                     eventFees.setText("Free");
                 }

                 else
                 {
                     eventFees.setText("Fees: "+Rs + teacherEventListArray.getJSONObject(position).getString("program_fees"));
                 }










                eventTitle.setText(teacherEventListArray.getJSONObject(position).getString("program_name"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date2 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat format2 = new SimpleDateFormat("EEEE d MMM yyyy");
                date4 = format2.format(date2);

                combinedDate = teacherEventListArray.getJSONObject(position).getString("program_start_date") + " " + teacherEventListArray.getJSONObject(position).getString("program_start_time");
                Date d1 = null;
                Date d2 = null;
                SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    d1 = format3.parse(crrDate);
                    d2 = format3.parse(combinedDate);

                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    /*System.out.print(diffDays + " days, ");
                    System.out.print(diffHours + " hours, ");
                    System.out.print(diffMinutes + " minutes, ");
                    System.out.print(diffSeconds + " seconds.");*/
                    //Toast.makeText(getApplicationContext(), String.valueOf(diffDays), Toast.LENGTH_SHORT).show();
                    if (diffDays <=0 && diffHours <=0 && diffMinutes <=0) {
                        eventStartingTime1.setText("Event Closed");
                    } else {
                        eventStartingTime1.setText(String.valueOf(diffDays) + " days, " + String.valueOf(diffHours) + " hours to go");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(teacherEventListArray.getJSONObject(position).getString("images").toString().matches("null") || teacherEventListArray.getJSONObject(position).getString("images").toString().matches(""))
                {
                    payNow.setText("Add Album");
                }

                else
                {
                    payNow.setText("View Album");
                    eventGalleryIcon.setVisibility(View.VISIBLE);
                }

                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date67=null;
                Date date68 =null;
                try {
                    try {
                        date67  = inFormat.parse(teacherEventListArray.getJSONObject(position).getString("program_start_date"));
                        date68 = inFormat.parse(teacherEventListArray.getJSONObject(position).getString("program_end_date"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String goal = outFormat.format(date67);
                String goal1 = outFormat.format(date68);

                String date56="";
                Date date45=null;

                date5 = teacherEventListArray.getJSONObject(position).getString("program_end_date");
                date56 = teacherEventListArray.getJSONObject(position).getString("program_start_date");
                try {
                    date3 = format.parse(date5);
                    date45 = format.parse(date56);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date6 = format2.format(date3);
                String date46="";
                date46 = format2.format(date45);

                //eventEndingTime.setText("Ending Date: " + date6 + " at " + teacherEventListArray.getJSONObject(position).getString("program_end_time"));

                try {
                    String _24HourTime = teacherEventListArray.getJSONObject(position).getString("program_start_time");
                    String _24HourTime1 = teacherEventListArray.getJSONObject(position).getString("program_end_time");
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh a");
                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                    Date _24HourDt1 = _24HourSDF.parse(_24HourTime1);
                    System.out.println(_24HourDt);
                    System.out.println(_12HourSDF.format(_24HourDt));
                    eventStartingTime.setText(date46 + " at " + _12HourSDF.format(_24HourDt)+"-");
                    eventEndingTime.setText(date6+" at "+ _12HourSDF.format(_24HourDt1));
                } catch (Exception e) {
                    e.printStackTrace();
                }





                // eventFees.setText("Fees: Rs " + studentEventListArray.getJSONObject(position).getString("program_fees"));

                eventVenue.setText("Venue: " + teacherEventListArray.getJSONObject(position).getString("place"));
                seatsLeft.setText(teacherEventListArray.getJSONObject(position).getString("available_seats"));
                confirmed.setText(teacherEventListArray.getJSONObject(position).getString("confirmed"));
                //eventChoice.setText(studentEventListArray.getJSONObject(position).getString("choice"));
                //eventAudience.setText(studentEventListArray.getJSONObject(position).getString("group_name"));
                eventDetails.setText(teacherEventListArray.getJSONObject(position).getString("event_detail"));

                date7 = teacherEventListArray.getJSONObject(position).getString("fees_submission_date");
                try {
                    date9 = format.parse(date7);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date8 = format2.format(date9);




                    eventFeesSubmisionDate.setText("Fees Submission Date: " + date8);


                if(date7.matches("") || date7.matches("null") || date7.matches("0000-00-00"))
                {
                    eventFeesSubmisionDate.setVisibility(View.GONE);
                }

                else
                {
                    eventFeesSubmisionDate.setVisibility(View.VISIBLE);
                }

                if(parent_event.matches(teacherEventListArray.getJSONObject(position).getString("event_id")))
                {
                    eventParentName.setVisibility(View.GONE);
                }
                else
                {
                    eventParentName.setText(program_name);
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator1").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator1").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator1").matches("0"))
                {
                    eventCoordinator1.setText("Coordinator1: Not mentioned");
                }
                else
                {
                    eventCoordinator1.setText("Coordinator1: "+teacherEventListArray.getJSONObject(position).getString("coordinator1"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator2").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator2").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator2").matches("0"))
                {
                    eventCoordinator2.setText("Coordinator2: Not mentioned");
                }
                else
                {
                    eventCoordinator2.setText("Coordinator2: "+teacherEventListArray.getJSONObject(position).getString("coordinator2"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator3").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator3").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator3").matches("0"))
                {
                    eventCoordinator3.setText("Coordinator3: Not mentioned");
                }
                else
                {
                    eventCoordinator3.setText("Coordinator3: "+teacherEventListArray.getJSONObject(position).getString("coordinator3"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator4").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator4").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator4").matches("0"))
                {
                    eventCoordinator4.setText("Coordinator4: Not mentioned");
                }
                else
                {
                    eventCoordinator4.setText("Coordinator4: "+teacherEventListArray.getJSONObject(position).getString("coordinator4"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator5").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator5").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator5").matches("0"))
                {
                    eventCoordinator5.setText("Coordinator5: Not mentioned");
                }
                else
                {
                    eventCoordinator5.setText("Coordinator5: "+teacherEventListArray.getJSONObject(position).getString("coordinator5"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void initData1()
    {

        Preferences.getInstance().loadPreference(getApplicationContext());
      //  Utils.showToast(getApplicationContext(),"ppp");


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SUB_EVENT_LIST+"?event_id="+parent_event+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                teacherEventListArray= null;
               // Utils.showToast(getApplicationContext(),"ko");
            }
            else
            {
                teacherEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherEventListArray!= null)
            try {
            //Utils.showToast(getApplicationContext(),"po");
                // Picasso.with(StudentEventListDetails.this).load(AppConstants.SERVER_URLS.SERVER_URL + studentEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(eventMainImage);
                Glide.with(TeacherEventListDetails.this).load(AppConstants.SERVER_URLS.IMAGE_URL+teacherEventListArray.getJSONObject(position).getString("event_image")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(eventMainImage);
                date = teacherEventListArray.getJSONObject(position).getString("program_start_date");
                date1 = date.substring(8, 10);
                month = date.substring(5, 7);
                year = date.substring(0, 4);
                //System.out.println(date1 + month);

                eventDate.setText(date1);

                if(teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("null") || teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("") || teacherEventListArray.getJSONObject(position).getString("sub_event_count").matches("0"))
                {
                    subEventsImageView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    subEventsImageView.setVisibility(View.VISIBLE);
                }

                subEventsImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eventId = "";
                        try {
                            eventId = teacherEventListArray.getJSONObject((Integer) v.getTag()).getString("event_id");

                            Intent intent = new Intent(TeacherEventListDetails.this, StudentSubEvents.class);
                            intent.putExtra("event_id", eventId);
                            intent.putExtra("value4", "5");

                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                });

                switch (Integer.parseInt(month)) {
                    case 1:

                        eventMonth.setText("Jan");
                        break;

                    case 2:
                        eventMonth.setText("Feb");
                        break;

                    case 3:
                        eventMonth.setText("Mar");
                        break;

                    case 4:
                        eventMonth.setText("Apr");
                        break;

                    case 5:
                        eventMonth.setText("May");
                        break;

                    case 6:
                        eventMonth.setText("Jun");
                        break;

                    case 7:
                        eventMonth.setText("Jul");
                        break;

                    case 8:
                        eventMonth.setText("Aug");
                        break;

                    case 9:
                        eventMonth.setText("Sep");
                        break;

                    case 10:
                        eventMonth.setText("Oct");
                        break;

                    case 11:
                        eventMonth.setText("Nov");
                        break;

                    case 12:
                        eventMonth.setText("Dec");
                        break;

                    default:
                        eventMonth.setText("Month");
                        break;

                }
                eventId = teacherEventListArray.getJSONObject(position).getString("event_id");
                fees = teacherEventListArray.getJSONObject(position).getString("program_fees");
                image = teacherEventListArray.getJSONObject(position).getString("images");
                //confirmationId = teacherEventListArray.getJSONObject(position).getString("confirm_id");

                if(fees.matches("") || fees.matches("null"))
                {
                    eventFees.setText("Free");
                }

                else
                {
                    eventFees.setText("Fees: "+Rs + teacherEventListArray.getJSONObject(position).getString("program_fees"));
                }










                eventTitle.setText(teacherEventListArray.getJSONObject(position).getString("program_name"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date2 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat format2 = new SimpleDateFormat("EEEE d MMM yyyy");
                date4 = format2.format(date2);

                combinedDate = teacherEventListArray.getJSONObject(position).getString("program_start_date") + " " + teacherEventListArray.getJSONObject(position).getString("program_start_time");
                Date d1 = null;
                Date d2 = null;
                SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    d1 = format3.parse(crrDate);
                    d2 = format3.parse(combinedDate);

                    //in milliseconds
                    long diff = d2.getTime() - d1.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    /*System.out.print(diffDays + " days, ");
                    System.out.print(diffHours + " hours, ");
                    System.out.print(diffMinutes + " minutes, ");
                    System.out.print(diffSeconds + " seconds.");*/
                    //Toast.makeText(getApplicationContext(), String.valueOf(diffDays), Toast.LENGTH_SHORT).show();
                    if (diffDays <=0 && diffHours <=0 && diffMinutes <=0) {
                        eventStartingTime1.setText("Event Closed");
                    } else {
                        eventStartingTime1.setText(String.valueOf(diffDays) + " days, " + String.valueOf(diffHours) + " hours to go");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(teacherEventListArray.getJSONObject(position).getString("images").toString().matches("null") || teacherEventListArray.getJSONObject(position).getString("images").toString().matches(""))
                {
                    payNow.setText("Add Album");
                }

                else
                {
                    payNow.setText("View Album");
                    eventGalleryIcon.setVisibility(View.VISIBLE);
                }

                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date67=null;
                Date date68 =null;
                try {
                    try {
                        date67  = inFormat.parse(teacherEventListArray.getJSONObject(position).getString("program_start_date"));
                        date68 = inFormat.parse(teacherEventListArray.getJSONObject(position).getString("program_end_date"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String goal = outFormat.format(date67);
                String goal1 = outFormat.format(date68);

                String date56="";
                Date date45=null;

                date5 = teacherEventListArray.getJSONObject(position).getString("program_end_date");
                date56 = teacherEventListArray.getJSONObject(position).getString("program_start_date");
                try {
                    date3 = format.parse(date5);
                    date45 = format.parse(date56);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date6 = format2.format(date3);
                String date46="";
                date46 = format2.format(date45);

                //eventEndingTime.setText("Ending Date: " + date6 + " at " + teacherEventListArray.getJSONObject(position).getString("program_end_time"));

                try {
                    String _24HourTime = teacherEventListArray.getJSONObject(position).getString("program_start_time");
                    String _24HourTime1 = teacherEventListArray.getJSONObject(position).getString("program_end_time");
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh a");
                    Date _24HourDt = _24HourSDF.parse(_24HourTime);
                    Date _24HourDt1 = _24HourSDF.parse(_24HourTime1);
                    System.out.println(_24HourDt);
                    System.out.println(_12HourSDF.format(_24HourDt));
                    eventStartingTime.setText(date46 + " at " + _12HourSDF.format(_24HourDt)+"-");
                    eventEndingTime.setText(date6+" at "+ _12HourSDF.format(_24HourDt1));
                } catch (Exception e) {
                    e.printStackTrace();
                }





                // eventFees.setText("Fees: Rs " + studentEventListArray.getJSONObject(position).getString("program_fees"));

                eventVenue.setText("Venue: " + teacherEventListArray.getJSONObject(position).getString("place"));
                seatsLeft.setText(teacherEventListArray.getJSONObject(position).getString("available_seats"));
                confirmed.setText(teacherEventListArray.getJSONObject(position).getString("confirmed"));
                //eventChoice.setText(studentEventListArray.getJSONObject(position).getString("choice"));
                //eventAudience.setText(studentEventListArray.getJSONObject(position).getString("group_name"));
                eventDetails.setText(teacherEventListArray.getJSONObject(position).getString("event_detail"));

                date7 = teacherEventListArray.getJSONObject(position).getString("fees_submission_date");
                try {
                    date9 = format.parse(date7);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date8 = format2.format(date9);




                eventFeesSubmisionDate.setText("Fees Submission Date: " + date8);


                if(date7.matches("") || date7.matches("null") || date7.matches("0000-00-00"))
                {
                    eventFeesSubmisionDate.setVisibility(View.GONE);
                }

                else
                {
                    eventFeesSubmisionDate.setVisibility(View.VISIBLE);
                }

                if(parent_event.matches(teacherEventListArray.getJSONObject(position).getString("event_id")))
                {
                    eventParentName.setVisibility(View.GONE);
                }
                else
                {
                    eventParentName.setText(program_name);
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator1").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator1").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator1").matches("0"))
                {
                    eventCoordinator1.setText("Coordinator1: Not mentioned");
                }
                else
                {
                    eventCoordinator1.setText("Coordinator1: "+teacherEventListArray.getJSONObject(position).getString("coordinator1"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator2").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator2").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator2").matches("0"))
                {
                    eventCoordinator2.setText("Coordinator2: Not mentioned");
                }
                else
                {
                    eventCoordinator2.setText("Coordinator2: "+teacherEventListArray.getJSONObject(position).getString("coordinator2"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator3").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator3").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator3").matches("0"))
                {
                    eventCoordinator3.setText("Coordinator3: Not mentioned");
                }
                else
                {
                    eventCoordinator3.setText("Coordinator3: "+teacherEventListArray.getJSONObject(position).getString("coordinator3"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator4").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator4").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator4").matches("0"))
                {
                    eventCoordinator4.setText("Coordinator4: Not mentioned");
                }
                else
                {
                    eventCoordinator4.setText("Coordinator4: "+teacherEventListArray.getJSONObject(position).getString("coordinator4"));
                }

                if(teacherEventListArray.getJSONObject(position).getString("coordinator5").matches("") || teacherEventListArray.getJSONObject(position).getString("coordinator5").matches("null") || teacherEventListArray.getJSONObject(position).getString("coordinator5").matches("0"))
                {
                    eventCoordinator5.setText("Coordinator5: Not mentioned");
                }
                else
                {
                    eventCoordinator5.setText("Coordinator5: "+teacherEventListArray.getJSONObject(position).getString("coordinator5"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    /*private void postMessage()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();


        //msg = comment.getText().toString();
        String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_EVENT_CONFIRMATION;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {

                        Utils.showToast(getApplicationContext(),"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Successfuly Submitted");


                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("event_id", eventId);
                params.put("stu_id",Preferences.getInstance().studentId);
                //params.put("feedback_id", strtext);
                //params.put("date", crrDate1);
                //params.put("reply", msg);
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
            //setSupportProgressBarIndeterminateVisibility(false);
        }
    }*/
}
