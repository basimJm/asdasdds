package com.schoofi.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.ChairmanAttendanceDetailsListViewAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import datepick.CalendarNumbersView;
import datepick.CalendarPickerView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

/**
 * Created by Schoofi on 04-08-2016.
 */
public class ChairmanStudentAttendanceDetailsNotRecordedAttendance extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    ListView recordedListView;
    private JSONArray recordedListArray;
    SwipyRefreshLayout swipyRefreshLayout;
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    TextView calender;
    private PopupWindow calendarPopup;
    int year_x,month_x,day_x;
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
    String from1,to1;
    ChairmanAttendanceDetailsListViewAdapter chairmanAttendanceDetailsListViewAdapter;
    HorizontalCalendar horizontalCalendar;
    Date dat;
    View view;

    private int mPage;

    public static ChairmanStudentAttendanceDetailsNotRecordedAttendance newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ChairmanStudentAttendanceDetailsNotRecordedAttendance chairmanStudentAttendanceDetailsNotRecordedAttendance = new ChairmanStudentAttendanceDetailsNotRecordedAttendance();
        chairmanStudentAttendanceDetailsNotRecordedAttendance.setArguments(args);
        return chairmanStudentAttendanceDetailsNotRecordedAttendance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chairman_attendance_details_fragment_layout, container, false);
        recordedListView = (ListView) view.findViewById(R.id.listView_teacher_feedback);
        swipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        calender = (TextView) view.findViewById(R.id.text_calender);
        calender.setOnClickListener(onEditTextClickListener);


        initData();
        getStudentFeedList();
        return view;
    }

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

            calender.setText(formatter.format(selectedDate.getTime()));
            //toEditTextDate.setText(formatter.format(selectedDate.getTime()));

            //date = from1;
            try {
                Cache.Entry e;
                e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&value=" + "2" + "&date=" +from1);
                if (e == null) {
                    recordedListArray = null;
                } else {
                    recordedListArray = new JSONArray(new String(e.data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (recordedListArray != null) {
                chairmanAttendanceDetailsListViewAdapter = new ChairmanAttendanceDetailsListViewAdapter(getActivity(), recordedListArray);
                recordedListView.setAdapter(chairmanAttendanceDetailsListViewAdapter);
                chairmanAttendanceDetailsListViewAdapter.notifyDataSetChanged();
            }
            RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
            final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&value=" + "2" + "&date=" + from1;
            StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject responseObject;
                    //System.out.println(response);
                    //System.out.println(url);
                    try {
                        responseObject = new JSONObject(response);
                        toa();
                        if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))

                            Utils.showToast(getActivity(), "No Records Found");
                        else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                            Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                        } else if (responseObject.has("Attendance")) {
                            recordedListArray = new JSONObject(response).getJSONArray("Attendance");
                            if (null != recordedListArray && recordedListArray.length() >= 0) {
                                Cache.Entry e = new Cache.Entry();
                                e.data = recordedListArray.toString().getBytes();
                                VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&value=" + "2" + "&date=" +from1, e);
                                recordedListView.invalidateViews();
                                chairmanAttendanceDetailsListViewAdapter = new ChairmanAttendanceDetailsListViewAdapter(getActivity(), recordedListArray);
                                recordedListView.setAdapter(chairmanAttendanceDetailsListViewAdapter);
                                chairmanAttendanceDetailsListViewAdapter.notifyDataSetChanged();
                                swipyRefreshLayout.setRefreshing(false);

                            }
                        } else
                            Utils.showToast(getActivity(), "Error Fetching Response");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

                }
            }) {
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
			}*/
            };

            requestObject.setRetryPolicy(new DefaultRetryPolicy(
                    25000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (Utils.isNetworkAvailable(getActivity()))
                queue.add(requestObject);
            else {
                Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
            }



        }
    };

    private void initData()
    {


        try
        {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS+"?token="+ Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+ "&date=" + date);
            if(e == null)
            {
                recordedListArray= null;
            }
            else
            {
                recordedListArray= new JSONArray(new String(e.data));
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        if(recordedListArray!= null)
        {
            chairmanAttendanceDetailsListViewAdapter= new ChairmanAttendanceDetailsListViewAdapter(getActivity(),recordedListArray);
            recordedListView.setAdapter(chairmanAttendanceDetailsListViewAdapter);
            chairmanAttendanceDetailsListViewAdapter.notifyDataSetChanged();
        }
    }

    protected void getStudentFeedList()
    {

        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+ "&date=" + date;
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
                        Utils.showToast(getActivity(),"No Records Found");
                    else
                    if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                    {
                        Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                    }
                    else
                    if(responseObject.has("Attendance"))
                    {
                        recordedListArray= new JSONObject(response).getJSONArray("Attendance");
                        if(null!=recordedListArray && recordedListArray.length()>=0)
                        {
                            Cache.Entry e = new Cache.Entry();
                            e.data = recordedListArray.toString().getBytes();
                            VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS+"?token="+Preferences.getInstance().token+"&sch_id="+Preferences.getInstance().schoolId+"&device_id="+Preferences.getInstance().deviceId+"&ins_id="+Preferences.getInstance().institutionId+"&value="+"2"+ "&date=" + date,e);
                            recordedListView.invalidateViews();
                            chairmanAttendanceDetailsListViewAdapter= new ChairmanAttendanceDetailsListViewAdapter(getActivity(),recordedListArray);
                            recordedListView.setAdapter(chairmanAttendanceDetailsListViewAdapter);
                            chairmanAttendanceDetailsListViewAdapter.notifyDataSetChanged();
                            swipyRefreshLayout.setRefreshing(false);

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
        if(Utils.isNetworkAvailable(getActivity()))
            queue.add(requestObject);
        else
        {
            Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
        }
    }
    private void toa()
    {
        System.out.println("aaa");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCalender();
    }


    private void initCalender() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 0);

        /** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -12);



        horizontalCalendar = new HorizontalCalendar.Builder(this.getActivity(), R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .textColor(Color.LTGRAY, Color.WHITE)
                .selectedDateBackground(Color.TRANSPARENT)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {

                from1= formatter1.format(date);

                try {
                    Cache.Entry e;
                    e = VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&value=" + "2" + "&date=" +from1);
                    if (e == null) {
                        recordedListArray = null;
                    } else {
                        recordedListArray = new JSONArray(new String(e.data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (recordedListArray != null) {
                    chairmanAttendanceDetailsListViewAdapter = new ChairmanAttendanceDetailsListViewAdapter(getActivity(), recordedListArray);
                    recordedListView.setAdapter(chairmanAttendanceDetailsListViewAdapter);
                    chairmanAttendanceDetailsListViewAdapter.notifyDataSetChanged();
                }
                RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
                final String url = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&value=" + "2" + "&date=" + from1;
                StringRequest requestObject = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseObject;
                        //System.out.println(response);
                        //System.out.println(url);
                        try {
                            responseObject = new JSONObject(response);
                            toa();
                            if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0")) {
                                Utils.showToast(getActivity(), "No Records Found");
                                recordedListView.setVisibility(View.INVISIBLE);
                            }
                            else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                                Utils.showToast(getActivity(), "Session Expired:Please Login Again");
                                recordedListView.setVisibility(View.INVISIBLE);
                            } else if (responseObject.has("Attendance")) {
                                recordedListArray = new JSONObject(response).getJSONArray("Attendance");
                                if (null != recordedListArray && recordedListArray.length() >= 0) {
                                    Cache.Entry e = new Cache.Entry();
                                    e.data = recordedListArray.toString().getBytes();
                                    recordedListView.setVisibility(View.VISIBLE);
                                    VolleySingleton.getInstance(getActivity()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHAIRMAN_STUDENT_ATTENDANCE_ANALYSIS + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&ins_id=" + Preferences.getInstance().institutionId + "&value=" + "2" + "&date=" +from1, e);
                                    recordedListView.invalidateViews();
                                    chairmanAttendanceDetailsListViewAdapter = new ChairmanAttendanceDetailsListViewAdapter(getActivity(), recordedListArray);
                                    recordedListView.setAdapter(chairmanAttendanceDetailsListViewAdapter);
                                    chairmanAttendanceDetailsListViewAdapter.notifyDataSetChanged();
                                    swipyRefreshLayout.setRefreshing(false);

                                }
                            } else
                                Utils.showToast(getActivity(), "Error Fetching Response");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showToast(getActivity(), "Error fetching modules! Please try after sometime.");

                    }
                }) {
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
			}
             */   };

                requestObject.setRetryPolicy(new DefaultRetryPolicy(
                        25000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                if (Utils.isNetworkAvailable(getActivity()))
                    queue.add(requestObject);
                else {
                    Utils.showToast(getActivity(), "Unable to fetch data, kindly enable internet settings!");
                }

            }
        });
    }
}
