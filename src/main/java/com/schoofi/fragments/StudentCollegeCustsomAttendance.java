package com.schoofi.fragments;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.StudentCollegeAttendanceAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;

/**
 * Created by Schoofi on 10-05-2019.
 */

public class StudentCollegeCustsomAttendance extends Fragment {

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

    ListView studentCustomAttendanceReplaceListView;
    public JSONArray studentCustomAttendanceArray;
    private Context context;
    StudentCollegeAttendanceAdapter studentCustomAttendanceAdapter;
    SwipyRefreshLayout swipyRefreshLayout;

    //private DatePickerDialog fromDatePickerDialog,toDatePickerDialog;

    //private SimpleDateFormat dateFormatter;
    public static final String ARG_PAGE = "ARG_PAGE";
    String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());



    private int mPage;

    public static StudentCollegeCustsomAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        StudentCollegeCustsomAttendance studentCollegeCustsomAttendance= new StudentCollegeCustsomAttendance();
        studentCollegeCustsomAttendance.setArguments(args);
        return studentCollegeCustsomAttendance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_college_custom_attendance_layout, container, false);
        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromEditTextDate = (EditText) view.findViewById(R.id.edit_fromDatePicker);
        toEditTextDate = (EditText) view.findViewById(R.id.edit_toDatePicker);
        studentCustomAttendanceReplaceListView = (ListView) view.findViewById(R.id.student_custom_attendance_replace_listview);
        context = getActivity();
        newView = (TextView) view.findViewById(R.id.newView1);
        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                initData();
                getStudentCustomAttendance();

            }
        });


		/*if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
		    // Do something for lollipop and above versions
			fromEditTextDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(dialog_id);

				}
			});


		toEditTextDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(dialog_id);

			}
		});
	}*/

        //else{
        // do something for phones running an SDK before lollipop
        fromEditTextDate.setOnClickListener(onEditTextClickListener);
        toEditTextDate.setOnClickListener(onEditTextClickListener1);
        //}

        //fromEditTextDate.setOnTouchListener(otl);
        fromEditTextDate.setInputType(0);
        toEditTextDate.setInputType(0);
        button_student_custom_date_picker = (Button) view.findViewById(R.id.btn_student_custom_date_picker);
        button_student_custom_date_picker.setText("Done");
        button_student_custom_date_picker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(count==0 && count1==0)
                    Toast.makeText(getActivity(), "not valid date", Toast.LENGTH_SHORT).show();
                else
                if(count==0 && count1==1)
                {
                    Toast.makeText(getActivity(), "not valid date", Toast.LENGTH_SHORT).show();
                }
                else
                if(count==1 && count1==0)
                {
                    Toast.makeText(getActivity(), "not valid date", Toast.LENGTH_SHORT).show();
                }
                else
                //view = inflater.inflate(R.layout.student_daily_attendance, container, false);
                {
                    initData();
                    getStudentCustomAttendance();
                }
            }
        });



        //setDateTimeField();
        System.out.println(date);
        return view;
    }

	/*@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
		getStudentCustomAttendance();
	}*/

    private View.OnClickListener onEditTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendarPopup == null) {
                calendarPopup = new PopupWindow(getActivity());
                CalendarPickerView calendarView = new CalendarPickerView(getActivity());
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
                calendarPopup1 = new PopupWindow(getActivity());
                CalendarPickerView calendarView = new CalendarPickerView(getActivity());
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
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1 =0;
                }
                else
                if(date2.compareTo(date3)<0)
                {
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
                    count1=0;
                }

                else
                if(date2.compareTo(date3)==0)
                {
                    Toast.makeText(getActivity(),"Not Valid",Toast.LENGTH_SHORT).show();
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

    private void initData()
    {
        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_CUSTOM_ATTENDANCE+"?sch_id="+ Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&stu_id="+Preferences.getInstance().studentId+"&u_id="+Preferences.getInstance().userId+"&startingDate="+from1+"&endingDate="+to1+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&subject_id="+Preferences.getInstance().studentSubjectId);
            if(e == null)
            {
                studentCustomAttendanceArray = null;
            }
            else
            {
                studentCustomAttendanceArray = new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(studentCustomAttendanceArray!= null)
        {
            studentCustomAttendanceAdapter= new StudentCollegeAttendanceAdapter(getActivity(),studentCustomAttendanceArray);
            studentCustomAttendanceReplaceListView.setAdapter(studentCustomAttendanceAdapter);
            studentCustomAttendanceAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentCustomAttendance()
    {
        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_CUSTOM_ATTENDANCE;
        StringRequest requestObject = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    toa();
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                    {
                        newView.setVisibility(View.VISIBLE);
                        studentCustomAttendanceReplaceListView.setVisibility(View.INVISIBLE);
                    }
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {

                        Utils.showToast(context, "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Test_Variable"))
                    {
                        newView.setVisibility(View.INVISIBLE);
                        studentCustomAttendanceReplaceListView.setVisibility(View.VISIBLE);
                        studentCustomAttendanceArray= new JSONObject(response).getJSONArray("Test_Variable");
                        if(null!=studentCustomAttendanceArray && studentCustomAttendanceArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = studentCustomAttendanceArray.toString().getBytes();
                            VolleySingleton.getInstance(context).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_COLLEGE_CUSTOM_ATTENDANCE+"?sch_id="+Preferences.getInstance().schoolId+"&sec_id="+Preferences.getInstance().studentSectionId+"&token="+Preferences.getInstance().token+"&u_email_id="+Preferences.getInstance().userEmailId+"&stu_id="+Preferences.getInstance().studentId+"&u_id="+Preferences.getInstance().userId+"&startingDate="+from1+"&endingDate="+to1+"&device_id="+Preferences.getInstance().deviceId+"&cls_id="+Preferences.getInstance().studentClassId+"&subject_id="+Preferences.getInstance().studentSubjectId,e);
                            studentCustomAttendanceReplaceListView.invalidateViews();
                            studentCustomAttendanceAdapter = new StudentCollegeAttendanceAdapter(context, studentCustomAttendanceArray);
                            studentCustomAttendanceReplaceListView.setAdapter(studentCustomAttendanceAdapter);
                            studentCustomAttendanceAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                        Utils.showToast(context, "Error Fetching Response");
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(context, "Error fetching modules! Please try after sometime.");
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("sch_id",Preferences.getInstance().schoolId);
                params.put("sec_id",Preferences.getInstance().studentSectionId);
                params.put("token",Preferences.getInstance().token);
                params.put("u_email_id",Preferences.getInstance().userEmailId);
                params.put("stu_id",Preferences.getInstance().studentId);
                params.put("u_id",Preferences.getInstance().userId);
                params.put("startingDate",from1);
                params.put("endingDate", to1);
                params.put("device_id", Preferences.getInstance().deviceId);
                params.put("cls_id", Preferences.getInstance().studentClassId);
                params.put("subject_id",Preferences.getInstance().studentSubjectId);
                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(context))
            queue.add(requestObject);
        else
        {
            Utils.showToast(context, "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }
}
