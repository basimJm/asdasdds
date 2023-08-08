package com.schoofi.activitiess;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

public class StudentEventListDetails extends AppCompatActivity {

    private JSONArray studentEventListArray;
    private TextView eventDate,eventMonth,eventTitle,eventStartingTime,eventEndingTime,eventVenue,eventFees,eventDetails,eventFeesSubmisionDate,eventChoice,eventAudience,confirmed,seatsLeft;
    private Button payNow;
    private TextView eventParentName,eventCoordinator1,eventCoordinator2,eventCoordinator3,eventCoordinator4,eventCoordinator5;
    private TextView eventStartingTime1;
    private ImageView eventGalleryIcon,eventMainImage,back,subEventsImageView;
    private int position;
    String crrDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String crrDate1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String combinedDate;
    String date,year,month,date1,date4,date5,date6,date7,date8;
    Date date2,date3,date9;
    String confirmationId,fees,eventId;
    String Rs;
    String image;
    ArrayList aList= new ArrayList();
    View view;
    LinearLayout linearLayout;
    ImageView addGallery;
    String value;
    String value4,parent_event,program_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Student EventList Details");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_student_event_list_details);

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
        value4 = getIntent().getStringExtra("value4");
        eventParentName = (TextView) findViewById(R.id.text_parent_event_name);
        eventCoordinator1 = (TextView) findViewById(R.id.text_coordinator1);
        eventCoordinator2 = (TextView) findViewById(R.id.text_coordinator2);
        eventCoordinator3 = (TextView) findViewById(R.id.text_coordinator3);
        eventCoordinator4 = (TextView) findViewById(R.id.text_coordinator4);
        eventCoordinator5 = (TextView) findViewById(R.id.text_coordinator5);

        back = (ImageView) findViewById(R.id.img_back);
        parent_event = getIntent().getStringExtra("parent_event");
        program_name = getIntent().getStringExtra("program_name");
        view = findViewById(R.id.view);
        linearLayout = (LinearLayout) findViewById(R.id.linear);

        addGallery = (ImageView) findViewById(R.id.addgallery);

