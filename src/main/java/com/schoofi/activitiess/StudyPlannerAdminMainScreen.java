package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.EventModel;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.SchoolPlannerListParsingMainVO;
import com.schoofi.utils.SchoolPlannerListParsingVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.github.memfis19.cadar.CalendarController;
import io.github.memfis19.cadar.data.entity.Event;
import io.github.memfis19.cadar.event.CalendarPrepareCallback;
import io.github.memfis19.cadar.event.DisplayEventCallback;
import io.github.memfis19.cadar.event.OnDayChangeListener;
import io.github.memfis19.cadar.event.OnEventClickListener;
import io.github.memfis19.cadar.event.OnMonthChangeListener;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.EventDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.MonthDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.WeekDecorator;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.EventDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.MonthDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.decorator.factory.WeekDecoratorFactory;
import io.github.memfis19.cadar.internal.ui.list.adapter.model.ListItemModel;
import io.github.memfis19.cadar.internal.utils.DateUtils;
import io.github.memfis19.cadar.settings.ListCalendarConfiguration1;
import io.github.memfis19.cadar.settings.ListCalendarConfiguration;
import io.github.memfis19.cadar.settings.MonthCalendarConfiguration;
import io.github.memfis19.cadar.view.ListCalendar;
import io.github.memfis19.cadar.view.MonthCalendar;

import static com.schoofi.activitiess.R.id.fab;
import static com.schoofi.activitiess.R.id.textView1;
import static com.schoofi.activitiess.R.id.textView2;

public class StudyPlannerAdminMainScreen extends AppCompatActivity implements CalendarPrepareCallback {

    private MonthCalendar monthCalendar;
    private ListCalendar listCalendar;


    private List<Event> events = new ArrayList<>();
    Event event;
    private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
    private ArrayList<SchoolPlannerListParsingMainVO> schoolPlannerListParsingMainVOs;
    private ArrayList<SchoolPlannerListParsingVO> schoolPlannerListParsingVOs;
    private JSONArray schoolPlannerListArray;
    JSONArray jsonArray;
    JSONObject jsonObject,jsonObject1;

