package com.schoofi.activitiess;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.CategoryStudentAnalysisVO;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.MyAxisValueFormatter;
import com.schoofi.utils.MyValueFormatter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VisitorTypeVO;
import com.schoofi.utils.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smtchahal.materialspinner.MaterialSpinner;


public class StudentAnalysis extends DemoBase implements OnChartValueSelectedListener {

    private MaterialSpinner categoryType;
    private String categoryId1="";
    ArrayList<String> visitorName;
    ImageView back;
    ArrayList<CategoryStudentAnalysisVO> visitorId;
    JSONObject jsonobject,jsonobject1;
    JSONArray jsonarray,jsonarray1;
    private BarChart mChart;
    public ArrayList<Float> absents = new ArrayList<>();
    public ArrayList<Float> presents = new ArrayList<>();
    //public ArrayList<Float> leaves = new ArrayList<>();
    public ArrayList<Float> totalStudents = new ArrayList<>();
    public ArrayList<String> classNames = new ArrayList<>();
    private JSONArray techerCoordinatorattendanceArrray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();
        setContentView(R.layout.activity_student_analysis);

        categoryType = (MaterialSpinner) findViewById(R.id.spinner_category);
        categoryType.setBackgroundResource(R.drawable.grey_button);
        mChart = (BarChart) findViewById(R.id.chart1);

        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new DownloadJSON1().execute();


    }

    private class DownloadJSON1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            // Create an array to populate the spinner
            visitorId = new ArrayList<CategoryStudentAnalysisVO>();
            visitorName = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

            // JSON file URL address
            Log.d("URL", AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CATEGORY_TYPE);
            jsonobject1 = JSONfunctions
                    .getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CATEGORY_TYPE);
            try {
                // Locate the NodeList name
                jsonarray1 = jsonobject1.getJSONArray("responseObject");
                for (int i = 0; i < jsonarray1.length(); i++) {
                    jsonobject1 = jsonarray1.getJSONObject(i);
                    //StudentExamVO studentExamVO1 = new StudentExamVO();
                    //ClassVO classVO = new ClassVO();
                    CategoryStudentAnalysisVO categoryStudentAnalysisVO = new CategoryStudentAnalysisVO();

                    categoryStudentAnalysisVO.setCategoryId(jsonobject1.optString("id"));
                    visitorId.add(categoryStudentAnalysisVO);

                    visitorName.add(jsonobject1.optString("name"));

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

            categoryType
                    .setAdapter(new ArrayAdapter<String>(StudentAnalysis.this,
                            android.R.layout.simple_spinner_dropdown_item,visitorName
                    ));

            categoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub


                    categoryId1 = visitorId.get(position).getCategoryId().toString();
                    //System.out.println(groupId1);

                    getChairmanStudentLeaveList();











                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }


            });




        }
    }

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);

        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_ANALYSIS/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
        StringRequest requestObject = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                classNames.clear();
                absents.clear();
                presents.clear();
               // leaves.clear();
               // totalStudents.clear();

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
                                classNames.add(techerCoordinatorattendanceArrray.getJSONObject(i).getString("type"));
                                absents.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("boys")));
                                presents.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("girls")));
                                //leaves.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_leaves")));
                                //totalStudents.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_students")));
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
                params.put("filter_value",categoryId1);

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

    protected void openChart()
    {
        mChart.setOnChartValueSelectedListener(this);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(20);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setLabelRotationAngle(25f);

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

        ArrayList<String> xVals = new ArrayList<String>();
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
           // float val3 = (float) (leaves.get(i));

            yVals1.add(new BarEntry(
                    i,
                    new float[]{val1, val2},
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
            set1 = new BarDataSet(yVals1, "Student Analysis");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{ "Girls","Boys" });

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.BLACK);

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
        // mChart.invalidate();
    }

  /*  @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getVals() != null)
        {

            *//*try {
                Intent intent = new Intent(TeacherCoordinatorClassWiseAttendanceScreen.this,TeacherCoordinatorSectionWiseAttendanceScreen.class);
                intent.putExtra("class_id",techerCoordinatorattendanceArrray.getJSONObject(e.getXIndex()).getString("class_ids"));
                intent.putExtra("date",date);

                //Utils.showToast(getApplicationContext(), String.valueOf(e.getXIndex()));
                startActivity(intent);
            } catch (JSONException e3) {
                e3.printStackTrace();
            }*//*

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

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < stacksize; i++) {
            colors[i] = ColorTemplate.VORDIPLOM_COLORS[i];
        }

        return colors;
    }
}
