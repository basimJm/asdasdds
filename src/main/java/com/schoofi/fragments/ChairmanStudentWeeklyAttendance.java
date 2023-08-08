/*
package com.schoofi.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.schoofi.activitiess.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.schoofi.activitiess.ChairmanSchoolAttendance;
import com.schoofi.adapters.ChairmanDailySectionListAdapter;
import com.schoofi.adapters.ChairmanStudentDailyClassListAdapter;
import com.schoofi.adapters.ChairmanStudentWeeklyClassListAdapter;
import com.schoofi.adapters.ChairmanWeeklySectionListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ChairmanStudentWeeklyAttendance extends Fragment implements OnChartValueSelectedListener{
	
	public static final String ARG_PAGE = "ARG_PAGE";
	private JSONArray chairmanStudentWeeklyAttendanceArray;
	private JSONArray chairmanClass1ListArray;
	private PieChart mChart;
	private Typeface tf;
	float mult = 100;
	String classId;
	int total = 0,mP=0,fP=0,tP=0,mA=0,fA=0,tA=0,mL=0,fL=0,tL=0;
	int total1 =0;
	private TextView noOfStudents,selectClass,selectSection,schoolWise,malesP,femalesP,malesA,femalesA,malesL,femalesL,totalP,totalA,totalL;
	//String totalClassStudents,totalSectionStudents;
	float mP1,mA1,mL1,mP2,mA2,mL2,fP1,fP2,fP3,tP2,tA2,tL2,mP3,mA3,mL3,fA1,fA2,fA3,fL1,fL2,fL3;
	double finalValue=0,finalValue1=0,finalValue2=0,finalValue3=0,finalValue4=0,finalValue5=0;
	
	String crrDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	float a,b,c,d,e1;
	int i;
	private int mPage;
	PopupWindow popupWindow,popupWindow1;
	private JSONArray chairmanSection1ListArray;
	ChairmanStudentWeeklyClassListAdapter chairmanStudentWeeklyClassListAdapter;
	ChairmanWeeklySectionListAdapter chairmanWeeklySectionListAdapter;
	private ListView chairmanSectionListView,chairmanClassListView;
	String sectionId;
	Date date = new Date();
	Calendar cal = Calendar.getInstance();
	Calendar cal1 = Calendar.getInstance();
	String fromDate,toDate;
	String totalClassStudents,totalSectionStudents;
	int width;
	
	public static ChairmanStudentWeeklyAttendance newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		ChairmanStudentWeeklyAttendance chairmanStudentWeeklyAttendance = new ChairmanStudentWeeklyAttendance();
		chairmanStudentWeeklyAttendance.setArguments(args);
		return chairmanStudentWeeklyAttendance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chairman_student_daily_attendance, container, false);
		cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();
        DisplayMetrics displaymetrics = new DisplayMetrics();
		((ChairmanSchoolAttendance) getContext()).getWindowManager()
        .getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
        cal1.add(Calendar.DATE, -1);
        Date toDate2 = cal1.getTime();
        toDate = new SimpleDateFormat("yyyy-MM-dd").format(toDate2);
        fromDate = new SimpleDateFormat("yyyy-MM-dd").format(todate1);
		mChart = (PieChart) view.findViewById(R.id.chart1);
		selectClass = (TextView) view.findViewById(R.id.text_ch_daily_class_wise);
		selectSection = (TextView) view.findViewById(R.id.text_ch_daily_section_wise);
		noOfStudents = (TextView) view.findViewById(R.id.text_no_of_students_ch_attendance);
		schoolWise = (TextView) view.findViewById(R.id.text_ch_daily_school_wise);
		malesP = (TextView) view.findViewById(R.id.text_no_of_males_ch_attendance);
		femalesP = (TextView) view.findViewById(R.id.text_no_of_females_ch_attendance);
		malesA = (TextView) view.findViewById(R.id.text_no_of_males_ch_attendance1);
		malesL = (TextView) view.findViewById(R.id.text_no_of_males_ch_attendance2);
		femalesA = (TextView) view.findViewById(R.id.text_no_of_females_ch_attendance1);
		femalesL = (TextView) view.findViewById(R.id.text_no_of_females_ch_attendance2);
		totalP = (TextView) view.findViewById(R.id.textTotalPresent);
		totalA = (TextView) view.findViewById(R.id.textTotalAbsent);
		totalL = (TextView) view.findViewById(R.id.textTotalLeave);
		
		initData();
		getStudentAnnouncementList();
        chairmanClassListView = new ListView(getActivity());

		
		popupWindow1 = ClassDailyListWindow();
		
		schoolWise.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectClass.setText("Select Class");
				selectSection.setText("Select Section");
				initData();
				getStudentAnnouncementList();
				
			}
		});
		
		selectClass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				popupWindow1.showAsDropDown(v, -5, 0);
				
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
					selectClass.setText(chairmanClass1ListArray.getJSONObject(position).getString("class_name"));
					classId = chairmanClass1ListArray.getJSONObject(position).getString("class_id");
					totalClassStudents = chairmanClass1ListArray.getJSONObject(position).getString("total_stu_cls");
					getStudentAnnouncementList1();
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});
		
		
		selectSection.setOnClickListener(new View.OnClickListener() {
			
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chairmanSectionListView = new ListView(getActivity());
				popupWindow = SectionDailyListWindow();
				popupWindow.showAsDropDown(v, -5, 0);
				chairmanSectionListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v, int position,
							long arg3) {
						// TODO Auto-generated method stub
						
						Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
						fadeInAnimation.setDuration(10);
						v.startAnimation(fadeInAnimation);

						popupWindow.dismiss();
						
						try {
							selectSection.setText(chairmanSection1ListArray.getJSONObject(position).getString("section_name"));
							sectionId = chairmanSection1ListArray.getJSONObject(position).getString("section_id");
							totalSectionStudents = chairmanSection1ListArray.getJSONObject(position).getString("total_stu_cls_sec");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						getStudentAnnouncementList2();
					}
				});
				
			}
		});
		
		

		
		return view;
	}
	
	public PopupWindow SectionDailyListWindow()
	{
		PopupWindow popupWindow = new PopupWindow(getActivity());


        initData1();
        getSectionList();
		popupWindow.setFocusable(true);
		popupWindow.setWidth(width);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


		popupWindow.setContentView(chairmanSectionListView);

		return popupWindow;
	}
	
	public PopupWindow ClassDailyListWindow()
	{
		PopupWindow popupWindow = new PopupWindow(getActivity());


         initData2();
         getClassList();
		popupWindow.setFocusable(true);
		popupWindow.setWidth(width);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


		popupWindow.setContentView(chairmanClassListView);

		return popupWindow;
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
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


		mChart.setUsePercentValues(false);
		mChart.setDescription("");
		mChart.setExtraOffsets(0,0,0,0);

		mChart.setDragDecelerationFrictionCoef(0.0f);


		//mChart.setCenterText(generateCenterSpannableText());
		tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

		mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));

		mChart.setDrawHoleEnabled(false);
		//mChart.setHoleColorTransparent(true);

		mChart.setTransparentCircleColor(Color.WHITE);
		mChart.setTransparentCircleAlpha(110);

		mChart.setHoleRadius(0f);
		mChart.setTransparentCircleRadius(0f);

		mChart.setDrawCenterText(true);

		mChart.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChart.setRotationEnabled(false);
		mChart.setHighlightPerTapEnabled(true);

		// mChart.setUnit(" �");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChart.setOnChartValueSelectedListener(this);

		// setData(3, 100);

		mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART);
		l.setXEntrySpace(50f);
		l.setYEntrySpace(20f);
		l.setYOffset(0f);


		ArrayList<String> v = new ArrayList<String>();
		v.add("Present");
		v.add("Absent");
		v.add("Leave");
		



		ArrayList<Entry> v1 = new ArrayList<Entry>();
		v1.add(new Entry((float)(a),0));
		v1.add(new Entry((float)(b),1));
		v1.add(new Entry((float)(c),2));



		ArrayList<Entry> yVals1 = new ArrayList<Entry>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		for (int i = 0; i < 2; i++) {
			yVals1.add(new Entry((float) (Math.random() * mult) + mult / 3, i));
		}

		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < 2; i++)
		{
			xVals.add(mParties[i % mParties.length]);
		}

		PieDataSet dataSet = new PieDataSet(v1, "      ");
		dataSet.setSliceSpace(0f);
		dataSet.setSelectionShift(5f);
		dataSet.setVisible(true);
		//dataSet.setLabel("kkkk");
		dataSet.setDrawValues(false);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);

		*/