    FloatingActionButton fab;
    ImageView back;
    String value;
    int k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Study Planner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("School Planner Making First Screen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.school_planner);



        postLocalRegistration();



        monthCalendar = (MonthCalendar) findViewById(R.id.month_calendar);
        listCalendar = (ListCalendar) findViewById(R.id.listCalendar);





        EventDecoratorFactory eventDecoratorFactory = new EventDecoratorFactory() {
            @Override
            public EventDecorator createEventDecorator(View parent) {
                return new EventDecoratorImpl(parent);
            }
        };

        WeekDecoratorFactory weekDecoratorFactory = new WeekDecoratorFactory() {
            @Override
            public WeekDecorator createWeekDecorator(View parent) {
                return new WeeDecoratorImpl(parent);
            }
        };

        MonthDecoratorFactory monthDecoratorFactory = new MonthDecoratorFactory() {
            @Override
            public MonthDecorator createMonthDecorator(View parent) {
                return new MonthDecoratorImpl(parent);
            }
        };

        Preferences.getInstance().loadPreference(getApplicationContext());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.getInstance().loadPreference(getApplicationContext());
                //Log.d("op",Preferences.getInstance().schoolType);

            }
        });


        if(Preferences.getInstance().userRoleId.matches("5") || Preferences.getInstance().userRoleId.matches("6") || Preferences.getInstance().userRoleId.matches("4"))
        {
            fab.setVisibility(View.INVISIBLE);
        }

        else
        {
            fab.setVisibility(View.VISIBLE);
        }







        /*listBuilder.setEventLayout(R.layout.custom_event_layout, eventDecoratorFactory);
        listBuilder.setWeekLayout(R.layout.custom_week_title_layout, weekDecoratorFactory);
        listBuilder.setMonthLayout(R.layout.custom_month_calendar_event_layout, monthDecoratorFactory);*/

        monthCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                listCalendar.setSelectedDay(DateUtils.setTimeToMidnight((Calendar) calendar.clone()), false);
                Log.d("ll", String.valueOf(calendar.getTime()));

                /*Intent intent = new Intent(SchoolPlannerMakingFirstScreen.this,AddPlannerEvent.class);
                startActivity(intent);*/
            }
        });
        monthCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                listCalendar.setSelectedDay(DateUtils.setTimeToMonthStart((Calendar) calendar.clone()), false);


            }
        });

        listCalendar.setOnDayChangeListener(new OnDayChangeListener() {
            @Override
            public void onDayChanged(Calendar calendar) {
                monthCalendar.setSelectedDay(calendar, false);
            }
        });

        listCalendar.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChanged(Calendar calendar) {
                monthCalendar.setSelectedDay(calendar, true);
            }
        });

        listCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onEventClick(Event event, int position) {
                Log.i("onEventClick", event.getEventTitle());
                Log.d("position",String.valueOf(position));
                Intent intent = new Intent(StudyPlannerAdminMainScreen.this,StudyPlannerDetailScreen.class);

                intent.putExtra("attachment",event.getPlannerID());
                intent.putExtra("position",position);
                intent.putExtra("title",event.getEventTitle());
                intent.putExtra("description",event.getEventDescription());
                intent.putExtra("location",event.getLocation());
                intent.putExtra("fromDate",event.getEventStartDate().toString());
                intent.putExtra("toDate",event.getEventEndDate().toString());
                intent.putExtra("references",event.getSectionId());
                intent.putExtra("subject",event.getClassName());


                startActivity(intent);
            }

            @Override
            public void onSyncClick(Event event, int position) {
                Log.i("onSyncClick", String.valueOf(event));
            }
        });
    }

    @Override
    public void onCalendarReady(CalendarController calendar) {
        if (calendar == monthCalendar) {
            monthCalendar.displayEvents(events, new DisplayEventCallback() {
                @Override
                public void onEventsDisplayed() {

                }
            });
        } else if (calendar == listCalendar) {
            listCalendar.displayEvents(events, new DisplayEventCallback() {
                @Override
                public void onEventsDisplayed() {

                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(k==1) {

            monthCalendar.releaseCalendar();
            listCalendar.releaseCalendar();
        }

        else {

        }
    }


    protected void postLocalRegistration()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        Preferences.getInstance().loadPreference(getApplicationContext());

        if(Preferences.getInstance().userRoleId.matches("3"))
        {
            value = "2";
        }

        else
        {
            value="1";
        }


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDY_PLANNER_LIST ;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                System.out.println(url1);


                try {
                    responseObject = new JSONObject(response);
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        loading.dismiss();
                    }
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                        loading.dismiss();
                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();

                        schoolPlannerListArray = new JSONObject(response).getJSONArray("responseObject");
                        // profileUrl = schoolDiaryArray.getJSONObject(0).getString("school_banner");



                        if(null!=schoolPlannerListArray && schoolPlannerListArray.length()>=0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = schoolPlannerListArray.toString().getBytes();
                            schoolPlannerListParsingMainVOs = new ArrayList<SchoolPlannerListParsingMainVO>();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.PLANNER_LIST + "?token=" + Preferences.getInstance().token + "&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&subject_id="+Preferences.getInstance().studentSubjectId, e);



                            for(int i=0;i<schoolPlannerListArray.length();i++)
                            {
                                SchoolPlannerListParsingMainVO schoolPlannerListParsingMainVO = new SchoolPlannerListParsingMainVO();
                                schoolPlannerListParsingMainVO.setDate(schoolPlannerListArray.getJSONObject(i).getString("date_scope"));
                                jsonObject = schoolPlannerListArray.getJSONObject(i);
                                jsonArray = jsonObject.getJSONArray("bifurcation");

                                schoolPlannerListParsingVOs= new ArrayList<SchoolPlannerListParsingVO>();
                                //Utils.showToast(getApplicationContext(),jsonArray.toString());
                                Log.d("fff",jsonArray.toString());

                                for(int j=0;j<jsonArray.length();j++)
                                {
                                    jsonObject1 = jsonArray.getJSONObject(j);
                                    SchoolPlannerListParsingVO schoolPlannerListParsingVO = new SchoolPlannerListParsingVO();
                                    schoolPlannerListParsingVO.setAllDay("no");
                                    schoolPlannerListParsingVO.setHoliday("no");
                                    schoolPlannerListParsingVO.setStartDate(jsonObject1.getString("from_date"));
                                    schoolPlannerListParsingVO.setEndDate(jsonObject1.getString("to_date"));
                                    schoolPlannerListParsingVO.setStartTime("00:00:00");
                                    schoolPlannerListParsingVO.setEndTime("00:00:00");

                                    schoolPlannerListParsingVO.setTitle(jsonObject1.getString("title"));
                                    schoolPlannerListParsingVO.setLocation(jsonObject1.getString("location"));
                                    schoolPlannerListParsingVO.setDetails(jsonObject1.getString("notes"));
                                    schoolPlannerListParsingVO.setPlannerId(jsonObject1.getString("attachment"));
                                    schoolPlannerListParsingVO.setClassName(jsonObject1.getString("subject_name"));
                                    schoolPlannerListParsingVO.setSectionId(jsonObject1.getString("references"));

                                    String demo = schoolPlannerListParsingMainVO.getDate()+" "+schoolPlannerListParsingVO.getStartTime();
                                    String demo1 = schoolPlannerListParsingVO.getEndDate()+" "+schoolPlannerListParsingVO.getEndTime();
                                    String demo2 = schoolPlannerListParsingVO.getStartDate()+" "+schoolPlannerListParsingVO.getStartTime();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    Date date3 = null;

                                    Date date4=null;

                                    Date date5=null;

                                    Date date6 = null;
                                    Date date9=null;
                                    Date date10=null;

                                    try {
                                        date3 = formatter.parse(demo);
                                        date5 = formatter.parse(demo1);
                                        date9 = formatter.parse(demo2);
                                    } catch (ParseException e3) {
                                        // TODO Auto-generated catch block
                                        e3.printStackTrace();
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                                    String date2="";
                                    String date7="";
                                    String date8="";
                                    date2 = sdf.format(date3);
                                    date7 = sdf.format(date5);
                                    date8 = sdf.format(date9);

                                    try {
                                        date4 = sdf.parse(date2);
                                        date6 = sdf.parse(date7);
                                        date10 = sdf.parse(date8);
                                    } catch (ParseException e3) {
                                        // TODO Auto-generated catch block
                                        e3.printStackTrace();
                                    }

                                    Boolean allDay;

                                    if(schoolPlannerListParsingVO.getAllDay().matches("yes"))
                                    {
                                        allDay = true;
                                    }

                                    else
                                    {
                                        allDay=false;
                                    }

                                    events.add(new EventModel(schoolPlannerListParsingVO.getTitle(),schoolPlannerListParsingVO.getLocation(),date4,date6,date10,schoolPlannerListParsingVO.getPlannerId(),schoolPlannerListParsingVO.getClassName(),schoolPlannerListParsingVO.getDetails(),allDay,schoolPlannerListParsingVO.getHoliday(),schoolPlannerListParsingVO.getSectionId(),schoolPlannerListParsingVO.getAllDay()));
                                    // events.add(new EventModel());




                                    //Log.d("kkk",date2);



                                    schoolPlannerListParsingVOs.add(schoolPlannerListParsingVO);
                                    // Utils.showToast(getApplicationContext(),"kkk");

                                }

                                schoolPlannerListParsingMainVO.setItems(schoolPlannerListParsingVOs);
                                schoolPlannerListParsingMainVOs.add(schoolPlannerListParsingMainVO);



                                //Date originalStartDate = new Date();

                                // Log.d("date", String.valueOf(originalStartDate));


                            }

                            k=1;

                            MonthCalendarConfiguration.Builder builder = new MonthCalendarConfiguration.Builder(StudyPlannerAdminMainScreen.this);
                           // ListCalendarConfiguration1.Builder listBuilder = new ListCalendarConfiguration1.Builder(StudyPlannerAdminMainScreen.this);
                            ListCalendarConfiguration.Builder listBuilder = new ListCalendarConfiguration.Builder(StudyPlannerAdminMainScreen.this);

                            monthCalendar.setCalendarPrepareCallback(StudyPlannerAdminMainScreen.this);
                            listCalendar.setCalendarPrepareCallback(StudyPlannerAdminMainScreen.this);

                            monthCalendar.prepareCalendar(builder.build());
                           listCalendar.prepareCalendar(listBuilder.build());


                        }
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong!!!", Toast.LENGTH_LONG).show();
                    setProgressBarIndeterminateVisibility(false);
                    loading.dismiss();
                }


                // mParties = new String[]
                // dynamicToolbarColor();


                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

               // params.put("value",value);
                params.put("token",Preferences.getInstance().token);
                params.put("device_id",Preferences.getInstance().deviceId);
                params.put("ins_id",Preferences.getInstance().institutionId);
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("cls_id",Preferences.getInstance().studentClassId);
                params.put("section",Preferences.getInstance().studentSectionId);
                params.put("subject_id",Preferences.getInstance().studentSubjectId);

                Log.d("token",Preferences.getInstance().token);
                Log.d("device_id",Preferences.getInstance().deviceId);
                Log.d("ins_id",Preferences.getInstance().institutionId);
                Log.d("sch_id",Preferences.getInstance().schoolId);
                Log.d("cls_id",Preferences.getInstance().studentClassId);
                Log.d("section",Preferences.getInstance().studentSectionId);
                Log.d("subject_id",Preferences.getInstance().studentSubjectId);
                /*if(diaryModuleId.matches("") || diaryModuleId.matches("null"))
                {
                    params.put("diary_module_id","A");
                }
                else {
                    params.put("diary_module_id", diaryModuleId);
                }

                if(subjectId.matches("") || subjectId.matches("null"))
                {
                    params.put("subject_id","A");
                }
                else {
                    params.put("subject_id", subjectId);
                }

                if(fromDate.matches("") || fromDate.matches("null"))
                {
                    params.put("from_date","");
                }

                else {
                    params.put("from_date", fromDate);
                }

                if(toDate.matches("") || toDate.matches("null"))
                {
                    params.put("to_date","");
                }

                else {
                    params.put("to_date", toDate);
                }

                if(teacherId.matches("") || teacherId.matches("null"))
                {
                    params.put("teacher_id",teacherId);
                }

                else {
                    params.put("teacher_id", teacherId);
                }*/
                //System.out.print(Preferences.getInstance().userId);




                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }

    private class EventDecoratorImpl implements EventDecorator {

        private TextView day,month,title,location;

        public EventDecoratorImpl(View parent) {
            day = (TextView) parent.findViewById(R.id.text_date);
            month = (TextView) parent.findViewById(R.id.text_month);
            title = (TextView) parent.findViewById(R.id.textView_student_announcement_title);
            location = (TextView) parent.findViewById(R.id.textView_student_announcement_person);

        }

        @Override
        public void onBindEventView(View view, Event event, ListItemModel previous, int position) {
            view.setBackgroundColor(ContextCompat.getColor(StudyPlannerAdminMainScreen.this, R.color.eventBackground));
            day.setText("kjh");
            month.setText("kkk");
            title.setText("mmm");
            location.setText("llk");

        }
    }

    private class MonthDecoratorImpl implements MonthDecorator {

        private ImageView monthBackground;
        private TextView monthTitle;
        private Custom custom;

        public MonthDecoratorImpl(View parent) {
            monthBackground = (ImageView) parent.findViewById(R.id.month_background);
            monthTitle = (TextView) parent.findViewById(R.id.month_label);
        }

        @Override
        public void onBindMonthView(View view, Calendar month) {
            monthBackground.setImageDrawable(null);

            final int backgroundId = getBackgroundId(month.get(Calendar.MONTH));
            monthTitle.setText(month.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
            Picasso.with(monthTitle.getContext().getApplicationContext()).load(backgroundId).into(monthBackground, new Callback() {
                @Override
                public void onSuccess() {
                    if (Build.VERSION.SDK_INT > 13) {
                        monthBackground.setScrollX(0);
                        monthBackground.setScrollY(0);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.OnScrollListener getScrollListener() {
            custom = new Custom();
            return custom;
        }
    }

    private class WeeDecoratorImpl implements WeekDecorator {

        private TextView title;

        public WeeDecoratorImpl(View parent) {
            title = (TextView) parent.findViewById(io.github.memfis19.cadar.R.id.week_title);
        }

        @Override
        public void onBindWeekView(View view, Pair<Calendar, Calendar> period) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(title.getContext().getString(io.github.memfis19.cadar.R.string.calendar_week));
            stringBuilder.append("custom ");
            stringBuilder.append(period.first.get(Calendar.WEEK_OF_YEAR));
            stringBuilder.append(", ");
            stringBuilder.append(DateFormat.format("dd MMM", period.first));
            stringBuilder.append(" - ");
            stringBuilder.append(DateFormat.format("dd MMM", period.second));

            final Spannable date = new SpannableString(stringBuilder.toString());

            title.setText(date);
        }
    }

    private int getBackgroundId(int month) {
        int backgroundId = io.github.memfis19.cadar.R.drawable.bkg_12_december;

        if (month == Calendar.JANUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_01_january;
        } else if (month == Calendar.FEBRUARY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_02_february;
        } else if (month == Calendar.MARCH) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_03_march;
        } else if (month == Calendar.APRIL) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_04_april;
        } else if (month == Calendar.MAY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_05_may;
        } else if (month == Calendar.JUNE) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_06_june;
        } else if (month == Calendar.JULY) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_07_july;
        } else if (month == Calendar.AUGUST) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_08_august;
        } else if (month == Calendar.SEPTEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_09_september;
        } else if (month == Calendar.OCTOBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_10_october;
        } else if (month == Calendar.NOVEMBER) {
            backgroundId = io.github.memfis19.cadar.R.drawable.bkg_11_november;
        }

        return backgroundId;
    }

    private class Custom extends RecyclerView.OnScrollListener {

        private View monthBackground;

        Custom() {
        }

        public void setMonthBackground(View monthBackground) {
            this.monthBackground = monthBackground;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (monthBackground != null) monthBackground.scrollBy(dx, (-1) * (dy / 10));
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        //postLocalRegistration();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_assignments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId())
        {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
