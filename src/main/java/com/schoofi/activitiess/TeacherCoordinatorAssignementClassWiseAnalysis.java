package com.schoofi.activitiess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DayAxisValueFormatter;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.MyAxisValueFormatter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.ReportXAxisValueFormatter;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.schoofi.utils.XYMarkerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TeacherCoordinatorAssignementClassWiseAnalysis extends DemoBase implements OnChartValueSelectedListener {

    private BarChart mChart;
    private Typeface tf;
    private JSONArray techerCoordinatorattendanceArrray;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    public ArrayList<String> classNames = new ArrayList<>();
    public ArrayList<Float> pendingFees = new ArrayList<>();
    private ImageView calendar1,back;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    String date1,date2;
    String year1,month1,day1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
    String selectedDate1,selectedDate2;
    Date selectedDate3;
    String dat,fromDate,toDate;
    Date date3,date4;

    TextView totalPendingfees,totalStudents;
    String Rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_coordinator_assignement_class_wise_analysis);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChart = (BarChart) findViewById(R.id.chart1);
        /*calendar1= (ImageView) findViewById(R.id.calendarIcon);
        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mChart.clear();

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);

                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                showDialog(999);
                //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                //.show();

                showDate(year, month, day);
            }
        });*/

        fromDate = getIntent().getStringExtra("fromDate");
        toDate = getIntent().getStringExtra("toDate");

        totalPendingfees = (TextView) findViewById(R.id.txt_total_pending_fees);
        getChairmanStudentLeaveList();
    }

    protected void openChart() {
        //tvX = (TextView) findViewById(R.id.tvXMax);
        //tvY = (TextView) findViewById(R.id.tvYMax);

        // mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        //mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        //mSeekBarY.setProgress(10);

        //mSeekBarX.setOnSeekBarChangeListener(this);
        //mSeekBarY.setOnSeekBarChangeListener(this);


       /* mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // define an offset to change the original position of the marker
        // (optional)
        // mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

        // set the marker to the chart
        //mChart.setMarkerView(mv);

        //mSeekBarX.setProgress(10);
        //mSeekBarY.setProgress(100);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(tf);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //rightAxis.setDrawGridLines(false);
        rightAxis.setSpaceTop(30f);
        rightAxis.setAxisMinValue(0f);
        ArrayList<String> xVals = new ArrayList<String>();
        for (int y=0;y<pendingFees.size();y++) {
            xVals.add(classNames.get(y));
        }
		*//*xVals.add("0");
		xVals.add("0");
		xVals.add("0");
		xVals.add("0");*//*




        *//*yVals1.add(new BarEntry(a, 0));
        yVals2.add(new BarEntry(b, 0));
        yVals3.add(new BarEntry(c, 0));
        yVals4.add(new BarEntry(d, 0));
        yVals5.add(new BarEntry(e1,0));*//*
        // create 3 datasets with different types
        ArrayList<Integer> colors = new ArrayList<Integer>();


        colors.add(Color.rgb(31, 144, 69));

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();


        for(int o=0;o<classNames.size();o++)
        {
            float val = pendingFees.get(o);
            yVals.add(new BarEntry(val, o));
        }

        BarDataSet set1 = new BarDataSet(yVals, "Class Assignments");
        set1.setColor(colors.get(0));


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);



        BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);
        data.setValueTypeface(tf);

        mChart.setData(data);
        mChart.invalidate();*/

        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);
