package com.schoofi.activitiess;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activities.LoginScreen;

import com.schoofi.adapters.StudentCollegeNewAttendanceAdapter;
import com.schoofi.adapters.StudentSubjectCollegeAdapter;
import com.schoofi.adapters.TeacherStudentAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.MyAxisValueFormatter;
import com.schoofi.utils.NonScrollableListview;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.ReportXAxisValueFormatter;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TeacherHomeScreenVO;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.schoofi.utils.XYMarkerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentCollegeAttendancePrimaryScreen extends DemoBase implements OnChartValueSelectedListener {
    private TextView schoolClass,logouut,totalAttendance,totalAttendance1,totalPercentage,totalPercentage1;
    ImageView backButton;
    public ArrayList<TeacherHomeScreenVO> teacherHomeScreenList;

    StudentCollegeNewAttendanceAdapter teacherHomeScreenAdapter;
    private JSONArray teacherHomeScreenArray;

    String email = Preferences.getInstance().userEmailId;
    String token = Preferences.getInstance().token;
    String schoolId = Preferences.getInstance().schoolId;
    String userId = Preferences.getInstance().userId;
    private Button viewAnalysis;
    private BarChart mChart;
    private Typeface tf;
    public ArrayList<String> classNames = new ArrayList<>();
    public ArrayList<Float> pendingFees = new ArrayList<>();
    NonScrollableListview teacherHomeScreenListView;

    List<Integer> mColors = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_new_student_college_attendance_layout);

        Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Teacher HomeScreen");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
//        schoolClass = (TextView) findViewById(R.id.txt_class);
       backButton = (ImageView) findViewById(R.id.img_back);
       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               finish();
           }
       });

        mChart = (BarChart) findViewById(R.id.chart1);

       totalAttendance = (TextView) findViewById(R.id.text_header2);
       totalAttendance1 = (TextView) findViewById(R.id.text_header_total_attendance);
       totalPercentage = (TextView) findViewById(R.id.text_header1);
       totalPercentage1 = (TextView) findViewById(R.id.text_header_total_percentage);

       teacherHomeScreenListView = (NonScrollableListview) findViewById(R.id.listView_students_polls);







        teacherHomeScreenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub

                Preferences.getInstance().loadPreference(StudentCollegeAttendancePrimaryScreen.this);
                try {
                    Preferences.getInstance().studentSubjectId = teacherHomeScreenArray.getJSONObject(position).getString("subject_id");
                    Preferences.getInstance().savePreference(StudentCollegeAttendancePrimaryScreen.this);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                //Preferences.getInstance().loadPreference(TeacherHomeScreen.this);
                //System.out.print(Preferences.getInstance().teachId);

                Intent intent = new Intent(StudentCollegeAttendancePrimaryScreen.this,StudentCollegeAttendance.class);
                startActivity(intent);

            }
        });

        getClassList();


    }

    protected void openChart() {


        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);
