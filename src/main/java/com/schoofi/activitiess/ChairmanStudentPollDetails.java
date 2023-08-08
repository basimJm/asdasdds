package com.schoofi.activitiess;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DemoBase;
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

public class ChairmanStudentPollDetails extends DemoBase implements OnChartValueSelectedListener{

	private TextView screenTitle,choice1,choice2,choice3,choice4,question,totalPolls,choice11,choice21,choice31,choice41;
	int position;
	private JSONArray studentPollArray;
	private JSONArray chairmanPollGraphArray;
	String qid;
	String studentId = Preferences.getInstance().studentId;
	String userId = Preferences.getInstance().userId;
	String userEmailId = Preferences.getInstance().userEmailId;
	String token = Preferences.getInstance().token;
	String schoolId = Preferences.getInstance().schoolId;
	String userType = Preferences.getInstance().userType;
	private BarChart mChart;
	ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
	ImageView back;
	private Typeface tf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.activity_chairman_student_poll_details);
		
		Tracker t = ((SchoofiApplication)this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentPollDetails");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());
		position = getIntent().getExtras().getInt("position");
		screenTitle = (TextView) findViewById(R.id.txt_chairmanPoll);
		choice1 = (TextView) findViewById(R.id.choice1);
		choice2 = (TextView) findViewById(R.id.choice2);
		choice3 = (TextView) findViewById(R.id.choice3);
		choice4 = (TextView) findViewById(R.id.choice4);
		mChart = (BarChart) findViewById(R.id.chart1);
		question = (TextView) findViewById(R.id.question);
		totalPolls = (TextView) findViewById(R.id.totalPolls);
		choice11 = (TextView) findViewById(R.id.choice11);
		choice21 = (TextView) findViewById(R.id.choice21);
		choice31 = (TextView) findViewById(R.id.choice31);
		choice41 = (TextView) findViewById(R.id.choice41);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		try
		{
			Entry e;
			e = VolleySingleton.getInstance(ChairmanStudentPollDetails.this).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_POLL_LIST+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&u_type="+userType+"&sch_id="+schoolId+"&device_id="+Preferences.getInstance().deviceId);
			studentPollArray= new JSONArray(new String(e.data));
			//System.out.println(studentLeaveListArray);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentPollArray!= null)
		{


			try {
				choice1.setText("Choice A: "+studentPollArray.getJSONObject(position).getString("choice_a"));
				choice2.setText("Choice B: "+studentPollArray.getJSONObject(position).getString("choice_b"));
				choice3.setText("Choice C: "+studentPollArray.getJSONObject(position).getString("choice_c"));
				choice4.setText("Choice D: "+studentPollArray.getJSONObject(position).getString("choice_d"));
				question.setText(studentPollArray.getJSONObject(position).getString("ques"));
				qid = studentPollArray.getJSONObject(position).getString("q_id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		initData();
		getStudentAnnouncementList();
		/*mChart.setDescription("");

		// scaling can now only be done on x- and y-axis separately
		mChart.setPinchZoom(false);

		mChart.setDrawBarShadow(false);
		mChart.setDrawGridBackground(false);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setLabelsToSkip(0);
		xAxis.setDrawGridLines(false);

		mChart.getAxisLeft().setDrawGridLines(false);

		mChart.getLegend().setEnabled(false);
		//mChart.setClickable(false); 
		//mChart.set*/

		//mChart = (BarChart) findViewById(R.id.chart1);
		mChart.setOnChartValueSelectedListener(ChairmanStudentPollDetails.this);
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
		classNames.add(0,"A");
		classNames.add(1,"B");
		classNames.add(2,"C");
		classNames.add(3,"D");

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
		

		
	}

	/*private void setData(int count) {


		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++) {
			//float val = (float) (Math.random() * count) + 15;


		}
		//yVals.add(new BarEntry((float) 20.0, 0));
		//yVals.add(new BarEntry((float) 40.0, 1));
		//yVals.add(new BarEntry((float) 60.0, 2));
		//yVals.add(new BarEntry((float) 80.0, 3));
		//yVals.add(new BarEntry((float) 100.0, 4));
		xVals.add("A");
		xVals.add("B");
		xVals.add("C");
		xVals.add("D");


		BarDataSet set = new BarDataSet(yVals, "Data Set");
		set.setColors(ColorTemplate.VORDIPLOM_COLORS);
		set.setDrawValues(true);

		BarData data = new BarData(xVals, set);

		mChart.setDrawValueAboveBar(true);

		mChart.setData(data);
		mChart.invalidate();
		mChart.animateY(800);
	}
*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chairman_student_poll_details, menu);
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
			Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_POLL_GRAPH+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&ques_id="+qid+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanPollGraphArray= null;
			}
			else
			{
				chairmanPollGraphArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanPollGraphArray!= null)
		{
			System.out.println("jjj");
		}
	}

	protected void getStudentAnnouncementList() 
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_POLL_GRAPH+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&ques_id="+qid+"&device_id="+Preferences.getInstance().deviceId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);

				try 
				{
					responseObject = new JSONObject(response);

					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(getApplicationContext(),"No Records Found");
						System.out.println("hhhhh");
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("poll_Graph"))
							{
								chairmanPollGraphArray= new JSONObject(response).getJSONArray("poll_Graph");
								if(null!=chairmanPollGraphArray && chairmanPollGraphArray.length()>=0)
								{
									Entry e = new Entry();
									e.data = chairmanPollGraphArray.toString().getBytes();
									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_POLL_GRAPH+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&ques_id="+qid+"&device_id="+Preferences.getInstance().deviceId,e);
									int i;
									float total=0;
									float a=0,b=0,c=0,d=0;
									float a1=0,b1=0,c1=0,d1=0;


									for(i=0;i<chairmanPollGraphArray.length();i++)
									{
										

									}
									
									
									//System.out.println(i);

									if(i==1)
									{
										//yVals.add(new BarFloat.parseFloat(studentPollArray.getJSONObject(0).getString("choice").toString()),0);
										if(chairmanPollGraphArray.getJSONObject(0).getString("choice").matches("null"))
										{
											a1 = 0;
										}
										else
										{
											a1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(0).getString("choice").toString());
										}
										
										if(chairmanPollGraphArray.getJSONObject(1).getString("choice").matches("null"))
										{
											b1 = 0;
										}
										else
										{
											b1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(1).getString("choice").toString());
										}
										
										if(chairmanPollGraphArray.getJSONObject(2).getString("choice").matches("null"))
										{
											c1 = 0;
										}
										else
										{
											c1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(2).getString("choice").toString());
										}
										
										if(chairmanPollGraphArray.getJSONObject(3).getString("choice").matches("null"))
										{
											d1 = 0;
										}
										else
										{
											d1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(3).getString("choice").toString());
										}
										
										total = a1+b1+c1+d1;
										a = ((float)a1/total)*100; 
										b = ((float)b1/total)*100;
										c = ((float)c1/total)*100;
										d = ((float)d1/total)*100;
										yVals.add(new BarEntry((float) 0, a));
										yVals.add(new BarEntry((float) 1, b));
										yVals.add(new BarEntry((float) 2, c));
										yVals.add(new BarEntry((float) 3, d));

										BarDataSet set1;

										if (mChart.getData() != null &&
												mChart.getData().getDataSetCount() > 0) {
											set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
											set1.setValues(yVals);

											mChart.getData().notifyDataChanged();
											mChart.notifyDataSetChanged();
										} else {
											set1 = new BarDataSet(yVals, "Poll Analysis");

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
										
										//setData(4);
									}

									else
										if(i == 2)
										{
											if(chairmanPollGraphArray.getJSONObject(0).getString("choice").matches("null"))
											{
												a1 = 0;
											}
											else
											{
												a1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(0).getString("choice").toString());
											}
											
											if(chairmanPollGraphArray.getJSONObject(1).getString("choice").matches("null"))
											{
												b1 = 0;
											}
											else
											{
												b1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(1).getString("choice").toString());
											}
											
											if(chairmanPollGraphArray.getJSONObject(2).getString("choice").matches("null"))
											{
												c1 = 0;
											}
											else
											{
												c1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(2).getString("choice").toString());
											}
											
											if(chairmanPollGraphArray.getJSONObject(3).getString("choice").matches("null"))
											{
												d1 = 0;
											}
											else
											{
												d1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(3).getString("choice").toString());
											}
											total = a1+b1+c1+d1;
											a = ((float)a1/total)*100; 
											b = ((float)b1/total)*100;
											c = ((float)c1/total)*100;
											d = ((float)d1/total)*100;
											yVals.add(new BarEntry((float) 0, a));
											yVals.add(new BarEntry((float) 1, b));
											yVals.add(new BarEntry((float) 2, c));
											yVals.add(new BarEntry((float) 3, d));

											BarDataSet set1;

											if (mChart.getData() != null &&
													mChart.getData().getDataSetCount() > 0) {
												set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
												set1.setValues(yVals);
												mChart.getData().notifyDataChanged();
												mChart.notifyDataSetChanged();
											} else {
												set1 = new BarDataSet(yVals, "Poll Analysis");

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
											
											//setData(4);

										}

										else
											if(i == 3)
											{
												if(chairmanPollGraphArray.getJSONObject(0).getString("choice").matches("null"))
												{
													a1 = 0;
												}
												else
												{
													a1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(0).getString("choice").toString());
												}
												
												if(chairmanPollGraphArray.getJSONObject(1).getString("choice").matches("null"))
												{
													b1 = 0;
												}
												else
												{
													b1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(1).getString("choice").toString());
												}
												
												if(chairmanPollGraphArray.getJSONObject(2).getString("choice").matches("null"))
												{
													c1 = 0;
												}
												else
												{
													c1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(2).getString("choice").toString());
												}
												
												if(chairmanPollGraphArray.getJSONObject(3).getString("choice").matches("null"))
												{
													d1 = 0;
												}
												else
												{
													d1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(3).getString("choice").toString());
												}
												total = a1+b1+c1+d1;
												a = ((float)a1/total)*100; 
												b = ((float)b1/total)*100;
												c = ((float)c1/total)*100;
												d = ((float)d1/total)*100;
												yVals.add(new BarEntry((float) 0, a));
												yVals.add(new BarEntry((float) 1, b));
												yVals.add(new BarEntry((float) 2, c));
												yVals.add(new BarEntry((float) 3, d));
												
												//setData(4);
												BarDataSet set1;

												if (mChart.getData() != null &&
														mChart.getData().getDataSetCount() > 0) {
													set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
													set1.setValues(yVals);
													mChart.getData().notifyDataChanged();
													mChart.notifyDataSetChanged();
												} else {
													set1 = new BarDataSet(yVals, "Poll Analysis");

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
											}

											else
												if(i == 4)
												{
													
													if(chairmanPollGraphArray.getJSONObject(0).getString("choice").matches("null"))
													{
														a1 = 0;
													}
													else
													{
														a1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(0).getString("choice").toString());
													}
													
													if(chairmanPollGraphArray.getJSONObject(1).getString("choice").matches("null"))
													{
														b1 = 0;
													}
													else
													{
														b1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(1).getString("choice").toString());
													}
													
													if(chairmanPollGraphArray.getJSONObject(2).getString("choice").matches("null"))
													{
														c1 = 0;
													}
													else
													{
														c1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(2).getString("choice").toString());
													}
													
													if(chairmanPollGraphArray.getJSONObject(3).getString("choice").matches("null"))
													{
														d1 = 0;
													}
													else
													{
														d1 = Float.parseFloat(chairmanPollGraphArray.getJSONObject(3).getString("choice").toString());
													}
													total = a1+b1+c1+d1;
													totalPolls.setText("Total Votes: "+String.valueOf(total));
													choice11.setText("Choice A: "+chairmanPollGraphArray.getJSONObject(0).getString("choice").toString());
													choice21.setText("Choice B: "+chairmanPollGraphArray.getJSONObject(1).getString("choice").toString());
													choice31.setText("Choice C: "+chairmanPollGraphArray.getJSONObject(2).getString("choice").toString());
													choice41.setText("Choice D: "+chairmanPollGraphArray.getJSONObject(3).getString("choice").toString());
													a = ((float)a1/total)*100; 
													b = ((float)b1/total)*100;
													c = ((float)c1/total)*100;
													d = ((float)d1/total)*100;
													yVals.add(new BarEntry((float) 0, a));
													yVals.add(new BarEntry((float) 1, b));
													yVals.add(new BarEntry((float) 2, c));
													yVals.add(new BarEntry((float) 3, d));
													
													//setData(4);

													BarDataSet set1;

													if (mChart.getData() != null &&
															mChart.getData().getDataSetCount() > 0) {
														set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
														set1.setValues(yVals);
														mChart.getData().notifyDataChanged();
														mChart.notifyDataSetChanged();
													} else {
														set1 = new BarDataSet(yVals, "Poll Analysis");

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

												}
									
									









								}
							}
							else
								Utils.showToast(getApplicationContext(), "Error Fetching Response");
					setProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setProgressBarIndeterminateVisibility(false);
				}






			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getApplicationContext(), "Error fetching modules! Please try after sometime.");
					setProgressBarIndeterminateVisibility(false);
				}
			})
		{
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
   			}*/};		

   			requestObject.setRetryPolicy(new DefaultRetryPolicy(
   					25000, 
   					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
   					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
   			if(Utils.isNetworkAvailable(getApplicationContext()))
   				queue.add(requestObject);
   			else
   			{
   				Utils.showToast(getApplicationContext(), "Unable to fetch data, kindly enable internet settings!");
   			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}

	@Override
	public void onValueSelected(com.github.mikephil.charting.data.Entry e, Highlight h) {

	}

	@Override
	public void onNothingSelected() {

	}
}
