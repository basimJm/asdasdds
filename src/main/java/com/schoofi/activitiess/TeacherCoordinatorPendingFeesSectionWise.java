package com.schoofi.activitiess;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TeacherCoordinatorPendingFeesSectionWise extends DemoBase implements OnChartValueSelectedListener {

    private BarChart mChart;
    private Typeface tf;
    private JSONArray techerCoordinatorattendanceArrray,techerCoordinatorattendanceArrray1;
    String date;
    public ArrayList<String> classNames = new ArrayList<>();
    public ArrayList<String> sectionId = new ArrayList<>();
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
    String dat;
    Date date3,date4;

    TextView totalPendingfees,totalStudents;
    String Rs,className;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_coordinator_pending_fees_class_wise);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChart = (BarChart) findViewById(R.id.chart1);
        calendar1= (ImageView) findViewById(R.id.calendarIcon);
        position = getIntent().getExtras().getInt("position");
        className = getIntent().getStringExtra("class_names");
        date = getIntent().getStringExtra("date");
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
        });

        totalPendingfees = (TextView) findViewById(R.id.txt_total_pending_fees);
        totalStudents = (TextView) findViewById(R.id.txt_total_student_count);
        getChairmanStudentLeaveList();
    }

    private void showDate(int year, int month, int day) {
        //date1 = day+month+year;
        //System.out.println(date1);
        //System.out.println(year);
        //System.out.println(month);
        //System.out.println(day);

        year1 = Integer.toString(year);
        month1 = Integer.toString(month);
        day1 = Integer.toString(day);

        date = year1+"-"+month1+"-"+day1;

        selectedDate1 = date;

        try {
            selectedDate3 = sdf.parse(selectedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        selectedDate2 = sdf1.format(selectedDate3);

        //selectedDate.setText(selectedDate2);
        getChairmanStudentLeaveList();











    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
        //.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);


        }
        //}
    };

    protected void openChart() {
        //tvX = (TextView) findViewById(R.id.tvXMax);
        //tvY = (TextView) findViewById(R.id.tvYMax);

        // mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        //mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        //mSeekBarY.setProgress(10);

        //mSeekBarX.setOnSeekBarChangeListener(this);
        //mSeekBarY.setOnSeekBarChangeListener(this);


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
        //l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc","def", "ghj", "ikl", "mno" });

       /* mChart.getAxisRight().setEnabled(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //rightAxis.setDrawGridLines(false);
        rightAxis.setSpaceTop(30f);
        rightAxis.setAxisMinValue(0f);*/

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);

        float start = 1f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < pendingFees.size(); i++) {
            //float mult = (range + 1);


            yVals1.add(new BarEntry(i, pendingFees.get(i)));

        }
        /*ArrayList<String> xVals = new ArrayList<String>();
        for (int y=0;y<pendingFees.size();y++) {
            xVals.add(classNames.get(y));
        }*/
		/*xVals.add("0");
		xVals.add("0");
		xVals.add("0");
		xVals.add("0");*/




        /*yVals1.add(new BarEntry(a, 0));
        yVals2.add(new BarEntry(b, 0));
        yVals3.add(new BarEntry(c, 0));
        yVals4.add(new BarEntry(d, 0));
        yVals5.add(new BarEntry(e1,0));*/
        // create 3 datasets with different types

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Section Fees Defaulters");

            set1.setDrawIcons(false);

            set1.setColors(Color.rgb(241, 196, 15));

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

    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        try {
            Log.i("VAL SELECTED",
                    "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                            + ", DataSet index: " + dataSetIndex);
            // Utils.showToast(getApplicationContext(), ""+dataSetIndex);
            Intent intent = new Intent(TeacherCoordinatorPendingFeesSectionWise.this,TeacherCoordinaterPendingFeesStudentWise.class);
            intent.putExtra("sec_id",sectionId.get(e.getXIndex()));
            intent.putExtra("class_id",techerCoordinatorattendanceArrray1.getJSONObject(position).getString("class_ids"));
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
        sectionId.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_PENDING_FEES/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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


                        techerCoordinatorattendanceArrray1= new JSONObject(response).getJSONArray("responseObject");
                        techerCoordinatorattendanceArrray= new JSONObject(response).getJSONArray("responseObject").getJSONObject(position).getJSONArray("sections");
                        if(null!=techerCoordinatorattendanceArrray && techerCoordinatorattendanceArrray.length()>=0) {
                            Context context;
                            context = TeacherCoordinatorPendingFeesSectionWise.this;
                            Rs = context.getString(R.string.Rs);
                            totalStudents.setText("Class "+className+" - Total Sudents: "+techerCoordinatorattendanceArrray1.getJSONObject(position).getString("total_stu_cls"));
                            totalPendingfees.setText("Total Pending Fees: "+Rs+techerCoordinatorattendanceArrray1.getJSONObject(position).getString("amount_due_cls"));


                            for(int i=0;i<techerCoordinatorattendanceArrray.length();i++)
                            {
                                classNames.add(techerCoordinatorattendanceArrray.getJSONObject(i).getString("section_name"));
                                sectionId.add(techerCoordinatorattendanceArrray.getJSONObject(i).getString("class_section_id"));

                                pendingFees.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("amount_due_sec")));
                            }

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
                //params.put("choosed_date",date);
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
                        + mChart.getHighestVisibleX()+e.getX());



        MPPointF.recycleInstance(position);
        int position1;
        //position1 = Integer.parseInt(mChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), mChart.getXAxis()));

        Intent intent = new Intent(TeacherCoordinatorPendingFeesSectionWise.this,TeacherCoordinaterPendingFeesStudentWise.class);
        intent.putExtra("sec_id",sectionId.get(Math.round(e.getX())));
        try {
            intent.putExtra("class_id",techerCoordinatorattendanceArrray1.getJSONObject(Math.round(e.getX())).getString("class_ids"));
            intent.putExtra("class_names",className);
            intent.putExtra("section_name",techerCoordinatorattendanceArrray.getJSONObject(Math.round(e.getX())).getString("section_name"));
        } catch (JSONException e1) {
        }
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() { }
}
