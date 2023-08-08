package com.schoofi.activitiess;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.schoofi.adapters.StudentResultSpinnerAdapter;
import com.schoofi.adapters.TermAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DayAxisValueFormatter;
import com.schoofi.utils.DemoBase;
import com.schoofi.utils.JSONfunctions;
import com.schoofi.utils.MyAxisValueFormatter;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.ReportXAxisValueFormatter;
import com.schoofi.utils.TeacherStudentResultVO;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;
import com.schoofi.utils.XYMarkerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherStudentResult extends DemoBase implements OnChartValueSelectedListener{


	//{"Marks":[{"no_stu":"1","grade":"A"}]}

	private Spinner spinner;
	private JSONArray teacherStudentResultArray;
	JSONObject jsonobject;
	JSONArray jsonarray,termArray,studentResultSpinnerArray;;
	ArrayList<String> examNameTeacherStudentResult;
	ArrayList<TeacherStudentResultVO> examIdTeacherStudentResult;
	String examId1TeacherStudentResult;
	TeacherStudentResultVO teacherStudentResultVO;
	private BarChart mChart;
	private Typeface tf;
	float mult = 100;
	int total = 0;
	private TextView noOfStudents;
	ImageView back;
	float a,b,c,d,e1;
	int i;
	TextView term,grade;
	ListView listView1,listView2;
	PopupWindow popupWindow1,popupWindow2;
	TermAdapter termAdapter;
	StudentResultSpinnerAdapter studentResultSpinnerAdapter;
	//DialogListAdapter dialogListAdapter;
	int width;
	String termId="";
	public ArrayList<String> classNames = new ArrayList<>();
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_teacher_student_result);

		//spinner = (Spinner) findViewById(R.id.spinTeacherStudentResult);
		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		noOfStudents = (TextView) findViewById(R.id.textNoOfStudents);
		term = (TextView) findViewById(R.id.textNewExam);
		grade = (TextView) findViewById(R.id.textReset);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;

		listView1 = new ListView(getApplicationContext());
		listView2 = new ListView(getApplicationContext());


		popupWindow1 = TermListWindow();
		//new DownloadJSON().execute();

		term.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow1.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			}
		});

		grade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(termId.matches("") || termId.matches("null"))
				{
					Utils.showToast(getApplicationContext(),"Please select the term");
				}

				else
				if(termId.matches("OT"))
				{
					Utils.showToast(getApplicationContext(),"No exams for final term");

				}

				else {

					popupWindow2 = ExamListWindow();
					popupWindow2.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				}
			}
		});

		listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				view.startAnimation(fadeInAnimation);

				popupWindow2.dismiss();

				try {
					grade.setText(studentResultSpinnerArray.getJSONObject(i).getString("exam_name"));
					examId1TeacherStudentResult= studentResultSpinnerArray.getJSONObject(i).getString("exam_id");
					grade.setTextColor(Color.BLACK);



					if(examId1TeacherStudentResult.matches("0"))
					{
						Log.d("hhhh", "kkk");
					}

					else
					{
						//mChart.setVisibility(View.VISIBLE);
						getStudentAnnouncementList();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		});

		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

				Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
				fadeInAnimation.setDuration(10);
				view.startAnimation(fadeInAnimation);

				popupWindow1.dismiss();

				try {
					term.setText(termArray.getJSONObject(position).getString("full_name"));
					termId = termArray.getJSONObject(position).getString("code");

					if(termId.matches("OT"))
					{
						term.setTextColor(Color.BLACK);
						grade.setText("Select Exam");
						grade.setTextColor(Color.GRAY);

						examId1TeacherStudentResult="";
						//mChart.setVisibility(View.VISIBLE);
						getStudentAnnouncementList();
					}

					else
					{
						term.setTextColor(Color.BLACK);
						grade.setText("Select Exam");
						grade.setTextColor(Color.GRAY);
						//mChart.setVisibility(View.INVISIBLE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
		
		
		









	}
	
	protected void openChart() {
		//tvX = (TextView) findViewById(R.id.tvXMax);
		//tvY = (TextView) findViewById(R.id.tvYMax);

		// mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
		//mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

		//mSeekBarY.setProgress(10);

		//mSeekBarX.setOnSeekBarChangeListener(this);
		//mSeekBarY.setOnSeekBarChangeListener(this);

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
        classNames.add(0,"A");
		classNames.add(1,"B");
		classNames.add(2,"C");
		classNames.add(3,"D");
		classNames.add(4,"E");
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
	
	/*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IPieDataSet set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (mChart.isDrawHoleEnabled())
                    mChart.setDrawHoleEnabled(false);
                else
                    mChart.setDrawHoleEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (mChart.isDrawCenterTextEnabled())
                    mChart.setDrawCenterText(false);
                else
                    mChart.setDrawCenterText(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {

                mChart.setDrawSliceText(!mChart.isDrawSliceTextEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
               // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.actionTogglePercent:
                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                mChart.invalidate();
                break;
            case R.id.animateX: {
                mChart.animateX(1400);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(1400, 1400);
                break;
            }
        }
        return true;
    }*/



	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {


			// Create an array to populate the spinner 
			examIdTeacherStudentResult= new ArrayList<TeacherStudentResultVO>();
			examNameTeacherStudentResult = new ArrayList<String>();
			/*examName.add("Select:");
			studentExamVO.setExamId("1");
			examId.add(studentExamVO);*/

			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL(AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.STUDENT_RESULT_EXAM_NAME_LIST+"?sec_id="+Preferences.getInstance().studentSectionId+"&sch_id="+Preferences.getInstance().schoolId+"&cls_id="+Preferences.getInstance().studentClassId+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId);
			try {
				// Locate the NodeList name
				//{"Student_list":[{"percent":"90.50","id":"1","picture":"img\/student_img\/vanshay_2.jpg","stu_name":"Vanshay Kaul"}]}
				jsonarray = jsonobject.getJSONArray("e_Name");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					TeacherStudentResultVO teacherStudentResultVO = new TeacherStudentResultVO();

					//TeacherStudentResultVO.s(jsonobject.optString("exam_id"));
					teacherStudentResultVO.setExamIdTeacherStudentResult(jsonobject.optString("exam_id"));
					examIdTeacherStudentResult.add(teacherStudentResultVO);

					examNameTeacherStudentResult.add(jsonobject.optString("exam_name"));

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

			spinner
			.setAdapter(new ArrayAdapter<String>(TeacherStudentResult.this,
					android.R.layout.simple_spinner_dropdown_item,
					examNameTeacherStudentResult));

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub


					examId1TeacherStudentResult = examIdTeacherStudentResult.get(position).getExamIdTeacherStudentResult().toString();
					System.out.println(examId1TeacherStudentResult);
					total = 0;
					//initData();
					a=0;
					b=0;
					c=0;d=0;
					e1=0;
					getStudentAnnouncementList();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}


			});




		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
       // Utils.showToast(getApplicationContext(), ""+dataSetIndex);
        Intent intent  = new Intent(TeacherStudentResult.this,TeacherStudentResultDetails.class);
        intent.putExtra("index",String.valueOf(dataSetIndex));
        intent.putExtra("examId", examId1TeacherStudentResult);
		intent.putExtra("termId",termId);
        startActivity(intent);
    }





	@Override
    public void onNothingSelected() {
        Log.d("PieChart", "nothing selected");
    }*/
    
    private void initData() 
   	{


   		try
   		{
   			com.android.volley.Cache.Entry e;
   			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_RESULT_LIST+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&exam_id="+examId1TeacherStudentResult+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId);
   			if(e == null)
   			{
   				teacherStudentResultArray= null;
   			}
   			else
   			{
   				teacherStudentResultArray= new JSONArray(new String(e.data));
   			}
   		}
   		catch(JSONException e)
   		{
   			e.printStackTrace();
   		}

   		if(teacherStudentResultArray!= null)
   		{
   			System.out.println("jjj");
   		}
   	}

   	protected void getStudentAnnouncementList() 
   	{
		total = 0;
		//initData();
		a=0;
		b=0;
		c=0;d=0;
		e1=0;
   		setProgressBarIndeterminateVisibility(true);
   		RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
   		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_RESULT_LIST+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&exam_id="+examId1TeacherStudentResult+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1;
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
   							if(responseObject.has("Marks"))
   							{
   								teacherStudentResultArray= new JSONObject(response).getJSONArray("Marks");
   								if(null!=teacherStudentResultArray && teacherStudentResultArray.length()>=0)
   								{

   									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
   									e.data = teacherStudentResultArray.toString().getBytes();
   									VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TEACHER_STUDENT_RESULT_LIST+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&sch_id="+Preferences.getInstance().schoolId+"&exam_id="+examId1TeacherStudentResult+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
   									for(i = 0;i<teacherStudentResultArray.length();i++)
   									{
   										total = total+Integer.parseInt(teacherStudentResultArray.getJSONObject(i).getString("grade").toString());
   										//System.out.println(total);
   										//System.out.println(i);
   										noOfStudents.setText("Total Students:"+total);
   									}
   									
   									if(i == 1)
   									{
   										a = Float.parseFloat(teacherStudentResultArray.getJSONObject(0).getString("grade"))/total;
   									}
   									
   									else
   										if(i == 2)
   										{
   											a = Float.parseFloat(teacherStudentResultArray.getJSONObject(0).getString("grade"))/total;
   											b = Float.parseFloat(teacherStudentResultArray.getJSONObject(1).getString("grade"))/total;
   										}
   									
   										else
   											if(i == 3)
   											{
   												a = Float.parseFloat(teacherStudentResultArray.getJSONObject(0).getString("grade"))/total;
   	   											b = Float.parseFloat(teacherStudentResultArray.getJSONObject(1).getString("grade"))/total;
   	   										    c = Float.parseFloat(teacherStudentResultArray.getJSONObject(2).getString("grade"))/total;
   											}
   									
   											else
   												if(i == 4)
   												{
   													a = Float.parseFloat(teacherStudentResultArray.getJSONObject(0).getString("grade"))/total;
   	   	   											b = Float.parseFloat(teacherStudentResultArray.getJSONObject(1).getString("grade"))/total;
   	   	   										    c = Float.parseFloat(teacherStudentResultArray.getJSONObject(2).getString("grade"))/total;
   	   	   									        d = Float.parseFloat(teacherStudentResultArray.getJSONObject(3).getString("grade"))/total;
   												}
   									
   												else
   													if(i == 5)
   													{
   														a = (Float.parseFloat(teacherStudentResultArray.getJSONObject(0).getString("grade"))/total)*100;
   	   	   	   											b = (Float.parseFloat(teacherStudentResultArray.getJSONObject(1).getString("grade"))/total)*100;
   	   	   	   										    c = (Float.parseFloat(teacherStudentResultArray.getJSONObject(2).getString("grade"))/total)*100;
   	   	   	   									        d = (Float.parseFloat(teacherStudentResultArray.getJSONObject(3).getString("grade"))/total)*100;
   	   	   	   								            e1 = (Float.parseFloat(teacherStudentResultArray.getJSONObject(4).getString("grade"))/total)*100;
   														
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
   				
   				/*try
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
   					}*/
   				



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

	public PopupWindow TermListWindow()
	{
		PopupWindow popupWindow = new PopupWindow(this);



		popupWindow.setFocusable(true);
		popupWindow.setWidth(width);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		initData3();
		getChairmanExamClassList();


		popupWindow.setContentView(listView1);

		return popupWindow;
	}

	public PopupWindow ExamListWindow()
	{
		PopupWindow popupWindow2 = new PopupWindow(this);



		popupWindow2.setFocusable(true);
		popupWindow2.setWidth(width);
		popupWindow2.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		initData4();
		getChairmanExamClassList1();


		popupWindow2.setContentView(listView2);

		return popupWindow2;
	}

	private void initData3()
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1);
			if(e == null)
			{
				termArray= null;
			}
			else
			{
				termArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(termArray!= null)
		{
			termAdapter= new TermAdapter(TeacherStudentResult.this,termArray);
			listView1.setAdapter(termAdapter);
			termAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanExamClassList()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherStudentResult.this).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
		StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				//System.out.println(url);
				try
				{
					responseObject = new JSONObject(response);
					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(TeacherStudentResult.this,"No Records Found");
						//mChart.setVisibility(View.INVISIBLE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(TeacherStudentResult.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("Typelist"))
					{
						//mChart.setVisibility(View.VISIBLE);
						termArray= new JSONObject(response).getJSONArray("Typelist");
						if(null!=termArray && termArray.length()>=0)
						{
							com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
							e.data = termArray.toString().getBytes();
							VolleySingleton.getInstance(TeacherStudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_TERM_TYPE+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+"&device_id="+Preferences.getInstance().deviceId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
							listView1.invalidateViews();
							termAdapter= new TermAdapter(TeacherStudentResult.this,termArray);
							listView1.setAdapter(termAdapter);
							termAdapter.notifyDataSetChanged();
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

				Preferences.getInstance().loadPreference(TeacherStudentResult.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("value","2");

				//params.put("stu_id",Preferences.getInstance().studentId);


				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("board",Preferences.getInstance().board);
				params.put("session",Preferences.getInstance().session1);
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

	private void initData4()
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&sec_id="+Preferences.getInstance().studentSectionId+"&cls_id="+Preferences.getInstance().studentClassId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1);
			if(e == null)
			{
				studentResultSpinnerArray= null;
			}
			else
			{
				studentResultSpinnerArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(studentResultSpinnerArray!= null)
		{
			studentResultSpinnerAdapter= new StudentResultSpinnerAdapter(TeacherStudentResult.this,studentResultSpinnerArray);
			listView2.setAdapter(studentResultSpinnerAdapter);
			studentResultSpinnerAdapter.notifyDataSetChanged();
		}
	}

	protected void getChairmanExamClassList1()
	{
		setProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(TeacherStudentResult.this).getRequestQueue();
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*/;
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
						Utils.showToast(TeacherStudentResult.this,"No Records Found");
						//mChart.setVisibility(View.INVISIBLE);
					}

					else
					if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
					{
						Utils.showToast(TeacherStudentResult.this, "Session Expired:Please Login Again");
					}
					else
					if(responseObject.has("e_Name"))
					{
						//mChart.setVisibility(View.VISIBLE);
						studentResultSpinnerArray= new JSONObject(response).getJSONArray("e_Name");
						if(null!=studentResultSpinnerArray && studentResultSpinnerArray.length()>=0)
						{
							com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
							e.data = studentResultSpinnerArray.toString().getBytes();
							VolleySingleton.getInstance(TeacherStudentResult.this).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.TERM_EXAM_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&ins_id="+Preferences.getInstance().institutionId+"&term="+termId+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&sec_id="+Preferences.getInstance().studentSectionId+"&board="+Preferences.getInstance().board+"&session="+Preferences.getInstance().session1,e);
							listView2.invalidateViews();
							studentResultSpinnerAdapter= new StudentResultSpinnerAdapter(TeacherStudentResult.this,studentResultSpinnerArray);
							listView2.setAdapter(studentResultSpinnerAdapter);
							studentResultSpinnerAdapter.notifyDataSetChanged();
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

				Preferences.getInstance().loadPreference(TeacherStudentResult.this);
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("ins_id",Preferences.getInstance().institutionId);
				params.put("term",termId);

				//params.put("stu_id",Preferences.getInstance().studentId);


				params.put("device_id", Preferences.getInstance().deviceId);
				params.put("cls_id", Preferences.getInstance().studentClassId);
				params.put("sec_id", Preferences.getInstance().studentSectionId);
				params.put("session",Preferences.getInstance().session1);
				params.put("board",Preferences.getInstance().board);
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
		Intent intent  = new Intent(TeacherStudentResult.this,TeacherStudentResultDetails.class);
		intent.putExtra("index",String.valueOf(Math.round(e.getX())));
		intent.putExtra("examId", examId1TeacherStudentResult);
		intent.putExtra("termId",termId);
		startActivity(intent);
	}

	@Override
	public void onNothingSelected() { }
    
   
}
