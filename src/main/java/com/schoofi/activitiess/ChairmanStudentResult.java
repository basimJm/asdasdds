package com.schoofi.activitiess;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.adapters.ChairmanClassExamListAdapter;
import com.schoofi.adapters.ChairmanClassListViewAdapter;
import com.schoofi.adapters.DialogListAdapter;
import com.schoofi.adapters.HomeUserAdapter;
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
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;

public class ChairmanStudentResult extends AppCompatActivity implements OnItemClickListener,OnChartValueSelectedListener {


	public TextView examSchedule, classList, classExam, reset;
	PopupWindow popupWindow, popupWindow1, popupWindow2;
	private JSONArray chairmanDialog1ListArray, chairmanPieCharArray, chairmanClassExamListArray;
	private ListView chairmanExamListView, chairmanClassListView, chairmanClassExamListView;
	DialogListAdapter dialogListAdapter;
	ChairmanClassExamListAdapter chairmanClassExamListAdapter;
	ChairmanClassListViewAdapter chairmanClassListViewAdapter;
	private JSONArray chairmanClassListArray;
	String examId, classId, examId1;
	int width;
	int height;
	private BarChart mChart;
	private Typeface tf;
	float mult = 100;
	int total = 0;
	private TextView noOfStudents;
	float a, b, c, d, e1;
	int i;
	int count = 1;
	private DrawerLayout drawerlayout;
	private ActionBarDrawerToggle drawerListener;
	ActionBar actionBar;
	private ListView listView;
	private HomeUserAdapter homeUserAdapter;
	ArrayList<String> permissionArray1 = new ArrayList<String>();
	TextView totalstudents1,totalstudents2,totalstudents3,totalstudents4,totalstudents5,totalStudents6;
	private JSONArray schoolNotificationListArray;
	String url1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE4749")));
		getSupportActionBar().setTitle("Results");

		setContentView(R.layout.activity_chairman_student_result);