/* for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);*//*


		// colors.add(ColorTemplate.getHoloBlue());



		dataSet.setColors(colors);
		//dataSet.setSelectionShift(0f);





		PieData data = new PieData(v, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.WHITE);
		data.setValueTypeface(tf);
		mChart.setData(data);



		// undo all highlights
		mChart.highlightValues(null);
		mChart.setDrawSliceText(false);

		mChart.invalidate();
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

	@Override
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
	}

	private void initData() 
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanStudentWeeklyAttendanceArray= null;
			}
			else
			{
				chairmanStudentWeeklyAttendanceArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanStudentWeeklyAttendanceArray!= null)
		{
			System.out.println("jjj");
		}
	}

	protected void getStudentAnnouncementList() 
	{
		a=0;
		b=0;
		c=0;
		d=0;
		e1=0;
		total =0;
		tP =0;
		tL =0;
		tA =0;
		mP1=0;
		mA1=0;
		mL1 =0;
		mP2=0;
		mL2=0;
		mA2=0;
		mP3=0;
		mA3=0;
		mL3=0;
		finalValue=0;
		finalValue1=0;
		finalValue2=0;
		finalValue3=0;
		finalValue4=0;
		finalValue5=0;

		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId;
		StringRequest requestObject = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				System.out.println(response);
				System.out.println(url);

				try 
				{
					responseObject = new JSONObject(response);

					toa();
					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
					{
						Utils.showToast(getActivity(),"No Records Found");
						System.out.println("hhhhh");
						loading.dismiss();
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
							loading.dismiss();
						}
						else
							if(responseObject.has("Attendance"))
							{
								chairmanStudentWeeklyAttendanceArray= new JSONObject(response).getJSONArray("Attendance");
								if(null!=chairmanStudentWeeklyAttendanceArray && chairmanStudentWeeklyAttendanceArray.length()>=0)
								{
									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
									e.data = chairmanStudentWeeklyAttendanceArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId,e);
									for(i = 0;i<chairmanStudentWeeklyAttendanceArray.length();i++)
									{
										total = total+Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(i).getString("count_attend").toString());
										//System.out.println(total);
										
									}
									System.out.println(i);
									String hh = Preferences.getInstance().totalStudents;
									//Utils.showToast(getActivity(),Preferences.getInstance().totalStudents);
									//System.out.println(hh);
									total1 = Integer.parseInt(Preferences.getInstance().totalStudents)*7;
									//Utils.showToast(getActivity(), ""+total1);
									noOfStudents.setText("Total Students:"+Preferences.getInstance().totalStudents);

									if(i == 1)
									{
										a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
										c=  (float)((total1-(total*7)));
									}

									else
										if(i == 2)
										{
											//a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
											//b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
											a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
											b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"));
											c=  (float)(((total1-(total*7))));
											//Utils.showToast(getActivity(), ""+c);
										}

										else
											if(i == 3)
											{
												loading.dismiss();
												a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												//c=  (Float.parseFloat(Preferences.getInstance().totalStudents)-total)/Integer.parseInt(Preferences.getInstance().totalStudents);
												c = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												mP1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("male"));
												mA1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("male"));
												mL1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("male"));
												fP1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("female"));
												fA1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("female"));
												fL1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("female"));
												tP = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
												tA = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"));
												tL = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("count_attend"));

												//System.out.println("jjj"+mP1+mA1+mL1);

												mP2 = (float)mP1/tP;
												mA2 = (float)mA1/tA;
												mL2 = (float)mL1/tL;

												mP3 = (float)mP2*100;
												mA3 = (float)mA2*100;
												mL3 = (float)mL2*100;

												fP2 = (float)fP1/tP;
												fA2 = (float)fA1/tA;
												fL2 = (float)fL1/tL;

												fP3 = (float)fP2*100;
												fA3 = (float)fA2*100;
												fL3 = (float)fL2*100;



												//System.out.println("jhg"+mP2+mA2+mL2);



												finalValue = Math.round(mP3);
												finalValue1 = Math.round(mA3);
												finalValue2 = Math.round(mL3);

												finalValue3 = Math.round(fP3);
												finalValue4 = Math.round(fA3);
												finalValue5 = Math.round(fL3);

												totalP.setText("Total(P): "+tP);
												totalA.setText("Total(A): "+tA);
												totalL.setText("Total(L): "+tL);

												malesP.setText("Boys(P): "+finalValue+"%");
												malesA.setText("Boys(A): "+finalValue1+"%");
												malesL.setText("Boys(L): "+finalValue2+"%");

												femalesP.setText("Females(P) "+finalValue3+"%");
												femalesA.setText("Females(A) "+finalValue4+"%");
												femalesL.setText("Females(L) "+finalValue5+"%");
											}




									openChart();






								}
							}
							else
								Utils.showToast(getActivity(), "Error Fetching Response");
					loading.dismiss();


				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
					loading.dismiss();

				}

				*/
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
   					}*//*





			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
					loading.dismiss();

				}
			})
		{
			*/
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
   			}*//*
};

   			requestObject.setRetryPolicy(new DefaultRetryPolicy(
   					25000, 
   					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
   					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
   			if(Utils.isNetworkAvailable(getActivity()))
   				queue.add(requestObject);
   			else
   			{
   				Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
				loading.dismiss();
   			}
	}	
	private void toa()
	{
		System.out.println("aaa");
	}
	
	private void initData1() 
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanSection1ListArray= null;
			}
			else
			{
				chairmanSection1ListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanSection1ListArray!= null)
		{
			chairmanWeeklySectionListAdapter= new ChairmanWeeklySectionListAdapter(getActivity(),chairmanSection1ListArray);
			chairmanSectionListView.setAdapter(chairmanWeeklySectionListAdapter);
			chairmanWeeklySectionListAdapter.notifyDataSetChanged();
		}
	}

	protected void getSectionList() 
	{

		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST*/
/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*
;
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
						Utils.showToast(getActivity(),"No Records Found");
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Sections"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanSection1ListArray= new JSONObject(response).getJSONArray("Sections");
								if(null!=chairmanSection1ListArray && chairmanSection1ListArray.length()>=0)
								{
									com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
									e.data = chairmanSection1ListArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_SECTION_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&cls_id="+classId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanSectionListView.invalidateViews();
									chairmanWeeklySectionListAdapter = new ChairmanWeeklySectionListAdapter(getActivity(), chairmanSection1ListArray);
									chairmanSectionListView.setAdapter(chairmanWeeklySectionListAdapter);
									chairmanWeeklySectionListAdapter.notifyDataSetChanged();	
								}
							}
							else
								Utils.showToast(getActivity(), "Error Fetching Response");


				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

				}
			})
		{
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(getActivity());
				Map<String,String> params = new HashMap<String, String>();
				params.put("sch_id",Preferences.getInstance().schoolId);
				//params.put("sec_id",Preferences.getInstance().studentSectionId);
				params.put("token",Preferences.getInstance().token);
				params.put("u_email_id",Preferences.getInstance().userEmailId);
				//params.put("stu_id",Preferences.getInstance().studentId);
				params.put("u_id",Preferences.getInstance().userId);
				params.put("cls_id", classId);
				params.put("device_id", Preferences.getInstance().deviceId);
				//params.put("sec_id", sectionId);
				//params.put("crr_date",currentDate);
				return params;
			}};		

			requestObject.setRetryPolicy(new DefaultRetryPolicy(
					25000, 
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			if(Utils.isNetworkAvailable(getActivity()))
				queue.add(requestObject);
			else
			{
				Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
			}
	}
	
	private void initData2() 
	{


		try
		{
			com.android.volley.Cache.Entry e;
			e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId);
			if(e == null)
			{
				chairmanClass1ListArray= null;
			}
			else
			{
				chairmanClass1ListArray= new JSONArray(new String(e.data));
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}

		if(chairmanClass1ListArray!= null)
		{
			chairmanStudentWeeklyClassListAdapter= new ChairmanStudentWeeklyClassListAdapter(getActivity(),chairmanClass1ListArray);
			chairmanClassListView.setAdapter(chairmanStudentWeeklyClassListAdapter);
			chairmanStudentWeeklyClassListAdapter.notifyDataSetChanged();
		}
	}

	protected void getClassList() 
	{

		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST*/
/*+"?u_email_id="+userEmailId+"&u_id="+userId+"&token="+token+"&student_ID=" +studentId*//*
;
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
						Utils.showToast(getActivity(),"No Records Found");
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
						}
						else
							if(responseObject.has("Classes"))
							{
								mChart.setVisibility(View.VISIBLE);
								chairmanClass1ListArray= new JSONObject(response).getJSONArray("Classes");
								if(null!=chairmanClass1ListArray && chairmanClass1ListArray.length()>=0)
								{
									com.android.volley.Cache.Entry e= new com.android.volley.Cache.Entry();
									e.data = chairmanClass1ListArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_CLASS_LIST+"?sch_id="+Preferences.getInstance().schoolId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&u_id="+Preferences.getInstance().userId+"&device_id="+Preferences.getInstance().deviceId,e);
									chairmanClassListView.invalidateViews();
									chairmanStudentWeeklyClassListAdapter = new ChairmanStudentWeeklyClassListAdapter(getActivity(), chairmanClass1ListArray);
									chairmanClassListView.setAdapter(chairmanStudentWeeklyClassListAdapter);
									chairmanStudentWeeklyClassListAdapter.notifyDataSetChanged();	
									
								}
							}
							else
								Utils.showToast(getActivity(), "Error Fetching Response");


				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

				}

			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

				}
			})
		{
			@Override
			protected Map<String,String> getParams(){

				Preferences.getInstance().loadPreference(getActivity());
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
			if(Utils.isNetworkAvailable(getActivity()))
				queue.add(requestObject);
			else
			{
				Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
			}
	}
	
	protected void getStudentAnnouncementList1() 
	{
		a=0;
		b=0;
		c=0;
		d=0;
		e1=0;
		total =0;
		tP =0;
		tL =0;
		tA =0;
		mP1=0;
		mA1=0;
		mL1 =0;
		mP2=0;
		mL2=0;
		mA2=0;
		mP3=0;
		mA3=0;
		mL3=0;
		finalValue=0;
		finalValue1=0;
		finalValue2=0;
		finalValue3=0;
		finalValue4=0;
		finalValue5=0;

		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE_CLASS_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&cls_id="+classId+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId;
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
						Utils.showToast(getActivity(),"No Records Found");
						System.out.println("hhhhh");
						loading.dismiss();
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
							loading.dismiss();
						}
						else
							if(responseObject.has("Attendance_cls_week"))
							{
								chairmanStudentWeeklyAttendanceArray= new JSONObject(response).getJSONArray("Attendance_cls_week");
								if(null!=chairmanStudentWeeklyAttendanceArray && chairmanStudentWeeklyAttendanceArray.length()>=0)
								{
									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
									e.data = chairmanStudentWeeklyAttendanceArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE_CLASS_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&frm_date="+fromDate+"&cls_id="+classId+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId,e);
									for(i = 0;i<chairmanStudentWeeklyAttendanceArray.length();i++)
									{
										total = total+Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(i).getString("count_attend").toString());
										//System.out.println(total);
										
									}
									
									//total1 = Integer.parseInt(totalClassStudents)*Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("days"));
									
									System.out.println(i);
									noOfStudents.setText("Total Students:"+totalClassStudents);

									if(i == 1)
									{
										a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
										c=  (float)(total1-(total));
									}

									else
										if(i == 2)
										{
											a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
											b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"));
											
											c=  (float)(total1-(total));
										}

										else
											if(i == 3)
											{
												loading.dismiss();
												a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												//c=  (Float.parseFloat(Preferences.getInstance().totalStudents)-total)/Integer.parseInt(Preferences.getInstance().totalStudents);
												c = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												mP1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("male"));
												mA1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("male"));
												mL1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("male"));
												fP1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("female"));
												fA1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("female"));
												fL1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("female"));
												tP = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
												tA = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"));
												tL = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("count_attend"));

												//System.out.println("jjj"+mP1+mA1+mL1);

												mP2 = (float)mP1/tP;
												mA2 = (float)mA1/tA;
												mL2 = (float)mL1/tL;

												mP3 = (float)mP2*100;
												mA3 = (float)mA2*100;
												mL3 = (float)mL2*100;

												fP2 = (float)fP1/tP;
												fA2 = (float)fA1/tA;
												fL2 = (float)fL1/tL;

												fP3 = (float)fP2*100;
												fA3 = (float)fA2*100;
												fL3 = (float)fL2*100;



												//System.out.println("jhg"+mP2+mA2+mL2);



												finalValue = Math.round(mP3);
												finalValue1 = Math.round(mA3);
												finalValue2 = Math.round(mL3);

												finalValue3 = Math.round(fP3);
												finalValue4 = Math.round(fA3);
												finalValue5 = Math.round(fL3);

												totalP.setText("Total(P): "+tP);
												totalA.setText("Total(A): "+tA);
												totalL.setText("Total(L): "+tL);

												malesP.setText("Boys(P): "+finalValue+"%");
												malesA.setText("Boys(A): "+finalValue1+"%");
												malesL.setText("Boys(L): "+finalValue2+"%");

												femalesP.setText("Females(P) "+finalValue3+"%");
												femalesA.setText("Females(A) "+finalValue4+"%");
												femalesL.setText("Females(L) "+finalValue5+"%");
											}



									openChart();






								}
							}
							else
								Utils.showToast(getActivity(), "Error Fetching Response");
					loading.dismiss();


				}
				catch(JSONException e)
				{
					e.printStackTrace();
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
					loading.dismiss();

				}

				*/
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
   					}*//*





			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
					loading.dismiss();

				}
			})
		{
			*/
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
   			}*//*
};

   			requestObject.setRetryPolicy(new DefaultRetryPolicy(
   					25000, 
   					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
   					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
   			if(Utils.isNetworkAvailable(getActivity()))
   				queue.add(requestObject);
   			else
   			{
   				Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
				loading.dismiss();
   			}
	}	

	
	protected void getStudentAnnouncementList2() 
	{
		a=0;
		b=0;
		c=0;
		d=0;
		e1=0;
		total =0;
		tP =0;
		tL =0;
		tA =0;
		mP1=0;
		mA1=0;
		mL1 =0;
		mP2=0;
		mL2=0;
		mA2=0;
		mP3=0;
		mA3=0;
		mL3=0;
		finalValue=0;
		finalValue1=0;
		finalValue2=0;
		finalValue3=0;
		finalValue4=0;
		finalValue5=0;

		RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
		final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", false, false);
		final String url =AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE_SECTION_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&cls_id="+classId+"&sec_id="+sectionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId;
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
						Utils.showToast(getActivity(),"No Records Found");
						System.out.println("hhhhh");
						loading.dismiss();
					}
					else
						if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
						{
							Utils.showToast(getActivity(), "Session Expired:Please Login Again");
							loading.dismiss();
						}
						else
							if(responseObject.has("Attendance_cls_sec_week"))
							{
								chairmanStudentWeeklyAttendanceArray= new JSONObject(response).getJSONArray("Attendance_cls_sec_week");
								if(null!=chairmanStudentWeeklyAttendanceArray && chairmanStudentWeeklyAttendanceArray.length()>=0)
								{
									com.android.volley.Cache.Entry e = new com.android.volley.Cache.Entry();
									e.data = chairmanStudentWeeklyAttendanceArray.toString().getBytes();
									VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_WEEKLY_ATTENDANCE_SECTION_WISE+"?u_id="+Preferences.getInstance().userId+"&u_email_id="+Preferences.getInstance().userEmailId+"&token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&inst_id="+Preferences.getInstance().institutionId+"&cls_id="+classId+"&sec_id="+sectionId+"&frm_date="+fromDate+"&to_date="+toDate+"&device_id="+Preferences.getInstance().deviceId,e);
									for(i = 0;i<chairmanStudentWeeklyAttendanceArray.length();i++)
									{
										total = total+Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(i).getString("count_attend").toString());
										//System.out.println(total);
										
									}
									
									//total1 = Integer.parseInt(totalSectionStudents)*Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("days"));
									
									System.out.println(i);
									noOfStudents.setText("Total Students:"+totalSectionStudents);

									if(i == 1)
									{
										a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
										
										
										c=  (float)((total1)-total);
									}

									else
										if(i == 2)
										{
											a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
											b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"));
											
											c=  (float)((total1)-total);
										}

										else
											if(i == 3)
											{
												loading.dismiss();
												a = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												b = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												//c=  (Float.parseFloat(Preferences.getInstance().totalStudents)-total)/Integer.parseInt(Preferences.getInstance().totalStudents);
												c = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("count_attend"))/Integer.parseInt(Preferences.getInstance().totalStudents);
												mP1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("male"));
												mA1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("male"));
												mL1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("male"));
												fP1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("female"));
												fA1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("female"));
												fL1 = Float.parseFloat(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("female"));
												tP = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(0).getString("count_attend"));
												tA = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(1).getString("count_attend"));
												tL = Integer.parseInt(chairmanStudentWeeklyAttendanceArray.getJSONObject(2).getString("count_attend"));

												//System.out.println("jjj"+mP1+mA1+mL1);

												mP2 = (float)mP1/tP;
												mA2 = (float)mA1/tA;
												mL2 = (float)mL1/tL;

												mP3 = (float)mP2*100;
												mA3 = (float)mA2*100;
												mL3 = (float)mL2*100;

												fP2 = (float)fP1/tP;
												fA2 = (float)fA1/tA;
												fL2 = (float)fL1/tL;

												fP3 = (float)fP2*100;
												fA3 = (float)fA2*100;
												fL3 = (float)fL2*100;



												//System.out.println("jhg"+mP2+mA2+mL2);



												finalValue = Math.round(mP3);
												finalValue1 = Math.round(mA3);
												finalValue2 = Math.round(mL3);

												finalValue3 = Math.round(fP3);
												finalValue4 = Math.round(fA3);
												finalValue5 = Math.round(fL3);

												totalP.setText("Total(P): "+tP);
												totalA.setText("Total(A): "+tA);
												totalL.setText("Total(L): "+tL);

												malesP.setText("Boys(P): "+finalValue+"%");
												malesA.setText("Boys(A): "+finalValue1+"%");
												malesL.setText("Boys(L): "+finalValue2+"%");

												femalesP.setText("Females(P) "+finalValue3+"%");
												femalesA.setText("Females(A) "+finalValue4+"%");
												femalesL.setText("Females(L) "+finalValue5+"%");
											}



									openChart();






								}
							}
							else
								Utils.showToast(getActivity(), "Error Fetching Response");
					loading.dismiss();


				}
				catch(JSONException e)
				{
					e.printStackTrace();
					//Utils.showToast(getActivity(), "");
					loading.dismiss();

				}

				*/
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
   					}*//*





			}}, new Response.ErrorListener() 
			{
				@Override
				public void onErrorResponse(VolleyError error) 
				{
					Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");
					loading.dismiss();

				}
			})
		{
			*/
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
   			}*//*
};

   			requestObject.setRetryPolicy(new DefaultRetryPolicy(
   					25000, 
   					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
   					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
   			if(Utils.isNetworkAvailable(getActivity()))
   				queue.add(requestObject);
   			else
   			{
   				Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
				loading.dismiss();
   			}
	}	



}
*/