//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDrawGridBackground(true);



        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");


        IAxisValueFormatter xAxisFormatter = new ReportXAxisValueFormatter(mChart,classNames);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tf);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(false);





        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart



        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < pendingFees.size(); i++) {
            //float mult = (range + 1);


            yVals1.add(new BarEntry(i, pendingFees.get(i)));

        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {

            mColors = new ArrayList<Integer>();


            //set1.setColors(Color.rgb(31, 144, 69));

            for (int i = 0; i < pendingFees.size(); i++) {
                //float mult = (range + 1);
                try {
                    if(!teacherHomeScreenArray.getJSONObject(i).getString("threshold").equalsIgnoreCase("null")) {
                        if (pendingFees.get(i) < Integer.parseInt(teacherHomeScreenArray.getJSONObject(i).getString("threshold"))) {

                            mColors.add(Color.rgb(238, 71, 73));

                        } else {
                            mColors.add(Color.rgb(31, 144, 69));

                        }
                    }
                    else{
                        mColors.add(Color.rgb(31, 144, 69));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            set1 = new BarDataSet(yVals1, "");

            set1.setDrawIcons(false);
            set1.setColors(mColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);


            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tf);
            data.setBarWidth(0.9f);


            mChart.setData(data);

            mChart.invalidate();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher_home_screen, menu);
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

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ATTENDANCE_SUBJECT+"?u_email="+email+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1+"&course_id="+Preferences.getInstance().studentCourse);
            if(e == null)
            {
                teacherHomeScreenArray= null;
            }
            else
            {
                teacherHomeScreenArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(teacherHomeScreenArray!= null)
        {
            try {
                if(teacherHomeScreenArray.getJSONObject(0).getString("is_coordinator").matches("YES"))
                {
                    viewAnalysis.setVisibility(View.VISIBLE);
                }
                else {
                    viewAnalysis.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            teacherHomeScreenAdapter= new StudentCollegeNewAttendanceAdapter(StudentCollegeAttendancePrimaryScreen.this,teacherHomeScreenArray,"1");
            teacherHomeScreenListView.setAdapter(teacherHomeScreenAdapter);
            teacherHomeScreenAdapter.notifyDataSetChanged();
        }
    }

    protected void getClassList()
    {
        classNames.clear();
        pendingFees.clear();
        RequestQueue queue = VolleySingleton.getInstance(StudentCollegeAttendancePrimaryScreen.this).getRequestQueue();
        final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ATTENDANCE_SUBJECT+"?u_email="+email+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1+"&course_id="+Preferences.getInstance().studentCourse;
        Log.d("url",url);
        StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                        Utils.showToast(StudentCollegeAttendancePrimaryScreen.this,"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(StudentCollegeAttendancePrimaryScreen.this, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("subject_attendance_list"))
                    {
                        teacherHomeScreenArray= new JSONObject(response).getJSONArray("subject_attendance_list");
                        if(null!=teacherHomeScreenArray && teacherHomeScreenArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = teacherHomeScreenArray.toString().getBytes();
                            VolleySingleton.getInstance(StudentCollegeAttendancePrimaryScreen.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ATTENDANCE_SUBJECT+"?u_email="+email+"&u_id="+userId+"&token="+token+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&stu_id="+Preferences.getInstance().studentId+"&session="+Preferences.getInstance().session1+"&course_id="+Preferences.getInstance().studentCourse,e);
                            float total_attendance=0,present_attendance=0;
                            float attendance_percentage =0;

                            int height=0;

                            for(int i=0;i<teacherHomeScreenArray.length();i++)
                            {
                                total_attendance = (total_attendance+Float.parseFloat(teacherHomeScreenArray.getJSONObject(i).getString("total_attendance")));
                                present_attendance = (present_attendance+Float.parseFloat(teacherHomeScreenArray.getJSONObject(i).getString("present_attendance")));

                                if (teacherHomeScreenArray.getJSONObject(i).getString("subject_name").length() == 0)
                                    return;

                                //split the string using 'space'
                                //and print the first character of every word
                                String words[] = teacherHomeScreenArray.getJSONObject(i).getString("subject_name").split(" ");
                                String co="";

                                Log.d("op", String.valueOf(words.length));
                                for(String word : words) {
                                    try {

                                        co = co.concat(Character.toUpperCase(word.charAt(0)) + " ");
                                    }
                                    catch(Exception e1)
                                    {

                                    }
                                }

                                classNames.add(co);

                                if(total_attendance == 0.0)
                                {
                                    pendingFees.add(Float.parseFloat("0.0"));
                                }

                                else {


                                    pendingFees.add((Float.parseFloat(teacherHomeScreenArray.getJSONObject(i).getString("present_attendance")) / Float.parseFloat(teacherHomeScreenArray.getJSONObject(i).getString("total_attendance"))) * 100);
                                }

                                    height =i;

                            }




                            openChart();
                         //   totalAttendance.setText("Total Attendance "+present_attendance+"/"+total_attendance);
                            totalAttendance.setText("Total Attendance");
                            totalAttendance1.setText(present_attendance+"/"+total_attendance);
                            Log.d("op",String.valueOf(total_attendance));
                         if(total_attendance == 0.0)
                            {
                                attendance_percentage = (float) 0.0;
                                totalPercentage.setText("Percentage");
                                totalPercentage1.setText(attendance_percentage+"%");
                            }
                            else {
                                Log.d("op1",String.valueOf(total_attendance));
                              attendance_percentage = (present_attendance/total_attendance)*100;
                              totalPercentage.setText("Percentage");
                                totalPercentage1.setText(Math.round(attendance_percentage)+"%");
                            }


                            if(!teacherHomeScreenArray.getJSONObject(0).getString("threshold").equalsIgnoreCase("null")) {

                                if (attendance_percentage > Integer.parseInt(teacherHomeScreenArray.getJSONObject(0).getString("threshold"))) {
                                    totalPercentage1.setTextColor(Color.parseColor("#009f4d"));
                                    totalAttendance1.setTextColor(Color.parseColor("#009f4d"));
                                } else {
                                    totalPercentage1.setTextColor(Color.parseColor("#ee4749"));
                                    totalAttendance1.setTextColor(Color.parseColor("#ee4749"));
                                }
                            }
                            else{
                                totalPercentage1.setTextColor(Color.parseColor("#ee4749"));
                                totalAttendance1.setTextColor(Color.parseColor("#ee4749"));
                            }


                                 teacherHomeScreenListView.invalidateViews();



                                 teacherHomeScreenAdapter = new StudentCollegeNewAttendanceAdapter(StudentCollegeAttendancePrimaryScreen.this, teacherHomeScreenArray,"1");
                                 teacherHomeScreenListView.setAdapter(teacherHomeScreenAdapter);
                                 teacherHomeScreenAdapter.notifyDataSetChanged();
//
//                             }catch (Exception e2)
//                             {
//                                Log.d("oo",e2.toString());
//                             }

                        }
                    }
                    else
                        Utils.showToast(StudentCollegeAttendancePrimaryScreen.this, "Error Fetching Response");


                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(StudentCollegeAttendancePrimaryScreen.this, "Error fetching modules! Please try after sometime.");

                }

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");

            }
        })
        {
			};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(StudentCollegeAttendancePrimaryScreen.this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(StudentCollegeAttendancePrimaryScreen.this, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
        float position1;

    }

    @Override
    public void onNothingSelected() { }
}