//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // define an offset to change the original position of the marker
        // (optional)
        // mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());

        // set the marker to the chart
        //mChart.setMarkerView(mv);

        //mSeekBarX.setProgress(10);
        //mSeekBarY.setProgress(100);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        /*Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTypeface(tf);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);*/

        IAxisValueFormatter xAxisFormatter = new ReportXAxisValueFormatter(mChart,classNames);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);
        xAxis.setDrawGridLines(false);
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
        rightAxis.setAxisMinimum(0f);
        // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

       /* mChart.getAxisRight().setEnabled(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //rightAxis.setDrawGridLines(false);
        rightAxis.setSpaceTop(30f);
        rightAxis.setAxisMinValue(0f);*/

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        /*ArrayList<String> xVals = new ArrayList<String>();
        for (int y=0;y<pendingFees.size();y++) {
            xVals.add(classNames.get(y));
        }
		*//*xVals.add("0");
		xVals.add("0");
		xVals.add("0");
		xVals.add("0");*//*




        *//*yVals1.add(new BarEntry(a, 0));
        yVals2.add(new BarEntry(b, 0));
        yVals3.add(new BarEntry(c, 0));
        yVals4.add(new BarEntry(d, 0));
        yVals5.add(new BarEntry(e1,0));*//*
        // create 3 datasets with different types
        ArrayList<Integer> colors = new ArrayList<Integer>();


        colors.add(Color.rgb(31, 144, 69));

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();


        for(int o=0;o<classNames.size();o++)
        {
            float val = pendingFees.get(o);
            yVals.add(new BarEntry(val, o));
        }

        BarDataSet set1 = new BarDataSet(yVals, "Class Assignments");
        set1.setColor(colors.get(0));


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);



        BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);
        data.setValueTypeface(tf);

        mChart.setData(data);
        mChart.invalidate();*/

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
            set1 = new BarDataSet(yVals1, "Class Assignment Analysis");

            set1.setDrawIcons(false);
            set1.setColors(Color.rgb(31, 144, 69));

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
            position1 = e.getX();
            Intent intent = new Intent(TeacherCoordinatorAssignementClassWiseAnalysis.this,TeacherCoordinaterSectionWiseAssignmentAnalysis.class);
            intent.putExtra("position",(int)(Math.round(position1)));
            intent.putExtra("fromDate",fromDate);
            intent.putExtra("toDate",toDate);
            try {
                intent.putExtra("classId",techerCoordinatorattendanceArrray.getJSONObject(Math.round(position1)).getString("class_ids"));
                intent.putExtra("class_names",techerCoordinatorattendanceArrray.getJSONObject(Math.round(position1)).getString("class_names"));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            startActivity(intent);
    }

        @Override
        public void onNothingSelected() { }

   /* @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
        // Utils.showToast(getApplicationContext(), ""+);


        try {
            Intent intent = new Intent(TeacherCoordinatorAssignementClassWiseAnalysis.this,TeacherCoordinaterSectionWiseAssignmentAnalysis.class);
            intent.putExtra("position",e.getXIndex());
            intent.putExtra("fromDate",fromDate);
            intent.putExtra("toDate",toDate);
            intent.putExtra("classId",techerCoordinatorattendanceArrray.getJSONObject(e.getXIndex()).getString("class_ids"));
            startActivity(intent);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }


    }





    @Override
    public void onNothingSelected() {
        Log.d("PieChart", "nothing selected");
    }*/

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);

        classNames.clear();
        pendingFees.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_ASSIGNMENT_ANALYSIS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        //chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {



                        techerCoordinatorattendanceArrray= new JSONObject(response).getJSONArray("responseObject");
                        if(null!=techerCoordinatorattendanceArrray && techerCoordinatorattendanceArrray.length()>=0) {



                           // totalStudents.setText("Total Sudents: "+techerCoordinatorattendanceArrray.getJSONObject(0).getString("final_sum"));
                            totalPendingfees.setText("Total: "+techerCoordinatorattendanceArrray.getJSONObject(0).getString("final_sum"));


                            for(int i=0;i<techerCoordinatorattendanceArrray.length();i++)
                            {
                                classNames.add(techerCoordinatorattendanceArrray.getJSONObject(i).getString("class_names"));

                                pendingFees.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_assignment")));
                            }

                            Log.d("kjh",pendingFees.toString());

                            /*for(int j=0;j<absents.size();j++)
                            {
                                if(absents.get(j) == 0 || totalStudents.get(j) ==0)
                                {
                                    absents1.add(j,Float.valueOf(0));
                                }

                                else {
                                    absents1.add(j,absents.get(j));
                                }
                            }



                            for(int k=0;k<presents.size();k++)
                            {
                                if(presents.get(k) == 0 || totalStudents.get(k) ==0)
                                {
                                    presents1.add(k,Float.valueOf(0));
                                }

                                else {
                                    presents1.add(k,presents.get(k));
                                }
                            }

                            for(int l=0;l<leaves.size();l++)
                            {
                                if(leaves.get(l) == 0 || totalStudents.get(l) ==0)
                                {
                                    leaves1.add(l,Float.valueOf(0));
                                }

                                else {
                                    leaves1.add(l,leaves.get(l));
                                }
                            }

                           Log.d("kkk",presents.toString());*/
                            openChart();


                        }
                        else
                        {

                        }

                    } else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    // setSupportProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    //setSupportProgressBarIndeterminateVisibility(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                //setSupportProgressBarIndeterminateVisibility(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Preferences.getInstance().loadPreference(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("sch_id", Preferences.getInstance().schoolId);

                params.put("token", Preferences.getInstance().token);

                params.put("ins_id", Preferences.getInstance().institutionId);


                params.put("device_id", Preferences.getInstance().deviceId);
                if(Preferences.getInstance().userRoleId.matches("4")) {
                    params.put("cordinater_id", Preferences.getInstance().userId);
                }
                else
                {

                }
                params.put("from_date",fromDate);
                params.put("to_date",toDate);
                params.put("type","Assignment");
                //params.put("class_id",classId);

                //Log.d("ki",classId+date);
                return params;
            }
        };

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (Utils.isNetworkAvailable(getApplicationContext()))
            queue.add(requestObject);
        else {
            Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
        }
    }


    private void toa() {
        System.out.println("aaa");
    }
}
