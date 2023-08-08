package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.MyAxisValueFormatter;
import com.schoofi.utils.MyValueFormatter;

import com.schoofi.utils.Preferences;
import com.schoofi.utils.ReportXAxisValueFormatter;
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
import java.util.HashMap;
import java.util.Map;


public class TeacherCoordinatorClassWiseAttendanceScreen extends DemoBase implements OnChartValueSelectedListener {

    private BarChart mChart;
    float a,b,c,d,e2,f;
    float total,total1,total3;
    float a1,b1,c1,d1,e1,f1;
    String date;
    public ArrayList<Float> absents = new ArrayList<>();
    public ArrayList<Float> presents = new ArrayList<>();
    public ArrayList<Float> leaves = new ArrayList<>();
    public ArrayList<Float> totalStudents = new ArrayList<>();
    public ArrayList<String> classNames = new ArrayList<>();
    private JSONArray techerCoordinatorattendanceArrray;
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
    Date selectedDate3,selectedDate4;
    String dat;
    Date date3,date4;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_coordinator_class_wise_attendance_screen);

        mChart = (BarChart) findViewById(R.id.chart1);
        date = getIntent().getStringExtra("date");
        dateView = (TextView) findViewById(R.id.txt_count);
        try {
            selectedDate4 = sdf.parse(date);                 // parse input
            dateView.setText(sdf1.format(selectedDate4));    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar1= (ImageView) findViewById(R.id.calendarIcon);
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
        mChart.setOnChartValueSelectedListener(this);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(120);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);


        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);

        // setting data


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        final ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < presents.size(); i++) {
            xVals.add(classNames.get(i));
        }

        IAxisValueFormatter xAxisFormatter = new IndexAxisValueFormatter(classNames);

        xLabels.setValueFormatter(xAxisFormatter);
        xLabels.setGranularity(1);





        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < presents.size(); i++) {

            float val1 = (float) (presents.get(i));
            float val2 = (float) (absents.get(i));
            float val3 = (float) (leaves.get(i));

            yVals1.add(new BarEntry(
                    i,
                    new float[]{val1, val2, val3},
                    getResources().getDrawable(R.drawable.star)));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Class Wise Attendance");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{ "Presents", "Absents", "Leaves" });

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();

        /*BarDataSet set1 = new BarDataSet(yVals1, "Class Wise Attendance");
        set1.setColors(getColors());
        set1.setStackLabels(new String[] { "Presents", "Absents", "Leaves" });
        set1.setValueTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf"));
        set1.setBarSpacePercent(20);
        set1.setValueTextColor(R.color.gray);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf"));
        data.setValueTextSize(12f);

        mChart.setData(data);*/
        //mChart.invalidate();
    }

   /* @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getVals() != null)
        {

            try {
                Intent intent = new Intent(TeacherCoordinatorClassWiseAttendanceScreen.this,TeacherCoordinatorSectionWiseAttendanceScreen.class);
                intent.putExtra("class_id",techerCoordinatorattendanceArrray.getJSONObject(e.getXIndex()).getString("class_ids"));
                intent.putExtra("date",date);

                //Utils.showToast(getApplicationContext(), String.valueOf(e.getXIndex()));
                startActivity(intent);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }

        }
        else
            Log.i("VAL SELECTED", "Value: " + entry.getVal());
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }*/

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        Log.d("lk", String.valueOf(h.getStackIndex()));


        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null) {
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
            try {
                Intent intent = new Intent(TeacherCoordinatorClassWiseAttendanceScreen.this,TeacherCoordinatorSectionWiseAttendanceScreen.class);
                intent.putExtra("class_id",techerCoordinatorattendanceArrray.getJSONObject(Math.round(e.getX())).getString("class_ids"));
                intent.putExtra("date",date);

                //Utils.showToast(getApplicationContext(), String.valueOf(e.getXIndex()));
                startActivity(intent);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    private int[] getColors() {

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < stacksize; i++) {
            colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
        }

        return colors;
    }

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);
        a=0;b=0;c=0;d=0;e2=0;f=0;
        total=0;total1=0;total3=0;
        a1=0;b1=0;c1=0;d1=0;e1=0;f1=0;
        classNames.clear();
        absents.clear();
        presents.clear();
        leaves.clear();
        totalStudents.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_COORDINATOR_ATTENDANCE_ANALYSIS_CLASS_WISE/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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


                           for(int i=0;i<techerCoordinatorattendanceArrray.length();i++)
                           {
                               classNames.add(techerCoordinatorattendanceArrray.getJSONObject(i).getString("class_names"));
                               absents.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_absent")));
                               presents.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_present")));
                               leaves.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_leaves")));
                               totalStudents.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_students")));
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
                    params.put("cordinater_id", Preferences.getInstance().teachId);
                }
                else
                {

                }
                params.put("choosed_date",date);
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
