package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddPlannerEvent extends AppCompatActivity {

    private static final String TAG = "Sample";

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private SwitchDateTimeDialogFragment dateTimeFragment,dateTimeFragment1;

    private LinearLayout from,to;
    private TextView textViewFrom,textViewTo,addClass,add;
    private JSONArray sectionArray;
    private Button done;
    private ImageView back;

    Calendar calendar ;

    int year,month,day,hour,minute,second;

    public ArrayList<String> attendance = new ArrayList<String>();

    Switch holiday,allDay;

    String array1,array2,holiday1,allDay1;

    EditText title,location,notes;

    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
    String date1,date2,date5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Add Planner Event");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_add_planner_event);

        from = (LinearLayout) findViewById(R.id.linear_from);
        to = (LinearLayout) findViewById(R.id.linear_to);

        addClass = (TextView) findViewById(R.id.text_add_class);

        calendar = Calendar.getInstance();

        title = (EditText) findViewById(R.id.edit_title);

        location = (EditText) findViewById(R.id.edit_location);

        notes = (EditText) findViewById(R.id.edit_notes);

        add = (TextView) findViewById(R.id.txt_add);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

        holiday = (Switch) findViewById(R.id.mySwitch1);
        allDay = (Switch) findViewById(R.id.mySwitch);

        textViewFrom = (TextView) findViewById(R.id.text_from);
        textViewTo = (TextView) findViewById(R.id.text_to);

        Preferences.getInstance().loadPreference(getApplicationContext());

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {

                    Cache.Entry e;
                    e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CLASS_SECTION_LIST+"?sch_id="+ Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&device_id="+Preferences.getInstance().deviceId);
                    if(e == null)
                    {
                        sectionArray= null;
                        Utils.showToast(getApplicationContext(),"Please select classes");
                    }
                    else
                    {
                        sectionArray= new JSONArray(new String(e.data));
                    }


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

                if(sectionArray!= null)
                {



                    if(title.getText().toString().matches("") || title.getText().toString().matches("null"))
                    {
                        Utils.showToast(getApplicationContext(),"Please fill the title");
                    }

                    else
                        if(textViewFrom.getText().toString().matches("") || textViewTo.getText().toString().matches("") || textViewFrom.getText().toString().matches("null") || textViewTo.getText().toString().matches("null"))
                        {
                            Utils.showToast(getApplicationContext(),"Please fill the event dates");
                        }

                    else {
                        for (int i = 0; i < sectionArray.length(); i++) {
                            try {
                                if (sectionArray.getJSONObject(i).getString("isAdded").matches("A")) {
                                    attendance.add(sectionArray.getJSONObject(i).getString("class_section_id"));
                                    System.out.print(sectionArray.getJSONObject(i).getString("isAdded"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        array1 = attendance.toString();
                        array2 = array1.substring(1, array1.length() - 1);


                            Date date3=null;
                            Date date4 = null;

                        try {
                            date3 = formatter.parse(textViewFrom.getText().toString());
                            date4 = formatter.parse(textViewTo.getText().toString());


                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            Log.d("jjj","ll");
                            e.printStackTrace();
                        }

                        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date2 = formatter1.format(date3);
                            date5 = formatter1.format(date4);

                            Log.d("ll",date2);

                            if(date3.compareTo(date4)>0)
                            {
                                Utils.showToast(getApplicationContext(),"End date should be greater than start date");
                            }

                            else

                            {


                                postAttendance();
                            }


                    }

                }



                }
        });





       /* holiday.setFocusable(false);
        allDay.setFocusable(false);*/





        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPlannerEvent.this,SchoolPlannerClassSelectionList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });



        if (savedInstanceState != null) {
            // Restore value from saved state
            textViewFrom.setText(savedInstanceState.getCharSequence(STATE_TEXTVIEW));
            textViewTo.setText(savedInstanceState.getCharSequence(STATE_TEXTVIEW));
        }


        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }

        dateTimeFragment1 = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if(dateTimeFragment1 == null) {
            dateTimeFragment1 = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }
        // Assign values we want
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMMM yyyy HH:mm:ss", java.util.Locale.getDefault());

        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(true);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(year, month,day, hour, minute,second).getTime());
        // Or assign each element, default element is the current moment
        // dateTimeFragment.setDefaultHourOfDay(15);
        // dateTimeFragment.setDefaultMinute(20);
        // dateTimeFragment.setDefaultDay(4);
        // dateTimeFragment.setDefaultMonth(Calendar.MARCH);
        // dateTimeFragment.setDefaultYear(2017);

        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                textViewFrom.setText(myDateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                textViewFrom.setText("");
            }
        });

        dateTimeFragment1.startAtCalendarView();
        dateTimeFragment1.set24HoursMode(true);
        dateTimeFragment1.setMinimumDateTime(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime());
        dateTimeFragment1.setMaximumDateTime(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime());
        dateTimeFragment1.setDefaultDateTime(new GregorianCalendar(year, month,day, hour, minute,second).getTime());
        // Or assign each element, default element is the current moment
        // dateTimeFragment.setDefaultHourOfDay(15);
        // dateTimeFragment.setDefaultMinute(20);
        // dateTimeFragment.setDefaultDay(4);
        // dateTimeFragment.setDefaultMonth(Calendar.MARCH);
        // dateTimeFragment.setDefaultYear(2017);

        // Define new day and month format
        try {
            dateTimeFragment1.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        dateTimeFragment1.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                textViewTo.setText(myDateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                textViewTo.setText("");
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateTimeFragment1.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current textView
        savedInstanceState.putCharSequence(STATE_TEXTVIEW, textViewFrom.getText());
        savedInstanceState.putCharSequence(STATE_TEXTVIEW, textViewTo.getText());
        super.onSaveInstanceState(savedInstanceState);
    }


    protected void postAttendance()
    {
        //setSupportProgressBarIndeterminateVisibility(true);
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();




        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ADD_PLANNER/*+"?Students="+"{"+"\"Students\""+":"+teacherAttendanceDetailsListViewAdapter.teacherStudentAttendanceArray1.toString()+"}"*/;

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
                        Utils.showToast(getApplicationContext(),"Error Creating Event");

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
                        Utils.showToast(getApplicationContext(),"Successfully Created Event");
                        Intent intent = new Intent(AddPlannerEvent.this,SchoolPlannerMakingFirstScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

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

                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("section_id",array2.toString());
                //Log.d("sss",array2);
                params.put("from_date_time",date2);
                Log.d("kk",date2);
                params.put("to_date_time",date5);
                params.put("type","");
                params.put("name","");
                if(notes.getText().toString().matches("") || notes.getText().toString().matches("null"))
                {

                }

                else
                {
                    params.put("details",notes.getText().toString());
                }

                if(location.getText().toString().matches("") || location.getText().toString().matches("null"))
                {

                }

                else
                {
                    params.put("location",location.getText().toString());
                }

                params.put("title",title.getText().toString());

                if(holiday.isChecked())
                {
                    params.put("is_holiday","yes");
                }

                else
                {
                     params.put("is_holiday","no");
                }

                if(allDay.isChecked())
                {
                    params.put("is_allday","yes");
                }

                else
                {
                    params.put("is_allday","no");
                }

                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("user_id",Preferences.getInstance().userId);

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
