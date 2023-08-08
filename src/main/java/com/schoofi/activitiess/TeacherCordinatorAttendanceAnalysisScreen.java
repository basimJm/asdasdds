package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.IntegerRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;

import com.github.mikephil.charting.data.PieDataSet;

import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.schoofi.adapters.AdminVisitorLogsTypeWiseAdapter;
import com.schoofi.adapters.TeacherAttendanceDetailsListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TeacherCordinatorAttendanceAnalysisScreen extends DemoBase implements OnChartValueSelectedListener{

    private JSONArray techerCoordinatorattendanceArrray;
    private ImageView back,class1,calendar1;
    private PieChart mChart,mChart1,mChart2;
    private Typeface tf,tf1;
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
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    float a,b,c,d,e2,f;
    float total,total1,total3;
    float a1,b1,c1,d1,e1,f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher_cordinator_attendance_analysis_screen);
        mChart1 = (PieChart) findViewById(R.id.chart2);
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart2 = (PieChart) findViewById(R.id.chart3);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        class1 = (ImageView) findViewById(R.id.settings);
        class1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherCordinatorAttendanceAnalysisScreen.this,TeacherCoordinatorClassWiseAttendanceScreen.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });

        calendar1= (ImageView) findViewById(R.id.calendarIcon);
        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    protected void openChart()
    {

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText("TOTAL PRESENT",total));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);


        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDrawSliceText(false);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(2, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
       // Utils.showToast(getApplicationContext(),"kkk");
    }

    protected void openChart1()
    {

        mChart1.setUsePercentValues(true);
        mChart1.getDescription().setText("");
        mChart.getDescription().setEnabled(false);
        mChart1.setExtraOffsets(5, 10, 5, 5);

        mChart1.setDragDecelerationFrictionCoef(0.95f);
        tf1 = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart1.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart1.setCenterText(generateCenterSpannableText("TOTAL ABSENT",total1));

        mChart1.setDrawHoleEnabled(true);
        mChart1.setHoleColor(Color.WHITE);

        mChart1.setTransparentCircleColor(Color.WHITE);
        mChart1.setTransparentCircleAlpha(110);

        mChart1.setHoleRadius(58f);
        mChart1.setTransparentCircleRadius(61f);

        mChart1.setDrawCenterText(true);
        mChart1.setDrawSliceText(false);

        mChart1.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart1.setRotationEnabled(true);
        mChart1.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart1.setOnChartValueSelectedListener(this);

        setData1(2, 100);

        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart1.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    protected void openChart2()
    {

        mChart2.setUsePercentValues(true);
        mChart2.getDescription().setText("");
        mChart.getDescription().setEnabled(false);

        mChart2.setExtraOffsets(5, 10, 5, 5);

        mChart2.setDragDecelerationFrictionCoef(0.95f);
        tf1 = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart2.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf"));
        mChart2.setCenterText(generateCenterSpannableText("TOTAL LEAVE",total3));

        mChart2.setDrawHoleEnabled(true);
        mChart2.setHoleColor(Color.WHITE);

        mChart2.setTransparentCircleColor(Color.WHITE);
        mChart2.setTransparentCircleAlpha(110);

        mChart2.setHoleRadius(58f);
        mChart2.setTransparentCircleRadius(61f);
        mChart2.setDrawSliceText(false);

        mChart2.setDrawCenterText(true);

        mChart2.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart2.setRotationEnabled(true);
        mChart2.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart2.setOnChartValueSelectedListener(this);

        setData2(2, 100);

        mChart2.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart2.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry((float)a1,"Boys"+"("+Math.round(a1)+")",getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry((float)b1,"Girls"+"("+Math.round(b1)+")",getResources().getDrawable(R.drawable.star)));

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
       /* for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }*/



       /*yVals1.add(new Entry((float)a,0));
        yVals1.add(new Entry((float)b,1));

        //Utils.showToast(getApplicationContext(), String.valueOf(a));


        ArrayList<String> xVals = new ArrayList<String>();


            xVals.add("Boys"+":" +Math.round(a1));
            xVals.add("Girls"+":"+Math.round(b1));

        PieDataSet dataSet = new PieDataSet(yVals1, "Present Students");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);*/

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(0f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private void setData1(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        /*if(c<1)
        {
            entries.add(new PieEntry((float)d,"Girls",getResources().getDrawable(R.drawable.star)));
        }
        else if(d<1)
        {
            entries.add(new PieEntry((float)c,"Boys",getResources().getDrawable(R.drawable.star)));
        }



        else
        {*/
            entries.add(new PieEntry((float)c1,"Boys"+"("+Math.round(c1)+")",getResources().getDrawable(R.drawable.star)));
            entries.add(new PieEntry((float)d1,"Girls"+"("+Math.round(d1)+")",getResources().getDrawable(R.drawable.star)));
        //}







        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(0f);
        /*dataSet.setSelectionShift(5f);
        dataSet.setValueLinePart1Length(0.45f);
        dataSet.setValueLinePart2Length(0.45f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);

        mChart1.setData(data);

        // undo all highlights
        mChart1.highlightValues(null);

        mChart1.invalidate();
    }

    private void setData2(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        /*if(c<1)
        {
            entries.add(new PieEntry((float)d,"Girls",getResources().getDrawable(R.drawable.star)));
        }
        else if(d<1)
        {
            entries.add(new PieEntry((float)c,"Boys",getResources().getDrawable(R.drawable.star)));
        }



        else
        {*/
        entries.add(new PieEntry((float)e1,"Boys"+"("+Math.round(e1)+")",getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry((float)f1,"Girls"+"("+Math.round(f1)+")",getResources().getDrawable(R.drawable.star)));
        //}







        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(0f);
        /*dataSet.setSelectionShift(5f);
        dataSet.setValueLinePart1Length(0.45f);
        dataSet.setValueLinePart2Length(0.45f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);

        mChart2.setData(data);

        // undo all highlights
        mChart2.highlightValues(null);

        mChart2.invalidate();
    }

   /* private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.VISITOR_ANALYSIS_PRIMARY_SCREEN + "?sch_id=" + Preferences.getInstance().schoolId + "&token=" + Preferences.getInstance().token + "&ins_id=" + Preferences.getInstance().institutionId + "&device_id=" + Preferences.getInstance().deviceId+"&value="+"1");
            if (e == null) {
                techerCoordinatorattendanceArrray = null;
            } else {
                techerCoordinatorattendanceArrray = new JSONArray(new String(e.data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (techerCoordinatorattendanceArrray != null) {

        }
    }*/

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);
        a=0;b=0;c=0;d=0;e2=0;f=0;
        total=0;total1=0;total3=0;
        a1=0;b1=0;c1=0;d1=0;e1=0;f1=0;
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_ATTENDANCE_ANALYSIS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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


                       // Log.d("kk",techerCoordinatorattendanceArrray.toString());

                            total = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("total_present"));

                            a1 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("present_boys"));
                            b1 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("present_girls"));

                        if(a1 ==0 || b1==0)
                        {
                            a=0;
                            b=0;
                        }

                        else {

                            a = (a1 / total) * 100;
                            b = (b1 / total) * 100;
                        }

                        /*float cq = (float) 0.0;

                        cq = Float.valueOf((a1/total)*100);

                           Utils.showToast(getApplicationContext(), String.valueOf(a1)+String.valueOf(total)+String.valueOf(cq));*/

                            total1= Float.parseFloat(responseObject.getJSONObject("responseObject").getString("total_absent"));
                            c1 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("absent_boys"));
                            d1 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("absent_girls"));

                        if(c1==0 || d1==0)
                        {
                            c=0;
                            d=0;
                        }

                        else {

                            c = (c1 / total1);
                            d = (d1 / total1);
                        }

                            total3 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("total_leave"));
                            e1 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("leave_boys"));
                            f1 = Float.parseFloat(responseObject.getJSONObject("responseObject").getString("leave_girls"));

                        if(e1==0|| f1==0)
                        {
                           e2=0;
                            f=0;
                        }

                        else {

                            e2 = (e1 / total3);
                            f = (f1 / total3);
                        }

                           openChart();
                            openChart1();
                           openChart2();





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
                params.put("choosed_date",date);
                if(Preferences.getInstance().userRoleId.matches("4")) {
                    params.put("cordinater_id", Preferences.getInstance().userId);
                }
                else
                {

                }

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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());


    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText(String text,float totalStudents) {

        SpannableString s = new SpannableString(text+" "+Math.round(totalStudents));
        s.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        /*s.setSpan(new RelativeSizeSpan(1.7f), 0, 40, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 40, s.length() - 41, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY),40, s.length() - 41, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 40, s.length() - 41, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 40, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 40, s.length(), 0);*/
        return s;
    }

    public class DecimalRemover extends PercentFormatter {

        protected DecimalFormat mFormat;

        public DecimalRemover(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(value < 0) return "";
            return mFormat.format(value);
        }
    }
}