        addGallery.setVisibility(View.INVISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Rs = getApplicationContext().getString(R.string.Rs);

        if(value4.matches("5"))
        {
            initData1();
        }
        else
        {
            initData();
        }

        //initData();




       eventGalleryIcon.setVisibility(View.VISIBLE);

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(payNow.getText().toString().matches("Pay Now")) {

                    Intent intent = new Intent(StudentEventListDetails.this, PaymentCharges.class);
                    intent.putExtra("fees",fees);
                    intent.putExtra("eventId",eventId);

                    startActivity(intent);
                    finish();
                }

                else
                    if(payNow.getText().toString().matches("See Album"))
                    {
                        aList = new ArrayList<String>(Arrays.asList(image.split(",")));
                        Intent intent = new Intent(getApplicationContext(),StudentFeedBackImages.class);
                        intent.putExtra("array", aList);
                        intent.putExtra("value",value);
                        intent.putExtra("position",position);
                        intent.putExtra("param","1");
                        startActivity(intent);
                    }

                    else
                        if(payNow.getText().toString().matches("No Album"))
                        {
                            Log.d("jjj","kkk");
                        }

                else
                {
                       postMessage();
                    payNow.setText("Confirmed");
                    payNow.setBackgroundResource(0);
                    payNow.setEnabled(false);
                    /*Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);*/

                }

            }
        });

        eventGalleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentEventListDetails.this,EventSharingScreen.class);
                intent.putExtra("position",position);
                intent.putExtra("startDate",date4);
                intent.putExtra("endDate",date6);
                intent.putExtra("value",value);
                intent.putExtra("param","1");
                startActivity(intent);
            }
        });




    }

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EVENT_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId+"&stu_id="+Preferences.getInstance().studentId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&value="+value);
            if(e == null)
            {
                studentEventListArray= null;

            }
            else
            {
                studentEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentEventListArray!= null)
            try {
               // Picasso.with(StudentEventListDetails.this).load(AppConstants.SERVER_URLS.SERVER_URL + studentEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(eventMainImage);
                Glide.with(StudentEventListDetails.this).load(AppConstants.SERVER_URLS.IMAGE_URL+studentEventListArray.getJSONObject(position).getString("event_image")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(eventMainImage);
                date = studentEventListArray.getJSONObject(position).getString("program_start_date");
                date1 = date.substring(8, 10);
                month = date.substring(5, 7);
                year = date.substring(0, 4);
                //System.out.println(date1 + month);

                eventDate.setText(date1);

                if(studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("null") || studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("") || studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("0"))
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
                            eventId = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("event_id");

                            Intent intent = new Intent(StudentEventListDetails.this, StudentSubEvents.class);
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
                eventId = studentEventListArray.getJSONObject(position).getString("event_id");
                fees = studentEventListArray.getJSONObject(position).getString("program_fees");
                confirmationId = studentEventListArray.getJSONObject(position).getString("confirm_id");
                image = studentEventListArray.getJSONObject(position).getString("images");




                eventTitle.setText(studentEventListArray.getJSONObject(position).getString("program_name"));
                Preferences.getInstance().productInfo = studentEventListArray.getJSONObject(position).getString("program_name")+"-"+"Event-Fees";
                Preferences.getInstance().savePreference(getApplicationContext());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date2 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat format2 = new SimpleDateFormat("EEEE d MMM yyyy");
                date4 = format2.format(date2);

                combinedDate = studentEventListArray.getJSONObject(position).getString("program_start_date") + " " + studentEventListArray.getJSONObject(position).getString("program_start_time");
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
                    if (diffDays < 0 && diffHours < 0 && diffMinutes < 0) {
                        eventStartingTime1.setText("Event finished");
                    } else {
                        eventStartingTime1.setText(String.valueOf(diffDays) + " days, " + String.valueOf(diffHours) + " hours to go");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fees.matches("") || fees.matches("null") || fees.matches("free") || fees.matches("Free"))
                {
                    eventFees.setText("Free");
                    if (confirmationId.matches("") || confirmationId.matches("null")) {
                        payNow.setText("Confirm");
                        eventFeesSubmisionDate.setVisibility(View.GONE);

                    } else {
                        payNow.setText("Confirmed");
                        payNow.setBackgroundResource(0);
                        payNow.setEnabled(false);
                        eventFeesSubmisionDate.setVisibility(View.GONE);

                    }





                } else
                       if((!fees.matches("") || !fees.matches("null") || !fees.matches("free") || !fees.matches("Free"))){
                           if (confirmationId.matches("") || confirmationId.matches("null")) {
                               eventFees.setText("Fees: " + Rs + studentEventListArray.getJSONObject(position).getString("program_fees"));
                               payNow.setText("Pay Now");

                           }
                           else
                           {
                               payNow.setText("Confirmed");
                               payNow.setBackgroundResource(0);
                               payNow.setEnabled(false);
                               eventFees.setText("Fees: " + Rs + studentEventListArray.getJSONObject(position).getString("program_fees"));
                               eventFeesSubmisionDate.setVisibility(View.GONE);
                               view.setVisibility(View.GONE);
                               linearLayout.setVisibility(View.GONE);
                           }


                }




                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date67=null;
                Date date68 =null;
                try {
                    try {
                        date67  = inFormat.parse(studentEventListArray.getJSONObject(position).getString("program_start_date"));
                        date68 = inFormat.parse(studentEventListArray.getJSONObject(position).getString("program_end_date"));
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

                date5 = studentEventListArray.getJSONObject(position).getString("program_end_date");
                date56 = studentEventListArray.getJSONObject(position).getString("program_start_date");
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
                    String _24HourTime = studentEventListArray.getJSONObject(position).getString("program_start_time");
                    String _24HourTime1 = studentEventListArray.getJSONObject(position).getString("program_end_time");
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

                eventVenue.setText("Venue: " + studentEventListArray.getJSONObject(position).getString("place"));
                seatsLeft.setText(studentEventListArray.getJSONObject(position).getString("available_seats"));
                confirmed.setText(studentEventListArray.getJSONObject(position).getString("confirmed"));
                //eventChoice.setText(studentEventListArray.getJSONObject(position).getString("choice"));
                //eventAudience.setText(studentEventListArray.getJSONObject(position).getString("group_name"));
                eventDetails.setText(studentEventListArray.getJSONObject(position).getString("event_detail"));

                date7 = studentEventListArray.getJSONObject(position).getString("fees_submission_date");
                try {
                    date9 = format.parse(date7);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date8 = format2.format(date9);



                if(eventStartingTime1.getText().toString().matches("Event finished"))
                {
                    if(image.matches("") ||  image.matches("null"))
                    {
                        payNow.setText("No album");
                        eventFeesSubmisionDate.setText("Event Finished");
                    }

                    else
                    {
                        payNow.setText("See Album");
                        eventFeesSubmisionDate.setText("Fees Submission Date: " + date8);
                    }
                }

                if(date7.matches("") || date7.matches("null") || date7.matches("0000-00-00"))
                {
                    eventFeesSubmisionDate.setVisibility(View.GONE);
                }

                else
                {
                    eventFeesSubmisionDate.setVisibility(View.VISIBLE);
                }

                if(parent_event.matches(studentEventListArray.getJSONObject(position).getString("event_id")))
                {
                    eventParentName.setVisibility(View.GONE);
                }
                else
                {
                    eventParentName.setText(program_name);
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator1").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator1").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator1").matches("0"))
                {
                    eventCoordinator1.setText("Coordinator1: Not mentioned");
                }
                else
                {
                    eventCoordinator1.setText("Coordinator1: "+studentEventListArray.getJSONObject(position).getString("coordinator1"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator2").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator2").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator2").matches("0"))
                {
                    eventCoordinator2.setText("Coordinator2: Not mentioned");
                }
                else
                {
                    eventCoordinator2.setText("Coordinator2: "+studentEventListArray.getJSONObject(position).getString("coordinator2"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator3").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator3").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator3").matches("0"))
                {
                    eventCoordinator3.setText("Coordinator3: Not mentioned");
                }
                else
                {
                    eventCoordinator3.setText("Coordinator3: "+studentEventListArray.getJSONObject(position).getString("coordinator3"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator4").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator4").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator4").matches("0"))
                {
                    eventCoordinator4.setText("Coordinator4: Not mentioned");
                }
                else
                {
                    eventCoordinator4.setText("Coordinator4: "+studentEventListArray.getJSONObject(position).getString("coordinator4"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator5").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator5").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator5").matches("0"))
                {
                    eventCoordinator5.setText("Coordinator5: Not mentioned");
                }
                else
                {
                    eventCoordinator5.setText("Coordinator5: "+studentEventListArray.getJSONObject(position).getString("coordinator5"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void postMessage()
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

                        Utils.showToast(getApplicationContext(),"Successfully Submitted");


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
    }


    private void initData1()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SUB_EVENT_LIST+"?event_id="+parent_event+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            if(e == null)
            {
                studentEventListArray= null;

            }
            else
            {
                studentEventListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentEventListArray!= null)
            try {
                // Picasso.with(StudentEventListDetails.this).load(AppConstants.SERVER_URLS.SERVER_URL + studentEventListArray.getJSONObject(position).getString("event_image")).placeholder(R.drawable.profilebig).error(R.drawable.profilebig).into(eventMainImage);
                Glide.with(StudentEventListDetails.this).load(AppConstants.SERVER_URLS.IMAGE_URL+studentEventListArray.getJSONObject(position).getString("event_image")).crossFade().placeholder(R.drawable.imagenotavailble).error(R.drawable.imagenotavailble).into(eventMainImage);
                date = studentEventListArray.getJSONObject(position).getString("program_start_date");
                date1 = date.substring(8, 10);
                month = date.substring(5, 7);
                year = date.substring(0, 4);
                //System.out.println(date1 + month);

                eventDate.setText(date1);

                if(studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("null") || studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("") || studentEventListArray.getJSONObject(position).getString("sub_event_count").matches("0"))
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
                            eventId = studentEventListArray.getJSONObject((Integer) v.getTag()).getString("event_id");

                            Intent intent = new Intent(StudentEventListDetails.this, StudentSubEvents.class);
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
                eventId = studentEventListArray.getJSONObject(position).getString("event_id");
                fees = studentEventListArray.getJSONObject(position).getString("program_fees");
                confirmationId = studentEventListArray.getJSONObject(position).getString("confirm_id");
                image = studentEventListArray.getJSONObject(position).getString("images");




                eventTitle.setText(studentEventListArray.getJSONObject(position).getString("program_name"));
                Preferences.getInstance().productInfo = studentEventListArray.getJSONObject(position).getString("program_name")+"-"+"Event-Fees";
                Preferences.getInstance().savePreference(getApplicationContext());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date2 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DateFormat format2 = new SimpleDateFormat("EEEE d MMM yyyy");
                date4 = format2.format(date2);

                combinedDate = studentEventListArray.getJSONObject(position).getString("program_start_date") + " " + studentEventListArray.getJSONObject(position).getString("program_start_time");
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
                    if (diffDays < 0 && diffHours < 0 && diffMinutes < 0) {
                        eventStartingTime1.setText("Event finished");
                    } else {
                        eventStartingTime1.setText(String.valueOf(diffDays) + " days, " + String.valueOf(diffHours) + " hours to go");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fees.matches("") || fees.matches("null") || fees.matches("free") || fees.matches("Free"))
                {
                    eventFees.setText("Free");
                    if (confirmationId.matches("") || confirmationId.matches("null")) {
                        payNow.setText("Confirm");
                        eventFeesSubmisionDate.setVisibility(View.GONE);

                    } else {
                        payNow.setText("Confirmed");
                        payNow.setBackgroundResource(0);
                        payNow.setEnabled(false);
                        eventFeesSubmisionDate.setVisibility(View.GONE);

                    }





                } else
                if((!fees.matches("") || !fees.matches("null") || !fees.matches("free") || !fees.matches("Free"))){
                    if (confirmationId.matches("") || confirmationId.matches("null")) {
                        eventFees.setText("Fees: " + Rs + studentEventListArray.getJSONObject(position).getString("program_fees"));
                        payNow.setText("Pay Now");

                    }
                    else
                    {
                        payNow.setText("Confirmed");
                        payNow.setBackgroundResource(0);
                        payNow.setEnabled(false);
                        eventFees.setText("Fees: " + Rs + studentEventListArray.getJSONObject(position).getString("program_fees"));
                        eventFeesSubmisionDate.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                    }


                }




                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date67=null;
                Date date68 =null;
                try {
                    try {
                        date67  = inFormat.parse(studentEventListArray.getJSONObject(position).getString("program_start_date"));
                        date68 = inFormat.parse(studentEventListArray.getJSONObject(position).getString("program_end_date"));
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

                date5 = studentEventListArray.getJSONObject(position).getString("program_end_date");
                date56 = studentEventListArray.getJSONObject(position).getString("program_start_date");
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
                    String _24HourTime = studentEventListArray.getJSONObject(position).getString("program_start_time");
                    String _24HourTime1 = studentEventListArray.getJSONObject(position).getString("program_end_time");
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

                eventVenue.setText("Venue: " + studentEventListArray.getJSONObject(position).getString("place"));
                seatsLeft.setText(studentEventListArray.getJSONObject(position).getString("available_seats"));
                confirmed.setText(studentEventListArray.getJSONObject(position).getString("confirmed"));
                //eventChoice.setText(studentEventListArray.getJSONObject(position).getString("choice"));
                //eventAudience.setText(studentEventListArray.getJSONObject(position).getString("group_name"));
                eventDetails.setText(studentEventListArray.getJSONObject(position).getString("event_detail"));

                date7 = studentEventListArray.getJSONObject(position).getString("fees_submission_date");
                try {
                    date9 = format.parse(date7);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                date8 = format2.format(date9);



                if(eventStartingTime1.getText().toString().matches("Event finished"))
                {
                    if(image.matches("") ||  image.matches("null"))
                    {
                        payNow.setText("No album");
                        eventFeesSubmisionDate.setText("Event Finished");
                    }

                    else
                    {
                        payNow.setText("See Album");
                        eventFeesSubmisionDate.setText("Fees Submission Date: " + date8);
                    }
                }

                if(date7.matches("") || date7.matches("null") || date7.matches("0000-00-00"))
                {
                    eventFeesSubmisionDate.setVisibility(View.GONE);
                }

                else
                {
                    eventFeesSubmisionDate.setVisibility(View.VISIBLE);
                }

                if(parent_event.matches(studentEventListArray.getJSONObject(position).getString("event_id")))
                {
                    eventParentName.setVisibility(View.GONE);
                }
                else
                {
                    eventParentName.setText(program_name);
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator1").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator1").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator1").matches("0"))
                {
                    eventCoordinator1.setText("Coordinator1: Not mentioned");
                }
                else
                {
                    eventCoordinator1.setText("Coordinator1: "+studentEventListArray.getJSONObject(position).getString("coordinator1"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator2").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator2").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator2").matches("0"))
                {
                    eventCoordinator2.setText("Coordinator2: Not mentioned");
                }
                else
                {
                    eventCoordinator2.setText("Coordinator2: "+studentEventListArray.getJSONObject(position).getString("coordinator2"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator3").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator3").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator3").matches("0"))
                {
                    eventCoordinator3.setText("Coordinator3: Not mentioned");
                }
                else
                {
                    eventCoordinator3.setText("Coordinator3: "+studentEventListArray.getJSONObject(position).getString("coordinator3"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator4").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator4").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator4").matches("0"))
                {
                    eventCoordinator4.setText("Coordinator4: Not mentioned");
                }
                else
                {
                    eventCoordinator4.setText("Coordinator4: "+studentEventListArray.getJSONObject(position).getString("coordinator4"));
                }

                if(studentEventListArray.getJSONObject(position).getString("coordinator5").matches("") || studentEventListArray.getJSONObject(position).getString("coordinator5").matches("null") || studentEventListArray.getJSONObject(position).getString("coordinator5").matches("0"))
                {
                    eventCoordinator5.setText("Coordinator5: Not mentioned");
                }
                else
                {
                    eventCoordinator5.setText("Coordinator5: "+studentEventListArray.getJSONObject(position).getString("coordinator5"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


}
