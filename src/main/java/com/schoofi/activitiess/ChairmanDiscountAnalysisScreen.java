package com.schoofi.activitiess;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
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
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.PercentFormatter1;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChairmanDiscountAnalysisScreen  extends DemoBase implements OnChartValueSelectedListener {

    private JSONArray techerCoordinatorattendanceArrray;
    private ImageView back,class1,calendar1;
    private PieChart mChart,mChart1,mChart2;
    private Typeface tf,tf1;
    float a,b,c,d,e2,f;
    float total,total1,total3;
    float a1,b1,c1,d1,e1,f1;
    public ArrayList<String> classNames1 = new ArrayList<>();
    public ArrayList<String> classNames = new ArrayList<>();
    public ArrayList<Float> pendingFees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chairman_discount_analysis_screen);

       // mChart1 = (PieChart) findViewById(R.id.chart2);
        mChart = (PieChart) findViewById(R.id.chart1);
       // mChart2 = (PieChart) findViewById(R.id.chart3);
        back = (ImageView) findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getChairmanStudentLeaveList();
    }

    protected void openChart()
    {

        mChart.setUsePercentValues(false);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        String Rs;

        Rs = getApplicationContext().getString(R.string.Rs);
        mChart.setCenterText(generateCenterSpannableText("Total Discount", Rs,total));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);


        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(0f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDrawSliceText(true);


        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(2, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(7f);
        l.setYOffset(7f);
       // mChart.getLegend().setWordWrapEnabled(true);

        // Utils.showToast(getApplicationContext(),"kkk");
    }

    /*protected void openChart1()
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
    }*/



    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        /*for (int i = 0; i < pendingFees.size(); i++) {
            //float mult = (range + 1);


            yVals1.add(new BarEntry(i, pendingFees.get(i)));

        }*/

        for(int i=0;i<pendingFees.size();i++)
        {
           entries.add(new PieEntry((float)pendingFees.get(i),classNames.get(i)+classNames1.get(i),getResources().getDrawable(R.drawable.star)));
        }



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
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setIconsOffset(new MPPointF(0,40));
        dataSet.setSelectionShift(0f);
        dataSet.setValueLinePart1Length(0.35f);
        dataSet.setValueLinePart2Length(0.35f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

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

        String Rs;

        Rs = getApplicationContext().getString(R.string.Rs);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter1(Rs));
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(11f);

        data.setValueTypeface(tf);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    /*private void setData1(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        *//*if(c<1)
        {
            entries.add(new PieEntry((float)d,"Girls",getResources().getDrawable(R.drawable.star)));
        }
        else if(d<1)
        {
            entries.add(new PieEntry((float)c,"Boys",getResources().getDrawable(R.drawable.star)));
        }



        else
        {*//*
        entries.add(new PieEntry((float)c1,"Boys"+"("+Math.round(c1)+")",getResources().getDrawable(R.drawable.star)));
        entries.add(new PieEntry((float)d1,"Girls"+"("+Math.round(d1)+")",getResources().getDrawable(R.drawable.star)));
        //}







        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(0f);
        *//*dataSet.setSelectionShift(5f);
        dataSet.setValueLinePart1Length(0.45f);
        dataSet.setValueLinePart2Length(0.45f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*//*

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
    }*/

    private void toa() {
        System.out.println("aaa");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex()+classNames.get((int) h.getX()));

        Intent intent = new Intent(ChairmanDiscountAnalysisScreen.this,ChairmanDiscountClassWise.class);
        intent.putExtra("index",classNames.get((int) h.getX()));
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText(String text, String text1,float totalStudents) {

        SpannableString s = new SpannableString(text+" "+text1+Math.round(totalStudents));
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

    protected void getChairmanStudentLeaveList() {
        //setSupportProgressBarIndeterminateVisibility(true);

        classNames.clear();
        pendingFees.clear();
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_DASHBOARD_ANALYSIS_PIE_CHART_SCREEN/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
                        techerCoordinatorattendanceArrray= new JSONObject(response).getJSONArray("responseObject");

                        int p=0;
                        for(int i=0;i<techerCoordinatorattendanceArrray.length();i++)
                        {
                            total = total+Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_discount"));
                            classNames.add(techerCoordinatorattendanceArrray.getJSONObject(i).getString("discount_type"));
                            classNames1.add("("+techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_stu")+")");
                            pendingFees.add(Float.parseFloat(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_discount")));
                            //p=p+Integer.parseInt(techerCoordinatorattendanceArrray.getJSONObject(i).getString("total_discount"));
                        }


                        openChart();






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
                params.put("session",Preferences.getInstance().session1);

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


}