		Tracker t = ((SchoofiApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Chairman StudentResult");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		drawerlayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		listView = (ListView) findViewById(R.id.drawerList);
		homeUserAdapter = new HomeUserAdapter(this, permissionArray1);
		listView.setAdapter(homeUserAdapter);
		listView.setOnItemClickListener(this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		drawerListener = new ActionBarDrawerToggle(ChairmanStudentResult.this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub

			}
		};

		drawerlayout.setDrawerListener(drawerListener);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(android.R.color.transparent);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		//drawerlayout.openDrawer(listView);
		//examSchedule = (TextView) findViewById(R.id.textNewExam);
		//reset = (TextView) findViewById(R.id.textReset);
		classExam = (TextView) findViewById(R.id.textSelectExamClassWise);
		classList = (TextView) findViewById(R.id.textClassWise);
		//mChart = (BarChart) findViewById(R.id.chart1);
		totalstudents1 = (TextView) findViewById(R.id.text_no_of_s1);
		totalstudents2 = (TextView) findViewById(R.id.text_no_of_s2);
		totalstudents3 = (TextView) findViewById(R.id.text_no_of_s3);
		totalstudents4 = (TextView) findViewById(R.id.text_no_of_s4);
		totalstudents5 = (TextView) findViewById(R.id.text_no_of_s5);
		totalStudents6 = (TextView) findViewById(R.id.text_no_of_s6);
		//openChart();
		getChairmanStudentResultList();

		classExam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ChairmanStudentResult.this,ChairmanStudentTermExamList.class);
				intent.putExtra("classId","");
				intent.putExtra("sectionId","");
				startActivity(intent);
			}
		});

		classList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ChairmanStudentResult.this,ChairmanStudentResultClassList.class);
				startActivity(intent);
			}
		});

		//classExam.setVisibility(View.INVISIBLE);

		

		/*initData1();
		getChairmanStudentResultList();
		chairmanExamListView = new ListView(getApplicationContext());
		chairmanClassListView = new ListView(getApplicationContext());


		popupWindow = ExamListWindow();
		popupWindow1 = ClassListWindow();


		*//*examSchedule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popupWindow.showAsDropDown(v, -5, 0);


			}
		});*//*

		*//*reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initData1();
				getChairmanStudentResultList();

			}
		});*//*



		classList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popupWindow1.showAtLocation(v, Gravity.BOTTOM, 0, 0);

			}
		});

		chairmanClassListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				v.startAnimation(fadeInAnimation);

				popupWindow1.dismiss();



				try {
					examSchedule.setText("School Wise");
					//examId = chairmanDialog1ListArray.getJSONObject(position).getString("exam_id");

					//Utils.showToast(getApplicationContext(), ""+examId);
					classList.setText(chairmanClassListArray.getJSONObject(position).getString("class_name"));
					classId = chairmanClassListArray.getJSONObject(position).getString("class_id");

					count = 2;
					classExam.setVisibility(View.VISIBLE);
					chairmanClassExamListView = new ListView(getApplicationContext());
					popupWindow2 = ClassExamListWindow();
					
					classExam();
					
					//System.out.println(count);



				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//getChairmanStudentResultList1();

			}
		});



		chairmanExamListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				v.startAnimation(fadeInAnimation);

				popupWindow.dismiss();



				try {
					examSchedule.setText(chairmanDialog1ListArray.getJSONObject(position).getString("exam_name"));
					examId = chairmanDialog1ListArray.getJSONObject(position).getString("exam_id");

					//Utils.showToast(getApplicationContext(), ""+examId);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				getChairmanStudentResultList1();



			}
		});
		
		

		
			

		
	}
	
	protected void classExam()
	{
		classExam.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popupWindow2.showAsDropDown(v, -5, 0);

			}
		});

		chairmanClassExamListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub

				Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				v.startAnimation(fadeInAnimation);

				popupWindow2.dismiss();



				try {
					//examSchedule.setText("School Wise");
					//examId = chairmanDialog1ListArray.getJSONObject(position).getString("exam_id");

					//Utils.showToast(getApplicationContext(), ""+examId);
					//Utils.showToast(getApplicationContext(), "m");
					classExam.setText(chairmanClassExamListArray.getJSONObject(position).getString("exam_name"));
					examId1 = chairmanClassExamListArray.getJSONObject(position).getString("exam_id");

					//classExam.setVisibility(View.VISIBLE);



				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				getChairmanStudentResultList2();

			}
		});*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chairman_student_result, menu);
		return true;
	}

	/*public PopupWindow ExamListWindow()
	{
		PopupWindow popupWindow = new PopupWindow(this);

		initData();
		getTeacherStudentLeaveList();

		popupWindow.setFocusable(true);
		popupWindow.setWidth(width);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


		popupWindow.setContentView(chairmanExamListView);

		return popupWindow;
	}*/

