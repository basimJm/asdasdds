package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
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
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CategoryStudentAnalysisVO;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.JSONfunctions;
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

import smtchahal.materialspinner.MaterialSpinner;

public class ChairmanEmployeeAnalysis extends DemoBase implements OnChartValueSelectedListener {

    private MaterialSpinner departmentType;
    private String departmentId1="";
    ArrayList<String> departmentName;
    ImageView back,calendarIcon,more;
    ArrayList<CategoryStudentAnalysisVO> departmentId;
    JSONObject jsonobject,jsonobject1;
    JSONArray jsonarray,jsonarray1;
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
    float total,total1,total3,totalEmployees;
    float a1,b1,c1,d1,e1,f1;
    public ArrayList<Float> absents = new ArrayList<>();
    public ArrayList<Float> presents = new ArrayList<>();
    public ArrayList<Float> leaves = new ArrayList<>();
    public ArrayList<Float> totalStudents = new ArrayList<>();
    public ArrayList<String> departmentsName = new ArrayList<>();
    private JSONArray chairmanEmployeeAttendanceArray;
    String from1,to1;
    int value;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chairman_employee_analysis);

        departmentType = (MaterialSpinner) findViewById(R.id.spinner_category);
        departmentType.setBackgroundResource(R.drawable.grey_button);
        mChart = (PieChart) findViewById(R.id.chart1);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        more = (ImageView) findViewById(R.id.img_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChairmanEmployeeAnalysis.this,ChairmanEmployeeWithoutGraphAnalysisScreen.class);
                intent.putExtra("value","1");
                startActivity(intent);






            }
        });


        new DownloadJSON1().execute();

        calendarIcon = (ImageView) findViewById(R.id.img_calender);

        calendarIcon.setOnClickListener(new View.OnClickListener() {
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

        value = getIntent().getIntExtra("value",1);
        if(value ==2)
        {
            from1 = getIntent().getStringExtra("startingDate");
            to1 = getIntent().getStringExtra("endingDate");
        }

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
            selectedDate1 = sdf.format(selectedDate3);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        selectedDate2 = sdf1.format(selectedDate3);

        //selectedDate.setText(selectedDate2);
        getChairmanStudentLeaveList1();











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

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Preferences.getInstance().loadPreference(getApplicationContext());


            // Create an array to populate the spinner
            departmentId = new ArrayList<CategoryStudentAnalysisVO>();
            departmentName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Log.d("URL", AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_DEPARTMENT_TYPE+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.EMPLOYEE_DEPARTMENT_TYPE+"?ins_id="+Preferences.getInstance().institutionId+"&sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
            try {
                // Locate the NodeList name
                jsonarray1 = jsonobject1.getJSONArray("Departmentlist");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //ClassVO classVO = new ClassVO();
                    CategoryStudentAnalysisVO categoryStudentAnalysisVO = new CategoryStudentAnalysisVO();

                    categoryStudentAnalysisVO.setCategoryId(jsonobject1.optString("dept_id"));
                    departmentId.add(categoryStudentAnalysisVO);

                    departmentName.add(jsonobject1.optString("dept_name"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            departmentType
                    .setAdapter(new ArrayAdapter<String>(ChairmanEmployeeAnalysis.this,
                            android.R.layout.simple_spinner_dropdown_item,departmentName
                    ));

            departmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    departmentId1 = departmentId.get(position).getCategoryId().toString();

                    System.out.println(departmentId1);
                    if(value== 1)
                    {
                        getChairmanStudentLeaveList();
                    }

                    else
                    {
                        getChairmanStudentLeaveList();
                    }












                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    protected void openChart()
    {

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText("TOTAL ATTENDANCE",(totalEmployees+1)));

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





    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry((float)total,"Presents"+"("+Math.round(total)+")",getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry((float)total1,"Absents"+"("+Math.round(total1)+")",getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry((float)total3,"Leaves"+"("+Math.round(total3)+")",getResources().getDrawable(R.drawable.star)));



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





    protected void getChairmanStudentLeaveList1() {
        //setSupportProgressBarIndeterminateVisibility(true);
        a=0;b=0;c=0;d=0;e2=0;f=0;
        total=0;total1=0;total3=0;
        a1=0;b1=0;c1=0;d1=0;e1=0;f1=0;
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                Log.d("op",response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        //chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("Employee_Attendance")) {


                        // Log.d("kk",techerCoordinatorattendanceArrray.toString());
                        chairmanEmployeeAttendanceArray= new JSONObject(response).getJSONArray("Employee_Attendance");
                        Cache.Entry e = new Cache.Entry();
                        e.data = chairmanEmployeeAttendanceArray.toString().getBytes();
                        VolleySingleton.getInstance(ChairmanEmployeeAnalysis.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&dept_id="+Preferences.getInstance().departmentId+"&from_dt="+selectedDate1+"&to_dt="+selectedDate1,e);

                        if(null!=chairmanEmployeeAttendanceArray && chairmanEmployeeAttendanceArray.length()>=0) {

                            for (int i = 0; i <chairmanEmployeeAttendanceArray.length();i++)
                            {
                                if(chairmanEmployeeAttendanceArray.getJSONObject(i).getString("attendance").matches("P"))
                                {
                                    total = total+1;
                                }
                                else
                                if(chairmanEmployeeAttendanceArray.getJSONObject(i).getString("attendance").matches("A"))
                                {
                                    total1 = total1+1;
                                }
                                else
                                {
                                    total3 = total3+1;
                                }
                                totalEmployees = i;
                                //total = Float.parseFloat(chairmanEmployeeAttendanceArray.getJSONObject(i).getString("total_present"));
                            }






                            if (total == 0) {
                                a = 0;

                            } else {

                                a = (total / (totalEmployees+1)) * 100;

                            }




                            if (total1 == 0) {
                                c = 0;

                            } else {

                                c = (total1 / (totalEmployees+1))*100;

                            }




                            if (total3 == 0) {
                                e2 = 0;

                            } else {

                                e2 = (total3 / (totalEmployees+1))*100;

                            }

                            openChart();

                        }
                        else
                        {

                        }






                    }
                    else
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
                System.out.print(selectedDate1);
                params.put("from_dt",selectedDate1);
                params.put("to_dt",selectedDate1);
                params.put("dept_id",departmentId1);

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

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);
        a=0;b=0;c=0;d=0;e2=0;f=0;
        total=0;total1=0;total3=0;
        a1=0;b1=0;c1=0;d1=0;e1=0;f1=0;
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                Log.d("op",response);
                //System.out.println(url);
                try {
                    responseObject = new JSONObject(response);
                    toa();
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                        Utils.showToast(getApplicationContext(), "No Records Found");
                        //chairmanAssignmentClassSectionWiseAnalysis.setVisibility(View.INVISIBLE);
                    } else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("Employee_Attendance")) {


                        // Log.d("kk",techerCoordinatorattendanceArrray.toString());
                        chairmanEmployeeAttendanceArray= new JSONObject(response).getJSONArray("Employee_Attendance");
                        Cache.Entry e = new Cache.Entry();
                        e.data = chairmanEmployeeAttendanceArray.toString().getBytes();
                        VolleySingleton.getInstance(ChairmanEmployeeAnalysis.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_EMPLOYEE_ATTENDANCE_GRAPH+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&dept_id="+Preferences.getInstance().departmentId+"&from_dt="+currentDate+"&to_dt="+currentDate,e);


                        if(null!=chairmanEmployeeAttendanceArray && chairmanEmployeeAttendanceArray.length()>=0) {

                            for (int i = 0; i <chairmanEmployeeAttendanceArray.length();i++)
                            {
                                if(chairmanEmployeeAttendanceArray.getJSONObject(i).getString("attendance").matches("P"))
                                {
                                    total = total+1;
                                }
                                else
                                    if(chairmanEmployeeAttendanceArray.getJSONObject(i).getString("attendance").matches("A"))
                                    {
                                        total1 = total1+1;
                                    }
                                    else
                                    {
                                        total3 = total3+1;
                                    }
                                    totalEmployees = i;
                                //total = Float.parseFloat(chairmanEmployeeAttendanceArray.getJSONObject(i).getString("total_present"));
                            }






                            if (total == 0) {
                                a = 0;

                            } else {

                                a = (total / (totalEmployees+1)) * 100;

                            }




                            if (total1 == 0) {
                                c = 0;

                            } else {

                                c = (total1 / (totalEmployees+1))*100;

                            }




                            if (total3 == 0) {
                                e2 = 0;

                            } else {

                                e2 = (total3 / (totalEmployees+1))*100;

                            }

                            openChart();

                        }
                        else
                        {

                        }






                    }
                    else
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
                params.put("from_dt",currentDate);
                params.put("to_dt",currentDate);
                params.put("dept_id",departmentId1);

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
        if(selectedDate1==null)
        {
           Intent intent = new Intent(ChairmanEmployeeAnalysis.this,ChairmanEmployeeAnalysisDetails.class);
           intent.putExtra("value",String.valueOf(Math.round(h.getX()+1)));
           intent.putExtra("currentDate",currentDate);
           intent.putExtra("dept_id",departmentId1);
           startActivity(intent);
        }

        else
        {
            Intent intent = new Intent(ChairmanEmployeeAnalysis.this,ChairmanEmployeeAnalysisDetails.class);
            intent.putExtra("value",String.valueOf(Math.round(h.getX()+1)));
            intent.putExtra("currentDate",selectedDate1);
            intent.putExtra("dept_id",departmentId1);
            startActivity(intent);
        }


    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText(String text, float totalStudents) {

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
