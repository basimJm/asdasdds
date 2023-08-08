package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.SchoolPlannerClassSectionListAdapter;
import com.schoofi.adapters.SchoolPlannerEventDetailClassListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SchoolPlannerEventDetail extends AppCompatActivity {

    TextView eventTitle,eventLocation,eventDescription,eventFromDate,eventToDate,editEvent;
    Button deleteEvent;
    ListView schoolPlannerClassListView;
    ImageView back;

    String title,location,description,startDate,endDate,plannerId,isAllday,isHoliday,classList,sectionId;
    ArrayList aList= new ArrayList();
    SchoolPlannerEventDetailClassListAdapter schoolPlannerEventDetailClassListAdapter;

    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
    final String date1="";
    String date2="";
    String date56="";
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    SimpleDateFormat myDateFormat1 = new SimpleDateFormat("d MMMM yyyy HH:mm:ss", java.util.Locale.getDefault());
    TextView holidayTextView;
    LinearLayout linearApplicable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("School Planner Event Detail");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_school_planner_event_detail);


        back = (ImageView) findViewById(R.id.img_back);

        title = getIntent().getStringExtra("title");
        location = getIntent().getStringExtra("location");
        description = getIntent().getStringExtra("description");
        startDate = getIntent().getStringExtra("fromDate");
        endDate = getIntent().getStringExtra("toDate");
        plannerId = getIntent().getStringExtra("plannerId");
        classList = getIntent().getStringExtra("classList");
        isAllday = getIntent().getStringExtra("isAllday");
        isHoliday = getIntent().getStringExtra("isHoliday");
        sectionId = getIntent().getStringExtra("sectionId");



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        schoolPlannerClassListView = (ListView) findViewById(R.id.listview);
        linearApplicable = (LinearLayout) findViewById(R.id.linear_appliacable);

        eventTitle = (TextView) findViewById(R.id.text_title);
        eventLocation = (TextView) findViewById(R.id.text_location);
        eventDescription = (TextView) findViewById(R.id.text_description);
        eventFromDate = (TextView) findViewById(R.id.text_from);
        eventToDate = (TextView) findViewById(R.id.text_to);
        editEvent = (TextView) findViewById(R.id.txt_edit);
        holidayTextView = (TextView) findViewById(R.id.text_holiday);


        Date date3 = null;

        Date date4=null;

        Date date5=null;

        Date date6 = null;

        try {
            date3 = sdf.parse(startDate);
            date5 = sdf.parse(endDate);

        } catch (ParseException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }

        String date11 = "";
        String date12="";

        date11 = myDateFormat1.format(date3);
        date12 = myDateFormat1.format(date5);

        if(isAllday.matches("yes"))
        {
            eventFromDate.setText("All Day from: "+date11+ " to");
            eventToDate.setText(date12);
        }

        else
        {
            eventFromDate.setText("From: "+date11+ " to");
            eventToDate.setText(date12);
        }

        if(isHoliday.matches("yes"))
        {
            holidayTextView.setText("Holiday");
            holidayTextView.setVisibility(View.VISIBLE);
        }

        else
        {
            holidayTextView.setVisibility(View.GONE);
        }





        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  intent = new Intent(SchoolPlannerEventDetail.this,SchoolPlannerEditScreen.class);
                intent.putExtra("title",title);
                intent.putExtra("location",location);
                intent.putExtra("description",description);
                intent.putExtra("fromDate",startDate);
                intent.putExtra("toDate",endDate);
                intent.putExtra("plannerId",plannerId);
                intent.putExtra("classList",classList);
                intent.putExtra("isAllDay",isAllday);
                intent.putExtra("isHoliday",isHoliday);
                intent.putExtra("sectionId",sectionId);
                startActivity(intent);


            }
        });

        deleteEvent = (Button) findViewById(R.id.btn_delete_event);

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAttendance();
            }
        });


        Preferences.getInstance().loadPreference(getApplicationContext());

        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6") || Preferences.getInstance().userRoleId.matches("4"))
        {
            editEvent.setVisibility(View.INVISIBLE);
            deleteEvent.setVisibility(View.INVISIBLE);
            schoolPlannerClassListView.setVisibility(View.INVISIBLE);
            linearApplicable.setVisibility(View.INVISIBLE);
        }

        else
        {
            editEvent.setVisibility(View.VISIBLE);
            deleteEvent.setVisibility(View.VISIBLE);
            linearApplicable.setVisibility(View.VISIBLE);
        }



        eventTitle.setText(title);
        eventLocation.setText(location);
        eventDescription.setText(description);

        if(classList.matches("") || classList.matches("null"))
        {
            schoolPlannerClassListView.setVisibility(View.INVISIBLE);
        }

        else {

            if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6") )
            {
                editEvent.setVisibility(View.INVISIBLE);
                deleteEvent.setVisibility(View.INVISIBLE);
                schoolPlannerClassListView.setVisibility(View.INVISIBLE);
                linearApplicable.setVisibility(View.INVISIBLE);
            }

            else {


                aList = new ArrayList<String>(Arrays.asList(classList.split(",")));
                schoolPlannerClassListView.setVisibility(View.VISIBLE);
                linearApplicable.setVisibility(View.VISIBLE);
                schoolPlannerEventDetailClassListAdapter = new SchoolPlannerEventDetailClassListAdapter(SchoolPlannerEventDetail.this, aList);
                schoolPlannerClassListView.setAdapter(schoolPlannerEventDetailClassListAdapter);

            }

            //Utils.showToast(getApplicationContext(),aList.toString());


        }

       if(eventLocation.getText().toString().matches("") || eventDescription.getText().toString().matches("null"))
       {
           eventLocation.setVisibility(View.GONE);
       }

       else
       {
           eventLocation.setVisibility(View.VISIBLE);
       }

       if(eventDescription.getText().toString().matches("") || eventDescription.getText().toString().matches("null"))
       {
           eventDescription.setVisibility(View.GONE);
       }

       else
           {
               eventDescription.setVisibility(View.VISIBLE);
       }



    }


    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.DELETE_EVENT/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                //System.out.println(url1);
                try
                {
                    responseObject = new JSONObject(response);

                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Error Submitting Comment");

                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    }

                    else
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {
                        loading.dismiss();
                        Utils.showToast(getApplicationContext(),"Successfuly Deleted Event");
                        final Intent intent = new Intent(SchoolPlannerEventDetail.this,SchoolPlannerMakingFirstScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    loading.dismiss();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                //setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String,String> params = new HashMap<String, String>();
                //params.put("Students", "{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}");
                //params.put("Students", jsonObject1.toString());
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("planner_id",plannerId);
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
}