/*	public PopupWindow ClassExamListWindow()
	{
		PopupWindow popupWindow2 = new PopupWindow(this);



		popupWindow2.setFocusable(true);
		popupWindow2.setWidth(width);
		popupWindow2.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		initData3();
		getChairmanExamClassList();


		popupWindow2.setContentView(chairmanClassExamListView);

		return popupWindow2;
	}*/
	/*public PopupWindow ClassListWindow()
	{
		PopupWindow popupWindow1 = new PopupWindow(this);

		initData2();
		getChairmanClassList();

		popupWindow1.setFocusable(true);
		popupWindow1.setWidth(width);
		popupWindow1.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


		popupWindow1.setContentView(chairmanClassListView);

		return popupWindow1;
	}*/

	protected void openChart() {
		//tvX = (TextView) findViewById(R.id.tvXMax);
		//tvY = (TextView) findViewById(R.id.tvYMax);

		// mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
		//mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

		//mSeekBarY.setProgress(10);

		//mSeekBarX.setOnSeekBarChangeListener(this);
		//mSeekBarY.setOnSeekBarChangeListener(this);


		//mChart = (BarChart) findViewById(R.id.chart1);
		/*mChart.setOnChartValueSelectedListener(this);
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
		l.setPosition(LegendPosition.BELOW_CHART_CENTER);
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

		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void getChairmanStudentResultList() {

		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_AVERAGE_RESULT + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1;
		StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.println(response);
				//System.out.println(url);

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
							VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_AVERAGE_RESULT + "?u_id=" + Preferences.getInstance().userId + "&u_email_id=" + Preferences.getInstance().userEmailId + "&token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&session="+Preferences.getInstance().session1, e);
							for (i = 0; i < chairmanPieCharArray.length(); i++) {
								total = total + Integer.parseInt(chairmanPieCharArray.getJSONObject(i).getString("grade").toString());
								//System.out.println(total);
								//System.out.println(i);
								//noOfStudents.setText("Total Students:"+total);

							}

							totalStudents6.setText("Total Students: "+chairmanPieCharArray.getJSONObject(0).getString("no_stu"));

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

							//openChart();


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


	/*private void initData()
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanDialog1ListArray= null;
			}
			else
			{
				chairmanDialog1ListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanDialog1ListArray!= null)
		{
			dialogListAdapter= new DialogListAdapter(ChairmanStudentResult.this,chairmanDialog1ListArray);
			chairmanExamListView.setAdapter(dialogListAdapter);
			dialogListAdapter.notifyDataSetChanged();
		}
	}*/

	/*protected void getTeacherStudentLeaveList()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentResult.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_EXAM_LIST*//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
						mChart.setVisibility(View.INVISIBLE);
						Utils.showToast(ChairmanStudentResult.this,"No Records Found");
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ChairmanStudentResult.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Exam_list"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanDialog1ListArray= new JSONObject(response).getJSONArray("Exam_list");
								if(null!=chairmanDialog1ListArray && chairmanDialog1ListArray.length()>=0)
								{
									com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
									e.data = chairmanDialog1ListArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanStudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanExamListView.invalidateViews();
									dialogListAdapter = new DialogListAdapter(ChairmanStudentResult.this, chairmanDialog1ListArray);
									chairmanExamListView.setAdapter(dialogListAdapter);
									dialogListAdapter.notifyDataSetChanged();	
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
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(ChairmanStudentResult.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("device_id", Preferences.getInstance().deviceId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				return params;
			}};		

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


	private void initData1() 
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_AVERAGE_RESULT+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanPieCharArray= null;
			}
			else
			{
				chairmanPieCharArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanPieCharArray!= null)
		{
			System.out.println("jjj");
		}
	}

	protected void getChairmanStudentResultList() 
	{

		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_AVERAGE_RESULT+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId;
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
						mChart.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Marks"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanPieCharArray= new JSONObject(response).getJSONArray("Marks");
								if(null!=chairmanPieCharArray && chairmanPieCharArray.length()>=0)
								{
									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
									e.data = chairmanPieCharArray.toString().getBytes();
									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_AVERAGE_RESULT+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
									for(i = 0;i<chairmanPieCharArray.length();i++)
									{
										total = total+Integer.parseInt(chairmanPieCharArray.getJSONObject(i).getString("grade").toString());
										//System.out.println(total);
										//System.out.println(i);
										//noOfStudents.setText("Total Students:"+total);
									}

									if(i == 1)
									{
										a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
									}

									else
										if(i == 2)
										{
											a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
											b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
										}

										else
											if(i == 3)
											{
												a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
												b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
												c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
											}

											else
												if(i == 4)
												{
													a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
													b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
													c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
													d = Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade"))/total;
												}

												else
													if(i == 5)
													{
														a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
														b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
														c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
														d = Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade"))/total;
														e1 = Float.parseFloat(chairmanPieCharArray.getJSONObject(4).getString("grade"))/total;

													}

									openChart();






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

				*//*try
	   				{
	   					responseObject = new JSONObject(response);
	   					if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("image"))
	   					{   
	   						String ddd = responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type");
	                           System.out.println(ddd);
	   						count = 1;
	   					}
	   					else if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("video"))
	   					{
	   						count =2;
	   					}
	   					else
	   						if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("audio"))
	   						{
	   							count =3;
	   						}
	   						else
	   							if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("text")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("pdf")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("ppt"))
	   							{
	   								count =4;
	   							}

	   					for(int i=0;i<=studentAnnouncementArray.length();i++)
	   					{
	   						if(studentAnnouncementArray.getJSONObject(0).getString("announcement_type").equals("image"))
	   						{
	   							count = 1;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(1).getString("announcement_type").equals("video"))
	   						{
	   							count = 2;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(2).getString("announcement_type").equals("audio"))
	   						{
	   							count =3;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(3).getString("announcement_type").equals("text")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("ppt")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("pdf"))
	   						{
	   							count =4;
	   						}
	   					}
	   				}
	   					catch (Exception e) {
	   						// TODO: handle exception
	   					}*//*




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
			*//*@Override
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
	   			}*//*};

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
	}	*/
	/*protected void getChairmanStudentResultList1()
	{
		chairmanPieCharArray = null;
		a=0;
		b=0;
		c=0;
		d=0;
		e1=0;
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_EXAM_WISE_RESULT+"?u_id="+Preferences.getInstance().userId+"&exam_id="+examId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId;
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
						//System.out.println("hhhhh");
						mChart.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Marks"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanPieCharArray= new JSONObject(response).getJSONArray("Marks");
								if(null!=chairmanPieCharArray && chairmanPieCharArray.length()>=0)
								{
									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
									e.data = chairmanPieCharArray.toString().getBytes();
									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_EXAM_WISE_RESULT+"?u_id="+Preferences.getInstance().userId+"&exam_id="+examId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId,e);
									for(i = 0;i<chairmanPieCharArray.length();i++)
									{
										total = total+Integer.parseInt(chairmanPieCharArray.getJSONObject(i).getString("grade").toString());
										//System.out.println(total);
										//System.out.println(i);
										//noOfStudents.setText("Total Students:"+total);
									}

									if(i == 1)
									{
										a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
									}

									else
										if(i == 2)
										{
											a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
											b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
										}

										else
											if(i == 3)
											{
												a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
												b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
												c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
											}

											else
												if(i == 4)
												{
													a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
													b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
													c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
													d = Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade"))/total;
												}

												else
													if(i == 5)
													{
														a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
														b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
														c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
														d = Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade"))/total;
														e1 = Float.parseFloat(chairmanPieCharArray.getJSONObject(4).getString("grade"))/total;

													}

									openChart();






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

				*//*try
	   				{
	   					responseObject = new JSONObject(response);
	   					if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("image"))
	   					{   
	   						String ddd = responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type");
	                           System.out.println(ddd);
	   						count = 1;
	   					}
	   					else if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("video"))
	   					{
	   						count =2;
	   					}
	   					else
	   						if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("audio"))
	   						{
	   							count =3;
	   						}
	   						else
	   							if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("text")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("pdf")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("ppt"))
	   							{
	   								count =4;
	   							}

	   					for(int i=0;i<=studentAnnouncementArray.length();i++)
	   					{
	   						if(studentAnnouncementArray.getJSONObject(0).getString("announcement_type").equals("image"))
	   						{
	   							count = 1;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(1).getString("announcement_type").equals("video"))
	   						{
	   							count = 2;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(2).getString("announcement_type").equals("audio"))
	   						{
	   							count =3;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(3).getString("announcement_type").equals("text")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("ppt")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("pdf"))
	   						{
	   							count =4;
	   						}
	   					}
	   				}
	   					catch (Exception e) {
	   						// TODO: handle exception
	   					}*//*




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
			*//*@Override
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
	   			}*//*};

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

	private void initData3() 
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_CLASS_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanClassExamListArray= null;
			}
			else
			{
				chairmanClassExamListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanClassExamListArray!= null)
		{
			chairmanClassExamListAdapter= new ChairmanClassExamListAdapter(ChairmanStudentResult.this,chairmanClassExamListArray);
			chairmanClassExamListView.setAdapter(chairmanClassExamListAdapter);
			chairmanClassExamListAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanExamClassList() 
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentResult.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_CLASS_EXAM_LIST*//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
						Utils.showToast(ChairmanStudentResult.this,"No Records Found");
						mChart.setVisibility(View.INVISIBLE);
					}

					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ChairmanStudentResult.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Exam_list"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanClassExamListArray= new JSONObject(response).getJSONArray("Exam_list");
								if(null!=chairmanClassExamListArray && chairmanClassExamListArray.length()>=0)
								{
									com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
									e.data = chairmanClassExamListArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanStudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_CLASS_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanClassExamListView.invalidateViews();
									chairmanClassExamListAdapter = new ChairmanClassExamListAdapter(ChairmanStudentResult.this, chairmanClassExamListArray);
									chairmanClassExamListView.setAdapter(chairmanClassExamListAdapter);
									chairmanClassExamListAdapter.notifyDataSetChanged();	
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
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(ChairmanStudentResult.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id", classId);
				params.put("device_id", Preferences.getInstance().deviceId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				return params;
			}};		

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

	private void initData2() 
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanClassListArray= null;
			}
			else
			{
				chairmanClassListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanClassListArray!= null)
		{
			chairmanClassListViewAdapter= new ChairmanClassListViewAdapter(ChairmanStudentResult.this,chairmanClassListArray);
			chairmanClassListView.setAdapter(chairmanClassListViewAdapter);
			chairmanClassListViewAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanClassList() 
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(ChairmanStudentResult.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_CLASS_LIST*//*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
						Utils.showToast(ChairmanStudentResult.this,"No Records Found");
						mChart.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(ChairmanStudentResult.this, "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Class_list"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanClassListArray= new JSONObject(response).getJSONArray("Class_list");
								if(null!=chairmanClassListArray && chairmanClassListArray.length()>=0)
								{
									com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
									e.data = chairmanClassListArray.toString().getBytes();
									VolleySingleton.getInstance(ChairmanStudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_SCHOOL_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanClassListView.invalidateViews();
									chairmanClassListViewAdapter = new ChairmanClassListViewAdapter(ChairmanStudentResult.this, chairmanClassListArray);
									chairmanClassListView.setAdapter(chairmanClassListViewAdapter);
									chairmanClassListViewAdapter.notifyDataSetChanged();	
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
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(ChairmanStudentResult.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("device_id", Preferences.getInstance().deviceId);
				//params.put("cls_id", classId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				return params;
			}};		

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

	protected void getChairmanStudentResultList2() 
	{

		chairmanPieCharArray = null;
		a=0;
		b=0;
		c=0;
		d=0;
		e1=0;

		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_CLASS_RESULT+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId;
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
						mChart.setVisibility(View.INVISIBLE);
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Marks"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanPieCharArray= new JSONObject(response).getJSONArray("Marks");
								if(null!=chairmanPieCharArray && chairmanPieCharArray.length()>=0)
								{
									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
									e.data = chairmanPieCharArray.toString().getBytes();
									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_CLASS_RESULT+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+classId+"&exam_id="+examId1+"&device_id="+Preferences.getInstance().deviceId,e);
									for(i = 0;i<chairmanPieCharArray.length();i++)
									{
										total = total+Integer.parseInt(chairmanPieCharArray.getJSONObject(i).getString("grade").toString());
										//System.out.println(total);
										//System.out.println(i);
										//noOfStudents.setText("Total Students:"+total);
									}

									if(i == 1)
									{
										a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
									}

									else
										if(i == 2)
										{
											a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
											b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
										}

										else
											if(i == 3)
											{
												a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
												b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
												c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
											}

											else
												if(i == 4)
												{
													a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
													b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
													c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
													d = Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade"))/total;
												}

												else
													if(i == 5)
													{
														a = Float.parseFloat(chairmanPieCharArray.getJSONObject(0).getString("grade"))/total;
														b = Float.parseFloat(chairmanPieCharArray.getJSONObject(1).getString("grade"))/total;
														c = Float.parseFloat(chairmanPieCharArray.getJSONObject(2).getString("grade"))/total;
														d = Float.parseFloat(chairmanPieCharArray.getJSONObject(3).getString("grade"))/total;
														e1 = Float.parseFloat(chairmanPieCharArray.getJSONObject(4).getString("grade"))/total;

													}

									openChart();






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

				*//*try
	   				{
	   					responseObject = new JSONObject(response);
	   					if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("image"))
	   					{   
	   						String ddd = responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type");
	                           System.out.println(ddd);
	   						count = 1;
	   					}
	   					else if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("video"))
	   					{
	   						count =2;
	   					}
	   					else
	   						if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("audio"))
	   						{
	   							count =3;
	   						}
	   						else
	   							if(responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("text")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("pdf")||responseObject.getJSONArray("Announcement").getJSONObject(0).getString("announcement_type").equals("ppt"))
	   							{
	   								count =4;
	   							}

	   					for(int i=0;i<=studentAnnouncementArray.length();i++)
	   					{
	   						if(studentAnnouncementArray.getJSONObject(0).getString("announcement_type").equals("image"))
	   						{
	   							count = 1;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(1).getString("announcement_type").equals("video"))
	   						{
	   							count = 2;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(2).getString("announcement_type").equals("audio"))
	   						{
	   							count =3;
	   						}
	   						else if(studentAnnouncementArray.getJSONObject(3).getString("announcement_type").equals("text")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("ppt")||studentAnnouncementArray.getJSONObject(i).getString("announcement_type").equals("pdf"))
	   						{
	   							count =4;
	   						}
	   					}
	   				}
	   					catch (Exception e) {
	   						// TODO: handle exception
	   					}*//*




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
			*//*@Override
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
	   			}*//*};

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
	}	*/


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK))
		{
			Intent intent = new Intent(ChairmanStudentResult.this,ChairmanDashboard.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(ChairmanStudentResult.this,ChairmanDashboard.class);
		startActivity(intent);
		this.finish();
	}
	

	

	
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}



	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub


		if(Preferences.getInstance().userRoleId.matches("7"))
		{


		switch(position)
		{
		case 0 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
		case 1:
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
			
			

		case 2:
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 3 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
			startActivity(intent);
			finish();

			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
		{

			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
			intent.putExtra("url", "https://www.schoofi.com/");
			intent.putExtra("position",0);
			startActivity(intent);
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
		{
			getStudentFeedList();
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
			Preferences.getInstance().isLoggedIn = false;
			Preferences.getInstance().savePreference(getApplicationContext());
			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

			startActivity(intent);
			finish();
			break;
		}

		else
		if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
		{
			Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

			startActivity(intent);
			finish();
			break;
		}

			break;

		case 4 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 5 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

			case 6 :
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;


		case 7 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 8 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 9 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 10 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

		case 11 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;
			
		case 12 :
			if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
				startActivity(intent);
				finish();

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
			{

				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
				intent.putExtra("url", "https://www.schoofi.com/");
				intent.putExtra("position",0);
				startActivity(intent);
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
			{
				getStudentFeedList();
			}


			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
				Preferences.getInstance().isLoggedIn = false;
				Preferences.getInstance().savePreference(getApplicationContext());
				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

				startActivity(intent);
				finish();
				break;
			}

			else
			if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
			{
				Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

				startActivity(intent);
				finish();
				break;
			}

			break;

			case 13:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 14:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 15:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 16:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 17:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

			case 18:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

				break;

			case 19:
				if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}


				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{

					break;
				}

				break;

		default:

			break;
		
		}
		}
		
		else
		{
			switch(position)
			{
				case 0 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;
				case 1:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;



				case 2:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 3 :if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
					startActivity(intent);
					finish();

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
				{

					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
					intent.putExtra("url", "https://www.schoofi.com/");
					intent.putExtra("position",0);
					startActivity(intent);
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
				{
					getStudentFeedList();
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
					Preferences.getInstance().isLoggedIn = false;
					Preferences.getInstance().savePreference(getApplicationContext());
					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

					startActivity(intent);
					finish();
					break;
				}

				else
				if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
				{
					Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

					startActivity(intent);
					finish();
					break;
				}

					break;

				case 4 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 5 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 6 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;


				case 7 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 8 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 9 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 10 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 11 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 12 :
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 13:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 14:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 15:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 16:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 17:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, HealthAndAuditHomeScreen.class);

						startActivity(intent);
						finish();
						break;
					}

					break;

				case 18:
					if(homeUserAdapter.partialNameArray1.get(position).matches("SWITCH SCHOOL")) {
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanHomeScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ATTENDANCE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanSchoolAttendance.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFees.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("RESULTS"))
					{

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("DASHBOARD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanDashboard.class);
						startActivity(intent);
						finish();

						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("POLL"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentPoll.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("EVENTS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentEvents.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LEAVE REQUEST"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentLeaves.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ANNOUNCEMENTS/NOTICES"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentAnnouncement.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("FEEDBACK"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanStudentFeedBack.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("BUS TRACKING"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanBusListScreen.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("NOTIFICATIONS"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, NotificationIntentClass.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("PROFILE"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanProfile.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT US"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
						intent.putExtra("url", "https://www.schoofi.com/");
						intent.putExtra("position",0);
						startActivity(intent);
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("ABOUT SCHOOL"))
					{
						getStudentFeedList();
					}


					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("CHANGE PASSWORD"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChangePassword.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HELP"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ContactUs.class);
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("LOGOUT"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, LoginScreen.class);
						Preferences.getInstance().isLoggedIn = false;
						Preferences.getInstance().savePreference(getApplicationContext());
						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("YEARLY PLANNER"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this, ChairmanYearlyPlanner.class);

						startActivity(intent);
						finish();
						break;
					}

					else
					if(homeUserAdapter.partialNameArray1.get(position).matches("HEALTH AND SAFETY"))
					{

						break;
					}

					break;
			default:

				break;
			}
		}

	}
	
	 protected String[] mMonths = new String[] {
	            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
	    };

	    protected String[] mParties = new String[] {
	            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
	            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
	            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
	            "Party Y", "Party Z"
	    };






	/*@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

	}

	@Override
		public void onNothingSelected() {
			// TODO Auto-generated method stub
			
		}*/


	protected void getStudentFeedList()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_WEBSITE_URL + "?ins_id=" + Preferences.getInstance().institutionId+"&sch_id=" + Preferences.getInstance().schoolId;
		StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					//toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Intent intent = new Intent(ChairmanStudentResult.this,AboutSchool.class);
						startActivity(intent);
					}
					//Utils.showToast(A,"No Records Found");
					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("school"))
					{
						schoolNotificationListArray= new JSONObject(response).getJSONArray("school");
						if(null!=schoolNotificationListArray && schoolNotificationListArray.length()>=0)
						{
							Cache.Entry e = new Cache.Entry();
							e.data = schoolNotificationListArray.toString().getBytes();
							VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.ABOUT_SCHOOL_LIST+"?u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId+"&sch_id="+Preferences.getInstance().schoolId,e);
							url1 = responseObject.getJSONArray("school").getJSONObject(0).getString("school_website_url");
							if(url1.matches("") || url1.matches("null"))
							{
								Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
							}

							else {

								Intent intent = new Intent(ChairmanStudentResult.this, WebViewActivity.class);
								intent.putExtra("url", url1);
								intent.putExtra("position",0);
								startActivity(intent);
							}

						}
					}
					else
						Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
					setSupportProgressBarIndeterminateVisibility(false);

				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
					setSupportProgressBarIndeterminateVisibility(false);
				}

			}}, new Response.ErrorListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				Utils.showToast(getApplicationContext(), "Not Able to Load Website Please Try After Sometime");
				setSupportProgressBarIndeterminateVisibility(false);
			}
		})
		{
			/*@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<String, String>();
				params.put("student_ID",Preferences.getInstance().studentId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				//params.put("crr_date",currentDate);
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


	@Override
	public void onValueSelected(Entry e, Highlight h) {

	}

	@Override
	public void onNothingSelected() {

	}
}
