package com.schoofi.activitiess;

import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.MyAxisValueFormatter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.ReportXAxisValueFormatter;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.schoofi.utils.XYMarkerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChairmanStudentResultTermWise extends AppCompatActivity implements OnChartValueSelectedListener  {

    private BarChart mChart;
    private ImageView back;
    private TextView  totalstudents1,totalstudents2,totalstudents3,totalstudents4,totalstudents5,totalstudents6;
    private Typeface tf;
    float a, b, c, d, e1;
    private JSONArray chairmanPieCharArray;
    String termId,value,classId,sectionId,examId;
    int total = 0;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Chairman Student Result Term Wise");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_chairman_student_result_term_wise);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        totalstudents1 = (TextView) findViewById(R.id.text_no_of_s1);
        totalstudents2 = (TextView) findViewById(R.id.text_no_of_s2);
        totalstudents3 = (TextView) findViewById(R.id.text_no_of_s3);
        totalstudents4 = (TextView) findViewById(R.id.text_no_of_s4);
        totalstudents5 = (TextView) findViewById(R.id.text_no_of_s5);
        totalstudents6 = (TextView) findViewById(R.id.text_no_of_s6);
       // mChart = (BarChart) findViewById(R.id.chart1);
        value = getIntent().getStringExtra("value");
        examId = getIntent().getStringExtra("examId");
        termId = getIntent().getStringExtra("termId");
        classId = getIntent().getStringExtra("classId");
        sectionId = getIntent().getStringExtra("sectionId");
        getChairmanStudentResultList();
    }

    protected void openChart() {

        mChart = (BarChart) findViewById(R.id.chart1);
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
        ArrayList<String> classNames = new ArrayList<>();
        classNames.add(0,">85");
        classNames.add(1,"71-85");
        classNames.add(2,"55-70");
        classNames.add(3,"41-54");
        classNames.add(4,"<40");
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
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
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
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals5 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(0, a));
        yVals2.add(new BarEntry(0, b));
        yVals3.add(new BarEntry(0, c));
        yVals4.add(new BarEntry(0, d));
        yVals5.add(new BarEntry(0, e1));
        // create 3 datasets with different types
		/*ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);

		BarDataSet set1 = new BarDataSet(yVals1, "A");

		// set1.setColors(ColorTemplate.createColors(getApplicationContext(),
		// ColorTemplate.FRESH_COLORS));
		set1.setColor(colors.get(0));

		BarDataSet set2 = new BarDataSet(yVals2, "B");
		set2.setColor(colors.get(1));

		BarDataSet set3 = new BarDataSet(yVals3, "C");
		set3.setColor(colors.get(2));


		BarDataSet set4 = new BarDataSet(yVals4, "D");
		set4.setColor(colors.get(3));


		BarDataSet set5 = new BarDataSet(yVals5, "E");
		set5.setColor(colors.get(4));


		ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
		dataSets.add(set1);
		dataSets.add(set2);
		dataSets.add(set3);
		dataSets.add(set4);
		dataSets.add(set5);


		BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());

		// add space between the dataset groups in percent of bar-width
		data.setGroupSpace(80f);
		data.setValueTypeface(tf);

		mChart.setData(data);
		mChart.invalidate();*/

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Result Analysis");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tf);
            data.setBarWidth(0.9f);

            mChart.setData(data);
            mChart.invalidate();
        }
     /*   //tvX = (TextView) findViewById(R.id.tvXMax);
        //tvY = (TextView) findViewById(R.id.tvYMax);

        // mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        //mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        //mSeekBarY.setProgress(10);

        //mSeekBarX.setOnSeekBarChangeListener(this);
        //mSeekBarY.setOnSeekBarChangeListener(this);


        //mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
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
        xVals.add("Average Result");
		*//**//*xVals.add("0");
		xVals.add("0");
		xVals.add("0");
		xVals.add("0");*//**//*


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals5 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(a, 0));
        yVals2.add(new BarEntry(b, 0));
        yVals3.add(new BarEntry(c, 0));
        yVals4.add(new BarEntry(d, 0));
        yVals5.add(new BarEntry(e1,0));
        // create 3 datasets with different types
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        BarDataSet set1 = new BarDataSet(yVals1, ">85");

        // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
        // ColorTemplate.FRESH_COLORS));
        set1.setColor(colors.get(0));

        BarDataSet set2 = new BarDataSet(yVals2, "71-85");
        set2.setColor(colors.get(1));

        BarDataSet set3 = new BarDataSet(yVals3, "55-70");
        set3.setColor(colors.get(2));


        BarDataSet set4 = new BarDataSet(yVals4, "41-54");
        set4.setColor(colors.get(3));


        BarDataSet set5 = new BarDataSet(yVals5, "<40");
        set5.setColor(colors.get(4));


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);


        BarData data = new BarData(xVals, dataSets);
//        data.setValueFormatter(new LargeValueFormatter());

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);
        data.setValueTypeface(tf);

        mChart.setData(data);
        mChart.invalidate();*/

    }

    protected void getChairmanStudentResultList() {

        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FINAL_RESULT + "?ins_id=" + Preferences.getInstance().institutionId + "&cls_id=" + classId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId+"&sec_id="+sectionId+"&term="+termId+"&exam_id="+examId+"&value="+value+"&session="+Preferences.getInstance().session1;
        StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                System.out.println(url);

                try {
                    responseObject = new JSONObject(response);

                    //toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        //System.out.println("hhhhh");
                        mChart.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("Marks")) {
                        mChart.setVisibility(View.VISIBLE);
                        chairmanPieCharArray = new JSONObject(response).getJSONArray("Marks");
                        if (null != chairmanPieCharArray && chairmanPieCharArray.length() >= 0) {
                            com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
                            e.data = chairmanPieCharArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_FINAL_RESULT + "?ins_id=" + Preferences.getInstance().institutionId + "&cls_id=" + classId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId+"&sec_id="+sectionId+"&term="+termId+"&exam_id="+examId+"&value="+value+"&session="+Preferences.getInstance().session1, e);
                            for (i = 0; i < chairmanPieCharArray.length(); i++) {
                                total = total + Integer.parseInt(chairmanPieCharArray.getJSONObject(i).getString("grade").toString());
                                //System.out.println(total);
                                //System.out.println(i);
                                //noOfStudents.setText("Total Students:"+total);
                            }

                            totalstudents6.setText("Total Students: "+chairmanPieCharArray.getJSONObject(0).getString("no_stu"));

                            if (i == 1) {
                                a = (Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade")) / total)*100;
                            } else if (i == 2) {
                                a = (Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade")) / total)*100;
                                b = (Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade")) / total)*100;
                            } else if (i == 3) {
                                a = (Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade")) / total)*100;
                                b = (Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade")) / total)*100;
                                c = (Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade")) / total)*100;
                            } else if (i == 4) {
                                a = (Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade")) / total)*100;
                                b = (Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade")) / total)*100;
                                c = (Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade")) / total)*100;
                                d = (Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade")) / total)*100;
                            } else if (i == 5) {
                                a = (Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade")) / total)*100;
                                b = (Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade")) / total)*100;
                                c = (Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade")) / total)*100;
                                d = (Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade")) / total)*100;
                                e1 = (Float.parseFloat(chairmanPieCharArray.getJSONObject(4).getString("grade")) / total)*100;

                                totalstudents1.setText(">85: "+chairmanPieCharArray.getJSONObject(0).getString("grade"));
                                totalstudents2.setText("71-85: "+chairmanPieCharArray.getJSONObject(1).getString("grade"));
                                totalstudents3.setText("55-70: "+chairmanPieCharArray.getJSONObject(2).getString("grade"));
                                totalstudents4.setText("41-54: "+chairmanPieCharArray.getJSONObject(3).getString("grade"));
                                totalstudents5.setText("<40: "+chairmanPieCharArray.getJSONObject(4).getString("grade"));

                            }

                           // openChart();


                        }
                    } else
                        Utils.showToast(getApplicationContext(), "Error Fetching Response");
                    setProgressBarIndeterminateVisibility(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                    setProgressBarIndeterminateVisibility(false);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
                setProgressBarIndeterminateVisibility(false);
            }
        }) {
			/*@Override
			protected Map<String,String> getParams(){
				Preferences.getInstance().loadPreference(TeacherStudentResult.this);
				Map<String,String> params = new HashMap<String, String>();
				//params.put("student_ID",Preferences.getInstance().studentId);
				params.put("sec_id=",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id",Preferences.getInstance().studentClassId);
				params.put("sch_id", Preferences.getInstance().schoolId);
				params.put("exam_id","1");
				return params;
			}*/
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }*/




}
