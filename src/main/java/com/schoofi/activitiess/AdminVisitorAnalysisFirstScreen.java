package com.schoofi.activitiess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.schoofi.adapters.AdminVisitorLogsTypeWiseAdapter;
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

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;


public class AdminVisitorAnalysisFirstScreen extends DemoBase implements OnChartValueSelectedListener {

    private ListView chairmanAssignmentClassSectionWiseAnalysis;
    private JSONArray chairmanAssigmentClassSectionWiseArray;
    private AdminVisitorLogsTypeWiseAdapter adminVisitorLogsTypeWiseAdapter;
    private EditText fromEditTextDate,toEditTextDate;
    private Button button_student_custom_date_picker;
    private PopupWindow calendarPopup,calendarPopup1;
    int count1 = 0;
    int count = 0;
    int year_x,month_x,day_x;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    static final int dialog_id=1;
    String from1,to1;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    private TextView newView;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String VALUE = "VALUE";
    private String value;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    // Date date = new Date();
    Calendar cal = Calendar.getInstance();
    Calendar cal1 = Calendar.getInstance();
    String fromDate,toDate;
    String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
    public ArrayList<String> classNames = new ArrayList<>();
    public ArrayList<Float> pendingFees = new ArrayList<>();

    private int mPage;
    private Context context;
    int v2;
    private ImageView back;
    private BarChart mChart;
    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_visitor_analysis_first_screen);
        chairmanAssignmentClassSectionWiseAnalysis = (ListView) findViewById(R.id.listview_chairman_analysis);

        fromEditTextDate = (EditText) findViewById(R.id.edit_fromDatePicker);
        toEditTextDate = (EditText) findViewById(R.id.edit_toDatePicker);
        context = AdminVisitorAnalysisFirstScreen.this;
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        fromDate = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
        cal1.add(Calendar.DATE, -1);
        Date todate2 = cal1.getTime();
        toDate = new SimpleDateFormat("yyyy-MM-dd").format(todate2);

        fromEditTextDate.setOnClickListener(onEditTextClickListener);
        toEditTextDate.setOnClickListener(onEditTextClickListener1);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mChart = (BarChart) findViewById(R.id.chart1);
        //}

        //fromEditTextDate.setOnTouchListener(otl);
        fromEditTextDate.setInputType(0);
        toEditTextDate.setInputType(0);
        button_student_custom_date_picker = (Button) findViewById(R.id.btn_student_custom_date_picker);
        button_student_custom_date_picker.setText("Done");
        button_student_custom_date_picker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Utils.showToast(getActivity(),Preferences.getInstance().schoolId+"k"+Preferences.getInstance().token+"k"+Preferences.getInstance().institutionId+"k"+Preferences.getInstance().deviceId+to1+from1);
                //Log.d("kl",Preferences.getInstance().schoolId+"k"+Preferences.getInstance().token+"k"+Preferences.getInstance().institutionId+"k"+Preferences.getInstance().deviceId+to1+from1);
                if(count==0 && count1==0) {
                    Toast.makeText(getApplicationContext(), "not valid date", Toast.LENGTH_SHORT).show();
                    v2 = 0;
                }
                else
                if(count==0 && count1==1)
                {
                    Toast.makeText(getApplicationContext(), "not valid date", Toast.LENGTH_SHORT).show();
                    v2=0;
                }
                else
                if(count==1 && count1==0)
                {
                    Toast.makeText(getApplicationContext(), "not valid date", Toast.LENGTH_SHORT).show();
                    v2=0;
                }
                else
                //view = inflater.inflate(R.layout.student_daily_attendance, container, false);
                {
                    //Utils.showToast(getActivity(),Preferences.getInstance().schoolId+"k"+Preferences.getInstance().token+"k"+Preferences.getInstance().institutionId+"k"+Preferences.getInstance().deviceId+to1+from1);
                    // Log.d("kl",Preferences.getInstance().schoolId+"k"+Preferences.getInstance().token+"k"+Preferences.getInstance().institutionId+"k"+Preferences.getInstance().deviceId+to1+from1);
v2=1;
                    //initData1();
                    getChairmanStudentLeaveList2();

                }
            }
        });



        //setDateTimeField();
        System.out.println(date);






        chairmanAssignmentClassSectionWiseAnalysis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Preferences.getInstance().loadPreference(getApplicationContext());
                try {
                    Intent intent = new Intent(getApplicationContext(), AdminVisitorTypeDetailsActivity.class);
                    if(v2==1) {
                        intent.putExtra("value", "3");
                        intent.putExtra("from1",from1);
                        intent.putExtra("to1",to1);
                    }
                    else
                    {
                        intent.putExtra("value","1");
                    }
                    Preferences.getInstance().visitorType = chairmanAssigmentClassSectionWiseArray.getJSONObject(position).getString("id");

                    Preferences.getInstance().savePreference(getApplicationContext());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        //initData();
        getChairmanStudentLeaveList3();
    }

    protected void openChart()
    {
        //tvX = (TextView) findViewById(R.id.tvXMax);
        //tvY = (TextView) findViewById(R.id.tvYMax);

        // mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        //mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        //mSeekBarY.setProgress(10);

        //mSeekBarX.setOnSeekBarChangeListener(this);
        //mSeekBarY.setOnSeekBarChangeListener(this);


      /*  mChart.setOnChartValueSelectedListener(this);
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

        BarDataSet set1 = new BarDataSet(yVals, "Visitor Analysis");
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
            set1 = new BarDataSet(yVals1, "Visitor Analysis");

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

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId+"&value="+"1");
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {
            adminVisitorLogsTypeWiseAdapter = new AdminVisitorLogsTypeWiseAdapter(AdminVisitorAnalysisFirstScreen.this, chairmanAssigmentClassSectionWiseArray,"1");
            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(adminVisitorLogsTypeWiseAdapter);
            adminVisitorLogsTypeWiseAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanAssigmentClassSectionWiseArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId+"&value="+"1", e);
                            chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.VISIBLE);
                            chairmanAssignmentClassSectionWiseAnalysis.invalidateViews();
                            adminVisitorLogsTypeWiseAdapter = new AdminVisitorLogsTypeWiseAdapter(AdminVisitorAnalysisFirstScreen.this, chairmanAssigmentClassSectionWiseArray,"1");
                            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(adminVisitorLogsTypeWiseAdapter);
                            adminVisitorLogsTypeWiseAdapter.notifyDataSetChanged();


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
                params.put("value","1");
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

    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(getApplicationContext());
                CalendarPickerView calendarView = new CalendarPickerView(getApplicationContext());
                calendarView.setListener(dateSelectionListener);
                calendarPopup.setContentView(calendarView);
                calendarPopup.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup.setHeight(1);
                calendarPopup.setWidth(view.getWidth());
                calendarPopup.setOutsideTouchable(true);
            }
            calendarPopup.showAsDropDown(view);
        }
    };

    private View.OnClickListener onEditTextClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup1 == null) {
                calendarPopup1 = new PopupWindow(getApplicationContext());
                CalendarPickerView calendarView = new CalendarPickerView(getApplicationContext());
                calendarView.setListener(dateSelectionListener1);
                calendarPopup1.setContentView(calendarView);
                calendarPopup1.setWindowLayoutMode(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                calendarPopup1.setHeight(1);
                calendarPopup1.setWidth(view.getWidth());
                calendarPopup1.setOutsideTouchable(true);
            }
            calendarPopup1.showAsDropDown(view);
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
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            from1= formatter1.format(selectedDate.getTime());

            fromEditTextDate.setText(formatter.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            try {
                Date date1 = formatter.parse(date);
                String fromEditTextDate1 = fromEditTextDate.getText().toString();
                Date date2 = formatter.parse(fromEditTextDate1);

                if(date1.compareTo(date2)<0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count=0;
                }
                else
                {
                    count =1;
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            to1= formatter1.format(selectedDate.getTime());
            toEditTextDate.setText(formatter.format(selectedDate.getTime()));
            try {
                Date date1 = formatter.parse(date);

                String toEditTextDate1 = toEditTextDate.getText().toString();
                Date date2 = formatter.parse(toEditTextDate1);
                String fromEditTextDate1 = fromEditTextDate.getText().toString();
                Date date3 = formatter.parse(fromEditTextDate1);
                if(date1.compareTo(date2)<0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1 =0;
                }
                else
                if(date2.compareTo(date3)<0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }

                else
                if(date2.compareTo(date3)==0)
                {
                    Toast.makeText(getApplicationContext(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }
                else
                {
                    count1 =1;
                }


            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };






    private void initData1() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&value=" + "3"+"&from_date="+from1+"&to_date="+to1);
            if (e == null) {
                chairmanAssigmentClassSectionWiseArray = null;
            } else {
                chairmanAssigmentClassSectionWiseArray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (chairmanAssigmentClassSectionWiseArray != null) {
            adminVisitorLogsTypeWiseAdapter = new AdminVisitorLogsTypeWiseAdapter(AdminVisitorAnalysisFirstScreen.this, chairmanAssigmentClassSectionWiseArray, "2");
            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(adminVisitorLogsTypeWiseAdapter);
            adminVisitorLogsTypeWiseAdapter.notifyDataSetChanged();
        }
    }

    protected void getChairmanStudentLeaveList1() {
        //setSupportProgressBarIndeterminateVisibility(true);
        classNames.clear();
        pendingFees.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = chairmanAssigmentClassSectionWiseArray.toString().getBytes();
                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId + "&value=" + "3"+"&from_date="+from1+"&to_date="+to1, e);
                            chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.VISIBLE);
                            chairmanAssignmentClassSectionWiseAnalysis.invalidateViews();
                            adminVisitorLogsTypeWiseAdapter = new AdminVisitorLogsTypeWiseAdapter(AdminVisitorAnalysisFirstScreen.this, chairmanAssigmentClassSectionWiseArray, "2");
                            chairmanAssignmentClassSectionWiseAnalysis.setAdapter(adminVisitorLogsTypeWiseAdapter);
                            adminVisitorLogsTypeWiseAdapter.notifyDataSetChanged();


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
                params.put("value", "3");
                params.put("from_date",from1);
                params.put("to_date",to1);
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


    protected void getChairmanStudentLeaveList2() {
        //setSupportProgressBarIndeterminateVisibility(true);
        pendingFees.clear();
        classNames.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(getApplicationContext(), "No Visitors Found");
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            for(int i=0;i<chairmanAssigmentClassSectionWiseArray.length();i++)
                            {
                                classNames.add(chairmanAssigmentClassSectionWiseArray.getJSONObject(i).getString("visit_name"));

                                pendingFees.add(Float.parseFloat(chairmanAssigmentClassSectionWiseArray.getJSONObject(i).getString("visit_count")));
                            }

                            openChart();



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
                params.put("value", "3");
                params.put("from_date",from1);
                params.put("to_date",to1);
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

    protected void getChairmanStudentLeaveList3() {
        //setSupportProgressBarIndeterminateVisibility(true);
        pendingFees.clear();
        classNames.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        Utils.showToast(getApplicationContext(), "No Visitors Today");
                        chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        chairmanAssigmentClassSectionWiseArray = new JSONObject(response).getJSONArray("responseObject");
                        if (null != chairmanAssigmentClassSectionWiseArray && chairmanAssigmentClassSectionWiseArray.length() >= 0) {
                            for(int i=0;i<chairmanAssigmentClassSectionWiseArray.length();i++)
                            {
                                classNames.add(chairmanAssigmentClassSectionWiseArray.getJSONObject(i).getString("visit_name"));

                                pendingFees.add(Float.parseFloat(chairmanAssigmentClassSectionWiseArray.getJSONObject(i).getString("visit_count")));
                            }

                            openChart();



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
                params.put("value", "1");

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

  /*  @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
        // Utils.showToast(getApplicationContext(), ""+);

        Preferences.getInstance().loadPreference(getApplicationContext());
        try {
            Intent intent = new Intent(getApplicationContext(), AdminVisitorTypeDetailsActivity.class);
            if(v2==1) {
                intent.putExtra("value", "3");
                intent.putExtra("from1",from1);
                intent.putExtra("to1",to1);
            }
            else
            {
                intent.putExtra("value","1");
            }
            Preferences.getInstance().visitorType = chairmanAssigmentClassSectionWiseArray.getJSONObject(e.getXIndex()).getString("id");

            Preferences.getInstance().savePreference(getApplicationContext());
            startActivity(intent);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }



        *//*try {
            Intent intent = new Intent(TeacherCoordinatorCircularAnalysisClassWise.this,TeacherCoordinatorCircularAnalysisSectionWise.class);
            intent.putExtra("position",e.getXIndex());
            intent.putExtra("fromDate",fromDate);
            intent.putExtra("toDate",toDate);
            intent.putExtra("classId",techerCoordinatorattendanceArrray.getJSONObject(e.getXIndex()).getString("class_ids"));
            startActivity(intent);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }*//*


    }





    @Override
    public void onNothingSelected() {
        Log.d("PieChart", "nothing selected");
    }*/

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

        Preferences.getInstance().loadPreference(getApplicationContext());
        try {
            Intent intent = new Intent(getApplicationContext(), AdminVisitorTypeDetailsActivity.class);
            if(v2==1) {
                intent.putExtra("value", "3");
                intent.putExtra("from1",from1);
                intent.putExtra("to1",to1);
            }
            else
            {
                intent.putExtra("value","1");
            }
            Preferences.getInstance().visitorType = chairmanAssigmentClassSectionWiseArray.getJSONObject(Math.round(e.getX())).getString("id");

            Preferences.getInstance().savePreference(getApplicationContext());
            startActivity(intent);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected() { }
}
